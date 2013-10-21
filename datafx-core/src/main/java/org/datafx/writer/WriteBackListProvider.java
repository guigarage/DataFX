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
package org.datafx.writer;

/**
 *
 * This interface should be implemented by a {@link org.datafx.provider.DataProvider} 
 * that is capable of receiving lists of entities, and that wants to write
 * new entries (added locally) back to the original data source.
 * @author johan
 * @param <T> the type of the data that is added locally and that has to be written back
 * to the original data source.
 */
public interface WriteBackListProvider<T> {
    
     /**
     * Set the {@link WriteBackHandler} for this provider to the specified handler.
     * When the provider decides to add data back to the original datasource 
     * (that is, when data has been added locally), the 
     * {@link WriteBackHandler#createDataSource(java.lang.Object) } method will
     * be called. This method returns a {@link org.datafx.reader.WritableDataReader}
     * that will be used to send the data to the origin, by calling the
     * {@link org.datafx.reader.WritableDataReader#writeBack() } method.
     * @param handler the handler that will be used when the provider decides to 
     * write data back to the original source.
     */
        public void setAddEntryHandler(WriteBackHandler<T> handler);

}
