/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import org.datafx.reader.util.Converter;
import org.datafx.reader.util.InputStreamConverter;
import org.datafx.reader.util.XmlConverter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
     * @param filename the filename
     * @throws FileNotFoundException
     */
    public FileSource(File f, Class clazz) throws FileNotFoundException {
        this(f, new XmlConverter<T>("", clazz), clazz);
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
    public FileSource(File f, InputStreamConverter converter, Class clazz) throws FileNotFoundException {
        setInputStream(new FileInputStream(f));
        setConverter(converter);
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
