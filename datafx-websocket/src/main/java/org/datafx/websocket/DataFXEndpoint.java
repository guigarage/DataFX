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

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
