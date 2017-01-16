package io.datafx.crud;

import io.datafx.crud.util.EntityWithId;
import io.datafx.crud.util.QueryParameter;

import java.util.List;

public interface CrudService<S extends EntityWithId<T>, T> {

    public void delete(S entity) throws CrudException;

    public S save(S entity) throws CrudException;

    public List<S> getAll() throws CrudException;

    public S getById(T id) throws CrudException;

    public List<S> query(String name, QueryParameter... params) throws CrudException;
}
