package edu.stanford.smi.protegex.owl.model.impl;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFSLiteral implements RDFSLiteral {
    
    public static final String DATATYPE_PREFIX = "~@";
    public static final String LANGUAGE_PREFIX = "~#";
    public static final char SEPARATOR = ' ';       

    private OWLModel owlModel;
    private String rawValue;    
    private String language; 


    public DefaultRDFSLiteral(OWLModel owlModel, String rawValue) {
    	this.owlModel = owlModel;
    	this.rawValue = rawValue;
        this.language = getLanguageFromRawValue();
    }
    
    public void accept(OWLModelVisitor visitor) {
        visitor.visitRDFSLiteral(this);
    }


    public int compareTo(RDFSLiteral other) {
        RDFSDatatype datatype = getDatatype();
        RDFSDatatype otherDatatype = other.getDatatype();
        if (datatype.equals(otherDatatype)) {
            if (owlModel.getIntegerDatatypes().contains(datatype)) {
                int a = getInt();
                int b = other.getInt();
                return new Integer(a).compareTo(new Integer(b));
            }
            else if (owlModel.getFloatDatatypes().contains(datatype)) {
                double a = getDouble();
                double b = other.getDouble();
                return new Double(a).compareTo(new Double(b));
            }
        }
        return rawValue.compareTo(other.getRawValue());
    }


    public static RDFSLiteral create(OWLModel owlModel, Object value) {
        RDFSDatatype datatype = null;
        if (value instanceof Boolean) {
            datatype = owlModel.getXSDboolean();
        }
        else if (value instanceof Long) {
            datatype = owlModel.getXSDlong();
        }
        else if (value instanceof Integer) {
            datatype = owlModel.getXSDint();
        }
        else if (value instanceof Short) {
            datatype = owlModel.getXSDshort();
        }
        else if (value instanceof Byte) {
            datatype = owlModel.getXSDbyte();
        }
        else if (value instanceof Double) {
            datatype = owlModel.getXSDdouble();
        }
        else if (value instanceof Float) {
            datatype = owlModel.getXSDfloat();
        }
        else if (value instanceof String) {
        	if (isRawValue((String)value)) {
        		return new DefaultRDFSLiteral(owlModel, (String)value);
        	}
            datatype = owlModel.getXSDstring();
        }
        else if (value instanceof byte[]) {
            datatype = owlModel.getXSDbase64Binary();
            value = XSDDatatype.XSDbase64Binary.unparse(value);
        }
        else if (value instanceof BigDecimal) {
            datatype = owlModel.getXSDdecimal();
        }
        else if (value instanceof BigInteger) {
            datatype = owlModel.getXSDinteger();
        }
        else {
            throw new IllegalArgumentException("Value type " + value.getClass() +
                                               " of value \"" + value + "\" cannot be mapped into default RDFSDatatype.");
        }
        return new DefaultRDFSLiteral(owlModel, getRawValue(String.valueOf(value), datatype));
    }

    public static RDFSLiteral create(OWLModel owlModel, String lexicalValue, RDFSDatatype datatype) {
        return new DefaultRDFSLiteral(owlModel, getRawValue(lexicalValue, datatype));
    }


    public static RDFSLiteral create(OWLModel owlModel, String text, String language) {
    	return new DefaultRDFSLiteral(owlModel, getRawValue(text, language));       
    }


    public boolean equals(Object obj) {
        if (obj instanceof DefaultRDFSLiteral) {
            return rawValue.equals(((DefaultRDFSLiteral) obj).rawValue);
        }
        return false;
    }


    public boolean equalsStructurally(RDFObject object) {
        if (object instanceof RDFSLiteral) {
            return ((RDFSLiteral) object).equals(this);
        }
        return false;
    }


    public boolean getBoolean() {
        final String str = getString();
        return Boolean.valueOf(str).booleanValue();
    }


    public String getBrowserText() {
        return toString();
    }


    public byte[] getBytes() {
        return (byte[]) XSDDatatype.XSDbase64Binary.parse(getString());
    }


    public RDFSDatatype getDatatype() {
        if (rawValue.startsWith(LANGUAGE_PREFIX)) {
            return owlModel.getXSDstring();
        }
        else {
            int index = rawValue.indexOf(SEPARATOR);
            
            if (index == -1) { // no separator - should not be the case
            	return owlModel.getXSDstring();
            }
            String datatypeName = rawValue.substring(2, index);
            RDFSDatatype datatype = owlModel.getRDFSDatatypeByName(datatypeName);                  
            return datatype; 
        }
    }


    public double getDouble() {
        return Double.parseDouble(getString().trim());
    }


    public float getFloat() {
        return Float.parseFloat(getString().trim());
    }


    public int getInt() {
        return Integer.parseInt(getString().trim());
    }

    public String getLanguage() {
    	return language;
    }
    
    protected String getLanguageFromRawValue() {
        String lang = null;
        if (rawValue.startsWith(LANGUAGE_PREFIX)) {
            int endIndex = rawValue.indexOf(SEPARATOR);
            if (endIndex > 0) {
                lang = rawValue.substring(2, endIndex);
            }
            else {
                lang = rawValue.substring(2);
            }
        }
        return lang;
    }


    public long getLong() {
        return Long.parseLong(getString());
    }

    public short getShort() {
      return Short.parseShort(getString());
    } 


    public Object getPlainValue() {
        String lang = getLanguage();
        if (lang != null) {
            if (lang.length() == 0) {
                return getString();
            }
            else {
                return null;
            }
        }
        RDFSDatatype datatype = getDatatype();
        if (owlModel.getXSDstring().equals(datatype)) {
            if (getLanguage() == null) {
                return getString();
            }
        }
        else if (owlModel.getXSDint().equals(datatype)) {
            try {
                return Integer.valueOf(getString().trim());
            }
            catch (Exception ex) {
                return new Integer(0);
            }
        }
        else if (owlModel.getXSDfloat().equals(datatype)) {
            try {
                return Float.valueOf(getString().trim());
            }
            catch (Exception ex) {
                return new Float(0);
            }
        }
        else if (owlModel.getXSDboolean().equals(datatype)) {
            try {
                return Boolean.valueOf(getString());
            }
            catch (Exception ex) {
                return Boolean.FALSE;
            }
        }
        return null;
    }


    public static Object getPlainValueIfPossible(Object value) {
        if (value instanceof RDFSLiteral) {
            RDFSLiteral literal = (RDFSLiteral) value;
            Object plainValue = literal.getPlainValue();
            if (plainValue != null) {
                return plainValue;
            }
        }
        return value;
    }


    public String getRawValue() {
        return rawValue;
    }


    public String getString() {
        if (rawValue.startsWith(LANGUAGE_PREFIX) || rawValue.startsWith(DATATYPE_PREFIX)) {
            return rawValue.substring(rawValue.indexOf(SEPARATOR) + 1);
        }
        return rawValue;
    }


    public final static String getRawValue(String lexicalValue, RDFSDatatype datatype) {   	
        if (datatype.equals(datatype.getOWLModel().getXSDboolean())) {
            if ("1".equals(lexicalValue)) {
                lexicalValue = Boolean.TRUE.toString();
            }
        }
        if (!datatype.isSystem() && datatype.isAnonymous()) {
            datatype = datatype.getBaseDatatype();
        }
        return DATATYPE_PREFIX + datatype.getName() + SEPARATOR + lexicalValue;
    }


    public final static String getRawValue(String text, String lang) {
        if (lang == null || lang.length() == 0) {
            return text;
        }
        else {
            return LANGUAGE_PREFIX + lang + SEPARATOR + text;
        }
    }


    public int hashCode() {
        return rawValue.hashCode();
    }


    public final static boolean isRawValue(String value) {
        return value.startsWith(LANGUAGE_PREFIX) || value.startsWith(DATATYPE_PREFIX);
    }


    public String toString() {
        return getString();
    }
}
