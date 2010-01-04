package edu.stanford.smi.protegex.owl.model.classparser;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;

import java.util.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 16, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DatatypeNameChecker {

	private Map names;

	public DatatypeNameChecker(OWLModel owlModel) {
		names = new HashMap();
		for(Iterator it = owlModel.getRDFSDatatypes().iterator(); it.hasNext(); ) {
			RDFSDatatype datatype = (RDFSDatatype) it.next();
			names.put(datatype.getBrowserText(), datatype.getName());
		}
	}

	public boolean isDatatypeName(String name) {
		return names.keySet().contains(name);
	}

	public String getDatatypeQName(String name) {
		return (String) names.get(name);
	}

}

