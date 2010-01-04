package edu.stanford.smi.protegex.owl.ui.restrictions;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLRestriction;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.code.OWLTextField;
import edu.stanford.smi.protegex.owl.ui.code.SymbolErrorDisplay;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

/**
 * A OWLTextField to edit the filler in a RestrictionsTable.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
class FillerTextField extends OWLTextField {

    private RDFProperty onProperty;

    private RDFProperty restrictionProperty;


    FillerTextField(OWLModel kb, SymbolErrorDisplay errorDisplay) {
        super(kb, errorDisplay);
    }


    protected void checkExpression(String text) throws Throwable {
        AbstractOWLRestriction.checkExpression(text, onProperty, restrictionProperty);
    }


    public void setOnProperty(RDFProperty onProperty) {
        this.onProperty = onProperty;
    }


    public void setRestrictionProperty(RDFProperty restrictionProperty) {
        this.restrictionProperty = restrictionProperty;
    }


    protected void stopEditing() {
        ProtegeUI.getModalDialogFactory().attemptDialogClose(ModalDialogFactory.OPTION_OK);
    }
}
