package edu.stanford.smi.protegex.owl.model.classparser;

import edu.stanford.smi.protegex.owl.model.RDFProperty;

/**
 * An Exception thrown by an OWLClassParser, providing additional information that can be
 * used to guide user input.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLClassParseException extends Exception {

    public String currentToken;

    public boolean nextCouldBeClass;

    public boolean nextCouldBeIndividual;

    public boolean nextCouldBeProperty;

	public boolean nextCouldBeDatatypeName;

    public RDFProperty recentHasValueProperty;

    private static OWLClassParseException recentInstance = new OWLClassParseException("");


    public OWLClassParseException(String message) {
        super(message);
        recentInstance = this;
    }


    /**
     * Gets the most recently created exception of this type.
     * This can be used to guide input in text fields etc.
     *
     * @return the most recently created exception
     */
    public static OWLClassParseException getRecentInstance() {
        return recentInstance;
    }
}
