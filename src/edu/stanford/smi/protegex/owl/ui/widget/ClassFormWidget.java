package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.classform.form.ClassForm;

import java.awt.*;

/**
 * A SlotWidget wrapping a ClassForm so that it can be used inside of a
 * traditional Protege form.
 *
 * @author Matthew Horridge  <matthew.horridge@cs.man.ac.uk>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassFormWidget extends AbstractPropertyWidget {

    private ClassForm classForm;


    public void initialize() {
        classForm = new ClassForm();
        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, classForm);
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        if (cls.getKnowledgeBase() instanceof OWLModel && cls instanceof RDFSNamedClass) {
            OWLModel owlModel = (OWLModel) cls.getKnowledgeBase();
            RDFSNamedClass namedClass = (RDFSNamedClass) cls;
            return owlModel.getOWLNamedClassClass().equals(cls) ||
                    namedClass.getSuperclasses(true).contains(owlModel.getOWLNamedClassClass());
        }
        return false;
    }


    public void setInstance(Instance newInstance) {
        super.setInstance(newInstance);
        if (newInstance instanceof OWLNamedClass) {
            classForm.setNamedClass((OWLNamedClass) newInstance);
        }
        else {
            classForm.setNamedClass(null);
        }
    }
}

