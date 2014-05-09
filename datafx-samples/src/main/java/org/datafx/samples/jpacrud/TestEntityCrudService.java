package org.datafx.samples.jpacrud;

import org.datafx.controller.flow.injection.FlowScoped;
import org.datafx.crud.jpa.JpaCrudService;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@FlowScoped
public class TestEntityCrudService extends JpaCrudService<TestEntity, Long> {

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("DataFX-Samples");

    public TestEntityCrudService() {
        super(factory.createEntityManager(), TestEntity.class);
    }

    @PreDestroy
    public void destroy() {
          getEntityManager().close();
    }
}
