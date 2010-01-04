package edu.stanford.smi.protegex.owl.ui.menu;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.tidy.Checker;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.OntModelProvider;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * An Action that allows to validate the OWL sublanguage of the current Jena ontology.
 *
 * @author Daniel Stoeckli <stoeckli@smi.stanford.edu>
 */
public class ValidatorAction extends AbstractOWLModelAction {
		
    public String getMenubarPath() {
        return TOOLS_MENU;
    }


    public String getName() {
        return "Determine OWL Sublanguage...";
    }


    public void run(OWLModel owlModel) {
        Set imports = owlModel.getAllImports();
        Map map = new HashMap();
        for (Iterator it = imports.iterator(); it.hasNext();) {
            String uriString = (String) it.next();
            try {
                URI uri = new URI(uriString);
                Repository rep = owlModel.getRepositoryManager().getRepository(uri);
                if (rep != null) {
                    map.put(uri, rep.getOntologyLocationDescription(uri));
                }
            }
            catch (URISyntaxException e) {
              Log.getLogger().log(Level.SEVERE, "Exception caught", e);
            }
        }

        if (!map.isEmpty()) {
            String str = "Your project uses redirected imports.  " +
                    "The species validation does not use these imports\n" +
                    "and therefore the following result may be wrong.  The following\n" +
                    "URI aliases are used:\n";
            for (Iterator it = map.keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                Object alias = map.get(key);
                str += "- " + key + "\n   has alias " + alias + "\n";
            }
            ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel, str, "Warning");
        }

        if (!OWLUI.isConfirmationNeeded(owlModel) ||
                OWLUI.isConfirmed(owlModel, owlModel.getRDFResourceCount() > OWLUI.getConfirmationThreshold(owlModel))) {
            performAction(owlModel);
        }
    }


    private void performAction(OWLModel owlModel) {
        OntModelProvider ontModelProvider = (OntModelProvider) owlModel;
        String msg = Jena.getOWLSpeciesString(ontModelProvider.getOWLSpecies());
        /*if (msg.equalsIgnoreCase(Jena.OWL_FULL)) {
            final OntModel owldlOntModel = ontModelProvider.getOWLDLOntModel();
            final String s = getSubLanguage(owldlOntModel);
            if (s.equalsIgnoreCase(Jena.OWL_FULL)) {
                msg += ",\nand Protege is currently not able to convert it to DL.";
            }
            else {
                msg += ",\nbut Protege can convert it to OWL " + s + ".\n\nWould you like to save the converted model to a file?";
                if (JOptionPane.showConfirmDialog(parent,
                        "The OWL sublanguage of this ontology is OWL " + msg, "OWL Sublanguage",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                    if (fileChooser == null) {
                        fileChooser = new JFileChooser(".");
                    }
                    if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        Jena.saveOntModel(file, owldlOntModel,
                                "A converted version of this ontology has been\nsaved as " + file);
                    }
                }
                return;
            }
        } */
        ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel,
                "The OWL sublanguage of this ontology is OWL " + msg,
                "OWL Sublanguage");
    }


    public static String getSubLanguage(OntModel ontModel) {
        Checker checker = new Checker(false);
        checker.addGraphAndImports(ontModel.getGraph());
        String sublanguage = checker.getSubLanguage();
        return sublanguage;
    }
}
