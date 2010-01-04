package edu.stanford.smi.protegex.owl.ui.cls;

import java.awt.BorderLayout;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.ModelUtilities;
import edu.stanford.smi.protege.resource.Colors;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.ClsesTreeTarget;
import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protege.util.DefaultRenderer;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protege.util.SelectableContainer;
import edu.stanford.smi.protege.util.SuperclassTraverser;
import edu.stanford.smi.protege.util.TreePopupMenuMouseListener;
import edu.stanford.smi.protege.util.WaitCursor;
import edu.stanford.smi.protegex.owl.model.Deprecatable;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.actions.ResourceActionManager;
import edu.stanford.smi.protegex.owl.ui.existential.ExistentialAction;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.search.finder.DefaultClassFind;
import edu.stanford.smi.protegex.owl.ui.search.finder.Find;
import edu.stanford.smi.protegex.owl.ui.search.finder.FindAction;
import edu.stanford.smi.protegex.owl.ui.search.finder.FindInDialogAction;
import edu.stanford.smi.protegex.owl.ui.search.finder.ResourceFinder;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * A SubclassPane optimized for OWLModels.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLSubclassPane extends SelectableContainer implements ClassTreePanel {

    private HierarchyManager hierarchyManager;

    public OWLModel owlModel;

    private ClassTree tree;

    private ResourceFinder finder;


    /**
     * @deprecated
     */
    @Deprecated
	public OWLSubclassPane(OWLModel owlModel, Action doubleClickAction,
                           Cls root, Action deleteClsAction) {
        this(owlModel, doubleClickAction, (RDFSNamedClass) root);
    }


    public OWLSubclassPane(OWLModel owlModel, Action doubleClickAction,
                           RDFSNamedClass rootClass) {
        this.owlModel = owlModel;
        tree = createSelectableTree(doubleClickAction, rootClass);
        tree.setLargeModel(true);
        tree.setSelectionRow(0);
        tree.setAutoscrolls(true);
        setSelectable(tree);
        setLayout(new BorderLayout());
        JScrollPane pane = ComponentFactory.createScrollPane(tree);
        add(BorderLayout.CENTER, pane);

        FindAction fAction = new FindInDialogAction(new DefaultClassFind(owlModel, Find.CONTAINS),
                                            Icons.getFindClsIcon(),
                                            tree, true);
        finder = new ResourceFinder(fAction);

        add(BorderLayout.SOUTH, finder);
        setupDragAndDrop();
        initializeTreeRenderer();
        
        getTree().addMouseListener(new TreePopupMenuMouseListener(tree) {
            @Override
			public JPopupMenu getPopupMenu() {
                return OWLSubclassPane.this.getPopupMenu();
            }
        });
    }


    protected JPopupMenu createPopupMenu() {
        Collection sel = getSelection();
        if (sel.size() == 1) {
            Cls cls = (Cls) sel.iterator().next();
            if (cls instanceof RDFSNamedClass) {
                JPopupMenu menu = new JPopupMenu();
                ResourceActionManager.addResourceActions(menu, this, (RDFResource) cls);
                if (cls instanceof OWLNamedClass) {
                    if (hierarchyManager != null) {
                        menu.addSeparator();
                        menu.add(new ExistentialAction(this, hierarchyManager, (OWLNamedClass) cls));
                    }
                }
                return menu;
            }
        }
        return null;
    }


    protected void initializeTreeRenderer() {
        /*
         * Optimization for client-server: 
         * In client mode, show only things that are not expensive to compute
         */
    	TreeCellRenderer renderer = owlModel.getProject().isMultiUserClient() ? 
    			getRemoteResourceRenderer() :
    			getLocalResourceRenderer();
    				
    	getTree().setCellRenderer(renderer);
    }
    
    
    protected ResourceRenderer getLocalResourceRenderer() {
    	return new ResourceRenderer(owlModel.getSystemFrames().getDirectSuperclassesSlot(), false);        	
    }
    
    protected ResourceRenderer getRemoteResourceRenderer() {
    	return new ResourceRenderer(owlModel.getSystemFrames().getDirectSuperclassesSlot(), false) {        	
        	@Override
        	protected void loadClsAfterIcon(Cls cls) {
                setMainText(cls.getBrowserText());
                appendText(getInstanceCountString(cls));
                setBackgroundSelectionColor(Colors.getClsSelectionColor());
                if (cls instanceof RDFSClass) {                                     
                    if (cls instanceof Deprecatable && ((Deprecatable) cls).isDeprecated()) {
                        addIcon(OWLIcons.getDeprecatedIcon());
                    }
                    loadedClass = (RDFSClass) cls;
                }
                else {
                    loadedClass = null;
                }
            }
        };
    }

    
    protected ClassTree createSelectableTree(Action doubleClickAction, Cls rootCls) {
        this.owlModel = (OWLModel) rootCls.getKnowledgeBase();
        LazyTreeRoot root = new ClassTreeRoot(rootCls, OWLUI.getSortClassTreeOption());
        return new ClassTree(doubleClickAction, root);
    }


    public void extendSelection(Cls cls) {
        ComponentUtilities.extendSelection(getTree(), cls);
    }


    public JComponent getDropComponent() {
        return getTree();
    }

    public ResourceFinder getFinder() {
        return finder;
    }

    private JPopupMenu getPopupMenu() {
        return createPopupMenu();
    }


    public JTree getTree() {
        return tree;
    }


    protected Cls pickConcreteCls(Collection allowedClses, String text) {
        RDFSNamedClass owlNamedClassMetaClass = owlModel.getOWLNamedClassClass();
        RDFSNamedClass rdfsNamedClassMetaClass = owlModel.getRDFSNamedClassClass();
        RDFSNamedClass metaClass = owlNamedClassMetaClass;
        boolean oldNamedVisible = owlNamedClassMetaClass.isVisible();
        boolean oldRDFSVisible = rdfsNamedClassMetaClass.isVisible();
        if (OWLUtil.hasRDFProfile(owlModel)) {
            metaClass = rdfsNamedClassMetaClass;
            rdfsNamedClassMetaClass.setVisible(true);
            owlNamedClassMetaClass.setVisible(true);
        }
        Cls result = ProtegeUI.getSelectionDialogFactory().selectClass(this, owlModel, Collections.singleton(metaClass), text);
        rdfsNamedClassMetaClass.setVisible(oldRDFSVisible);
        owlNamedClassMetaClass.setVisible(oldNamedVisible);
        return result;
    }


    public void removeSelection() {
        ComponentUtilities.removeSelection(getTree());
    }


    public void setHierarchyManager(HierarchyManager hierarchyManager) {
        this.hierarchyManager = hierarchyManager;
    }


    public void setSelectedClass(RDFSClass cls) {
        setSelectedClassDelegate(cls);
    }


    /**
     * @see #setSelectedClass
     * @deprecated
     */
    @Deprecated
	public void setSelectedCls(Cls cls) {
        setSelectedClassDelegate(cls);
    }

    
    protected void setSelectedClassDelegate(Cls cls) {
    	if (!getSelection().contains(cls) && !cls.isDeleted()) {
    		Collection path = ModelUtilities.getPathToRoot(cls);
    		setSelectedObjectPath(getTree(), path);
    	}
    }


    public void setSelectedClasses(Collection classes) {
        setSelectedClassesDelegate(classes);
    }


    protected void setSelectedClassesDelegate(Collection clses) {
        Collection paths = new ArrayList();
        Iterator i = clses.iterator();
        while (i.hasNext()) {
            Cls cls = (Cls) i.next();
            paths.add(ModelUtilities.getPathToRoot(cls));
        }
        ComponentUtilities.setSelectedObjectPaths(getTree(), paths);
    }


    /**
     * @see #setSelectedClasses
     * @deprecated
     */
    @Deprecated
	public void setSelectedClses(Collection clses) {
        setSelectedClassesDelegate(clses);
    }


    protected void setSelectedObjectPath(final JTree tree, Collection objectPath) {    	
        final TreePath path = ComponentUtilities.getTreePath(tree, objectPath);        
        
        if (path != null) {
        	final WaitCursor cursor = new WaitCursor(tree);       	
            tree.scrollPathToVisible(path);          
            tree.setSelectionPath(path);
            cursor.hide();
            tree.updateUI();
        }
    }




   protected void setupDragAndDrop() {
        if (OWLUI.isDragAndDropSupported(owlModel)) {
            final JTree tree = (JTree) getSelectable();
            DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(tree,
                                                                                 DnDConstants.ACTION_COPY_OR_MOVE, new OWLClassesTreeDragSourceListener());
            new DropTarget(tree, DnDConstants.ACTION_COPY_OR_MOVE, new ClsesTreeTarget());
        }
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


    public void setDisplayParent(Cls cls) {
        ComponentUtilities.setDisplayParent(getTree(), cls, new SuperclassTraverser());
    }


    @Override
	public String toString() {
        return "SubclassPane";
    }
}
