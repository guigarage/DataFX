package org.datafx.flow.wysiwyg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.datafx.flow.wysiwyg.data.FlowDefinition;
import org.datafx.flow.wysiwyg.data.FlowViewDefinition;
import org.datafx.flow.wysiwyg.netview.NetConnection;

public class Demo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FlowDefinition flowDefinition = new FlowDefinition();
        flowDefinition.getViews().add(new FlowViewDefinition("com.A"));
        flowDefinition.getViews().add(new FlowViewDefinition("com.B"));
        flowDefinition.getViews().add(new FlowViewDefinition("com.C"));

        FlowViewCanvas flowViewCanvas = new FlowViewCanvas();

        flowViewCanvas.setFlowDefinition(flowDefinition);
        NetConnection<FlowViewDefinition> con = new NetConnection<>();
        con.setStartItem(flowDefinition.getViews().get(0));
        con.setEndItem(flowDefinition.getViews().get(1));

        NetConnection<FlowViewDefinition> con2 = new NetConnection<>();
        con2.setStartItem(flowDefinition.getViews().get(1));
        con2.setEndItem(flowDefinition.getViews().get(2));

        NetConnection<FlowViewDefinition> con3 = new NetConnection<>();
        con3.setStartItem(flowDefinition.getViews().get(0));
        con3.setEndItem(flowDefinition.getViews().get(2));

        flowViewCanvas.getConnections().addAll(con, con2, con3);


        Scene myScene = new Scene(new StackPane(flowViewCanvas));
        primaryStage.setScene(myScene);
        primaryStage.show();

    }
}
