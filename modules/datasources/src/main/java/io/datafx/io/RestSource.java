/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of DataFX, the website
 * javafxdata.org, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.datafx.io.converter.InputStreamConverter;

/**
 * Client class that is used to create a request to an external REST endpoint.
 *
 * @param <T> the type of the data that is expected from the REST endpoint.
 */
public class RestSource<T> extends InputStreamDataReader<T> implements WritableDataReader<T> {

    private String host;
    // path should never be null. We check on this in all methods that can change the path.
    private String path = "";
    private String consumerKey;
    private String consumerSecret;
    protected boolean requestMade;
    private Map<String, String> requestProperties;
    private Map<String, String> queryParams = new HashMap<>();
    private MultiValuedMap formParams = new MultiValuedMap();
    private String dataString;
    private String requestMethod = "GET";
    private int timeout = -1;
    private String contentType;

    private int responseCode = -1;
    private String responseMessage;

    private static final Logger LOGGER = Logger.getLogger(RestSource.class.getName());

    /**
     * Create a new RestSource
     */
    public RestSource() {
    }

    /**
     * Create a new RestSource and specifies the endpoint and the converter
     *
     * @param host the host parameter of the endpoint
     * @param converter the {@link Converter} that will be used to convert the
     * raw data into Java objects of type T.
     */
    public RestSource(String host, InputStreamConverter<T> converter) {
        super(converter);
        this.host = host;
        //this.converter= converter;
    }

    /**
     * Set the host parameter for this endpoint.
     *
     * @param host the host parameter for this endpoint.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Explicitly sets the path for this resource.
     *
     * @param path the path. If null, the path will be the empty String
     */
    public void setPath(String path) {
        if (path == null) {
            this.path = "";
        } else {
            if (path.startsWith("/")) {
                this.path = path;
            } else {
                this.path = "/" + path;
            }
        }
    }

    protected synchronized void createRequest() throws IOException {
        if (requestMade) {
            return;
        }
        setInputStream(createInputStream());
        requestMade = true;
    }

    @Override
    public T get() throws IOException {
        LOGGER.log(Level.FINE, "[datafx] restsource will get a value, requestMade = {0}", requestMade);
        if (!requestMade) {
            createRequest();
        }
        if (getConverter() != null) {
            return super.get();
        } else {
            return null;
        }
    }

    @Override
    public boolean next() {
        if (!requestMade) {
            try {
                createRequest();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        return super.next();
    }

    /**
     * Create the InputStream for this request. This method does all the
     * required initialization, and passes the appropriate parameters. When the
     * server responds with an HTTP error code, the returned InputStream will be
     * the {@link HttpURLConnection#getErrorStream() errorStream of the URL
     * connection} instead and no IOException will be thrown. This allows the
     * coupled Converter to continue parsing the response from the server.
     *
     * @return the created {@link java.io.InputStream}
     * @throws IOException in case the InputStream cannot be created
     * successfully.
     */
    public InputStream createInputStream() throws IOException {
        String urlBase = host + path;

        String request = urlBase;
        String queryString = createQueryString();
        if (queryString != null) {
            request = request + "?" + queryString;
        }
        URL url = new URL(request);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (getConsumerKey() != null) {
            try {
                MultiValuedMap allParams = new MultiValuedMap();
                allParams.putMap(getQueryParams());
                allParams.putAll(getFormParams());
                String header = OAuth.getHeader(getRequestMethod(), urlBase, allParams, getConsumerKey(), getConsumerSecret());
                connection.addRequestProperty("Authorization", header);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(RestSource.class.getName()).log(Level.SEVERE, null, ex);
                } catch (GeneralSecurityException ex) {
                    Logger.getLogger(RestSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (getRequestMethod() != null) {
            connection.setRequestMethod(getRequestMethod());
        }
        if (timeout > -1) {
            connection.setReadTimeout(timeout);
            connection.setConnectTimeout(timeout);
        }
        if (getQueryParams() != null) {
            for (Map.Entry<String, String> requestProperty : getQueryParams().entrySet()) {
                connection.addRequestProperty(requestProperty.getKey(), requestProperty.getValue());
            }
        }
        if ((getFormParams() != null) && (getFormParams().size() > 0)) {
            if (dataString == null) {
                dataString = "";
            }
            boolean first = true;
            for (Map.Entry<String, List<String>> entryList : getFormParams().entrySet()) {
                String key = entryList.getKey();
                for (String val : entryList.getValue()) {
                    if (val == null) {
                        throw new IllegalArgumentException("Values in form parameters can't be null -- was null for key " + key);
                    }
                    if (!first) {
                        dataString = dataString + "&";
                    } else {
                        first = false;
                    }
                    String eval = URLEncoder.encode(val, "UTF-8");
                    dataString = dataString + key + "=" + eval;
                }
            }

        }
        if (getDataString() != null) {
            connection.setDoOutput(true);
            if (contentType == null) {
                contentType = "application/x-www-form-urlencoded";
            }
            connection.setRequestProperty("Content-Type", contentType);
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream())) {
                outputStreamWriter.write(getDataString());
            }
        }

        // catch the IOException when getting the InputStream and use the
        // errorStream from the HttpUrlConnection as the InputStream to return
        InputStream is;
        try {
            is = connection.getInputStream();
        } catch (IOException ex) {
            is = connection.getErrorStream();
        }

        // try to get the response code and response message that was returned
        // from the server. when this code is not available, the original
        // IOException will be thrown instead
        this.responseCode = connection.getResponseCode();
        this.responseMessage = connection.getResponseMessage();

        return is;
    }

    /**
     * Return the consumer key (used with oauth) for this request
     *
     * @return the consumerKey
     */
    public String getConsumerKey() {
        return consumerKey;
    }

    /**
     * Set the consumer key for this request.
     *
     * @param consumerKey the consumerKey to set
     */
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    /**
     * Return the consumer secret (if used with oauth) for this request
     *
     * @return the consumerSecret
     */
    public String getConsumerSecret() {
        return consumerSecret;
    }

    /**
     * Set the consumer secret
     *
     * @param consumerSecret the consumerSecret to set
     */
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    /**
     * Return the request properties as a {@link Map}
     *
     * @return the requestProperties
     */
    public Map<String, String> getRequestProperties() {
        return requestProperties;
    }

    /**
     * Set the request properties as a {@link Map}
     *
     * @param requestProperties the requestProperties to set
     */
    public void setRequestProperties(Map<String, String> requestProperties) {
        this.requestProperties = requestProperties;
    }

    /**
     * Return the query parameters as a {@link Map}
     *
     * @return the queryParams
     */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * Set the query parameters as a {@link Map}
     *
     * @param queryParams the queryParams to set. If null, this call is ignored
     */
    public void setQueryParams(Map<String, String> queryParams) {
        if (queryParams != null) {
            this.queryParams = queryParams;
        }
    }

    private String createQueryString() {
        StringBuilder queryString = null;
        if (queryParams.isEmpty()) {
            return null;
        }
        for (Entry<String, String> entry : queryParams.entrySet()) {
            if (queryString == null) {
                queryString = new StringBuilder(entry.getKey()).append("=").append(entry.getValue());
            } else {
                queryString.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return queryString.toString();
    }

    /**
     * Return a {@link MultiValuedMap} for the form parameters in this request.
     *
     * @return the formParams
     */
    public MultiValuedMap getFormParams() {
        return formParams;
    }

    /**
     * Set the form parameters as a {@link Map}. This is a convenience method,
     * as form parameters can also be passed as a {@link MultiValuedMap}
     *
     * @param formParams the formParams to set
     */
    public void setFormParams(Map<String, String> formParams) {

        this.formParams = new MultiValuedMap();
        for (Map.Entry<String, String> entry : formParams.entrySet()) {
            this.formParams.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Set the form parameters as a {@link MultiValuedMap}
     *
     * @param formParams the formParams to set
     */
    public void setFormParams(MultiValuedMap formParams) {
        this.formParams = formParams;
    }

    /**
     * Return the datastring that will be send with this request
     *
     * @return the dataString
     */
    public String getDataString() {
        return dataString;
    }

    /**
     * Explicitly set the datastring for this request. This is useful for POST
     * requests with a body.
     *
     * @param dataString the dataString to set
     */
    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    /**
     * Return the request method for this request.
     *
     * @return the requestMethod
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * Set the request method for this request as a {@link java.lang.String}
     * (e.g. "POST", "GET")
     *
     * @param requestMethod the requestMethod to set
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public void writeBack() {
        try {
            createInputStream();
        } catch (Exception ex) {
            Logger.getLogger(RestSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the timeout value specified for this instance
     *
     * @return the timeout value
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout value for this instance.
     *
     * @param timeout the timeout value
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * Set the contentType for this request. If the contentType is not set, and
     * form parameters or a post body is used, the contentType will be set to
     * "application/x-www-form-urlencoded"
     *
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets the status code from an HTTP response message.
     *
     * @return the HTTP Status-Code, or -1
     * @see HttpURLConnection#getResponseCode()
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Gets the HTTP response message, if any, returned along with the response
     * code from a server.
     *
     * @return the HTTP response message, or <code>null</code>
     * @see HttpURLConnection#getResponseMessage()
     */
    public String getResponseMessage() {
        return responseMessage;
    }

}
