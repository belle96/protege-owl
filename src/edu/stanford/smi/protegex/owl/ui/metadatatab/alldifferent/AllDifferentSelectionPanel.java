package edu.stanford.smi.protegex.owl.ui.metadatatab.alldifferent;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ClassAdapter;
import edu.stanford.smi.protegex.owl.model.event.ClassListener;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Daniel Stoeckli  <stoeckli@smi.stanford.edu>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AllDifferentSelectionPanel extends JPanel
        implements AllDifferentMemberChangedListener, Disposable {

    /**
     * A listener to the OWLAllDifferent metaclass which detected creating
     * and deleting AllDifferentInstances
     */
    private ClassListener clsListener = new ClassAdapter() {
        public void instanceAdded(RDFSClass cls, RDFResource instance) {
            listModel.addElement(instance);
        }


        public void instanceRemoved(RDFSClass cls, RDFResource instance) {
            listModel.removeElement(instance);
        }
    };

    private Action createAction =
            new AbstractAction("Create owl:AllDifferent", Icons.getCreateIcon()) {

                public void actionPerformed(ActionEvent arg0) {

                    OWLAllDifferent adi = owlModel.createOWLAllDifferent();
                    list.setSelectedValue(adi, true);
                }
            };

    private Action deleteAction =
            new AbstractAction("Delete owl:AllDifferent", OWLIcons.getDeleteIcon()) {

                public void actionPerformed(ActionEvent actionEvent) {

                    OWLAllDifferent ali = (OWLAllDifferent) list.getSelectedValue();

                    // no item is selected
                    if (ali == null) {
                        editorPanel.setIsAllDifferentItemSelected(false);
                        ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel,
                                "Select an All-Different Element first", "No Item Selected");
                        return;
                    }

                    ali.delete();

                    // remove from the jlist in editorPanel
                    editorPanel.setSelectedAllDifferentInstance(null);
                }
            };

    private AllDifferentEditorPanel editorPanel;

    /**
     * The JList displaying the AllDifferentInstances
     */
    private JList list;

    /**
     * A ListModel containing the AllDifferentInstances
     */
    private DefaultListModel listModel;

    private OWLModel owlModel;


    private ListSelectionListener selectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            OWLAllDifferent adi = (OWLAllDifferent) list.getSelectedValue();

            // at least one item in the list
            if (adi != null) {
                editorPanel.setIsAllDifferentItemSelected(true);
                editorPanel.setSelectedAllDifferentInstance(adi);
                deleteAction.setEnabled(true);
            }
            else { // no items in the list
                editorPanel.setSelectedAllDifferentInstance(null);
                editorPanel.disableAddAction();
                deleteAction.setEnabled(false);
            }
        }
    };


    public AllDifferentSelectionPanel(OWLModel owlModel, AllDifferentEditorPanel editorPanel) {

        this.owlModel = owlModel;
        this.editorPanel = editorPanel;

        listModel = new DefaultListModel();
        Collection adis = owlModel.getOWLAllDifferents();
        for (Iterator it = adis.iterator(); it.hasNext();) {
            OWLAllDifferent OWLAllDifferent = (OWLAllDifferent) it.next();
            listModel.addElement(OWLAllDifferent);
        }
        owlModel.getOWLAllDifferentClass().addClassListener(clsListener);

        deleteAction.setEnabled(false);

        list = new JList(listModel);
        list.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                OWLAllDifferent adi = (OWLAllDifferent) value;
                return super.getListCellRendererComponent(list, adi.getBrowserText(), index, isSelected, cellHasFocus);
            }
        });
        list.addListSelectionListener(selectionListener);
        JScrollPane scrollPane = new JScrollPane(list);
        LabeledComponent lc = new LabeledComponent("Sets of \"all different\" Individuals", scrollPane);

        lc.addHeaderButton(createAction);
        lc.addHeaderButton(deleteAction);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, lc);
    }


    public void dispose() {
        owlModel.getOWLAllDifferentClass().removeClassListener(clsListener);
    }


    protected void updateListUI() {
        list.updateUI();
    }


    public void allDifferentMemberChanged() {
        list.updateUI();
    }
}
