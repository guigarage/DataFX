package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;

public class SimpleJpaCrudService<S extends EntityWithId<T>, T extends Serializable> extends JpaCrudService<S, T> {

    public SimpleJpaCrudService(EntityManagerFactory factory, Class<S> entityClass) {
        super(factory.createEntityManager(), entityClass);
    }

    public SimpleJpaCrudService(String persistenceUnitName, Class<S> entityClass) {
        this(Persistence.createEntityManagerFactory("DataFX-Samples"), entityClass);
    }

    public void destroy() {
        getEntityManager().close();
    }
}
