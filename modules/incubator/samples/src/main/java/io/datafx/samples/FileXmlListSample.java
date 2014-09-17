/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.datafx.io.converter.XmlConverter;
import io.datafx.provider.ListDataProvider;
import io.datafx.io.FileSource;
import javafx.beans.property.ListProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 *
 * @author johan
 */
public class FileXmlListSample {
	
	public FileXmlListSample() {
	}

	public Node getContent(Scene scene) throws IOException {
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

	private void buildLocalTab(Tab tab) throws IOException {
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
