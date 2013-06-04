
package org.datafx.writer;

/**
 *
 * @author johan
 */
public class WriteBackEvent<T>  {
    
    private T changedObject;
    
    public T getObject() {
        return changedObject;
    }
    
}
