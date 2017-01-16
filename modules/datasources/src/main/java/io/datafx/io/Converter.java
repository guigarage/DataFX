/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.io;

import java.io.IOException;

/**
 * A Converter is responsible for converting raw data (obtained from a data source)
 * into one or more Java objects.
 * 
 * Converters are used by implementations of {@link DataReader}.
 * 
 * A converter instance is typically created by the application code, and passed to
 * the datareader.
 * 
 * @param <U>  The format of the raw data, e.g. an {@link java.io.InputStream}, 
 * a {@link java.sql.ResultSet}.
 * @param <T> The type of the converted objects.
 */
public interface Converter<U, T> {

    /**
     * Initialize the raw data. In many cases, some initialization is required on
     * the raw data before the conversion to one or more objects can be performed.
     * In the case of an {@link io.datafx.io.converter.XmlConverter}, for example,
     * the DOM-model is created in this method.
     * @param input the raw input.
     * @throws java.io.IOException in case the raw input can't be processed by this converter.
     */
    public void initialize(U input) throws IOException;

    /**
     * Get the next available data entity in the desired type
     * @return the next available data
     */
    public T get();

    /**
     * Indicate whether or not more data can be expected from this converter. 
     * It is up to the implementation to detect if we can deliver more data entities
     * or not. If this method returns <code>false</code> a call to {@link #get()} 
     * will fail.
     * @return true if more data can be obtained from this converter, false otherwise
     */
    public boolean next();
}
