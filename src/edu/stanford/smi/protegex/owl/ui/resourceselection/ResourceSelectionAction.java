package edu.stanford.smi.protegex.owl.ui.resourceselection;

import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * An Action that requires the selection of an RDFResource from a List.
 * This Action generalizes this behavior and separates selection from action, so that
 * the selection can be made either through a conventional resource picking dialog or
 * through a ResourceSelectionComboBox.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class ResourceSelectionAction extends AbstractAction
        implements ResourceSelectionListener {

    /**
     * A ugly global flag that can be used to deactivate the right-click popup
     * when working with huge ontologies (such as in database backend) for
     * performance reasons.
     */
    public static boolean activated = true;

    private boolean multipleSelection;

    private boolean showHiddenResources = false;


    /**
     * Constructs a new ResourceSelectionAction.
     *
     * @param name the name of the Action
     * @param icon the Icon
     */
    public ResourceSelectionAction(String name, Icon icon) {
        this(name, icon, false);
    }


    /**
     * Constructs a new ResourceSelectionAction.
     *
     * @param name              the name of the Action
     * @param icon              the Icon
     * @param multipleSelection true to allow multiple selection of Frames
     */
    public ResourceSelectionAction(String name, Icon icon, boolean multipleSelection) {
        super(name, icon);
        this.multipleSelection = multipleSelection;
    }


    /**
     * Shows the frame picker dialog from <CODE>pickFrame</CODE> and then calls
     * <CODE>resourceSelected</CODE> on the selected Frame.
     *
     * @param e ignored
     */
    public void actionPerformed(ActionEvent e) {
        if (multipleSelection) {
            Collection resources = pickResources();
            for (Iterator it = resources.iterator(); it.hasNext();) {
                RDFResource resource = (RDFResource) it.next();
                resourceSelected(resource);
            }
        }
        else {
            RDFResource resource = pickResource();
            if (resource != null) {
                resourceSelected(resource);
            }
        }
    }


    /**
     * Activates the right mouse click on a given JButton.
     * This installs a MouseListener to the JButton so that whenever the button is clicked
     * with the right mouse button, a combobox will appear that provides the selection.
     *
     * @param button
     */
    public void activateComboBox(final JButton button) {
        if (activated) {

            button.setRolloverIcon(new DropDownOverlayIcon(button.getIcon(), button));

            button.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e) && isEnabled()) {
                        Collection resources = null;
                        if (getShowHiddenResources()) {
                            resources = getSelectableResources();
                        }
                        else {
                            resources = new ArrayList();
                            for (Iterator it = getSelectableResources().iterator(); it.hasNext();) {
                                RDFResource resource = (RDFResource) it.next();
                                if (resource.isVisible() || resource.isSystem()) {
                                    resources.add(resource);
                                }
                            }
                        }
                        ResourceSelectionComboBox.selectResource(resources, button, 0,
                                ResourceSelectionAction.this, getRenderer());
                    }
                }
            });
        }
    }


    public ListCellRenderer getRenderer() {
        return new FrameRenderer();
    }


    public abstract Collection getSelectableResources();


    public boolean getShowHiddenResources() {
        return showHiddenResources;
    }


    /**
     * This method is used when single selection is allowed for this Action.
     *
     * @return the selected Frame
     */
    public RDFResource pickResource() {
        return null;
    }


    /**
     * This method is only used when multiple selection is allowed for this Action.
     * By default, it will call <CODE>pickFrame</CODE>.
     *
     * @return the Frame
     */
    public Collection pickResources() {
        RDFResource resource = pickResource();
        if (resource == null) {
            return Collections.EMPTY_LIST;
        }
        else {
            return Collections.singleton(resource);
        }
    }


    public static void setActivated(boolean value) {
        activated = value;
    }


    public void setShowHiddenResources(boolean value) {
        showHiddenResources = value;
    }
}
