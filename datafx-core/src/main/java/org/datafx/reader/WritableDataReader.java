package org.datafx.reader;

/**
 *
 * @author johan
 */
public interface WritableDataReader<T> extends DataReader<T> {
    
    void writeBack ();
    
}
