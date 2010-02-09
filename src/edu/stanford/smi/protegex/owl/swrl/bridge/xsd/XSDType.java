
package edu.stanford.smi.protegex.owl.swrl.bridge.xsd;

import java.net.URISyntaxException;

import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.DataValueConversionException;

public abstract class XSDType implements Comparable<XSDType>
{
  private String content;
  private java.net.URI uri = null;

  public XSDType(String content) throws DataValueConversionException
  {
    this.content = content;
    validate();
  } // XSDType

  public String getContent() { return content; }

  public String toString() { return content; }

  public java.net.URI getURI() { return uri; }

  public int compareTo(XSDType xsdType)
  {
    return content.compareTo(xsdType.getContent()); 
  } // compareTo

  protected abstract void validate() throws DataValueConversionException;

  protected void setURI(String uriString) throws DataValueConversionException
  {
    try {
      uri = new java.net.URI(uriString);
    } catch (URISyntaxException e) {
      throw new DataValueConversionException("invalid URI " + uri + " associated with value " + content + "");
    } // try
  } // setURI

} // XSDType
