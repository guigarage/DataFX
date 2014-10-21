package io.datafx.samples.inheritance;

import io.datafx.controller.ViewController;

import javax.annotation.PostConstruct;


@ViewController("inheritance.fxml")
public class ControllerImpl extends AbstractController {

    @PostConstruct
    public void init() {
        System.out.println("Check 1: " + getActionHandler().toString());
        System.out.println("Check 2: " + getContext().toString());
        System.out.println("Check 3: " + getDate().toString());
    }
}
