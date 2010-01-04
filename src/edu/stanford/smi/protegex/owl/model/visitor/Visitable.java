package edu.stanford.smi.protegex.owl.model.visitor;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface Visitable {

    /**
     * This method is provided to be used with the <code>OWLModelVisitor</code>,
     * which is part of the visitor design pattern.  By implementing this method,
     * instances of the implementing class are stating that they can accept an
     * <code>OWLModelVisitor</code>
     *
     * @param visitor The visitor that will operate on this object.
     */
    void accept(OWLModelVisitor visitor);
}
