package edu.stanford.smi.protegex.owl.ui.conditions;

import java.awt.Color;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.classparser.ParserUtils;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Nov 25, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class PropertySuggestionPopup {

	private JList propertyList;

	private JWindow popupWindow;

	private ConditionsTable table;

	private JPopupMenu popupMenu;

	public static final int POPUP_WIDTH = 220;

	public static final String POPUP_TITLE = "Suggested properties";

	public PropertySuggestionPopup(Window owner, ConditionsTable table) {
		propertyList = new JList();
		propertyList.setCellRenderer(new ResourceRenderer());
		propertyList.setRequestFocusEnabled(false);
		propertyList.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(e.getClickCount() == 2) {
					insertPropertyName();
				}
			}
		});
		popupMenu = new JPopupMenu();
		popupMenu.add(new AbstractAction("Close") {
			public void actionPerformed(ActionEvent e) {
				popupWindow.setVisible(false);
			}
		});
		popupWindow = new JWindow(owner);
		popupWindow.setFocusableWindowState(false);
		popupWindow.setFocusable(false);
		final LabeledComponent lc = new LabeledComponent(POPUP_TITLE, new JScrollPane(propertyList));
		lc.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
		                                                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		lc.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger()) {
					popupMenu.show(lc, e.getX(), e.getY());
				}
			}


			public void mousePressed(MouseEvent e) {
				if(e.isPopupTrigger()) {
					popupMenu.show(lc, e.getX(), e.getY());
				}
			}
		});
		popupWindow.setContentPane(lc);
		this.table = table;
	}

	/**
	 * Shows a popup that appears next to the conditions
	 * widget table.  The popup displays properties that
	 * have the class being displayed in their domain.
	 * @param cls The class being displayed by the conditions
	 * widget.
	 */
	public void showPopup(OWLNamedClass cls) {
		Set properties = cls.getAssociatedProperties();
		if(properties.size() > 0) {
			propertyList.setListData(properties.toArray());
			popupWindow.setSize(POPUP_WIDTH, table.getParent().getHeight());
			Point p = table.getLocation();
			SwingUtilities.convertPointToScreen(p, table);
			popupWindow.setLocation(p.x - popupWindow.getWidth() - 10, p.y);
			popupWindow.setVisible(true);
		}
	}

	public void hidePopup() {
		popupWindow.setVisible(false);
	}

	protected void insertPropertyName() {
		RDFProperty selProperty = (RDFProperty) propertyList.getSelectedValue();
		if(selProperty != null) {
			JTextComponent textComponent = table.getSymbolEditorComponent().getTextComponent();
			try {
				textComponent.getDocument().insertString(textComponent.getCaretPosition(), 
				                                         selProperty.getBrowserText(), null);
			}
			catch(BadLocationException e) {
                          Log.getLogger().log(Level.SEVERE, "Exception caught", e);
			}
		}
	}


}

