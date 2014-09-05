package io.datafx.tutorial;

import io.datafx.controller.FXMLController;

import javax.annotation.PostConstruct;

/**
 * This is a view controller for one of the steps in the wizard. All buttons of the action-bar that
 * is shown on each view of the wizard are defined in the AbstractWizardController class. The definition of the
 * actions that are registered to these buttons can be found in the {@link Tutorial4Main} class.
 *
 * Because the "next" and "finish" buttons shouldn't be used here they will become disabled by setting the disable
 * property for both of them in the {@link #init()} method. As described in tutorial 1 the {@link #init()} method is
 * annotated with the {@link PostConstruct} annotation and therefore this method will be called once all fields of the
 * controller instance were injected. So when the view appears on screen the buttons "next" and "finish" will be
 * disabled.
 */
@FXMLController(value="wizardDone.fxml", title = "Wizard: Finish")
public class WizardDoneController extends AbstractWizardController {

    @PostConstruct
    public void init() {
        getNextButton().setDisable(true);
        getFinishButton().setDisable(true);
    }
}
