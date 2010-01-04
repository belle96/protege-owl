package edu.stanford.smi.protegex.owl.model.triplestore.impl;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.Triple;
import edu.stanford.smi.protegex.owl.model.triplestore.Tuple;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public final class DefaultTuple implements Tuple {

    private RDFResource subject;

    private RDFProperty predicate;


    public DefaultTuple(RDFResource subject, RDFProperty predicate) {
        this.subject = subject;
        this.predicate = predicate;
    }


    public boolean equals(Object obj) {
        if (obj instanceof Tuple && !(obj instanceof Triple)) {
            Tuple other = (Tuple) obj;
            return subject.equals(other.getSubject()) &&
                    predicate.equals(other.getPredicate());
        }
        return false;
    }


    public final RDFResource getSubject() {
        return subject;
    }


    public final RDFProperty getPredicate() {
        return predicate;
    }


    public int hashCode() {
        return subject.hashCode() + predicate.hashCode();
    }


    public String toString() {
        return "Tuple(" + getSubject().getName() + ", " +
                getPredicate().getName() + ")";
    }
}
