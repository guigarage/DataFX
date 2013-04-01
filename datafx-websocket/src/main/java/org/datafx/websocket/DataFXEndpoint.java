/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
        session.addMessageHandler(this);
    }

    public void onMessage(String message) {
        parent.setAvailableData(message);
    }
    
}
