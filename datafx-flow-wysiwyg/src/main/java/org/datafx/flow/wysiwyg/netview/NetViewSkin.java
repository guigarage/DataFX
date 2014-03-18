package org.datafx.flow.wysiwyg.netview;

import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;

import java.util.ArrayList;
import java.util.List;

public class NetViewSkin<T> extends SkinBase<NetView<T>> {

    private final ListChangeListener<T> netViewItemsListener = (c) -> getSkinnable().requestLayout();
    private final WeakListChangeListener<T> weakNetViewItemsListener =
            new WeakListChangeListener<T>(netViewItemsListener);

    private final ListChangeListener<NetConnection<T>> netViewConnectionsListener = (c) -> getSkinnable().requestLayout();
    private final WeakListChangeListener<NetConnection<T>> weakNetViewConnectionsListener =
            new WeakListChangeListener<NetConnection<T>>(netViewConnectionsListener);


    protected NetViewSkin(NetView<T> control) {
        super(control);
        updateNetViewItems();
        updateNetViewConnections();

        getSkinnable().itemsProperty().addListener((observable, oldValue, newValue) -> updateNetViewItems());
        getSkinnable().connectionsProperty().addListener((observable, oldValue, newValue) -> updateNetViewConnections());
    }

    public void updateNetViewConnections() {
        if (getSkinnable().getConnections() != null) {
            getSkinnable().getConnections().removeListener(weakNetViewConnectionsListener);
        }

        if (getSkinnable().getConnections() != null) {
            getSkinnable().getConnections().addListener(weakNetViewConnectionsListener);
        }
        getSkinnable().requestLayout();
    }

    public void updateNetViewItems() {
        if (getSkinnable().getItems() != null) {
            getSkinnable().getItems().removeListener(weakNetViewItemsListener);
        }

        if (getSkinnable().getItems() != null) {
            getSkinnable().getItems().addListener(weakNetViewItemsListener);
        }
        getSkinnable().requestLayout();
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        if(getSkinnable().getItems() == null) {
            getChildren().clear();
            return;
        }

        updateCells();
        updateConnections();

        if (getSkinnable().getItems() != null) {

            for(Node child : getChildren()) {
                if(child instanceof NetCell) {
                    NetCell<T> cell = (NetCell<T>) child;
                    Point2D point = getSkinnable().getNetViewPositionHandler().getPosition(cell.getItem());
                    double prefWidth = cell.prefWidth(-1);
                    double prefHeigth = cell.prefHeight(-1);

                    cell.relocate(point.getX() - prefWidth / 2, point.getY() - prefHeigth / 2);
                    cell.resize(prefWidth, prefHeigth);
                }
            }

            for(Node child : getChildren()) {
                if(child instanceof NetConnectionView) {
                    NetConnectionView<T> connectionView = (NetConnectionView<T>) child;

                    NetCell<T> startCell = null;
                    NetCell<T> endCell = null;
                    for(Node child2 : getChildren()) {
                        if(child2 instanceof NetCell) {
                            NetCell<T> cell = (NetCell<T>) child2;
                            if(cell.getItem().equals(connectionView.getConnection().getStartItem())) {
                                startCell = cell;
                            }
                            if(cell.getItem().equals(connectionView.getConnection().getEndItem())) {
                                endCell = cell;
                            }
                        }
                    }

                    connectionView.layout(startCell, endCell);
                }
            }
        }
    }

    private void updateCells() {
        List<Node> toRemove = new ArrayList<>();
        List<T> usedItems = new ArrayList<>();
        for(Node child : getChildren()) {
            if(child instanceof NetCell) {
                T item = ((NetCell<T>) child).getItem();
                if(getSkinnable().getItems().contains(item)) {
                    usedItems.add(item);
                } else {
                    toRemove.add(child);
                }
            }
        }

        for(Node child : toRemove) {
            getChildren().remove(child);
        }

        for(T item : getSkinnable().getItems()) {
            if(!usedItems.contains(item)) {
                NetCell<T> cell = getSkinnable().getCellFactory().call(getSkinnable());
                cell.setNetView(getSkinnable());
                cell.setItem(item);
                getChildren().add(cell);
            }
        }
    }

    private void updateConnections() {
        if(getSkinnable().getConnections() == null) {
            return;
        }
        List<Node> toRemove = new ArrayList<>();
        List<NetConnection<T>> usedConnections = new ArrayList<>();
        for(Node child : getChildren()) {
            if(child instanceof NetConnectionView) {
                NetConnection<T> connection = ((NetConnectionView<T>) child).getConnection();
                if(getSkinnable().getConnections().contains(connection)) {
                    usedConnections.add(connection);
                } else {
                    toRemove.add(child);
                }
            }
        }

        for(Node child : toRemove) {
            getChildren().remove(child);
        }

        for(NetConnection<T> connection : getSkinnable().getConnections()) {
            if(!usedConnections.contains(connection)) {
                NetConnectionView<T> connectionView = getSkinnable().getConnectionFactory().call(getSkinnable());
                connectionView.setNetView(getSkinnable());
                connectionView.setConnection(connection);
                getChildren().add(connectionView);
            }
        }
    }
}
