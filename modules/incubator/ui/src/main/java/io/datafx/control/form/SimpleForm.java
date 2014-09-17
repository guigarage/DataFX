package io.datafx.control.form;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class SimpleForm extends VBox {

    private AnchorPane titlePane;
    private Label titleLabel;
    private GridPane mainPane;
    private FlowPane actionPane;
    private int rowCount = 0;

    public SimpleForm() {
        setSpacing(12);

        titlePane = new AnchorPane();
        titlePane.getStyleClass().add("datafx-form-title");
        titleLabel = new Label();
        titleLabel.getStyleClass().add("datafx-form-title-label");
        AnchorPane.setLeftAnchor(titleLabel, 12.0);
        AnchorPane.setTopAnchor(titleLabel, 6.0);
        AnchorPane.setBottomAnchor(titleLabel, 6.0);
        titlePane.getChildren().add(titleLabel);
        titlePane.setVisible(false);
        getChildren().add(titlePane);

        mainPane = new GridPane();
        mainPane.setAlignment(Pos.CENTER_RIGHT);
        mainPane.setHgap(12.0);
        mainPane.setVgap(6.0);

        VBox.setMargin(mainPane, new Insets(0, 16, 0, 16));
        getChildren().add(mainPane);

        actionPane = new FlowPane();
        actionPane.setAlignment(Pos.CENTER_RIGHT);
        actionPane.setHgap(6.0);
        VBox.setMargin(actionPane, new Insets(0, 16, 12, 16));
        getChildren().add(actionPane);
    }

    public Button addAction(String name, EventHandler<ActionEvent> eventHandler) {
        Button actionButton = new Button(name);
        actionButton.setOnAction(eventHandler);
        actionButton.getStyleClass().add("datafx-form-button");
        addToActionPane(actionButton);
        return actionButton;
    }

    public void addToActionPane(Node node) {
        actionPane.getChildren().add(node);
    }

    public void add(String dataName, Node dataNode) {
        add(new Label(dataName), dataNode);
    }

    public void add(Node labelNode, Node dataNode) {
        GridPane.setConstraints(labelNode, 0, rowCount);
        GridPane.setConstraints(dataNode, 1, rowCount);

        GridPane.setHgrow(labelNode, Priority.SOMETIMES);
        GridPane.setHgrow(dataNode, Priority.ALWAYS);

        GridPane.setHalignment(labelNode, HPos.RIGHT);
        GridPane.setHalignment(dataNode, HPos.LEFT);

        mainPane.getChildren().addAll(labelNode, dataNode);
        rowCount++;
    }

    public StringProperty formTitleProperty() {
        return titleLabel.textProperty();
    }

    public void setFormTitle(String formTitle) {
        formTitleProperty().set(formTitle);
    }

    public BooleanProperty titleVisibleProperty() {
        return titlePane.visibleProperty();
    }
}
