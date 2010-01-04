
package edu.stanford.smi.protegex.owl.swrl.bridge.xsd;

import edu.stanford.smi.protegex.owl.model.XSDNames;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;

public class XSDDuration extends XSDType
{
  public XSDDuration(String content) throws DataValueConversionException 
  { 
    super(content); 

    setURI(XSDNames.DURATION);
  } // XSDDuration

  public XSDDuration(org.apache.axis.types.Duration duration) throws DataValueConversionException 
  { 
    super(duration.toString()); 

    setURI(XSDNames.DURATION);
  } // XSDDuration

  protected void validate() throws DataValueConversionException
  {
    if (getContent() == null) throw new DataValueConversionException("null content for XSD:duration literal");

    if (!XSDTimeUtil.isValidXSDDuration(getContent())) throw new DataValueConversionException("invalid xsd:Duration: " + getContent());
  }  // validate

} // XSDDuration

