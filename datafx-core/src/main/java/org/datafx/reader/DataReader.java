/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.reader;

/**
 *
 * This is the root interface for all data readers. A DataReader is responsible
 * for retrieving external data, and convert the raw external data into a Java
 * Objects.
 * <br/>
 * Implementations should provide data, and should be able to inform dataproviders
 * when the last piece of data has arrived.
 * 
 * @param <T> the type of Java Objects that should be returned by this DataReader
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
         * @return true in case more data is available and obtainable via a {@link get()} call,
         * false otherwise.
         */
	public boolean next();
	
}
