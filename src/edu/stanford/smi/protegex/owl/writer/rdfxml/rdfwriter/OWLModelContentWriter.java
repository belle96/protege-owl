package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import edu.stanford.smi.protegex.owl.model.OWLAllDifferent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.writer.rdfxml.renderer.RDFAxiomRenderer;
import edu.stanford.smi.protegex.owl.writer.rdfxml.renderer.RDFResourceRenderer;
import edu.stanford.smi.protegex.owl.writer.xml.XMLWriter;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: March 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLModelContentWriter implements RDFXMLContentWriter {

    private OWLModel model;

    private TripleStore tripleStore;


    /**
     * Constructs an OWLModel RDF/XML rdfwriter to render the specified <code>OWLModel</code>.
     *
     * @param model       The model to be rendered.
     * @param tripleStore The triplestore to be rendered.
     */
    public OWLModelContentWriter(OWLModel model,
                                 TripleStore tripleStore) {
        this.model = model;
        this.tripleStore = tripleStore;
    }


    /**
     * Writes the RDF/XML content that corresponds to the ontology header (owl:Ontology),
     * the all different sets of individuals, the properties, classes and individuals.
     *
     * @param writer
     * @throws IOException
     */
    public void writeContent(XMLWriter writer)
            throws IOException {
        HashSet renderedResources = new HashSet();
        // Render the appropriate ontology and its properties
        for (Iterator it = model.getOWLOntologies().iterator(); it.hasNext();) {
            OWLOntology ont = (OWLOntology) it.next();        
			if (ont.getName().equals(tripleStore.getName())) {
                RDFResourceRenderer renderer = new RDFResourceRenderer(ont, tripleStore, writer);
                renderer.write();
                renderedResources.add(ont);
            }
        }

        // Render all different
        for (Iterator it = model.getOWLAllDifferents().iterator(); it.hasNext();) {
            OWLAllDifferent curAllDifferent = (OWLAllDifferent) it.next();
            if (model.getTripleStoreModel().getHomeTripleStore(curAllDifferent).equals(tripleStore)) {
                RDFResourceRenderer ren = new RDFResourceRenderer(curAllDifferent, tripleStore, writer);
                ren.write();
                renderedResources.add(curAllDifferent);
            }
        }

        // Render the resources that have this triplestore as their home
        // triplestore
        for (Iterator it = tripleStore.listHomeResources(); it.hasNext();) {
            RDFResource curRes = (RDFResource) it.next();
            if (curRes.isSystem() == false &&
                    curRes.isAnonymous() == false) {
                RDFAxiomRenderer axiomRenderer = new RDFAxiomRenderer(curRes, tripleStore, writer);
                axiomRenderer.write();
                renderedResources.add(curRes);
            }
        }

        // Render the resources that there are statements about in this triple store
        for (Iterator it = model.getRDFProperties().iterator(); it.hasNext();) {
            RDFProperty curProp = (RDFProperty) it.next();
            for (Iterator subjIt = tripleStore.listSubjects(curProp); subjIt.hasNext();) {
                Object subj = subjIt.next();
                if (subj instanceof RDFResource) {
                    RDFResource curSubj = (RDFResource) subj;
                    if (curSubj.isAnonymous() == false &&
                            curSubj.isSystem() == false &&
                            renderedResources.contains(curSubj) == false) {
                        RDFAxiomRenderer axiomRenderer = new RDFAxiomRenderer(curSubj, tripleStore, writer);
                        axiomRenderer.write();
                        renderedResources.add(curSubj);
                    }
                }
            }
        }
    }
}

