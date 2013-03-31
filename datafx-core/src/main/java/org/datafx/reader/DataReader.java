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
	
	public T getData();
	public boolean hasMoreData();
	
}
