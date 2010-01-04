package edu.stanford.smi.protegex.owl.ui.clsdesc.manchester;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class CreateSubClassSuggestion extends CreateClassSuggestion {

    public CreateSubClassSuggestion(OWLModel model,
                                    String name) {
        super(model, name);
    }


    public String getDescription() {
        return "Create " + getName() + " as subclass...";
    }


    public void createObject() {
        RDFSNamedClass cls = ProtegeUI.getSelectionDialogFactory().selectClass(null, getModel(),
                "Specify a superclass"); // TODO: replace null
        if (cls != null) {
            OWLNamedClass newCls = getModel().createOWLNamedClass(getName());
            newCls.addSuperclass(cls);
            if (cls.equals(getModel().getOWLThingClass()) == false) {
                newCls.removeSuperclass(getModel().getOWLThingClass());
            }
        }
    }
}

