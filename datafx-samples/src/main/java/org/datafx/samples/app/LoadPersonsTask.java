package org.datafx.samples.app;

import javax.inject.Inject;

public class LoadPersonsTask implements Runnable {

    @Inject
    private DataModel model;

    @Inject
    private PersonDataHandler dataHandler;

    @Override
    public void run() {
        model.getPersons().setAll(dataHandler.loadAllPersons());
    }
}
