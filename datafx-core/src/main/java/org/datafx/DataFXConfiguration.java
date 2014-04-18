package org.datafx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class DataFXConfiguration {

    private static DataFXConfiguration instance;

    private Document configurationDocument;

    private DataFXConfiguration() {}

    public boolean isExceptionLoggingActive() {
        return true;
    }

    public int getDefaultThreadMaxSize() {
        return 32;
    }

    public int getDefaultThreadPoolStartSize() {
        return 2;
    }

    public String getThreadGroupName() {
        return "DataFX thread pool";
    }

    public long getDefaultThreadTimeout() {
        return 1000;
    }

    public List<Element> getElements(String tagName) {
        NodeList nodes = getConfigurationDocument().getElementsByTagName(tagName);
        List<Element> ret = new ArrayList<>();
        for (int temp = 0; temp < nodes.getLength(); temp++) {
            Node node = nodes.item(temp);
            ret.add((Element) node);
        }
        return ret;
    }

    public Document getConfigurationDocument() {
        if(configurationDocument == null) {
            configurationDocument = readDataFXConfiguration();
        }
        return configurationDocument;
    }

    private Document readDataFXConfiguration() {
        try {
        DocumentBuilderFactory builderFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(this.getClass().getClassLoader().getResourceAsStream("META-INF/datafx.xml"));
        } catch (Exception e) {
            throw new RuntimeException("Can't load DataFX configuration. Please check META-INF/datafx.xml", e);
        }
    }

    public static synchronized DataFXConfiguration getInstance() {
        if(instance == null) {
            instance = new DataFXConfiguration();
        }
        return instance;
    }
}
