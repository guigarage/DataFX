package org.datafx.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.datafx.reader.converter.InputStreamConverter;

/**
 *
 * @author johan
 */
public class RestSource <T> extends InputStreamDataReader<T> {
    
 //   private InputStreamConverter<T> converter;
    private String host;
    private String path;
    private String urlBase;
    private String consumerKey;
    private String consumerSecret;
    private boolean requestMade;
    private Map<String, String> requestProperties;
    private Map<String, String> queryParams = new HashMap<String, String>();
    private MultiValuedMap formParams = new MultiValuedMap();
    private String dataString;
    private String requestMethod = "GET";
    private StringBuilder queryString;
 //   private InputStream is;
    
    public RestSource(String host,InputStreamConverter<T> converter) {
        super(converter);
        this.host = host;
        //this.converter= converter;
    }
    
    /**
     * Append the provided path segment to the path
     * @param p
     * @return 
     */
    public RestSource path (String p) {
        if (this.path == null) {
            this.path ="";
        }
        this.path = this.path + "/"+ p;
        
        return this;
    }
    
    /**
     * Explicitly sets the path for this resource. 
     * @param path the path. If null, the path will be empty
     */
    public void setPath(String path) {
        if (path == null) {
            this.path = "";
        }
        else {
            if (path.startsWith("/")) {
                this.path = path;
            }
            else {
                this.path = "/"+path;
            }
        }
    }
    
    private synchronized void createRequest () {
        try {
            if (requestMade) {
                return;
            }
            setInputStream(createInputStream());
            requestMade = true;
        } catch (IOException ex) {
            Logger.getLogger(RestSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override public T get() {
        if (!requestMade) {
            createRequest();
        }
        return super.get();
    }

    @Override public boolean next() {
        if (!requestMade) {
            createRequest();
        }
        return super.next();
    }
    
     public InputStream createInputStream() throws IOException  {
         String urlBase = host + path;
        try {
            
            String request = urlBase;
             if (queryString != null) {
                request = request + "?" + queryString;
            }
            URL url = new URL(request);

            URLConnection connection = url.openConnection();
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
                ((HttpURLConnection) connection).setRequestMethod(getRequestMethod());
            }
            if (getQueryParams() != null) {
                for (Map.Entry<String, String> requestProperty : getQueryParams().entrySet()) {
                    connection.addRequestProperty(requestProperty.getKey(), requestProperty.getValue());
                }
            }
            if ((getFormParams() != null) && (getFormParams().size() > 0)) {
                if (dataString == null) {
                    dataString="";
                }
                boolean first = true;
                for (Map.Entry<String, List<String>> entryList : getFormParams().entrySet()) {
                    String key = entryList.getKey();
                    for (String val : entryList.getValue()) {
                        if (!first) {
                            dataString = dataString + "&";
                        } else {
                            first = false;
                        }
                        dataString = dataString + key + "=" + val;
                    }
                }
    //            for (Map.Entry<String, String> entry : getFormParams().entrySet()) {
    //                if (!first) {
    //                    dataString = dataString+ "&";
    //                } else {first = false;}
    //                dataString = dataString+entry.getKey()+"="+entry.getValue();
    //            }
            }

            if (getDataString() != null) {
                connection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(getDataString());
                outputStreamWriter.close();
            }

            InputStream is = connection.getInputStream();
            return is;
        } catch (IOException ex) {
            throw new IOException("Can't read data from "+urlBase, ex);
        }
    }

    /**
     * @return the consumerKey
     */
    public String getConsumerKey() {
        return consumerKey;
    }

    /**
     * @param consumerKey the consumerKey to set
     */
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    /**
     * @return the consumerSecret
     */
    public String getConsumerSecret() {
        return consumerSecret;
    }

    /**
     * @param consumerSecret the consumerSecret to set
     */
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    /**
     * @return the requestProperties
     */
    public Map<String, String> getRequestProperties() {
        return requestProperties;
    }

    /**
     * @param requestProperties the requestProperties to set
     */
    public void setRequestProperties(Map<String, String> requestProperties) {
        this.requestProperties = requestProperties;
    }

    /**
     * @return the queryParams
     */
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    /**
     * @param queryParams the queryParams to set
     */
    public void setQueryParams(Map<String, String> queryParams) {
       for (Entry<String,String> entry: queryParams.entrySet()) {
           queryParam(entry.getKey(), entry.getValue());
       }
    }
    
    public RestSource queryParam(String key, String value) {
        this.queryParams.put(key, value); 
        if (queryString == null) {
            queryString = new StringBuilder(key).append("=").append(value);
        } else {
            queryString.append("&").append(key).append("=").append(value);
        }
        return this;
    }

    /**
     * @return the formParams
     */
    public MultiValuedMap getFormParams() {
        return formParams;
    }

    /**
     * @param formParams the formParams to set
     */
    public void setFormParams(Map<String, String> p) {
        
        this.formParams = new MultiValuedMap();
        for (Map.Entry<String, String> entry: p.entrySet()) {
            this.formParams.put(entry.getKey(), entry.getValue());
        }
    }
    
    public void setFormParams(MultiValuedMap formParams) {
        this.formParams = formParams;
    }

    /**
     * @return the dataString
     */
    public String getDataString() {
        return dataString;
    }

    /**
     * @param dataString the dataString to set
     */
    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    /**
     * @return the requestMethod
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * @param requestMethod the requestMethod to set
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    
    // fluent methods
    public RestSource consumerKey (String key) {
        setConsumerKey(key);
        return this;
    }
    
    public RestSource consumerSecret (String secret) {
        setConsumerSecret(secret);
        return this;
    }

    
}
