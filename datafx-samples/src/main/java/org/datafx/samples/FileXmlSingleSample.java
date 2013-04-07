package org.datafx.samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.datafx.provider.SingleObjectDataProvider;
import org.datafx.reader.FileSource;

/**
 *
 * @author johan
 */
class FileXmlSingleSample {

	public FileXmlSingleSample() {
	}

	public Node getContent(Scene scene) {
		// TabPane
		final TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.setPrefWidth(scene.getWidth());
		tabPane.setPrefHeight(scene.getHeight());

		tabPane.prefWidthProperty().bind(scene.widthProperty());
		tabPane.prefHeightProperty().bind(scene.heightProperty());

		Tab localTab = new Tab("local");
		buildLocalTab(localTab);
		tabPane.getTabs().add(localTab);

		Tab networkTab = new Tab("network");
		buildNetworkTab(networkTab);
		tabPane.getTabs().add(networkTab);

		return tabPane;
	}

	private void buildLocalTab(Tab tab) {
		try {
			URL resource = this.getClass().getResource("singlebook.xml");
			FileSource<Book> dr = new FileSource<Book>(new File(resource.getFile()));
			SingleObjectDataProvider<Book> sodp = new SingleObjectDataProvider<Book>(dr);
			sodp.retrieve();
			final Label title = new Label("HELLO");
			final ObjectProperty<Book> op = sodp.getData();
			op.addListener(new InvalidationListener() {
				@Override public void invalidated(Observable o) {
					Book book = op.get();
					title.setText(book.getTitle());
				}
			});
			tab.setContent(title);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileXmlSingleSample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void buildNetworkTab(Tab tab) {
	}
}
