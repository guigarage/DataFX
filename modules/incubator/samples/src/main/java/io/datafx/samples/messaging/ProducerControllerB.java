package io.datafx.samples.messaging;

import io.datafx.controller.FXMLController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.messages.Message;
import io.datafx.messages.MessageBus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by hendrikebbers on 12.10.14.
 */
@FXMLController("view.fxml")
public class ProducerControllerB {

    @FXML
    @ActionTrigger("send")
    private Button sendButton;

    @FXML
    private TextField textField;

    @ActionMethod("send")
    private void sendMessage() {
        MessageBus.getInstance().send("chat-message", textField.getText());
    }

}
