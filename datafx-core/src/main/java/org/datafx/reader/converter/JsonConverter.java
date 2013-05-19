package org.datafx.reader.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author johan
 */
public class JsonConverter<T> extends InputStreamConverter<T> {

    private JsonNode rootNode;
    private final String tag;
    private final Class<T> clazz;
    Iterator<JsonNode> iterator;

    //  private InputStream inputStream;
    public JsonConverter(String tag, Class<T> clazz) {
        this.tag = tag;
        this.clazz = clazz;
    }

    @Override
    public void initialize(InputStream input) {
        try {
            //    this.inputStream = input;
            MappingJsonFactory mappingJsonFactory = new MappingJsonFactory();
            JsonParser jp = mappingJsonFactory.createJsonParser(input);
            rootNode = jp.readValueAsTree();
            if (tag != null) {
                JsonNode cNode = rootNode.findValue(tag);
                iterator = cNode.iterator();
            }
        } catch (IOException ex) {
            // TODO throw an exception? fatal?
            Logger.getLogger(JsonConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public T get() { // if no tag is specified, we assume it is a single item
        if (tag == null) {
            try {
                ObjectMapper om = new ObjectMapper();
                Object o = om.readValue(rootNode, clazz);
                final T entry = (T) o;
                return entry;
            } catch (IOException ex) {
                Logger.getLogger(JsonConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
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
