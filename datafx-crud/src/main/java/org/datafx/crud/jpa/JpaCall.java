package org.datafx.crud.jpa;

import org.datafx.util.Call;

import javax.persistence.EntityManager;
import java.util.function.Supplier;

public abstract class JpaCall<S, T> implements Call<S, T>{

    private Supplier<EntityManager> managerSupplier;

    public JpaCall(Supplier<EntityManager> managerSupplier) {
          this.managerSupplier = managerSupplier;
    }

    public JpaCall(EntityManager manager) {
        this(() -> manager);
    }

    public EntityManager getManager() {
        return managerSupplier.get();
    }
}
