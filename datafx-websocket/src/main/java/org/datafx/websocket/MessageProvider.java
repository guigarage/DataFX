package org.datafx.websocket;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.datafx.provider.ListObjectDataProvider;

/**
 *
 * @author johan
 */
public class MessageProvider extends ListObjectDataProvider<String> {

    
    private final WebSocketReader reader;
    private ObservableList<String> incomingMessages = FXCollections.observableArrayList();

    public MessageProvider (WebSocketReader reader) {
        super(reader);
        this.reader = reader;
    }

    public void sendMessage (String msg) throws IOException {
        reader.sendMessage(msg);
    }
    
}
