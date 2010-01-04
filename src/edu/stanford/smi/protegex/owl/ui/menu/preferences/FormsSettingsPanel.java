package edu.stanford.smi.protegex.owl.ui.menu.preferences;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.project.SettingsMap;
import edu.stanford.smi.protegex.owl.ui.forms.AbsoluteFormsGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class FormsSettingsPanel extends JPanel {

    private OWLModel owlModel;

    private JCheckBox saveFormsBox;

    private JCheckBox saveAllBox;


    public FormsSettingsPanel(OWLModel owlModel) {
        this.owlModel = owlModel;

        setBorder(BorderFactory.createTitledBorder("Forms"));
        saveFormsBox = new JCheckBox("Save forms to .forms files");
        saveFormsBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSettingsMap();
            }
        });
        saveAllBox = new JCheckBox("Also save uncustomized forms");
        saveAllBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSettingsMap();
            }
        });

        String key = getSettingsMap().getString(AbsoluteFormsGenerator.SAVE_FORMS_KEY);
        if(AbsoluteFormsGenerator.MODIFIED.equals(key)) {
            saveFormsBox.setSelected(true);
            saveAllBox.setEnabled(true);
        }
        else if(AbsoluteFormsGenerator.ALL.equals(key)) {
            saveFormsBox.setSelected(true);
            saveAllBox.setSelected(true);
            saveAllBox.setEnabled(true);
        }
        else {
            saveAllBox.setEnabled(false);
        }

        setLayout(new GridLayout(2, 1));
        add(saveFormsBox);
        add(saveAllBox);
    }


    private SettingsMap getSettingsMap() {
        return owlModel.getOWLProject().getSettingsMap();
    }


    private void updateSettingsMap() {
        SettingsMap map = getSettingsMap();
        String value = null;
        boolean selected = saveFormsBox.isSelected();
        saveAllBox.setEnabled(selected);
        if(selected) {
            if(saveAllBox.isSelected()) {
                value = AbsoluteFormsGenerator.ALL;
            }
            else {
                value = AbsoluteFormsGenerator.MODIFIED;
            }
        }
        map.setString(AbsoluteFormsGenerator.SAVE_FORMS_KEY, value);
    }
}
