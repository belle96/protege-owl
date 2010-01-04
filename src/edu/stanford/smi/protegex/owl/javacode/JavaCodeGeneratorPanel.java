package edu.stanford.smi.protegex.owl.javacode;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.LabeledComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JavaCodeGeneratorPanel extends JPanel {

    private JCheckBox abstractCheckBox;

    private JTextField factoryClassNameTextField;

    private JFileChooser fileChooser = new JFileChooser(".");

    private EditableJavaCodeGeneratorOptions options;

    private JTextField packageTextField;

    private JTextField rootFolderTextField;

    private JCheckBox setCheckBox;

    private JCheckBox prefixCheckBox;


    public JavaCodeGeneratorPanel(EditableJavaCodeGeneratorOptions options) {

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

        factoryClassNameTextField = new JTextField();
        if (options.getFactoryClassName() != null) {
            factoryClassNameTextField.setText(options.getFactoryClassName());
        }

        abstractCheckBox = new JCheckBox("Create abstract base files (e.g., Person_)");
        abstractCheckBox.setSelected(options.getAbstractMode());

        setCheckBox = new JCheckBox("Return Set instead of Collection");
        setCheckBox.setSelected(options.getSetMode());

        prefixCheckBox = new JCheckBox("Include prefixes in generated Java names");
        prefixCheckBox.setSelected(options.getPrefixMode());
        
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
        add(new LabeledComponent("Factory class name", factoryClassNameTextField));
        add(Box.createVerticalStrut(8));
        add(createCheckBoxPanel(abstractCheckBox));
        add(Box.createVerticalStrut(8));
        add(createCheckBoxPanel(setCheckBox));
        add(Box.createVerticalStrut(8));
        add(createCheckBoxPanel(prefixCheckBox));
        add(Box.createVerticalStrut(8));
    }


    private JPanel createCheckBoxPanel(Component comp) {
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(BorderLayout.WEST, comp);
        southPanel.add(BorderLayout.CENTER, new JPanel());
        southPanel.setPreferredSize(new Dimension(300, 24));
        return southPanel;
    }


    public void ok() {
        File newFile = null;
        String rootFolder = rootFolderTextField.getText().trim();
        if (rootFolder.length() > 0) {
            newFile = new File(rootFolder);
        }
        options.setOutputFolder(newFile);

        options.setAbstractMode(abstractCheckBox.isSelected());
        options.setSetMode(setCheckBox.isSelected());
        options.setPrefixMode(prefixCheckBox.isSelected());
        options.setFactoryClassName(factoryClassNameTextField.getText());

        String pack = packageTextField.getText().trim();
        options.setPackage(pack.length() > 0 ? pack : null);
    }


    private void selectFolder() {
        if (fileChooser.showDialog(this, "Select") == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            rootFolderTextField.setText(file.toString());
        }
    }
}
