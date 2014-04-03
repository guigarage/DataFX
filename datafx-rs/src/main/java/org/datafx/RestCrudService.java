package org.datafx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RestCrudService<S extends EntityWithId<T>, T> {

    private RestCall<Void, List<S>> getAllCall;

    private RestCall<T, S> getByIdCall;

    private RestCall<T, Void> deleteByIdCall;

    private RestCall<S, Void> persistCall;

    private RestCall<S, Void> updateCall;

    public RestCrudService(final String baseUrl) {
        getAllCall = new RestCall<Void, List<S>>((o) -> createUrl(baseUrl + "/all"), HttpMethod.GET, null, new JsonBasedResponseDataHandler<>());
        getByIdCall = new RestCall<T, S>((o) -> createUrl(baseUrl + "/" + o), HttpMethod.GET, null, new JsonBasedResponseDataHandler<>());
        deleteByIdCall = new RestCall<T, Void>((o) -> createUrl(baseUrl + "/" + o), HttpMethod.DELETE, null, null);
        persistCall = new RestCall<S, Void>((o) -> createUrl(baseUrl), HttpMethod.PUT, new JsonBasedRequestDataHandler<>(), null);
        updateCall = new RestCall<S, Void>((o) -> createUrl(baseUrl), HttpMethod.POST, new JsonBasedRequestDataHandler<>(), null);
    }

    public void delete(S entity) throws IOException {
        T id = entity.getId();
        if (id == null) {
            throw new RuntimeException("TODO");
        }
        deleteByIdCall.call(id);
    }

    public void save(S entity) throws IOException {
        if (entity.getId() == null) {
            persistCall.call(entity);
        } else {
            updateCall.call(entity);
        }
    }

    public List<S> getAll() throws IOException {
        return getAllCall.call(null);
    }

    public S getById(T id) throws IOException {
        return getByIdCall.call(id);
    }

    private static URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("TODO");
        }
    }
}
