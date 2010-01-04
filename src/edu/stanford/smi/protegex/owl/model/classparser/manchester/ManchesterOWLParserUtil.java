package edu.stanford.smi.protegex.owl.model.classparser.manchester;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 5, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ManchesterOWLParserUtil {

    private static boolean lowerCase = true;


    public static boolean isLowerCase() {
        return lowerCase;
    }


    public static void setLowerCase(boolean lowerCase) {
        ManchesterOWLParserUtil.lowerCase = lowerCase;
    }


    public static String getAndKeyword() {
        return getKeyword("and");
    }


    public static String getOrKeyword() {
        return getKeyword("or");
    }


    public static String getNotKeyword() {
        return getKeyword("not");
    }


    public static String getSomeKeyword() {
        return getKeyword("some");
    }


    public static String getAllKeyword() {
        return getKeyword("only");
    }


    public static String getHasKeyword() {
        return getKeyword("has");
    }


    public static String getMinKeyword() {
        return getKeyword("min");
    }


    public static String getExactKeyword() {
        return getKeyword("exactly");
    }


    public static String getMaxKeyword() {
        return getKeyword("max");
    }


    private static String getKeyword(String s) {
        if (lowerCase == false) {
            s = s.toUpperCase();
        }
        return s;
    }
}

