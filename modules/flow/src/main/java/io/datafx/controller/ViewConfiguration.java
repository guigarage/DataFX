/**
 * Copyright (c) 2011, 2014, Jonathan Giles, Johan Vos, Hendrik Ebbers
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
 * DISCLAIMED. IN NO EVENT SHALL DATAFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package io.datafx.controller;

import java.nio.charset.Charset;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.BuilderFactory;

/**
 * This class defines the configuration for one view. In DataFX each view can
 * have its own configuration. If working with the Flow API a flow can define a
 * global configuration for all views in it.
 */
public class ViewConfiguration {

    private ResourceBundle resources;

    BuilderFactory builderFactory;

    Charset charset;

    /**
     * Default constructor
     */
    public ViewConfiguration() {
        builderFactory = new JavaFXBuilderFactory();
        charset = Charset.forName(FXMLLoader.DEFAULT_CHARSET_NAME);
    }

    /**
     * Returns the Builderfactory that is used for the UI generation by the FXMLLoader
     * @return the Builderfactory that is used for the UI generation by the FXMLLoader
     */
    public BuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    /**
     * setter for the Builderfactory that is used for the UI generation by the FXMLLoader
     * @param builderFactory the Builderfactory that is used for the UI generation by the FXMLLoader
     */
    public void setBuilderFactory(BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    /**
     * Getter for the Charset that is used by the FXMLLoader when loading an UI
     * by a FXML file
     *
     * @return the Charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Setter for the Charset that is used by the FXMLLoader when loading an UI by a FXML file
     * @param charset 
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Getter for the resource bundle that is used by the FXMLLoader when loading an UI by a FXML file
     * @return 
     */
    public ResourceBundle getResources() {
        return resources;
    }

    /**
     * Setter for the resource bundle that is used by the FXMLLoader when loading an UI by a FXML file
     * @param resources 
     */
    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }
}
