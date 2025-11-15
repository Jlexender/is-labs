package org.example.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception mapper to handle JSON deserialization errors
 * and prevent 500 Internal Server Error responses
 */
@Provider
public class JsonExceptionMapper implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        // Handle JSON parsing errors
        if (exception instanceof JsonParseException) {
            JsonParseException jpe = (JsonParseException) exception;
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid JSON syntax: " + jpe.getOriginalMessage())
                    .build();
        }
        
        // Handle JSON mapping errors (type mismatches, missing fields, etc.)
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
        
        // For all other exceptions, return null to let default handling occur
        return null;
    }
}
