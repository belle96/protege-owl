package edu.stanford.smi.protegex.owl.ui.properties.domain;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.event.PropertyAdapter;
import edu.stanford.smi.protegex.owl.model.event.PropertyListener;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A simple TableModel to list the domain of an RDFProperty.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLDomainTableModel extends AbstractTableModel implements Disposable {

    private int COL_CLASS = 0;

    private List clses = new ArrayList();

    private RDFProperty property;

    private PropertyListener propertyListener = new PropertyAdapter() {
        public void unionDomainClassAdded(RDFProperty property, RDFSClass rdfsClass) {
            refill();
        }


        // Overload this to do something useful
        public void unionDomainClassRemoved(RDFProperty property, RDFSClass rdfsClass) {
            refill();
        }
    };


    public OWLDomainTableModel(RDFProperty property) {
        this.property = property;
        if (property != null) {
            fill();
            property.addPropertyListener(propertyListener);
        }
    }


    private boolean containsSubclassOf(Cls cls) {
        for (Iterator it = clses.iterator(); it.hasNext();) {
            Cls cls1 = (Cls) it.next();
            if (cls1.hasSuperclass(cls)) {
                return true;
            }
        }
        return false;
    }


    public void dispose() {
        if (property != null) {
            property.removePropertyListener(propertyListener);
        }
    }


    private void fill() {
    	OWLModel owlModel = property.getOWLModel();
        final Slot domainSlot = owlModel.getSystemFrames().getDirectDomainSlot();
        final Collection localDomain = ((Slot) property).getDirectOwnSlotValues(domainSlot);
        Collection superProperties = property.getSuperproperties(true);
        if (localDomain.isEmpty() || 
        		(localDomain.size() == 1 && !superProperties.isEmpty() 
        				&& localDomain.contains(owlModel.getOWLThingClass()))) {
            for (Iterator it = superProperties.iterator(); it.hasNext();) {
                Slot superSlot = (Slot) it.next();
                for (Iterator ji = superSlot.getDirectOwnSlotValues(domainSlot).iterator(); ji.hasNext();) {
                    Cls cls = (Cls) ji.next();
                    if (cls instanceof RDFSClass && !clses.contains(cls) && !containsSubclassOf(cls)) {
                        clses.add(cls);
                    }
                }
            }
        }
        else {
            for (Iterator it = localDomain.iterator(); it.hasNext();) {
                Cls cls = (Cls) it.next();
                if (cls instanceof RDFSClass) {
                    clses.add(cls);
                }
            }
        }
    }


    RDFSClass getCls(int row) {
        return (RDFSClass) clses.get(row);
    }


    public Class getColumnClass(int columnIndex) {
        if (columnIndex == COL_CLASS) {
            return Frame.class;
        }
        else {
            return null;
        }
    }


    public int getColumnCount() {
        return 1;
    }


    public int getRowCount() {
        return clses.size();
    }


    public RDFProperty getSlot() {
        return property;
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == COL_CLASS) {
            return getCls(rowIndex);
        }
        else {
            return null;
        }
    }


    public boolean isInherited(Cls cls) {
        if (cls instanceof RDFSNamedClass) {
            final RDFProperty property = getSlot();
            final Slot domainSlot = ((KnowledgeBase) property.getOWLModel()).getSlot(Model.Slot.DIRECT_DOMAIN);
            final List domain = ((Slot) property).getDirectOwnSlotValues(domainSlot);
            return !domain.contains(cls);
        }
        else {
            return false;
        }
    }


    private void refill() {
        clses.clear();
        fill();
        fireTableDataChanged();
    }


    public void setSlot(RDFProperty newProperty) {
        dispose();
        property = newProperty;
        clses.clear();
        if (property != null) {
            property.addPropertyListener(propertyListener);
            fill();
        }
        fireTableDataChanged();
    }
}
