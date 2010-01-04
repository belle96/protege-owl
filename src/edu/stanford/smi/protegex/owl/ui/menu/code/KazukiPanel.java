package edu.stanford.smi.protegex.owl.ui.menu.code;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.LabeledComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class KazukiPanel extends JPanel {

    private JFileChooser fileChooser = new JFileChooser(".");

    private JTextField javaCTextField;

    private JCheckBox overwriteCheckBox;

    private JTextField packageTextField;

    private JTextField rootFolderTextField;


    public KazukiPanel() {
        packageTextField = new JTextField();
        rootFolderTextField = new JTextField("kazuki");
        javaCTextField = new JTextField();

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
        add(new LabeledComponent("Base Java package", packageTextField));
        add(Box.createVerticalStrut(8));
        add(new LabeledComponent("Java Compiler binary", javaCTextField));
        add(Box.createVerticalStrut(8));
        overwriteCheckBox.setPreferredSize(new Dimension(400, 24));
        add(overwriteCheckBox);
    }


    public String getJavaC() {
        return javaCTextField.getText();
    }


    public String getPackage() {
        return packageTextField.getText();
    }


    public String getRootFolder() {
        return rootFolderTextField.getText();
    }


    public boolean isOverwriteMode() {
        return overwriteCheckBox.isSelected();
    }


    private void selectFolder() {
        if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            rootFolderTextField.setText(file.toString());
        }
    }


    public void setJavaC(String javac) {
        javaCTextField.setText(javac);
    }


    public void setOverwriteMode(boolean value) {
        overwriteCheckBox.setSelected(value);
    }


    public void setPackage(String packageName) {
        packageTextField.setText(packageName);
    }


    public void setRootFolder(String folder) {
        rootFolderTextField.setText(folder);
    }
}
