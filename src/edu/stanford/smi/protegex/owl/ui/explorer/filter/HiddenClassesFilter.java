package edu.stanford.smi.protegex.owl.ui.explorer.filter;

import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.explorer.ExplorerFilter;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         24-Feb-2006
 */
public class HiddenClassesFilter implements ExplorerFilter {

    public boolean getUseInferredSuperclasses() {
        return false;
    }

    public boolean isValidChild(RDFSClass parentClass, RDFSClass childClass) {
        return !childClass.isVisible();
    }
}
