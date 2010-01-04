
package edu.stanford.smi.protegex.owl.swrl.bridge.xsd;

import edu.stanford.smi.protegex.owl.model.XSDNames;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;

public class XSDDateTime extends XSDType
{
  public XSDDateTime(String content) throws DataValueConversionException 
  { 
    super(content); 

    setURI(XSDNames.DATE_TIME);
  } // XSDDateTime

  public XSDDateTime(java.util.Date date) throws DataValueConversionException
  { 
    super(XSDTimeUtil.date2XSDDateTimeString(date)); 

    setURI(XSDNames.DATE_TIME);
  } // XSDDateTime

  protected void validate() throws DataValueConversionException
  {
    if (getContent() == null) throw new DataValueConversionException("null content for xsd:DateTime");

    if (!XSDTimeUtil.isValidXSDDateTime(getContent())) throw new DataValueConversionException("invalid xsd:DateTime '" + getContent() + "'");
  } // validate

} // XSDDateTime
