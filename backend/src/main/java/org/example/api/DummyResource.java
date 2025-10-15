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

}