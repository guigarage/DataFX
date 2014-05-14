package org.datafx.samples.ejb.wildfly;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.concurrent.ObservableExecutor;
import org.datafx.concurrent.ProcessChain;
import org.datafx.controller.FXMLController;
import org.datafx.controller.context.ConcurrencyProvider;
import org.datafx.ejb.RemoteCalculator;
import org.datafx.ejb.RemoteEjb;

import javax.annotation.PostConstruct;

@FXMLController("view.fxml")
public class ViewController {

    @RemoteEjb()
    RemoteCalculator calculator;

    @FXML
    Button myButton;

    @ConcurrencyProvider
    ObservableExecutor executor;

    @PostConstruct
    public void init() {
    ProcessChain.create().
            addRunnableInPlatformThread(() -> myButton.setDisable(true)).
            addRunnableInExecutor((Runnable) () -> calculator.add(3, 3)).
            addRunnableInPlatformThread(() -> myButton.setDisable(false));

        myButton.setOnAction(e -> System.out.println(calculator.add(3, 3)));
    }

}
