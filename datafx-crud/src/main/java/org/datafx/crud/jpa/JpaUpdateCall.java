package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;

import javax.persistence.EntityManager;
import java.util.function.Supplier;

public class JpaUpdateCall<S extends EntityWithId<T>, T> extends JpaCall<S, S> {

    public JpaUpdateCall(Supplier<EntityManager> managerSupplier) {
        super(managerSupplier);
    }

    public JpaUpdateCall(EntityManager manager) {
        this(() -> manager);
    }

    @Override
    public S call(S entity) throws Exception {
        return getManager().merge(entity);
    }
}
