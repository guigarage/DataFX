package org.datafx.flow.wysiwyg.netview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class NetConnectionView<T> extends Control {

    private ObjectProperty<NetConnection<T>> connection;
    private ObjectProperty<NetView<T>> netView;
    private Line line;

    public NetConnectionView() {
        connection = new SimpleObjectProperty<>();
        netView = new SimpleObjectProperty<>();

        line = new Line(0, 0, 0, 0);
        getChildren().addAll(line);

    }

    public void layout(NetCell<T> startCell, NetCell<T> endCell) {

        getChildren().clear();

        Bounds startBounds = startCell.getBoundsInParent();
        Point2D startCenterPoint = new Point2D(startBounds.getMinX() + (startBounds.getMaxX() - startBounds.getMinX()) / 2, startBounds.getMinY() + (startBounds.getMaxY() - startBounds.getMinY()) / 2);

        Bounds endBounds = endCell.getBoundsInParent();
        Point2D endCenterPoint = new Point2D(endBounds.getMinX() + (endBounds.getMaxX() - endBounds.getMinX()) / 2, endBounds.getMinY() + (endBounds.getMaxY() - endBounds.getMinY()) / 2);

        try {
            Point2D intersectionPoint = MathUtils.intersectLines(startCenterPoint, endCenterPoint, new Point2D(startCenterPoint.getX(), startCenterPoint.getY()), new Point2D(startCenterPoint.getX() + 1, startCenterPoint.getY()));
            Rectangle r = new Rectangle(intersectionPoint.getX() - 3, intersectionPoint.getY() - 3, 6, 6);
            r.setFill(Color.BLUE);
            getChildren().add(r);
        } catch (Exception e) {
        }

        line.setStartX(startCenterPoint.getX());
        line.setStartY(startCenterPoint.getY());

        line.setEndX(endCenterPoint.getX());
        line.setEndY(endCenterPoint.getY());

        getChildren().add(line);

        relocate(Math.min(startCenterPoint.getX(), endCenterPoint.getX()), Math.min(startCenterPoint.getY(), endCenterPoint.getY()));
        resize(Math.abs(endCenterPoint.getX() - startCenterPoint.getX()), Math.abs(endCenterPoint.getY() - startCenterPoint.getY()));
    }

    public NetView<T> getNetView() {
        return netView.get();
    }

    public void setNetView(NetView<T> netView) {
        this.netView.set(netView);
    }

    public ObjectProperty<NetView<T>> netViewProperty() {
        return netView;
    }

    public NetConnection<T> getConnection() {
        return connection.get();
    }

    public void setConnection(NetConnection<T> connection) {
        this.connection.set(connection);
    }

    public ObjectProperty<NetConnection<T>> connectionProperty() {
        return connection;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new NetConnectionViewSkin<>(this);
    }
}
