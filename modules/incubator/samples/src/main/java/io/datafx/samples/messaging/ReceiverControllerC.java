package io.datafx.samples.messaging;

import io.datafx.controller.FXMLController;
import io.datafx.messages.OnMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by hendrikebbers on 12.10.14.
 */
@FXMLController("view.fxml")
public class ReceiverControllerC {

    @FXML
    private Label messageLabel;

    @OnMessage("chat-message")
    private void onNewChatMessage() {
        messageLabel.setText("Message received!");
    }
}
