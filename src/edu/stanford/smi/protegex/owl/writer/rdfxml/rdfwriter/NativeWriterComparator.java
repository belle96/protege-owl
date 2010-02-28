package edu.stanford.smi.protegex.owl.writer.rdfxml.rdfwriter;

import java.util.Comparator;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

public class NativeWriterComparator extends FrameComparator<RDFResource> implements
        Comparator<RDFResource> {
    
    @Override
    public int compare(RDFResource f1, RDFResource f2) {
        int type1 = getType(f1);
        int type2 = getType(f2);
        if (type1 > type2) {
            return 1;
        }
        else if (type2 > type1) {
            return -1;
        }
        else {
            return super.compare(f1, f2);
        }
    }
    
    private static int getType(Frame f) {
        if (f instanceof OWLClass) {
            return 0;
        }
        else if (f instanceof OWLProperty) {
            return 1;
        }
        else if (f instanceof OWLIndividual) {
            return 2;
        }
        else {
            return -1;
        }
    }
}
