
package edu.stanford.smi.protegex.owl.swrl.bridge.sqwrl.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;

import java.util.*;

public abstract class OWLNaryIndividualAxiomImpl implements OWLNaryIndividualAxiom
{
  Set<OWLIndividual> individuals;
  
  public OWLNaryIndividualAxiomImpl() 
  { 
    individuals = new HashSet<OWLIndividual>();
  } // OWLNaryIndividualAxiomImpl
  
  public Set<OWLIndividual> getIndividuals() { return individuals; }

  void addIndividual(OWLIndividual owlIndividual) { individuals.add(owlIndividual); }
  void addIndividuals(Set<OWLIndividual> individuals) { this.individuals.addAll(individuals); }

  public String toString() 
  { 
    String result = "(";
    boolean isFirst = true;

    for (OWLIndividual individual: individuals) {
      if (!isFirst) result += ", ";
      result += individual.toString();
      isFirst = false;
    } // for

    result += ")";

    return result;
  } // toString    

} // OWLNaryIndividualAxiomImpl
