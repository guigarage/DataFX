package org.datafx.samples.jpacrud;

import org.datafx.controller.flow.injection.FlowScoped;
import org.datafx.controller.injection.Async;
import org.datafx.crud.jpa.SimpleJpaCrudService;

import javax.annotation.PreDestroy;

@Async
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
