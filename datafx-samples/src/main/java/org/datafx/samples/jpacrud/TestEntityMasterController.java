package org.datafx.samples.jpacrud;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.datafx.crud.CrudListProperty;
import org.datafx.crud.table.TableColumnFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class TestEntityMasterController {

    @FXML
    TableView dataTable;
    @Inject
    private TestEntityCrudService crudService;

    @PostConstruct
    public void init() {
        CrudListProperty data = new CrudListProperty(crudService);
        dataTable.setItems(data);
        data.update();
        dataTable.getColumns().setAll(TableColumnFactory.createColumns(TestEntity.class));
    }
}
