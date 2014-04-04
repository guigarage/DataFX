package org.datafx.crud;

import javafx.collections.FXCollections;
import javafx.collections.ModifiableObservableListBase;
import javafx.concurrent.Worker;
import org.datafx.concurrent.ConcurrentUtils;
import org.datafx.concurrent.ObservableExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class CrudListProperty<S extends EntityWithId<T>, T> extends ModifiableObservableListBase<CrudObjectProperty<S, T>> {

    private CrudService<S, T> crudService;

    private Executor executor;

    private List<CrudObjectProperty<S, T>> innerList;

    public CrudListProperty(CrudService<S, T> crudService) {
        this(crudService, ObservableExecutor.getDefaultInstance());
    }

    public CrudListProperty(CrudService<S, T> crudService, Executor executor) {
        this.crudService = crudService;
        this.executor = executor;
        innerList = new ArrayList<>();
    }

    public Worker<List<CrudObjectProperty<S, T>>> update() {
        return innerUpdate(() -> crudService.getAll());
    }

    public Worker<List<CrudObjectProperty<S, T>>> updateByQuery(String queryName, QueryParameter... params) {
        return innerUpdate(() -> crudService.query(queryName, params));
    }

    private Worker<List<CrudObjectProperty<S, T>>> innerUpdate(Callable<List<S>> supplier) {
        return ConcurrentUtils.executeService(executor, ConcurrentUtils.createService(() -> {
            try {
                List<CrudObjectProperty<S, T>> newProperties = FXCollections.observableArrayList();
                List<S> dataList = supplier.call();
                for (S data : dataList) {
                    newProperties.add(new CrudObjectProperty<S, T>(data, CrudListProperty.this, crudService, executor));
                }
                ConcurrentUtils.runAndWait(() -> setAll(newProperties));
                return newProperties;
            } catch (Exception e) {
                throw new RuntimeException("TODO", e);
            }
        }));
    }

    @Override
    public CrudObjectProperty<S, T> get(int index) {
        return innerList.get(index);
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    protected void doAdd(int index, CrudObjectProperty<S, T> element) {
        innerList.add(index, element);
    }

    @Override
    protected CrudObjectProperty<S, T> doSet(int index, CrudObjectProperty<S, T> element) {
        return innerList.set(index, element);
    }

    @Override
    protected CrudObjectProperty<S, T> doRemove(int index) {
        return innerList.remove(index);
    }
}
