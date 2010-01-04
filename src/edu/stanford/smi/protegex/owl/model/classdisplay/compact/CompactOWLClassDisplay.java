package edu.stanford.smi.protegex.owl.model.classdisplay.compact;

import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.classdisplay.AbstractOWLClassDisplay;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.ParserUtils;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.model.impl.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CompactOWLClassDisplay extends AbstractOWLClassDisplay {

    private static OWLClassParser parser = new CompactOWLClassParser();


    // Overloaded to improve performance
    protected String getDisplayTextOfOWLRestriction(OWLRestriction restriction) {
        RDFProperty onProperty = restriction.getOnProperty();
        return (onProperty != null ? onProperty.getBrowserText() : "?") +
                " " + restriction.getOperator() +
                " " + getOWLRestrictionFillerText(restriction);
    }


    public String getOWLAllValuesFromSymbol() {
        return String.valueOf(DefaultOWLAllValuesFrom.OPERATOR);
    }


    public String getOWLCardinalitySymbol() {
        return String.valueOf(DefaultOWLCardinality.OPERATOR);
    }


    public String getOWLComplementOfSymbol() {
        return String.valueOf(DefaultOWLComplementClass.OPERATOR);
    }


    public String getOWLHasValueSymbol() {
        return String.valueOf(DefaultOWLHasValue.OPERATOR);
    }


    public String getOWLIntersectionOfSymbol() {
        return String.valueOf(DefaultOWLIntersectionClass.OPERATOR);
    }


    public String getOWLMaxCardinalitySymbol() {
        return String.valueOf(DefaultOWLMaxCardinality.OPERATOR);
    }


    public String getOWLMinCardinalitySymbol() {
        return String.valueOf(DefaultOWLMinCardinality.OPERATOR);
    }


    public String getOWLSomeValuesFromSymbol() {
        return String.valueOf(DefaultOWLSomeValuesFrom.OPERATOR);
    }


    public String getOWLUnionOfSymbol() {
        return String.valueOf(DefaultOWLUnionClass.OPERATOR);
    }


    public OWLClassParser getParser() {
        return parser;
    }
}
