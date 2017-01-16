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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.datafx.reader.converter.InputStreamConverter;
import org.datafx.reader.converter.XmlConverter;

/**
 *
 * @author johan
 */
public class FileSource<T> extends InputStreamDataReader<T> {

  //  private InputStream is;
    private InputStreamConverter<T> converter;

    /**
     * Create a FileSource based on an existing file. By default, it is assumed
     * the input format is XML. Use FileSource (String, Converter) to specify a
     * different format.
     *
     * @param filename the filename
     * @throws FileNotFoundException
     */
//    public FileSource(String filename, Class<T> clazz) throws FileNotFoundException {
//        this(filename, new XmlConverter<T>("", clazz), clazz);
//    }

    /**
     * Create a FileSource based on an existing file. By default, it is assumed
     * the input format is XML. Use FileSource (String, Format) to specify a
     * different format.
     *
     * @param f
     * @param filename the filename
     * @throws FileNotFoundException
     */
    public FileSource(File f) throws FileNotFoundException, IOException {
        this(f, new XmlConverter<>("", null));
    }

    /**
     * Create a FileSource based on an existing inputstream. By default, it is
     * assumed the input format is XML. Use FileSource (String, Format) to
     * specify a different format.
     *
     * @param filename the filename
     * @throws FileNotFoundException
     */
//    public FileSource(InputStream is, Class clazz) {
//        this(is, Format.XML, clazz);
//    }

    /**
     * Create a FileSource based on an existing file.
     *
     * @param filename the filename
     * @param format the format of the data
     * @throws FileNotFoundException
     */
//    public FileSource(String filename, Format format, Class clazz) throws FileNotFoundException {
//        this.is = new FileInputStream(filename);
//        this.format = format;
//        this.clazz = clazz;
//    }

    /**
     * Create a FileSource based on an existing file.
     *
     * @param f the file
     * @param format the format of the data
     * @throws FileNotFoundException
     */
    public FileSource(File f, InputStreamConverter<T> converter) throws FileNotFoundException, IOException {
        super(new FileInputStream(f), converter);
    }

    /**
     * Create a FileSource based on an existing inputstream.
     *
     * @param filename the filename
     * @param format the format of the data
     * @throws FileNotFoundException
     */
//    public FileSource(InputStream is, Format format, Class clazz) {
//        this.is = is;
//        this.format = format;
//        this.clazz = clazz;
//    }
    
}
