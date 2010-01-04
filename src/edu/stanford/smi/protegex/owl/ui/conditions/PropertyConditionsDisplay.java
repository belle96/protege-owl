package edu.stanford.smi.protegex.owl.ui.conditions;

import edu.stanford.smi.protegex.owl.model.OWLProperty;

/**
 * An interface for components that can display a given property.
 * This is used by the OWLTemplateSlotsWidget to display the selected property.
 * This is implemented to select all rows where the slot is mentioned.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface PropertyConditionsDisplay {

    void displayRowsWithProperty(OWLProperty property);
}
