
package edu.stanford.smi.protegex.owl.swrl.bridge.impl;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.CollectionArgument;

public class CollectionArgumentImpl extends BuiltInArgumentImpl implements CollectionArgument
{
  private String collectionID;
  
  public CollectionArgumentImpl(String collectionID) { this.collectionID = collectionID; }
  
  public String getID() { return collectionID; }
  
  public String toString() { return getID(); }
  
  public int compareTo(BuiltInArgument o)
  {
  	return collectionID.compareTo(((CollectionArgument)o).getID());
  }

  public boolean equals(Object obj)
  {
    if(this == obj) return true;
    if((obj == null) || (obj.getClass() != this.getClass())) return false;
    CollectionArgumentImpl impl = (CollectionArgumentImpl)obj;
    return (getID() == impl.getID() || (getID() != null && getID().equals(impl.getID())));
    } 

  public int hashCode()
  {
    int hash = 12;
    hash = hash + (null == getID() ? 0 : getID().hashCode());   
    return hash;
  }
}
