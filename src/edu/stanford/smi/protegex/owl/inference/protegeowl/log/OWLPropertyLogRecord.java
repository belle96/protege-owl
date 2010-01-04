package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;

import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;

public class OWLPropertyLogRecord extends ReasonerLogRecord {

	private RDFProperty property;
	private JLabel label;

	public OWLPropertyLogRecord(RDFProperty prop, ReasonerLogRecord parent) {
		super(parent);
		this.property = prop;

		label = new JLabel(prop.getBrowserText());
		label.setIcon(ProtegeUI.getIcon(prop));
	}

	public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
			boolean arg4) {

		return label;
	}

	public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2,
			boolean arg3, boolean arg4, int arg5, boolean arg6) {
		return label;
	}

}
