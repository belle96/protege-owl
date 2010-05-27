
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.MapperException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;

public interface Mapper
{
  boolean isMapped(OWLClass owlClass);
  boolean isMapped(OWLProperty owlProperty);

  void open() throws MapperException;
  void close() throws MapperException;

  Set<OWLNamedIndividual> mapOWLClass(OWLClass owlClass) throws MapperException;
  Set<OWLNamedIndividual> mapOWLClass(OWLClass owlClass, OWLNamedIndividual owlIndividual) throws MapperException;

  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty, OWLNamedIndividual subject) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty, OWLDataValue value) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty, OWLNamedIndividual subject, OWLDataValue value) 
    throws MapperException;

  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty) throws MapperException;
  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty, OWLNamedIndividual subject) throws MapperException;
  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty, OWLNamedIndividual subject, OWLNamedIndividual object) 
    throws MapperException;
} // Mapper
