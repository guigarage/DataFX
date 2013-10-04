package org.datafx.websocket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import org.datafx.reader.converter.InputStreamConverter;

/**
 *
 * @author johan
 */
public class WebSocketReader<T> implements DataReader<T> {//, WritableDataReader<T> {
    
    private String address;
    private boolean connected = false;
    private List<String> availableData = Collections.synchronizedList(new LinkedList<String>());
    private final Object availableLock = new Object();
    private boolean closed = false;
    private Session session;
    private InputStreamConverter converter;
    
    public WebSocketReader (String address) {
        this.address = address;
        DataFXEndpoint.parent = this; // TODO refactor this
    }
    
    public void setConverter (InputStreamConverter converter) {
        this.converter = converter;
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
    
    public T get() {
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(WebSocketReader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("WebSocketReader GOT available data...");
        String msg = this.availableData.remove(0);
        if (converter != null) {
            converter.initialize(new ByteArrayInputStream(msg.getBytes()));
           return (T) converter.get();
        }
        return (T) msg;
    }

    public boolean next() {
        return !closed;
    }
    
    public void sendMessage (String msg) throws IOException {
        session.getBasicRemote().sendText(msg);
    }

//    public void writeBack() {
//        session.getBasicRemote().
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    
}
