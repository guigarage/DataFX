package io.datafx.controller.util;

import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

/**
 * A helper class that provides methods to register actions on nodes
 * Created by hendrikebbers on 03.11.14.
 */
public class ActionUtil {

    /**
     * Adds a event handler to the given node. If the node extends ButtonBase or Textfield a action listener will be
     * used. Otherwise the action will be triggered on doubleclick
     *
     * @param node The node that should trigger the action
     * @param action the action
     */
    public static void defineNodeAction(Node node, Runnable action) {
        if (node instanceof ButtonBase) {
            ((ButtonBase) node).setOnAction((e) -> action.run());
        } else if (node instanceof TextField) {
            ((TextField) node).setOnAction((e) -> action.run());
        } else {
            node.setOnMouseClicked((e) -> {
                if (e.getClickCount() > 1) {
                    action.run();
                }
            });
        }
    }

    /**
     * Adds a event handler to the given menu.
     * @param menuItem The menu item that should trigger the action
     * @param action the action
     */
    public static void defineItemAction(MenuItem menuItem, Runnable action) {
        menuItem.setOnAction((e) -> action.run());
    }
}
