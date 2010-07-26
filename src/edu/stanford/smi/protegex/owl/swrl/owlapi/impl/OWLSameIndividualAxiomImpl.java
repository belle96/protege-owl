
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSameIndividualAxiom;

public class OWLSameIndividualAxiomImpl extends OWLNaryIndividualAxiomImpl implements OWLSameIndividualAxiom
{
  private OWLNamedIndividual individual1, individual2;

  public OWLSameIndividualAxiomImpl(OWLNamedIndividual individual1, OWLNamedIndividual individual2)
  {
    addIndividual(individual1);
    addIndividual(individual2);
    this.individual1 = individual1;
    this.individual2 = individual2;
  } 

  public OWLNamedIndividual getIndividual1() { return individual1; }
  public OWLNamedIndividual getIndividual2() { return individual2; }

  public String toString() { return "sameAs" + super.toString(); }  
} 
