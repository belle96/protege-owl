package edu.stanford.smi.protegex.owl.ui.owltable;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * The base TableModel for all OWLTables.  This defines those methods that are common
 * to all TableModel implementations (ConditionsTableModel, EquivalentClassesTableModel,
 * SuperclassesTableModel, RestrictionsTableModel, and DisjointClassesTableModel).
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLTableModel extends SymbolTableModel {


    boolean addRow(RDFSClass aClass, int selectedRow);


    int addEmptyRow(int rowIndex);


    void deleteRow(int index);


    /**
     * Displays an illegal assignment error dialog, e.g. through a JOptionPane.
     * This can be used if someone tries to assign an illegal superclass to the model.
     *
     * @param message the message text
     */
    void displaySemanticError(String message);


    void dispose();


    RDFSClass getClass(int index);


    int getClassRow(RDFSClass cls);


    OWLNamedClass getEditedCls();


    boolean isAddEnabledAt(int rowIndex);


    boolean isDeleteEnabledFor(RDFSClass cls);


    boolean isEditable();


    void removeEmptyRow();


    void setCls(OWLNamedClass cls);
}
