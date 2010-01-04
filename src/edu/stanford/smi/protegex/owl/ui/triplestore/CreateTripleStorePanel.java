package edu.stanford.smi.protegex.owl.ui.triplestore;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class CreateTripleStorePanel extends AddTripleStorePanel {


    protected CreateTripleStorePanel(OWLModel owlModel) {
        super(owlModel);
    }


    protected TripleStore performAction() {
        return null;
    }


    public boolean validateContents() {
        return false;
    }
}
