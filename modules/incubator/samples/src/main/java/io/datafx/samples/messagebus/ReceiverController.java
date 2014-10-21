package io.datafx.samples.messagebus;

import io.datafx.controller.FXMLController;
import io.datafx.messages.Message;
import io.datafx.messages.OnMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by hendrikebbers on 10.10.14.
 */
@FXMLController("receiver.fxml")
public class ReceiverController {

    @FXML
    private Label messageLabel;

    @OnMessage("test-message")
    private void onNewChatMessage(Message<String> e) {
        messageLabel.setText(e.getContent());
    }
}
