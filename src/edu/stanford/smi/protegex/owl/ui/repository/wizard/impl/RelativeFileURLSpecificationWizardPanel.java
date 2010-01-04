package edu.stanford.smi.protegex.owl.ui.repository.wizard.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.repository.Repository;
import edu.stanford.smi.protegex.owl.repository.impl.RelativeFileRepository;
import edu.stanford.smi.protegex.owl.repository.util.RepositoryUtil;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 28, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RelativeFileURLSpecificationWizardPanel extends RelativeURLSpecificationWizardPanel {
	
	public RelativeFileURLSpecificationWizardPanel(WizardPage wizardPage,
			OWLModel model) {
		super(wizardPage, model);
	}

	@Override
	public Repository createRepository() {
		try {
			File file = RepositoryUtil.getRepositoryFileFromRelativePath(
					getOWLModel(), getRelativePath());

			if (file != null) {
				return new RelativeFileRepository(file, getRelativePath(),
						isForcedReadOnlySelected());
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}

		return null;
	}

	@Override
	protected String getDocumentation() {
		return HELP_TEXT;
	}

	private static final String HELP_TEXT = "<p>Please specify a relative <b>URL</b> that points "
			+ "to an ontology file.</p>"
			+ "<p>The URL should be relative to the current pprj/owl file."
			+ "For example if the pprj/owl file "
			+ "is located at /A/B/C/c.owl, the relative "
			+ "URL ../B/b.owl would specify the ontology file "
			+ "with the path /A/B/b.owl"
			+ "<p>Note that the path separator for URLs is the forward "
			+ "slash '/', and spaces must be replaced with \"%20\".</p>";
}

