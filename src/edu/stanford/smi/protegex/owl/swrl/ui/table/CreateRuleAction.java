package edu.stanford.smi.protegex.owl.swrl.ui.table;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.ui.code.SWRLTextAreaPanel;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateRuleAction extends AbstractAction 
{
  private OWLModel owlModel;
  private SWRLTable table;
  private SWRLFactory factory;

  public CreateRuleAction(SWRLTable table, OWLModel owlModel) {
    super("Create new rule...", OWLIcons.getCreateIcon(SWRLIcons.IMP, SWRLIcons.class));
    this.owlModel = owlModel;
    this.table = table;
    factory = new SWRLFactory(owlModel);
  }

  public void actionPerformed(ActionEvent e) 
  {
    final SWRLImp newImp = factory.createImp();
    if (SWRLTextAreaPanel.showEditDialog(table, owlModel, newImp)) {
      SwingUtilities.invokeLater(new Runnable() {
          public void run() { table.setSelectedRow(newImp); }
      });
    } else newImp.delete();
  }
}
