package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.datarangefield.DataRangeFieldComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DataRangeFieldWidget extends AbstractPropertyValuesWidget {


    public DataRangeFieldWidget() {
        setPreferredColumns(2);
        setPreferredRows(1);        
    }


    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        return new DataRangeFieldComponent(predicate, getLabel(), isReadOnlyConfiguredWidget());
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(DataRangeFieldWidget.class, cls, slot);
    }
}
