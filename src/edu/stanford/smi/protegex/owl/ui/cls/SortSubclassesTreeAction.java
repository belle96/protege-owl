package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SortSubclassesTreeAction extends ResourceAction {

    public SortSubclassesTreeAction() {
        super("Sort subtree below this", Icons.getBlankIcon(), SortSubclassesAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        sort((Cls) getResource(), new HashSet());
    }


    private void sort(Cls cls, Set doneSet) {
        if (!doneSet.contains(cls)) {
            doneSet.add(cls);
            OWLUtil.sortSubclasses(cls);
            for (Iterator it = cls.getDirectSubclasses().iterator(); it.hasNext();) {
                Cls subCls = (Cls) it.next();
                sort(subCls, doneSet);
            }
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof OWLSubclassPane;
    }
}
