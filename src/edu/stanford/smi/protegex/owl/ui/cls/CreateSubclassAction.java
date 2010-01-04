package edu.stanford.smi.protegex.owl.ui.cls;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * A ResourceAction for named classes, to create a subclass of the currently selected class.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateSubclassAction extends ResourceAction {

    public final static String TEXT = "Create subclass";


    public CreateSubclassAction() {
        super(TEXT, OWLIcons.getCreateIcon(OWLIcons.SUB_CLASS, 4));
    }


    public void actionPerformed(ActionEvent e) {
        ClassTreePanel classTreePanel = (ClassTreePanel) getComponent();
        RDFSNamedClass sibling = (RDFSNamedClass) getResource();
        performAction(Collections.singleton(sibling), classTreePanel);
    }


    @Override
	public int getPriority() {
        return 1;  // So that it shows up before "create sibling class"
    }


    @Override
	public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof ClassTreePanel &&
                resource instanceof RDFSNamedClass;
    }


    public static void performAction(Collection superclasses, ClassTreePanel classTreePanel) {
    	RDFSNamedClass cls = null;
        RDFSNamedClass superclass = (RDFSNamedClass) superclasses.iterator().next();
        OWLModel owlModel = superclass.getOWLModel();
        RDFSClass type = superclass.getProtegeType();
        if (superclasses.contains(owlModel.getOWLThingClass())) {
            if (!ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.Create_OWLClass) &&
                    ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.CreateRDFSClass)) {
                type = owlModel.getRDFSNamedClassClass();
            }
        }
        
        String name = owlModel.createNewResourceName(AbstractOWLModel.DEFAULT_CLASS_NAME);
        try {
            owlModel.beginTransaction("Create class " + NamespaceUtil.getLocalName(name) + " as subclass of " + (superclass == null ? "(unknown)" : superclass.getBrowserText()), name);            
            cls = owlModel.createRDFSNamedClass(name, superclasses, type);
            if (cls instanceof OWLNamedClass) {
                for (Iterator it = superclasses.iterator(); it.hasNext();) {
                    RDFSNamedClass s = (RDFSNamedClass) it.next();
                    ((OWLNamedClass) cls).addInferredSuperclass(s);
                }
            }
            owlModel.commitTransaction();
		} catch (Exception e) {
			owlModel.rollbackTransaction();
			OWLUI.handleError(owlModel, e);
		}
        
        if (cls != null) 
        	classTreePanel.setSelectedClass(cls);
    }
}
