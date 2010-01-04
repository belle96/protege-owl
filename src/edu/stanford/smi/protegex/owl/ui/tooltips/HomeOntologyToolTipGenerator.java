package edu.stanford.smi.protegex.owl.ui.tooltips;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStoreUtil;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.RepositoryManager;
import edu.stanford.smi.protegex.owl.ui.widget.OWLToolTipGenerator;

import java.net.URI;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         28-Mar-2006
 */
public class HomeOntologyToolTipGenerator implements OWLToolTipGenerator {

    public String getToolTipText(RDFSClass aClass) {
        return getToolTipText((RDFResource) aClass);
    }

    public String getToolTipText(RDFProperty prop) {
        return getToolTipText((RDFResource) prop);
    }

    public String getToolTipText(RDFResource res) {
        String text = null;
        if (res != null) {
            text = "";

            if (!(res instanceof OWLAnonymousClass)) {
                text += "<b>" + res.getURI() + "</b>";
            }

            OWLModel owlModel = res.getOWLModel();
            TripleStore homeTS = owlModel.getTripleStoreModel().getHomeTripleStore(res);
            if (homeTS != null) {
                OWLOntology homeOnt = (OWLOntology) homeTS.getOWLOntology();
                try {
                    URI homeOntURI = new URI(homeOnt.getURI());
                    text += "<br><b>ontology:</b> " + homeOntURI;

                    RepositoryManager repManager = owlModel.getRepositoryManager();
                    Repository homeRep = repManager.getRepository(homeOntURI);
                    String homeLocation = "";
                    if (homeRep != null) {
                        homeLocation = homeRep.getOntologyLocationDescription(homeOntURI);
                    }
                    else {
                        String projname = owlModel.getProject().getName();
                        if (projname == null) {
                            projname = "not yet saved";
                        }
                        homeLocation = "main ontology [" + projname + "]";
                    }
                    text += "<br><b>location:</b> " + homeLocation;
                }
                catch (Exception e) { // just do not print
                    Log.emptyCatchBlock(e);
                }
            }

            if (!text.equals("")) {
                text = "<html><body>" + text + "</body></html>";
            }
            else {
                text = null;
            }
        }

        return text;
    }
}
