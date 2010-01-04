package edu.stanford.smi.protegex.owl.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protege.ui.ListFinder;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.ComponentFactory;

/**
 *  Panel to allow a user to pick a single instance from a collection of instances.
 *
 * @author    Ray Fergerson <fergerson@smi.stanford.edu>
 */
public class SelectInstanceFromCollectionPanelWithFinder extends JComponent {
    private JList _list;

    public SelectInstanceFromCollectionPanelWithFinder(Collection c, int initialSelection) {
        setLayout(new BorderLayout());
        _list = ComponentFactory.createList(ModalDialog.getCloseAction(this));
        c = removeHidden(c);
        
        ArrayList<Instance> instanceList = new ArrayList<Instance>(c);
        Collections.sort(instanceList);
        
        _list.setListData(instanceList.toArray());
        configureRenderer();
        if (initialSelection >= 0) {
            setSelection(initialSelection);
        }
        add(new JScrollPane(_list), BorderLayout.CENTER);
        add(new ListFinder(_list, "Find"), BorderLayout.SOUTH);
        setPreferredSize(new Dimension(300, 150));
    }

    private boolean isMultiUserClient() {
        boolean isMultiUserClient = false;
        if (_list.getModel().getSize() > 0) {
            Object o = _list.getModel().getElementAt(0);
            if (o instanceof Frame) {
                Frame frame = (Frame) o;
                Project p = frame.getProject();
                isMultiUserClient = p.isMultiUserClient();
            }
        }
        return isMultiUserClient;
    }

    private Icon _clsIcon;

    private void configureRenderer() {
        FrameRenderer renderer;
        if (isMultiUserClient()) {
            // a really strange performance hack
            renderer = new FrameRenderer() {
                protected Icon getIcon(Cls cls) {
                    Icon icon;
                    if (_clsIcon == null) {
                        icon = cls.getIcon();
                        if (!cls.isMetaCls()) {
                            _clsIcon = icon;
                        }
                    } else {
                        icon = _clsIcon;
                    }
                    return icon;
                }
            };
        } else {
            renderer = FrameRenderer.createInstance();
            renderer.setDisplayTrailingIcons(false);
        }
        _list.setCellRenderer(renderer);
    }

    public Instance getSelection() {
        return (Instance) _list.getSelectedValue();
    }

    private static Collection removeHidden(Collection instances) {
        Collection result;
        Project p = ((Instance) (CollectionUtilities.getFirstItem(instances))).getProject();
        if (p.getDisplayHiddenClasses()) {
            result = instances;
        } else {
            result = new ArrayList();
            Iterator i = instances.iterator();
            while (i.hasNext()) {
                Instance instance = (Instance) i.next();
                if (instance.isVisible()) {
                    result.add(instance);
                }
            }
        }
        return result;
    }

    private void setSelection(final int index) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                _list.setSelectedIndex(index);
                _list.ensureIndexIsVisible(index);
            }
        });
    }
}
