package edu.stanford.smi.protegex.owl.swrl.ui.table;

import java.util.Set;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;
import edu.stanford.smi.protegex.owl.swrl.owlapi.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLRule;

public class SWRLRuleGroupTreeTableModel extends DefaultTreeTableModel 
{
	public static final int RuleGroupColumn = 0;
	public static final int IsEnabledColumn = 1;
	public static final int RuleTextColumn = 2;
	private static final int NumberOfColumns = 3;
	private DefaultMutableTreeTableNode rootNode;
	private Set<SWRLRule> rules;
	
	public SWRLRuleGroupTreeTableModel(OWLDataFactory owlFactory) throws OWLFactoryException
	{ 
	  rootNode = new DefaultMutableTreeTableNode(new SWRLRuleGroup()); // Not visible; dummy rule group
	  setRoot(rootNode); 
	  rules = owlFactory.getSWRLRules();
	  addRules(rules);
	} 
	
	public void addRule(SWRLRule rule)
	{
    boolean existingGroupFound = false;
		
    for (int i = 0; i < rootNode.getChildCount() && !existingGroupFound; i++) { // Find existing group and add this rule
    	if (getChild(rootNode, i) instanceof DefaultMutableTreeTableNode) {
    		DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode)getChild(rootNode, i);
    		if (defNode.getUserObject() instanceof SWRLRuleGroup) {
    			SWRLRuleGroup ruleGroup = (SWRLRuleGroup)defNode.getUserObject();
    			if (rule.getRuleGroupName().equals(ruleGroup.getGroupName())) {
    				defNode.add(new DefaultMutableTreeTableNode(rule));
    				existingGroupFound = true;
    			} // if
    		} // if
    	} // if
    } // for
		
    if (!existingGroupFound) {
    	DefaultMutableTreeTableNode groupNode = new DefaultMutableTreeTableNode(new SWRLRuleGroup(rule.getRuleGroupName(), true));
    	groupNode.add(new DefaultMutableTreeTableNode(rule));
    	rootNode.add(groupNode);
    } // if	
	} 
	
	public void addRules(Set<SWRLRule> rules) { for (SWRLRule rule : rules) addRule(rule); }
	public int getColumnCount() { return NumberOfColumns; }
	
	public String getColumnName(int column) 
	{
		String result = "";
		
		switch (column) {
		case RuleGroupColumn:
			result = "Group"; break;
		case IsEnabledColumn:
			result = "Enabled"; break;
		case RuleTextColumn:
			result = "Expression"; break;
		} // switch
			
		return result;
	}
	
	public Object getValueAt(Object node, int column) 
	{
		Object result = null;
			
		if (node instanceof DefaultMutableTreeTableNode) {
			DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode)node;
			if (defNode.getUserObject() instanceof SWRLRule) {
				SWRLRule rule = (SWRLRule)defNode.getUserObject();
				switch (column) {
				case IsEnabledColumn:
					result = rule.isEnabled(); break;
				case RuleTextColumn:
					result = rule.getRuleText(); break;
				} // switch
			} else if (defNode.getUserObject() instanceof SWRLRuleGroup) {
				SWRLRuleGroup ruleGroup = (SWRLRuleGroup)defNode.getUserObject();
				switch (column) {
				case RuleGroupColumn:
					result = ruleGroup.getGroupName(); break;
				case IsEnabledColumn:
					result = ruleGroup.getIsEnabled(); break;
				} // switch
			} // if
		} // if
		return result;
	}
	
	public boolean isCellEditable(Object node, int column) 
	{  
		boolean result = false;
			
		if (node instanceof DefaultMutableTreeTableNode) {
			DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode)node;
			if (defNode.getUserObject() instanceof SWRLRule) {
				//SWRLRule rule = (SWRLRule)defNode.getUserObject();
				switch (column) {
				case IsEnabledColumn:
				case RuleTextColumn:
					result = true; break;
				} // switch
			} else if (defNode.getUserObject() instanceof SWRLRuleGroup) {
				//SWRLRuleGroup ruleGroup = (SWRLRuleGroup)defNode.getUserObject();
				switch (column) {
				case RuleGroupColumn:
					result = true; break;
				case IsEnabledColumn:
					result = true; break;
				} // switch
			} // if
		}
		return result; 
	}
		
	public void setValueAt(Object value, Object node, int column) 
	{
		if (node instanceof DefaultMutableTreeTableNode) {
			DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode)node;
			if (defNode.getUserObject() instanceof SWRLRule) {
				SWRLRule rule = (SWRLRule)defNode.getUserObject();
				switch (column) {
				case IsEnabledColumn:
					System.err.println("rule enable toggled");
					rule.setEnabled((Boolean)value);
					defNode.setUserObject(rule);
					break;
				case RuleTextColumn:
					System.err.println("rule text changed: " + value);
					rule.setRuleText(value.toString());
					defNode.setUserObject(rule);
					break;
				} // switch
			} else if (defNode.getUserObject() instanceof SWRLRuleGroup) {
				SWRLRuleGroup ruleGroup = (SWRLRuleGroup)defNode.getUserObject();
				switch (column) {
				case RuleGroupColumn:
					System.err.println("rule group name changed: " + value);
					ruleGroup.setGroupName(value.toString());
					defNode.setUserObject(ruleGroup);
					break;
				case IsEnabledColumn:
					System.err.println("rule group enable toggled");
					ruleGroup.setIsEnabled((Boolean)value);
					defNode.setUserObject(ruleGroup);
					break;
				} // switch
			} // if
		} // if
	}

	public Class getColumnClass(int column) 
	{
		if (column == IsEnabledColumn) return Boolean.class;
		else return super.getColumnClass(column);
	} 
} 
