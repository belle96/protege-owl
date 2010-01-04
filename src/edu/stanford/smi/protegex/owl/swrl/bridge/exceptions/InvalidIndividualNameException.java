
package edu.stanford.smi.protegex.owl.swrl.bridge.exceptions;

public class InvalidIndividualNameException extends OWLFactoryException 
{
  public InvalidIndividualNameException(String name) 
  { 
    super("invalid individual name '" + name + "'"); 
  } // InvalidIndividualNameException
} // InvalidIndividualNameException
