package edu.stanford.smi.protegex.owl.ui.metadatatab.imports.wizard;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Dec 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ImportVerificationPage extends AbstractImportWizardPage {

	private Thread checkerThread;

	private Collection errors;

	public ImportVerificationPage(ImportWizard importWizard) {
		super("Import verification page", importWizard);
		setPageComplete(false);
	}

	public void pageSelected() {
		setPageComplete(false);
		getContentComponent().removeAll();
		showProgressPanel();
		Runnable runnable = new Runnable() {
			public void run() {
				errors = new ArrayList();
				for(Iterator it = getImportWizard().getImportData().getImportEntries().iterator(); it.hasNext(); ) {
					if(checkerThread == null) {
						break;
					}
					ImportEntry curEntry = (ImportEntry) it.next();
					if(curEntry.isPossibleToImport() == false) {
						errors.addAll(curEntry.getErrors());
					}
				}
				if(checkerThread != null &&
				   Thread.currentThread().equals(checkerThread)) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if(errors.size() == 0) {
								refill();
							}
							else {
								showErrorPanel();
							}
						}
					});
				}
			}
		};
		checkerThread = new Thread(runnable);
		checkerThread.start();
	}


	public void prevPressed() {
		checkerThread = null;
		setPageComplete(false);
	}


	public void onCancel() {
		checkerThread = null;
	}

	private void showProgressPanel() {
		getContentComponent().removeAll();
		getContentComponent().add(new ProgressPanel(), BorderLayout.NORTH);
		validate();
	}

	private void showErrorPanel() {
		getContentComponent().removeAll();
		getContentComponent().add(new ErrorPanel(), BorderLayout.NORTH);
		validate();
	}

	private void refill() {
		setPageComplete(true);
		getContentComponent().removeAll();
		getContentComponent().add(new ResultsPanel(), BorderLayout.NORTH);
		validate();
	}

	private class ProgressPanel extends JPanel {

		private JLabel messageLabel;

		public ProgressPanel() {
			setLayout(new BorderLayout(7, 7));
			setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
			messageLabel = new JLabel("Checking imports...");
			add(messageLabel, BorderLayout.NORTH);
			JProgressBar progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			add(progressBar, BorderLayout.SOUTH);
		}

		public void setMessage(String msg) {
			messageLabel.setText(msg);
		}
	}

	private class ErrorPanel extends JPanel {

		public ErrorPanel() {
			setLayout(new BorderLayout());
			String msg = "<html><body>" + errors.toArray()[0].toString() + "<br><br>" +
			             "Please press the Back button to specify another ontology.</body></html>";
			add(new JLabel(msg,
			               UIManager.getDefaults().getIcon("OptionPane.errorIcon"),
			               JLabel.LEFT));
			setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		}
	}

	private class ResultsPanel extends JPanel {

		public ResultsPanel() {
			Box box = new Box(BoxLayout.Y_AXIS);
			for(Iterator it = getImportWizard().getImportData().getImportEntries().iterator(); it.hasNext(); ) {
				ImportEntry curEntry = (ImportEntry) it.next();
				URI curURI = curEntry.getOntologyURI();
				box.add(new JLabel(curURI.toString()));
				String locMsg = "<html><body><font size=\"-2\" color=\"rgb(100, 100, 100)\">Imported from " +
				                curEntry.getRepository().getOntologyLocationDescription(curURI) +
                                "</font></body></html>";
				JLabel locLabel = new JLabel(locMsg);
				locLabel.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 0));
				box.add(locLabel);
			}
			box.setBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20));
			setLayout(new BorderLayout(7, 7));
			add(new JScrollPane(box), BorderLayout.CENTER);
			add(new JLabel("Press the finish button to import the following ontologies:"), BorderLayout.NORTH);
		}
	}
}

