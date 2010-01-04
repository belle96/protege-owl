
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.bridge.DataRangeAtom;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataValue;

/*
** Class representing a SWRL data range atom
*/
public class DataRangeAtomImpl extends AtomImpl implements DataRangeAtom
{
  private Set<OWLDataValue> values;
  
  public DataRangeAtomImpl(Set<OWLDataValue> values) 
  {
    this.values = values;
  } // DataRangeAtomImpl

} // DataRangeAtomImpl
