/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.websocket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import io.datafx.io.DataReader;
import io.datafx.io.converter.InputStreamConverter;

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
    
    @Override
    public T get() throws IOException {
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


    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("WebSockets don't support iterators.");
    }

    
}
