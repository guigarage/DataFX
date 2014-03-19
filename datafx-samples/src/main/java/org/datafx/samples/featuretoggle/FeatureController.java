package org.datafx.samples.featuretoggle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;
import org.datafx.featuretoggle.DisabledByFeature;

@FXMLController("featureView.fxml")
public class FeatureController {

    @FXML
    @DisabledByFeature("FEATURE1")
    private Button button1;

    @FXML
    @DisabledByFeature("FEATURE2")
    private Button button2;

}
