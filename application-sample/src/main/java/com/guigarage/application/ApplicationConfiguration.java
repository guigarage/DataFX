package com.guigarage.application;

import io.datafx.core.concurrent.ObservableExecutor;
import io.datafx.core.concurrent.StreamFX;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class ApplicationConfiguration {

    private final ObservableExecutor executor;

    private ReadOnlyBooleanProperty primaryStageInFullScreen;

    private ObservableList<Image> primaryStageIcons;

    private ReadOnlyStringProperty primaryStageTitle;

    private ReadOnlyBooleanProperty primaryStageIconified;

    private ReadOnlyBooleanProperty primaryStageMaximized;

    private ReadOnlyBooleanProperty primaryStageAlwaysOnTop;

    private ReadOnlyBooleanProperty primaryStageResizable;

    public ApplicationConfiguration() {
        executor = new ObservableExecutor(new ApplicationExceptionHandler());
    }

    public ObservableExecutor getExecutor() {
        return executor;
    }

    public boolean isPrimaryStageInFullScreen() {
        return primaryStageInFullScreen.get();
    }

    public ReadOnlyBooleanProperty primaryStageInFullScreenProperty() {
        return primaryStageInFullScreen;
    }

    public StreamFX<Image> getPrimaryStageIcons() {
        return new StreamFX<>(primaryStageIcons.stream());
    }

    public String getPrimaryStageTitle() {
        return primaryStageTitle.get();
    }

    public ReadOnlyStringProperty primaryStageTitleProperty() {
        return primaryStageTitle;
    }

    public boolean isPrimaryStageIconified() {
        return primaryStageIconified.get();
    }

    public ReadOnlyBooleanProperty primaryStageIconifiedProperty() {
        return primaryStageIconified;
    }

    public boolean isPrimaryStageMaximized() {
        return primaryStageMaximized.get();
    }

    public ReadOnlyBooleanProperty primaryStageMaximizedProperty() {
        return primaryStageMaximized;
    }

    public boolean isPrimaryStageAlwaysOnTop() {
        return primaryStageAlwaysOnTop.get();
    }

    public ReadOnlyBooleanProperty primaryStageAlwaysOnTopProperty() {
        return primaryStageAlwaysOnTop;
    }

    public boolean isPrimaryStageResizable() {
        return primaryStageResizable.get();
    }

    public ReadOnlyBooleanProperty primaryStageResizableProperty() {
        return primaryStageResizable;
    }
}
