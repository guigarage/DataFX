package org.datafx.samples.inheritance;

import org.datafx.controller.FXMLController;

import javax.annotation.PostConstruct;


@FXMLController("inheritance.fxml")
public class ControllerImpl extends AbstractController {

    @PostConstruct
    public void init() {
        System.out.println("Check 1: " + getActionHandler().toString());
        System.out.println("Check 2: " + getContext().toString());
        System.out.println("Check 3: " + getDate().toString());
    }
}
