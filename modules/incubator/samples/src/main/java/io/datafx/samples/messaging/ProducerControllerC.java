package io.datafx.samples.messaging;

import io.datafx.controller.FXMLController;
import io.datafx.messages.Message;
import io.datafx.messages.MessageProducer;
import io.datafx.messages.MessageTrigger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;

/**
 * Created by hendrikebbers on 11.10.14.
 */
@FXMLController("view.fxml")
public class ProducerControllerC {

    @FXML
    @MessageTrigger()
    private Button sendButton;

    @FXML
    private TextField textField;

    @MessageProducer("error-message")
    private Supplier<String> messageSupplier = () -> textField.getText();
}
