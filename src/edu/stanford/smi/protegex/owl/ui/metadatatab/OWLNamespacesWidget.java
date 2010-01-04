package edu.stanford.smi.protegex.owl.ui.metadatatab;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyWidget;

import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLNamespacesWidget extends AbstractPropertyWidget {

    private OWLNamespacesPanel panel;


    public void dispose() {
        super.dispose();
        if (panel != null) {
            panel.dispose();
        }
    }


    public void initialize() {
        panel = new OWLNamespacesPanel(getOWLModel().getDefaultOWLOntology());
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, panel);
    }


    public static boolean isSuitable(Cls cls, edu.stanford.smi.protege.model.Slot slot, Facet facet) {
        return cls.getKnowledgeBase() instanceof OWLModel &&
                slot.getName().equals(OWLNames.Slot.IMPORTS);
    }


    public void setInstance(Instance newInstance) {
        remove(panel);
        panel.dispose();
        panel = null;
        super.setInstance(newInstance);
        if (newInstance instanceof OWLOntology) {
            panel = new OWLNamespacesPanel((OWLOntology) newInstance);
            add(BorderLayout.CENTER, panel);
            revalidate();
        }
        TripleStoreModel tsm = getOWLModel().getTripleStoreModel();
        panel.setEnabled(tsm.getActiveTripleStore() == tsm.getTopTripleStore() && isEnabled());
    }
    
    public void setEnabled(boolean enabled) {
    	panel.setEnabled(enabled);
    	super.setEnabled(enabled);
    };
}
