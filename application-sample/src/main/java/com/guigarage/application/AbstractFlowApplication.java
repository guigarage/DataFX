package com.guigarage.application;

import io.datafx.controller.flow.Flow;
import io.datafx.core.Assert;
import javafx.application.Application;
import javafx.stage.Stage;

public abstract class AbstractFlowApplication extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        configurePrimaryStage(primaryStage);
        Flow flow = createApplicationFlow();
        Assert.requireNonNull(flow, "flow");
        flow.startInStage(primaryStage);
    }

    protected void configurePrimaryStage(final Stage primaryStage) {
        ApplicationConfiguration applicationConfiguration = getApplicationConfiguration();
        Assert.requireNonNull(applicationConfiguration, "applicationConfiguration");

        applicationConfiguration.primaryStageAlwaysOnTopProperty().addListener((obs, oldVal, newVal) -> primaryStage.setAlwaysOnTop(newVal));
        primaryStage.setAlwaysOnTop(applicationConfiguration.isPrimaryStageAlwaysOnTop());
    }

    protected abstract Flow createApplicationFlow();

    protected abstract ApplicationConfiguration getApplicationConfiguration();

}
