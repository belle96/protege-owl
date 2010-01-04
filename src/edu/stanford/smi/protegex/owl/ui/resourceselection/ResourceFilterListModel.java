package edu.stanford.smi.protegex.owl.ui.resourceselection;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.ui.code.OWLTextField;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * A ListModel for JComboBoxes and JLists that allows to select a frame by typing its
 * prefix characters.  The ListModel will contain exactly those frames that match the
 * current prefix.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ResourceFilterListModel extends AbstractListModel implements ComboBoxModel {

    /**
     * The full list of all Frames available as choice
     */
    private Frame[] frames;

    /**
     * The partial name of the frame entered by the user
     */
    private String prefix = "";

    /**
     * The currently selected Frame
     */
    private Frame selectedFrame;

    /**
     * The index of the first valid frame (0..frames.length - 1)
     */
    private int validStart = 0;

    /**
     * The number of valid entries (the list size)
     */
    private int validCount;


    public ResourceFilterListModel(Collection frames) {
        this.frames = (Frame[]) frames.toArray(new Frame[0]);
        Arrays.sort(this.frames, new ResourceIgnoreCaseComparator());
        validCount = this.frames.length;
        if (frames.size() > 0) {
            selectedFrame = this.frames[0];
        }
    }


    public void addChar(char ch) {
        if (OWLTextField.isIdChar(ch)) {
            prefix += Character.toLowerCase(ch);
            updateList();
        }
    }


    public void backspace() {
        int len = prefix.length();
        if (len > 0) {
            prefix = prefix.substring(0, len - 1);
            updateList();
        }
    }


    /**
     * Finds the first Frame that matches the suffix
     *
     * @return the index of the first frame
     */
    protected int findFirstMatchingFrame() {
        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            String name = frame.getBrowserText();
            if (name.toLowerCase().startsWith(prefix)) {
                return i;
            }
        }
        return 0;
    }


    private int findMatchingFrameCount(int startIndex) {
        for (int i = startIndex; i < frames.length; i++) {
            Frame frame = frames[i];
            String name = frame.getBrowserText();
            if (!name.toLowerCase().startsWith(prefix)) {
                return i - startIndex;
            }
        }
        return frames.length - startIndex;
    }


    public Object getElementAt(int index) {
        return frames[index + validStart];
    }


    public int getPrefixLength() {
        return prefix.length();
    }


    public Object getSelectedItem() {
        return selectedFrame;
    }


    public int getSize() {
        return validCount;
    }


    public void setSelectedItem(Object anItem) {
        if (anItem instanceof Frame) {
            if ((selectedFrame != null && selectedFrame != anItem) ||
                    selectedFrame == null && anItem != null) {
                selectedFrame = (Frame) anItem;
            }
        }
    }


    private void updateList() {
        validCount = 0;
        fireIntervalRemoved(this, 0, validCount);
        if (prefix.length() > 0) {
            validStart = findFirstMatchingFrame();
            validCount = findMatchingFrameCount(validStart);
        }
        else {
            validStart = 0;
            validCount = frames.length;
        }
        selectedFrame = validCount > 0 ? frames[validStart] : null;
        fireIntervalAdded(this, 0, validCount);
    }
}
