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
package org.datafx.reader.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * A Converter implementation that converts a
 * {@link java.io.InputStream} into one or more instances of a java class
 * with type T.
 * @author johan
 * @param <T> The type of the converted objects.
 */
public class XmlConverter<T> extends InputStreamConverter<T> {

    private String tag;
    private Class<T> clazz;
    private NodeList childNodes;
    private int totalNodes;
    private int currentNode = 0;
    private boolean domCreated = false;
    private InputStream inputStream;

    /**
     * Create an XmlConverter that will generate instances of the specified class.
     * @param clazz the entities returned by the {@link get()} call will be of class <code>clazz</code>
     */
    public XmlConverter (Class<T> clazz) {
        this(null, clazz);
    }
    
        
    /**
     * Create an XmlConverter that will generate instances of the specified class.
     * Using this constructor, it is assumed that the entities are contained within a 
     * XML structure in a DOM with the name specified by the value of the <code>tag</code> parameter.
     * @param clazz the entities returned by the {@link get()} call will be of class <code>clazz</code>
     * @param tag the name of the xml element(s) holding the data entity(ies).
     */
    public XmlConverter (String tag, Class<T> clazz) {
        this.tag = tag;
        this.clazz = clazz;
    }

    @Override public T get() {
        // if no tag is specified, we assume it is a single item
        if (tag == null) {
            if (currentNode == 0) {
                T answer = JAXB.unmarshal(inputStream, clazz);
                return answer;
            } else {
                return null;
            }
        }
        
        // otherwise we expect a DOM to have been created, and we navigate through
        // that
        if (!domCreated) {
            throw new IllegalStateException("XmlConverter has not been initialized");
        }
        
        Node node = childNodes.item(currentNode);
        DOMSource source = new DOMSource(node);
        final T entry = (T) JAXB.unmarshal(source, clazz);
        System.out.println("entry retrieved: "+entry);
        currentNode++;
        return entry;

    }

    @Override public boolean next() {
        if (clazz == null) {
            if (currentNode == 0) {
                currentNode++;
            }
            return currentNode == 0; 
        }
        
        if (!domCreated) {
            throw new IllegalStateException("XmlConverter has not been initialized");
        }
        return currentNode < totalNodes;
    }

    @Override 
    public synchronized void initialize(InputStream is) {
        this.inputStream = is;
        
        if (clazz == null) {
            return;
        }

        try {
            if (tag != null) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(inputStream);

                childNodes = doc.getElementsByTagName(tag);
                totalNodes = childNodes.getLength();
                System.out.println("childnodes for tag " + tag + "= " + childNodes + " and length = " + totalNodes);
            } else {
                totalNodes = 1;
            }
            currentNode = 0;
            domCreated = true;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XmlConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XmlConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
