package edu.stanford.smi.protegex.owl.swrl.ui.table;

import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.ui.code.SWRLTextAreaPanel;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ViewRuleAction extends AbstractAction {

	private SWRLTable table;

	public ViewRuleAction(SWRLTable table) 
	{
		super("Edit selected rule in multi-line editor...", OWLIcons.getViewIcon(SWRLIcons.IMP, SWRLIcons.class));
		this.table = table;
		table.addSelectionListener(new SelectionListener() {
			public void selectionChanged(SelectionEvent event) { updateEnabled();}
		});
		updateEnabled();
	}

	public void actionPerformed(ActionEvent e) 
	{
		final SWRLImp imp = table.getSelectedImp();
		OWLModel owlModel = imp.getOWLModel();
		if (SWRLTextAreaPanel.showEditDialog(table, owlModel, imp)) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					table.setSelectedRow(imp);
				}
			});
		}
	}

	private void updateEnabled() { setEnabled(table.getSelectedImp() != null && table.getSelectedImp().isEditable()); }
}
