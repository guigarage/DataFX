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
package io.datafx.control.cell;

import java.text.Format;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 * A class containing cell factories that make it easy to manipulate the String representation of
 * items contained within ListView, TreeView and TableView controls.
 *
 * @author <a href="http://www.jonathangiles.net">Jonathan Giles</a>
 */
public class LabelCellFactory {
    private LabelCellFactory() {
    }

    /**
     * Returns a cell factory that creates a default {@link LabelListCell}.
     */
    public static <T> Callback<ListView<T>, ListCell<T>> forListView() {
        return forListView(TextAlignment.LEFT);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(TextAlignment align) {
        return forListView(align, null);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(Format format) {
        return forListView(TextAlignment.LEFT, format);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final TextAlignment align, final Format format) {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override public ListCell<T> call(ListView<T> list) {
                return new LabelListCell<T>(align, format);
            }
        };
    }

    /**
     * Returns a cell factory that creates a {@link LabelListCell} that uses the provided {@link javafx.util.Callback}
     * to convert from the generic T type to a String, such that it is rendered appropriately.
     *
     * @param toString A {@link javafx.util.Callback} that converts an instance of type T to a String, for rendering
     * in the {@link io.datafx.control.cell.LabelCellFactory}.
     */
    public static <T> Callback<ListView<T>, ListCell<T>> forListView(final Callback<T, String> toString) {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override public ListCell<T> call(ListView<T> list) {
                return new LabelListCell<T>() {
                    @Override public String toString(T item) {
                        return toString == null ? super.toString(item) : toString.call(item);
                    }
                ;
            }
        ;
    }

    };
    }
    
    /**
     * Returns a cell factory that creates a default {@link LabelTreeCell}.
     */
    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView() {
        return forTreeView(TextAlignment.LEFT);
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(TextAlignment align) {
        return forTreeView(align, null);
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(Format format) {
        return forTreeView(TextAlignment.LEFT, format);
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(final TextAlignment align, final Format format) {
        return new Callback<TreeView<T>, TreeCell<T>>() {
            @Override public TreeCell<T> call(TreeView<T> list) {
                return new LabelTreeCell<T>(align, format);
            }
        };
    }

    /**
     * Returns a cell factory that creates a {@link LabelTableCell} that uses the provided {@link javafx.util.Callback}
     * to convert from the generic T type to a String, such that it is rendered appropriately.
     *
     * @param toString A {@link javafx.util.Callback} that converts an instance of type T to a String, for rendering
     * in the {@link javafx.scene.control.TableCell}.
     */
    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(final Callback<T, String> toString) {
        return new Callback<TreeView<T>, TreeCell<T>>() {
            @Override public TreeCell<T> call(TreeView<T> list) {
                return new LabelTreeCell<T>() {
                    @Override public String toString(T item) {
                        return toString == null ? super.toString(item) : toString.call(item);
                    }
                ;
            }
        ;
    }

    };
    }
    
    /**
     * Returns a cell factory that creates a default {@link LabelTableCell}.
     */
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn() {
        return forTableColumn(TextAlignment.LEFT);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(TextAlignment align) {
        return forTableColumn(align, null);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(Format format) {
        return forTableColumn(TextAlignment.LEFT, format);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final TextAlignment align, final Format format) {
        return new Callback<TableColumn<S, T>, TableCell<S, T>>() {
            @Override public TableCell<S, T> call(TableColumn<S, T> list) {
                return new LabelTableCell<S, T>(align, format);
            }
        };
    }

    /**
     * Returns a cell factory that creates a {@link LabelTreeCell} that uses the provided {@link javafx.util.Callback}
     * to convert from the generic T type to a String, such that it is rendered appropriately.
     *
     * @param toString A {@link javafx.util.Callback} that converts an instance of type T to a String, for rendering
     * in the {@link javafx.scene.control.TreeCell}.
     */
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(final Callback<T, String> toString) {
        return new Callback<TableColumn<S, T>, TableCell<S, T>>() {
            @Override public TableCell<S, T> call(TableColumn<S, T> list) {
                return new LabelTableCell<S, T>() {
                    @Override public String toString(T item) {
                        return toString == null ? super.toString(item) : toString.call(item);
                    }
                ;
            }
        ;
    }
};
}
}