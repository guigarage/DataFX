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
    private boolean connected = false;
    private T availableData;
    private Object availableLock = new Object();
    private boolean closed = false;
    
    public WebSocketReader (String address) {
        this.address = address;
        DataFXEndpoint.parent = this; // TODO refactor this
    }
    
    private void connectEndpoint () {
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
    
    public void setAvailableData (T data) {
        synchronized (this.availableLock) {
            this.availableData = data;
        }
    }
    
    public T get() {
        synchronized (this.availableLock) {
            if (this.availableData == null) {
                try {
                    this.availableLock.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(WebSocketReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return this.availableData;
    }

    public boolean next() {
        return !closed;
    }

    
}
