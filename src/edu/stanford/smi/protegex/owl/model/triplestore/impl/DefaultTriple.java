package edu.stanford.smi.protegex.owl.model.triplestore.impl;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.triplestore.Triple;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public final class DefaultTriple implements Triple {

    private RDFResource subject;

    private RDFProperty predicate;

    private Object object;


    public DefaultTriple(RDFResource subject, RDFProperty predicate, Object object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }


    public boolean equals(Object obj) {
        if (obj instanceof Triple) {
            Triple other = (Triple) obj;
            return subject.equals(other.getSubject()) &&
                    predicate.equals(other.getPredicate()) &&
                    object.equals(other.getObject());
        }
        return false;
    }


    public final RDFResource getSubject() {
        return subject;
    }


    public final RDFProperty getPredicate() {
        return predicate;
    }


    public final Object getObject() {
        return object;
    }


    public int hashCode() {
        return subject.hashCode() + predicate.hashCode() + object.hashCode();
    }


    public String toString() {
        Object object = getObject();
        String str = object instanceof Frame ? ((Frame) object).getName() : object.toString();
        return "Triple(" + getSubject().getName() + ", " +
                getPredicate().getName() + ", " +
                str + ")";
    }
}
