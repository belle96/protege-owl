package edu.stanford.smi.protegex.owl.swrl.ui.table;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;

public class SWRLRuleGroupTreeTable extends JXTreeTable
{
	SWRLRuleGroupTreeTableModel model;
	
	public SWRLRuleGroupTreeTable(SWRLRuleGroupTreeTableModel model)
	{
		super(model);
		this.model = model;
		setTreeCellRenderer(new SWRLRuleGroupTreeCellRenderer());
		setEditable(true);
		//treeTable.setLeaf, setClosed, setOpen icons
		createPopupMenu();
   } 

	private void createPopupMenu() 
	{
		JPopupMenu popup = new JPopupMenu();
		popup.add(new EnableAllRulesAction());      
		addMouseListener(new PopupListener(popup));
	} 

	private class PopupListener extends MouseAdapter 
	{
		JPopupMenu popup;
		
		PopupListener(JPopupMenu popupMenu) { popup = popupMenu; }

		public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
		public void mouseReleased(MouseEvent e) { maybeShowPopup(e);  }
		private void maybeShowPopup(MouseEvent e) { if (e.isPopupTrigger()) popup.show(e.getComponent(), e.getX(), e.getY()); }
  } 
    
	private class EnableAllRulesAction extends AbstractAction
	{
		public EnableAllRulesAction() { super("Enable all rules"); }
		
		public void actionPerformed(ActionEvent e)
		{
			System.err.println("Selected row?");
			System.err.println("selected: " + getSelectedRow());
			
			TreePath path = getPathForRow(getSelectedRow());
			Object component = path.getLastPathComponent();
			
			System.err.println("path: " + path);
			
			if (component instanceof DefaultMutableTreeTableNode) {
				DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode)component;
				if (defNode.getUserObject() instanceof SWRLRuleGroup) {
					SWRLRuleGroup ruleGroup = (SWRLRuleGroup)defNode.getUserObject();
					System.err.println("RuleGroup.name: " + ruleGroup.getGroupName());
				} else if (defNode.getUserObject() instanceof SWRLRule) {
					SWRLRule rule = (SWRLRule)defNode.getUserObject();
					System.err.println("Rule.name: " + rule.getURI());
				} // if
			} // if         
		} 
	} 
} 
