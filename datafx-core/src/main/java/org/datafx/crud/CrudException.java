package org.datafx.crud;

public class CrudException extends Exception {

    public CrudException() {
        super();
    }

    public CrudException(String message) {
        super(message);
    }

    public CrudException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrudException(Throwable cause) {
        super(cause);
    }
}
