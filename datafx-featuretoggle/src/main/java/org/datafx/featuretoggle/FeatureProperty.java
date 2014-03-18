package org.datafx.featuretoggle;

import com.guigarage.toggles.ObservableFeatureToggle;
import javafx.beans.property.BooleanPropertyBase;

public class FeatureProperty<T extends ObservableFeatureToggle> extends BooleanPropertyBase {

    private T feature;

    public FeatureProperty(T feature) {
        this.feature = feature;
        feature.addListener((f, a) -> set(a));
        set(feature.isActive());
    }

    @Override
    public Object getBean() {
        return feature;
    }

    @Override
    public String getName() {
        return "active";
    }
}
