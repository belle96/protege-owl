package edu.stanford.smi.protegex.owl.ui.query;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.Triple;
import edu.stanford.smi.protegex.owl.ui.actions.triple.AbstractTripleAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class QueryTripleAction extends AbstractTripleAction {


    public QueryTripleAction() {
        super("Copy into SPARQL query area", OWLIcons.SPARQL, OWLIcons.class);
    }


    public boolean isSuitable(Triple triple) {
        if (triple.getObject() instanceof String) {
            String str = (String) triple.getObject();
            return str.trim().toUpperCase().startsWith("SELECT");
        }
        else {
            return false;
        }
    }


    public void run(Triple triple) {
        OWLModel owlModel = triple.getSubject().getOWLModel();
        String str = (String) triple.getObject();
        SPARQLResultsPanel resultsPanel = SPARQLOWLModelAction.show(owlModel, true);
        resultsPanel.setQueryText(str);
    }
}
