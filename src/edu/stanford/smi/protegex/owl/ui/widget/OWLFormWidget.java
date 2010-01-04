package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.widget.AbstractSlotWidget;
import edu.stanford.smi.protege.widget.FormWidget;
import edu.stanford.smi.protege.widget.WidgetMapper;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLFormWidget extends FormWidget {

    public final static String DISABLED_BY_USER = "DisabledByUser";


    protected Cls getCustomizedParent(Cls cls) {
        return getCustomizedParent(cls, new HashSet());
    }


    protected Cls getCustomizedParent(Cls cls, Set reached) {
        reached.add(cls);
        Project project = cls.getProject();
        Cls customizedParent = null;
        Iterator i = cls.getDirectSuperclasses().iterator();
        while (i.hasNext() && customizedParent == null) {
            Cls parent = (Cls) i.next();
            if (!(parent instanceof OWLAnonymousClass) && !reached.contains(parent)) {
                if (project.hasCustomizedDescriptor(parent)) {
                    customizedParent = parent;
                    break;
                }
                customizedParent = getCustomizedParent(parent, reached);
                break;
            }
        }
        return customizedParent;
    }


    protected Collection getClsSlots() {
        Cls cls = getCls();
        if (cls instanceof RDFSNamedClass) {
            RDFSNamedClass namedClass = ((RDFSNamedClass) cls);
            OWLModel owlModel = namedClass.getOWLModel();
            Set properties = new HashSet();
            try {
            	properties = namedClass.getAssociatedProperties();
            } catch (Exception e) {
            	Log.getLogger().warning("Problem at getting class form slots for " + cls);
            }
            properties = new HashSet(owlModel.getVisibleResources(properties.iterator()));
            Iterator i = getPropertyList().getNames().iterator();
            while (i.hasNext()) {
                String name = (String) i.next();
                if (name != null) {
                    Slot property = (Slot) getKnowledgeBase().getSlot(name);
                    if (property != null) {
                        properties.add(property);
                    }
                }
            }
            return properties;
        }
        else {
            return super.getClsSlots();
        }
    }


    protected boolean isSuitable(WidgetDescriptor d, Cls cls, Slot slot) {
    	
        if (slot instanceof RDFProperty && cls instanceof RDFSNamedClass) {
            String className = d.getWidgetClassName();
            if (className != null && OWLWidgetMapper.isIncompatibleWidgetName(className)) {
                return false;
            }
            //TT: Why is this code here? I commented it out
            /*
            Boolean disabledByUser = d.getPropertyList().getBoolean(DISABLED_BY_USER);
            if (className == null && !Boolean.TRUE.equals(disabledByUser)) {
                RDFProperty owlProperty = (RDFProperty) slot;
                RDFSNamedClass namedClass = (RDFSNamedClass) cls;
                if (!owlProperty.isDomainDefined()) {
                    return !OWLWidgetUtil.isRestrictedProperty(namedClass, owlProperty);
                }
            }
            */
        }
        
        return super.isSuitable(d, cls, slot);
    }


    protected void onTemplateSlotAdded(Cls cls, Slot slot) {
        WidgetMapper widgetMapper = cls.getProject().getWidgetMapper();
        String name = widgetMapper.getDefaultWidgetClassName(cls, slot, null);
        if (name == null) {
            return; // Suppress
        }
        super.onTemplateSlotAdded(cls, slot);
    }


    protected void onTemplateFacetValueChanged(Cls cls, Slot slot, Facet facet) {
        if (hasWidgetDescriptor(slot)) {
            super.onTemplateFacetValueChanged(cls, slot, facet);
        }
    }


    protected void onTemplateSlotRemoved(Cls cls, Slot slot) {
        if (hasWidgetDescriptor(slot)) {
            super.onTemplateSlotRemoved(cls, slot);
        }
    }


    public void reload() {
        if (!OWLNames.Cls.ONTOLOGY.equals(getCls().getName())) {
            super.reload();
        }
    }


    public void replaceSelectedWidget(String newWidgetClassName) {
        Collection sels = getSelection();
        super.replaceSelectedWidget(newWidgetClassName);
        if (sels.size() == 1) {
            AbstractSlotWidget slotWidget = (AbstractSlotWidget) sels.iterator().next();
            slotWidget.getDescriptor().getPropertyList().setBoolean(DISABLED_BY_USER, newWidgetClassName == null);
        }
    }
}
