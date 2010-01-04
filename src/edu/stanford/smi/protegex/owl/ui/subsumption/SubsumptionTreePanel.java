package edu.stanford.smi.protegex.owl.ui.subsumption;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.ModelUtilities;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.HeaderComponent;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.widget.AbstractTreeWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceActionManager;
import edu.stanford.smi.protegex.owl.ui.cls.ClassTree;
import edu.stanford.smi.protegex.owl.ui.cls.ClassTreePanel;
import edu.stanford.smi.protegex.owl.ui.cls.Hierarchy;
import edu.stanford.smi.protegex.owl.ui.cls.ToggleSuperclassExplorerAction;
import edu.stanford.smi.protegex.owl.ui.search.finder.*;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;
import edu.stanford.smi.protegex.owl.ui.widget.WidgetUtilities;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * A component that displays the computed or asserted subsumption relationship
 * between classes.
 *
 * @author Holger Knublauch   <holger@knublauch.com>
 */
public abstract class SubsumptionTreePanel extends SelectableContainer implements Hierarchy, ClassTreePanel {

    private HeaderComponent headerComponent;

    private LabeledComponent lc;

    private OWLModel owlModel;

    private final static int MAX_EXPANSIONS = 100;

    private Slot superclassesSlot;

    private Action viewAction;

    public static final String TYPE = "Subclass Explorer";


    public SubsumptionTreePanel(Cls root,
                                Slot subclassesSlot,
                                Slot superclassesSlot,
                                boolean useInferredSuperclasses) {
        this(new SubsumptionTreeRoot(root, subclassesSlot), superclassesSlot, useInferredSuperclasses);
    }

    public SubsumptionTreePanel(LazyTreeRoot treeRoot,
                                Slot superclassesSlot,
                                boolean useInferredSuperclasses) {
        this(treeRoot, superclassesSlot, useInferredSuperclasses,
             new DefaultClassFind((OWLModel) superclassesSlot.getKnowledgeBase(),
                                  Find.CONTAINS));
    }

    public SubsumptionTreePanel(LazyTreeRoot treeRoot,
                                Slot superclassesSlot,
                                boolean useInferredSuperclasses,
                                ResultsViewModelFind findAlg) {

        this.superclassesSlot = superclassesSlot;
        setPreferredSize(new Dimension(260, 400));
        setMinimumSize(new Dimension(100, 100));
        owlModel = (OWLModel) superclassesSlot.getKnowledgeBase();
        viewAction = createViewClsAction();
        ClassTree tree = createSelectableTree(viewAction, treeRoot);
        tree.setLargeModel(true);
        tree.setSelectionRow(0);
        tree.setAutoscrolls(true);
        setSelectable(tree);
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(tree), BorderLayout.CENTER);

        FindAction fAction = new FindInDialogAction(findAlg,
                                                    Icons.getFindClsIcon(),
                                                    tree, true);
        ResourceFinder finder = new ResourceFinder(fAction);

        mainPanel.add(finder, BorderLayout.SOUTH);
        finder.addButton(new ToggleSuperclassExplorerAction(this, useInferredSuperclasses));

        lc = new LabeledComponent(getTitle(), mainPanel, true);
        WidgetUtilities.addViewButton(lc, viewAction);

        tree.addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                updateActions();
            }
        });

        JLabel label = ComponentFactory.createLabel(owlModel.getProject().getName(), Icons.getProjectIcon(), SwingConstants.LEFT);
        headerComponent = new HeaderComponent("CLASS BROWSER", "For Project", label);

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, headerComponent);
        add(BorderLayout.CENTER, lc);
        tree.addMouseListener(new TreePopupMenuMouseListener(tree) {
            public JPopupMenu getPopupMenu() {
                return SubsumptionTreePanel.this.getPopupMenu();
            }
        });
    }


    protected JPopupMenu createPopupMenu(Cls cls) {
        JPopupMenu menu = new JPopupMenu();
        if (cls instanceof RDFResource) {
            ResourceActionManager.addResourceActions(menu, this, (RDFResource) cls);
        }
        return menu;
    }


    protected ClassTree createSelectableTree(Action viewAction, LazyTreeRoot root) {
        ClassTree tree = new ClassTree(viewAction, root);
        tree.setCellRenderer(new ResourceRenderer(superclassesSlot));
        return tree;
    }


    protected Action createViewClsAction() {
        return new ViewAction("View selected class", this) {
            public void onView(Object o) {
                owlModel.getProject().show((Cls) o);
            }
        };
    }


    public void expandRoot() {
        setExpandedCls(owlModel.getOWLThingClass(), true);
    }


    public void extendSelection(Cls cls) {
        ComponentUtilities.extendSelection(getTree(), cls);
    }


    public JTree getClsesTree() {
        return getTree();
    }


    public Cls getDisplayParent() {
        TreePath path = getTree().getSelectionModel().getLeadSelectionPath().getParentPath();
        LazyTreeNode node = (LazyTreeNode) path.getLastPathComponent();
        Object o = node.getUserObject();
        return (o instanceof Cls) ? (Cls) o : null;
    }


    public HeaderComponent getHeaderComponent() {
        return headerComponent;
    }


    private Action getHideClassAction() {
        return new AbstractAction("Hide class", Icons.getBlankIcon()) {
            public void actionPerformed(ActionEvent event) {
                Iterator i = getSelection().iterator();
                while (i.hasNext()) {
                    Cls cls = (Cls) i.next();
                    cls.setVisible(false);
                }
            }
        };
    }


    protected LabeledComponent getLabeledComponent() {
        return lc;
    }


    protected OWLModel getOWLModel() {
        return owlModel;
    }


    public List getPathToRoot(OWLNamedClass cls) {
        return getPathToRoot(cls, new LinkedList());
    }


    private List getPathToRoot(OWLNamedClass cls, LinkedList list) {
        list.add(0, cls);
        Cls rootCls = cls.getOWLModel().getOWLThingClass();
        Collection superclasses = ((Cls) cls).getDirectOwnSlotValues(superclassesSlot);
        for (Iterator it = superclasses.iterator(); it.hasNext();) {
            Cls superclass = (Cls) it.next();
            if (superclass.equals(rootCls)) {
                list.add(0, superclass);
                return list;
            }
            else if (cls.isVisible() && superclass instanceof OWLNamedClass) {
                getPathToRoot((OWLNamedClass) superclass, list);
                break;
            }
        }
        return list;
    }


    public Collection getPathsToRoot(OWLNamedClass cls) {
        Collection results = new ArrayList();
        getPathsToRoot(cls, new LinkedList(), results);
        return results;
    }


    private void getPathsToRoot(OWLNamedClass cls, List list, Collection lists) {
        list.add(0, cls);
        Cls rootCls = cls.getOWLModel().getOWLThingClass();
        Collection superclasses = ((Cls) cls).getDirectOwnSlotValues(superclassesSlot);
        for (Iterator it = superclasses.iterator(); it.hasNext();) {
            Cls superclass = (Cls) it.next();
            if (superclass.equals(rootCls)) {
                List copy = new ArrayList(list);
                copy.add(0, superclass);
                lists.add(copy);
            }
            else if (cls.isVisible() && superclass instanceof OWLNamedClass) {
                if (!list.contains(superclass)) {
                    List copy = new ArrayList(list);
                    getPathsToRoot((OWLNamedClass) superclass, copy, lists);
                }
            }
        }
    }


    private JPopupMenu getPopupMenu() {
        JPopupMenu menu = null;
        Collection selection = getSelection();
        if (selection.size() == 1) {
            Cls cls = (Cls) CollectionUtilities.getFirstItem(selection);
            menu = createPopupMenu(cls);
        }
        return menu;
    }


    public RDFSClass getSelectedClass() {
        return getSelectedCls();
    }


    OWLNamedClass getSelectedCls() {
        JTree tree = getTree();
        if (tree.getSelectionCount() == 1) {
            TreePath path = tree.getSelectionPath();
            SubsumptionTreeNode node = (SubsumptionTreeNode) path.getLastPathComponent();
            if (node.getCls() instanceof OWLNamedClass) {
                return (OWLNamedClass) node.getCls();
            }
        }
        return null;
    }


    public String getType() {
        return TYPE;
    }


    public JTree getTree() {
        return (JTree) getSelectable();
    }


    private Action getUnhideClassAction() {
        return new AbstractAction("Make class visible", Icons.getBlankIcon()) {
            public void actionPerformed(ActionEvent event) {
                Iterator i = getSelection().iterator();
                while (i.hasNext()) {
                    Cls cls = (Cls) i.next();
                    cls.setVisible(true);
                }
            }
        };
    }


    public boolean isDefaultSynchronized() {
        return true;
    }


    public void removeSelection() {
        ComponentUtilities.removeSelection(getTree());
    }


    public void setDisplayParent(Cls cls) {
        AbstractTreeWidget.setDisplayParent(getTree(), cls);
    }


    public void setExpandedCls(Cls cls, boolean expanded) {
        Collection path = ModelUtilities.getPathToRoot(cls);
        ComponentUtilities.setExpanded(getTree(), path, expanded);
    }


    public void setFinderComponent(JComponent c) {
        add(c, BorderLayout.SOUTH);
    }


    public void setRenderer(DefaultRenderer renderer) {
        getTree().setCellRenderer(renderer);
    }


    public void setSelectedClass(RDFSClass cls) {
    	OWLUI.setSelectedNodeInTree((SelectableTree) getTree(),cls);
    }


    public String toString() {
        return "SubsumptionTreePanel";
    }


    protected void updateActions() {
    }


    public static interface SuperClsesPanel {

        void setCls(OWLNamedClass cls);
    }
}
