package edu.stanford.smi.protegex.owl.model.classdisplay;

import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;

/**
 * An interface for objects that can display (and parse) class expressions.
 * Each OWLModel uses one instance of this interface in places such as the conditions
 * widget and the expression editor.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLClassDisplay {


    /**
     * Gets the display text for a given class expression.
     * This will be used in the <CODE>getBrowserText()</CODE> call of the class.
     *
     * @param cls the class to get the text for
     * @return the display text (not null)
     */
    String getDisplayText(RDFSClass cls);


    /**
     * Gets the keyword for a class of a given type.  Depending on the provided argument
     * this should fork into the corresponding helper methods such as <CODE>getOWLHasValueSymbol()</CODE>
     * if the argument is an OWLHasValue restriction.
     *
     * @param cls the class to get the key for
     * @return the key  (undefined for OWLEnumeratedClasses)
     */
    String getSymbol(OWLAnonymousClass cls);


    /**
     * Gets the keyword used for owl:allValuesFrom restrictions in this rendering.
     * Examples are the reverse A or "only".
     *
     * @return the key for owl:allValuesFrom
     */
    String getOWLAllValuesFromSymbol();


    String getOWLCardinalitySymbol();


    String getOWLComplementOfSymbol();


    String getOWLHasValueSymbol();


    String getOWLIntersectionOfSymbol();


    String getOWLMaxCardinalitySymbol();


    String getOWLMinCardinalitySymbol();


    String getOWLSomeValuesFromSymbol();


    String getOWLUnionOfSymbol();


    /**
     * Gets the associated parser that allows users to enter class expressions in the
     * defined rendering.
     *
     * @return the OWLClassParser
     */
    OWLClassParser getParser();
}
