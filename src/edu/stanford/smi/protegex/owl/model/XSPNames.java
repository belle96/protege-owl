package edu.stanford.smi.protegex.owl.model;

/**
 * Constants for the Protege XML Schema datatype extensions.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class XSPNames {

    public final static String URI = "http://www.owl-ontologies.com/2005/08/07/xsp.owl";

    public final static String NS = URI + "#";

    public final static String DEFAULT_PREFIX = "xsp";

    public final static int XSP_BASE = 0;
    public final static int XSP_FRACTION_DIGITS = 1;
    public final static int XSP_LENGTH = 2;
    public final static int XSP_MIN_EXCLUSIVE = 3;
    public final static int XSP_MIN_INCLUSIVE = 4;
    public final static int XSP_MAX_EXCLUSIVE = 5;
    public final static int XSP_MAX_INCLUSIVE = 6;
    public final static int XSP_MIN_LENGTH = 7;
    public final static int XSP_MAX_LENGTH = 8;
    public final static int XSP_PATTERN = 9;
    public final static int XSP_TOTAL_DIGITS = 10;
    public final static int NAMES_COUNT = 11;

    private final static String NS_SEPERATOR = ":";

    private final static String[] names = new String[]{
            "base",
            "fractionDigits",
            "length",
            "minExclusive",
            "minInclusive",
            "maxExclusive",
            "maxInclusive",
            "minLength",
            "maxLength",
            "pattern",
            "totalDigits"};


    public static String getName(OWLModel owlModel, int resourceID) {
        String name = null;
        if (resourceID >= 0 && resourceID < NAMES_COUNT) {
            return NS + names[resourceID];
        }
        return null;
    }


    public static RDFProperty getRDFProperty(OWLModel owlModel, int resourceID) {
        return owlModel.getRDFProperty(getName(owlModel, resourceID));
    }


    /**
     * @deprecated use <code>XSPNames.DEFAULT_PREFIX</code>
     */
    public final static String PREFIX = "xsp";

    /**
     * @deprecated use the helper method <code>XSPNames.get(OWLModel owlModel, int resourceID)</code>
     */
    public final static String BASE = PREFIX + ":base";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String FRACTION_DIGITS = PREFIX + ":fractionDigits";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String LENGTH = PREFIX + ":length";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String MIN_EXCLUSIVE = PREFIX + ":minExclusive";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String MIN_INCLUSIVE = PREFIX + ":minInclusive";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String MAX_EXCLUSIVE = PREFIX + ":maxExclusive";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String MAX_INCLUSIVE = PREFIX + ":maxInclusive";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String MIN_LENGTH = PREFIX + ":minLength";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String MAX_LENGTH = PREFIX + ":maxLength";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String PATTERN = PREFIX + ":pattern";

    /**
     * @deprecated use the helper method <code>XSPNames.get(int resourceID)</code>
     */
    public final static String TOTAL_DIGITS = PREFIX + ":totalDigits";
}
