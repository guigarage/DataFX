/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;

import javax.annotation.PostConstruct;

/**
 * This is the controller class for the view used in this first tutorial. 
 * By using the DataFX @FXMLController annotation
 * the class defines its FXML file that contains the layout of the view and defines its UI components.
 * By using the JavaFX @FXML annotation that is part of the basic JavaFX API, components that are defined in the FXML file can be easily
 * injected in the controller. Once the controller is created the init() methods will be called by the Flow Framework. 
 * This method is annotated by the @PostConstruct annotation. 
 * By doing so the DataFX flow container will call this method once all injectable values of the controller
 * instance are injected. There three different types of values / fields that can be injected:
 * <ul>
 * <li>UI components that are annotated by @FXML</li>
 * <li>DataFX objects. Here DataFX provides several annotations</li>
 * <li>Custom implementations. These will be injected by using the @Inject annotation</li>
 * </ul>
 * <p>
 * The method that is annotated by @PostContruct will be called when all injections are finished.
 * </p>
 * <p>
 * In this first example we will only use @FXML to inject FXML UI components to the controller.
 * </p>
 * <p>
 * In addition DataFX action handling is introduced in this example. The view contains a button named actionButton.
 * Once this button is clicked, the onAction() method will be executed. To do so DataFX contains two annotation:
 * - The @ActionTrigger annotation annotates a UI component that will trigger an action. 
 * Each action in DataFX is defined by a unique id, which is passed as the value of the @ActionTrigger annotation
 * In this example the "myAction" id is used. The id must be unique in view controller.
 * - To handle the action a method of the controller should be annotated with the @ActionMethod annotation. 
 * The value of this annotation
 * must be the unique id of the defined action. Therefore @ActionMethod("myAction") is used here.
 * The method will be called in the JavaFX Application Thread
 * </p>
 * <p>
 * As you will see later there are other types of actions than simply calling a method. These will be shown later in other tutorial.
 * For now the most important point is that a component that is annotated with @ActionTrigger can trigger a method that is annotated with
 * {link @io.datafx.controller.flow.action.ActionMethod} if both annotations define the same unique action id.
 * So once the actionButton in this example is clicked the onAction() method will be called and the text of the label will change.
 * </p>
 * <p>
 * Note: This example is quite easy and normally you could define the action binding by only one line of Java code in the init() method:
 * actionButton.setOnAction((e) -&gt; onAction());
 * So why are these annotations used here?
 * As you will see in further tutorials that are more complex than this one it will make sense to use the annotations to provide
 * more readable code.
 * </p>
 */
@FXMLController("simpleView.fxml")
public class SimpleController {

    /**
     * In the FXML file of this view (simpleView.fxml) a Label is defined. This label has the attribute fx:id="resultLabel".
     * The definition of the @FXML annotation defines that the value of a field will be injected if the name of the field is
     * equal to an fx:id that is defined in the FXML file. So the value of this Label field will be injected and once the controller
     * is initialized it will contain the instance of the Label that is defined by the FXML file.
     */
    @FXML
    private Label resultLabel;
    @FXML
    @ActionTrigger("myAction")
    private Button actionButton;
    private int clickCount = 0;

    @PostConstruct
    public void init() {
        resultLabel.setText("Button was clicked " + clickCount + " times");
    }

    @ActionMethod("myAction")
    public void onAction() {
        clickCount++;
        resultLabel.setText("Button was clicked " + clickCount + " times");
    }
}
