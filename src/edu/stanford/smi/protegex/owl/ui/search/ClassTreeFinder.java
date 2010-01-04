package edu.stanford.smi.protegex.owl.ui.search;

import edu.stanford.smi.protege.ui.ClsTreeFinder;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import javax.swing.*;

/**
 * This is currently a wrapper of the core Protege ClsTreeFinder, so that
 * we have a greater flexibility with respect to handling search results etc.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassTreeFinder extends ClsTreeFinder {

    public ClassTreeFinder(OWLModel owlModel, JTree tree) {
        super(owlModel, tree);
    }


    public ClassTreeFinder(OWLModel owlModel, JTree tree, String description) {
        super(owlModel, tree, description);
    }
}
