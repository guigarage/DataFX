package org.datafx.flow.wysiwyg.netview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;

public class NetView<T> extends Control {

    private ObjectProperty<ObservableList<T>> items;

    private ObjectProperty<ObservableList<NetConnection<T>>> connections;

    private ObjectProperty<Callback<NetView<T>, NetCell<T>>> cellFactory;

    private ObjectProperty<Callback<NetView<T>, NetConnectionView<T>>> connectionFactory;

    private ObjectProperty<NetViewPositionHandler<T>> netViewPositionHandler;

    public NetView() {
        netViewPositionHandler = new SimpleObjectProperty<>(new NetViewPositionHandler<T>(this));
        items = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        connections = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        cellFactory = new SimpleObjectProperty<>((e) -> new NetCell<>());
        connectionFactory = new SimpleObjectProperty<>((e) -> new NetConnectionView<>());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new NetViewSkin<>(this);
    }

    public NetViewPositionHandler<T> getNetViewPositionHandler() {
        return netViewPositionHandler.get();
    }

    public ObjectProperty<NetViewPositionHandler<T>> netViewPositionHandlerProperty() {
        return netViewPositionHandler;
    }

    public ObservableList<NetConnection<T>> getConnections() {
        return connections.get();
    }

    public ObjectProperty<ObservableList<NetConnection<T>>> connectionsProperty() {
        return connections;
    }

    public void setConnections(ObservableList<NetConnection<T>> connections) {
        this.connections.set(connections);
    }

    public void setNetViewPositionHandler(NetViewPositionHandler<T> netViewPositionHandler) {
        this.netViewPositionHandler.set(netViewPositionHandler);
    }

    public final void setCellFactory(Callback<NetView<T>, NetCell<T>> value) {
        cellFactoryProperty().set(value);
    }

    public final Callback<NetView<T>, NetCell<T>> getCellFactory() {
        return cellFactory.get();
    }

    public final ObjectProperty<Callback<NetView<T>, NetCell<T>>> cellFactoryProperty() {
        return cellFactory;
    }

    public final void setConnectionFactory(Callback<NetView<T>, NetConnectionView<T>> value) {
        connectionFactoryProperty().set(value);
    }

    public final Callback<NetView<T>, NetConnectionView<T>> getConnectionFactory() {
        return connectionFactory.get();
    }

    public final ObjectProperty<Callback<NetView<T>, NetConnectionView<T>>> connectionFactoryProperty() {
        return connectionFactory;
    }

    public final void setItems(ObservableList<T> value) {
        itemsProperty().set(value);
    }

    public final ObservableList<T> getItems() {
        return items.get();
    }

    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }
}
