package org.datafx.flow.wysiwyg.netview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class NetConnection<T> {

    private ObjectProperty<T> startItem;

    private ObjectProperty<T> endItem;

    public NetConnection() {
        startItem = new SimpleObjectProperty<>();
        endItem = new SimpleObjectProperty<>();
    }

    public T getEndItem() {
        return endItem.get();
    }

    public ObjectProperty<T> endItemProperty() {
        return endItem;
    }

    public void setEndItem(T endItem) {
        this.endItem.set(endItem);
    }

    public T getStartItem() {
        return startItem.get();
    }

    public ObjectProperty<T> startItemProperty() {
        return startItem;
    }

    public void setStartItem(T startItem) {
        this.startItem.set(startItem);
    }
}
