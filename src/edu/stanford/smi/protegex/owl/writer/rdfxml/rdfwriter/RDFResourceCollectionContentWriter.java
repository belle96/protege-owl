package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.writer.rdfxml.renderer.RDFAxiomRenderer;
import edu.stanford.smi.protegex.owl.writer.rdfxml.renderer.RDFResourceRenderer;
import edu.stanford.smi.protegex.owl.writer.xml.XMLWriter;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: March 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RDFResourceCollectionContentWriter implements RDFXMLContentWriter {

    private Collection resources;

    private RDFAxiomRenderer.RenderableAxiomsChecker checker;

    private TripleStore tripleStore;


    /**
     * Renders a collection of RDFResources (the axioms about
     * the resources).  Note that this does not render the opening
     * and closing rdf:RDF document element and namespace declarations.
     *
     * @param resources A collection of RDFResources.
     */
    public RDFResourceCollectionContentWriter(Collection resources, TripleStore tripleStore) {
        this.resources = resources;
        this.tripleStore = tripleStore;
        checker = RDFAxiomRenderer.getChecker();
    }


    public void writeContent(XMLWriter writer) {
        // Render the axioms of the resources
        for (Iterator it = resources.iterator(); it.hasNext();) {
            RDFResource resource = (RDFResource) it.next();
            if (checker.isRenderable(resource)) {
                RDFAxiomRenderer render = new RDFAxiomRenderer(resource, tripleStore, writer);
                render.write();
            }
            else {
                RDFResourceRenderer renderer = new RDFResourceRenderer(resource, tripleStore, writer);
                renderer.write();
            }
        }
    }

}

