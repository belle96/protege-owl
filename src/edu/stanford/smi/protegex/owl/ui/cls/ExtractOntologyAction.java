package edu.stanford.smi.protegex.owl.ui.cls;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JFileChooser;

import com.hp.hpl.jena.ontology.OntModel;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.database.OWLDatabaseModel;
import edu.stanford.smi.protegex.owl.jena.Jena;
import edu.stanford.smi.protegex.owl.jena.JenaDLConverter;
import edu.stanford.smi.protegex.owl.jena.creator.JenaCreator;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;
import edu.stanford.smi.protegex.owl.ui.widget.ModalProgressBarManager;

/**
 * Currently only working in database mode!
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ExtractOntologyAction extends ResourceAction {
    private static transient final Logger log = Log.getLogger(ExtractOntologyAction.class);
    private JFileChooser fileChooser;


    public ExtractOntologyAction() {
        super("Extract (sub) ontology to OWL file...", Icons.getBlankIcon(),
                ExtractTaxonomyAction.GROUP);
    }


    public void actionPerformed(ActionEvent e) {

        int result = ProtegeUI.getModalDialogFactory().showConfirmCancelDialog(getResource().getOWLModel(),
                "Shall the OWL File be converted to OWL DL if needed?",
                getValue(Action.NAME).toString());

        if (result == ModalDialogFactory.OPTION_CANCEL) {
            return;
        }

        Collection clses = null;
        RDFSNamedClass aClass = (RDFSNamedClass) getResource();
        if (!getResource().equals(getResource().getOWLModel().getOWLThingClass())) {
            clses = new HashSet();
            for (Iterator it = aClass.getSubclasses(true).iterator(); it.hasNext();) {
                Object c = it.next();
                if (c instanceof OWLNamedClass && ((OWLNamedClass) c).isEditable()) {
                    clses.add(c);
                }
            }
            clses.add(aClass);
        }

        final OWLModel owlModel = aClass.getOWLModel();
        JenaCreator creator = new JenaCreator(owlModel, false, clses,
                new ModalProgressBarManager("Preparing Ontology"));
        OntModel ontModel = creator.createOntModel();
        if (result == ModalDialogFactory.OPTION_OK) {
            log.info("Running JenaDLConverter...");
            JenaDLConverter c = new JenaDLConverter(ontModel, owlModel.getNamespaceManager());
            ontModel = c.convertOntModel();
        }

        if (fileChooser == null) {
            fileChooser = new JFileChooser(".");
        }
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Jena.saveOntModel(owlModel, file, ontModel, "The (sub) ontology has been extracted to\n" + file);
        }
    }


    @Override
    public boolean isSuitable(Component component, RDFResource resource) {
        return component instanceof OWLSubclassPane &&
                resource instanceof RDFSNamedClass &&
                resource.getOWLModel() instanceof OWLDatabaseModel;
    }
}
