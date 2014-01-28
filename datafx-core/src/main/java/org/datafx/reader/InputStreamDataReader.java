/**
 * Copyright (c) 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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

import java.io.IOException;
import java.io.InputStream;

import org.datafx.reader.converter.InputStreamConverter;

/**
 *
 * Abstract class for functionality common across {@link DataReader} 
 * implementations that rely on
 * InputStreams. Common functionality includes the handling of a
 * Converting, translating the {@link java.io.InputStream} data into Object(s)
 * @author johan
 */
public abstract class InputStreamDataReader<T> extends AbstractDataReader<T> {

    private InputStream is;
    private InputStreamConverter<T> converter;
    
    public InputStreamDataReader () {}
    
    public InputStreamDataReader(final InputStreamConverter<T> converter) {
        this.converter = converter;
    }
    
    public InputStreamDataReader(final InputStream is, final InputStreamConverter<T> converter) {
        this.converter = converter;
        setInputStream(is);
    }

    public void setConverter (InputStreamConverter<T> converter) {
        this.converter = converter;
    }
    
    public InputStreamConverter<T> getConverter() {
        return this.converter;
    }
    
    public void setInputStream(InputStream is) {
        this.is = is;
        if (converter != null) {
            converter.initialize(is);
        }
    }

    public InputStream getInputStream() {
        return this.is;
    }
    
    @Override public T get() throws IOException {
        return converter.get();
    }

    @Override public boolean next() throws IOException {
        return converter.next();
    }
}
