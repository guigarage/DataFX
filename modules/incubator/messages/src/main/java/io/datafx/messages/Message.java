package io.datafx.messages;

/**
 * Created by hendrikebbers on 10.10.14.
 */
public class Message<T> {

    private T content;

    public Message(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }
}
