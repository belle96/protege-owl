package edu.stanford.smi.protegex.owl.swrl.ui.table;

import java.util.Set;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLDataFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.OWLFactoryException;

public class SWRLRuleGroupTreeTableModel extends DefaultTreeTableModel 
{
	public static final int RuleGroupColumn = 0;
	public static final int IsEnabledColumn = 1;
	public static final int RuleNameColumn = 2;
	public static final int RuleTextColumn = 3;
	
	private OWLDataFactory owlFactory;
	
	private static final int NumberOfColumns = 4;
	private DefaultMutableTreeTableNode rootNode;
	
	public SWRLRuleGroupTreeTableModel(OWLDataFactory owlFactory) throws OWLFactoryException
	{ 
	  rootNode = new DefaultMutableTreeTableNode(new SWRLRuleGroup()); // Not visible; dummy rule group
			
	  setRoot(rootNode); 
	  
	  this.owlFactory = owlFactory;
	  
	  addRules(owlFactory.getSWRLRules());
	  	  
	} // SWRLRuleGroupTreeTableModel
	
	public void addRule(SWRLRule rule)
	{
      boolean existingGroupFound = false;
		
      // Find existing group or groups and add this rule
      for (int i = 0; i < rootNode.getChildCount(); i++) {
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
    	  DefaultMutableTreeTableNode groupNode = new DefaultMutableTreeTableNode(new SWRLRuleGroup("", true));
    	  groupNode.add(new DefaultMutableTreeTableNode(rule));
    	  rootNode.add(groupNode);
      } // if
			
	} // addRule
	
	public void addRules(Set<SWRLRule> rules) { for (SWRLRule rule : rules) addRule(rule); }
	
	public int getColumnCount() { return NumberOfColumns; }
	
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
				case RuleNameColumn:
					result = rule.getURI(); break;
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
	} // getValueAt
	
	public String getColumnName(int column) 
	{
		String result = "";
		
		switch (column) {
		case RuleGroupColumn:
			result = "Group"; break;
		case IsEnabledColumn:
			result = "Enabled"; break;
		case RuleNameColumn:
			result = "Name"; break;
		case RuleTextColumn:
			result = "Expression"; break;
		} // switch
			
		return result;
	} // getColumnName
	
	public boolean isCellEditable(Object node, int column) 
	{  
		boolean result = false;
			
		if (node instanceof DefaultMutableTreeTableNode) {
			DefaultMutableTreeTableNode defNode = (DefaultMutableTreeTableNode)node;
			if (defNode.getUserObject() instanceof SWRLRule) {
				//SWRLRule rule = (SWRLRule)defNode.getUserObject();
				switch (column) {
				case IsEnabledColumn:
				case RuleNameColumn:
				case RuleTextColumn:
					result = true; break;
				} // switch
			} else if (defNode.getUserObject() instanceof SWRLRuleGroup) {
				//SWRLRuleGroup ruleGroup = (SWRLRuleGroup)defNode.getUserObject();
				switch (column) {
				case RuleGroupColumn:
					result = false; break;
				case IsEnabledColumn:
					result = true; break;
				} // switch
			} // if
		}
		return result; 
	} // isCellEditable
	
	public Class getColumnClass(int column) 
	{
		if (column == IsEnabledColumn) return Boolean.class;
		else return super.getColumnClass(column);
	} // getColumnClass
	
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
					break;
				case RuleNameColumn:
					System.err.println("rule name changed: " + value);
					rule.setURI(value.toString()); 
					break;
				case RuleTextColumn:
					System.err.println("rule text changed: " + value);
					rule.setRuleText(value.toString()); 
					break;
				} // switch
			} else if (defNode.getUserObject() instanceof SWRLRuleGroup) {
				SWRLRuleGroup ruleGroup = (SWRLRuleGroup)defNode.getUserObject();
				switch (column) {
				case RuleGroupColumn:
					System.err.println("rule group name changed: " + value);
					ruleGroup.setGroupName(value.toString()); 
					break;
				case IsEnabledColumn:
					System.err.println("rule group enable toggled");
					ruleGroup.setIsEnabled((Boolean)value); 
					break;
				} // switch
			} // if
		} // if
	} // setValueAt
	
} // RuleGroupTreeTableModel
