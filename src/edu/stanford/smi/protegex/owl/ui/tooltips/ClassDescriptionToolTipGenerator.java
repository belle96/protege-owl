package edu.stanford.smi.protegex.owl.ui.tooltips;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.prose.ProseGen;
import edu.stanford.smi.protegex.owl.ui.widget.OWLToolTipGenerator;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         28-Mar-2006
 */
public class ClassDescriptionToolTipGenerator implements OWLToolTipGenerator {

    public String getToolTipText(RDFSClass aClass) {
        return ProseGen.getProseAsString(aClass);
    }

    public String getToolTipText(RDFProperty prop) {
        return null;
    }

    public String getToolTipText(RDFResource res) {
        if (res instanceof RDFSClass) {
            return ProseGen.getProseAsString((RDFSClass) res);
        }
        return null;
    }
}
