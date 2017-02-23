package com.guigarage.application;

import io.datafx.controller.flow.Flow;

public class SampleApplication extends AbstractFlowApplication {

    private ApplicationConfiguration applicationConfiguration;

    @Override
    public void init() throws Exception {
        applicationConfiguration = new ApplicationConfiguration();
    }

    protected Flow createApplicationFlow() {
        return null;
    }

    public ApplicationConfiguration getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
