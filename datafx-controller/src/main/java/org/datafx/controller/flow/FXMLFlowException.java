package org.datafx.controller.flow;

public class FXMLFlowException extends Exception {

    private static final long serialVersionUID = 1L;

    public FXMLFlowException() {
        super();
    }

    public FXMLFlowException(String message) {
        super(message);
    }

    public FXMLFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FXMLFlowException(Throwable cause) {
        super(cause);
    }
}
