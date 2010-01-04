package edu.stanford.smi.protegex.owl.jena.graph;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.compose.MultiUnion;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A totally experimental class to create a Jena Model for a given OWLModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaModelFactory {

    /**
     * Creates a new Jena Model from a given OWLModel.
     * This Model will directly reflect the structure of the underlying OWLModel
     * for query purposes.  It will not be possible to write to this Model.
     *
     * @param owlModel the OWLModel to get a Model for
     * @return a (read-only) Model
     */
    public static Model createModel(OWLModel owlModel) {
        TripleStoreModel tsm = owlModel.getTripleStoreModel();
        if (tsm.getTripleStores().size() == 2) {
            TripleStore ts = tsm.getTopTripleStore();
            ProtegeGraph graph = new ProtegeGraph(owlModel, ts);
            return ModelFactory.createModelForGraph(graph);
        }
        else {
            Collection graphs = new ArrayList();
            Iterator it = tsm.listUserTripleStores();
            while (it.hasNext()) {
                TripleStore ts = (TripleStore) it.next();
                ProtegeGraph graph = new ProtegeGraph(owlModel, ts);
                graphs.add(graph);
            }
            Graph unionGraph = new MultiUnion(graphs.iterator());
            return ModelFactory.createModelForGraph(unionGraph);
        }
    }
}
