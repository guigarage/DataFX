/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.reader;

/**
 *
 * @author johan
 */
public class SSERestSourceBuilder<T> extends RestSourceBuilder<T> {
  
    public static<T> SSERestSourceBuilder create() {
        return new SSERestSourceBuilder();
    }
    
    protected SSERestSourceBuilder() {
        this.restSource = new SSERestSource();
    }
    
}
