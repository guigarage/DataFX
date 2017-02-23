package io.datafx.controller.util;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.Node;

/**
 * Created by hendrikebbers on 21.10.14.
 */
public class NullNode extends Node {
    @Override
    protected NGNode impl_createPeer() {
        throw new RuntimeException("This class should not be used!");
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        throw new RuntimeException("This class should not be used!");
    }

    @Override
    protected boolean impl_computeContains(double localX, double localY) {
        throw new RuntimeException("This class should not be used!");
    }

    @Override
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        throw new RuntimeException("This class should not be used!");
    }
}
