package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.UUID;

import com.hp.hpl.jena.rdf.arp.AResource;

import edu.stanford.smi.protege.model.DefaultKnowledgeBase;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.framestore.SimpleFrameStore;
import edu.stanford.smi.protegex.owl.model.impl.AbstractOWLModel;

public class ParserUtil {
    
    public static final String PARSERS_UNIQUE_SESSION_ID = UUID.randomUUID().toString().replace("-", "_");


	public static String getResourceName(AResource resource) {
		if (resource.isAnonymous()) {
		    /*
		     * Argh...  Must ensure that this cannot conflict with owlModel.getNextAnonymousId();
		     *          See the stuff involving the creation of logical named classes in the
		     *          TripleFrameCache code.  This is *nasty* but I don't yet the better way.
		     */
			StringBuffer buffer = new StringBuffer(AbstractOWLModel.ANONYMOUS_BASE);
			buffer.append(resource.getAnonymousID());
			buffer.append("_");
			buffer.append(PARSERS_UNIQUE_SESSION_ID);
			return buffer.toString();
		} else {
			return resource.getURI();
		}
	}

	public static SimpleFrameStore getSimpleFrameStore(KnowledgeBase kb) {
	    return (SimpleFrameStore) ((DefaultKnowledgeBase) kb).getTerminalFrameStore();
	}


	public static SimpleFrameStore getSimpleFrameStore(Frame frame) {
	    return getSimpleFrameStore(frame.getKnowledgeBase());
	}

}
