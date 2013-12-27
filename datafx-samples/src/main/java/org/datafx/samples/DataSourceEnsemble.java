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

import java.io.IOException;
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 *
 * @author johan
 */
public class DataSourceEnsemble extends Application {
      
    public static void main(String... args) {
        launch(DataSourceEnsemble.class, args);
    }
    
    @Override
    public void start(Stage stage) throws MalformedURLException, IOException {
        stage.setTitle("DataSource Samples");
        final Scene scene = new Scene(new Group(), 875, 700);
        scene.setFill(Color.LIGHTGRAY);
        Group root = (Group)scene.getRoot();
        
        // TabPane
        final TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("top-tab");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefWidth(scene.getWidth());
        tabPane.setPrefHeight(scene.getHeight());

        tabPane.prefWidthProperty().bind(scene.widthProperty());
        tabPane.prefHeightProperty().bind(scene.heightProperty());
        root.getChildren().add(tabPane);
       
//        createTab("File-XML-Single", tabPane, new FileXmlSingleSample().getContent(scene));
//        createTab("File-XML-List", tabPane, new FileXmlListSample().getContent(scene));
        createTab("jdbc-list", tabPane, new JdbcSample().getContent(scene));
 //       createTab("Network-XML-List", tabPane, new RestSample().getContent(scene));

   
        stage.setScene(scene);
        stage.show();
    }
    
    private void createTab(String text, TabPane tabPane, Node content) {
        Tab checkBoxTab = new Tab(text);
        content.getStyleClass().add("bottom-tab");
        checkBoxTab.setContent(content);
        tabPane.getTabs().add(checkBoxTab);
    }
}
