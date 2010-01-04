package edu.stanford.smi.protegex.owl.model.classparser;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * An interface for objects that can be used to parse a class expression.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLClassParser {

    /**
     * Performs a test of a given expression and throws an Exception if the expression
     * is currently invalid.
     *
     * @param owlModel   the OWLModel to operate on
     * @param expression the expression
     * @throws OWLClassParseException if expression cannot be parsed
     */
    void checkClass(OWLModel owlModel, String expression) throws OWLClassParseException;


    void checkHasValueFiller(OWLModel owlModel, String expression) throws OWLClassParseException;


    void checkQuantifierFiller(OWLModel owlModel, String expression) throws OWLClassParseException;


    RDFSClass parseClass(OWLModel owlModel, String expression) throws OWLClassParseException;


    Object parseHasValueFiller(OWLModel owlModel, String expression) throws OWLClassParseException;


    RDFResource parseQuantifierFiller(OWLModel owlModel, String expression) throws OWLClassParseException;
}
