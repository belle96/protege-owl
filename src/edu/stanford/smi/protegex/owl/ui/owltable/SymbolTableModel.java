package edu.stanford.smi.protegex.owl.ui.owltable;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SymbolTableModel extends TableModel {

    RDFProperty getPredicate(int row);


    RDFResource getRDFResource(int row);


    RDFResource getSubject();


    int getSymbolColumnIndex();


    Icon getIcon(RDFResource resource);
}
