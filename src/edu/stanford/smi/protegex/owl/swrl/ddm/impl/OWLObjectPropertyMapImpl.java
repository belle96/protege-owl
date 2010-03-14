
package edu.stanford.smi.protegex.owl.swrl.ddm.impl;

import edu.stanford.smi.protegex.owl.swrl.ddm.*;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLProperty;


public class OWLObjectPropertyMapImpl extends OWLPropertyMapImpl implements OWLObjectPropertyMap
{
  private ForeignKey foreignKey;

  public OWLObjectPropertyMapImpl(OWLProperty owlProperty, PrimaryKey primaryKey, ForeignKey foreignKey)
  {
    super(owlProperty, primaryKey);
    this.foreignKey = foreignKey;
  } // OWLObjectPropertyMapImpl

  public ForeignKey getForeignKey() { return foreignKey; }
} // OWLObjectPropertyMap
