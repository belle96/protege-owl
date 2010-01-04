package edu.stanford.smi.protegex.owl.ui.conditions;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;

/**
 * A SlotWidget used for describing logical class characteristics, i.e.
 * superclasses and equivalent classes.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InferredConditionsWidget extends AbstractConditionsWidget {

    public void initialize() {
        AbstractOWLModel owlModel = (AbstractOWLModel) getKnowledgeBase();
        Slot superclassesSlot = owlModel.getProtegeInferredSuperclassesProperty();
        initialize("Inferred Conditions", superclassesSlot);
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return cls.getKnowledgeBase() instanceof OWLModel &&
                slot.getName().equals(ProtegeNames.Slot.INFERRED_SUPERCLASSES);
    }
}
