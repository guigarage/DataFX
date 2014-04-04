package org.datafx.crud.jpa;

import org.datafx.crud.EntityWithId;
import org.datafx.crud.api.BasicCrudService;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class JpaCrudService<S extends EntityWithId<T>, T extends Serializable> extends BasicCrudService<S, T> {

    public JpaCrudService(EntityManager manager, Class<S> entityClass) {
        super(new JpaGetAllCall<S, T>(manager),
                new JpaGetByIdCall<S, T>(manager, entityClass),
                new JpaDeleteCall<S, T>(manager, entityClass),
                new JpaPersistCall<S, T>(manager),
                new JpaUpdateCall<S, T>(manager));
    }

}
