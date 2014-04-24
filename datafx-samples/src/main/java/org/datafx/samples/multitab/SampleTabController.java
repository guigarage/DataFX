package org.datafx.samples.multitab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import org.datafx.controller.FXMLController;
import org.datafx.controller.context.Metadata;
import org.datafx.controller.context.ViewMetadata;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@FXMLController(value = "view1.fxml", title = "Demo Dialog", iconPath = "smiley16.png")
public class SampleTabController {

    @FXML
    private Button titleButton;

    @FXML
    private Button iconButton;

    @Metadata
    private ViewMetadata metadata;

    @PostConstruct
    public void init() {
        titleButton.setOnAction((e) -> metadata.setTitle("View Title (" + new Date() + ")"));
        iconButton.setOnAction((e) -> {
            ProgressIndicator indicator = new ProgressIndicator();
            indicator.setPrefHeight(16);
            indicator.setPrefWidth(16);
            metadata.setGraphic(indicator);
        });
    }

    @PreDestroy
    public void onRemove() {
        System.out.println("View is removed!");
    }
}
