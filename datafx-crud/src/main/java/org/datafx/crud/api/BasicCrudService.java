package org.datafx.crud.api;

import org.datafx.crud.Call;
import org.datafx.crud.CrudService;
import org.datafx.crud.EntityWithId;
import org.datafx.crud.QueryParameter;

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
    public void delete(S entity) throws Exception {
        T id = entity.getId();
        if (id == null) {
            throw new RuntimeException("TODO");
        }
        deleteByIdCall.call(id);
    }

    @Override
    public S save(S entity) throws Exception {
        if (entity.getId() == null) {
            return persistCall.call(entity);
        } else {
            return updateCall.call(entity);
        }
    }

    @Override
    public List<S> getAll() throws Exception {
        return getAllCall.call(null);
    }

    @Override
    public S getById(T id) throws Exception {
        return getByIdCall.call(id);
    }

    public void addQuery(String name, Call<List<QueryParameter>, List<S>> query) {
        queries.put(name, query);
    }

    @Override
    public List<S> query(String name, QueryParameter... params) throws Exception {
        Call<List<QueryParameter>, List<S>> queryCall = queries.get(name);
        if(queryCall == null) {
            throw new RuntimeException("TODO");
        }
        List<QueryParameter> paramList = Arrays.asList(params);
        return queryCall.call(paramList);
    }
}
