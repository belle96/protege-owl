package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.AbstractTabWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * A simple demo tab widget that shows how to write custom extensions
 * to Protege-OWL.  This tab contains a JList which displays all classes
 * that have been added to the knowledge base since the program started.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TutorialTabWidget extends AbstractTabWidget {

    private JList list;


    /**
     * The quasi constructor of the tab widget, which creates the sub components
     * and adds them to the panel.
     */
    public void initialize() {

        setLabel("Tutorial-Tab");
        setShortDescription("A simple demo tab that displays the recently added classes");

        // Create a JList that will display the classes in the usual Protege look-and-feel
        list = new JList();
        list.setCellRenderer(new FrameRenderer());

        // Fill the tab component
        LabeledComponent lc = new LabeledComponent("Added Classes", new JScrollPane(list));
        lc.addHeaderButton(new AbstractAction("View selected class", Icons.getViewIcon()) {
            public void actionPerformed(ActionEvent e) {
                viewSelectedCls();
            }
        });
        add(BorderLayout.CENTER, lc);

        // Install a ModelListener that automatically updates the list
        OWLModel owlModel = (OWLModel) getKnowledgeBase();
        owlModel.addModelListener(new ModelAdapter() {
            public void classCreated(RDFSClass cls) {
                addListEntry(cls);
            }
        });
    }


    /**
     * Adds a class to the list if it is a RDFSNamedClass (ignoring anonymous classes).
     *
     * @param cls the RDFSClass to add
     */
    private void addListEntry(RDFSClass cls) {
        if (cls instanceof RDFSNamedClass) {
            Vector values = new Vector();
            for (int i = 0; i < list.getModel().getSize(); i++) {
                values.add(list.getModel().getElementAt(i));
            }
            values.add(cls);
            list.setListData(values);
        }
    }


    /**
     * Views the selected class in an extra window.
     */
    private void viewSelectedCls() {
        RDFSNamedClass selectedClass = (RDFSNamedClass) list.getSelectedValue();
        if (selectedClass != null) {
            getProject().show(selectedClass);
        }
    }
}

