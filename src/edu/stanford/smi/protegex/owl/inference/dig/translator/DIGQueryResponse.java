package edu.stanford.smi.protegex.owl.inference.dig.translator;

import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 19, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface DIGQueryResponse {

    /**
     * Gets the ID of the query that the reponse corresponds to
     */
    public String getID();


    /**
     * If the query resulted in a concept set response type
     * for example a query for super concepts then
     * this method can be used to get the concepts.
     *
     * @return A <code>Collection</code> of <code>RDFSClass</code>s
     */
    public Collection getConcepts();


    /**
     * If the query resulted in an individual set response type
     * then this method may be used to obtain the individuals
     * in the response.
     *
     * @return A <code>Collection</code> of <code>RDFIndividual</code>s
     */
    public Collection getIndividuals();


    /**
     * If the query resultied in a boolean response,
     * for example asking if a concept was satisfiable, then
     * this method may be used to get the boolean result.
     */
    public boolean getBoolean();
}
