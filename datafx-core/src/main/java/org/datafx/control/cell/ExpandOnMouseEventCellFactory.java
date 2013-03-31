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

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.datafx.control.cell.ExpandOnMouseEventListCell.CellResizeFeatures;
import org.datafx.control.cell.ExpandOnMouseEventListCell.CellUpdate;
import org.datafx.control.cell.ExpandOnMouseEventTableRow.CellResizeEvent;

/**
 * A class containing cell factories that make it easy to
 *
 * @author <a href="http://www.jonathangiles.net">Jonathan Giles</a>
 */
public class ExpandOnMouseEventCellFactory {
    private ExpandOnMouseEventCellFactory() {
    }

    /**
     * Returns a cell factory that creates a default
     * {@link ExpandOnMouseEventListCell} where there can only be one expanded
     * cell at any one time.
     */
    public static <T> Callback<ListView<T>, ListCell<T>> forListView() {
        return forListView(1);
    }

    /**
     * Returns a cell factory that creates a {@link ExpandOnMouseEventListCell},
     * where there can only be
     * <code>maxExpandedCells</code> at any one time.
     */
    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final int maxExpandedCells) {
        return forListView(maxExpandedCells, ExpandOnMouseEventListCell.DEFAULT_EXPAND_HEIGHT, MouseEvent.MOUSE_CLICKED);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final int maxExpandedCells, final double expandHeight) {
        return forListView(maxExpandedCells, expandHeight, MouseEvent.MOUSE_CLICKED);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final int maxExpandedCells, EventType<MouseEvent> mouseEventType) {
        return forListView(maxExpandedCells, ExpandOnMouseEventListCell.DEFAULT_EXPAND_HEIGHT, mouseEventType);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final int maxExpandedCells, final double expandHeight, EventType<MouseEvent> mouseEventType) {
        return forListView(maxExpandedCells, expandHeight, mouseEventType, null);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final int maxExpandedCells, final Callback<CellResizeFeatures<T>, CellUpdate> onCellResizeStarting) {
        return forListView(maxExpandedCells, ExpandOnMouseEventListCell.DEFAULT_EXPAND_HEIGHT, MouseEvent.MOUSE_CLICKED, onCellResizeStarting);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final int maxExpandedCells, final double expandHeight, final Callback<CellResizeFeatures<T>, CellUpdate> onCellResizeStarting) {
        return forListView(maxExpandedCells, expandHeight, MouseEvent.MOUSE_CLICKED, onCellResizeStarting);
    }

    /**
     * Returns a cell factory that creates a {@link ExpandOnMouseEventListCell},
     * where there can only be
     * <code>maxExpandedCells</code> at any one time.
     */
    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final int maxExpandedCells, final double expandHeight, final EventType<MouseEvent> mouseEventType, final Callback<CellResizeFeatures<T>, CellUpdate> onCellResizeStarting) {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override public ListCell<T> call(ListView<T> list) {
                ExpandOnMouseEventListCell<T> cell = new ExpandOnMouseEventListCell<T>(maxExpandedCells, expandHeight, mouseEventType);
                cell.setOnCellResizeStarting(onCellResizeStarting);
                return cell;
            }
        };
    }

    public static <S> Callback<TableView<S>, TableRow<S>> forTableView() {
        return forTableView(1);
    }

    public static <S> Callback<TableView<S>, TableRow<S>> forTableView(final int maxExpandedCells) {
        return forTableView(maxExpandedCells, ExpandOnMouseEventTableRow.DEFAULT_EXPAND_HEIGHT, MouseEvent.MOUSE_CLICKED);
    }

    public static <S> Callback<TableView<S>, TableRow<S>> forTableView(final int maxExpandedCells, final double expandHeight) {
        return forTableView(maxExpandedCells, expandHeight, MouseEvent.MOUSE_CLICKED);
    }

    public static <S> Callback<TableView<S>, TableRow<S>> forTableView(final int maxExpandedCells, EventType<MouseEvent> mouseEventType) {
        return forTableView(maxExpandedCells, ExpandOnMouseEventTableRow.DEFAULT_EXPAND_HEIGHT, mouseEventType);
    }

//    public static <S> Callback<TableView<S>, TableRow<S>> forTableView(final int maxExpandedCells, final double expandHeight, EventType<MouseEvent> mouseEventType) {
//        return forTableView(maxExpandedCells, expandHeight, mouseEventType);
//    }
    private static <S> Callback<TableView<S>, TableRow<S>> forTableView(final int maxExpandedCells, final EventHandler<CellResizeEvent<S>> onCellResize) {
        return forTableView(maxExpandedCells, ExpandOnMouseEventTableRow.DEFAULT_EXPAND_HEIGHT, MouseEvent.MOUSE_CLICKED/*, onCellResize*/);
    }

    private static <S> Callback<TableView<S>, TableRow<S>> forTableView(final int maxExpandedCells, final double expandHeight, final EventHandler<CellResizeEvent<S>> onCellResize) {
        return forTableView(maxExpandedCells, expandHeight, MouseEvent.MOUSE_CLICKED/*, onCellResize*/);
    }

    public static <S> Callback<TableView<S>, TableRow<S>> forTableView(final int maxExpandedCells, final double expandHeight, final EventType<MouseEvent> mouseEventType/*, final EventHandler<CellResizeEvent<S>> onCellResize*/) {
        return new Callback<TableView<S>, TableRow<S>>() {
            @Override public TableRow<S> call(TableView<S> list) {
                ExpandOnMouseEventTableRow<S> cell = new ExpandOnMouseEventTableRow<S>(maxExpandedCells, expandHeight, mouseEventType);
//                cell.setOnCellResize(onCellResize);
                return cell;
            }
        };
    }
}