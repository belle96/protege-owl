package edu.stanford.smi.protegex.owl.ui.repository;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.Wizard;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.parser.UnresolvedImportHandler;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.ui.repository.wizard.RepositoryWizard;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class UnresolvedImportUIHandler implements UnresolvedImportHandler {

    public static final String ADD_OPTION = "Add Repository";

    public static final String CANCEL_OPTION = "Cancel";

    public static final ArrayList OPTIONS = new ArrayList();


    public UnresolvedImportUIHandler() {
        OPTIONS.add(ADD_OPTION);
        OPTIONS.add(CANCEL_OPTION);
    }


    public Repository handleUnresolvableImport(OWLModel model,
                                               TripleStore tripleStore,
                                               URI ontologyName) {
        try {
        	//FIXME: Check this with UI and without!!
        	if (!OWLUtil.runsWithGUI(model)) {
        		Log.getLogger().warning("The system cannot find the ontology " + ontologyName + " in any of the repositories. This import will be ignored.");
        		return null;
        	}
        	
            Repository rep = null;
            while (rep == null) {
                if (showMessage(ontologyName) == OPTIONS.indexOf(CANCEL_OPTION)) {
                    return null;
                }
                RepositoryWizard wizard = new RepositoryWizard(null, model);
                if (wizard.execute() == Wizard.RESULT_CANCEL) {
                    return null;
                }
                rep = wizard.getRepository();
                if (rep != null) {
                    if (rep.contains(ontologyName)) {
                        return rep;
                    }
                    else {
                        rep = null;
                    }
                }
                wizard.dispose();
            }
            return null;
        }
        catch (HeadlessException he) {
            return null;
        }
    }


    private int showMessage(URI ontologyName) {
        return JOptionPane.showOptionDialog(null, "The system cannot find the ontology:\n" + ontologyName + "\n\n" + "Select '" + ADD_OPTION + "' to add a repository that contains\n" + "this ontology, or select '" + CANCEL_OPTION + "' to stop " + "loading and exit.", "Unresolved import",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, OPTIONS.toArray(),
                ADD_OPTION);
    }
}

