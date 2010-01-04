package edu.stanford.smi.protegex.owl.ui;

import java.util.Comparator;

import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * A Comparator that compares two RDFResources by their name.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ResourceComparator implements Comparator<RDFResource> {

    public int compare(RDFResource o1, RDFResource o2) {
        return o1.compareTo(o2);
    }

}
