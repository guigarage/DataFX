package io.datafx.crud.jpa;

import io.datafx.crud.util.EntityWithId;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.function.Supplier;

public class SimpleJpaCrudService<S extends EntityWithId<T>, T extends Serializable> extends JpaCrudService<S, T> {

    private CreateOnceSupplier<EntityManagerFactory> factorySupplier;

    private boolean closeFactoryOnDestroy;

    public SimpleJpaCrudService(EntityManagerFactory factory, Class<S> entityClass) {
        this(factory, entityClass, true);
    }

    public SimpleJpaCrudService(EntityManagerFactory factory, Class<S> entityClass, boolean closeFactoryOnDestroy) {
        this(() -> factory, entityClass, closeFactoryOnDestroy);
    }

    public SimpleJpaCrudService(Supplier<EntityManagerFactory> factorySupplier, Class<S> entityClass) {
        this(new CreateOnceSupplier<EntityManagerFactory>(factorySupplier), entityClass, true);
    }

    public SimpleJpaCrudService(Supplier<EntityManagerFactory> factorySupplier, Class<S> entityClass, boolean closeFactoryOnDestroy) {
        this(new CreateOnceSupplier<EntityManagerFactory>(factorySupplier), entityClass, closeFactoryOnDestroy);
    }

    public SimpleJpaCrudService(CreateOnceSupplier<EntityManagerFactory> factorySupplier, Class<S> entityClass, boolean closeFactoryOnDestroy) {
        super(() -> factorySupplier.get().createEntityManager(), entityClass);
        this.closeFactoryOnDestroy = closeFactoryOnDestroy;
        this.factorySupplier =  factorySupplier;
    }

    public SimpleJpaCrudService(String persistenceUnitName, Class<S> entityClass) {
        this(() -> Persistence.createEntityManagerFactory("DataFX-Samples"), entityClass, false);
    }

    public void destroy() {
        getEntityManager().close();
        if(closeFactoryOnDestroy) {
            EntityManagerFactory f = factorySupplier.getCreatedInstance();
            if(f != null) {
                f.close();
            }
        }
    }
}
