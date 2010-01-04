package edu.stanford.smi.protegex.owl.ui.metadata;

import edu.stanford.smi.protegex.owl.ui.editors.PropertyValueEditor;

/**
 * An object that can be used to customize the appearance and behavior of datatype values
 * in the AnnotationsWidget.  Implementations of this interface can be registered with
 * the AnnotationsWidget through a ProjectPlugin, calling
 * (@link AnnotationsWidget#addPlugin}.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface AnnotationsWidgetPlugin extends PropertyValueEditor {

}
