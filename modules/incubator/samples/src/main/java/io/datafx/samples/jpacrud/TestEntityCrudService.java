package io.datafx.samples.jpacrud;

import io.datafx.controller.injection.scopes.FlowScoped;
import io.datafx.crud.jpa.SimpleJpaCrudService;

import javax.annotation.PreDestroy;

@FlowScoped
public class TestEntityCrudService extends SimpleJpaCrudService<TestEntity, Long> {

    public TestEntityCrudService() {
        super("DataFX-Samples", TestEntity.class);
    }

    @PreDestroy
    public void destroy() {
          super.destroy();
    }
}
