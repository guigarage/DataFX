package org.datafx.controller.util;

import java.nio.charset.Charset;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.BuilderFactory;

public class ViewConfiguration {

    private ResourceBundle resources;

    BuilderFactory builderFactory;

    Charset charset;

    public ViewConfiguration() {
        builderFactory = new JavaFXBuilderFactory();
        charset = Charset.forName(FXMLLoader.DEFAULT_CHARSET_NAME);
    }

    public BuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    public void setBuilderFactory(BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources = resources;
    }
}
