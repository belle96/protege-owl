package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.tests;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Model;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.protege2jena.Protege2Jena;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 9, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractRDFXMLWriterTestCases extends AbstractJenaTestCase {
    private static transient final Logger log = Log.getLogger(AbstractRDFXMLWriterTestCases.class);

    protected void doCheck() {
        try {
            OntModel ontModel1 = Protege2Jena.createOntModel(owlModel);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Jena writer ontModel1 ------------------------------------------------\n");
                Jena.dumpRDF(ontModel1, log, Level.FINE);
            }

            StringWriter writer = new StringWriter();
            OWLModelWriter omw = new OWLModelWriter(owlModel, owlModel.getTripleStoreModel().getActiveTripleStore(), writer);
            omw.write();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Native writer intermediate to ontModel2 ------------------------------------------------\n");
                log.fine(writer.getBuffer().toString());
            }

            OWLModel model = ProtegeOWL.createJenaOWLModelFromReader(new StringReader(writer.getBuffer().toString()));
            OntModel ontModel2 = Protege2Jena.createOntModel(model);
            
            if (log.isLoggable(Level.FINE)) {
                log.fine("Jena writer ontModel2 ------------------------------------------------\n");
                Jena.dumpRDF(ontModel2, log, Level.FINE);
            }

            if (log.isLoggable(Level.FINER)) {
                if (!ontModel1.isIsomorphicWith(ontModel2) || 
                        !ontModel2.isIsomorphicWith(ontModel1)) {
                    log.finer("-----------------------ontModel1---------------------------------");
                    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
                    ontModel1.write(out1, "N-TRIPLE");
                    log.finer(out1.toString());
                    log.finer("-----------------------ontModel1---------------------------------");
                    log.finer("-----------------------ontModel2---------------------------------");
                    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                    ontModel2.write(out2, "N-TRIPLE");
                    log.finer(out2.toString());
                    log.finer("-----------------------ontModel2---------------------------------");
                }
            }
            
            assertTrue(ontModel1.isIsomorphicWith(ontModel2));
            assertTrue(ontModel2.isIsomorphicWith(ontModel1));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

