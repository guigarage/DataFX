package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;

import javax.annotation.PostConstruct;

@FXMLController(value="wizardDone.fxml", title = "Wizard: Finish")
public class WizardDoneController extends AbstractWizardController {

    @FXML
    private Button nextButton;

    @PostConstruct
    public void init() {
        nextButton.setVisible(false);
    }
}
