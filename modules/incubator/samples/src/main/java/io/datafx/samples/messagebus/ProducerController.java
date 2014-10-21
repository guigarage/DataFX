package io.datafx.samples.messagebus;

import io.datafx.controller.FXMLController;
import io.datafx.messages.MessageProducer;
import io.datafx.messages.MessageTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by hendrikebbers on 10.10.14.
 */
@FXMLController("producer.fxml")
public class ProducerController {

    @FXML
    @MessageTrigger()
    private Button sendButton;

    @FXML
    private TextField textField;

    @MessageProducer("test-message")
    private String getMessage() {
        return textField.getText();
    }

}
