package edu.stanford.smi.protegex.owl.model;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface XSDNames {

    final static String PREFIX = "xsd:";
    
    //TT: replace PREFIX with XSD_NAMESPACE
    final static String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";


    final static String ANY_URI = (XSD_NAMESPACE + "anyURI").intern();

    final static String BASE_64_BINARY = (XSD_NAMESPACE + "base64Binary").intern();

    final static String BOOLEAN = (XSD_NAMESPACE + "boolean").intern();

    final static String BYTE = (XSD_NAMESPACE + "byte").intern();

    final static String DATE = (XSD_NAMESPACE + "date").intern();

    final static String DATE_TIME = (XSD_NAMESPACE + "dateTime").intern();

    final static String DECIMAL = (XSD_NAMESPACE + "decimal").intern();

    final static String DOUBLE = (XSD_NAMESPACE + "double").intern();

    final static String DURATION = (XSD_NAMESPACE + "duration").intern();

    final static String FLOAT = (XSD_NAMESPACE + "float").intern();

    final static String INT = (XSD_NAMESPACE + "int").intern();

    final static String INTEGER = (XSD_NAMESPACE + "integer").intern();
    
    final static String NON_NEGATIVE_INTEGER = (XSD_NAMESPACE + "nonNegativeInteger").intern();

    final static String LONG = (XSD_NAMESPACE + "long").intern();

    final static String SHORT = (XSD_NAMESPACE + "short").intern();

    final static String STRING = (XSD_NAMESPACE + "string").intern();

    final static String TIME = (XSD_NAMESPACE + "time").intern();


    static interface Facet {

        final static String LENGTH = (XSD_NAMESPACE + "length").intern();

        final static String MAX_EXCLUSIVE = (XSD_NAMESPACE + "maxExclusive").intern();

        final static String MAX_INCLUSIVE = (XSD_NAMESPACE + "maxInclusive").intern();

        final static String MAX_LENGTH = (XSD_NAMESPACE + "maxLength").intern();

        final static String MIN_EXCLUSIVE = (XSD_NAMESPACE + "minExclusive").intern();

        final static String MIN_INCLUSIVE = (XSD_NAMESPACE + "minInclusive").intern();

        final static String MIN_LENGTH = (XSD_NAMESPACE + "minLength").intern();

        final static String PATTERN = (XSD_NAMESPACE + "pattern").intern();
    }
}
