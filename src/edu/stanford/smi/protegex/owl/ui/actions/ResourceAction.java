package edu.stanford.smi.protegex.owl.ui.actions;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class ResourceAction extends AbstractAction implements Comparable {

    private Component component;

    private String group;

    private boolean inToolBar;

    private RDFResource resource;


    public ResourceAction(String name, Icon icon) {
        this(name, icon, null);
    }


    public ResourceAction(String name, Icon icon, String group) {
        this(name, icon, group, false);
    }


    /**
     * Constructs a new ResourceAction.
     *
     * @param name      the display name of the Action
     * @param icon      the Icon
     * @param group     the (optional) group this should belong to
     * @param inToolBar true to put this into the tool bar at the bottom of forms
     *                  (this is only possible if there is an icon as well)
     */
    public ResourceAction(String name, Icon icon, String group, boolean inToolBar) {
        super(name, icon);
        this.group = group;
        this.inToolBar = inToolBar;
    }


    public int compareTo(Object o) {
        if (o instanceof ResourceAction) {
            ResourceAction other = (ResourceAction) o;
            String thisGroup = getGroup();
            if (thisGroup == null) {
                thisGroup = "";
            }
            String otherGroup = other.getGroup();
            if (otherGroup == null) {
                otherGroup = "";
            }
            String thisName = (String) getValue(Action.NAME);
            String otherName = (String) other.getValue(Action.NAME);
            int groupCompare = thisGroup.compareTo(otherGroup);
            if (groupCompare == 0) {
                int result = new Integer(other.getPriority()).compareTo(new Integer(getPriority()));
                if (result != 0) {
                    return result;
                }
                return thisName.compareTo(otherName);
            }
            else {
                return groupCompare;
            }
        }
        return 0;
    }


    protected Component getComponent() {
        return component;
    }


    public String getGroup() {
        return group;
    }


    /**
     * Gets an (optional) integer that can be used to control the order of actions
     * in a menu.  The higher the number, the further up the item will appear.
     *
     * @return the priority (0 is default)
     */
    public int getPriority() {
        return 0;
    }


    public OWLModel getOWLModel() {
        if (resource != null) {
            return resource.getOWLModel();
        }
        else {
            return null;
        }
    }


    protected RDFResource getResource() {
        return resource;
    }


    public void initialize(Component component, RDFResource resource) {
        this.component = component;
        this.resource = resource;
    }


    public boolean isInToolBar() {
        return inToolBar;
    }


    public abstract boolean isSuitable(Component component, RDFResource resource);
}
