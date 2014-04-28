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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParser;
//import org.codehaus.jackson.JsonNode;
//import org.codehaus.jackson.JsonParser;
//import org.codehaus.jackson.map.MappingJsonFactory;
//import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * An implementation of {@link org.datafx.util.Converter} that converts JSON data into Java Objects
 * of type T.
 * 
 * @param <T> the type of the resulting Java objects
 * @author johan
 */
public class JsonConverter<T> extends InputStreamConverter<T> {

    private JsonStructure rootNode;
  //  private JsonNode rootNode;
    private final String tag;
    private final Class<T> clazz;
 //   Iterator<JsonNode> iterator;
    Iterator<JsonObject> iterator;
    private static final Logger LOGGER = Logger.getLogger(JsonConverter.class.getName());


    /**
     * Create a JsonConverter that will generate instances of the specified class.
     * @param clazz the entities returned by the {@link get()} call will be of class <code>clazz</code>
     */
    public JsonConverter (Class<T> clazz) {
        this (null, clazz);
    }
    
    /**
     * Create a JsonConverter that will generate instances of the specified class.
     * Using this constructor, it is assumed that the entities are contained within a 
     * Json structure in a Node with the name specified by the value of the <code>tag</code> parameter.
     * @param clazz the entities returned by the {@link get()} call will be of class <code>clazz</code>
     * @param tag the name of the json node(s) holding the data entity(ies).
     */
    public JsonConverter(String tag, Class<T> clazz) {
        this.tag = tag;
        this.clazz = clazz;
    }

    @Override
    public void initialize(InputStream input) {
        try {
            JsonReader reader = Json.createReader(input);
            rootNode = reader.read();
            MappingJsonFactory mappingJsonFactory = new MappingJsonFactory();
            JsonParser jp = mappingJsonFactory.createJsonParser(input);
            rootNode = jp.readValueAsTree();
            if (tag != null) {
                JsonObject obj = (JsonObject)rootNode;
                obj.getJsonArray(tag);
                JsonNode cNode = rootNode.findValue(tag);
                if (cNode != null) {
                    iterator = cNode.iterator();
                }
                else {
                    System.err.println("Couldn't find a rootnode for tag "+tag+", rootNode = "+rootNode);
                    // we were looking for a tag, but couldn't find one. 
                    // TODO: does this mean we should throw an exception, or will we use the children on the root node instead?
                }
            }
        } catch (IOException ex) {
            // TODO throw an exception? fatal?
            Logger.getLogger(JsonConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public T get() { // if no tag is specified, we assume it is a single item
        LOGGER.log(Level.FINER, "getting json data, tag = {0}", tag);
        if (tag == null) {
            try {
                ObjectMapper om = new ObjectMapper();
                Object o = om.readValue(rootNode, clazz);
                LOGGER.finer("did read Object "+o);
                final T entry = (T) o;
                return entry;
            } catch (IOException ex) {
                Logger.getLogger(JsonConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                // this will fail if the iterator is null, which happens if we don't find the tag in the nodelist
                JsonNode candidate = iterator.next();
                ObjectMapper om = new ObjectMapper();
                final T entry = (T) om.readValue(candidate, clazz);
                return entry;
            } catch (IOException ex) {
                Logger.getLogger(JsonConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // TODO rather throw an exception
        return null;
    }

    @Override
    public boolean next() {
        if (tag == null) {
            return false;
        } else {
            return iterator.hasNext();
        }
    }
}
