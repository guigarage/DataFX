/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.reader.util;

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
    private int currentNode;
    private boolean domCreated = false;
    
    public XmlConverter (String tag, Class<T> clazz) {
        System.out.println("Created xmlconverter, tag = "+tag+" class = "+clazz);
        this.tag = tag;
        this.clazz = clazz;
    }
    
    public T convert(InputStream input) {
        T answer = JAXB.unmarshal(input, clazz);
        return answer;
    }

    public T next (InputStream is) {
        if (!domCreated) {
            initialize(is);
        }
        Node node = childNodes.item(currentNode);
                DOMSource source = new DOMSource(node);
                final T entry = (T) JAXB.unmarshal(source, clazz);
                currentNode++;
                return entry;
    }
    
    public boolean hasMoreData (InputStream is) {
     
            if (!domCreated) {
                initialize(is);
            }
        System.out.println("hasMoreData called, cn = "+currentNode+" total = "+totalNodes);
            return (currentNode < totalNodes);
        }
    
    

    private synchronized void initialize(InputStream is) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            childNodes = doc.getElementsByTagName(tag);
            System.out.println("tag = "+tag);
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
