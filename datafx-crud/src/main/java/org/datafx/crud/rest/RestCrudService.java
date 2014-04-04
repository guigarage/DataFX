package org.datafx.crud.rest;

import org.datafx.crud.api.BasicCrudService;
import org.datafx.crud.EntityWithId;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RestCrudService<S extends EntityWithId<T>, T> extends BasicCrudService<S, T> {

    public RestCrudService(final String baseUrl) {
        super(new RestCall<Void, List<S>>((o) -> createUrl(baseUrl + "/all"), HttpMethod.GET, null, new JsonBasedResponseDataHandler<>()),
                new RestCall<T, S>((o) -> createUrl(baseUrl + "/" + o), HttpMethod.GET, null, new JsonBasedResponseDataHandler<>()),
                new RestCall<T, Void>((o) -> createUrl(baseUrl + "/" + o), HttpMethod.DELETE, null, null),
                new RestCall<S, S>((o) -> createUrl(baseUrl), HttpMethod.PUT, new JsonBasedRequestDataHandler<>(), new JsonBasedResponseDataHandler<>()),
                new RestCall<S, S>((o) -> createUrl(baseUrl), HttpMethod.POST, new JsonBasedRequestDataHandler<>(), new JsonBasedResponseDataHandler<>())
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
