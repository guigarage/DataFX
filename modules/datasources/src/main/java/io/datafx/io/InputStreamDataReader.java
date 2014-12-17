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
import java.io.InputStream;
import io.datafx.io.converter.InputStreamConverter;

/**
 * Abstract class for functionality common across {@link DataReader}
 * implementations that rely on InputStreams. Common functionality includes the
 * handling of a Converter, translating the {@link java.io.InputStream} data
 * into Object(s)
 *
 * @param <T> the type of data this reader expects
 */
public abstract class InputStreamDataReader<T> extends AbstractDataReader<T> {

    private InputStream is;
    private InputStreamConverter<T> converter;

    /**
     * Create an InputStreamDataReader without any configuration options
     */
    public InputStreamDataReader() {
    }

    /**
     * Create an InputStreamDataReader and assign a {@link Converter} to this.
     * @param converter  the {@link Converter} that will be used to convert raw
     * data into object(s) of type {@link T}
     */
    public InputStreamDataReader(final InputStreamConverter<T> converter) {
        this.converter = converter;
    }

    /**
     * Create an InputStreamDataReader, set the {@link java.io.InputStream} and 
     * assign a {@link Converter} to this.
     * Once the InputStream is supplied, the {@link #setInputStream(java.io.InputStream)}
     * method will be called, which will also initialize the InputStream.
     * @param is  the InputStream containing the data for this InputStreamReader
     * @param converter  the {@link Converter} that will be used to convert raw
     * data into object(s) of type T
     * @throws IOException  in case the initialization of the InputStream fails.
     */
    public InputStreamDataReader(final InputStream is, final InputStreamConverter<T> converter) throws IOException {
        this.converter = converter;
        setInputStream(is);
    }

    /**
     * Set the converter that will convert the raw data into object(s) of type T.
     * @param converter  the {@link Converter} that will be used to convert raw
     * data into object(s) of type T
     */
    public void setConverter(InputStreamConverter<T> converter) {
        this.converter = converter;
    }

    /**
     * Get the converter that is used by this InputStreamDataReader.
     * @return  the converter that is used by this InputStreamDataReader, 
     * either supplied with the constructor or using the 
     * {@link #setConverter(io.datafx.io.converter.InputStreamConverter) }
     * method.
     */
    public InputStreamConverter<T> getConverter() {
        return this.converter;
    }

    /**
     * Explicitly set the InputStream for this InputStreamDataReader. In case
     * the InputStream is supplied with the constructor, this method will be
     * called by the constructor.
     * This method will initialize the InputStream, and assign it to the 
     * converter. It is up the the implementation of {@link Converter} to
     * decide what to do upon initialization (e.g. checking headers).
     * @param is  the InputStream that will be initialized.
     * @throws  IOException in case initialization of the InputStream fails. 
     */
    public void setInputStream(InputStream is) throws IOException {
        this.is = is;
        if (converter != null) {
            converter.initialize(is);
        }
    }

    /**
     * Return the InputStream used by this InputStreamDataReader.
     * @return  the InputStream, either passed via the constructor or via the
     * {@link #setInputStream(java.io.InputStream) } method.
     */
    public InputStream getInputStream() {
        return this.is;
    }

    @Override
    public T get() throws IOException {
        return converter.get();
    }

    @Override
    public boolean next() {
        return converter.next();
    }
}
