package edu.stanford.smi.protegex.owl.ui.components.annotations;

import javax.swing.*;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 14, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class AnnotationsTableCellHolder extends JPanel {

    public static final Color LINE_COLOR = Color.LIGHT_GRAY;

    public static final Color TABLE_SELECTION_FOREGROUND = UIManager.getDefaults().getColor("Table.selectionForeground");

    public static final Color TABLE_FOREGROUND = UIManager.getDefaults().getColor("Table.foreground");

    public static final Color TABLE_SELECTION_BACKGROUND = UIManager.getDefaults().getColor("Table.selectionBackground");

    public static final Color TABLE_BACKGROUND = UIManager.getDefaults().getColor("Table.background");

    private JComponent component;


    public AnnotationsTableCellHolder(JComponent component, String location) {
        this.component = component;
        setBorder(BorderFactory.createEmptyBorder(0, 1, 1, 1));
        setOpaque(true);
        setLayout(new BorderLayout());
        if (component != null) {
            add(component, location);
        }
        setRequestFocusEnabled(true);
    }


    public void setColors(boolean selected, boolean focused) {
        if (selected) {
            setBackground(TABLE_SELECTION_BACKGROUND);
            setForeground(TABLE_SELECTION_FOREGROUND);
        }
        else {
            setBackground(TABLE_BACKGROUND);
            setForeground(TABLE_FOREGROUND);
        }
    }
}
