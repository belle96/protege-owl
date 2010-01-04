
package edu.stanford.smi.protegex.owl.swrl.bridge.xsd;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;

import edu.stanford.smi.protegex.owl.model.XSDNames;

public class XSDAnyURI extends XSDType
{
  public XSDAnyURI(String content) throws DataValueConversionException 
  { 
    super(content); 

    setURI(XSDNames.ANY_URI);
  } // XSDAnyURI

  protected void validate() throws DataValueConversionException
  {
    if (getContent() == null) throw new DataValueConversionException("null content for AnyURI literal");
    // TODO: validate
  }  // validate
} // XSDAnyURI
