package edu.stanford.smi.protegex.owl.jena.graph;

import com.hp.hpl.jena.shared.PrefixMapping;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.triplestore.TripleStore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegePrefixMapping implements PrefixMapping {

    private OWLModel owlModel;

    private TripleStore ts;


    public ProtegePrefixMapping(OWLModel owlModel, TripleStore ts) {
        this.owlModel = owlModel;
        this.ts = ts;
    }


    public PrefixMapping setNsPrefix(String string, String string1) {
        throw new RuntimeException("Not supported yet");
    }


    public PrefixMapping removeNsPrefix(String string) {
        throw new RuntimeException("Not supported yet");
    }


    public PrefixMapping setNsPrefixes(PrefixMapping prefixMapping) {
        throw new RuntimeException("Not supported yet");
    }


    public PrefixMapping setNsPrefixes(Map map) {
        throw new RuntimeException("Not supported yet");
    }


    public PrefixMapping withDefaultMappings(PrefixMapping prefixMapping) {
        return this;
    }


    public String getNsPrefixURI(String prefix) {
        if (prefix.length() == 0) {
            return ts.getDefaultNamespace();
        }
        else {
            return ts.getNamespaceForPrefix(prefix);
        }
    }


    public String getNsURIPrefix(String uri) {
        return ts.getPrefix(uri);
    }


    public Map getNsPrefixMap() {
        Map map = new HashMap();
        for (Iterator it = ts.getPrefixes().iterator(); it.hasNext();) {
            String prefix = (String) it.next();
            String uri = ts.getNamespaceForPrefix(prefix);
            map.put(prefix, uri);
        }
        map.put("", ts.getDefaultNamespace());
        return map;
    }


    public String expandPrefix(String qname) {
        String prefix = owlModel.getPrefixForResourceName(qname);
        if (prefix == null) {
            prefix = "";
        }
        String namespace = owlModel.getNamespaceManager().getNamespaceForPrefix(prefix);
        if (namespace != null) {
            String uri = owlModel.getURIForResourceName(qname);
            if (uri != null) {
                return uri;
            }
        }
        return qname;
    }


    public String shortForm(String uri) {
        String namespace = owlModel.getNamespaceForURI(uri);
        if (namespace != null) {
            String prefix = owlModel.getNamespaceManager().getPrefix(namespace);
            if (prefix != null) {
                String localName = owlModel.getLocalNameForURI(uri);
                if (localName != null) {
                    if (prefix.length() > 0) {
                        return prefix + ProtegeNames.PREFIX_LOCALNAME_SEPARATOR + localName;
                    }
                    else {
                        return localName;
                    }
                }
            }
        }
        return uri;
    }


    public String usePrefix(String string) {
        throw new RuntimeException("Not supported yet");
    }


    public String qnameFor(String string) {
        throw new RuntimeException("Not supported yet");
    }


    public PrefixMapping lock() {
        throw new RuntimeException("Not supported yet");
    }


    public boolean samePrefixMappingAs(PrefixMapping arg0) {
      throw new UnsupportedOperationException("Not implemented yet");
    }
}
