package edu.stanford.smi.protegex.owl.ui.navigation;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protege.util.Selectable;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protege.widget.TabWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TabNavigationHistorySelectable implements Disposable, NavigationHistorySelectable, SelectionListener {

    private Collection listeners = new HashSet();

    private boolean programmaticNavigation = false;

    private Project project;

    private Collection tabs = new ArrayList();

    private ContainerListener containerListener = new ContainerListener() {
        public void componentAdded(ContainerEvent e) {
            reinit();
        }
        public void componentRemoved(ContainerEvent e) {
            reinit();
        }
    };

    public TabNavigationHistorySelectable(OWLModel owlModel) {
        this.project = owlModel.getProject();
        ProjectView view = ProtegeUI.getProjectView(project);
		view.getTabbedPane().addContainerListener(containerListener);
        reinit();
    }


    /**
     * Adds a global selection listener to this to be notified whenever the user
     * has selected a different resource.  The resulting SelectionEvents will be
     * flagged to indicate whether the selection has changed as result of a programmatic
     *
     * @param listener
     */
    public void addSelectionListener(SelectionListener listener) {
        listeners.add(listener);
    }


    public void clearSelection() {
    }


    public void dispose() {
    	ProjectView view = ProtegeUI.getProjectView(project);
    	view.getTabbedPane().removeContainerListener(containerListener);
        removeListener();
    }


    public Collection getSelection() {
        ProjectView view = ProtegeUI.getProjectView(project);
        Object tabWidget = view.getSelectedTab();
        if (tabWidget instanceof NavigationHistoryTabWidget) {
            NavigationHistoryTabWidget tab = (NavigationHistoryTabWidget) tabWidget;
            Selectable sel = tab.getNestedSelectable();
            return sel.getSelection();
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }


    public void navigateToResource(RDFResource resource) {
        ProjectView view = ProtegeUI.getProjectView(project);
        List list = new ArrayList();
        list.add(view.getSelectedTab());
        list.addAll(tabs);
        try {
            programmaticNavigation = true;
            for (Iterator it = list.iterator(); it.hasNext();) {
                Object tab = it.next();
                if (tab instanceof NavigationHistoryTabWidget) {
                    if (((NavigationHistoryTabWidget) tab).displayHostResource(resource)) {
                        view.setSelectedTab((TabWidget) tab);
                        ((JComponent) tab).requestFocusInWindow();
                        return;
                    }
                }
            }
        }
        finally {
            programmaticNavigation = false;
        }
    }


    public void notifySelectionListeners() {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            SelectionListener listener = (SelectionListener) it.next();
            SelectionEvent event;
            if(programmaticNavigation) {
                event = new ProgrammaticSelectionEvent(this);
            }
            else {
                event = new SelectionEvent(this, SelectionEvent.SELECTION_CHANGED);
            }
            listener.selectionChanged(event);
        }
    }


    public void reinit() {

        removeListener();

        ProjectView view = ProtegeUI.getProjectView(project);
        for (Iterator it = view.getTabs().iterator(); it.hasNext();) {
            Object tab = it.next();
            if (tab instanceof NavigationHistoryTabWidget) {
                NavigationHistoryTabWidget widget = (NavigationHistoryTabWidget) tab;
                tabs.add(tab);
                Selectable selectable = widget.getNestedSelectable();
                selectable.addSelectionListener(this);
            }
        }
    }


    private void removeListener() {
        for (Iterator it = tabs.iterator(); it.hasNext();) {
            NavigationHistoryTabWidget tab = (NavigationHistoryTabWidget) it.next();
            Selectable selectable = tab.getNestedSelectable();
            selectable.removeSelectionListener(this);
        }
        tabs.clear();
    }


    public void removeSelectionListener(SelectionListener listener) {
        listeners.remove(listener);
    }


    public void selectionChanged(SelectionEvent event) {
        notifySelectionListeners();
    }
}
