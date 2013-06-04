
package org.datafx.writer;

/**
 *
 * @author johan
 */
public abstract class WriteBackHandler<T> {
    
    public abstract void writeBackRequested (WriteBackEvent<T> event);
}
