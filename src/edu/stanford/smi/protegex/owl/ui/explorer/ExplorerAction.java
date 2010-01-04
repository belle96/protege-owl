package edu.stanford.smi.protegex.owl.ui.explorer;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.cls.HierarchyManager;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.explorer.filter.DefaultExplorerFilter;
import edu.stanford.smi.protegex.owl.ui.explorer.filter.ExplorerFilterPanel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.search.SearchNamedClassAction;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExplorerAction extends ResourceAction {


    public ExplorerAction() {
        super("Explore superclass relationships...",
                OWLIcons.getImageIcon(OWLIcons.SHOW_EXPLORER),
                SearchNamedClassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        RDFSNamedClass cls = (RDFSNamedClass) getResource();
        DefaultExplorerFilter filter = new DefaultExplorerFilter();
        if (ExplorerFilterPanel.show(getResource().getOWLModel(), filter)) {
            ExplorerTreePanel tp = new ExplorerTreePanel(cls, filter, "Asserted Recursive Hierachy", true);
            OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(getComponent());
            HierarchyManager hierarchyManager = tab.getHierarchyManager();
            hierarchyManager.addHierarchy(tp);
        }
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof RDFSClass &&
                OWLClassesTab.getOWLClassesTab(component) != null;
    }
}
