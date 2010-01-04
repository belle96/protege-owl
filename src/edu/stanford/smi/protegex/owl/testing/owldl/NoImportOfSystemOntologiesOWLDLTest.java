package edu.stanford.smi.protegex.owl.testing.owldl;

import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoImportOfSystemOntologiesOWLDLTest extends AbstractOWLTest implements OWLDLTest, OWLModelTest {

    public final static String[] ILLEGAL_URIS = {
            OWL.getURI(),
            RDF.getURI(),
            RDFS.getURI()
    };


    public NoImportOfSystemOntologiesOWLDLTest() {
        super(GROUP, null);
    }


    public List test(OWLModel owlModel) {
        List results = new ArrayList();
        Iterator it = owlModel.getOWLOntologyClass().getInstances(false).iterator();
        while (it.hasNext()) {
            OWLOntology oi = (OWLOntology) it.next();
            for (int i = 0; i < ILLEGAL_URIS.length; i++) {
                String uri = ILLEGAL_URIS[i];
                uri = uri.substring(0, uri.length() - 1);
                if (oi.getImports().contains(uri)) {
                    results.add(new DefaultOWLTestResult("OWL DL ontologies must not import \"" + uri + "\".",
                            oi,
                            OWLTestResult.TYPE_OWL_FULL,
                            this));
                }
            }
        }
        return results;
    }
}
