
package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.MapperException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;

public interface Mapper
{
  boolean isMapped(OWLClass owlClass);
  boolean isMapped(OWLProperty owlProperty);

  void open() throws MapperException;
  void close() throws MapperException;

  Set<OWLIndividual> mapOWLClass(OWLClass owlClass) throws MapperException;
  Set<OWLIndividual> mapOWLClass(OWLClass owlClass, OWLIndividual owlIndividual) throws MapperException;

  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty, OWLIndividual subject) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty, OWLDataValue value) throws MapperException;
  Set<OWLDataPropertyAssertionAxiom> mapOWLDataProperty(OWLProperty owlProperty, OWLIndividual subject, OWLDataValue value) 
    throws MapperException;

  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty) throws MapperException;
  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty, OWLIndividual subject) throws MapperException;
  Set<OWLObjectPropertyAssertionAxiom> mapOWLObjectProperty(OWLProperty owlProperty, OWLIndividual subject, OWLIndividual object) 
    throws MapperException;
} // Mapper
