package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.flow.action.ActionTrigger;

public class AbstractWizardController {

    @FXML
    @ActionTrigger("back")
    private Button backButton;

    @FXML
    @ActionTrigger("finish")
    private Button finishButton;

    @FXML
    @ActionTrigger("next")
    private Button nextButton;

    public Button getBackButton() {
        return backButton;
    }

    public Button getFinishButton() {
        return finishButton;
    }

    public Button getNextButton() {
        return nextButton;
    }
}
