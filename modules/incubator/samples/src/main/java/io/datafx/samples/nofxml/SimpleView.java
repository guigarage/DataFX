package io.datafx.samples.nofxml;

import io.datafx.controller.ViewNode;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class SimpleView extends StackPane {

    @ViewNode
    private Button myButton;

    public SimpleView() {
        setPadding(new Insets(24));
        myButton = new Button("Test");
        getChildren().addAll(myButton);
    }
}
