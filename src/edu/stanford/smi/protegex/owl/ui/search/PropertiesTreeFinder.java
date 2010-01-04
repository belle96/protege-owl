package edu.stanford.smi.protegex.owl.ui.search;

import edu.stanford.smi.protege.ui.SlotsTreeFinder;
import edu.stanford.smi.protegex.owl.model.OWLModel;

import javax.swing.*;

/**
 * This is currently a wrapper of the core Protege SlotsTreeFinder, so that
 * we have a greater flexibility with respect to handling search results etc.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertiesTreeFinder extends SlotsTreeFinder {

    public PropertiesTreeFinder(OWLModel owlModel, JTree tree) {
        super(owlModel, tree);
    }


    public PropertiesTreeFinder(OWLModel owlModel, JTree tree, String description) {
        super(owlModel, tree, description);
    }
}
