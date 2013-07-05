package org.datafx.websocket;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import org.datafx.provider.ListObjectDataProvider;

/**
 *
 * @author johan
 */
public class MessageProvider extends ListObjectDataProvider<String> {

//implements DataProvider<ObservableList<String>>{
    
    private final WebSocketReader reader;
    private ObservableList<String> incomingMessages = FXCollections.observableArrayList();

    public MessageProvider (WebSocketReader reader) {
        super(reader);
        this.reader = reader;
    }

//    public ListProperty<String> getData() {
//        return new SimpleListProperty(incomingMessages);
//    }
//
//    public Worker<ObservableList<String>> retrieve() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
