package org.datafx.crud;

import org.datafx.util.Call;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicCrudService<S extends EntityWithId<T>, T> implements CrudService<S, T> {

    private Call<Void, List<S>> getAllCall;

    private Call<T, S> getByIdCall;

    private Call<T, Void> deleteByIdCall;

    private Call<S, S> persistCall;

    private Call<S, S> updateCall;

    private Map<String, Call<List<QueryParameter>, List<S>>> queries;

    public BasicCrudService(Call<Void, List<S>> getAllCall, Call<T, S> getByIdCall, Call<T, Void> deleteByIdCall, Call<S, S> persistCall, Call<S, S> updateCall) {
        this.deleteByIdCall = deleteByIdCall;
        this.getAllCall = getAllCall;
        this.getByIdCall = getByIdCall;
        this.persistCall = persistCall;
        this.updateCall = updateCall;
        queries = new HashMap<>();
    }

    @Override
    public void delete(S entity) throws CrudException {
        T id = entity.getId();
        if (id == null) {
            throw new CrudException("TODO");
        }
        try {
            deleteByIdCall.call(id);
        } catch (Exception e) {
            throw new CrudException("TODO", e);
        }
    }

    @Override
    public S save(S entity) throws CrudException {
        if (entity.getId() == null) {
            try {
                return persistCall.call(entity);
            } catch (Exception e) {
                throw new CrudException("TODO", e);
            }
        } else {
            try {
                return updateCall.call(entity);
            } catch (Exception e) {
                throw new CrudException("TODO", e);
            }
        }
    }

    @Override
    public List<S> getAll() throws CrudException {
        try {
            return getAllCall.call(null);
        } catch (Exception e) {
            throw new CrudException("TODO", e);
        }
    }

    @Override
    public S getById(T id) throws CrudException {
        try {
            return getByIdCall.call(id);
        } catch (Exception e) {
            throw new CrudException("TODO", e);
        }
    }

    public void addQuery(String name, Call<List<QueryParameter>, List<S>> query) {
        queries.put(name, query);
    }

    @Override
    public List<S> query(String name, QueryParameter... params) throws CrudException {
        Call<List<QueryParameter>, List<S>> queryCall = queries.get(name);
        if(queryCall == null) {
            throw new CrudException("TODO");
        }
        List<QueryParameter> paramList = Arrays.asList(params);
        try {
            return queryCall.call(paramList);
        } catch (Exception e) {
            throw new CrudException("TODO", e);
        }
    }
}
