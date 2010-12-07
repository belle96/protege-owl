package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSubClassAxiom;

/**
 * Interface that defines method that must be implemented by a target rule engine to natively representing OWL axioms.
 * 
 * A default implementation that does most of the heavy lifting can be found in the DefaultOWLAxiomConvertor class.
 */
public interface OWLAxiomConvertor
{
	void generateOWLAxiom(OWLAxiom axiom) throws TargetSWRLRuleEngineException;
	
	void generateOWLDeclarationAxiom(OWLDeclarationAxiom axiom) throws TargetSWRLRuleEngineException; 
  void generateOWLDataPropertyAssertionAxiom(OWLDataPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLSameIndividualAxiom(OWLSameIndividualAxiom axiom)	throws TargetSWRLRuleEngineException;
  void generateOWLDifferentIndividualsAxiom(OWLDifferentIndividualsAxiom axiom)	throws TargetSWRLRuleEngineException;
  void generateOWLClassAssertionAxiom(OWLClassAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLSubclassAxiom(OWLSubClassAxiom axiom) throws TargetSWRLRuleEngineException;
  
  void generateOWLClassPropertyAssertionAxiom(OWLClassPropertyAssertionAxiom axiom)	throws TargetSWRLRuleEngineException;
  void generateOWLPropertyPropertyAssertionAxiom(OWLPropertyPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
} 
