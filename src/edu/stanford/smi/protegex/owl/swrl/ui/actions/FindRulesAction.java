package edu.stanford.smi.protegex.owl.swrl.ui.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;

import edu.stanford.smi.protege.model.FrameSlotCombination;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.swrl.ui.table.SWRLResultsPanel;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanelManager;

/**
 * An Action to search for SWRL rules for the selected frame.
 * This will show up in bottom area of the ResourceDisplay.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FindRulesAction extends AllowableAction {


    public FindRulesAction(Selectable selectable) {
        this(selectable, "Find rules about displayed resource");
    }


    public FindRulesAction(Selectable selectable, String text) {
        super(text, SWRLIcons.getImpIcon(), selectable);
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
                SWRLResultsPanel panel = new SWRLResultsPanel(findInstance);
                ResultsPanelManager.addResultsPanel(findInstance.getOWLModel(), panel, true);
            }
        }
    }
}
