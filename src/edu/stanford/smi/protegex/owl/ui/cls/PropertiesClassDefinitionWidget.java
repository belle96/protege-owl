package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.ui.clsdesc.PropertiesSuperclassesWidget;
import edu.stanford.smi.protegex.owl.ui.clsproperties.PropertyRestrictionsTreeWidget;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertiesClassDefinitionWidget extends AbstractClassDefinitionWidget {

    private PropertyRestrictionsTreeWidget propertyRestrictionsTreeWidget;

    private PropertiesSuperclassesWidget superclassesWidget;


    protected void createNestedWidgets() {

        superclassesWidget = new PropertiesSuperclassesWidget();
        addNestedWidget(superclassesWidget, Model.Slot.DIRECT_SUPERCLASSES,
                "Superclasses", "Superclasses");

        propertyRestrictionsTreeWidget = new PropertyRestrictionsTreeWidget();
        addNestedWidget(propertyRestrictionsTreeWidget, Model.Slot.DIRECT_TEMPLATE_SLOTS,
                "Properties and Restrictions", "Properties");

        super.createNestedWidgets();
    }


    public void initialize() {
        super.initialize();
        propertyRestrictionsTreeWidget.setDisplayRestrictions(true);
    }


    protected void initAllPanel(JPanel allPanel, java.util.List widgets) {

        JSplitPane combiPanel = new MySplitPane(JSplitPane.HORIZONTAL_SPLIT,
                superclassesWidget, disjointClassesWidget, 0.5);
        combiPanel.setDividerLocation(340);
        JSplitPane mainPanel = new MySplitPane(JSplitPane.VERTICAL_SPLIT,
                propertyRestrictionsTreeWidget, combiPanel, 0.7);

        allPanel.setLayout(new BorderLayout());
        allPanel.add(BorderLayout.CENTER, mainPanel);
    }
}
