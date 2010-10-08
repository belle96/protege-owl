package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.TargetSWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLClassPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDeclarationAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDifferentIndividualsAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLNamedIndividual;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLObjectPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSameIndividualAxiom;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLSubClassAxiom;

/**
 * Interface that defines method that must be implemented by a target rule engine when natively representing OWL axioms.
 * 
 * A default implementation that does most of the heavy lifting can be found in the DefaultOWLAxiomConvetor class.
 */
public interface OWLAxiomConvertor
{
  void generateOWLAxiomRepresentation(OWLAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLClassDeclarationRepresentation(OWLClass owlClass) throws TargetSWRLRuleEngineException;
  void generateOWLPropertyDeclarationRepresentation(OWLProperty owlProperty) throws TargetSWRLRuleEngineException;
  void generateOWLIndividualDeclarationRepresentation(OWLNamedIndividual individual) throws TargetSWRLRuleEngineException;
  void generateOWLDataPropertyAssertionAxiomRepresentation(OWLDataPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLObjectPropertyAssertionAxiomRepresentation(OWLObjectPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLClassPropertyAssertionAxiomRepresentation(OWLClassPropertyAssertionAxiom axiom)	throws TargetSWRLRuleEngineException;
  void generateOWLPropertyPropertyAssertionAxiomRepresentation(OWLPropertyPropertyAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLSameIndividualAxiomRepresentation(OWLSameIndividualAxiom axiom)	throws TargetSWRLRuleEngineException;
  void generateOWLDifferentIndividualsAxiomRepresentation(OWLDifferentIndividualsAxiom axiom)	throws TargetSWRLRuleEngineException;
  void generateOWLDeclarationAxiomRepresentation(OWLDeclarationAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLClassAssertionAxiomRepresentation(OWLClassAssertionAxiom axiom) throws TargetSWRLRuleEngineException;
  void generateOWLSubclassAxiomRepresentation(OWLSubClassAxiom axiom) throws TargetSWRLRuleEngineException;
} 
