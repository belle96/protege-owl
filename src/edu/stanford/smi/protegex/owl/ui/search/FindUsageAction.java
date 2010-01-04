package edu.stanford.smi.protegex.owl.ui.search;

import edu.stanford.smi.protege.model.FrameSlotCombination;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;

import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindUsageAction extends AllowableAction {


    public FindUsageAction(Selectable selectable) {
        this(selectable, "Find usage of this resource");
    }


    public FindUsageAction(Selectable selectable, String text) {
        super(text, OWLIcons.getFindUsageIcon(), selectable);
    }


    public void actionPerformed(ActionEvent e) {
        Collection sel = getSelection();
        if (sel.size() == 1) {
            Object next = sel.iterator().next();
            RDFResource findInstance = null;
            if (next instanceof RDFResource) {
                findInstance = (RDFResource) sel.iterator().next();
            }
            else if (next instanceof FrameSlotCombination) {
                FrameSlotCombination c = (FrameSlotCombination) next;
                if (c.getSlot() instanceof RDFResource) {
                    findInstance = (RDFResource) c.getSlot();
                }
            }
            if (findInstance != null) {
                FindUsagePanel panel = FindUsagePanel.create(findInstance);
                ResultsPanelManager.addResultsPanel(findInstance.getOWLModel(), panel, true);
            }
        }
    }
}
