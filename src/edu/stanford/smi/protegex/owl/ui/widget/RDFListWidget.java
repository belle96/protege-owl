package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.components.rdflist.RDFListComponent;

/**
 * The default widget for properties of type rdf:List (or a subclass of rdf:List).
 * It looks similar to a normal instance list widget but operates on a linked RDF list.
 * <p/>
 * It is possible to subclass this widget class to operate on simulated lists with first
 * and rest properties.  To do so, overload the corresponding methods of the RDFListComponent class.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFListWidget extends AbstractPropertyValuesWidget {


    public RDFListWidget() {
        setPreferredColumns(2);
        setPreferredRows(2);
    }


    protected PropertyValuesComponent createComponent(RDFProperty predicate) {
        return new RDFListComponent(predicate, getLabel(), isReadOnlyConfiguredWidget());
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return OWLWidgetMapper.isSuitable(RDFListWidget.class, cls, slot);
    }
}
