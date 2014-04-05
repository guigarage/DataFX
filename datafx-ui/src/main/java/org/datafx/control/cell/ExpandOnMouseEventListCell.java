/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.control.cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import static org.datafx.control.cell.ExpandOnMouseEventCell.ANIMATION_DURATION;
import static org.datafx.control.cell.ExpandOnMouseEventCell.expandedCellsMap;
import static org.datafx.control.cell.ExpandOnMouseEventCell.expandedIndicesMap;

/**
 * <p>A class containing a {@link ListCell} implementation that expands when a
 * certain mouse event occurs. When the cell expands, it allows for additional
 * information to be shown to the user.
 *
 * <img src="http://www.javafxdata.org/screenshots/ExpandOnMouseEventCell-ListView.PNG"
 * style="float: right; padding: 0px 0px 0px 3px;"/>
 *
 * @param <T> The type of the elements contained within the ListView.
 * @author <a href="http://www.jonathangiles.net">Jonathan Giles</a>
 */
public class ExpandOnMouseEventListCell<T> extends ListCell<T> {
    public static final double DEFAULT_PREF_HEIGHT = 24;
    public static final double DEFAULT_EXPAND_HEIGHT = DEFAULT_PREF_HEIGHT * 3;
    private int maxExpandedCells = 1;
    private final double expandedHeight;

    public ExpandOnMouseEventListCell() {
        this(1);
    }

    public ExpandOnMouseEventListCell(int maxExpandedCells) {
        this(maxExpandedCells, DEFAULT_EXPAND_HEIGHT, MouseEvent.MOUSE_CLICKED);
    }

    public ExpandOnMouseEventListCell(int maxExpandedCells, double expandHeight) {
        this(maxExpandedCells, expandHeight, MouseEvent.MOUSE_CLICKED);
    }

    public ExpandOnMouseEventListCell(int maxExpandedCells, double expandHeight, EventType<MouseEvent> mouseEventType) {
        this.maxExpandedCells = maxExpandedCells >= 0 ? maxExpandedCells : 1;
        this.expandedHeight = expandHeight < 0.0 ? DEFAULT_EXPAND_HEIGHT : expandHeight;

        prefHeightProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable arg0) {
                requestLayout();
            }
        });

        addEventFilter(mouseEventType == null ? MouseEvent.MOUSE_CLICKED : mouseEventType,
                new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent t) {
                if (isEmpty()) {
                    return;
                }
                // flip whether this cell index is expanded or not
                flip(ExpandOnMouseEventListCell.this, getMaxExpandedCells());
            }
        });
    }

    @Override public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setText(item == null ? "" : item.toString());
    }

    @Override public void updateIndex(int i) {
        super.updateIndex(i);

        if (isExpanded()) {
            // immediately expand this cell
            setPrefHeight(expandedHeight);
        } else {
            // immediately collapse this cell
            setPrefHeight(DEFAULT_PREF_HEIGHT);
        }
    }
    private Callback<CellResizeFeatures<T>, CellUpdate> onCellResizeStarting;

    public void setOnCellResizeStarting(Callback<CellResizeFeatures<T>, CellUpdate> onCellResizeStarting) {
        this.onCellResizeStarting = onCellResizeStarting;
    }

    private synchronized static void flip(ExpandOnMouseEventListCell<?> cell, int maxExpandedCells) {
        Queue<Cell> expandedCells = expandedCellsMap.get(cell.getListView());
        if (expandedCells == null) {
            expandedCells = new LinkedBlockingQueue<Cell>();
            expandedCellsMap.put(cell.getListView(), expandedCells);
        }

        List<Integer> expandedIndices = expandedIndicesMap.get(cell.getListView());
        if (expandedIndices == null) {
            expandedIndices = new ArrayList<Integer>();
            expandedIndicesMap.put(cell.getListView(), expandedIndices);
        }

        boolean cellIsExpanded = expandedCells.contains(cell);

        if (maxExpandedCells <= expandedCells.size() && !cellIsExpanded) {
            ExpandOnMouseEventListCell collapsed = (ExpandOnMouseEventListCell) expandedCells.remove();
            animate(collapsed, false);
            expandedIndices.remove((Object) collapsed.getIndex());
        }

        if (cellIsExpanded) {
            expandedCells.remove(cell);
            expandedIndices.remove((Object) cell.getIndex());
        } else {
            expandedCells.add(cell);
            expandedIndices.add(cell.getIndex());
        }

        animate(cell, !cellIsExpanded);
    }

    public boolean isExpanded() {
        if (expandedIndicesMap.containsKey(getListView())) {
            return expandedIndicesMap.get(getListView()).contains(getIndex());
        }
        return false;
    }

    public int getMaxExpandedCells() {
        return maxExpandedCells;
    }

    public void setMaxExpandedCells(int maxExpandedCells) {
        this.maxExpandedCells = maxExpandedCells;
    }

    @Override protected double computePrefHeight(double width) {
        return isExpanded() ? getPrefHeight() : super.computePrefHeight(width);
    }

    @Override protected double computeMinHeight(double width) {
        return computePrefHeight(width);
    }

    private static <T> void animate(final ExpandOnMouseEventListCell<T> cell, final boolean toExpanded) {
        double startHeight = cell.getHeight();

        // the end height is the opposite of the current state - 
        // we are animating out of this state after all
        double endHeight = toExpanded ? cell.expandedHeight : DEFAULT_PREF_HEIGHT;

        // create a timeline to expand/collapse the cell. All this
        // really does is modify the height of the content
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        CellResizeFeatures<T> crf = new CellResizeFeatures<T>(cell, toExpanded);
        final CellUpdate cellUpdate = cell.onCellResizeStarting == null ? null : cell.onCellResizeStarting.call(crf);

        final String text = cellUpdate == null ? cell.getText() : cellUpdate.text;
        final Node graphic = cellUpdate == null ? cell.getGraphic() : cellUpdate.graphic;
        double endOpacity = toExpanded ? 1.0 : 0.0;

        // first key frame
        KeyFrame firstFrame = graphic == null
                ? new KeyFrame(Duration.ZERO,
                new KeyValue(cell.prefHeightProperty(), startHeight, Interpolator.EASE_BOTH))
                : new KeyFrame(Duration.ZERO,
                new KeyValue(cell.prefHeightProperty(), startHeight, Interpolator.EASE_BOTH),
                new KeyValue(graphic.opacityProperty(), 1.0, Interpolator.EASE_BOTH));



        // second key frame
        // at the middle point, the cell opacity is 0.0, so we switch in the new content
        EventHandler<ActionEvent> replaceGraphicEvent = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                // switch the content if we have the method
                cell.setText(text);
                cell.setGraphic(graphic);
            }
        };
        KeyFrame secondFrame = graphic == null
                ? new KeyFrame(Duration.millis(ANIMATION_DURATION / 2.0), replaceGraphicEvent)
                : new KeyFrame(Duration.millis(ANIMATION_DURATION / 2.0),
                replaceGraphicEvent,
                new KeyValue(graphic.opacityProperty(), 0.0, Interpolator.EASE_BOTH));



        // third key frame
        EventHandler<ActionEvent> fireEvent2 = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                cell.setGraphic(graphic);
            }
        };
        KeyFrame thirdFrame = graphic == null
                ? new KeyFrame(Duration.millis(ANIMATION_DURATION),
                fireEvent2,
                new KeyValue(cell.prefHeightProperty(), endHeight, Interpolator.EASE_BOTH))
                : new KeyFrame(Duration.millis(ANIMATION_DURATION),
                fireEvent2,
                new KeyValue(cell.prefHeightProperty(), endHeight, Interpolator.EASE_BOTH),
                new KeyValue(graphic.opacityProperty(), endOpacity, Interpolator.EASE_BOTH));

        timeline.getKeyFrames().addAll(
                firstFrame,
                secondFrame,
                thirdFrame);

        timeline.playFromStart();
    }

    public static class CellResizeFeatures<T> {
        public final Cell<T> cell;
        public final boolean isExpanded;

        public CellResizeFeatures(Cell<T> cell, boolean isExpanded) {
            this.cell = cell;
            this.isExpanded = isExpanded;
        }
    }

    public static class CellUpdate {
        private Node graphic;
        private String text;

        public CellUpdate(String text, Node graphic) {
            this.text = text;
            this.graphic = graphic;
        }
    }
}