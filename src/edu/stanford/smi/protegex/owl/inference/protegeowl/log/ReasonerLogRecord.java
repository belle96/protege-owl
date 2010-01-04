package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 23, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class ReasonerLogRecord implements ListCellRenderer, TreeCellRenderer {

    private ReasonerLogRecord parent;


    public ReasonerLogRecord(ReasonerLogRecord parent) {
        this.parent = parent;
    }


    public ReasonerLogRecord getParent() {
        return parent;
    }
}

