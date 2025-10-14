package org.example.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.domain.Dummy;
import org.example.service.DummyService;

import java.util.List;

@Path("/dummy")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DummyResource {

    @EJB
    private DummyService dummyService;

    @POST
    public Response createDummy(Dummy dummy) {
        try {
            Dummy savedDummy = dummyService.save(dummy);
            return Response.status(Response.Status.CREATED).entity(savedDummy).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error saving dummy: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/simple")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createSimpleDummy(String name) {
        try {
            Dummy savedDummy = dummyService.save(name);
            return Response.status(Response.Status.CREATED).entity(savedDummy).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error saving dummy: " + e.getMessage()).build();
        }
    }

    @GET
    public List<Dummy> getAllDummies() {
        return dummyService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getDummyById(@PathParam("id") Long id) {
        Dummy dummy = dummyService.findById(id);
        if (dummy != null) {
            return Response.ok(dummy).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCount() {
        long count = dummyService.count();
        return Response.ok(String.valueOf(count)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDummy(@PathParam("id") Long id) {
        try {
            dummyService.deleteById(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting dummy: " + e.getMessage()).build();
        }
    }
}