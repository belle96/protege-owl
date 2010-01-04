package edu.stanford.smi.protegex.owl.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromReaderCreator;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromUriCreator;

/**
 * The base class of various JUnit tests in this package.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractJenaTestCase extends AbstractOWLTestCase {
    private static transient final Logger log = Log.getLogger(AbstractJenaTestCase.class);

    protected OntModel ontModel;

    protected Model owlFullModel;


    protected int getNamedClassesCount() {
        return list(ontModel.listNamedClasses()).size();
    }


    public void loadRemoteOntology(String localFileName) throws OntologyLoadException {
        loadTestOntology(getRemoteOntologyURI(localFileName));
    }


    protected void loadRemoteOntologyWithProtegeMetadataOntology() throws Exception {
        loadRemoteOntology("import-protege.owl");
    }


    @SuppressWarnings("unchecked")
    public void loadTestOntology(URI uri) throws OntologyLoadException {
        Collection errors = new ArrayList();
        OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
        creator.setOntologyUri(uri.toString());
        creator.create(errors);
        owlModel = creator.getOwlModel();
        owlModel.setExpandShortNameInMethods(true);
        project = owlModel.getProject();
        owlThing = owlModel.getOWLThingClass();
    }


    protected boolean ontResourceExists(Iterator it, String uri) {
        while (it.hasNext()) {
            OntResource ontResource = (OntResource) it.next();
            String otherURI = ontResource.getURI();
            if (otherURI.equals(uri)) {
                return true;
            }
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    public JenaOWLModel reload(JenaOWLModel owlModel) throws Exception {
        Collection errors = new ArrayList();
        OntModel ontModel = owlModel.getOntModel();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Jena.dumpRDF(ontModel, stream);
        String str = stream.toString();
        StringReader reader = new StringReader(str);
        if (log.isLoggable(Level.FINE)) {
            log.fine("Saved ontology to string");
            log.fine(str);
            log.fine("reloading...");
        }
        OwlProjectFromReaderCreator creator = new OwlProjectFromReaderCreator();
        creator.setReader(reader);
        creator.create(errors);
        return creator.getOwlModel();
    }


    public JenaOWLModel reloadWithJenaLoader(JenaOWLModel owlModel) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Jena.dumpRDF(owlModel.getOntModel(), stream);
        String str = stream.toString();
        return ProtegeOWL.createJenaOWLModelFromReader(new StringReader(str));
    }
}
