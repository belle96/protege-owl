package edu.stanford.smi.protegex.owl.swrl.ui.icons;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * A singleton that provides access to the SWRL specific icons.
 *
 * @author Martin O'Connor  <moconnor@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLIcons {

    public final static String IMP = "SWRLImp";

    public static final String VARIABLE = "SWRLVariable";


    public static ImageIcon getImpIcon() {
        return getImageIcon("SWRLImp");
    } // getImpIcon


    public static ImageIcon getOpenBrackets() {
        return getImageIcon("OpenBrackets");
    } // getOpenBrackets


    public static ImageIcon getCloseBrackets() {
        return getImageIcon("CloseBrackets");
    } // getCloseBrackets


    public static ImageIcon getOpenParenthesis() {
        return getImageIcon("OpenParenthesis");
    } // getOpenParenthesis


    public static ImageIcon getCloseParenthesis() {
        return getImageIcon("CloseParenthesis");
    } // getCloseParenthesis


    public static Icon getAndIcon() {
        return getImageIcon("Ugly");
    } // getAndIcon


    public static Icon getInsertImpIcon() {
        return getImageIcon("InsertImp");
    } // getInsertImpIcon


    public static Icon getVariableIcon() {
        return getImageIcon(VARIABLE);
    } // getVariableIcon


    public static Icon getBuiltinIcon() {
        return getImageIcon("SWRLBuiltin");
    } // getBuiltinIcon


    public static Icon getSameAsIcon() {
        return getImageIcon("SWRLSameAs");
    } // getSameAsIcon


    public static Icon getDifferentFromIcon() {
        return getImageIcon("SWRLDifferentFrom");
    } // getDifferentFromIcon


    public static ImageIcon getImageIcon(String name) {
        return OWLIcons.getImageIcon(name, SWRLIcons.class);
    }


    public static Icon getInsertAndIcon() {
        return getImageIcon("InsertAnd");
    }


    public static Icon getImpsIcon() {
        return getImageIcon("SWRLImps");
    }
} // SWRLIcons
