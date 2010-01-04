package edu.stanford.smi.protegex.owl.ui.actions;

/**
 * A common base interface for things that can be represented by an icon.
 * This is on purpose abstracted from the Swing Icon class, so that instances
 * of this class can also be used under non-Swing environments like Eclipse.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface IconOwner {


    /**
     * Gets a Class that is in the same folder as the icon file.
     *
     * @return the icon resource Class
     */
    Class getIconResourceClass();


    /**
     * Gets the relative name of an (optional) icon.
     *
     * @return the icon name such as <CODE>"classify.gif"</CODE> or null
     */
    String getIconFileName();
}
