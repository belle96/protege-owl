package edu.stanford.smi.protegex.owl.ui.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.JComponent;

import edu.stanford.smi.protege.action.ProjectAction;
import edu.stanford.smi.protege.ui.AboutBox;
import edu.stanford.smi.protege.util.ModalDialog;
import edu.stanford.smi.protegex.owl.resource.OWLText;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 *
 */
public class ShowAboutProtegeOWLAction extends ProjectAction {
	private final static String title = "About " + OWLText.getName() + "...";
	
	public ShowAboutProtegeOWLAction() {
		super(title);
        setEnabled(true);
	}

	public void actionPerformed(ActionEvent e) {
        JComponent pane = getProjectManager().getMainPanel();
        URL url = OWLText.getAboutURL();
        Dimension preferredSize = new Dimension(575, 525);
        AboutBox aboutProtegeOWL = new AboutBox(url, preferredSize);
        ModalDialog.showDialog(pane, aboutProtegeOWL, "About " + OWLText.getName(), ModalDialog.MODE_CLOSE);
	}
}
