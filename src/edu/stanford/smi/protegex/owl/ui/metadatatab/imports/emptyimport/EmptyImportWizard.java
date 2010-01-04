package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.emptyimport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.logging.Level;

import javax.swing.JComponent;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.repository.RepositoryManager;
import edu.stanford.smi.protegex.owl.repository.impl.LocalFileRepository;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizard;
import edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter.OWLModelWriter;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 5, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class EmptyImportWizard extends OWLWizard {

    private URI ontologyURI;

    private File localFile;

    private OWLModel owlModel;


    public EmptyImportWizard(JComponent parent, OWLModel owlModel) {
        super(parent, "Empty Import");
        this.owlModel = owlModel;
        addPage(new EmptyImportExplanationPage(this, owlModel));
    }


    protected void onFinish() {
        super.onFinish();
        try {
            if (localFile != null && ontologyURI != null) {
                JenaOWLModel model = ProtegeOWL.createJenaOWLModel();
                model.getNamespaceManager().setDefaultNamespace(ontologyURI + "#");
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(localFile));
                OWLModelWriter writer = new OWLModelWriter(model,
                        model.getTripleStoreModel().getActiveTripleStore(),
                        osw);
                writer.write();
                osw.flush();
                osw.close();
                RepositoryManager rm = owlModel.getRepositoryManager();
                rm.addProjectRepository(new LocalFileRepository(localFile));
                ImportHelper importHelper = new ImportHelper((JenaOWLModel) owlModel);
	            importHelper.addImport(ontologyURI);
	            importHelper.importOntologies();
            }
        }
        catch (Exception e) {
          Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }


    public URI getOntologyURI() {
        return ontologyURI;
    }


    public void setOntologyURI(URI ontologyURI) {
        this.ontologyURI = ontologyURI;
    }


    public File getLocalFile() {
        return localFile;
    }


    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }
}

