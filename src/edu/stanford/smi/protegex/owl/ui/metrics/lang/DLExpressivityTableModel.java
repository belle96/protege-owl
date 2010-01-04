package edu.stanford.smi.protegex.owl.ui.metrics.lang;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLExpressivityTableModel extends AbstractTableModel {

	private List features;

	public static final int SYMBOL_COLUMN = 0;

	public static final int EXPLANATION_COLUMN = 1;

	public static final String [] COLUMNS = {"Symbol", "Explanation"};

	public DLExpressivityTableModel(List features) {
		this.features = features;
	}


	public int getRowCount() {
		return features.size();
	}


	public int getColumnCount() {
		return COLUMNS.length;
	}


	public Object getValueAt(int rowIndex,
	                         int columnIndex) {

		String s = (String) features.get(rowIndex);
		if(columnIndex == SYMBOL_COLUMN) {
			return ExpressivityIcons.getIcon(s);
		}
		else {
			return DLExpressivityExplanation.getExplanation(s);
		}
	}


	public String getColumnName(int column) {
		return COLUMNS[column];
	}


	public Class getColumnClass(int columnIndex) {
		if(columnIndex == SYMBOL_COLUMN) {
			return ImageIcon.class;
		}
		else {
			return String.class;
		}
	}
}

