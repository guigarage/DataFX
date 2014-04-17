package org.datafx.crud.rest;

import org.datafx.util.Call;
import org.datafx.util.HttpMethods;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

public class RestCall<S, T> implements Call<S, T> {

    private Function<S, URL> urlFactory;

    private HttpMethods httpMethod;

    private RequestDataHandler<S> requestDataProvider;

    private Function<InputStream, T> responseDataHandler;

    public RestCall(Function<S, URL> urlFactory, HttpMethods httpMethod, RequestDataHandler<S> requestDataProvider, Function<InputStream, T> responseDataHandler) {
        this.httpMethod = httpMethod;
        this.requestDataProvider = requestDataProvider;
        this.responseDataHandler = responseDataHandler;
        this.urlFactory = urlFactory;
    }

    public T call(S input) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) urlFactory.apply(input).openConnection();
        connection.setRequestMethod(httpMethod.toString());

        if(requestDataProvider != null) {
            requestDataProvider.writeData(input, connection.getOutputStream());
        }

        if(responseDataHandler != null) {
            return responseDataHandler.apply(connection.getInputStream());
        }
        return null;
    }
}
