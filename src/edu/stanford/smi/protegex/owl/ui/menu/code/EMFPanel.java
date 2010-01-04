package edu.stanford.smi.protegex.owl.ui.menu.code;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.emf.EditableEMFGeneratorOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class EMFPanel extends JPanel {

    private JFileChooser fileChooser = new JFileChooser(".");

    private EditableEMFGeneratorOptions options;

    private JCheckBox overwriteCheckBox;

    private JTextField packageTextField;

    private JTextField rootFolderTextField;


    public EMFPanel(EditableEMFGeneratorOptions options) {

        this.options = options;

        packageTextField = new JTextField();
        if (options.getPackage() != null) {
            packageTextField.setText(options.getPackage());
        }
        rootFolderTextField = new JTextField();
        if (options.getOutputFolder() != null) {
            rootFolderTextField.setText(options.getOutputFolder().getAbsolutePath());
        }

        fileChooser.setDialogTitle("Select output folder");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        overwriteCheckBox = new JCheckBox("Overwrite all files");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        LabeledComponent lc = new LabeledComponent("Root output folder", rootFolderTextField);
        lc.addHeaderButton(new AbstractAction("Select folder...", Icons.getAddIcon()) {
            public void actionPerformed(ActionEvent e) {
                selectFolder();
            }
        });
        add(lc);
        add(Box.createVerticalStrut(8));
        add(new LabeledComponent("Java package", packageTextField));
        add(Box.createVerticalStrut(8));
        overwriteCheckBox.setPreferredSize(new Dimension(400, 24));
        add(overwriteCheckBox);
    }


    public boolean isOverwriteMode() {
        return overwriteCheckBox.isSelected();
    }


    public void ok() {
        File newFile = null;
        String rootFolder = rootFolderTextField.getText().trim();
        if (rootFolder.length() > 0) {
            newFile = new File(rootFolder);
        }
        options.setOutputFolder(newFile);

        String pack = packageTextField.getText().trim();
        options.setPackage(pack.length() > 0 ? pack : null);
    }


    private void selectFolder() {
        if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            rootFolderTextField.setText(file.toString());
        }
    }


    public void setOverwriteMode(boolean value) {
        overwriteCheckBox.setSelected(value);
    }
}
