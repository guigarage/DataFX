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
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.reader;

import java.util.HashMap;
import java.util.Map;
import org.datafx.reader.converter.InputStreamConverter;

/**
 *
 * Builder class for creating an {@link RestSource}.
 * @author johan
 */
public  class RestSourceBuilder<T> {
    
    protected  RestSource<T> restSource;
    private String path = "";
    private Map<String, String> queryParams = new HashMap<>();
    private MultiValuedMap formParams = new MultiValuedMap();
    
    public static<T> RestSourceBuilder create() {
        return new RestSourceBuilder();
    }
    
    protected RestSourceBuilder() {
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
    
    public RestSourceBuilder contentType (String contentType) {
        restSource.setContentType(contentType);
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
