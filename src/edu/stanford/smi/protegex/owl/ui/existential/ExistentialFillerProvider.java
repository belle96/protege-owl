package edu.stanford.smi.protegex.owl.ui.existential;

import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitorAdapter;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.Set;
import java.util.HashSet;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Feb 5, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ExistentialFillerProvider extends OWLModelVisitorAdapter {

    private Set fillers;

    private Set visitedResources;

    //private OWLObjectProperty property;

    private Set properties;

    public ExistentialFillerProvider(OWLObjectProperty property) {
        fillers = new HashSet();
        visitedResources = new HashSet();
        properties = new HashSet();
        properties.add(property);
        properties.addAll(property.getSubproperties(true));
    }

    public void reset() {
        fillers.clear();
        visitedResources.clear();
    }

    public Set getFillers() {
        return fillers;
    }

    public void visitOWLNamedClass(OWLNamedClass owlNamedClass) {
        if(visitedResources.contains(owlNamedClass)) {
            return;
        }
        visitedResources.add(owlNamedClass);
        for (Object o : owlNamedClass.getSuperclasses(false)) {
            if (o instanceof OWLClass) {
                ((OWLClass) o).accept(this);
            }
        }
        for (Object o : owlNamedClass.getEquivalentClasses()) {
            if (o instanceof OWLClass) {
                ((OWLClass) o).accept(this);
            }
        }
    }

    public void visitOWLIntersectionClass(OWLIntersectionClass owlIntersectionClass) {
        // Intersection classes are a special case, because of the semantics
        // of intersection, we can simply process each operand of the intersection
        // rather than the intersection itself.
        for (Object o : owlIntersectionClass.getOperands()) {
            if (o instanceof OWLClass) {
                ((OWLClass) o).accept(this);
            }
        }
    }


    public void visitOWLSomeValuesFrom(OWLSomeValuesFrom someValuesFrom) {
        if (properties.contains(someValuesFrom.getOnProperty())) {
            processFiller((OWLObjectProperty) someValuesFrom.getOnProperty(), (OWLClass) someValuesFrom.getFiller());
        }
    }

    public void visitOWLMinCardinality(OWLMinCardinality owlMinCardinality) {
        processCardinality(owlMinCardinality);
    }

    public void visitOWLCardinality(OWLCardinality owlCardinality) {
        processCardinality(owlCardinality);
    }

    /**
     * Processes different types of cardinality restrictions (really min and
     * exact) where the cardinality is greater than zero.
     * @param cardinalityBase - should be a min or max cardinality restriction
     */
    private void processCardinality(OWLCardinalityBase cardinalityBase) {
        if (cardinalityBase.getCardinality() > 0 && properties.contains(cardinalityBase.getOnProperty())) {
            if (cardinalityBase.getValuesFrom() != null) {
                if (cardinalityBase.getValuesFrom() instanceof OWLClass) {
                    processFiller((OWLObjectProperty) cardinalityBase.getOnProperty(),  (OWLClass) cardinalityBase.getValuesFrom());
                }
            }
        }
    }

    private void processFiller(OWLObjectProperty property, OWLClass cls) {
        if (cls instanceof OWLIntersectionClass) {
            OWLIntersectionClass fillerCls = (OWLIntersectionClass) cls;
            for (Object o : fillerCls.getOperands()) {
                if (o instanceof OWLClass) {
                    OWLClass curOp = (OWLClass) o;
                    fillers.add(curOp);
                    if (property.isTransitive()) {
                        curOp.accept(this);
                    }
                }
            }
        } else {
            fillers.add(cls);
            if (property.isTransitive()) {
                cls.accept(this);
            }
        }
    }
}
