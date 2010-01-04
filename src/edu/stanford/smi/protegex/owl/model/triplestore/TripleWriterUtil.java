package edu.stanford.smi.protegex.owl.model.triplestore;

import edu.stanford.smi.protegex.owl.model.OWLModel;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TripleWriterUtil {

    /**
     * Writes all prefixes of a given OWLModel into a given TripleWriter.
     * This will iterate over the prefixes defined in the OWLModel's NamespaceManager
     * and then call the corresponding write method to the writer.
     * Note that this does not write the default namespace (which is typically required).
     *
     * @param writer   the target writer
     * @param owlModel the OWLModel to get the prefixes from
     * @throws Exception if the writer failed
     * @see TripleWriter#writePrefix
     */
    public static void writePrefixes(TripleWriter writer, OWLModel owlModel) throws Exception {
        Iterator prefixes = owlModel.getNamespaceManager().getPrefixes().iterator();
        while (prefixes.hasNext()) {
            String prefix = (String) prefixes.next();
            String namespace = owlModel.getNamespaceManager().getNamespaceForPrefix(prefix);
            writer.writePrefix(prefix, namespace);
        }
    }
}
