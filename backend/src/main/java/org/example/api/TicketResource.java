package org.example.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;

import org.example.entity.ImportHistory;
import org.example.entity.Ticket;
import org.example.service.ImportService;
import org.example.service.TicketService;
import org.example.ws.WebSocket;

import java.util.List;

@Path("/ticket")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TicketResource {
    @EJB
    private TicketService ticketService;
    
    @EJB
    private ImportService importService;

    @POST
    @Path("/create")
    public Response createTicket(@Valid Ticket ticket) {
        Ticket created = ticketService.save(ticket);
        WebSocket.ticketCreated(created);
        return Response.ok(created).build();
    }

    @GET
    @Path("/all")
    public Response getAllTickets() {
        return Response.ok(ticketService.findAll()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTicket(@PathParam("id") Long id) {
        Ticket existing = ticketService.findById(id);
        if (existing == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        ticketService.deleteById(id);
        WebSocket.ticketDeleted(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTicket(@PathParam("id") Long id, @Valid Ticket ticket) {
        Ticket updated = ticketService.update(id, ticket);
        if (updated == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        WebSocket.ticketUpdated(updated);
        return Response.ok(updated).build();
    }

    // Special operations

    @GET
    @Path("/min-number")
    public Response getTicketWithMinNumber() {
        Ticket ticket = ticketService.findMinByNumber();
        if (ticket == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ticket).build();
    }

    @GET
    @Path("/count-less-than/{number}")
    public Response countTicketsWithNumberLessThan(@PathParam("number") long number) {
        long count = ticketService.countByNumberLessThan(number);
        return Response.ok(count).build();
    }

    @GET
    @Path("/by-comment-prefix")
    public Response getTicketsByCommentPrefix(@QueryParam("prefix") String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Query parameter 'prefix' is required").build();
        }
        return Response.ok(ticketService.findByCommentStartsWith(prefix)).build();
    }

    @POST
    @Path("/sell/{ticketId}")
    public Response sellTicket(
            @PathParam("ticketId") Long ticketId,
            @QueryParam("price") float price,
            org.example.entity.Person person) {
        try {
            Ticket sold = ticketService.sellTicket(ticketId, price, person);
            WebSocket.ticketUpdated(sold);
            return Response.ok(sold).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage()).build();
        }
    }
    
    // Import operations
    
    @POST
    @Path("/import")
    public Response importTickets(
            @QueryParam("userId") String userId,
            List<Ticket> tickets) {
        try {
            // Validate userId
            if (userId == null || userId.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("User ID is required").build();
            }
            
            // Validate tickets list
            if (tickets == null || tickets.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Ticket list cannot be empty").build();
            }
            
            ImportHistory history = importService.importTickets(tickets, userId);
            
            // Check if import failed
            if ("FAILED".equals(history.getStatus())) {
                // Return the error message from history
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(history.getErrorMessage()).build();
            }
            
            // Only notify WebSocket if import was successful
            if ("SUCCESS".equals(history.getStatus())) {
                // Reload tickets from DB to get their IDs for WebSocket notification
                List<Ticket> allTickets = ticketService.findAll();
                // Notify only for the last N tickets (where N = imported count)
                int startIdx = Math.max(0, allTickets.size() - tickets.size());
                for (int i = startIdx; i < allTickets.size(); i++) {
                    WebSocket.ticketCreated(allTickets.get(i));
                }
            }
            
            return Response.ok(history).build();
        } catch (jakarta.ejb.EJBException e) {
            // Handle EJB exceptions (unwrap the cause)
            Throwable cause = e.getCause();
            String message = cause != null ? cause.getMessage() : e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message != null ? message : "Import failed").build();
        } catch (Exception e) {
            // Catch any other unexpected errors (prevents 500)
            String message = e.getMessage();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message != null ? message : "Import failed").build();
        }
    }
    
    @GET
    @Path("/import-history")
    public Response getImportHistory(
            @QueryParam("userId") String userId,
            @QueryParam("isAdmin") @DefaultValue("false") boolean isAdmin) {
        List<ImportHistory> history = importService.getImportHistory(userId, isAdmin);
        return Response.ok(history).build();
    }
}

