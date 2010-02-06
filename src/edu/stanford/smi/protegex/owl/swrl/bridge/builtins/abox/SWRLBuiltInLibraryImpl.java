
// TODO: lot of repetition in methods. Clean up.
// TODO :has prefix

package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.abox;

import java.util.List;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.bridge.ArgumentFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.MultiArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClass;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLClassAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLIndividual;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLProperty;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLPropertyAssertionAxiom;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLConversionFactoryException;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

/**
 ** Implementations library for SWRL ABox built-in methods. See <a
 ** href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLABoxBuiltIns">here</a> for documentation on this library.
 **
 ** See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLBuiltInBridge">here</a> for documentation on defining SWRL built-in libraries.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary
{
  private static String SWRLABoxLibraryName = "SWRLABoxBuiltIns";

  private ArgumentFactory argumentFactory;

  public SWRLBuiltInLibraryImpl() 
  { 
    super(SWRLABoxLibraryName); 

    argumentFactory = ArgumentFactory.getFactory();
  } // SWRLBuiltInLibraryImpl

  public void reset() {}

  /**
   ** Determine if a single argument is an OWL individual. If the argument is unbound, bind it to all OWL individuals in an ontology.
   */
  public boolean isIndividual(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());
    boolean isUnboundArgument = isUnboundArgument(0, arguments);   
    boolean result = false;

    if (isUnboundArgument) {
      MultiArgument multiArgument = argumentFactory.createMultiArgument(getVariableName(0, arguments));
      for (OWLIndividual individual : getInvokingBridge().getOWLIndividuals()) multiArgument.addArgument(individual);
      arguments.set(0, multiArgument);
      result = !multiArgument.hasNoArguments();
    } else {
      String individualName = getArgumentAsAnIndividualName(0, arguments);
      result = getInvokingBridge().isOWLIndividual(individualName);
    } // if

    return result;
  } // isIndividual

  // TODO: this needs serious cleanup.
  /**
   ** Returns true if the individual named by the first argument has a property specified by the second argument with the value specified by
   ** the third argument. If the third argument in unbound, bind it to all the values for this property for the specified individual.
   */
  public boolean hasValue(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String individualName, propertyName = null;
    Object propertyValue = null;
    boolean propertyValueSupplied;
    boolean result = false, isObjectProperty;

    checkNumberOfArgumentsEqualTo(3, arguments.size());
    individualName = getArgumentAsAnIndividualName(0, arguments);
    propertyName = getArgumentAsAPropertyName(1, arguments);

    propertyValueSupplied = !isUnboundArgument(2, arguments);

    try {
      isObjectProperty = getInvokingBridge().isOWLObjectProperty(propertyName);

      if (getIsInConsequent()) {
        OWLIndividual subject = getInvokingBridge().getOWLDataFactory().getOWLIndividual(individualName);
        OWLProperty property;
        OWLPropertyAssertionAxiom axiom;

        if (isObjectProperty) {
          OWLIndividual value = getArgumentAsAnOWLIndividual(2, arguments);
          property = getInvokingBridge().getOWLDataFactory().getOWLObjectProperty(propertyName);
          axiom = getInvokingBridge().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(subject, property, value);
        } else {
          OWLDataValue value = getArgumentAsAnOWLDatatypeValue(2, arguments);
          property = getInvokingBridge().getOWLDataFactory().getOWLDataProperty(propertyName);
          axiom = getInvokingBridge().getOWLDataFactory().getOWLDataPropertyAssertionAxiom(subject, property, value);
        } // if
        getInvokingBridge().injectOWLAxiom(axiom);
      } else { // In antecedent
        if (propertyValueSupplied) {
          propertyValue = getArgumentAsAPropertyValue(2, arguments);
          if (isObjectProperty) {
            RDFResource resource = SWRLOWLUtil.getObjectPropertyValue(getInvokingBridge().getOWLModel(), individualName, propertyName);
            result = propertyValue.equals(resource.getName());
          } else 
            result = SWRLOWLUtil.getDatavaluedPropertyValues(getInvokingBridge().getOWLModel(), individualName, propertyName).contains(propertyValue);
        } else { // Property value unbound
          MultiArgument multiArgument = argumentFactory.createMultiArgument(getVariableName(2, arguments));
          if (isObjectProperty) {
            for (RDFResource value : SWRLOWLUtil.getObjectPropertyValues(getInvokingBridge().getOWLModel(), individualName, propertyName)) {
              if (value instanceof edu.stanford.smi.protegex.owl.model.OWLIndividual) {
                edu.stanford.smi.protegex.owl.model.OWLIndividual individual = (edu.stanford.smi.protegex.owl.model.OWLIndividual)value;
                multiArgument.addArgument(argumentFactory.createIndividualArgument(individual.getName()));
              } else if (value instanceof edu.stanford.smi.protegex.owl.model.OWLNamedClass) {
                edu.stanford.smi.protegex.owl.model.OWLNamedClass cls = (edu.stanford.smi.protegex.owl.model.OWLNamedClass)value;
                multiArgument.addArgument(argumentFactory.createClassArgument(cls.getName()));
              } else if (value instanceof edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty) {
                edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty owlDatatypeProperty = (edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty)value;
                multiArgument.addArgument(argumentFactory.createDataPropertyArgument(owlDatatypeProperty.getName()));
              } else if (value instanceof edu.stanford.smi.protegex.owl.model.OWLObjectProperty) {
                edu.stanford.smi.protegex.owl.model.OWLObjectProperty owlObjectProperty = (edu.stanford.smi.protegex.owl.model.OWLObjectProperty)value;
                multiArgument.addArgument(argumentFactory.createObjectPropertyArgument(owlObjectProperty.getName()));
              } // if
            } // for
          } else { // Data property
            for (Object value : SWRLOWLUtil.getDatavaluedPropertyValues(getInvokingBridge().getOWLModel(), individualName, propertyName)) {
              if (value instanceof edu.stanford.smi.protegex.owl.model.RDFSLiteral) {
                edu.stanford.smi.protegex.owl.model.RDFSLiteral literal = (edu.stanford.smi.protegex.owl.model.RDFSLiteral)value;
                multiArgument.addArgument(argumentFactory.createDataValueArgument(getInvokingBridge().getOWLDataFactory().getOWLDataValue(literal)));
              } else {
                multiArgument.addArgument(argumentFactory.createDataValueArgument(getInvokingBridge().getOWLDataFactory().getOWLDataValue(value)));
              } // if
            } // for
          } // if
          arguments.set(2, multiArgument);
          result = !multiArgument.hasNoArguments();
        } // if
      } // if
    } catch (OWLConversionFactoryException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // hasValue

  /**
   ** Returns true if the individual named by the first argument has a property specified by the second argument. If the second argument in
   ** unbound, bind it to all the properties that currently have a value for this individual.
   */
  public boolean hasProperty(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String individualName, propertyName = null;
    boolean hasUnboundPropertyArgument = isUnboundArgument(1, arguments);
    boolean result = false;

    checkNumberOfArgumentsInRange(2, 3, arguments.size());
    individualName = getArgumentAsAnIndividualName(0, arguments);
    propertyName = getArgumentAsAPropertyName(1, arguments);

    try {
      if (hasUnboundPropertyArgument) {
        MultiArgument multiArgument = argumentFactory.createMultiArgument(getVariableName(1, arguments));
        for (edu.stanford.smi.protegex.owl.model.OWLProperty property : SWRLOWLUtil.getPropertiesOfIndividual(getInvokingBridge().getOWLModel(), individualName)) {
          if (property.isObjectProperty()) multiArgument.addArgument(argumentFactory.createObjectPropertyArgument(property.getName()));
          else multiArgument.addArgument(argumentFactory.createDataPropertyArgument(property.getName()));
        } // for
        arguments.set(1, multiArgument);
        result = !multiArgument.hasNoArguments();
      } else
        result = SWRLOWLUtil.getNumberOfPropertyValues(getInvokingBridge().getOWLModel(), individualName, propertyName, true) != 0;
    } catch (OWLConversionFactoryException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // hasProperty

  /**
   ** Returns true if the class named by the first argument has an individual identified by the second argument. If the second argument is
   ** unbound, bind it to all individuals of the class.
   */
  public boolean hasIndividual(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean isUnboundArgument = isUnboundArgument(1, arguments);   
    String className;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkThatArgumentIsAClass(0, arguments);

    className = getArgumentAsAClassName(0, arguments);

    try {
      if (isUnboundArgument) {
        MultiArgument multiArgument = argumentFactory.createMultiArgument(getVariableName(1, arguments));
        for (edu.stanford.smi.protegex.owl.model.OWLIndividual individual: SWRLOWLUtil.getIndividualsOfClass(getInvokingBridge().getOWLModel(), className)) 
          multiArgument.addArgument(argumentFactory.createIndividualArgument(individual.getName()));
        arguments.set(1, multiArgument);
        result = !multiArgument.hasNoArguments();
      } else { // Bound argument
        String individualName = getArgumentAsAnIndividualName(1, arguments);
        result = getInvokingBridge().isOWLIndividualOfClass(individualName, className);
      } // if
    } catch (OWLConversionFactoryException e) {
      throw new BuiltInException(e.getMessage());
    } // try
    return result;
  } // hasIndividual

  /**
   ** Returns true if the OWL class, property, or individual named by the first argument has a URI identified by the second
   ** argument. If the second argument is unbound, bind it to URI of the resource.
   */
  public boolean hasURI(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean isUnboundArgument = isUnboundArgument(1, arguments);   
    String resourceName, uri;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());
    checkThatArgumentIsAClassPropertyOrIndividual(0, arguments);

    resourceName = getArgumentAsAResourceName(0, arguments);

    try {
      if (isUnboundArgument) {
        uri = SWRLOWLUtil.getURI(getInvokingBridge().getOWLModel(), resourceName);
        arguments.get(1).setBuiltInResult(argumentFactory.createDataValueArgument(uri));
        result = true;
      } else { // Bound argument
        uri = getArgumentAsAString(1, arguments);
        result = uri.equals(SWRLOWLUtil.getURI(getInvokingBridge().getOWLModel(), resourceName));
      } // if
    } catch (OWLConversionFactoryException e) {
      throw new BuiltInException(e.getMessage());
    } // try
    return result;
  } // hasURI

  /**
   ** Returns true if the individual named by the first argument is an instance of the class identified by the second argument. If the
   ** second argument is unbound, bind it to all defining classes of the individual.
   */
  public boolean hasClass(List<BuiltInArgument> arguments) throws BuiltInException
  {
    boolean isUnboundArgument = isUnboundArgument(1, arguments);   
    String individualName;
    boolean result = false;

    checkNumberOfArgumentsEqualTo(2, arguments.size());

    individualName = getArgumentAsAnIndividualName(0, arguments);

    try {
      if (getIsInConsequent()) {
        String className;
        OWLClass owlClass;
        OWLIndividual owlIndividual;
        OWLClassAssertionAxiom axiom;

        if (isArgumentAString(1, arguments)) { 
          className = SWRLOWLUtil.getFullName(getInvokingBridge().getOWLModel(), getArgumentAsAString(1, arguments));;
          if (!getInvokingBridge().isOWLClass(className)) getInvokingBridge().injectOWLClass(className);
        } else className = getArgumentAsAClassName(1, arguments);

        owlClass  = getInvokingBridge().getOWLDataFactory().getOWLClass(className);
        owlIndividual = getInvokingBridge().getOWLDataFactory().getOWLIndividual(individualName);
        axiom = getInvokingBridge().getOWLDataFactory().getOWLClassAssertionAxiom(owlIndividual, owlClass);

        getInvokingBridge().injectOWLAxiom(axiom);
      } else {
        if (isUnboundArgument) {
          MultiArgument multiArgument = argumentFactory.createMultiArgument(getVariableName(1, arguments));
          for (OWLNamedClass cls: SWRLOWLUtil.getClassesOfIndividual(getInvokingBridge().getOWLModel(), individualName)) 
            multiArgument.addArgument(argumentFactory.createClassArgument(cls.getName()));
          arguments.get(1).setBuiltInResult(multiArgument);
          result = !multiArgument.hasNoArguments();
        } else { // Bound argument
          String className = getArgumentAsAClassName(1, arguments);
          result = getInvokingBridge().isOWLIndividualOfClass(individualName, className);
        } // if
      } // if
    } catch (OWLConversionFactoryException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // hasClass

  /**
   ** Returns true if the individual named by the second argument has the number of values specified by the first argument of the property
   ** specified by the third argument.
   */
  public boolean hasNumberOfPropertyValues(List<BuiltInArgument> arguments) throws BuiltInException
  {
    String individualName, propertyName = null;
    int numberOfPropertyValues;
    boolean hasUnboundNumberOfPropertyValuesArgument = isUnboundArgument(0, arguments);
    boolean result = false;

    checkNumberOfArgumentsEqualTo(3, arguments.size());
    individualName = getArgumentAsAnIndividualName(1, arguments);
    propertyName = getArgumentAsAPropertyName(2, arguments);

    try {
      if (hasUnboundNumberOfPropertyValuesArgument) {
        numberOfPropertyValues = SWRLOWLUtil.getNumberOfPropertyValues(getInvokingBridge().getOWLModel(), individualName, propertyName, true);
        arguments.get(0).setBuiltInResult(argumentFactory.createDataValueArgument(numberOfPropertyValues));
        result = true;
      } else {
        numberOfPropertyValues = getArgumentAsAnInteger(0, arguments);
        result = SWRLOWLUtil.getNumberOfPropertyValues(getInvokingBridge().getOWLModel(), individualName, propertyName, true) == numberOfPropertyValues;
      } // if
    } catch (OWLConversionFactoryException e) {
      throw new BuiltInException(e.getMessage());
    } // try

    return result;
  } // hasNumberOfPropertyValues

  public boolean isConstant(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());

    return isArgumentALiteral(0, arguments);
  } // isConstant

  public boolean notConstant(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());

    return !isArgumentALiteral(0, arguments);
  } // notConstant

  public boolean isNumeric(List<BuiltInArgument> arguments) throws BuiltInException
  {
    checkNumberOfArgumentsEqualTo(1, arguments.size());

    return isArgumentNumeric(0, arguments);
  } // isNumeric

  public boolean notNumeric(List<BuiltInArgument> arguments) throws BuiltInException
  {
    return !isNumeric(arguments);
  } // notNumeric

} // SWRLBuiltInLibraryImpl
