package io.datafx.samples.featuretoggle;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;

public enum SampleFeatures implements Feature {
    @EnabledByDefault
    FEATURE1,

    FEATURE2;
}
