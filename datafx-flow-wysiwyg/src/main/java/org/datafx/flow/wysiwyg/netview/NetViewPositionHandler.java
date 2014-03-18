package org.datafx.flow.wysiwyg.netview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Point2D;

public class NetViewPositionHandler<T> {

    ObservableMap<T, Point2D> positions;

    private ObjectProperty<NetView<T>> netView;


    public NetViewPositionHandler(NetView<T> netView) {
        positions = FXCollections.observableHashMap();
        this.netView = new SimpleObjectProperty<>(netView);
    }

    public Point2D getPosition(T item) {
       if(positions.containsKey(item)) {
           return positions.get(item);
       }
        return new Point2D(100, 100);
    }

    public void setPosition(T item, Point2D position) {
        System.out.println("Pos: " + position);
        this.positions.remove(item);
        this.positions.put(item, position);
        netView.get().requestLayout();
    }
}
