package edu.stanford.smi.protegex.owl.ui.menu.preferences;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.stanford.smi.protegex.owl.jena.writersettings.JenaWriterSettings;
import edu.stanford.smi.protegex.owl.jena.writersettings.WriterSettings;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.writer.rdfxml.util.ProtegeWriterSettings;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class WriterSettingsPanel extends JComponent {

    private OWLModel owlModel;

    private JRadioButton jenaButton;

    private JRadioButton protegeButton;

    private JCheckBox sortAlphabeticallyBox;

    private WriterSettings writerSettings;

    private JCheckBox useXMLEntitiesBox;


    public WriterSettingsPanel(OWLModel owlModel) {
        this.owlModel = owlModel;

        jenaButton = new JRadioButton("Default Jena writer");
        protegeButton = new JRadioButton("Native writer");

        writerSettings = owlModel.getWriterSettings();
        if (writerSettings instanceof JenaWriterSettings) {
            jenaButton.setSelected(true);
        }
        else {
            protegeButton.setSelected(true);
        }
        ButtonGroup group = new ButtonGroup();
        group.add(jenaButton);
        group.add(protegeButton);

        sortAlphabeticallyBox = new JCheckBox("Sort resources alphabetically");
        sortAlphabeticallyBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setSortAlphabetically(sortAlphabeticallyBox.isSelected());
            }
        });
        useXMLEntitiesBox = new JCheckBox("Use XML entities");
        useXMLEntitiesBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setUseXMLEntities(useXMLEntitiesBox.isSelected());
            }
        });

        jenaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setJenaWriterSettings();
            }
        });

        protegeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setProtegeWriterSettings();
            }
        });

        JPanel mainPanel = new JPanel();
        setBorder(BorderFactory.createTitledBorder("RDF/XML Writer Settings"));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(jenaButton);
        mainPanel.add(protegeButton);
        setLayout(new BorderLayout());

        Box protegePanel = Box.createVerticalBox();
        protegePanel.add(useXMLEntitiesBox);
        protegePanel.add(sortAlphabeticallyBox);

        updateProtegePanel();

        add(BorderLayout.WEST, mainPanel);
        add(BorderLayout.CENTER, new JPanel());
        add(BorderLayout.EAST, protegePanel);
    }


    private void setJenaWriterSettings() {
        owlModel.setWriterSettings(new JenaWriterSettings(owlModel));
        updateProtegePanel();
    }


    private void setProtegeWriterSettings() {
        owlModel.setWriterSettings(new ProtegeWriterSettings(owlModel));
        updateProtegePanel();
    }


    private void setSortAlphabetically(boolean selected) {
        ProtegeWriterSettings p = (ProtegeWriterSettings) owlModel.getWriterSettings();
        p.setSortAlphabetically(selected);
    }


    private void setUseXMLEntities(boolean selected) {
        ProtegeWriterSettings p = (ProtegeWriterSettings) owlModel.getWriterSettings();
        p.setUseXMLEntities(selected);
    }


    private void updateProtegePanel() {
        WriterSettings settings = owlModel.getWriterSettings();
        boolean enabled = settings instanceof ProtegeWriterSettings;
        if (enabled) {
            ProtegeWriterSettings p = (ProtegeWriterSettings) settings;
            sortAlphabeticallyBox.setSelected(p.isSortAlphabetically());
            useXMLEntitiesBox.setSelected(p.getUseXMLEntities());
        }
        sortAlphabeticallyBox.setEnabled(enabled);
        useXMLEntitiesBox.setEnabled(enabled);
    }
}
