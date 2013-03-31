package org.datafx.provider;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.WritableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.datafx.concurrent.ObservableExecutor;
import org.datafx.reader.DataReader;

/**
 *
 * @author johan
 */
public class ListObjectDataProvider<T> implements DataProvider<ObservableList<T>>{
	
	private ListProperty<T> listProperty;
    final private DataReader<T> reader;
    private Executor executor;

	public ListObjectDataProvider (DataReader<T> reader) {
		this (reader, null, null);
	}

    public ListObjectDataProvider(DataReader<T> reader, Executor executor, ListProperty<T> existingList) {
		this.reader = reader;
        this.executor = executor;
        if (existingList != null) {
            this.listProperty = existingList;
        } else {
            this.listProperty = new SimpleListProperty<T>(FXCollections.<T>observableArrayList());
        }
    }

    public void setReader(DataReader<T> reader) {
        reader = reader;
    }

    public DataReader<T> getReader() {
        return reader;
    }

    public Worker<ObservableList<T>> retrieve() {
        final Service<ObservableList<T>> retriever = createService(listProperty);
        retriever.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override public void handle(WorkerStateEvent arg0) {
                System.err.println("Default DataFX error handler:");
                retriever.getException().printStackTrace();
            }
        });

        if (executor != null && executor instanceof ObservableExecutor) {
            return ((ObservableExecutor) executor).submit(retriever);
        } else {
            if (executor != null) {
                retriever.setExecutor(executor);
            }
            retriever.start();
            
            return retriever;
        }
    }

    protected Service<ObservableList<T>> createService(WritableListValue<T> value) {
        return new Service<ObservableList<T>>() {
            @Override protected Task<ObservableList<T>> createTask() {
                final Task<ObservableList<T>> task = createReceiverTask();
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override public void handle(WorkerStateEvent arg0) {
                        List<T> value = null;
                        try {
                            value = task.get();
                        } catch (InterruptedException e) {
                            // Execution of the task was not working. So we do
                            // not need
                            // to update the property
                            return;
                        } catch (ExecutionException e) {
                            // Execution of the task was not working. So we do
                            // not need
                            // to update the property
                            return;
                        }

                    }
                });
                return task;
            }
        };
    }
	
 protected PublishingTask<T> createPublishingReceiverTask() {

        PublishingTask<T> answer = new PublishingTask<T>() {
            @Override protected void callTask() throws Exception {
                System.out.println("CallTask called");
				while (getReader().hasMoreData()) {
					final T entry = getReader().getData();
                    System.out.println("Got data: "+entry);
					publish (entry);
				}
                System.out.println("No more data");
             
            }
        };
        return answer;
    }
 
    protected final Task<ObservableList<T>> createReceiverTask() {
        PublishingTask<T> task = createPublishingReceiverTask();
        listProperty.bind(task.getPublishedValues());
        return task;
    }

  
    public Executor getExecutor() {
        return executor;
    }

   public ListProperty<T> getData() {
        return listProperty;
    }



}
