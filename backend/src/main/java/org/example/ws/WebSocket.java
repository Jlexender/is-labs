package org.example.ws;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.example.entity.Dummy;

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

    public static void dummyCreated(Dummy dummy) {
        try {
            String message = """
                    {
                        "type": "DUMMY_CREATED",
                        "data": %s
                    }
                    """.formatted(objectMapper.writeValueAsString(dummy));
            broadcast(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void dummyDeleted(Long id) {
        String message = """
                    {
                        "type": "DUMMY_DELETED",
                        "data": %s
                    }
                    """.formatted(id);
        broadcast(message);
    }

    public static void broadcast(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }
}
