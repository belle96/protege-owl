package edu.stanford.smi.protegex.owl.model;

/**
 * The base class of all anonymous OWL class types.
 * Anonymous classes should be handled with care, as they do follow some
 * life cycle restrictions.  Their life cycle depends on a host class,
 * which references them.  When the host class is deleted, then the
 * frame store will automatically delete any depending anonymous classes.
 * As a result, it is not permitted to share anonymous classes between
 * instances.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLAnonymousClass extends OWLClass {

    /**
     * This routine performs very slowly.  Usually this
     * can be avoided by keeping track of how this OWLAnonymous class was constructed.
     * 
     * Gets the root of the expression three where this is a part of.
     * For example, if this is the !A in the expression (!A & B), then
     * this method will return the OWLIntersectionClass (!A & B).
     *
     * @return the root (may be this if noone is pointing to this)
     */
    OWLAnonymousClass getExpressionRoot();


    /**
     * Gets the named class where this is attached to (directly or indirectly
     * as part of a nested expression).
     *
     * @return the owning named class
     */
    OWLNamedClass getOwner();
}
