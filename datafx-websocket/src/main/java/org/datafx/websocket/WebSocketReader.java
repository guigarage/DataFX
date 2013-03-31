/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import org.datafx.reader.DataReader;

/**
 *
 * @author johan
 */
public class WebSocketReader<T> implements DataReader<T> {
    
    private String address;
    
    public WebSocketReader (String address) {
        this.address = address;
    }
    
    private void connectEndpoing () {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(DataFXEndpoint.class, null, new URI(address));
        } catch (URISyntaxException ex) {
            Logger.getLogger(WebSocketReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DeploymentException ex) {
            Logger.getLogger(WebSocketReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebSocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public T getData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasMoreData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
