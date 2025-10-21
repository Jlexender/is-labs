package org.example.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;

import org.example.entity.Ticket;
import org.example.service.TicketService;
import org.example.ws.WebSocket;

@Path("/ticket")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TicketResource {
    @EJB
    private TicketService ticketService;

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
}
