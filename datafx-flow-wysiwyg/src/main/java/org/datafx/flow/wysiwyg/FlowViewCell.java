package org.datafx.flow.wysiwyg;

import javafx.geometry.Point2D;
import org.datafx.flow.wysiwyg.data.FlowViewDefinition;
import org.datafx.flow.wysiwyg.netview.NetCell;

public class FlowViewCell extends NetCell<FlowViewDefinition> {

    private Point2D dragStartPoint;

    @Override
    public void updateItem(FlowViewDefinition item) {
        setGraphic(null);
        if(item != null) {
            setText(item.getControllerClassName());
            setOnMousePressed((e) ->  dragStartPoint = new Point2D(e.getX(), e.getY()));
            setOnMouseDragged((e) ->  getNetView().getNetViewPositionHandler().setPosition(item, getNetView().getNetViewPositionHandler().getPosition(item).add(e.getX(), e.getY()).subtract(dragStartPoint)));
        }
    }
}
