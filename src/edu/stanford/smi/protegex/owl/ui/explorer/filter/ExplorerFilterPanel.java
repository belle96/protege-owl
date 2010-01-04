package edu.stanford.smi.protegex.owl.ui.explorer.filter;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExplorerFilterPanel extends JPanel {

    private DefaultExplorerFilter filter;


    public ExplorerFilterPanel(OWLModel owlModel) {
        this(owlModel, new DefaultExplorerFilter());
    }


    public ExplorerFilterPanel(OWLModel owlModel, DefaultExplorerFilter filter) {
        this.filter = filter;
        ValidClassesPanel validClassesPanel = new ValidClassesPanel(filter);
        setLayout(new BorderLayout(8, 8));
        add(BorderLayout.WEST, validClassesPanel);
        add(BorderLayout.EAST, new ValidPropertyPanel(owlModel, filter));
    }


    public static boolean show(OWLModel owlModel, DefaultExplorerFilter filter) {
        ExplorerFilterPanel panel = new ExplorerFilterPanel(owlModel, filter);
        Component parent = ProtegeUI.getTopLevelContainer(owlModel.getProject());
        return ProtegeUI.getModalDialogFactory().showDialog(parent, panel, "Explore superclass relationships",
                ModalDialogFactory.MODE_OK_CANCEL) == ModalDialogFactory.OPTION_OK;
    }
}
