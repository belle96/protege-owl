package edu.stanford.smi.protegex.owl.ui.query;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.testing.constraints.SPARQLAssertTest;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SPARQLQueryPanel extends JPanel {

    private OWLModel owlModel;

    private JTextArea textArea;

    private static String TEXT = "SELECT ?subject ?object\nWHERE { ?subject rdfs:subClassOf ?object }";


    public SPARQLQueryPanel(OWLModel owlModel) {

        this.owlModel = owlModel;

        textArea = new JTextArea(TEXT);
        OWLLabeledComponent lc = new OWLLabeledComponent("Query", new JScrollPane(textArea));
        JButton queryButton = new JButton("Execute Query");
        queryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeQuery();
            }
        });

        lc.addHeaderButton(new AbstractAction("Assert matches for this query", OWLIcons.getImageIcon(OWLIcons.ASSERT_TRUE)) {
            public void actionPerformed(ActionEvent e) {
                handleAssertMatches(SPARQLAssertTest.NOT_EMPTY_PROPERTY_URI, SPARQLAssertTest.EMPTY_PROPERTY_URI);
            }
        });

        lc.addHeaderButton(new AbstractAction("Assert no matches for this query", OWLIcons.getImageIcon(OWLIcons.ASSERT_FALSE)) {
            public void actionPerformed(ActionEvent e) {
                handleAssertMatches(SPARQLAssertTest.EMPTY_PROPERTY_URI, SPARQLAssertTest.NOT_EMPTY_PROPERTY_URI);
            }
        });
        lc.setPreferredSize(new Dimension(300, 50));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, lc);
        add(BorderLayout.SOUTH, queryButton);
    }


    private boolean canAssert(String propertyURI) {
        String propertyName = owlModel.getResourceNameForURI(propertyURI);
        if (propertyName != null && owlModel.getRDFProperty(propertyName) != null) {
            if (ProtegeUI.getProjectView(owlModel.getProject()).getTabbedPane().getSelectedComponent() instanceof OWLClassesTab) {
                return true;
            }
            else {
                ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                        "You need to select a class in the OWLClasses tab\nbefore you can assert a query.");
                return false;
            }
        }
        else {
            if (owlModel instanceof JenaOWLModel) {
                if (ProtegeUI.getModalDialogFactory().showConfirmDialog(this,
                        "You need to import the following ontology before you\ncan use the assert queries support:\n" +
                                SPARQLAssertTest.URI + "\nShall this be added now?", "Missing import")) {
                    try {
                        owlModel.getNamespaceManager().setPrefix(SPARQLAssertTest.NAMESPACE, SPARQLAssertTest.PREFIX);
	                    ImportHelper importHelper = new ImportHelper((JenaOWLModel) owlModel);
	                    importHelper.addImport(URI.create(SPARQLAssertTest.URI));
	                    importHelper.importOntologies();
                    }
                    catch (Exception ex) {
                        ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel,
                                "Import failed: " + ex);
                    }
                }
            }
            return false;
        }
    }


    private void executeQuery() {
        SPARQLResultsPanel resultsPanel = SPARQLOWLModelAction.show(owlModel, false);
        String queryText = getQueryText();
        resultsPanel.executeQuery(queryText);
    }


    public String getQueryText() {
        return textArea.getText();
    }


    private void handleAssertMatches(String propertyURI, String inversePropertyURI) {
        if (canAssert(propertyURI)) {
            OWLClassesTab classesTab = (OWLClassesTab) ProtegeUI.getProjectView(owlModel.getProject()).getTabByClassName(OWLClassesTab.class.getName());
            RDFSNamedClass subject = classesTab.getSelectedClass();
            if (subject != null) {
                String propertyName = owlModel.getResourceNameForURI(propertyURI);
                RDFProperty property = owlModel.getRDFProperty(propertyName);
                String queryText = getQueryText();
                if (!subject.getPropertyValues(property).contains(queryText)) {
                    subject.addPropertyValue(property, queryText);
                }
                String inversePropertyName = owlModel.getResourceNameForURI(inversePropertyURI);
                RDFProperty inverseProperty = owlModel.getRDFProperty(inversePropertyName);
                if (inverseProperty != null) {
                    subject.removePropertyValue(inverseProperty, queryText);
                }
            }
        }
    }


    public void rememberQueryText() {
        TEXT = getQueryText();
    }


    public void setQueryText(String str) {
        textArea.setText(str);
    }
}
