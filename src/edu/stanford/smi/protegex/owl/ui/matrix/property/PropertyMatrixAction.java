package edu.stanford.smi.protegex.owl.ui.matrix.property;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixFilter;
import edu.stanford.smi.protegex.owl.ui.matrix.MatrixPanel;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyMatrixAction extends AbstractAction {

    private OWLModel owlModel;


    public PropertyMatrixAction(OWLModel owlModel) {
        super("Show properties list", OWLIcons.getImageIcon(OWLIcons.PROPERTY_MATRIX));
        this.owlModel = owlModel;
    }


    public void actionPerformed(ActionEvent e) {
        MatrixFilter filter = new MatrixFilter() {

            public Collection getInitialValues() {
                Collection properties = owlModel.getVisibleUserDefinedRDFProperties();
                for (Iterator it = properties.iterator(); it.hasNext();) {
                    RDFResource slot = (RDFResource) it.next();
                    if (!isSuitable(slot)) {
                        it.remove();
                    }
                }
                return properties;
            }


            public String getName() {
                return "All Properties";
            }


            public boolean isSuitable(RDFResource instance) {
                return instance instanceof RDFProperty &&
                        instance.isVisible() &&
                        (instance.isEditable() || instance.isIncluded());
            }
        };
        MatrixPanel panel = new MatrixPanel(owlModel, filter, new PropertyMatrixTableModel(owlModel, filter));
        ResultsPanelManager.addResultsPanel(owlModel, panel, true);
    }
}
