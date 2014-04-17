package org.datafx.crud.rest;

import org.datafx.crud.BasicCrudService;
import org.datafx.util.EntityWithId;
import org.datafx.util.HttpMethods;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RestCrudService<S extends EntityWithId<T>, T> extends BasicCrudService<S, T> {

    public RestCrudService(final String baseUrl) {
        super(new RestCall<Void, List<S>>((o) -> createUrl(baseUrl + "/all"), HttpMethods.GET, null, new JsonBasedResponseDataHandler<>()),
                new RestCall<T, S>((o) -> createUrl(baseUrl + "/" + o), HttpMethods.GET, null, new JsonBasedResponseDataHandler<>()),
                new RestCall<T, Void>((o) -> createUrl(baseUrl + "/" + o), HttpMethods.DELETE, null, null),
                new RestCall<S, S>((o) -> createUrl(baseUrl), HttpMethods.PUT, new JsonBasedRequestDataHandler<>(), new JsonBasedResponseDataHandler<>()),
                new RestCall<S, S>((o) -> createUrl(baseUrl), HttpMethods.POST, new JsonBasedRequestDataHandler<>(), new JsonBasedResponseDataHandler<>())
                );
    }

    private static URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("TODO");
        }
    }
}
