package org.datafx.samples.featuretoggle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.datafx.controller.FXMLController;
import org.datafx.featuretoggle.DisabledByFeature;
import org.datafx.featuretoggle.HideByFeature;

@FXMLController("featureView.fxml")
public class FeatureController {

    @FXML
    @HideByFeature("PLAY_FEATURE")
    private Button playButton;

    @FXML
    @DisabledByFeature("FEATURE2")
    private Button button2;

}
