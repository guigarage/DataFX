package io.datafx.featuretoggle;

import com.guigarage.toggles.ObservableToggleManager;
import io.datafx.core.DataFXConfiguration;
import javafx.scene.Node;
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
        node.visibleProperty().bind(createFeatureProperty(featureName).not());
    }

    public void disableByFeature(Node node, String featureName) {
        node.disableProperty().bind(createFeatureProperty(featureName).not());
    }

}
