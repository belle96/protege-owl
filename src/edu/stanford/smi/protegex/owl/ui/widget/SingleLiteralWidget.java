package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.singleliteral.SingleLiteralComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SingleLiteralWidget extends AbstractPropertyValuesWidget {


    public SingleLiteralWidget() {
        setPreferredColumns(2);
        setPreferredRows(1);
    }


    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        return new SingleLiteralComponent(predicate, getLabel(), isReadOnlyConfiguredWidget());
    }


    /**
     * @param cls
     * @param slot
     * @param facet
     * @deprecated
     */
    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(SingleLiteralWidget.class, cls, slot);
    }
}
