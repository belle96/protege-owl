package edu.stanford.smi.protegex.owl.util;

import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;


/**
 * This class represents information that should be collected about parts of 
 * expressions when they are parsed from the Protege Frames knowledge base.  The
 * intention is that this class will be utilized instead of simply
 * collecting the expressions and then calling getExpressionRoot
 * later.  getExpressionRoot is extremely expensive and not
 * recommended.
 */

public class ExpressionInfo<E extends OWLAnonymousClass> {
  private E expression;
  private OWLAnonymousClass headOfExpression;
  private OWLNamedClass directNamedClass;
        
  public ExpressionInfo(E expression, 
                        OWLAnonymousClass headOfExpression, 
                        OWLNamedClass directNamedClass) {
    this.expression = expression;
    this.headOfExpression = headOfExpression;
    this.directNamedClass = directNamedClass;
  }
  public OWLNamedClass getDirectNamedClass() {
    return directNamedClass;
  }
  public OWLAnonymousClass getHeadOfExpression() {
    return headOfExpression;
  }

  public E getExpression() {
    return expression;
  }
        
        
}

