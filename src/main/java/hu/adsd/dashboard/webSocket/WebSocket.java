package hu.adsd.dashboard.webSocket;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/websocket")
@Component
public class WebSocket {

    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        synchronized(clients){
            // Iterate over the connected sessions
            // and broadcast the received message
            for(Session client : clients){
                client.getBasicRemote().sendText(message);
            }
        }
    }

    @OnOpen
    public void onOpen (Session session) {
        // Add session to the connected sessions set
        clients.add(session);
    }

    @OnClose
    public void onClose (Session session) {
        // Remove session from the connected sessions set
        clients.remove(session);
    }

    public void sendMessage (String message) {
        synchronized(clients){
            // Iterate over the connected sessions
            // and broadcast the received message
            for(Session client : clients){
                try {
                    client.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}