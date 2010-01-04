package edu.stanford.smi.protegex.owl.ui;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.ui.icons.OverlayIcon;
import edu.stanford.smi.protegex.owl.ui.resourceselection.ResourceSelectionAction;
import edu.stanford.smi.protegex.owl.ui.widget.WidgetUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLLabeledComponent extends LabeledComponent {


    public OWLLabeledComponent(String label, Component c) {
        super(label, c);
    }


    public OWLLabeledComponent(String label, Component c, boolean verticallyStretchable, boolean swappedHeader) {
        super(label, c, verticallyStretchable, swappedHeader);
    }


    public OWLLabeledComponent(String label, JScrollPane c) {
        super(label, c);
    }


    public JButton addHeaderButton(Action action) {
        JButton button = super.addHeaderButton(action);
        Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
        if (icon instanceof OverlayIcon) {
            button.setDisabledIcon(((OverlayIcon) icon).getGrayedIcon());
        }
        if (action instanceof ResourceSelectionAction) {
            ((ResourceSelectionAction) action).activateComboBox(button);
        }
        return button;
    }


    public void insertHeaderComponent(Component component, int index) {
        JToolBar toolBar = WidgetUtilities.getJToolBar(this);
        toolBar.add(component, index);
    }
}
