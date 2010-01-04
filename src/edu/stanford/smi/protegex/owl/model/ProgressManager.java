package edu.stanford.smi.protegex.owl.model;

/**
 * An interface for objects that can manage the status and cancellation of
 * a longish operation on an OWLModel.  The use contract of this is
 * <p/>
 * <OL>
 * <LI>run()</LI>
 * <LI>( set...() | isCancelled() )*</LI>
 * <LI>end()</LI>
 * </OL>
 * <p/>
 * A typical implementation of this interface would display a status dialog
 * with a status text, perhaps an icon, and perhaps a status bar showing progress
 * from left to right.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProgressManager {

    /**
     * Notifies the manager that a new modal process is about to start.
     * Calling this method is only valid if no other process is currently
     * managed by this instance.
     *
     * @param title an (optional) title that could be displayed in a dialog etc.
     */
    void begin(String title, boolean canBeCancelled);


    /**
     * Notifies the manager that the recently started process has ended.
     * This method must <B>always</B> be called after a process was terminated
     * so that the manager can close windows, etc.
     */
    void end();


    /**
     * Checks if the user has cancelled the current process.
     * The process can call this method occasionally to interrupt itself,
     * and then invoke <CODE>endProcess()</CODE) to close dialogs etc.
     *
     * @return true  if the user wants to cancel
     */
    boolean isCancelled();


    /**
     * Sets an (optional) value for progress bars.  The caller
     * typically makes sure that the values increase continuously
     * from 0 to 1
     *
     * @param progress the progress value between 0 and 1
     */
    void setProgress(double progress);


    /**
     * Specifies an (optional) icon that can be displayed alongside with
     * the status text.
     *
     * @param iconBaseClass the Class that the icon is relative to
     * @param iconName      the file name relative to the base class
     */
    void setIcon(Class iconBaseClass, String iconName);


    /**
     * Changes the status text that is being displayed.
     *
     * @param text the new text
     */
    void setText(String text);
}
