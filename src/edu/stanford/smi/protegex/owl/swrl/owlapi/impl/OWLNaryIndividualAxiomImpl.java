
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.*;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNaryIndividualAxiom;

import java.util.*;

public abstract class OWLNaryIndividualAxiomImpl implements OWLNaryIndividualAxiom
{
  Set<OWLNamedIndividual> individuals;
  
  public OWLNaryIndividualAxiomImpl() 
  { 
    individuals = new HashSet<OWLNamedIndividual>();
  } // OWLNaryIndividualAxiomImpl
  
  public Set<OWLNamedIndividual> getIndividuals() { return individuals; }

  void addIndividual(OWLNamedIndividual owlIndividual) { individuals.add(owlIndividual); }
  void addIndividuals(Set<OWLNamedIndividual> individuals) { this.individuals.addAll(individuals); }

  public String toString() 
  { 
    String result = "(";
    boolean isFirst = true;

    for (OWLNamedIndividual individual: individuals) {
      if (!isFirst) result += ", ";
      result += individual.toString();
      isFirst = false;
    } // for

    result += ")";

    return result;
  } // toString    

} // OWLNaryIndividualAxiomImpl
