package org.datafx.crud;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.collections.transformation.TransformationList;
import javafx.concurrent.Worker;
import org.datafx.concurrent.ConcurrentUtils;
import org.datafx.concurrent.ObservableExecutor;
import org.datafx.util.EntityWithId;
import org.datafx.util.QueryParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class CrudListProperty<S extends EntityWithId<T>, T> extends ModifiableObservableListBase<CrudObjectProperty<S, T>> {

    private CrudService<S, T> crudService;
    private Executor executor;
    private List<CrudObjectProperty<S, T>> innerList;
    private ObservableList<S> immutableEntityList;

    public CrudListProperty(CrudService<S, T> crudService) {
        this(crudService, ObservableExecutor.getDefaultInstance());
    }

    public CrudListProperty(CrudService<S, T> crudService, Executor executor) {
        this.crudService = crudService;
        this.executor = executor;
        innerList = new ArrayList<>();
    }

    public ObservableList<S> getImmutableEntityList() {
        if (immutableEntityList == null) {
            immutableEntityList = new TransformationList<S, CrudObjectProperty<S, T>>(this) {

                @Override
                public boolean addAll(S... elements) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean setAll(S... elements) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean setAll(Collection<? extends S> col) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean removeAll(S... elements) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public boolean retainAll(S... elements) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void remove(int from, int to) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public S get(int index) {
                    return CrudListProperty.this.get(index).get();
                }

                @Override
                public int size() {
                    return CrudListProperty.this.size();
                }

                @Override
                protected void sourceChanged(ListChangeListener.Change<? extends CrudObjectProperty<S, T>> c) {
                    beginChange();
                    while (c.next()) {
                        if (c.wasPermutated()) {
                            int[] perm = new int[c.getTo() - c.getFrom() + 1];
                            int permIndex = 0;
                            for(int i = c.getFrom(); i <= c.getTo(); i++) {
                                perm[permIndex] = i;
                                permIndex++;
                            }
                            nextPermutation(c.getFrom(), c.getTo(), perm);
                        } else if (c.wasUpdated()) {
                            nextUpdate(c.getFrom());
                        } else {
                            if(c.wasReplaced()) {
                                List<S> replacedItems = new ArrayList<>();
                                for(CrudObjectProperty<S, T> removedProperty : c.getRemoved()) {
                                    replacedItems.add(removedProperty.get());
                                }
                                nextReplace(c.getFrom(), c.getTo(), replacedItems);
                            } else if(c.wasAdded()) {
                                nextAdd(c.getFrom(), c.getTo());
                            } else if(c.wasRemoved()) {
                                List<S> removedItems = new ArrayList<>();
                                for(CrudObjectProperty<S, T> removedProperty : c.getRemoved()) {
                                    removedItems.add(removedProperty.get());
                                }
                                nextRemove(c.getFrom(), removedItems);
                            }
                        }
                    }
                    endChange();
                }

                @Override
                public int getSourceIndex(int index) {
                    return index;
                }
            };
        }
        return immutableEntityList;
    }

    public Worker<List<CrudObjectProperty<S, T>>> reload() {
        return innerReload(() -> crudService.getAll());
    }

    public Worker<List<CrudObjectProperty<S, T>>> reloadByQuery(String queryName, QueryParameter... params) {
        return innerReload(() -> crudService.query(queryName, params));
    }

    private Worker<List<CrudObjectProperty<S, T>>> innerReload(Callable<List<S>> supplier) {
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

    public void addAndSave(S entity) {
        CrudObjectProperty<S, T> p = new CrudObjectProperty<>(entity, this, crudService);
        p.save();
        add(p);
        //return ProcessChain.create().addFunctionInPlatformThread((Supplier<Worker<S>>) () -> p.save()).addFunctionInExecutor((Consumer<Worker<S>>) (w) -> w.isRunning()).addFunctionInExecutor((Runnable) () -> addFunction(p)).run();
    }
}
