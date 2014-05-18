package org.datafx.samples.jpacrud;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.datafx.concurrent.ConcurrentUtils;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.crud.CrudListProperty;
import org.datafx.crud.table.TableColumnFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@FXMLController(value = "master.fxml", title = "CRUD")
public class TestEntityMasterController {

    @FXML
    TableView<TestEntity> dataTable;
    @FXML
    @ActionTrigger("addAction")
    private Button addButton;
    @FXML
    @ActionTrigger("reloadAction")
    private Button reloadButton;
    @FXML
    @ActionTrigger("removeAction")
    private Button removeButton;
    @Inject
    private TestEntityCrudService crudService;
    private CrudListProperty<TestEntity, Long> data;

    private BooleanProperty disableUiProperty;

    @PostConstruct
    public void init() {
        initDisableUiProperty();
        data = new CrudListProperty(crudService);
        dataTable.getColumns().setAll(TableColumnFactory.createColumns(TestEntity.class));
        dataTable.setItems(data.getImmutableEntityList());
    }

    @ActionMethod("addAction")
    public void onAddAction() {
        data.addAndSave(new TestEntity("Entity"));
    }

    @ActionMethod("reloadAction")
    public void onReloadAction() {
        disableUiProperty.setValue(true);
        ConcurrentUtils.then(data.reload(), (e) -> disableUiProperty.setValue(false));
    }

    @ActionMethod("removeAction")
    public void onRemoveAction() {
        disableUiProperty.setValue(true);
        ConcurrentUtils.then(data.get(dataTable.getSelectionModel().getSelectedIndex()).delete(), (e) -> disableUiProperty.setValue(false));
    }

    private void initDisableUiProperty() {
        disableUiProperty = new SimpleBooleanProperty(false);
        addButton.disableProperty().bind(disableUiProperty);
        reloadButton.disableProperty().bind(disableUiProperty);
        removeButton.disableProperty().bind(disableUiProperty.or(dataTable.getSelectionModel().selectedIndexProperty().lessThan(0)));
    }
}
