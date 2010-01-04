package edu.stanford.smi.protegex.owl.ui.dialogs;

import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protegex.owl.ui.search.ResourceListFinder;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SelectResourcesFromCollectionPanel extends JPanel {

    private JList list;


    public SelectResourcesFromCollectionPanel(Collection instances) {
        ArrayList slotList = new ArrayList(instances);
        Collections.sort(slotList, new FrameComparator());
        list = ComponentFactory.createList(edu.stanford.smi.protege.util.ModalDialog.getCloseAction(this));
        list.setListData(slotList.toArray());
        list.setCellRenderer(FrameRenderer.createInstance());
        setLayout(new BorderLayout());
        add(new JScrollPane(list), BorderLayout.CENTER);
        add(new ResourceListFinder(list, "Find"), BorderLayout.SOUTH);
        setPreferredSize(new Dimension(300, 300));
    }


    public Collection getSelection() {
        return ComponentUtilities.getSelection(list);
    }
}
