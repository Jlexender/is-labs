package org.example.ws;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws")
public class WebSocket {
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }


    public static void ticketCreated(org.example.entity.Ticket ticket) {
        try {
            String message = """
                    {
                        "type": "TICKET_CREATED",
                        "data": %s
                    }
                    """.formatted(objectMapper.writeValueAsString(ticket));
            broadcast(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void ticketDeleted(Long id) {
        String message = """
                    {
                        "type": "TICKET_DELETED",
                        "data": %s
                    }
                    """.formatted(id);
        broadcast(message);
    }

    public static void ticketUpdated(org.example.entity.Ticket ticket) {
        try {
            String message = """
                    {
                        "type": "TICKET_UPDATED",
                        "data": %s
                    }
                    """.formatted(objectMapper.writeValueAsString(ticket));
            broadcast(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }
}
