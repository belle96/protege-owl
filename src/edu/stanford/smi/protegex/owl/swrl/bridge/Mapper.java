
package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.*;

import java.util.*;

public interface Mapper
{
  boolean isMapped(OWLClass owlClass);
  boolean isMapped(OWLProperty owlProperty);

  void open() throws MapperException;
  void close() throws MapperException;

  Set<OWLIndividual> mapOWLClass(OWLClass owlClass) throws MapperException;
  Set<OWLIndividual> mapOWLClass(OWLClass owlClass, OWLIndividual owlIndividual) throws MapperException;

  Set<OWLDataPropertyAssertionAxiom> mapOWLDatatypeProperty(OWLProperty owlProperty) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDatatypeProperty(OWLProperty owlProperty, OWLIndividual subject) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDatatypeProperty(OWLProperty owlProperty, OWLDataValue value) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDatatypeProperty(OWLProperty owlProperty, OWLIndividual subject, OWLDataValue value) 
    throws MapperException;

  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty) throws MapperException;
  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty, OWLIndividual subject) throws MapperException;
  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty, OWLIndividual subject, OWLIndividual object) 
    throws MapperException;
} // Mapper
