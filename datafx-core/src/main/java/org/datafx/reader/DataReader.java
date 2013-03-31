package org.datafx.reader;

/**
 *
 * @author johan
 */
public interface DataReader<T> {
	
	public T getData();
	public boolean hasMoreData();
	
}
