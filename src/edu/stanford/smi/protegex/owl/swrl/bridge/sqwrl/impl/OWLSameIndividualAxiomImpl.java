
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

public class OWLSameIndividualAxiomImpl extends OWLNaryIndividualAxiomImpl implements OWLSameIndividualAxiom
{
  private OWLIndividual individual1, individual2;

  public OWLSameIndividualAxiomImpl(OWLIndividual individual1, OWLIndividual individual2)
  {
    addIndividual(individual1);
    addIndividual(individual2);
    this.individual1 = individual1;
    this.individual2 = individual2;
  } // OWLSameIndividualsAxiomImpl

  public OWLIndividual getIndividual1() { return individual1; }
  public OWLIndividual getIndividual2() { return individual2; }

  public String toString() { return "sameAs" + super.toString(); }  
} // OWLSameIndividualAxiomImpl
