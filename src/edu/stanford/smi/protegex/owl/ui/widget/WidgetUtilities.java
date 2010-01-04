package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.ui.InstanceDisplay;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLNames;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides generic methods used by several
 * OWL widgets to fullfill its tasks.
 *
 * @author Daniel Stoeckli <stoeckli@smi.stanford.edu>
 */
public class WidgetUtilities {

    public static void addViewButton(LabeledComponent lc, Action viewAction) {
        if (ApplicationProperties.getBooleanProperty("showViewButtons", false)) {
            lc.addHeaderButton(viewAction);
        }
    }


    public static Cls getDefaultRestrictionMetaCls(KnowledgeBase kb) {
        return kb.getCls(OWLNames.Cls.SOME_VALUES_FROM_RESTRICTION);
    }


    public static JToolBar getJToolBar(InstanceDisplay instanceDisplay) {
        Container child = (Container) instanceDisplay.getComponent(0);
        return getJToolBar((Container) child.getComponent(0));
    }


    public static JToolBar getJToolBar(Container container) {
        return searchFakeToolBarRecursive(container);
    }


    private static JToolBar searchFakeToolBarRecursive(Container c) {

        if (c instanceof JToolBar) {
            return (JToolBar) c;
        }

        if (c.getComponents().length > 0) {
            Component[] comps = c.getComponents();
            for (int i = 0; i < comps.length; i++) {
                JToolBar f = searchFakeToolBarRecursive((Container) comps[i]);
                if (f != null) {
                    return f;
                }
            }
        }
        return null;
    }
}
