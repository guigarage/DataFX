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

import java.text.Format;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 * <p>A class containing a {@link TableCell} implementation that draws a {@link Label}
 * node inside the cell. This Label is exposed to the developer to allow for easy customisation,
 * either via code or using the APIs exposed in the {@link CSSTableCell} class, from which this
 * class extends.
 *
 * @param <T> The type of the elements contained within the TableColumn.
 * @author <a href="http://www.jonathangiles.net">Jonathan Giles</a>
 */
public class LabelTableCell<S, T> extends CSSTableCell<S, T> {
    private Format format;

    /**
     * Creates a default {@link LabelTableCell} instance with the label position set
     * to {@link TextAlignment#LEFT}.
     */
    public LabelTableCell() {
        this(TextAlignment.LEFT);
    }

    public LabelTableCell(Format format) {
        this(TextAlignment.LEFT, format);
    }

    /**
     * Creates a {@link LabelTableCell} instance with the label position set
     * to the provided {@link TextAlignment} value.
     */
    public LabelTableCell(TextAlignment align) {
        this(align, null);
    }

    public LabelTableCell(TextAlignment align, Format format) {
        this.getStyleClass().add("label-cell");
        setTextAlignment(align);
        setFormat(format);
    }

    // --- Format
    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
        doUpdate();
    }

    /**
     * A method that can be overridden by the developer to specify an alternate
     * formatting of the given item argument. By default, this just calls toString()
     * on the item, or returns an empty string if the item is null.
     *
     * @param item The item for which a String representation is required.
     * @return An empty string if item is null, or a string representation if it is not.
     */
    public String toString(T item) {
        return item == null ? "" : item.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override public void updateItem(T item, boolean empty) {
        if (item == getItem()) {
            return;
        }

        super.updateItem(item, empty);

        doUpdate(item, empty);
    }

    private void doUpdate() {
        doUpdate(getItem(), isEmpty());
    }

    private void doUpdate(T item, boolean empty) {
        if (item == null || empty) {
            super.setText(null);
            super.setGraphic(null);
        } else if (format != null) {
            super.setText(format.format(item));
        } else if (item instanceof Node) {
            super.setText(null);
            super.setGraphic((Node) item);
        } else {
            super.setText(toString(item));
            super.setGraphic(null);
        }
    }
}