package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import java.util.Comparator;

public class NativeValueComparator implements Comparator {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int compare(Object o1, Object o2) {
        if (!o1.getClass().equals(o2.getClass())) {
            return o1.getClass().getCanonicalName().compareTo(o2.getClass().getCanonicalName());
        }
        else if (o1 instanceof Comparable) { // this case covers RDFResource, RDFSLiteral and many other things as well.
            return ((Comparable) o1).compareTo(o2);
        }
        else {  // WARNING! Note that if the following gives equality for two unequal values then a value will be removed from the saved ontology.
                //          I don't know of an ideal fix for this
                //      There are a limited number of value types that can be achieved by the object o1.
            return o1.toString().compareTo(o2.toString());
        }
    }

}
