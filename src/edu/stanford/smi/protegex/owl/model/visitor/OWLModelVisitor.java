package edu.stanford.smi.protegex.owl.model.visitor;

import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtomList;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividual;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * This visitor interface may be used when there is a need
 * to perform operations on the objects in the <code>OWLModel</code>.
 * The visitor pattern allows operations on the <code>OWLModel</code>
 * to be added without changing or polluting the interfaces and implementations of
 * the <code>OWLModel</code>.  A typical use is to define an implementation of the
 * visitor pattern, which is then used when iterating over the <code>RDFResources</code>
 * and other elements in the <code>OWLModel</code>.  Visitor is a recognised object oriented
 * design pattern - further details may be found on page 331 of
 * the Gamma et al "Design Patterns Elements of Reusable Object Oriented Software"
 * book.
 */
public interface OWLModelVisitor {

    void visitOWLAllDifferent(OWLAllDifferent owlAllDifferent);


    void visitOWLAllValuesFrom(OWLAllValuesFrom owlAllValuesFrom);


    void visitOWLCardinality(OWLCardinality owlCardinality);


    void visitOWLComplementClass(OWLComplementClass owlComplementClass);


    void visitOWLDataRange(OWLDataRange owlDataRange);


    void visitOWLDatatypeProperty(OWLDatatypeProperty owlDatatypeProperty);


    void visitOWLEnumeratedClass(OWLEnumeratedClass owlEnumeratedClass);


    void visitOWLHasValue(OWLHasValue owlHasValue);


    void visitOWLIndividual(OWLIndividual owlIndividual);


    void visitOWLIntersectionClass(OWLIntersectionClass owlIntersectionClass);


    void visitOWLMaxCardinality(OWLMaxCardinality owlMaxCardinality);


    void visitOWLMinCardinality(OWLMinCardinality owlMinCardinality);


    void visitOWLNamedClass(OWLNamedClass owlNamedClass);


    void visitOWLObjectProperty(OWLObjectProperty owlObjectProperty);


    void visitOWLOntology(OWLOntology owlOntology);


    void visitOWLSomeValuesFrom(OWLSomeValuesFrom someValuesFrom);


    void visitOWLUnionClass(OWLUnionClass owlUnionClass);


    void visitRDFDatatype(RDFSDatatype rdfsDatatype);


    void visitRDFIndividual(RDFIndividual rdfIndividual);


    void visitRDFList(RDFList rdfList);


    void visitRDFProperty(RDFProperty rdfProperty);


    void visitRDFSLiteral(RDFSLiteral rdfsLiteral);


    void visitRDFSNamedClass(RDFSNamedClass rdfsNamedClass);


    void visitRDFUntypedResource(RDFUntypedResource rdfUntypedResource);
    
    void visitSWRLIndividual(SWRLIndividual swrlIndividual);
    
    void visitSWRLAtomListIndividual(SWRLAtomList swrlAtomList);
}
