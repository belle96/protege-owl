package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protege.widget.StringListWidget;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * An StringListWidget that only allows to edit valid resource names.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 * @deprecated no longer really needed
 */
public class ResourceListWidget extends StringListWidget {

    protected void handleCreateAction() {
        String uri = ProtegeUI.getModalDialogFactory().showInputDialog(this,
                "Please enter a resource URI", "http://");
        if (uri != null) {
            if (getInstance().getDirectOwnSlotValues(getSlot()).contains(uri)) {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(this,
                        uri + " is already one of the values.",
                        "Duplicate value");
            }
            else {
                try {
                    new URI(uri);
                    getInstance().addOwnSlotValue(getSlot(), uri);
                }
                catch (URISyntaxException ex) {
                    ProtegeUI.getModalDialogFactory().showErrorMessageDialog(this,
                            uri + " is not a valid URI.",
                            "Invalid URI");
                }
            }
        }
    }


    protected void handleViewAction(String str) {
        String uri = JOptionPane.showInputDialog(ProtegeUI.getTopLevelContainer(getProject()),
                "Please enter a resource URI", str);
        if (uri != null && !uri.equals(str)) {
            try {
                new URI(uri);
                getInstance().removeOwnSlotValue(getSlot(), str);
                getInstance().addOwnSlotValue(getSlot(), uri);
            }
            catch (URISyntaxException ex) {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(this,
                        uri + " is not a valid URI.",
                        "Invalid URI");
            }
        }
    }
}
