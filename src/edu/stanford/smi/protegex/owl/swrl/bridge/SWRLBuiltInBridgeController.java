package edu.stanford.smi.protegex.owl.swrl.bridge;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.SWRLBuiltInBridgeException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;

/*
 * 
 */
public interface SWRLBuiltInBridgeController 
{
	void reset() throws SWRLBuiltInBridgeException;
	
  int getNumberOfInjectedOWLClassDeclarations();
  int getNumberOfInjectedOWLIndividualDeclarations() ;
  int getNumberOfInjectedOWLAxioms();

  boolean isInjectedOWLClass(String classURI);
  boolean isInjectedOWLIndividual(String individualURI);
  boolean isInjectedOWLAxiom(OWLAxiom axiom);

  Set<OWLClass> getInjectedOWLClassDeclarations();
  Set<OWLNamedIndividual> getInjectedOWLIndividualDeclarations();
  Set<OWLAxiom> getInjectedOWLAxioms();
}
