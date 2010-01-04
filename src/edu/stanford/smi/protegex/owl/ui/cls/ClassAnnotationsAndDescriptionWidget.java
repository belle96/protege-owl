package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.ui.widget.HeaderWidget;
import edu.stanford.smi.protegex.owl.ui.widget.MultiWidgetPropertyWidget;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 20, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ClassAnnotationsAndDescriptionWidget extends MultiWidgetPropertyWidget {

    private SwitchableClassDefinitionWidget switchableClassDefinitionWidget;

    private HeaderWidget headerWidget;

    protected void createNestedWidgets() {

        headerWidget = new HeaderWidget();
        addNestedWidget(headerWidget, OWLNames.Slot.EQUIVALENT_CLASS, "Class Annotations",
                "Class Annotations");

        switchableClassDefinitionWidget = new SwitchableClassDefinitionWidget();
         addNestedWidget(switchableClassDefinitionWidget, OWLNames.Slot.EQUIVALENT_CLASS, "Class Description",
                "Class Description");

    }


    public void initialize() {
        super.initialize();
        setAllMode(true);
    }

    protected void initAllPanel(JPanel allPanel, java.util.List widgets) {


        JSplitPane mainPanel = new MySplitPane(JSplitPane.VERTICAL_SPLIT,
                headerWidget, switchableClassDefinitionWidget, 0.3);

        allPanel.setLayout(new BorderLayout());
        allPanel.add(mainPanel);

    }

    protected class MySplitPane extends JSplitPane {

        MySplitPane(int orientation, Component leftComponent, Component rightComponent, double resizeWeight) {
            super(orientation, leftComponent, rightComponent);
            setDividerSize(2);
            setBorder(null);
            setResizeWeight(resizeWeight);
        }
    }

    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        if (cls.getKnowledgeBase() instanceof OWLModel  &&
            cls instanceof OWLNamedClass) {
            return true;
        }
        return false;
    }
}
