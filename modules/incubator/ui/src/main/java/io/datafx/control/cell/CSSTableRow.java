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

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TableRow;

/**
 * Convenience extension to the {@link javafx.scene.control.TableRow} class to expose a basic
 * CSS implementation (whilst the real JavaFX implementation is in
 * development). The only methods that should be used are {@link #getCssState(java.util.List)}
 * and {@link #stateChanged(String, String...)}. Refer to these methods
 * for more information on how to use them in your custom cells.
 *
 * @author <a href="http://www.jonathangiles.net">Jonathan Giles</a>
 * @param <S> The type of the item contained within the cell.
 */
public abstract class CSSTableRow<S> extends TableRow<S> {
    /**
     * Override this method to specify which pseudo class states the
     * cell is in, based on the {@link #itemProperty() item} that is
     * currently contained within it. For example, the {@link MoneyCellFactory}
     * implementation of this method might resemble the following:
     *
     * <pre>
     * TODO: Example removed
     * </pre>
     *
     * Doing this, it is now posible to style the cell based on these
     * psuedo class states. For example, assuming the {@link MoneyCellFactory}
     * had a style class of 'money-cell' (which it does), you could then
     * have CSS along the lines of the following:
     *
     * <pre>
     * .money-cell:positive {
     *   -fx-text-fill: green;
     * }
     * .money-cell:negative {
     *   -fx-text-fill: red;
     * }
     * </pre>
     *
     * This CSS results in positive values being green, and negative values
     * being red. By exposing the pseudo class states in CSS, it is possible
     * for the cell to be totally styled using CSS. This makes it trivial to
     * reuse the cell with an entirely different look.
     *
     * @param s A {@link java.util.List} of Strings which represent the current state
     * of the cell. It is legal to add further strings to the list, to
     * specify further states.
     */
    public void getCssState(List<String> s) {
        // no-op by default
    }

    /**
     * Call this method from the {@link #updateItem(Object, boolean) updateItem}
     * method when the item changes, and therefore the state of the cell has
     * changed. The strings passed to this method should be the same as those
     * that are provided via {@link #getCssState(java.util.List) getCssState}.
     *
     * <p>Only call this method using the strings of states that have actually
     * changed. For example, when the MoneyCell changes from being negative to
     * positive, call
     * <code>stateChanged("negative", "positive")</code>. Note
     * that order does not matter.
     */
    protected final void stateChanged(String state, String... states) {
        // TODO port me
//        impl_pseudoClassStateChanged(state);
//        for (String s : states) {
//            impl_pseudoClassStateChanged(s);
//        }
    }

    /**
     * @treatasprivate implementation detail
     * @deprecated This is an internal API that is not intended for use
     * and will be removed in the next version.
     */
    // TODO port me
//    @Deprecated @Override public long impl_getPseudoClassState() {
//        List<String> states = new ArrayList<String>();
//        getCssState(states);
//        return CSSCell.getPseudoClassState(getScene(), super.impl_getPseudoClassState(), states);
//    }
}