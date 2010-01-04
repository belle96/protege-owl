package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import java.util.Comparator;

import edu.stanford.smi.protegex.owl.model.RDFResource;

public class NativeValueComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        int type1 = getType(o1);
        int type2 = getType(o2);
        if (type1 < type2) {
            return -1;
        }
        else if (type1 > type2) {
            return +1;
        }
        else if (o1 instanceof RDFResource) {
            return ((RDFResource) o1).compareTo((RDFResource) o2);
        }
        else {
            return o1.toString().compareTo(o2.toString());
        }
    }
    
    private int getType(Object o) {
        if (o instanceof RDFResource) {
            return 0;
        }
        else {
            return -1;
        }
    }

}
