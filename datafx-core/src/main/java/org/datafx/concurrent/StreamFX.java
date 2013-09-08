package org.datafx.concurrent;

import java.util.function.Consumer;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

/**
 * Stream Helper class for JavaFX. Combines the Stream API with the JavaFX platform thread
 * @author hendrikebbers
 *
 * @param <T> type of elements in Stream
 */
public class StreamFX<T> {

	private Stream<T> stream;
	
	/**
	 * Create a StreamFX as a wrapper of a Stream
	 * @param stream the stream to wrap
	 */
	public StreamFX(Stream<T> stream) {
		this.stream = stream;
	}
	
	/**
	 * Performs an action for each element of this stream.
	 * Each element is wrapped in a ObjectProperty and the action will run on the JavaFX Platform Thread
	 * @param action action to perform on the elements
	 */
	void forEach(final Consumer<ObjectProperty<? super T>> action) {
		stream.forEach(new Consumer<T>() {

			@Override
			public void accept(final T t) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						action.accept(new SimpleObjectProperty<T>(t));
					}
				});
			}

		});
	}
	
	/**
	 * Performs an action for each element of this stream.
	 * Each element is wrapped in a ObjectProperty and the action will run on the JavaFX Platform Thread
	 * each element is processed in encounter order for streams that have a
	 * defined encounter order
	 * @param action action to perform on the elements
	 */
	void forEachOrdered(final Consumer<ObjectProperty<? super T>> action) {
		stream.forEachOrdered(new Consumer<T>() {

			@Override
			public void accept(final T t) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						action.accept(new SimpleObjectProperty<T>(t));
					}
				});
			}

		});
	}
	
	/**
	 * Publishes all elements of the stream to a ObservableList. 
	 * the action for each element will run on the JavaFX Platform Thread
	 * @param list List to publish all elements to
	 */
	public void publish(final ObservableList<T> list) {
		stream.forEach(new Consumer<T>() {

			@Override
			public void accept(final T t) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						list.add(t);
					}
				});
			}

		});
	}
	
	/**
	 * Publishes all elements of the stream to a ObservableList. 
	 * the action for each element will run on the JavaFX Platform Thread
	 * each element is processed in encounter order for streams that have a
	 * defined encounter order
	 * @param list List to publish all elements to
	 */
	public void publishOrderer(final ObservableList<T> list) {
		stream.forEachOrdered(new Consumer<T>() {

			@Override
			public void accept(final T t) {
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						list.add(t);
					}
				});
			}

		});
	}
}
