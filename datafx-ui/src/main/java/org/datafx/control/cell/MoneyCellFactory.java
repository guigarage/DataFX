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

import java.util.Locale;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * A class containing cell factories that format numbers such that they appear
 * as to represent a currency.
 *
 * <p>Note that the cells in this class are styled using CSS. The style class
 * for all cells is
 * <code>money-cell</code>. The added pseudo class states are
 * <code>negative</code> and
 * <code>positive</code>. The default CSS for the
 * cells in this class is of the form:
 *
 * <pre>
 * {@code
 * .money-cell {
 *   -fx-text-fill: -fx-text-inner-color;
 * }
 *
 * .money-cell:selected {
 *   -fx-text-fill: white;
 * }
 *
 * .money-cell:positive {
 *   -fx-text-fill: green;
 * }
 *
 * .money-cell:negative {
 *   -fx-text-fill: red;
 * }}
 * </pre>
 *
 * <p>You may customise this CSS in your own applications by providing your own
 * stylesheet that overrides these values.
 *
 * @author <a href="http://www.jonathangiles.net">Jonathan Giles</a>
 */
public class MoneyCellFactory {
    /**
     * A String used to represent cells that have negative Number values in them, which
     * is then exposed via CSS to allow for easy styling of these cells.
     */
    public static final String NEGATIVE_STATE = "negative";
    /**
     * A String used to represent cells that have positive Number values in them, which
     * is then exposed via CSS to allow for easy styling of these cells.
     */
    public static final String POSITIVE_STATE = "positive";

    private MoneyCellFactory() {
    }

    /**
     * Returns a cell factory that creates a default {@link MoneyListCell} with the
     * default Locale for the users computer.
     */
    public static <T extends Number> Callback<ListView<T>, ListCell<T>> forListView() {
        return forListView(Locale.getDefault());
    }

    /**
     * Returns a cell factory that creates a {@link MoneyListCell} with the given Locale.
     */
    public static <T extends Number> Callback<ListView<T>, ListCell<T>> forListView(final Locale locale) {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override public ListCell<T> call(ListView<T> list) {
                return new MoneyListCell<T>(locale);
            }
        };
    }

    /**
     * Returns a cell factory that creates a default {@link MoneyTableCell} with the
     * default Locale for the users computer.
     */
    public static <S, T extends Number> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn() {
        return forTableColumn(null, Locale.getDefault());
    }

    /**
     * Returns a cell factory that creates a default {@link MoneyTableCell} with the
     * default Locale for the users computer.
     */
    public static <S, T extends Number> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(TableColumn<S, T> column) {
        return forTableColumn(column, Locale.getDefault());
    }

    /**
     * Returns a cell factory that creates a {@link MoneyTableCell} with the given Locale.
     */
    public static <S, T extends Number> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(TableColumn<S, T> column, final Locale locale) {
        return new Callback<TableColumn<S, T>, TableCell<S, T>>() {
            @Override public TableCell<S, T> call(TableColumn<S, T> list) {
                return new MoneyTableCell<S, T>(locale);
            }
        };
    }
}
