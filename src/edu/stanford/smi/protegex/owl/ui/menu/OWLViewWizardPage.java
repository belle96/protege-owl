package edu.stanford.smi.protegex.owl.ui.menu;

import edu.stanford.smi.protege.util.Wizard;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.jena.OWLCreateProjectPlugin;
import edu.stanford.smi.protegex.owl.ui.cls.SwitchClassDefinitionResourceDisplayPlugin;
import edu.stanford.smi.protegex.owl.ui.cls.SwitchableClassDefinitionWidget;
import edu.stanford.smi.protegex.owl.ui.cls.SwitchableType;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLViewWizardPage extends WizardPage {

    private Map button2Type;

    private OWLCreateProjectPlugin plugin;

    private final static String HELP_TEXT =
            "<P>On this page you can specify the initial user interface settings for the OWL Classes Tab. " +
                    "The preferred user interface depends on the language features and experience. " +
                    "You can change these settings later at any time, at the bottom of the tab.</P>";


    public OWLViewWizardPage(Wizard wizard, OWLCreateProjectPlugin plugin) {
        super("View Settings", wizard);
        this.plugin = plugin;

        button2Type = new HashMap();
        boolean selected = false;
        JPanel viewPanel = new JPanel();
        String defaultClassView = SwitchClassDefinitionResourceDisplayPlugin.getDefaultClassView();
        ButtonGroup group = new ButtonGroup();
        Iterator<SwitchableType> types = SwitchableClassDefinitionWidget.listSwitchableTypes();
        while (types.hasNext()) {
            SwitchableType type = types.next();
            JRadioButton button = new JRadioButton(type.getButtonText());
            button2Type.put(button, type);
            String name = type.getClass().getName();
            if (name.equals(defaultClassView)) {
                button.setSelected(true);
                plugin.setDefaultClassView(type.getClass());
                selected = true;
            }
            group.add(button);
            viewPanel.add(button);
        }
        if (!selected) {
            JRadioButton first = (JRadioButton) viewPanel.getComponent(0);
            first.setSelected(true);
        }
        viewPanel.setLayout(new GridLayout(button2Type.size(), 1));
        viewPanel.setBorder(BorderFactory.createTitledBorder("OWL Classes View"));

        setLayout(new BorderLayout());

        add(BorderLayout.NORTH, viewPanel);
        add(BorderLayout.SOUTH, OWLUI.createHelpPanel(HELP_TEXT,
                "Do you prefer a less complex user interface?",
                OWLUI.WIZARD_HELP_HEIGHT));
    }


    public void onFinish() {
        for (Iterator it = button2Type.keySet().iterator(); it.hasNext();) {
            JRadioButton button = (JRadioButton) it.next();
            if (button.isSelected()) {
                SwitchableType type = (SwitchableType) button2Type.get(button);
                plugin.setDefaultClassView(type.getClass());
            }
        }
    }
}
