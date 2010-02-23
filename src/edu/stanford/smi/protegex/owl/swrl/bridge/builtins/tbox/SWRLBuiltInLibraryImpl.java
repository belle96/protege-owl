// TODO: a lot of repetition here
// cf. http://listserv.manchester.ac.uk/cgi-bin/wa?A2=ind0611&L=dig-wg&T=0&P=754
// lca, max flow, shortest path, http://www.ifi.unizh.ch/ddis/isparql.html
// icon to show OWL expressible SWRL rules.

// How to extract axiom information:

// c = class
// i = individual
// d = data value
// p = property

// a = axiom
// ca = class axiom (composed of class descriptions)
// pa = property axiom
// ia = individual axiom
// da = datatype axiom

// r = restriction 

// Class descriptions: (six types: (1) URI, i.e., a c, (2) enumeration of individuals, (3) property restriction, (4) intersection, (5)
// union, (6) complement

// (1) URI, i.e., a named class
// (2) enumeration of class description
// 3.1.1 enumeration: isOWLEnumerationDescription(?cd), onIndividual(?cd, ?i)

// (3) property restriction class descriptions
// 3.1.2 cardinality restrictions = isCardinalityRestriction(?a), isMinCardinalityRestriction(?a), isMaxCardinalityRestriction(?a), hasCardinality(?a, ?d)
// 3.1.2.1.1 allValuesFromRestriction: isAllValuesFromRestriction(?a), onProperty(?a, ?p), hasValue(?a, ?{i, d})
// 3.1.2.1.2 someValuesFrom: isSomeValuesFrom(?a), onProperty(?a, ?p), hasValue(?a, ?{i,d})
// 3.1.2.1.3 hasValueRestriction: isHasValueRestriction(?a), onProperty(?a, ?p), hasValue(?a, ?{i,d})

// (4) intersection of class description
// 3.1.3.1 intersectionOf: list of owl:oneOf class descriptions: isIntersectionOfClassDescription(?cd), onDescription(?cd, ?cd)

// (5) union of class description
// 3.1.3.2 unionOf: list of owl:oneOf class descriptions: isOWLUnionOfDescription(?cd), onDescription(?cd, ?cd)

// (6) complement of class description
// 3.1.3.3 complementOf: isOWLComplementOfDescription(?cd), onDescription(?cd, ?cd)

// OWL Class axioms (which use cds)
// 3.2.1 rdfs:subClassOf: isSubClassOf(?c, ?c), isOWLSubClassAxiom(?ca), ...
// 3.2.2 owl:equivalentClass: isEquivalentClass(?c, ?c), isOWLEquivalentClassAxiom(?ca), onDescription(?ca, ?cd)
// 3.2.3 isOWLOneOfDescription? how to distinguish from 3.1.1??
// 3.2.4 owl:isDisjointWith: isDisjointWith(?c, ?c), isDisjointWithClassAxiom(?ca), onDescription(?ca, ?cd)

// OWL Property axioms:
// 4.1.1 rdfs:subPropertyOf: isSubPropertyOf(?p,?p), isDirectSubPropertyOf(?p, ?p)
// 4.1.2 rdfs:domain: isInDomainOf(?p, ?c), isInDirectDomainOf(?p, ?c), isOWLInDomainOfAxiom(?pa), onDescription(?pa, ?cd)
// 4.1.3 rdfs:range: isInRangeOf(?p, ?c), isInDirectRangeOf(?p, ?c), isOWLInRangeOfAxiom(?pa), onDescription(?pa, ?cd)
// 4.2.1 owl:equivalentProperty: isEquivalentProperty(?p, ?p), isOWLEquivalentPropertyAxiom(?pa), onProperty(?pa, ?p)
// 4.2.2 owl:inverseOf: isInverseOf(?p, ?p), isOWLInverseOfAxiom(?pa), onProperty(?pa, ?p)
// 4.3.1 owl:FunctionalProperty: isOWLFunctionalProperty(?p), isFunctionalPropertyAxiom(?pa), onProperty(?pa, ?p)
// 4.3.2 owl:InverseFunctionalProperty: isOWLInverseFunctionalProperty(?p), isInverseFunctionalPropertyAxiom(?pa), onProperty(?pa, ?p)
// 4.4.1 owl:TransitiveProperty: isOWLTransitiveProperty(?p), isTransitivePropertyAxiom(?pa), onProperty(?pa, ?p)

// OWL Individual axioms:
// 5.2.1 owl:sameAs: isOWLSameAsAxiom(?ia), onIndividual(?ia, ?i)
// 5.2.2 owl:differentFrom: isOWLDifferentFromAxiom(?ia), onIndividual(?ia, ?i)
// 5.2.3 owl:AllDifferent: isOWLAllDifferentAxiom(?ia), onIndividual(?ia, ?i)
//
// Datatype axioms:
// 6.2 Enumerated datatype: isOWLEnumeratedDatatypeAxiom(?da), onValue(?da, ?d)

package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.tbox;

import java.util.List;
import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInNotImplementedException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

/**
 ** Implementations library for SWRL TBox built-in methods. See <a
 ** href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLTBoxBuiltIns">here</a> for documentation on this library.
 **
 ** See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation on defining SWRL built-in libraries.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  private static String SWRLTBoxLibraryName = "SWRLTBoxBuiltIns";
  
  public SWRLBuiltInLibraryImpl() 
  { 
    super(SWRLTBoxLibraryName); 
  } // SWRLBuiltInLibraryImpl

  public void reset() {}

  /**
   ** Is the second annotation property argument associated with the first ontology, class, property or individual argument. If the second
   ** argument is unbound, bind it to the annotation properties associated with the first argument (if any exist).
   */
  public boolean hasOWLAnnotationProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasOWLAnnotationProperty

  /**
   ** It the second integer argument equal to the cardinality specified by the first owl:Cardinality, owl:MaxCardinality, or
   ** owl:MinCardinality restriction argument. If the second argument is unbound, bind it to the cardinality value.
   */
  public boolean hasCardinality(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasCardinality

  /**
   ** Is the second string argument the value specified by the first rdfs:comment annotation property argument. If the second argument is
   ** unbound, bind it to the value specified by the first rdfs:comment annotation property argument.
   */
  public boolean hasRDFSComment(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasRDFSComment

  /**
   ** Is the second description argument associated with the first class or individual argument. If the second argument is unbound, bind it
   ** to the descriptions associated with the first argument (if any exist).
   */
  public boolean hasOWLDescription(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasOWLDescription

  /**
   ** Is the second resource argument the value specified by the first rdfs:isDefinedBy annotation property argument. If the second argument
   ** is unbound, bind it to the value specified by the first rdfs:isDefinedBy annotation property argument.
   */
  public boolean hasRDFSIsDefinedBy(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasRDFSIsDefinedBy

  /**
   ** Is the second string argument the value specified by the first rdfs:label annotation property argument. If the second argument is
   ** unbound, bind it to the value specified by the first rdfs:label annotation property argument.
   */
  public boolean hasRDFSLabel(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasRDFSLabel

  /**
   ** Is the second resource argument the value specified by the first rdfs:seeAlso annotation property argument. If the second argument
   ** is unbound, bind it to the value specified by the first rdfs:seeAlso annotation property argument.
   */
  public boolean hasRDFSSeeAlso(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasRDFSSeeAlso

  /**
   ** Is the second string argument the value specified by the first rdfs:versionInfo annotation property argument. If the second argument
   ** is unbound, bind it to the value specified by the first rdfs:versionInfo annotation property argument.
   */
  public boolean hasOWLVersionInfo(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // hasOWLVersionInfo

  /**
   ** Are all individual arguments declared to be the different from each other.
   */
  public boolean isOWLAllDifferents(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLAllDifferents

  /**
   ** Is the single argument an owl:AllDifferentsAxiom.
   */
  public boolean isOWLAllDifferentsAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLAllDifferentsAxiom

  /**
   ** Is the single argument an owl:AllValuesFrom restriction.
   */
  public boolean isOWLAllValuesFromRestriction(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLAllValuesFromRestriction

  /**
   ** Is the single argument an annotation property.
   */
  public boolean isOWLAnnotationProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    String propertyName = getArgumentAsAPropertyURI(0, arguments);
    boolean result = false;

    try {
      result = SWRLOWLUtil.isAnnotationProperty(getInvokingBridge().getOWLModel(), propertyName, true);
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLAnnotationProperty

  /**
   ** Is the single argument an owl:Cardinality restriction.
   */
  public boolean isOWLCardinalityRestriction(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLCardinalityRestriction

  /**
   ** Determine if a single argument is an OWL named class. If the argument is unbound, bind it to all OWL named classes in an ontology.
   */
  public boolean isOWLClass(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    boolean isUnboundArgument = isUnboundArgument(0, arguments);   
    boolean result = false;

    try {
      if (getIsInConsequent()) {
        if (isArgumentAString(0, arguments)) {
          String className = SWRLOWLUtil.getFullName(getInvokingBridge().getOWLModel(), getArgumentAsAString(0, arguments));
          if (!getInvokingBridge().isOWLClass(className)) getInvokingBridge().injectOWLClass(className);
        } else checkThatArgumentIsAClass(0, arguments);
        result = true;
      } else {
        if (isUnboundArgument) {
          MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
          for (edu.stanford.smi.protegex.owl.model.OWLNamedClass cls : SWRLOWLUtil.getUserDefinedOWLNamedClasses(getInvokingBridge().getOWLModel()))
            multiArgument.addArgument(createClassArgument(cls.getName()));
          arguments.get(0).setBuiltInResult(multiArgument);
          result = !multiArgument.hasNoArguments();
        } else {
          String className = getArgumentAsAClassURI(0, arguments);
          result = getInvokingBridge().isOWLClass(className);
        } // if
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLClass

  /**
   ** Is the single argument an OWL class description.
   */
  public boolean isOWLClassDescription(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;
    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLClassDescription

  /**
   ** Is the single argument an rdfs:comment annotation.
   */
  public boolean isRDFSComment(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isRDFSComment

  /**
   ** Is the first class argument the complement of the second class argument. If the second argument is unbound, bind it to the complement
   ** of the first argument.
   */
  public boolean isComplementOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;
    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLComplementOf

  /**
   ** Is the single argument an owl:ComplementOf class description.
   */
  public boolean isOWLComplementOfDescription(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;
    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLComplementOfDescription

  /**
   ** Determine if the single argument is an OWL datatype property. If the argument is unbound, bind it to all OWL datatype
   ** properties in an ontology.
   */
  public boolean isOWLDatatypeProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    boolean isUnboundArgument = isUnboundArgument(0, arguments);   
    boolean result = false;

    try {
      if (isUnboundArgument) {
        MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
        for (edu.stanford.smi.protegex.owl.model.OWLProperty property : SWRLOWLUtil.getUserDefinedOWLDatatypeProperties(getInvokingBridge().getOWLModel())) {
          if (property.isObjectProperty()) multiArgument.addArgument(createObjectPropertyArgument(property.getName()));
          else multiArgument.addArgument(createDataPropertyArgument(property.getName()));
        } // for
        arguments.get(0).setBuiltInResult(multiArgument);
        result = !multiArgument.hasNoArguments();
      } else {
        String propertyName = getArgumentAsAPropertyURI(0, arguments);
        result = SWRLOWLUtil.isOWLDataProperty(getInvokingBridge().getOWLModel(), propertyName, false);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLDatatypeProperty

  /**
   ** Is the single argument an rdfs:isDefinedBy annotation.
   */
  public boolean isRDFSIsDefinedBy(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isRDFSIsDefinedBy

  /**
   ** Is the single argument an owl:DifferentFrom axiom.
   */
  public boolean isOWLDifferentFromAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLDifferentFromAxiom

  /**
   ** Check that the second class argument is a direct subclass of the first class argument. If the second argument is unbound, bind it to
   ** the direct subclasses of the first argument (if any exist).
   */
  public boolean isDirectSubClassOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSubClassOf(arguments, false);
  } // isDirectSubClassOf

  /**
   ** Determine if the second property argument is a direct subproperty of the first property argument. If the second argument is unbound,
   ** bind it to the direct sub properties of the first argument (if any exist).
   */
  public boolean isDirectSubPropertyOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSubPropertyOf(arguments, false);
  } // isDirectSubPropertyOf

  /**
   ** Check that the second class argument is a direct superclass of the first class argument. If the second argument is unbound, bind it to
   ** the direct superclasses of the first argument (if any exist).
   */
  public boolean isDirectSuperClassOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSuperClassOf(arguments, false);
  } // isDirectSuperClassOf

  /**
   ** Determine if the second property argument is a direct superproperty of the first property argument. If the second argument is unbound,
   ** bind it to the direct super properties of the first argument (if any exist).
   */
  public boolean isDirectSuperPropertyOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSuperPropertyOf(arguments, false);
  } // isDirectSuperPropertyOf

  /**
   ** Determine if the two class arguments represent classes or properties that are disjoint with each other. If the second argument is
   ** unbound, bind it to the disjoint classes of the first argument (if any exist).
   */
  public boolean isDisjointWith(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isDisjointWith

  /**
   ** Is the single argument an owl:DisjointWith axiom.
   */
  public boolean isOWLDisjointWithAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLDisjointWithAxiom

  /**
   ** Is the single argument an enumerated datatype axiom.
   */
  public boolean isOWLEnumeratedDatatypeAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLEnumeratedDatatypeAxiom

  /**
   ** Determine if the two class arguments are equivalent to each other. If the second
   ** argument is unbound, bind it to the equivalent classes of the first argument (if any exist).
   */
  public boolean isOWLEquivalentClass(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLEquivalentClass

  /**
   ** Is the single argument an owl:EquivalentClass axiom.
   */
  public boolean isOWLEquivalentClassAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLEquivalentClassAxiom

  /**
   ** Determine if the two property arguments are equivalent to each other. If the second argument is unbound, bind it to the equivalent
   ** properties of the first argument (if any exist).
   */
  public boolean isOWLEquivalentProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLEquivalentProperty

  /**
   ** Is the single argument an owl:EquivalentProperty axiom.
   */
  public boolean isOWLEquivalentPropertyAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLEquivalentPropertyAxiom

  /**
   ** Determine if a single property argument is functional.
   */
  public boolean isOWLFunctionalProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    String propertyName = getArgumentAsAPropertyURI(0, arguments);

    boolean result = false;
    try {
      result = SWRLOWLUtil.isOWLFunctionalProperty(getInvokingBridge().getOWLModel(), propertyName, true);
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLFunctionalProperty

  /**
   ** Is the single argument an owl:functionalProperty axiom.
   */
  public boolean isOWLFunctionalPropertyAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isIWKFunctionalPropertyAxiom

  /**
   ** Is the single argument an owl:hasValue restriction
   */
  public boolean isOWLHasValueRestriction(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLHasValueRestriction

  /**
   ** Check that the first class argument is in the domain of the second property argument (excluding its superproperties). If the first
   ** argument is unbound and the second argument is bound, bind the first argument to the domain(s) of the second property argument (if any
   ** exist). If the first class argument is bound and the second argument is unbound, bind the second argument to the properties that have
   ** the class in their domain (if any). An error is thrown if both arguments are unbound.
   */
  public boolean isInDirectDomainOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isInDomainOf(arguments, false);
  } // isInDirectDomainOf

  /**
   ** Check that the first class argument is in the range of the second property argument excluding its superproperties. If the first
   ** argument is unbound, bind it to the range of the second argument (if any exist).
   */
  public boolean isInDirectRangeOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isInRangeOf(arguments, false);
  } // isInDirectRangeOf

  /**
   ** Check that the first class argument is in the domain of the second property argument (including its superproperties). If the first
   ** argument is unbound and the second argument is bound, bind the first argument to the domain(s) of the second property argument (if any
   ** exist). If the first class argument is bound and the second argument is unbound, bind the second argument to the properties that have
   ** the class in their domain (if any). An error is thrown if both arguments are unbound.
   */
  public boolean isInDomainOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isInDomainOf(arguments, true);
  } // isInDomainOf

  /**
   ** Is the single argument an OWL domain axiom.
   */
  public boolean isOWLInDomainOfAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLInDomainOfAxiom

  /**
   ** Check that the first class argument is in the range of the second property argument (including its superproperties). If the first
   ** argument is unbound, bind it to the range of the second argument (if any exist).
   */
  public boolean isInRangeOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isInRangeOf(arguments, true);
  } // isInRangeOf

  /**
   ** Is the single argument an OWL range axiom.
   */
  public boolean isOWLInRangeOfAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLInRangeOfAxiom

  /**
   ** Is the single argument an owl:InteresctionOf class description.
   */
  public boolean isIntersectionOfDescription(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isIntersectionOfDescription

  /**
   ** Is the single argument an owl:InverseFunctionalProperty axiom.
   */
  public boolean isOWLInverseFunctionalPropertyAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLInverseFunctionalPropertyAxiom

  /**
   ** Determine if the second property argument is the inverse of the first property argument.
   */
  public boolean isInverseOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isInverseOf

  /**
   ** Determine if a single property argument is inverse functional.
   */
  public boolean isInverseFunctionalProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    String propertyName = getArgumentAsAPropertyURI(0, arguments);

    boolean result = false;
    try {
      result = SWRLOWLUtil.isInverseFunctionalProperty(getInvokingBridge().getOWLModel(), propertyName, true);
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isInverseFunctionalProperty

  /**
   ** Is the single argument an owl:inverseOf axiom.
   */
  public boolean isOWLInverseOfAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLInverseOfAxiom

  /**
   ** Is the single argument an rdfs:label annotation.
   */
  public boolean isRDFSLabelAnnotation(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isRDFSLabelAnnotation

  /**
   ** Is the single argument an owl:MaxCardinality restriction.
   */
  public boolean isOWLMaxCardinalityRestriction(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLMaxCardinalityRestriction

  /**
   ** Is the single argument an owl:MinCardinality restriction.
   */
  public boolean isOWLMinCardinalityRestriction(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLMinCardinalityRestriction

  /**
   ** Determine if a single argument is an OWL object property. If the argument is unbound, bind it to all OWL object properties in an
   ** ontology.
   */
  public boolean isOWLObjectProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    boolean isUnboundArgument = isUnboundArgument(0, arguments);   
    boolean result = false;

    try {
      if (isUnboundArgument) {
        MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
        for (edu.stanford.smi.protegex.owl.model.OWLProperty property : SWRLOWLUtil.getUserDefinedOWLObjectProperties(getInvokingBridge().getOWLModel())) {
          if (property.isObjectProperty()) multiArgument.addArgument(createObjectPropertyArgument(property.getName()));
          else multiArgument.addArgument(createDataPropertyArgument(property.getName()));
        } // for
        arguments.get(0).setBuiltInResult(multiArgument);
        result = !multiArgument.hasNoArguments();
      } else {
        String propertyName = getArgumentAsAPropertyURI(0, arguments);
        result = SWRLOWLUtil.isObjectProperty(getInvokingBridge().getOWLModel(), propertyName, false);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLObjectProperty

  /**
   ** Is the single argument an owl:oneOf class description.
   */
  public boolean isOWLOneOfClassDescription(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOneOfClassDescription

  /**
   ** Is the single argument an owl:Ontology resource. If the argument is unbound, bind it to the current ontology.
   */
  public boolean isOWLOntology(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLOntology

  /**
   ** Determine if a single property argument is an OWL object or datatype property. If the argument is unbound, bind it to all OWL properties in an ontology.
   */
  public boolean isOWLProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    boolean isUnboundArgument = isUnboundArgument(0, arguments);   
    boolean result = false;

    try {
      if (isUnboundArgument) {
        MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
        for (edu.stanford.smi.protegex.owl.model.OWLProperty property : SWRLOWLUtil.getUserDefinedOWLProperties(getInvokingBridge().getOWLModel())) {
          if (property.isObjectProperty()) multiArgument.addArgument(createObjectPropertyArgument(property.getName()));
          else multiArgument.addArgument(createDataPropertyArgument(property.getName()));
        } // for
        arguments.get(0).setBuiltInResult(multiArgument);
        result = !multiArgument.hasNoArguments();
      } else {
        String propertyName = getArgumentAsAPropertyURI(0, arguments);
        result = SWRLOWLUtil.isProperty(getInvokingBridge().getOWLModel(), propertyName, false);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLProperty

  /**
   ** Is the single argument an rdfs:seeAlso annotation.
   */
  public boolean isRDFSSeeAlsoAnnotation(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isRDFSSeeAlsoAnnotation

  /**
   ** Is the single argument an owl:SameAs axiom.
   */
  public boolean isOWLSameAsAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;
    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLSameAsAxiom

  /**
   ** Check that the first class argument is a subclass of the second class argument. If the first argument is unbound, bind it to
   ** the subclasses of the second argument (if any exist).
   */
  public boolean isSubClassOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSubClassOf(arguments, true);
  } // isSubClassOf

  /**
   ** Determine if the first property argument is a subproperty of the second property argument. If the first argument is unbound,
   ** bind it to the sub properties of the second argument (if any exist).
   */
  public boolean isSubPropertyOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSubPropertyOf(arguments, true);
  } // isSubPropertyOf

  /**
   ** Check that the first class argument is a superclass of the second class argument. If the first argument is unbound, bind it to
   ** the superclasses of the second argument (if any exist).
   */
  public boolean isSuperClassOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSuperClassOf(arguments, true);
  } // isSuperClassOf

  /**
   ** Determine if the first property argument is a superproperty of the second property argument. If the first argument is unbound,
   ** bind it to the super properties of the second argument (if any exist).
   */
  public boolean isSuperPropertyOf(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return isSuperPropertyOf(arguments, true);
  } // isSuperPropertyOf

  /**
   ** Determine if a single property argument is symmetric.
   */
  public boolean isSymmetricProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    String propertyName = getArgumentAsAPropertyURI(0, arguments);

    boolean result = false;
    try {
      result = SWRLOWLUtil.isOWLSymmetricProperty(getInvokingBridge().getOWLModel(), propertyName, true);
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isSymmetricProperty

  /**
   ** Is the single argument an owl:SymmetricProperty axiom.
   */
  public boolean isOWLSymmetricPropertyAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLSymmetricPropertyAxiom

  /**
   ** Determine if a single property argument is transitive.
   */
  public boolean isOWLTransitiveProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    String propertyName = getArgumentAsAPropertyURI(0, arguments);

    boolean result = false;
    try {
      result = SWRLOWLUtil.isOWLTransitiveProperty(getInvokingBridge().getOWLModel(), propertyName);
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLTransitiveProperty

  /**
   ** Is the single argument an owl:TransitiveProperty axiom.
   */
  public boolean isOWLTransitivePropertyAxiom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLTransitivePropertyAxiom

  public boolean isOWLRestriction(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    String className = getArgumentAsAClassURI(0, arguments);
    boolean result = false;

    try {
      result = SWRLOWLUtil.isAnonymousResourceName(getInvokingBridge().getOWLModel(), className);
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isOWLRestriction
    
  /**
   ** Is the single argument an owl:UnionOf class description.
   */
  public boolean isOWLUnionOfDescription(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLUnionOfDescription

  /**
   ** Is the single argument an owl:versionInfo annotation.
   */
  public boolean isOWLVersionInfoAnnotation(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // isOWLVersionInfoAnnotation

  /**
   ** It the second class description argument a subject of the first axiom argument. If the second argument is unbound, bind it to the the
   ** axiom's subject(s).
   */
  public boolean onDescription(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // onDescription

  /**
   ** It the second individual argument a subject of the first individual axiom argument. If the second argument is unbound, bind it to the
   ** individual axiom's subject(s).
   */
  public boolean onIndividual(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // onIndividual

  /**
   ** It the second property argument a subject of the first property axiom argument. If the second argument is unbound, bind it to the
   ** property axiom's subject(s).
   */
  public boolean isSomeValuesFrom(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;
    String restrictionName, onPropertyName, onClassName;

    checkNumberOfArgumentsEqualTo(3, arguments.size());
    restrictionName  = getArgumentAsAClassURI(0, arguments);
    onPropertyName = getArgumentAsAPropertyURI(1, arguments);

    try {
      if (getIsInConsequent()) {
        OWLDataFactory owlFactory = getInvokingBridge().getOWLDataFactory();
        OWLSomeValuesFrom someValuesFrom;
        OWLProperty onProperty;

        if (isArgumentAString(2, arguments)) {
          onClassName = SWRLOWLUtil.getFullName(getInvokingBridge().getOWLModel(), getArgumentAsAString(2, arguments));
          if (!getInvokingBridge().isOWLClass(onClassName)) getInvokingBridge().injectOWLClass(onClassName);
        } else onClassName = getArgumentAsAClassURI(2, arguments);

        if (SWRLOWLUtil.isOWLObjectProperty(getInvokingBridge().getOWLModel(), onPropertyName)) onProperty = owlFactory.getOWLObjectProperty(onPropertyName);
        else onProperty = owlFactory.getOWLDataProperty(onPropertyName);

        someValuesFrom = owlFactory.getOWLSomeValuesFrom(owlFactory.getOWLClass(restrictionName), onProperty, owlFactory.getOWLClass(onClassName));

        // getInvokingBridge().injectOWLRestriction(someValuesFrom); // TODO
        if (someValuesFrom != null) throw new BuiltInNotImplementedException();
      } else {
        onClassName = getArgumentAsAClassURI(2, arguments);
        throw new BuiltInNotImplementedException();
      } // if
    } catch (OWLConversionFactoryException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // onProperty

  /**
   ** It the second datatype value argument a subject of the first datatype axiom argument. If the second argument is unbound, bind it to the
   ** data value axiom's subject(s).
   */
  public boolean onValue(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean result = false;

    if (!result) throw new BuiltInNotImplementedException();

    return result;
  } // onValue

  /**
   ** Check that the two class or property arguments refer to the same underlying entity.
   */
  public boolean equalTo(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(2, arguments.size());

    String resourceName1 = getArgumentAsAURI(0, arguments);
    String resourceName2 = getArgumentAsAURI(1, arguments);

    return resourceName1.equals(resourceName2); // According to RDF specification URI names must character (and cas)e match.
  } // equalTo

  /**
   ** Check that the two class or property arguments do not refer to the same underlying entity.
   */
  public boolean notEqualTo(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return !equalTo(arguments);
  } // notEqualTo

  private boolean isSuperClassOf(List<BuiltInArgument> arguments, boolean transitive) throws BuiltInException
  {
    boolean superClassArgumentUnbound = false;
    String className;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    superClassArgumentUnbound = isUnboundArgument(0, arguments);
    className = getArgumentAsAClassURI(1, arguments);

    try {
      if (superClassArgumentUnbound) {
        List<edu.stanford.smi.protegex.owl.model.OWLNamedClass> superClasses;
        if (transitive) superClasses = SWRLOWLUtil.getSuperClassesOf(getInvokingBridge().getOWLModel(), className);
        else superClasses = SWRLOWLUtil.getDirectSuperClassesOf(getInvokingBridge().getOWLModel(), className);
        if (!superClasses.isEmpty()) {
          MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
          for (edu.stanford.smi.protegex.owl.model.OWLNamedClass superClass : superClasses) multiArgument.addArgument(createClassArgument(superClass.getName()));
          arguments.get(0).setBuiltInResult(multiArgument);
        result = !multiArgument.hasNoArguments();
        } // if
      } else {
        String superClassName = getArgumentAsAClassURI(0, arguments);
        if (transitive) result = SWRLOWLUtil.isSuperClassOf(getInvokingBridge().getOWLModel(), superClassName, className, true);
        else result = SWRLOWLUtil.isDirectSuperClassOf(getInvokingBridge().getOWLModel(), superClassName, className, true);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isSuperClassOf

  private boolean isSubClassOf(List<BuiltInArgument> arguments, boolean transitive) throws BuiltInException
  {
    boolean subClassArgumentUnbound = false;
    String className;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    subClassArgumentUnbound = isUnboundArgument(0, arguments);

    try {
      if (getIsInConsequent()) {
        OWLDataFactory owlFactory = getInvokingBridge().getOWLDataFactory();
        String superclassName;

        if (isArgumentAString(1, arguments)) {
          superclassName = SWRLOWLUtil.getFullName(getInvokingBridge().getOWLModel(), getArgumentAsAString(1, arguments));
          if (!getInvokingBridge().isOWLClass(superclassName)) getInvokingBridge().injectOWLClass(superclassName);
        } else superclassName = getArgumentAsAClassURI(1, arguments);

        if (isArgumentAString(0, arguments)) 
          className = SWRLOWLUtil.getFullName(getInvokingBridge().getOWLModel(), getArgumentAsAString(0, arguments));
        else className = getArgumentAsAClassURI(0, arguments);

        getInvokingBridge().injectOWLAxiom(owlFactory.getOWLSubClassAxiom(owlFactory.getOWLClass(className),owlFactory.getOWLClass(superclassName)));

        result = true;
      } else {
        className = getArgumentAsAClassURI(1, arguments);

        if (subClassArgumentUnbound) {
          List<edu.stanford.smi.protegex.owl.model.OWLNamedClass> subClasses;
          if (transitive) subClasses = SWRLOWLUtil.getSubClassesOf(getInvokingBridge().getOWLModel(), className);
          else subClasses = SWRLOWLUtil.getDirectSubClassesOf(getInvokingBridge().getOWLModel(), className);
          if (!subClasses.isEmpty()) {
            MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
            for (edu.stanford.smi.protegex.owl.model.OWLNamedClass subClass : subClasses) multiArgument.addArgument(createClassArgument(subClass.getName()));
            arguments.get(0).setBuiltInResult(multiArgument);
            result = !multiArgument.hasNoArguments();
          } // if
        } else {
          String subClassName = getArgumentAsAClassURI(0, arguments);
          if (transitive) result = SWRLOWLUtil.isSubClassOf(getInvokingBridge().getOWLModel(), subClassName, className, true);
          else  result = SWRLOWLUtil.isDirectSubClassOf(getInvokingBridge().getOWLModel(), subClassName, className, true);
        } // if
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isSubClassOf

  private boolean isSubPropertyOf(List<BuiltInArgument> arguments, boolean transitive) throws BuiltInException
  {
    boolean subPropertyArgumentUnbound = false;
    String propertyName;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    subPropertyArgumentUnbound = isUnboundArgument(0, arguments);
    propertyName = getArgumentAsAPropertyURI(1, arguments);

    try {
      if (subPropertyArgumentUnbound) {
        List<edu.stanford.smi.protegex.owl.model.OWLProperty> subProperties;
        if (transitive) subProperties = SWRLOWLUtil.getSubPropertiesOf(getInvokingBridge().getOWLModel(), propertyName);
        else subProperties = SWRLOWLUtil.getDirectSubPropertiesOf(getInvokingBridge().getOWLModel(), propertyName);
        if (!subProperties.isEmpty()) {
          MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
          for (edu.stanford.smi.protegex.owl.model.OWLProperty subProperty : subProperties) {
            if (subProperty.isObjectProperty()) multiArgument.addArgument(createObjectPropertyArgument(subProperty.getName()));
            else multiArgument.addArgument(createDataPropertyArgument(subProperty.getName()));
          } // for
          arguments.get(0).setBuiltInResult(multiArgument);
          result = !multiArgument.hasNoArguments();
        } // if
      } else {
        String subPropertyName = getArgumentAsAPropertyURI(0, arguments);
        if (transitive) result = SWRLOWLUtil.isSubPropertyOf(getInvokingBridge().getOWLModel(), subPropertyName, propertyName, true);
        else result = SWRLOWLUtil.isDirectSubPropertyOf(getInvokingBridge().getOWLModel(), subPropertyName, propertyName, true);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isSubPropertyOf

  private boolean isSuperPropertyOf(List<BuiltInArgument> arguments, boolean transitive) throws BuiltInException
  {
    boolean superPropertyArgumentUnbound = false;
    String propertyName;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    superPropertyArgumentUnbound = isUnboundArgument(0, arguments);
    propertyName = getArgumentAsAPropertyURI(1, arguments);

    try {
      if (superPropertyArgumentUnbound) {
        List<edu.stanford.smi.protegex.owl.model.OWLProperty> superProperties;
        if (transitive) superProperties = SWRLOWLUtil.getSuperPropertiesOf(getInvokingBridge().getOWLModel(), propertyName);
        else superProperties = SWRLOWLUtil.getDirectSuperPropertiesOf(getInvokingBridge().getOWLModel(), propertyName);
        if (!superProperties.isEmpty()) {
          MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
          for (edu.stanford.smi.protegex.owl.model.OWLProperty superProperty : superProperties) {
            if (superProperty.isObjectProperty()) multiArgument.addArgument(createObjectPropertyArgument(superProperty.getName()));
            else multiArgument.addArgument(createDataPropertyArgument(superProperty.getName()));
          } // for
          arguments.get(0).setBuiltInResult(multiArgument);
          result = !multiArgument.hasNoArguments();
        } // if
      } else {
        String superPropertyName = getArgumentAsAPropertyURI(0, arguments);
        if (transitive) result = SWRLOWLUtil.isSuperPropertyOf(getInvokingBridge().getOWLModel(), superPropertyName, propertyName, true);
        else result = SWRLOWLUtil.isDirectSuperPropertyOf(getInvokingBridge().getOWLModel(), superPropertyName, propertyName, true);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isSuperPropertyOf

  private boolean isInDomainOf(List<BuiltInArgument> arguments, boolean includingSuperproperties) throws BuiltInException
  {
    boolean domainClassArgumentUnbound, propertyArgumentUnbound = false;
    String propertyName, domainClassName;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    domainClassArgumentUnbound = isUnboundArgument(0, arguments);
    propertyArgumentUnbound = isUnboundArgument(1, arguments);

    if (domainClassArgumentUnbound && propertyArgumentUnbound) throw new BuiltInException("at least one argument must be bound");

    try {
      if (domainClassArgumentUnbound) {
        Set<edu.stanford.smi.protegex.owl.model.OWLNamedClass> domainClasses;
        propertyName = getArgumentAsAPropertyURI(1, arguments);
        if (includingSuperproperties) domainClasses = SWRLOWLUtil.getDomainClasses(getInvokingBridge().getOWLModel(), propertyName);
        else domainClasses = SWRLOWLUtil.getDirectDomainClasses(getInvokingBridge().getOWLModel(), propertyName);
        if (!domainClasses.isEmpty()) {
          MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
          for (edu.stanford.smi.protegex.owl.model.OWLNamedClass domainClass : domainClasses) multiArgument.addArgument(createClassArgument(domainClass.getName()));
          arguments.get(0).setBuiltInResult(multiArgument);
        result = !multiArgument.hasNoArguments();
        } // if
      } else if (propertyArgumentUnbound) {
        Set<edu.stanford.smi.protegex.owl.model.OWLProperty> domainProperties;
        domainClassName = getArgumentAsAClassURI(0, arguments);
        domainProperties = SWRLOWLUtil.getDomainProperties(getInvokingBridge().getOWLModel(), domainClassName, includingSuperproperties);
        if (!domainProperties.isEmpty()) {
          MultiArgument multiArgument = createMultiArgument(getVariableName(1, arguments));
          for (edu.stanford.smi.protegex.owl.model.OWLProperty property : domainProperties) {
            if (property.isObjectProperty()) multiArgument.addArgument(createObjectPropertyArgument(property.getName()));
            else multiArgument.addArgument(createDataPropertyArgument(property.getName()));
          } // for
          arguments.get(1).setBuiltInResult(multiArgument);
        result = !multiArgument.hasNoArguments();
        } // if
      } else { // Both arguments bound
        domainClassName = getArgumentAsAClassURI(0, arguments);
        propertyName = getArgumentAsAPropertyURI(1, arguments);
        if (includingSuperproperties) 
          result = SWRLOWLUtil.isInPropertyDomain(getInvokingBridge().getOWLModel(), propertyName, domainClassName, true);
        else 
          result = SWRLOWLUtil.isInDirectPropertyDomain(getInvokingBridge().getOWLModel(), propertyName, domainClassName, true);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isInDomainOf

  private boolean isInRangeOf(List<BuiltInArgument> arguments, boolean includingSuperproperties) throws BuiltInException
  {
    boolean rangeClassArgumentUnbound = false;
    String rangeClassName, propertyName;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    rangeClassArgumentUnbound = isUnboundArgument(0, arguments);
    propertyName = getArgumentAsAPropertyURI(1, arguments);

    try {
      if (rangeClassArgumentUnbound) {
        Set<edu.stanford.smi.protegex.owl.model.OWLNamedClass> rangeClasses;
        if (includingSuperproperties) rangeClasses = SWRLOWLUtil.getRangeClasses(getInvokingBridge().getOWLModel(), propertyName);
        else rangeClasses = SWRLOWLUtil.getDirectRangeClasses(getInvokingBridge().getOWLModel(), propertyName);
        if (!rangeClasses.isEmpty()) {
          MultiArgument multiArgument = createMultiArgument(getVariableName(0, arguments));
          for (edu.stanford.smi.protegex.owl.model.OWLNamedClass rangeClass : rangeClasses) 
            multiArgument.addArgument(createClassArgument(rangeClass.getName()));
          arguments.get(0).setBuiltInResult(multiArgument);
          result = !multiArgument.hasNoArguments();
        } // if
      } else {
        propertyName = getArgumentAsAPropertyURI(1, arguments);
        rangeClassName = getArgumentAsAClassURI(0, arguments);
        if (includingSuperproperties) result = SWRLOWLUtil.isInPropertyRange(getInvokingBridge().getOWLModel(), propertyName, rangeClassName, true);
        else result = SWRLOWLUtil.isInDirectPropertyRange(getInvokingBridge().getOWLModel(), propertyName, rangeClassName, true);
      } // if
    } catch (SWRLOWLUtilException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // isInRangeOf

} // SWRLBuiltInLibraryImpl
