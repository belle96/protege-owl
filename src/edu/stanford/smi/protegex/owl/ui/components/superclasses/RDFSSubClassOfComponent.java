package edu.stanford.smi.protegex.owl.ui.components.superclasses;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.components.multiresource.MultiResourceComponent;

import javax.swing.*;

/**
 * A MultiResourceComponent for editing values of the rdfs:subClassOf property.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSSubClassOfComponent extends MultiResourceComponent {

    public RDFSSubClassOfComponent(OWLModel owlModel) {
        super(owlModel.getRDFSSubClassOfProperty(), false);
    }

    public RDFSSubClassOfComponent(OWLModel owlModel, boolean isReadOnly) {
        super(owlModel.getRDFSSubClassOfProperty(), false, null, isReadOnly );
    }
    

    public void addObject(RDFResource resource, boolean symmetric) {
        RDFSNamedClass cls = (RDFSNamedClass) getSubject();
        if (!cls.equals(resource) && resource instanceof RDFSNamedClass) {
            cls.addSuperclass((RDFSNamedClass) resource);
        }
    }


    protected Action createCreateAction() {
        return null;
    }


    protected void handleRemove() {
        OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(this);
        Object[] sels = getSelectedObjects();
        RDFSNamedClass cls = (RDFSNamedClass) getSubject();
        if (cls.getSuperclasses(false).size() - sels.length <= 0) {
            ProtegeUI.getModalDialogFactory().showErrorMessageDialog(getOWLModel(),
                    "Each class must have at least one superclass.");
        }
        else {
            for (int i = 0; i < sels.length; i++) {
                RDFSClass superclass = (RDFSClass) sels[i];
                cls.removeSuperclass(superclass);
            }
            if (tab != null) {
                tab.ensureClsSelected(cls, -1);
            }
        }
    }
}
