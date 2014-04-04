package org.datafx.crud.jpa;

import org.datafx.util.Call;

import javax.persistence.EntityManager;

public abstract class JpaCall<S, T> implements Call<S, T>{

    private EntityManager manager;

    public JpaCall(EntityManager manager) {
          this.manager = manager;
    }

    public EntityManager getManager() {
        return manager;
    }
}
