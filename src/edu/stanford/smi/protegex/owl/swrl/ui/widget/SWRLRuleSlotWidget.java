package edu.stanford.smi.protegex.owl.swrl.ui.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.widget.AbstractSlotWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParser;
import edu.stanford.smi.protegex.owl.swrl.ui.code.SWRLSymbolPanel;
import edu.stanford.smi.protegex.owl.swrl.ui.code.SWRLTextArea;

public class SWRLRuleSlotWidget extends AbstractSlotWidget {
	private static final String SWRL_RULE_LABEL = "SWRL Rule";
	private static final String SWRL_RULE_INVALID_LABEL = "SWRL Rule: invalid rule - not savable!";
	private static final String SWRL_RULE_INCOMPLETE_LABEL = "SWRL Rule: incomplete rule - not yet savable";
	
	private SWRLTextArea swrlTextArea;
	private LabeledComponent swrlTextAreaLabeledComponent;
	
	private String ruleExpressionInKb = new String();
    private SWRLParser parser;

    
	private FocusListener _focusListener = new FocusAdapter() {
		public void focusLost(FocusEvent event) {
			commitChanges();
		}
	};

	public void initialize() {
		buildGUI();

		setPreferredColumns(2);
		setPreferredRows(5);

        parser = new SWRLParser((OWLModel)getKnowledgeBase());
	}


	protected void buildGUI() {
		final OWLModel owlModel = (OWLModel) getKnowledgeBase();

		SWRLSymbolPanel symbolPanel = new SWRLSymbolPanel(owlModel, false, false) {
			@Override
			public void setErrorFlag(boolean error) {
				if (error) {
					setInvalidValueBorder();
				}
				super.setErrorFlag(error);
			}
		};
				
		
		swrlTextArea = new SWRLTextArea(owlModel, symbolPanel) {
		    protected void updateErrorDisplay() {
		        String uniCodeText = getText();		        
		        try {
		            checkUniCodeExpression(uniCodeText);
		            getErrorSymbolDisplay().displayError((Throwable) null);		            
		            swrlTextAreaLabeledComponent.setHeaderLabel(getWidgetIncompleteOrOKTitle());
		            setBackground(Color.white);
		            setNormalBorder();
		        }
		        catch (Throwable ex) {
		            getErrorSymbolDisplay().setErrorFlag(true);
		            getErrorSymbolDisplay().displayError(ex.getMessage());
		            swrlTextAreaLabeledComponent.setHeaderLabel(SWRL_RULE_INVALID_LABEL);
		            setBackground(new Color(240, 240, 240));
		            setInvalidValueBorder();
		        }
		    }
		};

		swrlTextArea.addFocusListener(_focusListener);
		//swrlTextArea.addKeyListener(_keyListener);

		symbolPanel.setSymbolEditor(swrlTextArea);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(swrlTextArea), BorderLayout.CENTER);
		panel.add(symbolPanel, BorderLayout.SOUTH);

		swrlTextAreaLabeledComponent = new LabeledComponent(SWRL_RULE_LABEL, panel, true);

		add(swrlTextAreaLabeledComponent);
	}

	protected String getWidgetIncompleteOrOKTitle() {
		String newRule = swrlTextArea.getText().trim();
                String oldRule = ruleExpressionInKb.trim();
		
                if (newRule.equals(oldRule)) return SWRL_RULE_LABEL;
                else return (parser.isCorrectAndIncomplete(newRule) ? SWRL_RULE_INCOMPLETE_LABEL : SWRL_RULE_LABEL);

	}
	
	@Override
	public void setInstance(Instance newInstance) {
		
		commitChanges();
		
		updateGUI((SWRLImp) newInstance);

		super.setInstance(newInstance);
	}


	protected void updateGUI(SWRLImp imp) {
		swrlTextAreaLabeledComponent.setHeaderLabel(SWRL_RULE_LABEL);

		ruleExpressionInKb = imp == null ? "" : imp.getBrowserText();		
		ruleExpressionInKb = SWRLTextArea.reformatText(ruleExpressionInKb);

		if (ruleExpressionInKb.equals(DefaultSWRLImp.EMPTY_RULE_TEXT)) swrlTextArea.setText("");
		else swrlTextArea.setText(ruleExpressionInKb);

		swrlTextArea.getErrorSymbolDisplay().displayError((Throwable) null);
		swrlTextArea.setBackground(Color.white);
		setNormalBorder();
	}


	public boolean commitChanges() {
		SWRLImp swrlimp = (SWRLImp) getInstance();
		
		if (swrlimp == null) {
			return true;
		}
		if (swrlimp.isDeleted()) {
		    return true;
		}

		try {			
			swrlimp.setExpression(swrlTextArea.getText());	
			ruleExpressionInKb = swrlTextArea.getText();
			
			swrlTextAreaLabeledComponent.setHeaderLabel(SWRL_RULE_LABEL);
		} catch (SWRLParseException e) {
			setInvalidValueBorder();
			swrlTextArea.getErrorSymbolDisplay().displayError(e);
			
			swrlTextAreaLabeledComponent.setHeaderLabel(SWRL_RULE_INVALID_LABEL);			
			return false;
		}
		return true;
	}	


	public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
		KnowledgeBase kb = cls.getKnowledgeBase();

		if (!(kb instanceof OWLModel)) {
			return false;
		}

		if (slot.getName().equals(SWRLNames.Slot.BODY)) {
			return true;
		}

		return false;
	}
	
	public String getSwrlTextAreaText() {
		return swrlTextArea.getText();
	}

	@Override
	public void dispose() {
		try {
			swrlTextArea.removeFocusListener(_focusListener);
		} catch (Throwable t) {
			//do nothing
		}
		
		parser=null;
		
		super.dispose();
	}
	
}
