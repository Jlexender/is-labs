package org.example.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.example.dto.DummyDto;
import org.example.entity.Dummy;
import org.example.mapper.DummyMapper;
import org.example.service.DummyService;

import java.util.List;

@Path("/dummy")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DummyResource {
    @EJB
    private DummyService dummyService;

    private DummyMapper dummyMapper = DummyMapper.INSTANCE;

    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String healthCheck() {
        return "OK";
    }

    @POST
    @Path("/simple")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createDummyByName(String name) {
        try {
            Dummy dummy = dummyService.save(name);
            return Response.status(Response.Status.CREATED).entity(dummyMapper.toDto(dummy)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/schema")
    public Response getJsonSchema() {
        return Response.ok(DummyDto.getJsonFieldSchema()).build();
    }

    @GET
    @Path("/all")
    public Response getAllDummies() {
        List<Dummy> dummies = dummyService.findAll();
        List<DummyDto> dummyDtos = dummies.stream()
                .map(dummyMapper::toDto)
                .toList();
        return Response.ok(dummyDtos).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response countDummies() {
        long count = dummyService.count();
        return Response.ok(count).build();
    }
    
}