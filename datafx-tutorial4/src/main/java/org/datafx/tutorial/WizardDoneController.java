package org.datafx.tutorial;

import org.datafx.controller.FXMLController;

import javax.annotation.PostConstruct;

@FXMLController(value="wizardDone.fxml", title = "Wizard: Finish")
public class WizardDoneController extends AbstractWizardController {

    @PostConstruct
    public void init() {
        getNextButton().setDisable(true);
        getFinishButton().setDisable(true);
    }
}
