package edu.stanford.smi.protegex.owl.swrl.bridge;

import edu.stanford.smi.protegex.owl.swrl.bridge.impl.OWLDataValueFactoryImpl;
import edu.stanford.smi.protegex.owl.swrl.bridge.xsd.XSDType;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLLiteral;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.DataValueConversionException;

/**
 * The SWRLTab deals with literals using its local OWLDataValue class. This class wraps the less convenient OWLLiteral and OWLDataType classes.
 */
public abstract class OWLDataValueFactory 
{
	public static OWLDataValueFactory create() { return new OWLDataValueFactoryImpl(); }
	
	public abstract OWLDataValue getOWLDataValue(OWLLiteral literal);
	public abstract OWLDataValue getOWLDataValue(DataValue dataValue);
	public abstract OWLDataValue getOWLDataValue(String s);
	public abstract OWLDataValue getOWLDataValue(boolean b);
	public abstract OWLDataValue getOWLDataValue(Boolean b);
	public abstract OWLDataValue getOWLDataValue(int i);
	public abstract OWLDataValue getOWLDataValue(long l);
	public abstract OWLDataValue getOWLDataValue(float f);
	public abstract OWLDataValue getOWLDataValue(double d);
	public abstract OWLDataValue getOWLDataValue(short s);
	public abstract OWLDataValue getOWLDataValue(Byte b);
	public abstract OWLDataValue getOWLDataValue(XSDType xsd);
	public abstract OWLDataValue getOWLDataValue(Object o) throws DataValueConversionException; // TODO: get rid of this
}
