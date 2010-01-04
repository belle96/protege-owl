package edu.stanford.smi.protegex.owl.ui.explorer.filter;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.explorer.ExplorerFilter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultExplorerFilter implements ExplorerFilter {

    private boolean useInferredSuperclasses;

    private Set validClasses = new HashSet();

    private RDFProperty validProperty = null;


    public DefaultExplorerFilter() {
        // Add all class types
        addAllValidClasses();
    }


    public void addAllValidClasses() {
        validClasses.add(RDFSNamedClass.class);
        validClasses.add(OWLSomeValuesFrom.class);
        validClasses.add(OWLAllValuesFrom.class);
        validClasses.add(OWLHasValue.class);
        validClasses.add(OWLMaxCardinality.class);
        validClasses.add(OWLMinCardinality.class);
        validClasses.add(OWLCardinality.class);
        validClasses.add(OWLIntersectionClass.class);
        validClasses.add(OWLUnionClass.class);
        validClasses.add(OWLComplementClass.class);
        validClasses.add(OWLEnumeratedClass.class);
    }


    public void addValidClass(Class cls) {
        validClasses.add(cls);
    }


    public Set getValidClasses() {
        return new HashSet(validClasses);
    }


    public RDFProperty getValidProperty() {
        return validProperty;
    }


    private boolean hasValidType(RDFSClass childClass) {
        Class childType = childClass.getClass();
        for (Iterator it = validClasses.iterator(); it.hasNext();) {
            Class c = (Class) it.next();
            if (c.isAssignableFrom(childType)) {
                return true;
            }
        }
        return false;
    }


    public boolean getUseInferredSuperclasses() {
        return useInferredSuperclasses;
    }


    public boolean isValidChild(RDFSClass parentClass, RDFSClass childClass) {
        if (parentClass instanceof OWLQuantifierRestriction) {
            return true;
        }
        if (!hasValidType(childClass)) {
            return false;
        }
        if (getValidProperty() != null) {
            if (childClass instanceof OWLRestriction) {
                RDFProperty onProperty = ((OWLRestriction) childClass).getOnProperty();
                return onProperty.equals(getValidProperty()) ||
                        onProperty.isSubpropertyOf(getValidProperty(), true);
            }
            else {
                return false;
            }
        }
        return true;
    }


    public void removeAllValidClasses() {
        validClasses.clear();
    }


    public void removeValidClass(Class c) {
        validClasses.remove(c);
    }


    public void setUseInferredSuperclasses(boolean value) {
        this.useInferredSuperclasses = value;
    }


    public void setValidProperty(RDFProperty property) {
        this.validProperty = property;
    }
}
