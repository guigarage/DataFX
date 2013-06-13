package org.datafx.provider;

import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 *
 * @author johan
 */
public abstract class PublishingTask<T> extends Task<ObservableList<T>> {
    private final  ObservableList<T> publishedValues;

    public PublishingTask() {
        this(new SimpleListProperty<T>(FXCollections.<T>observableArrayList()));
    }

    public PublishingTask(ObservableList<T> values) {
        this.publishedValues = values;
    }

    public ObservableList<T> getPublishedValues() {
        return publishedValues;
    }

    @Override protected final ObservableList<T> call() throws Exception {
        callTask();
        return publishedValues;
    }

    protected abstract void callTask() throws Exception;

    public void publish(final T... values) {
        if (values != null && values.length > 0) {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    publishedValues.addAll(Arrays.asList(values));
                }
            });
        }
    }
}