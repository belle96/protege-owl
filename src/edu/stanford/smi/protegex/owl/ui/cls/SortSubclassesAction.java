package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SortSubclassesAction extends ResourceAction {

    public final static String GROUP = "Sort/";


    public SortSubclassesAction() {
        super("Sort subclasses", Icons.getBlankIcon(), GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        OWLUtil.sortSubclasses((Cls) getResource());
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof OWLSubclassPane;
    }
}
