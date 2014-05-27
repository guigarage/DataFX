package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionTrigger;

@FXMLController(value="wizard1.fxml", title = "Wizard: Step 1")
public class Wizard1Controller extends AbstractWizardController {

    @FXML
    @ActionTrigger("help")
    private Hyperlink helpLink;
}
