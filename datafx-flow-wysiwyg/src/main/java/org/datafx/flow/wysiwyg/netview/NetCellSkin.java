package org.datafx.flow.wysiwyg.netview;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;

public class NetCellSkin<T> extends LabeledSkinBase<NetCell<T>, BehaviorBase<NetCell<T>>> {

    protected NetCellSkin(NetCell<T> control) {
        super(control, new BehaviorBase(control, null));
    }
}
