/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author johan
 */
public class XmlConverter<T> extends InputStreamConverter<T> {

    private String tag;
    private Class<T> clazz;
    private NodeList childNodes;
    private int totalNodes;
    private int currentNode = 0;
    private boolean domCreated = false;
    private InputStream inputStream;
    
    public XmlConverter (String tag, Class<T> clazz) {

        this.tag = tag;
        this.clazz = clazz;
    }

    @Override public T get() {
        // if no clazz is specified, we assume it is a single item
        if (clazz == null) {
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
        System.out.println(source + " , " + clazz);
        final T entry = (T) JAXB.unmarshal(source, clazz);
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

    @Override public synchronized void initialize(InputStream is) {
        this.inputStream = is;
        
        if (clazz == null) {
            return;
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            childNodes = doc.getElementsByTagName(tag);
            totalNodes = childNodes.getLength();
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
