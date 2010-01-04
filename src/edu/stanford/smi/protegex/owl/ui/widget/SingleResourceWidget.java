package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.singleresource.SingleResourceComponent;

/**
 * A widget that allows users to create or select an RDFResource
 * as the (single) value of a property.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SingleResourceWidget extends AbstractPropertyValuesWidget {

    public SingleResourceWidget() {
        setPreferredColumns(2);
        setPreferredRows(1);
    }


    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        return new SingleResourceComponent(predicate, getLabel(), isReadOnlyConfiguredWidget());
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(SingleResourceWidget.class, cls, slot);
    }
}
