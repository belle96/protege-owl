package edu.stanford.smi.protegex.owl.inference.dig.reasoner;

import java.util.logging.Logger;

import org.w3c.dom.Document;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGReasonerException;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jun 14, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface DIGReasoner {

    public static final String LOGGER_NAME = "edu.stanford.smi.protegex.owl.inference.dig.reasoner";
    public static final Logger digLogger = Logger.getLogger(DIGReasoner.LOGGER_NAME);

    /**
     * Sets the URL of the reasoner.
     *
     * @param url The URL
     */
    public void setReasonerURL(String url);


    /**
     * Gets the URL of the reasoner.
     */
    public String getReasonerURL();


    /**
     * A helper method that gets the identity of
     * the inference
     *
     * @return A<code>DIGReasonerIdentity</code> object that encapsulates the
     *         information about the inference.
     */
    public DIGReasonerIdentity getIdentity() throws DIGReasonerException;


    /**
     * A helper method that asks the inference to create
     * a new knowledgebase.
     *
     * @return A <code>String</code> that represents a URI
     *         that is an identifier for the newly created knowledgebase.
     */
    public String createKnowledgeBase() throws DIGReasonerException;


    /**
     * A helper method that releases a previously created
     * knowledgebase.
     *
     * @param kbURI The <code>URI</code> of the knowledgebase
     */
    public void releaseKnowledgeBase(String kbURI) throws DIGReasonerException;


    /**
     * A helper method that clears the knowledge base
     *
     * @param kbURI The uri that identifies the knowledge
     *              base to be cleared.
     */
    public void clearKnowledgeBase(String kbURI) throws DIGReasonerException;


    /**
     * Sends a request to the reasoner and retrieves the response.
     *
     * @param request A <code>Document</code> containing the
     *                DIG request
     * @return A <code>Document</code> containing the reponse from the reasoner
     * @throws DIGReasonerException
     */
    public Document performRequest(Document request) throws DIGReasonerException;
}
