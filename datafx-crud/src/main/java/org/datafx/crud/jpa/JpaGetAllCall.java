package org.datafx.crud.jpa;

import org.datafx.util.EntityWithId;

import javax.persistence.EntityManager;
import java.util.List;

public class JpaGetAllCall<S extends EntityWithId<T>, T> extends JpaCall<Void, List<S>> {

    public JpaGetAllCall(EntityManager manager) {
        super(manager);
    }

    @Override
    public List<S> call(Void dummy) throws Exception {
        //TODO
        return null;
    }
}
