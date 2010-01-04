package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.singleliteral.SingleLiteralAreaComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SingleLiteralAreaWidget extends AbstractPropertyValuesWidget {


    public SingleLiteralAreaWidget() {
        setPreferredColumns(2);
        setPreferredRows(2);
    }


    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        return new SingleLiteralAreaComponent(predicate, getLabel(), isReadOnlyConfiguredWidget());
    }


    /**
     * @param cls
     * @param slot
     * @param facet
     * @deprecated
     */
    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(SingleLiteralAreaWidget.class, cls, slot);
    }
}
