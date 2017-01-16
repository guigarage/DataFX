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
package io.datafx.io;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for storing a Map where a specific key can hold more than
 * one value. In HTTP Rest requests, form parameters are allowed to have 
 * more than one value for a specific key, in which case this class can be used.
 */
public class MultiValuedMap extends HashMap<String, List<String>> {

    public MultiValuedMap() {
    }

    /**
     * Convenience method. Only add the value to the values already belonging to
     * this key in case value is not NULL. This saves client-checks on null
     * values. // TODO not sure we really want this.
     *
     * @param key  the key of which the values need to be updated
     * @param value  the value to be added when not NULL
     * @return the new list of values mapped to the specified key
     */
    public List<String> optPut(String key, String value) {
        if (value != null) {
            return put(key, value);
        } else {
            return super.get(key);
        }
    }

    /**
     * Add the provided value to the list of values associated with this key
     * @param key  the key
     * @param value  the new (additional) value belonging to this key.
     * @return the list associated with this key 
     */
    public List<String> put(String key, String value) {
        List<String> l = super.get(key);
        if (l == null) {
            l = new LinkedList<>();
            super.put(key, l);
        }
        l.add(value);
        return l;
    }

    /**
     * Copies all of the mappings from the specified map into this map.
     * @param map  mappings to be stored in this map
     */
    public void putMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

}
