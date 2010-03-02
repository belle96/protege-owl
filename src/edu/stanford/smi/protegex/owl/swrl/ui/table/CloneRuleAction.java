package edu.stanford.smi.protegex.owl.swrl.ui.table;

import edu.stanford.smi.protegex.owl.model.OWLModel;
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
public class CloneRuleAction extends AbstractAction 
{
  private SWRLTable table;

  public CloneRuleAction(SWRLTable table, OWLModel owlModel) 
  {
  	super("Clone selected rule", OWLIcons.getAddIcon(SWRLIcons.IMP, SWRLIcons.class));
  	this.table = table;
  	table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
  		public void valueChanged(ListSelectionEvent e) { updateEnabled();	}
  	});
  	updateEnabled();
  }

  public void actionPerformed(ActionEvent e) 
  {
  	SWRLImp imp = table.getSelectedImp();
  	final SWRLImp c = imp.createClone();
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() { table.setSelectedRow(c); }
  	});
  }

  private void updateEnabled() { setEnabled(table.getSelectedRowCount() == 1); }
}
