package org.datafx.samples;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.datafx.concurrent.ProcessChain;
import org.datafx.control.cell.ActionTableCell;

import java.util.function.Consumer;

public class ActionTableCellDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public class Person {

        private String name;

        Person(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ObservableList<Person> data = FXCollections.observableArrayList(new Person("Hendrik"), new Person("Jonathan"), new Person("Johan"));

        TableView<Person> table = new TableView<>(data);

        TableColumn<Person, String> col1 = new TableColumn("Names");
        col1.setCellValueFactory((e) -> new SimpleStringProperty(e.getValue().getName()));

        TableColumn<Person, String> col2 = new TableColumn("Delete");
        col2.setCellFactory((e) -> ActionTableCell.createWithButton((s) -> data.remove(s), "X"));

        table.getColumns().addAll(col1, col2);

        primaryStage.setScene(new Scene(new StackPane(table)));
        primaryStage.show();
    }

}
