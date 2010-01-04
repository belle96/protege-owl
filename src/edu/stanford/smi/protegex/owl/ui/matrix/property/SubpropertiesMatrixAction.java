package edu.stanford.smi.protegex.owl.ui.matrix.property;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixPanel;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;
import edu.stanford.smi.protegex.owl.ui.search.SearchNamedClassAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SubpropertiesMatrixAction extends ResourceAction {

    public SubpropertiesMatrixAction() {
        super("Show list of subproperties",
                OWLIcons.getImageIcon(OWLIcons.PROPERTY_MATRIX),
                SearchNamedClassAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {
        final OWLModel owlModel = getOWLModel();
        final RDFProperty parentProperty = (RDFProperty) getResource();
        MatrixFilter filter = new MatrixFilter() {

            public Collection getInitialValues() {
                Collection results = new ArrayList();
                Iterator it = parentProperty.getSubproperties(true).iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (next instanceof RDFProperty) {
                        results.add(next);
                    }
                }
                results.remove(parentProperty);
                return results;
            }


            public String getName() {
                return "Subproperties of " + parentProperty.getBrowserText();
            }


            public boolean isSuitable(RDFResource instance) {
                return instance instanceof RDFProperty &&
                        instance.isVisible() &&
                        (instance.isEditable() || instance.isIncluded()) &&
                        ((RDFProperty) instance).isSubpropertyOf(parentProperty, true);
            }
        };
        MatrixPanel panel = new MatrixPanel(owlModel, filter, new PropertyMatrixTableModel(owlModel, filter));
        ResultsPanelManager.addResultsPanel(owlModel, panel, true);
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof RDFProperty;
    }
}
