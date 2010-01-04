package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.explorer.ExplorerFilter;
import edu.stanford.smi.protegex.owl.ui.explorer.ExplorerTreePanel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ToggleSuperclassExplorerAction extends AbstractAction {

    private Component component;

    private ExplorerTreePanel explorerTreePanel;

    private Selectable selectable;

    private boolean useInferredSuperclasses;


    public ToggleSuperclassExplorerAction(Selectable selectable, boolean useInferredSuperclasses) {
        super("Show/Hide superclass explorer", OWLIcons.getImageIcon(OWLIcons.SUPERCLASS_EXPLORER));
        this.component = (Component) selectable;
        this.selectable = selectable;
        this.useInferredSuperclasses = useInferredSuperclasses;
        selectable.addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                if (explorerTreePanel != null) {
                    RDFSClass root = getSelectedClass();
                    explorerTreePanel.setRoot(root);
                    explorerTreePanel.expandToFillSpace();
                }
            }
        });
    }


    public void actionPerformed(ActionEvent e) {
        Component c = component;
        while (c != null && !(c instanceof HierarchyPanel)) {
            c = c.getParent();
        }
        if (c != null) {
            HierarchyPanel panel = (HierarchyPanel) c;
            if (panel.getNestedHierarchy() != null) {
                panel.setNestedHierarchy(null);
                explorerTreePanel = null;
            }
            else {
                RDFSClass root = getSelectedClass();
                String prefix = useInferredSuperclasses ? "Inferred" : "Asserted";
                explorerTreePanel = new ExplorerTreePanel(root, new ExplorerFilter() {
                    public boolean getUseInferredSuperclasses() {
                        return useInferredSuperclasses;
                    }


                    public boolean isValidChild(RDFSClass parentClass, RDFSClass childClass) {
                        return childClass instanceof RDFSNamedClass;
                    }
                }, prefix + " Superclasses", false);
                panel.setNestedHierarchy(explorerTreePanel);
                explorerTreePanel.expandToFillSpace();
            }
        }
    }


    private RDFSClass getSelectedClass() {
        RDFSClass selectedClass = null;
        Collection sel = selectable.getSelection();
        if (sel != null && sel.size()>0){
            selectedClass = (RDFSClass) CollectionUtilities.getFirstItem(sel);
        }
        return selectedClass;
    }
}
