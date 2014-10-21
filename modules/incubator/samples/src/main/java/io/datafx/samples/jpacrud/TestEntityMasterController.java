package io.datafx.samples.jpacrud;

import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.core.concurrent.ConcurrentUtils;
import io.datafx.crud.CrudListProperty;
import io.datafx.crud.table.TableColumnFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@ViewController(value = "master.fxml", title = "CRUD")
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
