
package edu.stanford.smi.protegex.owl.swrl.ui.table;

import javax.swing.tree.DefaultTreeCellRenderer;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLRule;

public class SWRLRuleGroupTreeCellRenderer extends DefaultTreeCellRenderer
{
	public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel,
				                                                 boolean expanded, boolean leaf, int row, boolean hasFocus) 
	{	
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
		if (value instanceof DefaultMutableTreeTableNode) {
			DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode)value;
			if (node.getUserObject() instanceof SWRLRule) {
				SWRLRule rule = (SWRLRule)node.getUserObject();
				setText(rule.getRuleText());
			} else if (node.getUserObject() instanceof SWRLRuleGroup) {
				SWRLRuleGroup ruleGroup = (SWRLRuleGroup)node.getUserObject();
				setText(ruleGroup.getGroupName());
			} // if
			
			if (node.isLeaf()) {
				if (node.getParent() == tree.getModel().getRoot()) setIcon(getDefaultClosedIcon());
				else setIcon(getDefaultLeafIcon());
			} // if
		} // if
        
		return this;
	} 
} 
