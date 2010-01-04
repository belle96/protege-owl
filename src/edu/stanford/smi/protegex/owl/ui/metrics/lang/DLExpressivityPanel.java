package edu.stanford.smi.protegex.owl.ui.metrics.lang;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.util.DLExpressivityChecker;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLExpressivityPanel extends JPanel {

	private OWLModel owlModel;

	public DLExpressivityPanel(OWLModel owlModel) {
		this.owlModel = owlModel;
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout(12, 12));
		DLExpressivityChecker checker = new DLExpressivityChecker(owlModel);
		checker.check();
		JPanel holder = new JPanel(new BorderLayout(7, 7));
		holder.add(new JLabel("The DL expressivity of this ontology is:"), BorderLayout.NORTH);
		ArrayList list = new ArrayList(checker.getDL());
		DLNamePanel DLNamePanel = new DLNamePanel(list);
		DLNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		holder.add(DLNamePanel, BorderLayout.SOUTH);
		add(holder, BorderLayout.NORTH);
		JTable table = new JTable(new DLExpressivityTableModel(list));
		table.setRowHeight(55);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getColumnModel().getColumn(DLExpressivityTableModel.SYMBOL_COLUMN).setPreferredWidth(60);
		table.getColumnModel().getColumn(DLExpressivityTableModel.EXPLANATION_COLUMN).setPreferredWidth(600);
		table.getColumnModel().getColumn(DLExpressivityTableModel.EXPLANATION_COLUMN).setCellRenderer(new ExplanationRenderer());
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		add(new JScrollPane(table));
	}

	private class ExplanationRenderer implements TableCellRenderer {

		private JTextArea textArea;

		public ExplanationRenderer() {
			textArea = new JTextArea();
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
		}

		public Component getTableCellRendererComponent(JTable table,
		                                               Object value,
		                                               boolean isSelected,
		                                               boolean hasFocus,
		                                               int row,
		                                               int column) {
			textArea.setText(value != null ? value.toString() : "");
			return textArea;
		}
	}
}

