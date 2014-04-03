package org.datafx.samples.app;

import javax.inject.Inject;

/**
 * A Flow action that removes the currently selected Person instance from the data model
 */
public class RemoveActionTask implements Runnable {

    /**
     * The data model will be injected
     */
    @Inject
    private DataModel model;

    /**
     * Removes the selected person. This method will be called whenever the matching action is called in the Flow.
     */
    @Override
    public void run() {
        model.getPersons().remove(model.getSelectedPersonIndex());
    }
}
