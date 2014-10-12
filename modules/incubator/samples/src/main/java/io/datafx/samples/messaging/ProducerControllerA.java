package io.datafx.samples.messaging;

import io.datafx.controller.FXMLController;
import io.datafx.messages.MessageProducer;
import io.datafx.messages.MessageTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by hendrikebbers on 10.10.14.
 */
@FXMLController("view.fxml")
public class ProducerControllerA {

    @FXML
    @MessageTrigger("chat-message")
    private Button sendButton;

    @FXML
    private TextField textField;

    @MessageProducer("chat-message")
    private String getMessage() {
        return textField.getText();
    }

}
