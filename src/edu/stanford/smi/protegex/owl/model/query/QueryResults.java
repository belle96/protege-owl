package edu.stanford.smi.protegex.owl.model.query;

import java.util.List;
import java.util.Map;

/**
 * An object wrapping the results of a query.  A query results in an Iterator of
 * variable bindings, where each variable can be bound to an RDFObject.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface QueryResults {


    /**
     * Gets a list of all variables that have been declared in the query.
     * The variables are the keys in the Maps returned by the <CODE>next()</CODE> method.
     *
     * @return a List of Objects (e.g. Strings)
     * @see #next
     */
    List getVariables();


    /**
     * Checks if there are more results available.
     *
     * @return true  if there are more results
     */
    boolean hasNext();


    /**
     * Gets the next results, where each result is a Map from variables to bindings.
     * This method can only be called as long as <CODE>hasNext()</CODE> delivers true.
     *
     * @return a Map (keys are the members of the <CODE>getVariables()</CODE> call).
     * @see #getVariables
     */
    Map next();
}
