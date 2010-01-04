package edu.stanford.smi.protegex.owl.ui.editors;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.awt.*;

/**
 * An object that can be used to customize the appearance and behavior of (datatype) values
 * in visual editors.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface PropertyValueEditor {


    /**
     * Checks whether this is able to edit values for a certain predicate.
     *
     * @param subject
     * @param predicate
     * @param value
     * @return true  if this can edit such values
     */
    public boolean canEdit(RDFResource subject, RDFProperty predicate, Object value);


    /**
     * Creates a default value for a given predicate at a given subject.
     * The hosting widget should call this method on each of its registered editors
     * to see whether any of them defines a non-null default value.
     *
     * @param subject   the RDFResource to create a default value for
     * @param predicate the annotation predicate
     * @return the default value or null if this does not define a default for this predicate
     */
    public Object createDefaultValue(RDFResource subject, RDFProperty predicate);


    /**
     * Edits a given value for a given resource/predicate pair.  For example, after the user
     * has double-clicked on the value, the host widget could iterate on all editors to see
     * whether any of them is ready to edit this value (returning true).
     *
     * @param parent
     * @param subject
     * @param predicate
     * @param value
     * @return the new value (!= null) if this has handled editing for the value
     */
    public Object editValue(Component parent, RDFResource subject, RDFProperty predicate, Object value);


    /**
     * Checks whether this is the only way to edit values for a certain predicate.
     * This is used to force editing of value types like xsd:date, where simple string
     * input fields would not be sufficient.
     *
     * @param subject
     * @param predicate
     * @param value
     * @return true  if this can edit such values
     */
    public boolean mustEdit(RDFResource subject, RDFProperty predicate, Object value);
}
