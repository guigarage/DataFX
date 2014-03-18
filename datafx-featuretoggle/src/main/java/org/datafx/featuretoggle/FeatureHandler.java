package org.datafx.featuretoggle;

import com.guigarage.toggles.ObservableToggleManager;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import org.datafx.DataFXConfiguration;
import org.togglz.core.Feature;
import org.w3c.dom.Element;

import java.util.List;

public class FeatureHandler {

    private static FeatureHandler instance;

    private ObservableToggleManager manager;

    private FeatureHandler() {}

    public static synchronized FeatureHandler getInstance() {
        if (instance == null) {
            instance = new FeatureHandler();
        }
        return instance;
    }

    private ObservableToggleManager createManagerFromConfig() {
        try {
        List<Element> elements = DataFXConfiguration.getInstance().getElements("features");
        if (elements == null) {
            throw new RuntimeException("Can't load config. Features section not specified!");
        }
        if (elements.isEmpty()) {
            throw new RuntimeException("Can't load config. Features section not specified!");
        }
        if (elements.size() > 1) {
            throw new RuntimeException("Can't load config. Features section must only be specified once!");
        }
        Class<? extends Feature> featureClass = (Class<? extends Feature>) Class.forName(elements.get(0).getElementsByTagName("featureEnum").item(0).getTextContent());
        return new ObservableToggleManager(featureClass);
        } catch (Exception e) {
            throw new RuntimeException("Can't generate Feature Manager", e);
        }
    }

    protected synchronized ObservableToggleManager getManager() {
        if (manager == null) {
            manager = createManagerFromConfig();
        }
        return manager;
    }

    public FeatureProperty<?> createFeatureProperty(String featureName) {
        return new FeatureProperty<>(getManager().getFeatureToggle(featureName));
    }

    public void hideByFeature(Node node, String featureName) {
        bindToBooleanProperty(featureName, node.visibleProperty());
    }

    public void disableByFeature(Node node, String featureName) {
        bindToBooleanProperty(featureName, node.disableProperty());
    }

    public void bindToBooleanProperty(String featureName, BooleanProperty property) {
        bindToBooleanProperty(createFeatureProperty(featureName), property);
    }

    public void bindToBooleanProperty(FeatureProperty<?> feature, BooleanProperty property) {
        property.bind(feature);
        property.setValue(feature.get());
    }
}
