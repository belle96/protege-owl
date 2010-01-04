package edu.stanford.smi.protegex.owl.jena.parser;

import edu.stanford.smi.protege.exception.OntologyLoadException;

public class ProtegeOWLParserException extends OntologyLoadException {

	public ProtegeOWLParserException(Throwable t, String message,
			String suggestion) {
		super(t, message, suggestion);	
	}

}
