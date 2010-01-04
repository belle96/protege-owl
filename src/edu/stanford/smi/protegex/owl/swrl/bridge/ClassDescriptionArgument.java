
package edu.stanford.smi.protegex.owl.swrl.bridge;

/**
 ** Interface representing OWL class description arguments to  built-ins.
 */
public interface ClassDescriptionArgument extends BuiltInArgument 
{
  String getRepresentation();
  boolean isNamedClass();
} // ClassDescriptionArgument
