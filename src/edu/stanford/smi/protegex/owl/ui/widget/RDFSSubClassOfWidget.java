package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.superclasses.RDFSSubClassOfComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSSubClassOfWidget extends AbstractPropertyValuesWidget {

    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        return new RDFSSubClassOfComponent(predicate.getOWLModel(), isReadOnlyConfiguredWidget());
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(RDFSSubClassOfWidget.class, cls, slot);
    }
}
