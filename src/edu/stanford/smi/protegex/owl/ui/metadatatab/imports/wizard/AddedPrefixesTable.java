package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.NamespaceManager;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 30, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AddedPrefixesTable extends JTable {

    public AddedPrefixesTable(NamespaceManager nsm, Collection addedPrefixes) {
        super(new AddedPrefixesTableModel(nsm, addedPrefixes));
        setPreferredScrollableViewportSize(new Dimension(600, 200));
        getColumnModel().getColumn(AddedPrefixesTableModel.PREFIX_COLUMN).setWidth(30);
    }


    public static void showDialog(Component parent, OWLModel owlModel, Collection addedPrefixes) {
        JPanel holder = new JPanel(new BorderLayout(12, 12));
        AddedPrefixesTable table = new AddedPrefixesTable(owlModel.getNamespaceManager(), addedPrefixes);
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);
        holder.add(new LabeledComponent("Added namespace prefixes", sp));
        ProtegeUI.getModalDialogFactory().showDialog(parent, holder, "Added Prefixes", ModalDialogFactory.MODE_CLOSE);
    }


}

