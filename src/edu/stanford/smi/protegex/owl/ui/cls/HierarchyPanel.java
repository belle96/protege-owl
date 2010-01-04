package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.ui.HeaderComponent;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.resourcedisplay.ResourceDisplay;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A JPanel holding a Hierarchy.  This provides some buttons to manage the
 * hierarchy within the OWLClassesTab.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class HierarchyPanel extends JPanel implements Disposable {

    private Action spawnAction = new AbstractAction("Spawn off cloned window",
            OWLIcons.getImageIcon("SpawnHierarchy")) {
        public void actionPerformed(ActionEvent e) {
            spawn();
        }
    };

    protected Hierarchy hierarchy;

    protected HierarchyManager hierarchyManager;

    private edu.stanford.smi.protegex.owl.ui.navigation.NavigationHistoryManager navigationHistoryManager;

    private Hierarchy nestedHierarchy;

    private JSplitPane nestedSplitPane;

    protected JToolBar toolBar;


    public HierarchyPanel(Hierarchy hierarchy,
                          HierarchyManager hierarchyManager,
                          boolean withNavigationHistory,
                          OWLModel owlModel) {

        this.hierarchy = hierarchy;
        this.hierarchyManager = hierarchyManager;

        if (withNavigationHistory) {
            navigationHistoryManager = new edu.stanford.smi.protegex.owl.ui.navigation.NavigationHistoryManager(hierarchy, owlModel);
            navigationHistoryManager.add(owlModel.getOWLThingClass());
        }

        setLayout(new BorderLayout(0, 1));

        toolBar = OWLUI.createToolBar(); // SwingConstants.RIGHT, new Dimension(16, 16));
        // toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

        HeaderComponent hc = hierarchy.getHeaderComponent();

        if (withNavigationHistory) {
            JButton backButton = ComponentFactory.addToolBarButton(toolBar,
                    navigationHistoryManager.getBackAction(), ResourceDisplay.SMALL_BUTTON_WIDTH);
            backButton.setOpaque(false);
            navigationHistoryManager.getBackAction().activateComboBox(backButton);
            JButton forwardButton = ComponentFactory.addToolBarButton(toolBar,
                    navigationHistoryManager.getForwardAction(), ResourceDisplay.SMALL_BUTTON_WIDTH);
            forwardButton.setOpaque(false);
            navigationHistoryManager.getForwardAction().activateComboBox(forwardButton);
            toolBar.addSeparator();
        }
        ComponentFactory.addToolBarButton(toolBar, spawnAction, ResourceDisplay.SMALL_BUTTON_WIDTH).setOpaque(false);
        Container hp = (Container) hc.getComponent(0);
        toolBar.setBackground(hp.getBackground());
        hp.add(BorderLayout.EAST, toolBar);

        hc.setTitle(hierarchy.getType());
        add(BorderLayout.CENTER, (Component) hierarchy);

        setMinimumSize(new Dimension(40, 40));
    }


    public void dispose() {
        if (navigationHistoryManager != null) {
            navigationHistoryManager.dispose();
        }
    }


    public Hierarchy getHierarchy() {
        return hierarchy;
    }


    public Hierarchy getNestedHierarchy() {
        return nestedHierarchy;
    }


    public String getTitle() {
        return hierarchy.getTitle();
    }


    public boolean isSynchronized() {
        return true;
    }


    public void setNestedHierarchy(Hierarchy newNestedHierarchy) {
        if (newNestedHierarchy != null) {
            Component top = (Component) hierarchy;
            int height = top.getHeight();
            Component bottom = (Component) newNestedHierarchy;
            nestedSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
            removeAll();
            add(BorderLayout.CENTER, nestedSplitPane);
            revalidate();
            nestedSplitPane.setDividerLocation(height - 150);
            nestedSplitPane.setResizeWeight(1);
            revalidate();
            newNestedHierarchy.addSelectionListener(new SelectionListener() {
                public void selectionChanged(SelectionEvent event) {
                    RDFSClass sel = nestedHierarchy.getSelectedClass();
                    if (sel != null) {
                        hierarchy.setSelectedClass(sel);
                    }
                }
            });
        }
        else {
            if (nestedSplitPane != null) {
                ComponentUtilities.dispose(nestedSplitPane.getBottomComponent());
                removeAll();
                add(BorderLayout.CENTER, (Component) hierarchy);
                nestedSplitPane = null;
                revalidate();
            }
        }
        this.nestedHierarchy = newNestedHierarchy;
    }


    private void spawn() {
        Hierarchy clonedHierarchy = hierarchy.createClone();
        hierarchyManager.spawnHierarchy(clonedHierarchy);
    }
}
