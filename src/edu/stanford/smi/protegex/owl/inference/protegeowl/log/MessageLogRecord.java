package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 22, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class MessageLogRecord extends ReasonerLogRecord {

    private JLabel label;

    private String message;

    private RDFResource cause;


    public MessageLogRecord(RDFResource cause, String message, ReasonerLogRecord parent) {
        // Need to refactor this and tidy it up!
        super(parent);

        this.message = message;

        this.cause = cause;

        label = new JLabel();

        label.setVerticalAlignment(SwingConstants.TOP);

        label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }


    protected JLabel getJLabel() {
        return label;
    }


    public String getHTMLMessage() {
        String htmlMessage = "";

        if (message != null) {
        	htmlMessage = message.replaceAll("[\n]", "<br>");
        }

        return htmlMessage;
    }


    public String getMessage() {
        return message;
    }


    /**
     * Gets the cause of this warning.
     */
    public RDFResource getCause() {
        return cause;
    }


    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     *
     * @param list         The JList we're painting.
     * @param value        The value returned by list.getModel().getElementAt(index).
     * @param index        The cells index.
     * @param isSelected   True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     * @see javax.swing.JList
     * @see javax.swing.ListSelectionModel
     * @see javax.swing.ListModel
     */
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

