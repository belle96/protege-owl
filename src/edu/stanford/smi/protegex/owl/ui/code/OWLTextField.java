package edu.stanford.smi.protegex.owl.ui.code;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * A SymbolTextField with special support for editing OWL expressions in
 * the Protege compact syntax.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class OWLTextField extends SymbolTextField {

    public OWLTextField(OWLModel owlModel, SymbolErrorDisplay errorDisplay) {
        super(owlModel, errorDisplay, new OWLResourceNameMatcher(), new OWLSyntaxConverter(owlModel));
        OWLTextFormatter.initKeymap(this);
    }


    /**
     * Used for error checking during input.
     *
     * @param text
     * @throws Throwable ParseExceptions etc.
     */
    protected abstract void checkExpression(String text) throws Throwable;


    protected void checkUniCodeExpression(String uniCodeText) throws Throwable {
        checkExpression(uniCodeText);
    }
}
