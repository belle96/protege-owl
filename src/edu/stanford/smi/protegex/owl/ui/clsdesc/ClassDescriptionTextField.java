package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.code.OWLTextField;
import edu.stanford.smi.protegex.owl.ui.code.SymbolErrorDisplay;

/**
 * A OWLTextField to edit a superclass in a ClassDescriptionTable.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassDescriptionTextField extends OWLTextField {


    public ClassDescriptionTextField(OWLModel kb, SymbolErrorDisplay errorDisplay) {
        super(kb, errorDisplay);
    }


    protected void checkExpression(String text) throws Exception {
        OWLModel owlModel = getOWLModel();
        owlModel.getOWLClassDisplay().getParser().checkClass(owlModel, text);
    }
}
