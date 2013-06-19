package org.datafx.reader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author johan
 */
public class MultiValuedMap extends HashMap<String, List<String>>{
    
    public MultiValuedMap() {    
    }
    
    /**
     * Convenience method. Only add the value to the values already belonging 
     * to this key in case value is not NULL. This saves client-checks on null
     * values. 
     * // TODO not sure we really want this.
     * @param key
     * @param value
     * @return 
     */
    public List<String> optPut(String key, String value) {
        if (value != null) {
            return put(key, value);
        }
        else {
            return super.get(key);
        }
    }
    
    public List<String> put(String key, String value) {
        List<String> l = super.get(key);
        if (l == null) {
            l = new LinkedList<String>();
            super.put(key, l);
        }
        l.add(value);
        return l;
    }
    
    /**
     *
     * @param map
     */
    public void putMap(Map<String, String> map) {
        for (Map.Entry<String, String> entry: map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    
}
