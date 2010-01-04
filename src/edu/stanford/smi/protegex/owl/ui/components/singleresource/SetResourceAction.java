package edu.stanford.smi.protegex.owl.ui.components.singleresource;

import java.awt.Component;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Action;

import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.components.PropertyValuesComponent;
import edu.stanford.smi.protegex.owl.ui.dialogs.DefaultSelectionDialogFactory;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SetResourceAction extends ResourceSelectionAction {

    private PropertyValuesComponent component;


    public SetResourceAction(PropertyValuesComponent component) {
        super("Select existing resource...", OWLIcons.getAddIcon(OWLIcons.RDF_INDIVIDUAL));
        this.component = component;
    }


    public Collection getEnumeratedValues(Collection unionRangeClasses) {
        Collection results = new HashSet();
        for (Iterator it = unionRangeClasses.iterator(); it.hasNext();) {
            RDFSClass rangeClass = (RDFSClass) it.next();
            if (rangeClass instanceof OWLEnumeratedClass) {
                results.addAll(((OWLEnumeratedClass) rangeClass).getOneOf());
            }
        }
        return results;
    }


    @Override
	public Collection getSelectableResources() {
        RDFResource subject = component.getSubject();
        RDFProperty predicate = component.getPredicate();
        Set clses = getUnionRangeClasses(subject, predicate, true);
        Collection enums = getEnumeratedValues(clses);
        if (enums.size() > 0) {
            return enums;
        }
        else if (clses.contains(subject.getOWLModel().getOWLThingClass())) {
            return subject.getOWLModel().getUserDefinedRDFIndividuals(true);
        }
        else {
            Set instances = new HashSet();
            for (Iterator it = clses.iterator(); it.hasNext();) {
                RDFSClass cls = (RDFSClass) it.next();
                instances.addAll(cls.getInstances(true));
            }
            return instances;
        }
    }


    private Set getUnionRangeClasses(RDFResource subject, RDFProperty predicate, boolean includingEnumeratedClasses) {
        OWLModel owlModel = subject.getOWLModel();
        Set clses = new HashSet();
        for (Iterator it = subject.getRDFTypes().iterator(); it.hasNext();) {
            RDFSClass type = (RDFSClass) it.next();
            if (type instanceof RDFSNamedClass) {
                RDFSNamedClass namedType = (RDFSNamedClass) type;
                Collection urcs = namedType.getUnionRangeClasses(predicate);
                for (Iterator us = urcs.iterator(); us.hasNext();) {
                    RDFSClass rangeClass = (RDFSClass) us.next();
                    if (rangeClass instanceof RDFSNamedClass ||
                            (includingEnumeratedClasses && rangeClass instanceof OWLEnumeratedClass)) {
                        clses.add(rangeClass);
                    }
                }
            }
        }
        if (clses.isEmpty()) {
            clses.add(owlModel.getOWLThingClass());
        }
        return clses;
    }


    @Override
	public RDFResource pickResource() {
        RDFResource subject = component.getSubject();
        RDFProperty predicate = component.getPredicate();
        OWLModel owlModel = predicate.getOWLModel();
        Collection allClasses = getUnionRangeClasses(subject, predicate, true);
        Collection enums = getEnumeratedValues(allClasses);
        if (enums.isEmpty()) {
            Collection clses = getUnionRangeClasses(subject, predicate, false);
            if (OWLUI.isExternalResourcesSupported(owlModel)) {
                owlModel.getRDFUntypedResourcesClass().setVisible(true);
                clses.add(owlModel.getRDFUntypedResourcesClass());
            }
            //RDFResource resource = ProtegeUI.getSelectionDialogFactory().selectResourceByType((Component) component, owlModel, clses);
            RDFResource resource = new DefaultSelectionDialogFactory().selectResourceWithBrowserTextByType((Component) component, owlModel, clses, "Select Resource");
            owlModel.getRDFUntypedResourcesClass().setVisible(false);
            return resource;
        }
        else {
            return ProtegeUI.getSelectionDialogFactory().selectResourceFromCollection((Component) component, owlModel, enums, (String) getValue(Action.NAME));
        }
    }


    public void resourceSelected(RDFResource resource) {
        component.getSubject().setPropertyValue(component.getPredicate(), resource);
    }
}
