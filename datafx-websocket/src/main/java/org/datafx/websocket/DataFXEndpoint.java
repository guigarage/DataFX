package org.datafx.websocket;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 *
 * @author johan
 */
public class DataFXEndpoint extends Endpoint implements MessageHandler.Whole<String> {

    static protected WebSocketReader parent;
    
    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Endpoint is open now");
        session.addMessageHandler(this);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("DataFXEndpoint got a message!!");
        parent.addAvailableData(message);
    }
    
}
