package org.datafx.tutorial;

import org.datafx.controller.FXMLController;

import javax.annotation.PostConstruct;

@FXMLController(value="wizardStart.fxml", title = "Wizard: Start")
public class WizardStartController extends AbstractWizardController {

    @PostConstruct
    public void init() {
        getBackButton().setDisable(true);
    }
}
