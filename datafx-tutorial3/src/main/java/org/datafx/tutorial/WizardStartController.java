package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.LinkAction;

import javax.annotation.PostConstruct;

@FXMLController(value="wizardStart.fxml", title = "Wizard: Start")
public class WizardStartController extends AbstractWizardController {

    @FXML
    @LinkAction(Wizard1Controller.class)
    private Button nextButton;

    @PostConstruct
    public void init() {
        getBackButton().setVisible(false);
    }
}
