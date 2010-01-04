package edu.stanford.smi.protegex.owl.ui.clsdesc.manchester;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface Suggestion {

    public void performSuggestion();


    public Icon getIcon();


    public String getDescription();
}
