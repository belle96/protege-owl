package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.ui.clsdesc.DisjointClassesWidget;
import edu.stanford.smi.protegex.owl.ui.widget.MultiWidgetPropertyWidget;

import javax.swing.*;
import java.awt.*;

/**
 * The base class of the two ClsDefinitionWidgets.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractClassDefinitionWidget extends MultiWidgetPropertyWidget {

    protected DisjointClassesWidget disjointClassesWidget;


    protected void createNestedWidgets() {
        disjointClassesWidget = new DisjointClassesWidget();
        addNestedWidget(disjointClassesWidget, OWLNames.Slot.DISJOINT_WITH,
                "Disjoints", "Disjoints");
    }


    public void initialize() {
        super.initialize();
        setAllMode(true);
    }


    protected class MySplitPane extends JSplitPane {

        public MySplitPane(int orientation, Component leftComponent, Component rightComponent, double resizeWeight) {
            super(orientation, leftComponent, rightComponent);
            setDividerSize(2);
            setBorder(null);
            setResizeWeight(resizeWeight);
        }
    }
    
    @Override
    public void dispose() {    
    	super.dispose();
    	disjointClassesWidget = null;
    }
}
