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
package org.datafx.samples.masterdetail;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.context.ViewFlowContext;
import org.datafx.controller.flow.DefaultFlowContainer;
import org.datafx.controller.flow.Flow;
import org.datafx.samples.masterdetail.action.DeleteAction;
import org.datafx.samples.masterdetail.controller.DetailViewController;
import org.datafx.samples.masterdetail.controller.MasterViewController;
import org.datafx.samples.masterdetail.data.DataModel;

public class MasterDetailApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		StackPane pane = new StackPane();
		
		DefaultFlowContainer flowContainer = new DefaultFlowContainer(pane);

		Flow flow = new Flow(ViewController1.class)
				.withGlobalLink("view1ActionId",
                        ViewController1.class)
				.withGlobalLink("view2ActionId",
                        ViewController1.class);

        FlowHandler handler = flow.createHandler();
        handler.start(flowContainer);

        Button b1 = new Button("View 1");
        b1.setOnAction((e) -> handler.handle(("view1ActionId"));

        Button b2 = new Button("View 1");
        b2.setOnAction((e) -> handler.handle(("view2ActionId"));

        VBox box = new VBox();
        box.getChildren().addAll()

		Scene myScene = new Scene(pane);

		stage.setScene(myScene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
