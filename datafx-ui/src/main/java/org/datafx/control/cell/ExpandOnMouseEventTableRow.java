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

import com.sun.javafx.scene.control.skin.TableRowSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import static org.datafx.control.cell.ExpandOnMouseEventCell.ANIMATION_DURATION;
import static org.datafx.control.cell.ExpandOnMouseEventCell.expandedCellsMap;
import static org.datafx.control.cell.ExpandOnMouseEventCell.expandedIndicesMap;

/**
 * <p>A class containing a {@link TableRow} implementation that expands when a
 * certain mouse event occurs. When the row expands, it allows for additional
 * information to be shown to the user in all columns.
 *
 * <img src="http://www.javafxdata.org/screenshots/ExpandOnMouseEventCell-TableView.PNG"
 * style="float: right; padding: 0px 0px 0px 3px;"/>
 *
 * @param <S> The type of the elements contained within the TableView.
 * @author <a href="http://www.jonathangiles.net">Jonathan Giles</a>
 */
public class ExpandOnMouseEventTableRow<S> extends TableRow<S> {
    public static final double DEFAULT_PREF_HEIGHT = 24;
    public static final double DEFAULT_EXPAND_HEIGHT = DEFAULT_PREF_HEIGHT * 3;

    @SuppressWarnings("unchecked")
    public static <T> EventType<CellResizeEvent<T>> cellResizeEvent() {
        return (EventType<CellResizeEvent<T>>) CELL_RESIZE_EVENT;
    }
    private static final EventType<?> CELL_RESIZE_EVENT =
            new EventType<Event>(Event.ANY, "CELL_RESIZE");
    private int maxExpandedCells = 1;
    private final double expandedHeight;

    public ExpandOnMouseEventTableRow() {
        this(1);
    }

    public ExpandOnMouseEventTableRow(int maxExpandedCells) {
        this(maxExpandedCells, DEFAULT_EXPAND_HEIGHT, MouseEvent.MOUSE_CLICKED);
    }

    public ExpandOnMouseEventTableRow(int maxExpandedCells, double expandHeight) {
        this(maxExpandedCells, expandHeight, MouseEvent.MOUSE_CLICKED);
    }

    public ExpandOnMouseEventTableRow(int maxExpandedCells, double expandHeight, EventType<MouseEvent> mouseEventType) {
        this.maxExpandedCells = maxExpandedCells >= 0 ? maxExpandedCells : 1;
        this.expandedHeight = expandHeight < 0.0 ? DEFAULT_EXPAND_HEIGHT : expandHeight;
        this.setEditable(false);

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
                flip(ExpandOnMouseEventTableRow.this, getMaxExpandedCells());
            }
        });
    }

    @Override public void updateIndex(int i) {
        super.updateIndex(i);

        if (isExpanded()) {
            // immediately expand this cell
            setPrefHeight(DEFAULT_EXPAND_HEIGHT);
        } else {
            // immediately collapse this cell
            setPrefHeight(DEFAULT_PREF_HEIGHT);
        }
    }

    public void setOnCellResize(EventHandler<CellResizeEvent<S>> evt) {
        if (evt == null) {
            return;
        }
        addEventHandler(ExpandOnMouseEventTableRow.<S>cellResizeEvent(), evt);
    }

    private static void flip(ExpandOnMouseEventTableRow<?> cell, int maxExpandedCells) {
        Queue<Cell> expandedCells = expandedCellsMap.get(cell.getTableView());
        if (expandedCells == null) {
            expandedCells = new LinkedBlockingQueue<Cell>();
            expandedCellsMap.put(cell.getTableView(), expandedCells);
        }

        List<Integer> expandedIndices = expandedIndicesMap.get(cell.getTableView());
        if (expandedIndices == null) {
            expandedIndices = new ArrayList<Integer>();
            expandedIndicesMap.put(cell.getTableView(), expandedIndices);
        }

        boolean cellIsExpanded = expandedCells.contains(cell);

        if (maxExpandedCells <= expandedCells.size() && !cellIsExpanded) {
            ExpandOnMouseEventTableRow collapsed = (ExpandOnMouseEventTableRow) expandedCells.remove();
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
        return isExpanded(this);
    }

    public static boolean isExpanded(ExpandOnMouseEventTableRow<?> cell) {
        if (expandedIndicesMap.containsKey(cell.getTableView())) {
            boolean isExpanded = expandedIndicesMap.get(cell.getTableView()).contains(cell.getIndex());
//            if (isExpanded) {
//                System.out.println("row " + cell.getIndex() + " is expanded: " + isExpanded);
//            }
            return isExpanded;
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

    private static <S> void animate(final ExpandOnMouseEventTableRow<S> cell, final boolean toExpanded) {
        double startHeight = cell.getHeight();

        // the end height is the opposite of the current state - 
        // we are animating out of this state after all
        double endHeight = toExpanded ? cell.expandedHeight : DEFAULT_PREF_HEIGHT;

        // create a timeline to expand/collapse the cell. All this
        // really does is modify the height of the content
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);

        // first key frame
        EventHandler<ActionEvent> fireEvent = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                // if we are collapsing, we inform listeners now
                if (!toExpanded && !isExpanded(cell)) {
                    fireEvents(cell, toExpanded);
//                    cell.fireEvent(new CellResizeEvent<S>(cellResizeEvent(), cell, toExpanded));
                }
            }
        };
        KeyFrame firstFrame = new KeyFrame(Duration.ZERO,
                fireEvent,
                new KeyValue(cell.prefHeightProperty(), startHeight, Interpolator.EASE_BOTH));



        // second key frame
        EventHandler<ActionEvent> fireEvent2 = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                // if we are collapsing, we inform listeners now
                if (toExpanded && isExpanded(cell)) {
                    fireEvents(cell, toExpanded);
//                    cell.fireEvent(new CellResizeEvent<S>(cellResizeEvent(), cell, toExpanded));
                }
            }
        };
        KeyFrame secondFrame = new KeyFrame(Duration.millis(ANIMATION_DURATION),
                fireEvent2,
                new KeyValue(cell.prefHeightProperty(), endHeight, Interpolator.EASE_BOTH));

        timeline.getKeyFrames().addAll(
                firstFrame,
                secondFrame);

        timeline.playFromStart();
    }

    private static <S> void fireEvents(ExpandOnMouseEventTableRow<S> tableRow, boolean toExpanded) {
        CellResizeEvent event = new CellResizeEvent<S>(cellResizeEvent(), tableRow, toExpanded);
        tableRow.fireEvent(event);

        Map<TableColumn, TableCell> cells = event.getCells();
        for (Map.Entry<TableColumn, TableCell> e : cells.entrySet()) {
            if (e.getValue() instanceof ExpandingTableCell) {
                ((ExpandingTableCell) e.getValue()).updateExpanded(toExpanded);
            }
        }
    }

    public static class CellResizeEvent<S> extends Event {
        private final ExpandOnMouseEventTableRow<S> tableRow;
        private final boolean expanded;
        private Map<TableColumn, TableCell> cells;

        public CellResizeEvent(EventType<? extends Event> eventType, ExpandOnMouseEventTableRow<S> tableRow, boolean isExpanded) {
            super(eventType);
            this.tableRow = tableRow;
            this.expanded = isExpanded;
        }

        public ExpandOnMouseEventTableRow<S> getTableRow() {
            return tableRow;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public Map<TableColumn, TableCell> getCells() {
            if (cells != null) {
                return cells;
            }

            if (tableRow.getSkin() instanceof TableRowSkin) {
                TableRowSkin trs = (TableRowSkin) tableRow.getSkin();
                List<Node> children = trs.getChildren();
                if (children == null || children.isEmpty()) {
                    return Collections.emptyMap();
                }

                cells = new WeakHashMap<TableColumn, TableCell>();
                for (int i = 0; i < children.size(); i++) {
                    Node n = children.get(i);

                    if (n instanceof TableCell) {
                        TableCell tableCell = (TableCell) n;
                        if (tableCell.getTableColumn() == null) {
                            continue;
                        }
                        cells.put(tableCell.getTableColumn(), tableCell);
                    }
                }
                return cells;
            }
            return Collections.emptyMap();
        }
    }
}