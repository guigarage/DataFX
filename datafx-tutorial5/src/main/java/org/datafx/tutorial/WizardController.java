package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.DefaultFlowContainer;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.util.VetoException;

import javax.annotation.PostConstruct;

@FXMLController("wizard.fxml")
public class WizardController {

    @FXML
    @ActionTrigger("back")
    private Button backButton;
    @FXML
    @ActionTrigger("finish")
    private Button finishButton;
    @FXML
    @ActionTrigger("next")
    private Button nextButton;
    @FXML
    private StackPane centerPane;
    private FlowHandler flowHandler;

    @PostConstruct
    public void init() throws FlowException {
        Flow flow = new Flow(WizardView1Controller.class).
                withLink(WizardView1Controller.class, "next", WizardView2Controller.class).
                withLink(WizardView2Controller.class, "next", WizardView3Controller.class).
                withLink(WizardView3Controller.class, "next", WizardView4Controller.class).
                withLink(WizardView4Controller.class, "next", WizardView5Controller.class);

        flowHandler = flow.createHandler();
        flowHandler.start(new DefaultFlowContainer(centerPane));

        backButton.setDisable(true);
    }

    @ActionMethod("back")
    public void onBack() throws VetoException, FlowException {
        flowHandler.navigateBack();
        if(flowHandler.getCurrentViewControllerClass().equals(WizardView1Controller.class)) {
            backButton.setDisable(true);
        } else {
            backButton.setDisable(false);
        }
        finishButton.setDisable(false);
        nextButton.setDisable(false);
    }

    @ActionMethod("next")
    public void onNext() throws VetoException, FlowException {
        flowHandler.handle("next");
        if(flowHandler.getCurrentViewControllerClass().equals(WizardView5Controller.class)) {
            nextButton.setDisable(true);
            finishButton.setDisable(true);
        } else {
            nextButton.setDisable(false);
        }
        backButton.setDisable(false);
    }

    @ActionMethod("finish")
    public void onFinish() throws VetoException, FlowException {
        flowHandler.navigateTo(WizardView5Controller.class);
        finishButton.setDisable(true);
        nextButton.setDisable(true);
        backButton.setDisable(false);
    }
}
