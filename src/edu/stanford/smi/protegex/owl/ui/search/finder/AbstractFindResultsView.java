package edu.stanford.smi.protegex.owl.ui.search.finder;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         19-Oct-2005
 */
abstract class AbstractFindResultsView extends JComponent {

    private HostResourceDisplay hostResourceDisplay;

    protected AbstractFindResultsView(HostResourceDisplay hostResourceDisplay) {
        this.hostResourceDisplay = hostResourceDisplay;
    }

    abstract RDFResource getSelectedResource();

//    abstract void setResults(Map results);

    public abstract void addMouseListener(MouseListener l);

    public abstract void addKeyListener(KeyListener l);

    public abstract void requestFocus();

    /**
     * Make sure the resource is selected in the main Protege window
     */
    public void selectResource() {
        RDFResource resource = getSelectedResource();
        requestDispose();
        if (resource != null) {
            OWLUI.selectResource(resource, hostResourceDisplay);
        }
    }

    protected void requestDispose() {
        try {
            JDialog dialog = (JDialog) getTopLevelAncestor();
            if (dialog != null) {
                dialog.dispose();
            }
        }
        catch (Exception e) {
        }
    }
}
