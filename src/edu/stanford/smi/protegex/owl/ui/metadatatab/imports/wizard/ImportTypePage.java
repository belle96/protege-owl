package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizard;
import edu.stanford.smi.protegex.owl.ui.wizard.OWLWizardPage;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 1, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ImportTypePage extends OWLWizardPage implements ActionListener {


    private JRadioButton webRadioButton;

    private JRadioButton repRadioButton;

    private JRadioButton locRadioButton;


    public ImportTypePage(OWLWizard wizard) {
        super("Import type", wizard);
        createUI();
    }


    private void createUI() {
	    setHelpText("Import methods", HELP_TEXT);
        JPanel northPanel = new JPanel(new BorderLayout(7, 7));
        northPanel.add(new JLabel("Please specify how you would like Protege-OWL to obtain the ontology to be imported:"),
                BorderLayout.NORTH);
        webRadioButton = new JRadioButton(IMPORT_FROM_WEB_TEXT);
        webRadioButton.addActionListener(this);
        repRadioButton = new JRadioButton(IMPORT_FROM_REP_TEXT);
        repRadioButton.addActionListener(this);
        locRadioButton = new JRadioButton(IMPORT_FROM_FILE_TEXT);
        locRadioButton.addActionListener(this);
        ButtonGroup bg = new ButtonGroup();
        bg.add(webRadioButton);
        bg.add(locRadioButton);
        bg.add(repRadioButton);
        webRadioButton.setSelected(true);
        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(webRadioButton);
        box.add(locRadioButton);
        box.add(repRadioButton);
        box.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        northPanel.add(box, BorderLayout.SOUTH);
        getContentComponent().add(northPanel, BorderLayout.NORTH);
    }


    public void actionPerformed(ActionEvent e) {
        updateNextPage();
    }


    public WizardPage getNextPage() {
        if (webRadioButton.isSelected()) {
            return new URLImportPage((ImportWizard) getWizard());
        }
        else if (repRadioButton.isSelected()) {
            return new RepositoryImportPage((ImportWizard) getWizard());
        }
        else if (locRadioButton.isSelected()) {
            return new FileImportPage((ImportWizard) getWizard());
        }
        else {
            return new NullWizardPage((ImportWizard) getWizard());
        }
    }


	public void nextPressed() {
		((ImportWizard) getWizard()).reset();
	}


    private static final String HELP_TEXT = "<p>Please specify where you would like to import an ontology from.</p>" +
            "<p>The system can either import an ontology directly from the web, import " +
            "an ontology contained in a local file, or import an ontology that is " +
            "contained in one of the available ontology repositories.</p>";


    private static final String IMPORT_FROM_WEB_TEXT = "Import an ontology from the web by specifying the " +
            "http://... URL";

    private static final String IMPORT_FROM_REP_TEXT = "Import an ontology contained " +
            "in one of the available repositories";

    private static final String IMPORT_FROM_FILE_TEXT = "Import an ontology contained in a " +
            "specific local file.";


    public static void main(String [] args) throws OntologyLoadException {
        OWLModel model = ProtegeOWL.createJenaOWLModel();
        ImportWizard w = new ImportWizard(null, model);
        ImportTypePage page = new ImportTypePage(w);
        w.addPage(page);
        w.execute();
    }

}

