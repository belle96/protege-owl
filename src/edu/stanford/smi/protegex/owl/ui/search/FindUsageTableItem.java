package edu.stanford.smi.protegex.owl.ui.search;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * An entry in the FindUsageTableModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindUsageTableItem {

    public final static int SUPERCLASS = 0;

    public final static int EQUIVALENT_CLASS = 1;

    public final static int DISJOINT_CLASS = 2;

    public final static int RANGE = 3;

    public final static int VALUE = 4;

    public RDFResource host;

    public RDFResource usage;

    public int type;


    public FindUsageTableItem(int type, RDFResource host, RDFResource usage) {
        this.type = type;
        this.host = host;
        this.usage = usage;
    }


    public boolean contains(Frame frame) {
        return host.equals(frame) || usage.equals(frame);
    }


    public Icon getIcon() {
        switch (type) {
            case SUPERCLASS:
                return OWLIcons.getImageIcon(OWLIcons.RDFS_SUBCLASS_OF);
            case EQUIVALENT_CLASS:
                return OWLIcons.getImageIcon(OWLIcons.OWL_EQUIVALENT_CLASS);
            case DISJOINT_CLASS:
                return OWLIcons.getImageIcon(OWLIcons.OWL_DISJOINT_CLASSES);
            case RANGE:
                return OWLIcons.getImageIcon(OWLIcons.RDFS_RANGE);
            case VALUE:
                return OWLIcons.getImageIcon(OWLIcons.OWL_OBJECT_PROPERTY);
        }
        return null;
    }
}
