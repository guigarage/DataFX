/**
 * Copyright (c) 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of DataFX, the website
 * javafxdata.org, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.samples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.datafx.provider.ObjectDataProvider;
import org.datafx.provider.ObjectDataProviderBuilder;
import org.datafx.reader.FileSource;
import org.datafx.reader.converter.JsonConverter;

/**
 *
 * Demo for retrieving JSON info containing a list of items inside a structure
 */
public class NestedListSample {

    public NestedListSample() {
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
            final ListView<String> lv = new ListView<>();

            URL resource = this.getClass().getResource("responsewithlist.json");
            JsonConverter<Response> converter = new JsonConverter<>(null, Response.class);
            FileSource<Response> dr = new FileSource<Response>(new File(resource.getFile()), converter);
            ObjectDataProviderBuilder odpb = ObjectDataProviderBuilder.create();
            ObjectProperty<Response> resultProperty = new SimpleObjectProperty<>();
            odpb.dataReader(dr).resultProperty(resultProperty);
            ObjectDataProvider provider = odpb.build();
            Worker worker = provider.retrieve();
            worker.stateProperty().addListener(new ChangeListener<Worker.State>() {

                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    if (newValue.equals(Worker.State.SUCCEEDED)) {
                        lv.setItems(resultProperty.get().getDetails());
                    }
                }
            }
            );
            tab.setContent(lv);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileXmlSingleSample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buildNetworkTab(Tab tab) {
    }
}
