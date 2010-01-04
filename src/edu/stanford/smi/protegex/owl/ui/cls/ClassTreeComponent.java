package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.ui.ParentChildRoot;
import edu.stanford.smi.protege.util.SelectableTree;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * An panel showing a JTree of all classes in an OWLModel.
 * This can be configured in various ways, e.g. you can get the OWLLabeledComponent
 * to add buttons to the header, or you can listen to selection changes in the tree.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassTreeComponent extends JPanel {

    private OWLLabeledComponent lc;

    private SelectableTree tree;


    public ClassTreeComponent(String title, OWLModel owlModel) {
        this(title, owlModel.getOWLThingClass(),
                new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        // Do nothing
                    }
                });
    }


    public ClassTreeComponent(String title, RDFSNamedClass rootClass, Action doubleClickAction) {
        tree = new SelectableTree(doubleClickAction, new ParentChildRoot(rootClass));
        tree.setExpandsSelectedPaths(true);
        tree.setSelectionRow(0);
        tree.setCellRenderer(new ResourceRenderer());
        setLayout(new BorderLayout());
        lc = new OWLLabeledComponent(title, new JScrollPane(tree));
        add(BorderLayout.CENTER, lc);
    }


    public OWLLabeledComponent getLabeledComponent() {
        return lc;
    }


    public SelectableTree getTree() {
        return tree;
    }
}
