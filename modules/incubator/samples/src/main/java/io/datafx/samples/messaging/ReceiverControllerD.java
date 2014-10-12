package io.datafx.samples.messaging;

import io.datafx.controller.FXMLController;
import io.datafx.messages.Message;
import io.datafx.messages.OnMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.function.Consumer;

/**
 * Created by hendrikebbers on 12.10.14.
 */
@FXMLController("view.fxml")
public class ReceiverControllerD {

    @FXML
    private Label messageLabel;

    @OnMessage("chat-message")
    private Consumer<Message<String>> messageConsumer = m -> messageLabel.setText(m.getContent());
}
