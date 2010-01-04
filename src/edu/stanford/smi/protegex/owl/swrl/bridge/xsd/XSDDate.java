
package edu.stanford.smi.protegex.owl.swrl.bridge.xsd;

import edu.stanford.smi.protegex.owl.model.XSDNames;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;

public class XSDDate extends XSDType
{
  public XSDDate(String content) throws DataValueConversionException 
  { 
    super(content); 

    setURI(XSDNames.DATE);
  } // XSDDate

  public XSDDate(java.util.Date date) throws DataValueConversionException 
  { 
    super(XSDTimeUtil.date2XSDDateString(date)); 

    setURI(XSDNames.DATE);
  } // XSDDate

  protected void validate() throws DataValueConversionException
  {
    if (getContent() == null) throw new DataValueConversionException("null content for xsd:Date");

    if (!XSDTimeUtil.isValidXSDDate(getContent())) throw new DataValueConversionException("invalid xsd:Date '" + getContent() + "'");
  } // validate

} // XSDDate

