package edu.stanford.smi.protegex.owl.ui.classform.form;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;

/**
 * A ResourceSelectionAction allowing users to add a property component to a ClassForm.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddPropertyFormComponentAction extends ResourceSelectionAction {

    private ClassForm classForm;

    public static final Icon ICON = OWLIcons.getImageIcon(OWLIcons.ADD_PROPERTY_TO_CLASS_FORM); //AddIcon(OWLIcons.OWL_SOME_VALUES_FROM);


    public AddPropertyFormComponentAction(ClassForm classForm) {
        super("Add restrictions on property...", ICON);
        this.classForm = classForm;
    }


    public Collection getSelectableResources() {
        return Collections.EMPTY_LIST;
    }


    public void resourceSelected(RDFResource resource) {
        // TODO
    }
}
