package org.datafx.samples.ejb.wildfly;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.concurrent.ObservableExecutor;
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
        myButton.setOnAction(e -> System.out.println(calculator.add(3, 3)));
    }

    private void action() {
        executor.createProcessChain().
                inPlatformThread(() -> myButton.setDisable(true)).
                inExecutor(() -> System.out.println(calculator.add(3, 3))).
                inPlatformThread(() -> myButton.setDisable(false)).
                run();
    }

}
