package edu.stanford.smi.protegex.owl.ui.search;

import edu.stanford.smi.protege.ui.ListFinder;

import javax.swing.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ResourceListFinder extends ListFinder {

    public ResourceListFinder(JList list, String description) {
        super(list, description);
    }


    public ResourceListFinder(JList list, String description, Icon icon) {
        super(list, description, icon);
    }
}
