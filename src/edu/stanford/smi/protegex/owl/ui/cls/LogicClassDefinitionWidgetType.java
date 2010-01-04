package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LogicClassDefinitionWidgetType implements SwitchableType {

    public String getButtonText() {
        return "Logic View";
    }


    public Class getWidgetClassType() {
        return LogicClassDefinitionWidget.class;
    }


    /**
     * Indicates that this type can display all classes.
     *
     * @param namedClass the class to check
     * @return true
     */
    public boolean isSufficientlyExpressive(RDFSNamedClass namedClass) {
        return true;
    }


    public boolean isSuitable(OWLModel owlModel) {
        return true;
    }
}
