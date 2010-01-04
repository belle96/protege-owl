package edu.stanford.smi.protegex.owl.model;

/**
 * An interface for objects that can create user-defined XML Schema datatypes.
 * This has been abstracted so that alternative implementations can be provides
 * until the RDF/OWL specification has found a suitable solution.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFSDatatypeFactory {

    RDFSDatatype createAnonymousDatatype(RDFSDatatype baseType);


    RDFSDatatype createDatatype(RDFSDatatype baseType, String name);


    void setLength(RDFSDatatype datatype, int value);


    void setMaxExclusive(RDFSDatatype datatype, RDFSLiteral literal);


    void setMaxInclusive(RDFSDatatype datatype, RDFSLiteral literal);


    void setMaxLength(RDFSDatatype datatype, int value);


    void setMinExclusive(RDFSDatatype datatype, RDFSLiteral literal);


    void setMinInclusive(RDFSDatatype datatype, RDFSLiteral literal);


    void setMinLength(RDFSDatatype datatype, int value);


    void setPattern(RDFSDatatype datatype, String value);
}
