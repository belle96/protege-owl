package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertiesClassDefinitionWidgetType implements SwitchableType {

    public String getButtonText() {
        return "Properties View";
    }


    public Class getWidgetClassType() {
        return PropertiesClassDefinitionWidget.class;
    }


    public boolean isSufficientlyExpressive(RDFSNamedClass namedClass) {
        return true;  // TODO
    }


    public boolean isSuitable(OWLModel owlModel) {
        return true;
    }
}
