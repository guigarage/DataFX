package io.datafx.samples.nofxml;

import io.datafx.controller.ViewController;
import io.datafx.controller.ViewNode;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.scene.control.Button;

@ViewController(root = SimpleView.class)
public class SimpleController {

    @ViewNode
    @ActionTrigger("action")
    private Button myButton;

    @ActionMethod("action")
    private void onClick() {
        System.out.println("TADA!");
    }
}
