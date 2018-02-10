/**
 * Copyright (c) 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.tutorial;

import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.container.AnimatedFlowContainer;
import io.datafx.controller.flow.container.ContainerAnimations;
import io.datafx.controller.util.VetoException;

import javax.annotation.PostConstruct;

/**
 * This class defines the main controller of the wizard. The complete action toolbar is managed here. In addition, a
 * flow that contains all the custom views of the wizard is added to the view. The navigation for this internal flow
 * is managed here.
 */
@ViewController("wizard.fxml")
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

    /**
     * Here, our {@code init} method defines a internal flow that contains the steps of the wizard as separate views.
     * This internal flow will use animations for the navigation between different views.
     * <p>
     * Using a snippet of the below as an example,
     * <pre>{@code
     * Flow(WizardView1Controller.class).
     *     withLink(WizardView1Controller.class, "next", WizardView2Controller.class)
     * }</pre>
     * will make it so that whenever you're in Wizard1, the "next" button will take you to Wizard2.
     * That is to say, that it will assign the action with the id of "next" to navigate to Wizard2, whenever you're
     * in Wizard1.
     *
     * @throws FlowException if the internal flow can't be created
     */
    @PostConstruct
    public void init() throws FlowException {
        Flow flow = new Flow(WizardView1Controller.class).
                withLink(WizardView1Controller.class, "next", WizardView2Controller.class).
                withLink(WizardView2Controller.class, "next", WizardView3Controller.class).
                withLink(WizardView3Controller.class, "next", WizardView4Controller.class).
                withLink(WizardView4Controller.class, "next", WizardView5Controller.class);

        flowHandler = flow.createHandler();
        centerPane.getChildren().add(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.ZOOM_IN)));

        backButton.setDisable(true);
    }

    /**
     * This method will be called when the {@code back} action will be executed. The method handles the navigation of
     * the internal flow that contains the steps of the wizard as separate views. In addition the states of the action
     * buttons will be managed.
     * @throws VetoException If the navigation can't be executed
     * @throws FlowException If the navigation can't be executed
     */
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

    /**
     * This method will be called when the {@code next} action will be executed. The method handles the navigation of
     * the internal flow that contains the steps of the wizard as separate views. In addition the states of the action
     * buttons will be managed.
     * @throws VetoException If the navigation can't be executed
     * @throws FlowException If the navigation can't be executed
     */
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

    /**
     * This method will be called when the {@code finish} action will be executed. The method handles the navigation of
     * the internal flow that contains the steps of the wizard as separate views. In addition the states of the action
     * buttons will be managed.
     * @throws VetoException If the navigation can't be executed
     * @throws FlowException If the navigation can't be executed
     */
    @ActionMethod("finish")
    public void onFinish() throws VetoException, FlowException {
        flowHandler.navigateTo(WizardView5Controller.class);
        finishButton.setDisable(true);
        nextButton.setDisable(true);
        backButton.setDisable(false);
    }
}
