package edu.stanford.smi.protegex.owl.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.framestore.updater.RestrictionUpdater;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;

/**
 * This class contains a variety of useful OWL Utilities.  A major goal of this class is 
 * to take things out of OWLFrameStore where they do not belong.
 * 
 * @author tredmond
 *
 */

public class OWLFrameStoreUtils {
  
  public static Collection convertValueListToRDFLiterals(OWLModel owlModel, Collection values) {
    if (!values.isEmpty()) {
        for (Iterator it = values.iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof String && DefaultRDFSLiteral.isRawValue((String) o)) {
                return copyValueListToRDFLiterals(owlModel, values);
            }
        }
    }
    return values;
  }

  @SuppressWarnings("unchecked")
  public static List copyValueListToRDFLiterals(OWLModel owlModel, Collection values) {
    List result = new LinkedList();
    for (Iterator it = values.iterator(); it.hasNext();) {
        Object o = it.next();
        if (o instanceof String) {
            final String str = (String) o;
            if (DefaultRDFSLiteral.isRawValue(str)) {
                result.add(new DefaultRDFSLiteral(owlModel, str));
            }
            else {
                result.add(o);
            }
        }
        else {
            result.add(o);
        }
    }
    return result;
  }
}
