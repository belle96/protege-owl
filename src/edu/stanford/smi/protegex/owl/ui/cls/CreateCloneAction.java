package edu.stanford.smi.protegex.owl.ui.cls;

import java.awt.Component;
import java.awt.event.ActionEvent;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.util.CloneFactory;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateCloneAction extends ResourceAction {

    public CreateCloneAction() {
        super("Create clone", Icons.getBlankIcon(), AddSubclassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        OWLNamedClass namedCls = (OWLNamedClass) getResource();
        OWLNamedClass c = null;
        OWLModel owlModel = namedCls.getOWLModel();
        //TT: This is may be dangerous.. Needs some testing
        String cloneName = CloneFactory.getNextAvailableCloneName(namedCls);
        try {
            owlModel.beginTransaction("Create " + NamespaceUtil.getLocalName(cloneName) + 
            		" as clone of " + namedCls.getBrowserText(), cloneName);
            c = createClone(namedCls, cloneName);
            owlModel.commitTransaction();
        }
        catch (Exception ex) {
        	owlModel.rollbackTransaction();        
            OWLUI.handleError(owlModel, ex);
        }
        ((ClassTreePanel) getComponent()).setSelectedClass(c);
    }

    public static OWLNamedClass createClone(OWLNamedClass oldCls) {
        return CloneFactory.cloneOWLNamedClass(oldCls);
    }
    
    public static OWLNamedClass createClone(OWLNamedClass oldCls, String cloneName) {
        return CloneFactory.cloneOWLNamedClass(oldCls, cloneName);
    }

//    public static OWLNamedClass createClone(OWLNamedClass oldCls) {
//        OWLModel owlModel = oldCls.getOWLModel();
//        String newName = null;
//        int i = 2;
//        do {
//            newName = oldCls.getName() + "_" + i;
//            i++;
//        }
//        while (owlModel.getRDFResource(newName) != null);
//        OWLNamedClass newCls = owlModel.createOWLNamedClass(newName);
//        boolean hasThing = false;
//        for (Iterator it = oldCls.getSuperclasses(false).iterator(); it.hasNext();) {
//            Cls superCls = (Cls) it.next();
//            if (superCls instanceof RDFSClass) {
//                RDFSClass cloneClass = ((RDFSClass) superCls).createClone();
//                newCls.addSuperclass(cloneClass);
//                if (newCls.equals(owlModel.getOWLThingClass())) {
//                    hasThing = true;
//                }
//                if (superCls.getDirectSuperclasses().contains(oldCls)) {
//                    cloneClass.addSuperclass(newCls);
//                }
//            }
//        }
//        if (!hasThing) {
//            newCls.removeSuperclass(owlModel.getOWLThingClass());
//        }
//        Iterator infs = oldCls.getInferredSuperclasses().iterator();
//        while (infs.hasNext()) {
//            RDFSClass inferredSuperclass = (RDFSClass) infs.next();
//            newCls.addInferredSuperclass(inferredSuperclass);
//        }
//        for (Iterator it = oldCls.getDisjointClasses().iterator(); it.hasNext();) {
//            RDFSClass disjointClass = (RDFSClass) it.next();
//            final RDFSClass c = disjointClass.createClone();
//            if (!newCls.getDisjointClasses().contains(c)) {
//                newCls.addDisjointClass(c);
//            }
//        }
//        for (Iterator it = oldCls.getUnionDomainProperties().iterator(); it.hasNext();) {
//            RDFProperty property = (RDFProperty) it.next();
//            property.addUnionDomainClass(newCls);
//        }
//        return newCls;
//    }


    @Override
	public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof ClassTreePanel &&
               resource instanceof OWLNamedClass &&
               !(resource.equals(resource.getOWLModel().getOWLThingClass()));
    }
}
