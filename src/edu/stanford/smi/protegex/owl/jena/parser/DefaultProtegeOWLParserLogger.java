package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultProtegeOWLParserLogger implements ProtegeOWLParserLogger {
    private static transient final Logger log = Log.getLogger(DefaultProtegeOWLParserLogger.class);
    private int count = 0;


    private final static String PREFIX = "[ProtegeOWLParser] ";


    public void logImport(String uri, String physicalURL) {
        String msg = PREFIX + "Importing " + uri;
        if (!uri.equals(physicalURL)) {
            msg += " (from " + physicalURL + ")";
        }       
        log.info(msg);
    }


    public void logTripleAdded(RDFResource subject, RDFProperty predicate, Object object) {
        if (log.isLoggable(Level.FINER)) {
            if(object instanceof Frame) {
                log.finer(" + " + subject.getName() + " " + predicate.getName() + " " + ((Frame)object).getName());
            }
            else {
                log.finer(" + " + subject.getName() + " " + predicate.getName() + " " + object);
            }
        }
        if ((++count % 10000) == 0) {
            log.info(PREFIX + "Triple " + count);
        }
    }


    public void logWarning(String message) {
        log.warning(PREFIX + "Warning: " + message);
    }
}
