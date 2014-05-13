package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;
import org.datafx.util.QueryParameter;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

public class JpaQueryCall<S extends EntityWithId<T>, T> extends JpaCall<List<QueryParameter>, List<S>> {

    private Class<S> entityClass;

    private String query;

    public JpaQueryCall(Supplier<EntityManager> managerSupplier, Class<S> entityClass, String query) {
        super(managerSupplier);
        this.entityClass = entityClass;
        this.query = query;
    }

    public JpaQueryCall(EntityManager manager, Class<S> entityClass, String query) {
        this(() -> manager, entityClass, query);
    }

    @Override
    public List<S> call(List<QueryParameter> params) throws Exception {
        Query queryObject = getManager().createQuery(query);
        for(QueryParameter param : params) {
            queryObject.setParameter(param.getName(), param.getValue());
        }
        return queryObject.getResultList();
    }
}
