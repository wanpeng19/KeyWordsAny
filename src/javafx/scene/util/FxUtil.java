/*
 * Copyright (c) 2013, FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */
package javafx.scene.util;

import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;

public class FxUtil {

    @SuppressWarnings("rawtypes")
    public static void clearForm(Node areaNode) {
        String[] nodesType = { ".text-field", ".text-area", ".choice-box", ".combo-box" };
        for (String type : nodesType) {
            Set<Node> nodes = areaNode.lookupAll(type);
            if (nodes != null && !nodes.isEmpty()) {
                for (Node node : nodes) {
                    if (node instanceof TextInputControl) {
                        ((TextInputControl) node).setText("");
                    } else if (node instanceof ChoiceBox) {
                        ((ChoiceBox) node).getSelectionModel().selectFirst();
                    } else if (node instanceof ComboBox) {
                        ((ComboBox) node).getSelectionModel().selectFirst();
                    }
                }
            }
        }
    }
}
