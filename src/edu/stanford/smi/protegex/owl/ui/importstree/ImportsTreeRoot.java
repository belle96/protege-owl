package edu.stanford.smi.protegex.owl.ui.importstree;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Comparator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportsTreeRoot extends LazyTreeRoot {

    public ImportsTreeRoot(OWLOntology rootOntology) {
        super(rootOntology);
    }


    protected LazyTreeNode createNode(Object o) {
        return new ImportsTreeNode(this, (RDFResource) o);
    }


    protected Comparator getComparator() {
        return null;
    }
}
