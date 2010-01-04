package edu.stanford.smi.protegex.owl.model.classdisplay.dl;

import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classdisplay.OWLClassDisplay;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.dl.DLSyntaxClassParser;
import edu.stanford.smi.protegex.owl.model.impl.*;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 25, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLSyntaxClassDisplay implements OWLClassDisplay {


    private DLSyntaxBrowserTextGenerator gen;

    private DLSymbolGenerator symbolGen;

    public DLSyntaxClassDisplay() {
        gen = new DLSyntaxBrowserTextGenerator();
        symbolGen = new DLSymbolGenerator();
    }

    public String getDisplayText(RDFSClass cls) {
        gen.reset();
        cls.accept(gen);
        return gen.getBrowserText();
    }

    public String getSymbol(OWLAnonymousClass cls) {
        return symbolGen.getSymbol();
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
        return String.valueOf(DefaultOWLSomeValuesFrom.OPERATOR);
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
        return new DLSyntaxClassParser();
    }
}
