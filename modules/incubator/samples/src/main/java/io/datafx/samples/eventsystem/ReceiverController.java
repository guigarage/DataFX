package io.datafx.samples.eventsystem;

import io.datafx.controller.ViewController;
import io.datafx.eventsystem.Event;
import io.datafx.eventsystem.OnEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by hendrikebbers on 10.10.14.
 */
@ViewController("receiver.fxml")
public class ReceiverController {

    @FXML
    private Label messageLabel;

    @OnEvent("test-message")
    private void onNewChatMessage(Event<String> e) {
        messageLabel.setText(e.getContent());
    }
}
