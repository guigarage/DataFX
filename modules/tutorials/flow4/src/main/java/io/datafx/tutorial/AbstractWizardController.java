package io.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import io.datafx.controller.flow.action.ActionTrigger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Note: Tutorial 4 depends on tutorial 3. To understand the complete tutorial please check tutorial 3 because some
 * informations are documented there. Here only the features and code that differ from tutorial 3 will be described.
 *
 * This is the base class for all view controllers in the wizard. Each view has a action-bar with an "back", "next" and
 * "finish" button. All 3 buttons are defined in this abstract class and will be injected by DataFX. 
 * 
 * In contrast to tutorial 3 all buttons are annotated by the @ActionTrigger annotation that was introduces in tutorial 1.
 * By using this annotation a defined action will be called once the button is pressed. All these actions are defined by
 * an unique id. In this case there are three different ids: "back", "finish" and "next". The action for these ids is 
 * defined in the {@link Tutorial4Main} class. As you can see in the {@link Tutorial4Main} class some of the action ids 
 * are unique for a view and other are unique in a flow. By doing so you can define global actions for a flow or actions 
 * that will execute a specific tasks in each view.
 *
 * About the action-bar:
 * The action-bar is defined in the actionBar.fxml file. When looking at the fxml files of the wizard views
 * (wizardStart.fxml, wizard1.fxml, ...) you will see that the fxml of the action-bar is included in each of them. If
 * you open the fxml files with Scene Builder you can see the action-bar included in the view.
 */
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

    @PreDestroy
    private void onClose() {
        System.out.println("Closing " + getClass());
    }

    @PostConstruct
    private void onOpen() {
        System.out.println("Closing " + getClass());
    }

}
