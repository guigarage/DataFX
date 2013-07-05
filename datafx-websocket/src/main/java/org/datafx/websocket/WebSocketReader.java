package org.datafx.websocket;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.datafx.reader.DataReader;

/**
 *
 * @author johan
 */
public class WebSocketReader implements DataReader<String> {//, WritableDataReader<T> {
    
    private String address;
    private boolean connected = false;
    private List<String> availableData = Collections.synchronizedList(new LinkedList<String>());
    private final Object availableLock = new Object();
    private boolean closed = false;
    private Session session;
    
    public WebSocketReader (String address) {
        this.address = address;
        DataFXEndpoint.parent = this; // TODO refactor this
    }
    
    private void connectEndpoint () {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(DataFXEndpoint.class, null, new URI( address));
            connected = true;
        } catch (Exception ex) {
            Logger.getLogger(WebSocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addAvailableData (String data) {
        System.out.println("WebSocketReader has new data");
        synchronized (this.availableLock) {
             this.availableData.add( data);
            this.availableLock.notifyAll();
        }
        
        System.out.println("WebSocketReader was notified new data");
    }
    
    public String get() {
        System.out.println("WebSocketReader in get...");
        synchronized (this.availableLock) {
            if (!connected) {
                System.out.println("WebSocketReader needs to connect first...");
                connectEndpoint();
                System.out.println("WebSocketReader connected...");
            }
            if (this.availableData.isEmpty()) {
                System.out.println("WebSocketReader waits for available data...");
                try {
                    this.availableLock.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(WebSocketReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("WebSocketReader GOT available data...");
        return this.availableData.remove(0);
    }

    public boolean next() {
        return !closed;
    }

//    public void writeBack() {
//        session.getBasicRemote().
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    
}
