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

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.datafx.control.TableViewFactory;

/**
 * <p>A class containing a {@link TableCell} implementation for a money cell,
 * which can format numbers such that they appear as to represent a currency.
 *
 * <img src="http://www.javafxdata.org/screenshots/MoneyCell-TableView.PNG"
 * style="float: right; padding: 0px 0px 0px 3px;"/>
 *
 * <p>Note that this class extends {@link CSSTableCell}, which allows for
 * easy CSS styling. This class adds the
 * <code>money-cell</code> style class.
 * In addition, it adds two pseudo class states:
 * <code>negative</code> and
 * <code>positive</code>. Using these, it is possible
 * to provide CSS styling to customise the look of the cell. For example, the
 * default CSS for the Money*Cell implementations is the following:
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
public class MoneyTableCell<S, T extends Number> extends LabelTableCell<S, T> {
    private final Locale locale;

    /**
     * Creates a default cell with a style class of 'money-cell'.
     */
    public MoneyTableCell() {
        this(Locale.getDefault());
    }

    /**
     * Creates a default cell with the given {@link Locale}, and the default
     * style class of 'money-cell'.
     *
     * @param locale The locale to use when rendering the number as a currency.
     * If this is null, the the default (i.e. local) locale is used.
     */
    public MoneyTableCell(Locale locale) {
        this.getStyleClass().add("money-cell");
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override protected String getUserAgentStylesheet() {
        URL resource = TableViewFactory.class.getResource("cells.css");
        if (resource != null) {
            return resource.toString();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override public void getCssState(List<String> s) {
        MoneyCell.getCssState(this, s);
    }

    /**
     * {@inheritDoc}
     */
    @Override public String toString(T item) {
        String str = item == null ? ""
                : NumberFormat.getCurrencyInstance(locale).format(item);

        stateChanged(MoneyCellFactory.NEGATIVE_STATE, MoneyCellFactory.POSITIVE_STATE);

        return str;
    }
;
}