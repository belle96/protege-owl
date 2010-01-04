package edu.stanford.smi.protegex.owl.ui.triplestore;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreUtil;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourceselection.DropDownOverlayIcon;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TripleStoreSelectionAction extends AbstractAction {

    private JButton button;

    private JLabel label;

    private OWLModel owlModel;


    public TripleStoreSelectionAction(OWLModel owlModel) {
        super("Select active sub-ontology...", OWLIcons.getImageIcon(OWLIcons.SELECT_ACTIVE_TRIPLESTORE));
        this.owlModel = owlModel;
        label = new JLabel("                       ");
        updateLabel();
    }


    public void actionPerformed(ActionEvent e) {
        TripleStoreSelectionPanel.showDialog(owlModel);
        updateLabel();
    }


    public void activateButton(JButton button) {

        this.button = button;

        button.setToolTipText((String) getValue(Action.NAME));

        button.setRolloverIcon(new DropDownOverlayIcon(button.getIcon(), button));

        button.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && isEnabled()) {
                    handleRightClick();
                }
            }


            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && isEnabled()) {
                    handleRightClick();
                }

            }
        });
    }


    private void handleRightClick() {
        JPopupMenu menu = new JPopupMenu();
        TripleStoreTableModel tableModel = new TripleStoreTableModel(owlModel);
        final TripleStoreModel tsm = owlModel.getTripleStoreModel();
        TripleStore activeTripleStore = owlModel.getTripleStoreModel().getActiveTripleStore();

        {
            final TripleStore topTripleStore = tsm.getTopTripleStore();
            Action action = new DropDownAction(TripleStoreTableModel.MAIN_FILE_NAME, topTripleStore);
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
            item.setSelected(topTripleStore == activeTripleStore);
            menu.add(item);
        }

        for (int i = 1; i < tableModel.getRowCount(); i++) {
            TripleStore tripleStore = tableModel.getTripleStore(i);
            try {
                URI uri = new URI(tripleStore.getName());
                Repository rep = owlModel.getRepositoryManager().getRepository(uri);
                if (rep != null) {
                    if (rep.isWritable(uri)) {
                        Action action = new DropDownAction(uri.toString(), tripleStore);
                        JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
                        item.setToolTipText(tripleStore.getName());
                        item.setSelected(tripleStore == activeTripleStore);
                        menu.add(item);
                    }
                }
            }
            catch (Exception ex) {
              Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
            }
        }

        menu.show(button, 0, button.getHeight());
    }


    public Component getLabelPanel() {
        return label;
    }


    private void updateLabel() {
        //TripleStoreTableModel tableModel = new TripleStoreTableModel(owlModel);
        //int row = tableModel.getSelectedTripleStoreRow();
        TripleStore ts = owlModel.getTripleStoreModel().getActiveTripleStore();
        OWLOntology ont = ts.getOWLOntology();
        if (ont != null) {
            String file = ont.getURI();
            int index = file.lastIndexOf('/');
            if (index < 0) {
                index = file.lastIndexOf('\\');
            }
            if (index >= 0) {
                file = file.substring(index + 1);
            }
            label.setText(file);
        }
        else {
            label.setText("");
        }
    }


    private class DropDownAction extends AbstractAction {

        private TripleStore tripleStore;


        public DropDownAction(String name, TripleStore tripleStore) {
            super(name);
            this.tripleStore = tripleStore;
        }


        public void actionPerformed(ActionEvent e) {
            TripleStoreUtil.switchTripleStore(owlModel, tripleStore);
            updateLabel();
        }
    }
}
