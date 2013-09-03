package org.datafx.reader;

import java.util.HashMap;
import java.util.Map;
import org.datafx.reader.converter.InputStreamConverter;

/**
 *
 * Builder class for creating an {@link RestSource}.
 * @author johan
 */
public final class RestSourceBuilder<T> {
    
    private final RestSource<T> restSource;
    private String path = "";
    private Map<String, String> queryParams = new HashMap<>();
    private MultiValuedMap formParams = new MultiValuedMap();
    
    public static<T> RestSourceBuilder create() {
        return new RestSourceBuilder();
    }
    
    private RestSourceBuilder() {
        this.restSource = new RestSource();
    }
    
    public RestSourceBuilder converter(InputStreamConverter<T> converter) {
        this.restSource.setConverter(converter);
        return this;
    }
    
   public RestSourceBuilder path (String p) {
        this.path = this.path + "/"+ p;
        return this;
    }
        
            
   public RestSourceBuilder requestMethod (String method) {
       this.restSource.setRequestMethod(method);
       return this;
   }
           
   public RestSourceBuilder queryParam(String key, String value) {
        this.queryParams.put(key, value); 
        return this;
    }
    
    public RestSourceBuilder formParam (String key, String value) {
        this.formParams.put(key, value);
        return this;
    }
    
    public RestSourceBuilder dataString (String dataString) {
        this.restSource.setDataString(dataString);
        return this;
    }
    
    public RestSourceBuilder consumerKey (String key) {
        restSource.setConsumerKey(key);
        return this;
    }
    
    public RestSourceBuilder consumerSecret (String secret) {
        restSource.setConsumerSecret(secret);
        return this;
    }
    
    public RestSourceBuilder host (String host) {
        restSource.setHost(host);
        return this;
    }
    
    public RestSourceBuilder timeout(int to) {
        restSource.setTimeout(to);
        return this;
    }
    
    public RestSource build () {
        restSource.setQueryParams(queryParams);
        restSource.setFormParams(formParams);
        restSource.setPath(path);
        return restSource;
    }
    
}
