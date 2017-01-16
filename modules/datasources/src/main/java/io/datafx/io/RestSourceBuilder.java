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
import java.util.Map;
import io.datafx.io.converter.InputStreamConverter;

/**
 * Builder class for creating an {@link RestSource}.
 * @param <T>  the type of the data that is expected from the REST endpoint.
 */
public  class RestSourceBuilder<T> {
    
    protected  RestSource<T> restSource;
    private String path = "";
    private Map<String, String> queryParams = new HashMap<>();
    private MultiValuedMap formParams = new MultiValuedMap();
    
    /**
     * Create a RestSourceBuilder in order to create a 
     * {@link RestSource}
     * @param <T>  the type of the data that is expected from the REST endpoint.
     * @return This RestSourceBuilder
     */
    public static<T> RestSourceBuilder create() {
        return new RestSourceBuilder();
    }
    
    protected RestSourceBuilder() {
        this.restSource = new RestSource();
    }
    
    /**
     * Set the converter that will convert the raw data into object(s) of type T.
     * @param converter  the {@link Converter} that will be used to convert raw
     * data into object(s) of type T
     * @return This RestSourceBuilder
     */
    public RestSourceBuilder converter(InputStreamConverter<T> converter) {
        this.restSource.setConverter(converter);
        return this;
    }
    
    /**
     * Add a path element to the path that ultimately will point to the
     * endpoint.
     * @param p  the path element
     * @return This RestSourceBuilder
     */
   public RestSourceBuilder path (String p) {
        this.path = this.path + "/"+ p;
        return this;
    }
        
    /**
     * Set the request method for this request as a 
     * {@link java.lang.String} (e.g. "POST", "GET")
     * @param requestMethod  the requestMethod to set
     * @return This RestSourceBuilder
     */
   public RestSourceBuilder requestMethod (String requestMethod) {
       this.restSource.setRequestMethod(requestMethod);
       return this;
   }
           
   /**
    * Add a specific query parameter
    * @param key the key for the query parameter
    * @param value the value for the query parameter
    * @return This RestSourceBuilder
    */
   public RestSourceBuilder queryParam(String key, String value) {
        this.queryParams.put(key, value); 
        return this;
    }
    
   /**
    * Add a specific form parameter
    * @param key the key for the form parameter
    * @param value the value for the form parameter
    * @return This RestSourceBuilder
    */
    public RestSourceBuilder formParam (String key, String value) {
        this.formParams.put(key, value);
        return this;
    }
        
    /**
     * Explicitly set the datastring for this request. This is
     * useful for POST requests with a body.
     * 
     * @param dataString  the dataString to set
     * @return This RestSourceBuilder
     */
    public RestSourceBuilder dataString (String dataString) {
        this.restSource.setDataString(dataString);
        return this;
    }
    
    
    /**
     * Set the consumer key for this request.
     * 
     * @param consumerKey  the consumerKey to set
     * @return This RestSourceBuilder
     */
    public RestSourceBuilder consumerKey (String consumerKey) {
        restSource.setConsumerKey(consumerKey);
        return this;
    }
    
    /**
     * Set the consumer secret for this request.
     * 
     * @param consumerSecret  the consumer secret to set
     * @return This RestSourceBuilder
     */
    public RestSourceBuilder consumerSecret (String consumerSecret) {
        restSource.setConsumerSecret(consumerSecret);
        return this;
    }
    
    /**
     * Set the contentType for this request. If the contentType is not set, and
     * form parameters or a post body is used, the contentType will be set to
     * "application/x-www-form-urlencoded"
     *
     * @param contentType the contentType to set
     * @return This RestSourceBuilder
     */
    public RestSourceBuilder contentType (String contentType) {
        restSource.setContentType(contentType);
        return this;
    }


    /**
     * Set the host parameter for this endpoint.
     * @param host  the host parameter for this endpoint.
     * @return This RestSourceBuilder
     */
    public RestSourceBuilder host (String host) {
        restSource.setHost(host);
        return this;
    }
    
    
    /**
     * Set the timeout value for this instance. 
     * @param timeout the timeout value
     * @return This RestSouceBuilder
     */
    public RestSourceBuilder timeout(int timeout) {
        restSource.setTimeout(timeout);
        return this;
    }
    
    /**
     * Create the RestSource based on all parameters supplied on this
     * RestSource Builder
     * @return  the created {@link RestSource}
     */
    public RestSource build () {
        restSource.setQueryParams(queryParams);
        restSource.setFormParams(formParams);
        restSource.setPath(path);
        return restSource;
    }
    
}
