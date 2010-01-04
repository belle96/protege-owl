package edu.stanford.smi.protegex.owl.model.visitor;

import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class VisitorUtil {

    /**
     * Visits the <code>RDFResource</code>s in the specified collection using the
     * specified visitor.
     * Note that this method does a type cast of everything in the collection
     * to <code>RDFResource</code>, so the specified collection should <b>only</b>
     * contain instances of <code>RDFResource</code>.
     *
     * @param collection A <code>Collection</code> that <b>only</b> contains instances
     *                   of <code>RDFResource</code>.
     */
    public static void visitRDFResources(Collection collection, OWLModelVisitor visitor) {
        for (Iterator it = collection.iterator(); it.hasNext();) {
            RDFResource curRes = (RDFResource) it.next();
            curRes.accept(visitor);
        }
    }
}

