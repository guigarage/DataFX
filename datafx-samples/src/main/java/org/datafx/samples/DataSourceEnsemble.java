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
       
        createTab("File-XML-Single", tabPane, new FileXmlSingleSample().getContent(scene));
        createTab("File-XML-List", tabPane, new FileXmlListSample().getContent(scene));
        createTab("jdbc-list", tabPane, new JdbcSample().getContent(scene));
        createTab("Network-XML-List", tabPane, new RestSample().getContent(scene));

   
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
