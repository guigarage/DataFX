package org.datafx.controller.util;

public class FxmlLoadException extends Exception{

    private static final long serialVersionUID = 1L;

    public FxmlLoadException() {
        super();
    }

    public FxmlLoadException(String message) {
        super(message);
    }

    public FxmlLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FxmlLoadException(Throwable cause) {
        super(cause);
    }

}
