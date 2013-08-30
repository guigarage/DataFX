package org.datafx.reader;

/**
 *
 * This is the root interface for all data readers. 
 * Implementations should provide data, and should be able to inform dataproviders
 * when the last piece of data has arrived.
 * 
 * @author johan
 */
public interface DataReader<T> {
	/**
         * Obtain the next entity of data. In case the data is a single entity, 
         * this method returns all data. In case the data is a list, this method
         * returns the next entity and moves a pointer to the subsequent entity, if any
         * @return the next available data entity or null if no new data can be
         * retrieved
         */
	public T get();
	
        /**
         * Check if more data is available on this DataReader. This method will only
         * return false if no data is available. It should not return false in case
         * data will be available at a later time.
         * Calling this method on a DataReader that does not support lists always returns
         * false
         * @return true in case more data is available and obtainable via a @link get call,
         * false otherwise.
         */
	public boolean next();
	
}
