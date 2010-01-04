package edu.stanford.smi.protegex.owl.ui.jena;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.LabeledComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JenaSchemagenPanel extends JPanel {

    private JFileChooser fileChooser = new JFileChooser(".");

    private JTextField fileTextField;

    private JTextField packageTextField;


    public JenaSchemagenPanel() {
        packageTextField = new JTextField();
        fileTextField = new JTextField();

        fileChooser.setDialogTitle("Select output Java file");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        LabeledComponent lc = new LabeledComponent("Output Java file", fileTextField);
        lc.addHeaderButton(new AbstractAction("Select file...", Icons.getAddIcon()) {
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });
        add(lc);
        add(Box.createVerticalStrut(8));
        add(new LabeledComponent("Java package", packageTextField));
        //setMinimumSize(new Dimension(400, 150));
    }


    public String getFileName() {
        return fileTextField.getText();
    }


    public String getPackage() {
        return packageTextField.getText();
    }


    public void setFileName(String fileName) {
        fileTextField.setText(fileName);
    }


    public void setPackage(String packageName) {
        packageTextField.setText(packageName);
    }


    private void selectFile() {
        if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileTextField.setText(file.toString());
        }
    }
}
