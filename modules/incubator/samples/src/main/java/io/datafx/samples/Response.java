package io.datafx.samples;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Response {
    
    private int code;
    private String message;
    private ObservableList<String> details = FXCollections.observableArrayList();

    public Response() {}
    
    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the details
     */
    public ObservableList<String> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(ObservableList<String> details) {
        this.details = details;
    }

    
}
