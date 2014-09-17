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
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.io.converter;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * An implementation of {@link io.datafx.io.Converter} that converts JSON
 * data into Java Objects of type T.
 *
 * @param <T> the type of the resulting Java objects
 */
public class JsonConverter<T> extends InputStreamConverter<T> {

    private JsonStructure rootNode;
    private final String tag;
    private final Class<T> clazz;
    private final ObjectMapper<T> objectMapper;
    private Iterator<JsonValue> iterator;
    private static final Logger LOGGER = Logger.getLogger(JsonConverter.class.getName());

    /**
     * Create a JsonConverter that will generate instances of the specified
     * class.
     *
     * @param clazz the entities returned by the {@link #get()} call will be of
     * class <code>clazz</code>
     */
    public JsonConverter(Class<T> clazz) {
        this(null, clazz);
    }

    /**
     * Create a JsonConverter that will generate instances of the specified
     * class. Using this constructor, it is assumed that the entities are
     * contained within a Json structure in a Node with the name specified by
     * the value of the <code>tag</code> parameter.
     *
     * @param clazz the entities returned by the {@link #get()} call will be of
     * class <code>clazz</code>
     * @param tag the name of the json node(s) holding the data entity(ies).
     */
    public JsonConverter(String tag, Class<T> clazz) {
        this.tag = tag;
        this.clazz = clazz;
        this.objectMapper = new ObjectMapper<>(clazz);
    }

    @Override
    public void initialize(InputStream input) {
        JsonReader reader = Json.createReader(input);
        rootNode = reader.read();

        System.out.println("rootNode.toString() = " + rootNode.toString());

        if (rootNode.getValueType() == JsonValue.ValueType.ARRAY) {
            JsonArray rootNodeArray = (JsonArray) rootNode;
            iterator = rootNodeArray.iterator();
        } else if (tag != null && rootNode.getValueType() == JsonValue.ValueType.OBJECT) {
            JsonObject rootNodeObject = (JsonObject) rootNode;
            if (rootNodeObject.containsKey(tag)) {
                JsonValue tagValue = rootNodeObject.get(tag);
                if (tagValue.getValueType() == JsonValue.ValueType.ARRAY) {
                    JsonArray rootNodeArray = (JsonArray) tagValue;
                    iterator = rootNodeArray.iterator();
                }
            }
        }
    }

    @Override
    public T get() { 
        T item;
        LOGGER.log(Level.FINER, "getting json data, tag = {0}", tag);

        // if no tag is specified, we assume it is a single item
        if (tag == null) {
            item = objectMapper.readValue(rootNode);
        } else {
            JsonValue next = iterator.next();
            item = objectMapper.readValue(next);
        }

        LOGGER.log(Level.FINER, "did read Object {0}", item);
        return item;
    }

    @Override
    public boolean next() {
        if (tag == null) {
            return false;
        } else {
            return iterator.hasNext();
        }
    }

    class ObjectMapper<T> {

        private final Class<T> clazz;
        private final Map<String, Method> settersMappedByPropertyName = new HashMap<>();

        public ObjectMapper(Class<T> clazz) {
            this.clazz = clazz;

            resolveProperties();
        }

        private synchronized void resolveProperties() {
            Method[] methods = clazz.getMethods();

            // sort methods array by method name, so getter methods are processed before setter methods
            Arrays.sort(methods, new Comparator<Method>() {
                @Override
                public int compare(Method o1, Method o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            Map<String, Method> getters = new HashMap<>();
            for (Method method : methods) {
                String methodName = method.getName();
                if (Modifier.isPublic(method.getModifiers()) &&
                        method.getParameterTypes().length == 0 &&
                        !method.isAnnotationPresent(XmlTransient.class) &&
                        ((methodName.matches("^get[A-Z].*") && !method.getReturnType().equals(void.class)) ||
                         (methodName.matches("^is[A-Z].*") && method.getReturnType().equals(boolean.class)))) {
                    String bareMethodName = methodName.startsWith("get") ? methodName.substring(3) : methodName.substring(2);
                    getters.put(bareMethodName, method);
                }

                if (Modifier.isPublic(method.getModifiers()) &&
                        method.getParameterTypes().length == 1 &&
                        method.getReturnType().equals(void.class) &&
                        !method.isAnnotationPresent(XmlTransient.class) &&
                        methodName.matches("^set[A-Z].*")) {
                    String bareMethodName = methodName.substring(3);
                    Method getter = getters.get(bareMethodName);
                    if (getter != null) {
                        String finalName = bareMethodName.substring(0, 1).toLowerCase();
                        if (bareMethodName.length() > 1) {
                            finalName += bareMethodName.substring(1);
                        }

                        settersMappedByPropertyName.put(finalName, method);
                    }
                }
            }
        }

        private T readValue(JsonValue value) {
            try {
                T t = clazz.newInstance();

                if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject object = (JsonObject) value;

                    for (String property : settersMappedByPropertyName.keySet()) {
                        System.out.println("Evaluate property "+property);
                        if (object.containsKey(property)) {
                            Method setter = settersMappedByPropertyName.get(property);

                            JsonValue propertyValue = (JsonValue) object.get(property);
                            System.out.println("[JVDBG] we have to set the propval to "+propertyValue);
                            Object[] args = new Object[1];
                            switch (propertyValue.getValueType()) {
                                case NULL:
                                    args[0] = null;
                                    break;
                                case FALSE:
                                    args[0] = Boolean.FALSE;
                                    break;
                                case TRUE:
                                    args[0] = Boolean.TRUE;
                                    break;
                                case STRING:
                                    JsonString stringProperty = (JsonString) propertyValue;
                                    args[0] = stringProperty.getString();
                                    break;
                                case NUMBER:
                                    JsonNumber numberProperty = (JsonNumber) propertyValue;
                                    Class setterParameterType = setter.getParameterTypes()[0];
                                    if (!setterParameterType.isArray()) {
                                        String setterParameterTypeName = setterParameterType.getName();
                                        switch (setterParameterTypeName) {
                                            case "byte":
                                            case "java.lang.Byte":
                                            case "int":
                                            case "java.lang.Integer":
                                            case "short":
                                            case "java.lang.Short":
                                                args[0] = numberProperty.intValue();
                                                break;
                                            case "long":
                                            case "java.lang.Long":
                                                args[0] = numberProperty.longValue();
                                                break;
                                            case "double":
                                            case "java.lang.Double":
                                                args[0] = numberProperty.doubleValue();
                                                break;
                                            case "float":
                                            case "java.lang.Float":
                                                args[0] = (float) numberProperty.doubleValue();
                                                break;
                                            case "java.lang.String":
                                            case "javafx.beans.property.StringProperty":
                                                args[0] = numberProperty.toString();
                                        }
                                    }
                                    break;
                                case ARRAY:
                                    JsonArray arrayProperty = (JsonArray) propertyValue;
                                    Class<?> parameterType = setter.getParameterTypes()[0];
                                    List values;
                                    if (parameterType.isAssignableFrom(ObservableList.class)) {
                                        values = FXCollections.observableArrayList();
                                    }
                                    else {
                                        values = new ArrayList();
                                    }
                                    for (int i = 0; i < arrayProperty.size(); i++) {
                                        JsonValue arrayValue = arrayProperty.get(i);
                                        switch (arrayValue.getValueType()) {
                                            case NULL:
                                                values.add(null);
                                                break;
                                            case FALSE:
                                                values.add(Boolean.FALSE);
                                                break;
                                            case TRUE:
                                                values.add(Boolean.TRUE);
                                                break;
                                            case STRING:
                                                JsonString stringArrayValue = (JsonString) arrayValue;
                                                values.add(stringArrayValue.getString());
                                                break;
                                            case NUMBER:
                                                // TODO: find out how to know what type of numbers are contained the list
                                                throw new UnsupportedOperationException("Arrays of numbers not yet supported.");
                                            default:
                                                // TODO: implement nested arrays and objects in arrays
                                                throw new UnsupportedOperationException("Arrays or objects within arrays not yet supported.");
                                        }
                                    }
                                    args[0] = values;
                                    break;
                                case OBJECT:
                                    // TODO: implement nested objects
                                    throw new UnsupportedOperationException("Nested Json Objects not yet supported.");
                            }

                            try {
                                setter.invoke(t, args);
                            } catch (IllegalArgumentException | InvocationTargetException ex) {
                                Logger.getLogger(JsonConverter.class.getName()).log(Level.SEVERE, "Failed to call setter " + setter + " with value " + propertyValue, ex);
                            }
                        }
                    }
                }

                return t;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(JsonConverter.class.getName()).log(Level.SEVERE, "Could not instantiate object of type " + clazz, ex);
            }

            return null;
        }

    }
}
