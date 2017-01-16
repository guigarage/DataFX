package io.datafx.samples.eventsystem;

import io.datafx.controller.ViewController;
import io.datafx.eventsystem.EventProducer;
import io.datafx.eventsystem.EventTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by hendrikebbers on 10.10.14.
 */
@ViewController("producer.fxml")
public class ProducerController {

    @FXML
    @EventTrigger()
    private Button sendButton;

    @FXML
    private TextField textField;

    @EventProducer("test-message")
    private String getMessage() {
        return textField.getText();
    }

}
