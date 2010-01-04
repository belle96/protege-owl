package edu.stanford.smi.protegex.owl.ui.metadata;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.ui.widget.MultiWidgetPropertyWidget;

/**
 * A combined widget with name and documentation side by side.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 * @deprecated no longer encouraged
 */
public class NameDocumentationWidget extends MultiWidgetPropertyWidget {

    private AnnotationTextAreaWidget textAreaWidget;

    private RDFResourceNameWidget nameWidget;


    protected void createNestedWidgets() {

        nameWidget = new RDFResourceNameWidget();
        addNestedWidget(nameWidget, Model.Slot.NAME, "Name", "Name");

        textAreaWidget = new AnnotationTextAreaWidget(); //new OWLCommentsWidget();   
        
        addNestedWidget(textAreaWidget, RDFSNames.Slot.COMMENT, "Comment", "Comment");
    }


    public AnnotationTextAreaWidget getAnnotationTextAreaWidget() {
        return textAreaWidget;
    }


    protected void initAllPanel(JPanel allPanel, List widgets) {
        allPanel.setLayout(new BorderLayout());
        allPanel.add(BorderLayout.NORTH, nameWidget);
        allPanel.add(BorderLayout.CENTER, (Component) textAreaWidget);
    }


    public void initialize() {
        super.initialize();
        setPreferredColumns(2);
        setPreferredRows(2);
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return slot.getKnowledgeBase() instanceof OWLModel &&
                (slot.getName().equals(Model.Slot.NAME) ||
                        slot.getName().equals(RDFSNames.Slot.COMMENT));
    }
}
