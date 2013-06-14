
package org.datafx.writer;

/**
 *
 * @author johan
 */
public interface WriteBackListProvider<T> {
    
        public void setAddEntryHandler(WriteBackHandler<T> handler);

}
