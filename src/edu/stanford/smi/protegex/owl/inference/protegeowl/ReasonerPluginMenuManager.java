package edu.stanford.smi.protegex.owl.inference.protegeowl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import edu.stanford.smi.protegex.owl.model.OWLModel;

public class ReasonerPluginMenuManager {
	public final static String NONE_REASONER = "None";

	public static void fillReasoningMenu(OWLModel owlModel, JMenu reasoningMenu) {
		Map<String, String> name2ClassName = ReasonerPluginManager.getSuitableReasonerMap(owlModel);
		
		if (name2ClassName.size() == 0) {
			return;
		}
		
		ArrayList<String> names = new ArrayList<String>(name2ClassName.keySet());
		Collections.sort(names);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		buttonGroup.add(addMenuItem(owlModel, reasoningMenu, NONE_REASONER, null));
		
		for (String name : names) {
			buttonGroup.add(addMenuItem(owlModel, reasoningMenu, name, name2ClassName.get(name)));
		}
		
	}

	private static JRadioButtonMenuItem addMenuItem(final OWLModel owlModel, JMenu reasoningMenu, String name, final String className) {
		final JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(name);
		
		String defaultReasonerClassName = ReasonerManager.getInstance().getDefaultReasonerClassName();
		
		if (defaultReasonerClassName.equals(className)) {
			ReasonerManager.getInstance().setProtegeReasonerClass(owlModel, className);
			menuItem.setSelected(true);
		}
		
		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (menuItem.isSelected()) {
					ReasonerManager.getInstance().setDefaultReasonerClass(className);
					ReasonerManager.getInstance().setProtegeReasonerClass(owlModel, className);
				}				
			}			
		});
		
		reasoningMenu.add(menuItem);
		
		return menuItem;
	}
	
}
