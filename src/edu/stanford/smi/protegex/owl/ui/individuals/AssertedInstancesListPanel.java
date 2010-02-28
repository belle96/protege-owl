package edu.stanford.smi.protegex.owl.ui.individuals;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ListModel;

import edu.stanford.smi.protege.action.DeleteInstancesAction;
import edu.stanford.smi.protege.action.MakeCopiesAction;
import edu.stanford.smi.protege.action.ReferencersAction;
import edu.stanford.smi.protege.event.ClsAdapter;
import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protege.event.ClsListener;
import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.model.BrowserSlotPattern;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.SimpleInstance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.Transaction;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protege.resource.Colors;
import edu.stanford.smi.protege.resource.LocalizedText;
import edu.stanford.smi.protege.resource.ResourceKey;
import edu.stanford.smi.protege.server.framestore.RemoteClientFrameStore;
import edu.stanford.smi.protege.ui.BrowserTextListFinder;
import edu.stanford.smi.protege.ui.ConfigureAction;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.ui.HeaderComponent;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protege.util.CreateAction;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.FrameWithBrowserText;
import edu.stanford.smi.protege.util.FrameWithBrowserTextComparator;
import edu.stanford.smi.protege.util.GetInstancesAndBrowserTextJob;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protege.util.ModalDialog;
import edu.stanford.smi.protege.util.SelectableContainer;
import edu.stanford.smi.protege.util.SimpleListModel;
import edu.stanford.smi.protege.util.StringUtilities;
import edu.stanford.smi.protege.util.ViewAction;
import edu.stanford.smi.protegex.owl.model.NamespaceUtil;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * The panel that holds the list of direct instances of one or more classes. If
 * only one class is chosen then you can also create new instances of this
 * class.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 * @author Ray Fergerson <fergerson@smi.stanford.edu>
 */
public class AssertedInstancesListPanel extends SelectableContainer implements Disposable {

	private OWLModel owlModel;
    private Collection<Cls> classes = Collections.emptyList();

    private AllowableAction createAction;
    private AllowableAction createAnonymousAction;
    private AllowableAction copyAction;
    private AllowableAction deleteAction;

    private HeaderComponent header;
    private OWLLabeledComponent lc;
    private InstancesList list;

    private Collection<Instance> listenedToInstances = new ArrayList<Instance> ();
    private boolean showSubclassInstances;

    private static final int SORT_LIMIT;
    static {
    	//Not applicable anymore - instances are always sorted when displayed.
    	//If in client-server mode, the instances are sorted on the server
    	//and they are retrieved by a client using a Protege Job
        SORT_LIMIT = ApplicationProperties.getIntegerProperty("ui.DirectInstancesList.sort_limit", 1000);
    }

    private ClsListener _clsListener = new ClsAdapter() {
        @Override
		public void directInstanceAdded(ClsEvent event) {
        	if (event.isReplacementEvent()) { return; }
            Instance instance = event.getInstance();
            FrameWithBrowserText fbt = new FrameWithBrowserText(instance, instance.getBrowserText(), instance.getDirectTypes());
			if (!getModel().contains(fbt)) {
                ComponentUtilities.addListValue(list, fbt);
                instance.addFrameListener(_instanceFrameListener);
            }
        }

        @Override
		public void directInstanceRemoved(ClsEvent event) {
        	if (event.isReplacementEvent()) { return; }
        	removeInstanceListener(event.getInstance());
        	ComponentUtilities.removeListValue(list, new FrameWithBrowserText(event.getInstance()));            
        }
        
        
    };

    private FrameListener _clsFrameListener = new FrameAdapter() {
        @Override
		public void ownSlotValueChanged(FrameEvent event) {
            super.ownSlotValueChanged(event);
            updateButtons();
        }       
    };

    private FrameListener _instanceFrameListener = new FrameAdapter() {
        @Override
		public void browserTextChanged(FrameEvent event) {
        	Frame frame = event.getFrame();
        	ComponentUtilities.replaceListValue(list, 
        			new FrameWithBrowserText(frame), 
        			new FrameWithBrowserText(frame, frame.getBrowserText(), ((Instance)frame).getDirectTypes()));
            repaint();
        }

        @Override
        public void frameReplaced(FrameEvent event) {        	
        	Instance oldInst = (Instance) event.getFrame();
        	Instance newInst = (Instance) event.getNewFrame();
        	
        	ComponentUtilities.replaceListValue(list, 
        			new FrameWithBrowserText(oldInst, oldInst.getBrowserText(), oldInst.getDirectTypes()), 
        			new FrameWithBrowserText(newInst, newInst.getBrowserText(), newInst.getDirectTypes()));       	 
        }
    };


    public AssertedInstancesListPanel(OWLModel owlModel) {
        this.owlModel = owlModel;

        Action viewAction = createViewAction();
        list = new InstancesList(viewAction);
        createListRenderer();
        lc = new OWLLabeledComponent(null, ComponentFactory.createScrollPane(list));
        addButtons(viewAction, lc);
        lc.setFooterComponent(new BrowserTextListFinder(list, ResourceKey.INSTANCE_SEARCH_FOR));

        lc.setBorder(ComponentUtilities.getAlignBorder());
        add(lc, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createHeader(), BorderLayout.NORTH);
        add(panel, BorderLayout.NORTH);

        setSelectable(list);
        // initializeShowSubclassInstances();
        lc.setHeaderLabel("Asserted Instances");
    }


	private void updateLabel() {
        String text;
        Cls cls = getSoleAllowedCls();
        BrowserSlotPattern pattern = cls == null ? null : cls.getBrowserSlotPattern();
        if (pattern == null) {
            text = null;
        }
        else {
            // text = "Instances by ";
            if (pattern.isSimple()) {
                text = pattern.getFirstSlot().getBrowserText();
                if (Model.Slot.NAME.equals(text)) {
                    text = "Asserted Instances";
                }
            }
            else {
                text = "multiple properties";
            }
        }
        lc.setHeaderLabel(text);
    }


    private HeaderComponent createHeader() {
        JLabel label = ComponentFactory.createLabel();
        String instanceBrowserLabel = LocalizedText.getText(ResourceKey.INSTANCE_BROWSER_TITLE);
        String forClassLabel = LocalizedText.getText(ResourceKey.CLASS_EDITOR_FOR_CLASS_LABEL);
        header = new HeaderComponent(instanceBrowserLabel, forClassLabel, label);
        header.setColor(Colors.getInstanceColor());
        return header;
    }


    //TODO: fix me
    private void fixRenderer() {
        FrameRenderer frameRenderer = (FrameRenderer) list.getCellRenderer();
        frameRenderer.setDisplayType(showSubclassInstances);
    }


    protected void addButtons(Action viewAction, LabeledComponent c) {
        // c.addHeaderButton(createReferencersAction());
        c.addHeaderButton(createConfigureAction());
        c.addHeaderButton(createCreateAction());
        c.addHeaderButton(createCopyAction());
        c.addHeaderButton(createDeleteAction());
        c.addHeaderButton(createCreateAnonymousAction());
    }


    private void addClsListeners() {
        Iterator i = classes.iterator();
        while (i.hasNext()) {
            Cls cls = (Cls) i.next();
            cls.addClsListener(_clsListener);
            cls.addFrameListener(_clsFrameListener);
        }
    }


    private void addInstanceListeners() {
        ListModel model = getModel();
        int start = list.getFirstVisibleIndex();
        int stop = list.getLastVisibleIndex();
        if (start < 0) { return; }        
        for (int i = start; i < stop + 1 ; i++) {
           FrameWithBrowserText fbt = (FrameWithBrowserText) model.getElementAt(i);
           Instance instance = (Instance)fbt.getFrame();
           addInstanceListener(instance);
        }
    }


    private void removeInstanceListeners() {
        Iterator i = listenedToInstances.iterator();
        while (i.hasNext()) {
            Instance instance = (Instance) i.next();
            instance.removeFrameListener(_instanceFrameListener);
        }
        listenedToInstances.clear();
    }


    private void addInstanceListener(Instance instance) {
        instance.addFrameListener(_instanceFrameListener);
        listenedToInstances.add(instance);
    }


    //TODO
    protected Action createCreateAction() {
        createAction = new CreateAction("Create instance ", OWLIcons.getCreateIndividualIcon(OWLIcons.RDF_INDIVIDUAL)) {
            @Override
			public void onCreate() {
                if (!classes.isEmpty()) {
                	RDFSClass firstType = (RDFSClass) CollectionUtilities.getFirstItem(classes);
                	final String name = owlModel.createNewResourceName(NamespaceUtil.getLocalName(firstType.getName()));

                	Transaction<Instance> t = new Transaction<Instance>(owlModel, "Create Individual: " +
                			NamespaceUtil.getLocalName(name) + " of " + CollectionUtilities.toString(classes), name) {
                	    private Instance instance;
                	    
                	    @Override
                	    public boolean doOperations() {
                            instance = owlModel.createInstance(name, classes);
                            if (instance instanceof Cls) {
                                Cls newCls = (Cls) instance;
                                if (newCls.getDirectSuperclassCount() == 0) {
                                    newCls.addDirectSuperclass(owlModel.getOWLThingClass());
                                }
                            }
                            return true;
                	    }
                	    
                	    @Override
						public Instance getResult() {
                	        return instance;
                	    }
                	};
                	t.execute();
                    list.setSelectedValue(new FrameWithBrowserText(t.getResult()), true);
                }
            }
        };
        return createAction;
    }

    //TODO
    protected Action createCreateAnonymousAction() {
        createAnonymousAction = new CreateAction("Create anonymous instance", OWLIcons.getCreateIndividualIcon(OWLIcons.RDF_ANON_INDIVIDUAL)) {
            @Override
			public void onCreate() {
                if (!classes.isEmpty()) {
                	String name = owlModel.getNextAnonymousResourceName();
                    Instance instance = owlModel.createInstance(new FrameID(name), classes, false);
                    if (instance instanceof Cls) {
                        Cls newCls = (Cls) instance;
                        if (newCls.getDirectSuperclassCount() == 0) {
                            newCls.addDirectSuperclass(owlModel.getOWLThingClass());
                        }
                    }
                    list.setSelectedValue(new FrameWithBrowserText(instance), true);
                }
            }
        };
        return createAnonymousAction;
    }


    protected Action createConfigureAction() {
        return new ConfigureAction() {
            @Override
			public void loadPopupMenu(JPopupMenu menu) {
                menu.add(createSetDisplaySlotAction());
                menu.add(createShowAllInstancesAction());
            }
        };
    }


    protected JMenuItem createShowAllInstancesAction() {
        Action action = new AbstractAction("Show Subclass Instances") {
            public void actionPerformed(ActionEvent event) {
                setShowAllInstances(!showSubclassInstances);
            }
        };
        JMenuItem item = new JCheckBoxMenuItem(action);
        item.setSelected(showSubclassInstances);
        return item;
    }

    //    private void initializeShowSubclassInstances() {
    //        showSubclassInstances = ApplicationProperties.getBooleanProperty(SHOW_SUBCLASS_INSTANCES, false);
    //        reload();
    //        fixRenderer();
    //    }


    private void setShowAllInstances(boolean b) {
        showSubclassInstances = b;
        // ApplicationProperties.setBoolean(SHOW_SUBCLASS_INSTANCES, b);
        reload();
        fixRenderer();
    }


    protected Cls getSoleAllowedCls() {
        Cls cls;
        if (classes.size() == 1) {
            cls = CollectionUtilities.getFirstItem(classes);
        }
        else {
            cls = null;
        }
        return cls;
    }


    protected JMenu createSetDisplaySlotAction() {
        JMenu menu = ComponentFactory.createMenu("Set Display Property");
        boolean enabled = false;
        Cls cls = getSoleAllowedCls();
        if (cls != null) {
            BrowserSlotPattern pattern = cls.getBrowserSlotPattern();
            Slot browserSlot = pattern != null && pattern.isSimple() ? pattern.getFirstSlot() : null;
            ArrayList<Slot> slots = new ArrayList<Slot>(cls.getVisibleTemplateSlots());
            slots.add(cls.getKnowledgeBase().getNameSlot());
            Collections.sort(slots, new FrameComparator<Slot>());
            Iterator<Slot> i = slots.iterator();
            while (i.hasNext()) {
                Slot slot = i.next();
                JRadioButtonMenuItem item = new JRadioButtonMenuItem(createSetDisplaySlotAction(slot));
                if (slot.equals(browserSlot)) {
                    item.setSelected(true);
                }
                menu.add(item);
                enabled = true;
            }
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(createSetDisplaySlotMultipleAction());
            if (browserSlot == null) {
                item.setSelected(true);
            }
            menu.add(item);
        }
        menu.setEnabled(enabled);
        return menu;
    }


    protected Action createSetDisplaySlotAction(final Slot slot) {
        return new AbstractAction(slot.getBrowserText(), slot.getIcon()) {
            public void actionPerformed(ActionEvent event) {
                getSoleAllowedCls().setDirectBrowserSlot(slot);
                updateLabel();
                repaint();
            }
        };
    }


    protected Action createSetDisplaySlotMultipleAction() {
        return new AbstractAction("Multiple Slots...") {
            public void actionPerformed(ActionEvent event) {
                Cls cls = getSoleAllowedCls();
                BrowserSlotPattern currentPattern = getSoleAllowedCls().getBrowserSlotPattern();
                MultiSlotPanel panel = new MultiSlotPanel(currentPattern, cls);
                int rval = ModalDialog.showDialog(AssertedInstancesListPanel.this, panel, "Multislot Display Pattern",
                                                  ModalDialog.MODE_OK_CANCEL);
                if (rval == ModalDialog.OPTION_OK) {
                    BrowserSlotPattern pattern = panel.getBrowserTextPattern();
                    if (pattern != null) {
                        cls.setDirectBrowserSlotPattern(pattern);
                    }
                }
                updateLabel();
                repaint();
            }
        };
    }


    protected Action createDeleteAction() {
        deleteAction = new DeleteInstancesAction(this);
        return deleteAction;
    }


    protected Action createCopyAction() {
        copyAction = new MakeCopiesAction(ResourceKey.INSTANCE_COPY, this) {
            @Override
			protected Instance copy(Instance instance, boolean isDeep) {
                Instance copy = super.copy(instance, isDeep);
                setSelectedInstance(copy);
                return copy;
            }
        };
        return copyAction;
    }


    protected Action createReferencersAction() {
        return new ReferencersAction(ResourceKey.INSTANCE_VIEW_REFERENCES, this);
    }


    protected Action createViewAction() {
        return new ViewAction(ResourceKey.INSTANCE_VIEW, this) {
            @Override
			public void onView(Object o) {
                owlModel.getProject().show((Instance) o);
            }
        };
    }


    @Override
	public void dispose() {
        removeClsListeners();
        removeInstanceListeners();
    }


    public JComponent getDragComponent() {
        return list;
    }


    private SimpleListModel getModel() {
        return (SimpleListModel) list.getModel();
    }


    private boolean isSelectionEditable() {
        boolean isEditable = true;
        Iterator i = getSelection().iterator();
        while (i.hasNext()) {
            Instance instance = (Instance) i.next();
            if (!instance.isEditable()) {
                isEditable = false;
                break;
            }
        }
        return isEditable;
    }


    @Override
	public void onSelectionChange() {
        // Log.enter(this, "onSelectionChange");
        boolean editable = isSelectionEditable();
        ComponentUtilities.setDragAndDropEnabled(list, editable);
        updateButtons();
    }


    private void removeInstanceListener(Instance instance) {                
        instance.removeFrameListener(_instanceFrameListener);
        listenedToInstances.remove(instance);
    }


    private void removeClsListeners() {
        Iterator i = classes.iterator();
        while (i.hasNext()) {
            Cls cls = (Cls) i.next();
            cls.removeClsListener(_clsListener);
            cls.removeFrameListener(_clsFrameListener);
        }
    }


    public void setClses(Collection newClses) {
        removeClsListeners();
        classes = new ArrayList(newClses);
        list.setClasses(newClses);
        reload();
        updateButtons();
        addClsListeners();
    }


    public void reload() {
        removeInstanceListeners();   
        getModel().setValues(getFramesWithBrowserText(classes));
        addInstanceListeners();
        reloadHeader(classes);
        updateLabel();
    }


    private void reloadHeader(Collection clses) {
        StringBuffer text = new StringBuffer();
        Icon icon = null;
        Iterator i = clses.iterator();
        while (i.hasNext()) {
            Cls cls = (Cls) i.next();
            if (icon == null) {
                icon = cls.getIcon();
            }
            if (text.length() != 0) {
                text.append(", ");
            }
            text.append(cls.getBrowserText());
        }
        JLabel label = (JLabel) header.getComponent();
        label.setText(text.toString());
        label.setIcon(icon);
    }

    private static Collection removeHiddenInstances(Collection instances) {
        Collection visibleInstances = new ArrayList(instances);
        Iterator i = visibleInstances.iterator();
        while (i.hasNext()) {
            Instance instance = (Instance) i.next();
            if (!instance.isVisible()) {
                i.remove();
            }
        }
        return visibleInstances;
    }

    public void setSelectedInstance(Instance instance) {
        list.setSelectedValue(new FrameWithBrowserText(instance), true);
        updateButtons();
    }


    private void updateButtons() {
        Cls cls = CollectionUtilities.getFirstItem(classes);
        createAction.setEnabled(cls == null ? false : cls.isConcrete());
        createAnonymousAction.setEnabled(cls == null ? false : cls.isConcrete());

        Instance instance = (Instance) getSoleSelection();
        boolean allowed = instance != null && instance instanceof SimpleInstance;
        copyAction.setAllowed(allowed);
    }



    private void createListRenderer() {
    	FrameRenderer listRenderer = new FrameRenderer() {
    		@Override
    		public void load(Object value) {
    			if (value instanceof FrameWithBrowserText) {
    				showFrameWithBrowserText((FrameWithBrowserText)value);
    			} else {
    				super.load(value);
    			}
    		}

			private void showFrameWithBrowserText(FrameWithBrowserText fbt) {
				setMainText(StringUtilities.unquote(fbt.getBrowserText()));
				if (fbt.getFrame() != null) {
					setMainIcon(fbt.getFrame().getIcon()); //should not go to the server
				}
			}
    	};
    	list.setCellRenderer(listRenderer);
	}

    @Override
    public Collection getSelection() {
    	Collection<FrameWithBrowserText> fbts = super.getSelection();
    	if (fbts == null) { return null; }
    	Collection<Instance> instances = new HashSet<Instance>();
    	for (FrameWithBrowserText fbt : fbts) {
			instances.add((Instance)fbt.getFrame());
		}
    	return instances;
    }


    /**
     * Does nothing anymore. This functionality moved to the menu button.
     *
     * @deprecated
     */
    @Deprecated
	public void setShowDisplaySlotPanel(boolean b) {

    }
    
    /*
     * Code for getting the instances of classes that tries to optimize performance
     * for client-server
     */
    
    private Collection<FrameWithBrowserText> getFramesWithBrowserText(Collection<Cls> clses) {
    	if (!showSubclassInstances && useCacheHeuristics() &&
    			owlModel.getProject().isMultiUserClient() &&
    			hasOnlyDataBrowserSlots(clses) && isCached(clses)) {
    		//use the cache
    		return getValuesFromCache(clses);    		
    	} else { //go to the server
    		GetInstancesAndBrowserTextJob job = new GetInstancesAndBrowserTextJob(owlModel, clses, !showSubclassInstances);
    		return job.execute();
    	}
    }
    
    /**
     * Determine whether the browser slots are nested (e.g. they are one level deep).
     * If they are, then don't use the local cache, use the job to get the instances.
     */
    private boolean hasOnlyDataBrowserSlots(Collection<Cls> clses) {
    	for (Cls cls : clses) {
    		BrowserSlotPattern bsp = cls.getBrowserSlotPattern();
    		if (bsp != null) {
    			List<Slot> slots = bsp.getSlots();
    			for (Slot slot : slots) {
					if (slot.getValueType() == ValueType.INSTANCE || slot.getValueType() == ValueType.CLS) {
						return false;
					}
				}
    		}
		}
    	return true;
	}


	/**
     * This is a heuristic if the instances of clses are cached
     */
    private boolean isCached(Collection<Cls> clses) {    	
    	for (Cls cls : clses) {
			if (!RemoteClientFrameStore.isCached(cls, owlModel.getSystemFrames().getDirectInstancesSlot(), null, false)) {
				return false;
			}
			Collection<Instance> instances = cls.getDirectInstances(); //should not call the server
			for (Instance instance : instances) {
				if (!RemoteClientFrameStore.isCacheComplete(instance)) {
					return false;
				}
			}
		}
    	return true;
    }
    
    
    //TODO: refactor out
    private List<FrameWithBrowserText> getValuesFromCache(Collection<Cls> clses) {
    	List<FrameWithBrowserText> framesWithBrowserText = new ArrayList<FrameWithBrowserText>();
		for (Cls cls : clses) {
			Collection<Instance> instances = showSubclassInstances ? cls.getInstances() : cls.getDirectInstances();
			for (Instance instance : instances) {
				framesWithBrowserText.add(
						new FrameWithBrowserText(instance, instance.getBrowserText(), instance.getDirectTypes()));
			}			
		}
		Collections.sort(framesWithBrowserText, new FrameWithBrowserTextComparator());
		return framesWithBrowserText;
    }   
    

    private boolean useCacheHeuristics() {
    	return ApplicationProperties.getBooleanProperty(OWLUI.USE_CACHE_HEURISTICS_PROP, true);
    }
    
}