package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.visitor.OWLModelVisitor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultOWLDataRange extends DefaultRDFIndividual implements OWLDataRange {

    public DefaultOWLDataRange(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    public DefaultOWLDataRange() {
    }


    public boolean equalsStructurally(RDFObject object) {
        if (object instanceof OWLDataRange) {
            OWLDataRange comp = (OWLDataRange) object;
            return OWLUtil.equalsStructurally(getOneOfValueLiterals(), comp.getOneOfValueLiterals());
        }
        return false;
    }


    public String getBrowserText() {
        String str = "owl:oneOf{";
        RDFList oneOf = getOneOf();
        if (oneOf != null) {
            for (Iterator it = oneOf.getValues().iterator(); it.hasNext();) {
                Object o = it.next();
                if (o instanceof String) {
                    str += "\"" + o + "\"";
                }
                else if (o instanceof RDFSLiteral) {
                    RDFSLiteral literal = (RDFSLiteral) o;
                    if (literal.getDatatype().equals(getOWLModel().getXSDstring())) {
                        str += "\"" + literal.getString() + "\"";
                    }
                    else {
                        str += literal.getString();
                    }
                }
                else {
                    str += o;
                }
                if (it.hasNext()) {
                    str += " ";
                }
            }
        }
        return str + "}";
    }


    public RDFList getOneOf() {
        return (RDFList) getPropertyValue(getOWLModel().getOWLOneOfProperty());
    }


    public List getOneOfValueLiterals() {
        RDFList oneOf = getOneOf();
        if (oneOf == null) {
            return Collections.EMPTY_LIST;
        }
        else {
            return oneOf.getValueLiterals();
        }
    }


    public List getOneOfValues() {
        RDFList oneOf = getOneOf();
        if (oneOf == null) {
            return Collections.EMPTY_LIST;
        }
        else {
            return oneOf.getValues();
        }
    }


    public RDFSDatatype getRDFDatatype() {
        RDFList oneOf = getOneOf();
        if (oneOf != null) {
            Object first = oneOf.getFirst();
            if (first != null && !getOWLModel().getRDFNil().equals(first)) {
                return getOWLModel().getRDFSDatatypeOfValue(first);
            }
        }
        return null;
    }


    public void accept(OWLModelVisitor visitor) {
        visitor.visitOWLDataRange(this);
    }
}
