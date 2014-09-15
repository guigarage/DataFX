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
package io.datafx.controller.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract base class for all context classes. See {@link ApplicationContext}
 * for more informations.
 *
 * @author hendrikebbers
 */
public class AbstractContext {

    private String id = UUID.randomUUID().toString();
    private Map<String, Object> registeredObjects;

    public AbstractContext() {
        registeredObjects = new HashMap<String, Object>();
    }

    /**
     * Returns the unique id of this context
     *
     * @return unique id
     */
    public String getId() {
        return id;
    }

    /**
     * Register a object to the context by using a key
     *
     * @param key   the key
     * @param value the object
     */
    public void register(String key, Object value) {
        registeredObjects.put(key, value);
    }

    /**
     * Returns a registered object that is registered in the context with the
     * given key
     *
     * @param key the key
     * @return the registered object
     */
    public Object getRegisteredObject(String key) {
        return registeredObjects.get(key);
    }

    /**
     * Returns a registered object that is registered by its' class
     *
     * @param cls the class of the object
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public <T> T getRegisteredObject(Class<T> cls) {
        return (T) getRegisteredObject(cls.toString());
    }

    public void register(Object value) {
        register(value.getClass().toString(), value);
    }

    public <S, T extends S> void register(T value, Class<S> asClass) {
        register(asClass.toString(), value);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AbstractContext{");
        sb.append("id='").append(id).append('\'');
        sb.append(", registeredObjects=").append(registeredObjects);
        sb.append('}');
        return sb.toString();
    }
}
