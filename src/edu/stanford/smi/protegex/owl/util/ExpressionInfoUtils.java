package edu.stanford.smi.protegex.owl.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

public class ExpressionInfoUtils {
  
  public static List<ExpressionInfo<OWLRestriction>> getDirectContainingRestrictions(OWLNamedClass c) {
    List<ExpressionInfo<OWLRestriction>> rs = new ArrayList<ExpressionInfo<OWLRestriction>>();
    for (Iterator it = c.getSuperclasses(false).iterator(); it.hasNext();) {
      RDFSClass superClass = (RDFSClass) it.next();
      if (superClass instanceof OWLRestriction) {
        ExpressionInfo<OWLRestriction> ri = new ExpressionInfo<OWLRestriction>((OWLRestriction) superClass, 
                                                                               (OWLAnonymousClass) superClass, 
                                                                               c);
        rs.add(ri);
      }
      else if (superClass instanceof OWLIntersectionClass) {
        OWLIntersectionClass intersectionClass = (OWLIntersectionClass) superClass;
        for (Iterator ot = intersectionClass.getOperands().iterator(); ot.hasNext();) {
          RDFSClass operand = (RDFSClass) ot.next();
          if (operand instanceof OWLRestriction) {
            ExpressionInfo<OWLRestriction> ri = new ExpressionInfo<OWLRestriction>((OWLRestriction) operand, 
                                                                                   intersectionClass, c);
            rs.add(ri);
          }
        }
      }
    }
    Collections.sort(rs, new Comparator<ExpressionInfo<OWLRestriction>>() {
      public int compare(ExpressionInfo<OWLRestriction> o1, ExpressionInfo<OWLRestriction> o2) {
        RDFSClass directType1 = o1.getExpression().getProtegeType();
        RDFSClass directType2 = o2.getExpression().getProtegeType();
        return directType1.getName().compareTo(directType2.getName());
      }
    });
    /* We used to remove redundant restrictions here */
    return rs;
  }

}
