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
 * DISCLAIMED. IN NO EVENT SHALL DataFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.core;

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
