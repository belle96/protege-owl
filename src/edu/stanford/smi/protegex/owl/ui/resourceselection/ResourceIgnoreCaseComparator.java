package edu.stanford.smi.protegex.owl.ui.resourceselection;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.util.Log;

import java.util.Comparator;

/**
 * A Comparator for Frame instances that compares the lower case browser texts.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ResourceIgnoreCaseComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        if (o1 instanceof Frame && o2 instanceof Frame) {
            String name1 = ((Frame) o1).getBrowserText().toLowerCase();
            String name2 = ((Frame) o2).getBrowserText().toLowerCase();
            return name1.compareTo(name2);
        }
        else {
            Log.getLogger().severe("[ResourceIgnoreCareComparator]  Invalid types " + o1 + ", " + o2);
            return 0;
        }
    }
}
