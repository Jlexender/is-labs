package org.example.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.example.entity.Dummy;
import org.example.service.DummyService;
import org.example.ws.WebSocket;

@Path("/dummy")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DummyResource {
    @EJB
    private DummyService dummyService;

    @POST
    @Path("/simple")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createDummyByName(String name) {
        Dummy dummy = dummyService.save(name);
        WebSocket.dummyCreated(dummy);
        return Response.ok(dummy).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDummy(Dummy dummy) {
        Dummy createdDummy = dummyService.save(dummy);
        WebSocket.dummyCreated(createdDummy);
        return Response.ok(createdDummy).build();
    }

    @GET
    @Path("/all")
    public Response getAllDummies() {
        return Response.ok(dummyService.findAll()).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countDummies() {
        long count = dummyService.count();
        return Response.ok(count).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDummy(@PathParam("id") Long id) {
        Dummy existing = dummyService.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        dummyService.deleteById(id);
        WebSocket.dummyDeleted(id);
        return Response.noContent().build();
    }
}