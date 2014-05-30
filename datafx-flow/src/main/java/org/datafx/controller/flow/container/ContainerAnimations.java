package org.datafx.controller.flow.container;

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
