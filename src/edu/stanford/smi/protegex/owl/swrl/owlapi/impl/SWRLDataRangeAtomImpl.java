
package edu.stanford.smi.protegex.owl.swrl.owlapi.impl;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLDataRangeAtom;

/**
 * Class representing a SWRL data range atom
 */
public class SWRLDataRangeAtomImpl extends SWRLAtomImpl implements SWRLDataRangeAtom
{
  private Set<OWLDataValue> values;
  
  public SWRLDataRangeAtomImpl(Set<OWLDataValue> values) 
  {
    this.values = values;
  }
  
  public int getNumberOfArguments() { return values.size(); }
}
