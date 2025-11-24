package org.example.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonExceptionMapper implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof JsonParseException) {
            JsonParseException jpe = (JsonParseException) exception;
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid JSON syntax: " + jpe.getOriginalMessage())
                    .build();
        }
        
        if (exception instanceof JsonMappingException) {
            JsonMappingException jme = (JsonMappingException) exception;
            String message = "Unable to deserialize JSON data";
            if (jme.getPath() != null && !jme.getPath().isEmpty()) {
                message += " at path: " + jme.getPathReference();
            }
            message += ". " + jme.getOriginalMessage();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(message)
                    .build();
        }
        
        return null;
    }
}
