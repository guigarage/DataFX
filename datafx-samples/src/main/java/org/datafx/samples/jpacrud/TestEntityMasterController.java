package org.datafx.samples.jpacrud;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.datafx.controller.FXMLController;
import org.datafx.crud.CrudListProperty;
import org.datafx.crud.table.TableColumnFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@FXMLController("master.fxml")
public class TestEntityMasterController {

    @FXML
    TableView<TestEntity> dataTable;

    @Inject
    private TestEntityCrudService crudService;

    @PostConstruct
    public void init() {
        CrudListProperty<TestEntity, Long> data = new CrudListProperty(crudService);

        dataTable.getColumns().setAll(TableColumnFactory.createColumns(TestEntity.class));
        dataTable.setItems(data.getImmutableEntityList());

        TestEntity e = new TestEntity();
        e.setName("HALLO");
        data.addAndSave(e);
    }
}
