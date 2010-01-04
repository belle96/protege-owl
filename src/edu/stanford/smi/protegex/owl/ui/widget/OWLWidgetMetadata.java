package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * An interface for objects that provide metadata about a widget type.
 * This is used by the OWLWidgetMapper to determine whether a certain widget
 * class shall be chosen for a certain class/property combination.
 * <p/>
 * The widget class should register an implementation of this in a static block.
 * <p/>
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLWidgetMetadata {

    /**
     * Indicates that this widget is not suitable for the given class/property combination
     */
    final static int NOT_SUITABLE = 0;

    /**
     * Indicates that this widget is suitable (but not necessarily default) for the given
     * class/property combination
     */
    final static int SUITABLE = 1;

    /**
     * Indicates that this widget is the default widget unless some other plugin declares
     * a widget with higher suitability
     */
    final static int DEFAULT = 2;


    /**
     * <P>Gets the suitability index of a widget for a given class/property pair.
     * The suitability indicates whether a widget shall be selected as default
     * widget on a form, or whether it shall be listed as one of the options in
     * the form configuration component.</P>
     * <P>The system widgets of Protege-OWL all return one of the predefined constants
     * <CODE>NOT_SUITABLE</CODE>, <CODE>SUITABLE</CODE> or <CODE>DEFAULT</CODE>.
     * Custom widget classes can also return the same constants, or they can return
     * higher values to override the current default.  For example, returning
     * <CODE>DEFAULT + 1</CODE> will make sure that the widget will have higher
     * preferences than any of the system widgets.</P>
     *
     * @param cls      the named class of the form
     * @param property the property to get the suitability for
     * @return an index >= 0
     */
    int getSuitability(RDFSNamedClass cls, RDFProperty property);
}
