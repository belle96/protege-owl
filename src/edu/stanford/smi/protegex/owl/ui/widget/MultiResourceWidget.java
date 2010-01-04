package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.multiresource.MultiResourceComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class MultiResourceWidget extends AbstractPropertyValuesWidget {


    public MultiResourceWidget() {
        setPreferredColumns(2);
        setPreferredRows(2);
    }


    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        boolean symmetric = false;
        if (predicate instanceof OWLObjectProperty) {
            symmetric = ((OWLObjectProperty) predicate).isSymmetric();
        }
        else {
            OWLModel owlModel = predicate.getOWLModel();
            if (owlModel.getOWLSameAsProperty().equals(predicate) ||
                    owlModel.getOWLDifferentFromProperty().equals(predicate)) {
                symmetric = true;
            }
        }
        return new MultiResourceComponent(predicate, symmetric, getLabel(), isReadOnlyConfiguredWidget());
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(MultiResourceWidget.class, cls, slot);
    }
}
