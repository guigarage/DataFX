/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.datafx.provider.ListDataProvider;
import org.datafx.reader.FileSource;
import org.datafx.reader.converter.XmlConverter;

/**
 *
 * @author johan
 */
public class FileXmlListSample {
	
	public FileXmlListSample() {
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
			URL resource = this.getClass().getResource("manybooks.xml");
            XmlConverter<Book> converter = new XmlConverter<Book>("book", Book.class);
			FileSource<Book> dr = new FileSource<Book>(new File(resource.getFile()),converter);
			ListDataProvider<Book> sodp = new ListDataProvider<Book>(dr);
			sodp.retrieve();
		
			final ListProperty<Book> op = sodp.getData();
            ListView<Book> lv = new ListView<Book>(op.get());
			tab.setContent(lv);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileXmlSingleSample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void buildNetworkTab(Tab tab) {
	}
}
