package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.inference.ui.icons.InferenceIcons;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 23, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DefaultConceptConsistencyLogRecord extends ReasonerLogRecord {

    private RDFSClass aClass;

    private boolean consistent;

    private JLabel label;


    public DefaultConceptConsistencyLogRecord(RDFSClass aClass, boolean consistent, ReasonerLogRecord parent) {
        super(parent);

        this.aClass = aClass;

        this.consistent = consistent;

        label = new JLabel();


        String colour;

        if (consistent == true) {
            colour = "009900";

            label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GREEN)));

            label.setIcon(InferenceIcons.getInformationMessageIcon());
        }
        else {
            colour = "ff0000";

            label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2),
                    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED)));


            label.setIcon(OWLIcons.getOWLTestErrorIcon());
        }

        label.setText("<html><body color=\"" + colour + "\">" +
                getMessage() +
                "</body></html>");


    }


    public RDFSClass getInconsistentConcept() {
        return aClass;
    }


    public String getMessage() {
        String message = aClass.getBrowserText();

        if (consistent == true) {
            message += " is consistent";
        }
        else {
            message += " is inconsistent";
        }

        return message;
    }


    public String toString() {
        return getMessage();
    }


    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        return label;
    }


    /**
     * Sets the value of the current tree cell to <code>value</code>.
     * If <code>selected</code> is true, the cell will be drawn as if
     * selected. If <code>expanded</code> is true the node is currently
     * expanded and if <code>leaf</code> is true the node represets a
     * leaf and if <code>hasFocus</code> is true the node currently has
     * focus. <code>tree</code> is the <code>JTree</code> the receiver is being
     * configured for.  Returns the <code>Component</code> that the renderer
     * uses to draw the value.
     *
     * @return the <code>Component</code> that the renderer uses to draw the value
     */
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        return label;
    }
}

