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
        if (existing == null) return Response.status(Response.Status.NOT_FOUND).build();
        ticketService.deleteById(id);
        WebSocket.ticketDeleted(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    public Response updateTicket(@PathParam("id") Long id, @Valid Ticket ticket) {
        Ticket updated = ticketService.update(id, ticket);
        if (updated == null) return Response.status(Response.Status.NOT_FOUND).build();
        WebSocket.ticketUpdated(updated);
        return Response.ok(updated).build();
    }
}

