/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.controller.flow.container;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Some default animations for the flow container.
 * This enum implements animations that can be used to define the animation for view changes in a {@link AnimatedFlowContainer}.
 */
public enum ContainerAnimations {
    /**
     * A fade between the old and new view
     */
    FADE((c) ->
            new ArrayList<>(Arrays.asList(new KeyFrame(Duration.ZERO, new KeyValue(c.getPlaceholder().opacityProperty(), 1.0, Interpolator.EASE_BOTH)),
                    new KeyFrame(c.getDuration(), new KeyValue(c.getPlaceholder().opacityProperty(), 0.0, Interpolator.EASE_BOTH))))),
    /**
     * A zoom effect
     */
    ZOOM_IN((c) ->
            new ArrayList<>(Arrays.asList(new KeyFrame(Duration.ZERO, new KeyValue(c.getPlaceholder().scaleXProperty(), 1, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().scaleYProperty(), 1, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().opacityProperty(), 1.0, Interpolator.EASE_BOTH)),
                    new KeyFrame(c.getDuration(), new KeyValue(c.getPlaceholder().scaleXProperty(), 4, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().scaleYProperty(), 4, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().opacityProperty(), 0, Interpolator.EASE_BOTH))))),
    /**
     * A zoom effect
     */
    ZOOM_OUT((c) ->
            new ArrayList<>(Arrays.asList(new KeyFrame(Duration.ZERO, new KeyValue(c.getPlaceholder().scaleXProperty(), 1, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().scaleYProperty(), 1, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().opacityProperty(), 1.0, Interpolator.EASE_BOTH)),
                    new KeyFrame(c.getDuration(), new KeyValue(c.getPlaceholder().scaleXProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().scaleYProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().opacityProperty(), 0, Interpolator.EASE_BOTH))))),
    /**
     * A swipe effect
     */
    SWIPE_LEFT((c) ->
            new ArrayList<>(Arrays.asList(new KeyFrame(Duration.ZERO, new KeyValue(c.getView().translateXProperty(), c.getView().getWidth(), Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().translateXProperty(), -c.getView().getWidth(), Interpolator.EASE_BOTH)),
                    new KeyFrame(c.getDuration(), new KeyValue(c.getView().translateXProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().translateXProperty(), -c.getView().getWidth(), Interpolator.EASE_BOTH))))),
    /**
     * A swipe effect
     */
    SWIPE_RIGHT((c) ->
            new ArrayList<>(Arrays.asList(new KeyFrame(Duration.ZERO, new KeyValue(c.getView().translateXProperty(), -c.getView().getWidth(), Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().translateXProperty(), c.getView().getWidth(), Interpolator.EASE_BOTH)),
                    new KeyFrame(c.getDuration(), new KeyValue(c.getView().translateXProperty(), 0, Interpolator.EASE_BOTH), new KeyValue(c.getPlaceholder().translateXProperty(), c.getView().getWidth(), Interpolator.EASE_BOTH)))));

    private Function<AnimatedFlowContainer, List<KeyFrame>> animationProducer;

    private ContainerAnimations(Function<AnimatedFlowContainer, List<KeyFrame>> animationProducer) {
        this.animationProducer = animationProducer;
    }

    /**
     * Returns the list of {@link KeyFrame} instances that defines the animation.
     * @return
     */
    public Function<AnimatedFlowContainer, List<KeyFrame>> getAnimationProducer() {
        return animationProducer;
    }
}
