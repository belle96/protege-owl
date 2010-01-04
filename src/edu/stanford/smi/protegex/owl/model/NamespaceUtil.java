package edu.stanford.smi.protegex.owl.model;

import com.hp.hpl.jena.rdf.model.impl.Util;

import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.model.impl.OWLNamespaceManager;

public class NamespaceUtil {
    
    public static final String PREFIX_TO_NAME_SEPARATOR = ":";

	public static String getNameSpace(String fullURI) {
		if (!URIUtilities.isAbsoluteURI(fullURI)) {
			return null;
		}
		return fullURI.substring(0, Util.splitNamespace(fullURI) );
	}

	
	public static String getLocalName(String fullURI)	{
		if (fullURI == null) { return null; }
		if (!URIUtilities.isAbsoluteURI(fullURI)) {
			return fullURI;
		}
		return fullURI.substring(Util.splitNamespace(fullURI) );
	}
	
	public static String getPrefixForResourceName(OWLModel owlModel, String fullURI) {
	    String namespace = getNameSpace(fullURI);
	    return owlModel.getNamespaceManager().getPrefix(namespace);
	}
	
	public static String getPrefixedName(OWLModel owlModel, String fullURI)	{	
	    return getPrefixedName(owlModel.getNamespaceManager(), fullURI);
	}
	
	
	public static String getPrefixedName(NamespaceManager names, String fullURI)   {
		String uri = getNameSpace(fullURI);

		if (uri == null) {
			return fullURI;
		}

		String localName = getLocalName(fullURI);
		
		
		String prefix = names.getPrefix(uri);

		if (prefix == null) {
			return fullURI; //do we want another strategy here?
		} else if (prefix.equals(OWLNamespaceManager.DEFAULT_NAMESPACE_PREFIX)) {
			return localName;
		} else {
			StringBuffer buffer = new StringBuffer();
			buffer.append(prefix);
			buffer.append(':');
			buffer.append(localName);
			return buffer.toString();
		}
	}
	
	public static String getFullName(OWLModel owlModel, String prefixedName) {
		int ind = prefixedName.indexOf(PREFIX_TO_NAME_SEPARATOR);
		
		NamespaceManager nsm = owlModel.getNamespaceManager();
		
		if (ind == -1) { //no ":" in the prefixed name, this is a name in the default namespace
			String defaultNamespace = nsm.getDefaultNamespace();
			
			if (defaultNamespace == null) {
				return prefixedName;
			}
			
			StringBuffer buffer = new StringBuffer();
			buffer.append(defaultNamespace);
			buffer.append(prefixedName);
			
			return buffer.toString();	
		}
						
		if (ind == 0) {//e.g. ":protege" - what to do in this case?
			//TODO: what should happen in this case?
			return prefixedName;
		}
		
		String prefix = prefixedName.substring(0, ind);
		String localName = prefixedName.substring(ind + 1);
		
		String namespace = nsm.getNamespaceForPrefix(prefix);
		
		if (namespace == null) { // no namespace for prefix defined
			return null;
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(namespace);
		buffer.append(localName);
		
		return buffer.toString();	
				
	}
	
}
