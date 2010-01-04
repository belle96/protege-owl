package edu.stanford.smi.protegex.owl.ui.navigation;

import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A JPanel to drive the selection associated to a NavigationHistoryManager.
 * This is used in the main toolbar of Protege-OWL.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NavigationHistoryPanel extends JComponent {

    private JComboBox comboBox;

    private NavigationHistoryManager manager;


    public NavigationHistoryPanel(NavigationHistoryManager manager) {

        this.manager = manager;

        JToolBar toolBar = OWLUI.createToolBar();
        JButton backButton = ComponentFactory.addToolBarButton(toolBar, manager.getBackAction());
        manager.getBackAction().activateComboBox(backButton);
        JButton forwardButton = ComponentFactory.addToolBarButton(toolBar, manager.getForwardAction());
        manager.getForwardAction().activateComboBox(forwardButton);

        comboBox = new JComboBox(manager) {
            public Dimension getPreferredSize() {
                Dimension s = super.getPreferredSize();
                return new Dimension(200, s.height + 1);
            }
        };
        // comboBox.setEditable(true);
        comboBox.setRenderer(new ResourceRenderer());
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleComboBoxChange();
            }
        });
        manager.addIndexListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                comboBox.setSelectedIndex(NavigationHistoryPanel.this.manager.getSelectedIndex());
                comboBox.repaint();
            }
        });

        setLayout(new FlowLayout());
        add(toolBar);
        // add(comboBox);
    }


    private void handleComboBoxChange() {
        Object frame = comboBox.getSelectedItem();
        if (frame instanceof Frame) {
            manager.setSelectedItem((Frame) frame);
        }
    }
}
