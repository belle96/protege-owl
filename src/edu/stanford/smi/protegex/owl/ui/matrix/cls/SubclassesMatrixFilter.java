package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.matrix.DependentMatrixFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SubclassesMatrixFilter implements DependentMatrixFilter {

    private RDFSNamedClass parentClass;


    public SubclassesMatrixFilter(RDFSNamedClass parentClass) {
        this.parentClass = parentClass;
    }


    public Collection getInitialValues() {
        Collection results = new ArrayList();
        Iterator it = parentClass.getSubclasses(true).iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof RDFSNamedClass) {
                results.add(next);
            }
        }
        results.remove(parentClass);
        return results;
    }


    public String getName() {
        return "Subclasses of " + parentClass.getBrowserText();
    }


    public boolean isDependentOn(RDFResource instance) {
        return parentClass.equals(instance);
    }


    public boolean isSuitable(RDFResource instance) {
        return instance instanceof RDFSNamedClass &&
                instance.isVisible() &&
                (instance.isEditable() || instance.isIncluded()) &&
                ((RDFSNamedClass) instance).isSubclassOf(parentClass);
    }
}
