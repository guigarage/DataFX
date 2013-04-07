/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.provider;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
public class SingleObjectDataProvider<T> implements DataProvider<T> {
 private ObjectProperty<T> objectProperty;
    private Executor executor;
	private final DataReader<T> reader;

    public SingleObjectDataProvider(DataReader<T> reader) {
         this(reader,null);
    }

    public SingleObjectDataProvider(DataReader<T> reader, Executor executor) {
         this.reader = reader;
		 this.executor = executor;
		 this.objectProperty = new SimpleObjectProperty<T>();

    }

    public Worker<T> retrieve() {
        final Service<T> retriever = createService(objectProperty);
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


    protected Service<T> createService(ObjectProperty<T> value) {
		System.out.println("Create Service");
        return new Service<T>() {
            @Override protected Task<T> createTask() {
				System.out.println("[JVDBG] CreateTask called");
                final Task<T> task = createReceiverTask(reader);
                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override public void handle(WorkerStateEvent arg0) {
                        T value = null;
                        try {
							System.out.println("[JVDBG] handle got something!");
                            value = task.get();
							System.out.println("[JVDBG] handle got "+value);
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
                        objectProperty.set(value);
                    }
                });
                return task;
            }
        };
    }
	
	
    protected Task<T> createReceiverTask(final DataReader<T> reader) {
		System.out.println("[JVDBG] createReceivertask called");
        Task<T> answer = new Task<T>() {
            @Override protected T call() throws Exception {
				T entry = reader.get();
				System.out.println("[JVDBG] RECEIVERTASK RETURNS "+entry);
                return entry;
            }
        };
        return answer;
    }

    @Override public ObjectProperty<T> getData() {
        return objectProperty;
    }
	
}
