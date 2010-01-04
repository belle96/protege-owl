package edu.stanford.smi.protegex.owl.swrl.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.ui.code.SWRLTextAreaPanel;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * A ResourceAction to edit a SWRLImp in a multi-line editor.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class EditRuleAction extends ResourceAction {

    public EditRuleAction() {
        super("Edit rule in multi-line editor...",
                OWLIcons.getViewIcon(SWRLIcons.IMP, SWRLIcons.class));
    }


    public void actionPerformed(ActionEvent e) {
        SWRLImp oldImp = (SWRLImp) getResource();
        SWRLTextAreaPanel.showEditDialog(getComponent(), getOWLModel(), oldImp);
    }


    public boolean isSuitable(Component component, RDFResource resource) {
        return resource instanceof SWRLImp && resource.isEditable();
    }
}
