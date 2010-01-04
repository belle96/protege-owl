package edu.stanford.smi.protegex.owl.ui.subsumption;

import java.util.Comparator;

public class ChangedClassesChangeComporator implements Comparator<ChangedClassItem> {
    
    public int compare(ChangedClassItem o1, ChangedClassItem o2) {
        int result = o1.toString().compareTo(o2.toString());
        if (result == 0) {
            result = o1.getCls().compareTo(o2.getCls());
        }
        return result;
    }
}
