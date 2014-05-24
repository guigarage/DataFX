package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.flow.action.BackAction;
import org.datafx.controller.flow.action.LinkAction;

public class AbstractWizardController {

    @FXML
    @BackAction
    private Button backButton;

    @FXML
    @LinkAction(WizardDoneController.class)
    private Button finishButton;

    public Button getBackButton() {
        return backButton;
    }

    public Button getFinishButton() {
        return finishButton;
    }
}
