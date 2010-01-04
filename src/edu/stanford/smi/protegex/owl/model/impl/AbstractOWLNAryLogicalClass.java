package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.exception.ProtegeException;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.util.LocalizeUtils;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.ProtegeJob;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.swrl.bridge.OWLNaryClassAxiom;

import java.util.*;
import java.util.logging.Level;

import com.hp.hpl.jena.sparql.algebra.op.OpReduced;

/**
 * The base class of DefaultOWLIntersectionClass and DefaultOWLUnionClass.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLNAryLogicalClass extends AbstractOWLLogicalClass
        implements OWLNAryLogicalClass {


    protected AbstractOWLNAryLogicalClass(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    protected AbstractOWLNAryLogicalClass() {
    }


    public void addOperand(RDFSClass operand) {
        RDFList list = (RDFList) getPropertyValue(getOperandsProperty());
        if (list == null || getOWLModel().getRDFNil().equals(list)) {
            list = getOWLModel().createRDFList();
            setOwnSlotValue(getOperandsProperty(), list);
        }
        list.append(operand);
    }


    public boolean equalsStructurally(RDFObject object) {
        if (object instanceof AbstractOWLNAryLogicalClass) {
            AbstractOWLNAryLogicalClass compCls = (AbstractOWLNAryLogicalClass) object;
            if (getOperatorSymbol() == compCls.getOperatorSymbol()) {
                return OWLUtil.equalsStructurally(getOperands(), compCls.getOperands());
            }
        }
        return false;
    }

    /*public String getBrowserText() {
       final Collection operands = getOperands();
       char operator = getOperatorSymbol();
       if (operands.size() == 0) {
           return "<empty " + getClass().getName() + ">";
       }
       String text = "";
       for (Iterator it = operands.iterator(); it.hasNext();) {
           Cls cls = (Cls) it.next();
           String clsText = cls instanceof RDFSClass ?
                   ((RDFSClass) cls).getNestedBrowserText() :
                   cls.getBrowserText();
           text += clsText;
           if (it.hasNext()) {
               text += " " + operator + " ";
           }
       }
       return text;
   } */


    public Collection<RDFSNamedClass> getNamedOperands() {
        Collection<RDFSNamedClass> result = new HashSet<RDFSNamedClass>();
        for (RDFSClass operand : getOperands()) {
            if (operand instanceof RDFSNamedClass) {
                result.add((RDFSNamedClass) operand);
            }
        }
        return result;
    }


    public String getNestedBrowserText() {
        return "(" + getBrowserText() + ")";
    }


    @SuppressWarnings("unchecked")
    public Collection<RDFSClass> getOperands() {
        Collection<RDFSClass> operands = null;
        try {
            operands = (Collection<RDFSClass>) new GetOperandsJob(getOWLModel(), 
                                                                  this, getOperandsProperty()).execute();
        } catch (Throwable t) {
            Log.getLogger().log(Level.WARNING, "Could not get operands for " + this, t);
        }
        return (Collection<RDFSClass>) (operands == null ? Collections.emptyList() : operands);
    }


    public boolean hasOperandWithBrowserText(String browserText) {
        for (Iterator it = getOperands().iterator(); it.hasNext();) {
            RDFSClass operand = (RDFSClass) it.next();
            if (browserText.equals(operand.getBrowserText())) {
                return true;
            }
        }
        return false;
    }


    public boolean hasSameOperands(OWLNAryLogicalClass other) {
        Set setA = new HashSet();
        for (Iterator it = getOperands().iterator(); it.hasNext();) {
            RDFSClass operand = (RDFSClass) it.next();
            setA.add(operand.getBrowserText());
        }
        Set setB = new HashSet();
        for (Iterator it = other.getOperands().iterator(); it.hasNext();) {
            RDFSClass operand = (RDFSClass) it.next();
            setB.add(operand.getBrowserText());
        }
        if (setA.size() == setB.size()) {
            setA.removeAll(setB);
            return setA.isEmpty();
        }
        else {
            return false;
        }
    }


    public Iterator listOperands() {
        return getOperands().iterator();
    }


    public void removeOperand(RDFSClass operand) {
        DefaultRDFList.removeListValue(this, getOperandsProperty(), operand);
    }
    
    private static class GetOperandsJob extends ProtegeJob {

        private OWLClass owlClass;
        private RDFProperty operandsProp;
        
        public GetOperandsJob(KnowledgeBase kb, OWLNAryLogicalClass owlClass, RDFProperty operandsProp) {
            super(kb);
            this.owlClass = owlClass;
            this.operandsProp = operandsProp;
        }

        @Override
        public Object run() throws ProtegeException {
            RDFList list = (RDFList) owlClass.getPropertyValue(operandsProp);
            if (list == null) {
                return Collections.emptyList();
            }
            else {
                return list.getValues();
            }
        }
        
        @Override
        public void localize(KnowledgeBase kb) {         
            super.localize(kb);
            LocalizeUtils.localize(owlClass, kb);
            LocalizeUtils.localize(operandsProp, kb);
        }
    }
}
