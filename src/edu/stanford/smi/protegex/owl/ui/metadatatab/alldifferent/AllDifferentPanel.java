/*
 * Created on Aug 13, 2003
 */
package edu.stanford.smi.protegex.owl.ui.metadatatab.alldifferent;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;

import javax.swing.*;
import java.awt.*;

/**
 * A component to edit owl:AllDifferent blocks in the OWL ontology.
 * <p/>
 * The AllDifferentPanel contains two sub-components called
 * 'AllDifferentSelectionPanel' and 'AllDifferentEditorPanel'.
 * The first one represents all AllDifferent tags in the .owl file.
 * The second Panel shows all members which belong to an AllDifferent
 * tag selected in the 'AllDifferentSelectionPanel'.
 *
 * @author Daniel Stoeckli <stoeckli@smi.stanford.edu>
 */
public class AllDifferentPanel extends JPanel {


    public AllDifferentPanel(OWLModel owlModel) {

        AllDifferentEditorPanel editorPanel = new AllDifferentEditorPanel(owlModel);
        AllDifferentSelectionPanel selectionPanel = new AllDifferentSelectionPanel(owlModel, editorPanel);
        editorPanel.setAllDifferentMemberChangedListener(selectionPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, selectionPanel, editorPanel);

        setLayout(new BorderLayout());
        OWLLabeledComponent lc = new OWLLabeledComponent("owl:AllDifferent", splitPane, true, false);
        add(BorderLayout.CENTER, lc);
        splitPane.setDividerLocation(200);

        setPreferredSize(new Dimension(400, 500));
    }
}
