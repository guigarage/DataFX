package io.datafx.samples.ejb.wildfly;

import io.datafx.controller.FXMLController;
import io.datafx.controller.context.ConcurrencyProvider;
import io.datafx.core.concurrent.ObservableExecutor;
import io.datafx.core.concurrent.ProcessChain;
import io.datafx.ejb.RemoteCalculator;
import io.datafx.ejb.RemoteEjb;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    ProcessChain.create(executor).
            addRunnableInPlatformThread(() -> myButton.setDisable(true)).
            addRunnableInExecutor((Runnable) () -> calculator.add(3, 3)).
            addRunnableInPlatformThread(() -> myButton.setDisable(false));

        myButton.setOnAction(e -> System.out.println(calculator.add(3, 3)));
    }

}
