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
package io.datafx.control.cell;

import io.datafx.core.concurrent.DataFxService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * A cell that visualizes the current state of a
 * <code>Service</code>
 *
 * @author hendrikebbers
 *
 */
public class ServiceListCell<T> extends ListCell<Service<T>> {
    @FXML private Label taskTitleLabel;
    @FXML private Label taskMessageLabel;
    @FXML private Button killTaskButton;
    @FXML private ProgressBar taskProgress;
    @FXML private AnchorPane anchorPane;

    public ServiceListCell() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "ServiceListCell.fxml"));
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        setGraphic(anchorPane);

        anchorPane.setVisible(false);

        killTaskButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                Service<?> service = getItem();
                if (service != null) {
                    service.cancel();
                }
            }
        });

        itemProperty().addListener(new ChangeListener<Service<?>>() {
            @Override public void changed(
                    ObservableValue<? extends Service<?>> observableValue,
                    Service<?> oldValue, Service<?> newValue) {
                try {
                    if (oldValue != null) {
                        taskTitleLabel.textProperty().unbind();
                        taskMessageLabel.textProperty().unbind();
                        taskProgress.progressProperty().unbind();
                        killTaskButton.visibleProperty().unbind();
                    }
                    if (newValue != null) {
                        taskTitleLabel.textProperty().bind(newValue.titleProperty());
                        taskMessageLabel.textProperty().bind(newValue.messageProperty());
                        taskProgress.progressProperty().bind(newValue.progressProperty());

                        if (newValue instanceof DataFxService<?>) {
                            killTaskButton.visibleProperty().bind(((DataFxService<?>) newValue).cancelableProperty());
                        } else {
                            killTaskButton.visibleProperty().set(true);
                        }
                        anchorPane.setVisible(true);
                    } else {
                        anchorPane.setVisible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
