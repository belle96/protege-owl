package edu.stanford.smi.protegex.owl.ui.clsdesc.manchester;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class SuggestionList extends JList {

    private MouseListener mouseListener;

    private SuggestionPopup popup;


    public SuggestionList(SuggestionPopup popup) {
        this.popup = popup;
        mouseListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    performSelectedSuggestion();
                }
            }
        };
        if (System.getProperty("os.name").indexOf("Mac") != -1) {
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }
        else {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2))
            );
        }
        addMouseListener(mouseListener);
        setCellRenderer(new SuggestionRenderer());
    }


    private void performSelectedSuggestion() {
        Suggestion suggestion = (Suggestion) getSelectedValue();
        if (suggestion != null) {
            suggestion.performSuggestion();
            popup.updateCurrentEditorPane();
            popup.reset();
        }
    }
}


