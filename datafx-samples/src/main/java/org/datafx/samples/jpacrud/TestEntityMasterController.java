package org.datafx.samples.jpacrud;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.FXMLFlowAction;
import org.datafx.crud.CrudListProperty;
import org.datafx.crud.table.TableColumnFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@FXMLController("master.fxml")
public class TestEntityMasterController {

    @FXML
    TableView<TestEntity> dataTable;

    @FXML
    @FXMLFlowAction("addAction")
    private Button addButton;

    @FXML
    @FXMLFlowAction("reloadAction")
    private Button reloadButton;

    @FXML
    @FXMLFlowAction("removeAction")
    private Button removeButton;

    @Inject
    private TestEntityCrudService crudService;

    private CrudListProperty<TestEntity, Long> data;

    @PostConstruct
    public void init() {
        data = new CrudListProperty(crudService);

        removeButton.setDisable(true);
        dataTable.getColumns().setAll(TableColumnFactory.createColumns(TestEntity.class));
        dataTable.setItems(data.getImmutableEntityList());
        dataTable.getSelectionModel().getSelectedIndices().addListener((InvalidationListener)(e) -> removeButton.setDisable(dataTable.getSelectionModel().getSelectedIndices().isEmpty()));
    }

    @ActionMethod("addAction")
    public void onAddAction() {
        TestEntity e = new TestEntity();
        e.setName("Entity");
        e.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore.");
        data.addAndSave(e);
    }

    @ActionMethod("reloadAction")
    public void onReloadAction() {
        data.reload();
    }

    @ActionMethod("removeAction")
    public void onRemoveAction() {
        data.get(dataTable.getSelectionModel().getSelectedIndex()).delete();
    }
}
