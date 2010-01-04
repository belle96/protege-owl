package edu.stanford.smi.protegex.owl.ui.code;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNAryLogicalClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * A SymbolTextArea with special support for editing OWL expressions in
 * the Protege compact syntax.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class OWLTextArea extends SymbolTextArea {

    public OWLTextArea(OWLModel owlModel, SymbolErrorDisplay errorDisplay) {
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


    public void setText(RDFSClass aClass) {
        String str = null;
        if (aClass instanceof OWLNAryLogicalClass) {
            str = getIndentedClsString((OWLNAryLogicalClass) aClass, "");
        }
        else {
            str = aClass.getBrowserText();
        }
        setText(str);
    }
}
