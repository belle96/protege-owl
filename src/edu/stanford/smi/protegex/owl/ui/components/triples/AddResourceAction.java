package edu.stanford.smi.protegex.owl.ui.components.triples;

import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddResourceAction extends ResourceSelectionAction {

    private TriplesTable table;


    public AddResourceAction(TriplesTable table) {
        this(table, "Add existing resource as value...", OWLIcons.getAddIcon(OWLIcons.RDF_INDIVIDUAL));
    }


    public AddResourceAction(TriplesTable table, String name, Icon icon) {
        super(name, icon);
        this.table = table;
    }


    protected Collection getAllowedProperties(OWLModel owlModel) {
        return owlModel.getRDFProperties();
    }


    public Collection getSelectableResources() {
        TriplesTableModel tableModel = table.getTableModel();
        OWLModel owlModel = tableModel.getOWLModel();
        Collection<RDFProperty> properties = new ArrayList<RDFProperty>();
        Collection allowedProperties = getAllowedProperties(owlModel);
        for (Iterator it = allowedProperties.iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (property.isVisible() && property.hasObjectRange() && !property.isSystem()) {
                properties.add(property);
            }
            else if (property.isAnnotationProperty() && !(property instanceof OWLDatatypeProperty)) {
                properties.add(property);
            }
        }
        properties.add(owlModel.getOWLDisjointWithProperty());
        properties.add(owlModel.getOWLDifferentFromProperty());
        properties.add(owlModel.getOWLEquivalentPropertyProperty());
        properties.add(owlModel.getOWLSameAsProperty());
        properties.add(owlModel.getRDFProperty(RDFSNames.Slot.IS_DEFINED_BY));
        properties.add(owlModel.getRDFProperty(RDFSNames.Slot.SEE_ALSO));
        return properties;
    }


    public void resourceSelected(RDFResource resource) {
        TriplesTableModel tableModel = table.getTableModel();
        OWLModel owlModel = tableModel.getOWLModel();
        RDFProperty property = (RDFProperty) resource;
        if (property.hasObjectRange() || 
                (property.isAnnotationProperty() && !(property instanceof OWLDatatypeProperty))) {
            owlModel.getRDFUntypedResourcesClass().setVisible(true);
            Collection unionRangeClasses = property.getUnionRangeClasses();
            if(unionRangeClasses.isEmpty()) {
                unionRangeClasses = Collections.singleton(owlModel.getOWLThingClass());
            }
            RDFResource value = ProtegeUI.getSelectionDialogFactory().selectResourceByType(table, owlModel, unionRangeClasses);
            owlModel.getRDFUntypedResourcesClass().setVisible(false);
            tryToAddValue(property, value);
        }
        else {
            int row = tableModel.addRow(property);
            table.editCell(row);
        }
    }


    public RDFResource pickResource() {
        TriplesTableModel tableModel = table.getTableModel();
        OWLModel owlModel = tableModel.getOWLModel();
        Collection properties = getSelectableResources();
        return ProtegeUI.getSelectionDialogFactory().selectProperty(table, owlModel, properties);
    }


    private void tryToAddValue(RDFProperty property, RDFResource value) {
        if (value != null) {
            TriplesTableModel tableModel = table.getTableModel();
            if (tableModel.getSubject().getPropertyValues(property).contains(value)) {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(table, "This value is already used.");
            }
            else {
                int row = tableModel.addRow(property, value);
                table.getSelectionModel().setSelectionInterval(row, row);
                table.scrollRectToVisible(table.getCellRect(row, 0, true));
            }
        }
    }
}
