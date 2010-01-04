package edu.stanford.smi.protegex.owl.model.util;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Reference;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class that manages the (in) visibility of RDFSDatatypes, depending
 * on their usage.  Default datatypes (xsd:int etc) are always visible
 * unless the user has explicitly made them invisible, and other datatypes
 * are visible if they are used (have References to them).
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class XSDVisibility {

    public static Set<RDFSDatatype> getDefaultDatatypes(OWLModel owlModel) {
        Set<RDFSDatatype> set = new HashSet<RDFSDatatype>();
        set.add(owlModel.getXSDboolean());
        set.add(owlModel.getXSDfloat());
        set.add(owlModel.getXSDint());
        set.add(owlModel.getXSDstring());
        set.add(owlModel.getXSDdate());
        set.add(owlModel.getXSDdateTime());
        set.add(owlModel.getXSDtime());
        return set;
    }


    public static void updateVisibility(OWLModel owlModel) {
        Set<RDFSDatatype> defaultDatatypes = getDefaultDatatypes(owlModel);
        Iterator<RDFSDatatype> types = owlModel.getRDFSDatatypes().iterator();
        while (types.hasNext()) {
            RDFSDatatype datatype = types.next();
            if (datatype.isSystem() && !defaultDatatypes.contains(datatype)) {
                Collection<Reference> refs = ((KnowledgeBase) owlModel).getReferences(datatype, 10);
                boolean visible = false;
                for (Reference ref : refs) {
                    if (ref.getFrame().isSystem() 
                            && ref.getSlot().isSystem() 
                            && (ref.getFacet() == null || ref.getFacet().isSystem())) {
                        continue;
                    }
                    else {
                        visible = true;
                        break;
                    }
                }
                datatype.setVisible(visible);
            }
        }
    }
}
