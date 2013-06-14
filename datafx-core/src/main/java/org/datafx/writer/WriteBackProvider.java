package org.datafx.writer;

/**
 * 
 * Classes implementing this interface are capable of writing changes to
 * entities of type <T> back to the original dataprovider.
 *
 * @author johan
 */
public interface WriteBackProvider<T> {
    
    public void setWriteBackHandler(WriteBackHandler<T> handler);

}
