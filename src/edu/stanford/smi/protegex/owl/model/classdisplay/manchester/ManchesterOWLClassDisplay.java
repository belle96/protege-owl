package edu.stanford.smi.protegex.owl.model.classdisplay.manchester;

import edu.stanford.smi.protegex.owl.model.classdisplay.AbstractOWLClassDisplay;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.manchester.ManchesterOWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.manchester.ManchesterOWLParserUtil;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ManchesterOWLClassDisplay extends AbstractOWLClassDisplay {


    private static ManchesterOWLClassParser parser;


    public ManchesterOWLClassDisplay() {
        ManchesterOWLParserUtil.setLowerCase(true);
    }


    public String getOWLAllValuesFromSymbol() {
        return ManchesterOWLParserUtil.getAllKeyword();
    }


    public String getOWLCardinalitySymbol() {
        return ManchesterOWLParserUtil.getExactKeyword();
    }


    public String getOWLComplementOfSymbol() {
        return ManchesterOWLParserUtil.getNotKeyword();
    }


    public String getOWLHasValueSymbol() {
        return ManchesterOWLParserUtil.getHasKeyword();
    }


    public String getOWLIntersectionOfSymbol() {
        return ManchesterOWLParserUtil.getAndKeyword();
    }


    public String getOWLMaxCardinalitySymbol() {
        return ManchesterOWLParserUtil.getMaxKeyword();
    }


    public String getOWLMinCardinalitySymbol() {
        return ManchesterOWLParserUtil.getMinKeyword();
    }


    public String getOWLSomeValuesFromSymbol() {
        return ManchesterOWLParserUtil.getSomeKeyword();
    }


    public String getOWLUnionOfSymbol() {
        return ManchesterOWLParserUtil.getOrKeyword();
    }


    public OWLClassParser getParser() {
        if (parser == null) {
            parser = new ManchesterOWLClassParser();
        }
        return parser;
    }


    public static String getUIDescription() {
        return "Manchester OWL Syntax";
    }
}
