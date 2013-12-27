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
package org.datafx.samples;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.datafx.provider.ListDataProvider;
import org.datafx.reader.RestSource;
import org.datafx.reader.converter.XmlConverter;

/**
 *
 * @author johan
 */
public class RestSample {

    public RestSample() {
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
//
//		Tab networkTab = new Tab("network");
//		buildNetworkTab(networkTab);
//		tabPane.getTabs().add(networkTab);

        return tabPane;
    }

    private void buildLocalTab(Tab tab) {
        try {
            XmlConverter converter = new XmlConverter("item", Tweet.class);
            RestSource dr = new RestSource("http://search.twitter.com", converter);
            dr.setPath("search.rss?q=javafx");
            ListDataProvider<Tweet> sodp = new ListDataProvider(dr);
            sodp.retrieve();

            final ListProperty<Tweet> op = sodp.getData();
            ListView lv = new ListView(op.get());
            tab.setContent(lv);
        } catch (Exception ex) {
            Logger.getLogger(RestSample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class Tweet {

        private StringProperty titleProperty = new SimpleStringProperty();
        private StringProperty authorProperty = new SimpleStringProperty();
        private StringProperty pubDateProperty = new SimpleStringProperty();

        public String getTitle() {
            return titleProperty.get();
        }

        public void setTitle(String title) {
            this.titleProperty.set(title);
        }

        public StringProperty titleProperty() {
            return titleProperty;
        }

        public String getAuthor() {
            return authorProperty.get();
        }

        public void setAuthor(String author) {
            this.authorProperty.set(author);
        }

        public StringProperty authorProperty() {
            return authorProperty;
        }

        public String getPubDate() {
            return pubDateProperty.get();
        }

        public void setPubDate(String date) {
            this.pubDateProperty.set(date);
        }

        public StringProperty pubDateProperty() {
            return pubDateProperty;
        }

        @Override
        public String toString() {
            return getPubDate() + "-" + getAuthor() + ": " + getTitle();
        }
    }
}
