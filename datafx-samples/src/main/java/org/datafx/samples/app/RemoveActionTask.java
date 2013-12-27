package org.datafx.samples.app;

import javax.inject.Inject;

public class RemoveActionTask implements Runnable {

    @Inject
    private DataModel model;

    @Override
    public void run() {
        model.getPersons().remove(model.getSelectedPersonIndex());
    }
}
