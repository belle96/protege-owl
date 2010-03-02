package edu.stanford.smi.protegex.owl.swrl.ui.table;

import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeleteRuleAction extends AbstractAction 
{
	private SWRLTable table;

	public DeleteRuleAction(SWRLTable table) 
	{
		super("Delete selected rule", OWLIcons.getDeleteIcon(SWRLIcons.IMP, SWRLIcons.class));
		this.table = table;
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) { updateEnabled(); }
		});
		updateEnabled();
	}

	public void actionPerformed(ActionEvent e) 
	{
		SWRLImp imp = table.getSelectedImp();
		imp.deleteImp();
	}

  private void updateEnabled() { setEnabled(table.getSelectedRowCount() == 1); }
}
