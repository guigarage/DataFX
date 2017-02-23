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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import io.datafx.controller.context.ViewContext;
import io.datafx.controller.flow.FlowContainer;

import java.util.List;
import java.util.function.Function;

/**
 * A {@link FlowContainer} that supports animation for the view change.
 */
public class AnimatedFlowContainer implements FlowContainer<StackPane> {

    private StackPane root;
    private Duration duration;
    private Function<AnimatedFlowContainer, List<KeyFrame>> animationProducer;
    private Timeline animation;
    private ImageView placeholder;

    /**
     * Defaults constructor that creates a container with a fade animation that last 320 ms.
     */
    public AnimatedFlowContainer() {
        this(Duration.millis(320));
    }

    /**
     *  Creates a container with a fade animation and the given duration
     *
     * @param duration the duration of the animation
     */
    public AnimatedFlowContainer(Duration duration) {
        this(duration, ContainerAnimations.FADE);
    }

    /**
     *  Creates a container with the given animation type and  duration
     *
     * @param duration the duration of the animation
     * @param animation the animation type
     */
    public AnimatedFlowContainer(Duration duration, ContainerAnimations animation) {
        this(duration, animation.getAnimationProducer());
    }

    /**
     *    Creates a container with the given animation type and duration
     * @param duration  the duration of the animation
     * @param animationProducer   the {@link KeyFrame} instances that define the animation
     */
    public AnimatedFlowContainer(Duration duration, Function<AnimatedFlowContainer, List<KeyFrame>> animationProducer) {
        this.root = new StackPane();
        this.duration = duration;
        this.animationProducer = animationProducer;
        placeholder = new ImageView();
        placeholder.setPreserveRatio(true);
        placeholder.setSmooth(true);
    }

    @Override
    public <U> void setViewContext(ViewContext<U> context) {
        updatePlaceholder(context.getRootNode());

        if (animation != null) {
            animation.stop();
        }

        animation = new Timeline();
        animation.getKeyFrames().addAll(animationProducer.apply(this));
        animation.getKeyFrames().add(new KeyFrame(duration, (e) -> clearPlaceholder()));

        animation.play();
    }

    /**
     * Returns the {@link ImageView} instance that is used as a placeholder for the old view in each navigation animation.
     * @return
     */
    public ImageView getPlaceholder() {
        return placeholder;
    }

    /**
     * Returns the duration for the animation
     * @return the duration for the animation
     */
    public Duration getDuration() {
        return duration;
    }

    @Override
    public StackPane getView() {
        return root;
    }

    private void clearPlaceholder() {
        placeholder.setImage(null);
        placeholder.setVisible(false);
    }

    private void updatePlaceholder(Node newView) {
        if (root.getWidth() > 0 && root.getHeight() > 0) {
            Image placeholderImage = root.snapshot(null, new WritableImage((int) root.getWidth(), (int) root.getHeight()));
            placeholder.setImage(placeholderImage);
            placeholder.setFitWidth(placeholderImage.getWidth());
            placeholder.setFitHeight(placeholderImage.getHeight());
        } else {
            placeholder.setImage(null);
        }
        placeholder.setVisible(true);
        placeholder.setOpacity(1.0);
        root.getChildren().setAll(placeholder);
        root.getChildren().add(newView);
        placeholder.toFront();

    }
}
