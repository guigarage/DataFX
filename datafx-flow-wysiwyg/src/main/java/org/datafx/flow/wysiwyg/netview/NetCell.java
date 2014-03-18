package org.datafx.flow.wysiwyg.netview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class NetCell<T> extends Labeled {

    private ObjectProperty<Point2D> position;

    private ObjectProperty<NetView<T>> netView = new SimpleObjectProperty<>();

    private ObjectProperty<T> item;


    public NetCell() {
        netView = new SimpleObjectProperty<>();
        position = new SimpleObjectProperty<>(new Point2D(0,0));
        item = new SimpleObjectProperty<>();
        item.addListener((observable, oldValue, newValue) -> updateItem(newValue));

        setPadding(new Insets(24));
        setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

    }

    public void updateItem(T item) {

    }

    public T getItem() {
        return item.get();
    }

    public ObjectProperty<T> itemProperty() {
        return item;
    }

    public void setItem(T item) {
        this.item.set(item);
    }

    public Point2D getPosition() {
        return position.get();
    }

    public ObjectProperty<Point2D> positionProperty() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position.set(position);
    }

    public NetView<T> getNetView() {
        return netView.get();
    }

    public ObjectProperty<NetView<T>> netViewProperty() {
        return netView;
    }

    public void setNetView(NetView<T> netView) {
        this.netView.set(netView);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new NetCellSkin<>(this);
    }
}
