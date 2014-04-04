package org.datafx.crud;

import java.util.List;

public interface CrudService<S extends EntityWithId<T>, T> {

    public void delete(S entity) throws Exception;

    public S save(S entity) throws Exception;

    public List<S> getAll() throws Exception;

    public S getById(T id) throws Exception;

    public List<S> query(String name, QueryParameter... params) throws Exception;
}
