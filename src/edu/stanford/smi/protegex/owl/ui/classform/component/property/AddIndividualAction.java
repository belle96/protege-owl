package edu.stanford.smi.protegex.owl.ui.classform.component.property;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import java.util.Collection;
import java.util.Collections;

/**
 * A ResourceSelectionAction to add an individual to a PropertyFormTable.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddIndividualAction extends ResourceSelectionAction {

    private PropertyFormTable table;


    public AddIndividualAction(PropertyFormTable table) {
        super("Add individual...", OWLIcons.getAddIcon(OWLIcons.RDF_INDIVIDUAL));
        this.table = table;
    }


    public Collection getSelectableResources() {
        return Collections.EMPTY_SET;
    }


    public void resourceSelected(RDFResource resource) {
        // TODO
    }
}
