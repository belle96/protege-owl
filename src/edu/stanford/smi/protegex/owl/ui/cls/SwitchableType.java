package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * An interface for factory objects that can register with the SwitchableClassDefinitionWidget
 * to create the available options (Logic View, Properties View, etc).
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SwitchableType {


    String getButtonText();


    Class getWidgetClassType();


    /**
     * Checks if this type would be able to display a given class.
     * This may happen if the definition of the class uses constructs that lay outside
     * of the expressivity supported by this type.
     * If not, then the container may decide to switch to another, more generic type.
     *
     * @param namedClass the named class to test
     * @return true  if this type could handle namedClass
     */
    boolean isSufficientlyExpressive(RDFSNamedClass namedClass);


    boolean isSuitable(OWLModel owlModel);
}
