
package org.datafx.writer;

import org.datafx.reader.DataReader;

/**
 *
 * @author johan
 */
public abstract class WriteBackHandler<T> {
    
   // public abstract DataReader createDataSource (WriteBackEvent<T> event);
    public abstract DataReader createDataSource (T observable);
}
