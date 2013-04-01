/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.datafx.provider.ListObjectDataProvider;
import org.datafx.reader.RestSource;
import org.datafx.reader.util.XmlConverter;

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
            ListObjectDataProvider<Tweet> sodp = new ListObjectDataProvider(dr);
            sodp.retrieve();

            final ListProperty<Tweet> op = sodp.getData();
            ListView lv = new ListView(op.get());
            tab.setContent(lv);
        } catch (Exception ex) {
            Logger.getLogger(RestSample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class Tweet {

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
