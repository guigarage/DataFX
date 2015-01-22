package io.datafx.tutorial;

import io.datafx.controller.ViewController;

/**
 * This is a view controller for one of the steps in the wizard. All buttons of the action-bar that
 * is shown on each view of the wizard are defined in the AbstractWizardController class. The definition of the
 * actions that are registered to these buttons can be found in the {@link Tutorial4Main} class.
 */
@ViewController(value="wizard2.fxml", title = "Wizard: Step 2")
public class Wizard2Controller extends AbstractWizardController {

}
