package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.ui.clsproperties.PropertyRestrictionsTreeWidget;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsWidget;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LogicClassDefinitionWidget extends AbstractClassDefinitionWidget {

    private ConditionsWidget conditionsWidget;

    private PropertyRestrictionsTreeWidget clsPropertiesWidget;

    protected void createNestedWidgets() {

        conditionsWidget = new ConditionsWidget();
        addNestedWidget(conditionsWidget, Model.Slot.DIRECT_SUPERCLASSES,
                "Conditions", "Conditions");

//        clsPropertiesWidget = new PropertyRestrictionsTreeWidget();
//        addNestedWidget(clsPropertiesWidget, Model.Slot.DIRECT_TEMPLATE_SLOTS,
//                "Properties", "Properties");

        super.createNestedWidgets();
    }

    protected void initAllPanel(JPanel allPanel, java.util.List widgets) {

//        JSplitPane bottomPanel = new MySplitPane(JSplitPane.HORIZONTAL_SPLIT,
//              disjointClassesWidget  , assertedSuperClassesWidget, 0.5);
        JSplitPane mainPanel = new MySplitPane(JSplitPane.VERTICAL_SPLIT,
                conditionsWidget, disjointClassesWidget, 0.75);
//                conditionsWidget, rightPanel, 0.65);

        allPanel.setLayout(new BorderLayout());
        allPanel.add(BorderLayout.CENTER, mainPanel);
    }
}
