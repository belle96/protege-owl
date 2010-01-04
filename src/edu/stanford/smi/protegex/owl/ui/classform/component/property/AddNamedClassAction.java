package edu.stanford.smi.protegex.owl.ui.classform.component.property;

import edu.stanford.smi.protege.Application;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;

import java.awt.*;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AddNamedClassAction extends ResourceSelectionAction {

    private PropertyFormTable table;

    public AddNamedClassAction(PropertyFormTable table) {
        super("Add named class...", OWLIcons.getAddIcon(OWLIcons.PRIMITIVE_OWL_CLASS));
        this.table = table;
    }


    public Collection getSelectableResources() {
        OWLModel owlModel = table.getTableModel().getProperty().getOWLModel();
        Collection col = owlModel.getUserDefinedOWLNamedClasses();
        col.removeAll(table.getTableModel().getRDFResources());
        return col;
    }


    public void resourceSelected(RDFResource resource) {
        RDFProperty prop = table.getTableModel().getProperty();
        RDFSClass subject = (RDFSClass) table.getTableModel().getNamedClass();
        OWLSomeValuesFrom someRestr =
                subject.getOWLModel().createOWLSomeValuesFrom(prop, resource);
        subject.addSuperclass(someRestr);
        //@@TODO update closure if it exists
    }

    public RDFResource pickResource() {
        RDFProperty prop = table.getTableModel().getProperty();
        Collection ranges = prop.getRanges(true);
        OWLModel owlModel = prop.getOWLModel();
        Component mainWindow = Application.getMainWindow();

        return ProtegeUI.getSelectionDialogFactory().selectClass(mainWindow,
                                                                 owlModel,
                                                                 ranges);
    }
}
