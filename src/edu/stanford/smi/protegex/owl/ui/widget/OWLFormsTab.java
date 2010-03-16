package edu.stanford.smi.protegex.owl.ui.widget;

import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.LinkedList;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.FormsPanel;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.SelectableTree;
import edu.stanford.smi.protege.widget.FormsTab;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;
import edu.stanford.smi.protegex.owl.ui.search.finder.DefaultClassFind;
import edu.stanford.smi.protegex.owl.ui.search.finder.Find;
import edu.stanford.smi.protegex.owl.ui.search.finder.FindAction;
import edu.stanford.smi.protegex.owl.ui.search.finder.FindInDialogAction;
import edu.stanford.smi.protegex.owl.ui.search.finder.ResourceFinder;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         25-Oct-2005
 */
public class OWLFormsTab extends FormsTab implements HostResourceDisplay {

    private SelectableTree theTree;

    @Override
	public void initialize() {
        super.initialize();
        FormsPanel panel = (FormsPanel) getSubComponent(this, FormsPanel.class);
        LabeledComponent lc = (LabeledComponent) getSubComponent(panel, LabeledComponent.class);
        theTree = (SelectableTree) panel.getSelectable();

        FindAction fAction = new FindInDialogAction(new DefaultClassFind((OWLModel) getKnowledgeBase(),
                                                                         Find.CONTAINS),
                                                                         Icons.getFindFormIcon(),
                                                                         this, true);

        ResourceFinder finder = new ResourceFinder(fAction);

        lc.setFooterComponent(finder);
    }

    private Object getSubComponent(Component start, Class searchClass) {
        Object component = null;
        java.util.List components = new LinkedList();
        components.add(start);

        while (component == null && components.size() > 0) {
            Component current = (Component) components.remove(0);
            if (searchClass.isAssignableFrom(current.getClass())) {
                component = current;
            }
            else if (current instanceof Container) {
                components.addAll(Arrays.asList(((Container) current).getComponents()));
            }
        }
        return component;
    }

    public boolean displayHostResource(RDFResource resource) {
       return OWLUI.setSelectedNodeInTree(theTree, resource);
    }

}
