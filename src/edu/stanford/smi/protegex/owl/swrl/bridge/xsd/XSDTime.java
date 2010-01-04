
package edu.stanford.smi.protegex.owl.swrl.bridge.xsd;

import edu.stanford.smi.protegex.owl.model.XSDNames;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;

public class XSDTime extends XSDType
{
  public XSDTime(String content) throws DataValueConversionException 
  { 
    super(content); 

    setURI(XSDNames.TIME);
  } // XSDTime

  public XSDTime(java.util.Date date) throws DataValueConversionException 
  { 
    super(XSDTimeUtil.date2XSDTimeString(date)); 

    setURI(XSDNames.TIME);
  } // XSDTime

  protected void validate() throws DataValueConversionException
  {
    if (getContent() == null) throw new DataValueConversionException("null content for xsd:Time");

    if (!XSDTimeUtil.isValidXSDTime(getContent())) throw new DataValueConversionException("invalid xsd:Time '" + getContent() + "'");
  } // validate

} // XSDTime

