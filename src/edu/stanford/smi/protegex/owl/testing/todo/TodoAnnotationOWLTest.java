package edu.stanford.smi.protegex.owl.testing.todo;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.testing.AbstractOWLTest;
import edu.stanford.smi.protegex.owl.testing.DefaultOWLTestResult;
import edu.stanford.smi.protegex.owl.testing.OWLTestResult;
import edu.stanford.smi.protegex.owl.testing.RDFResourceTest;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TodoAnnotationOWLTest extends AbstractOWLTest implements RDFResourceTest {

    public final static String GROUP = "Maintenance Tests";


    public TodoAnnotationOWLTest() {
        super(GROUP, null);
    }


    public String getName() {
        return "Find TODO list items";
    }


    public static List getTODOListItems(RDFResource instance) {
        List results = new ArrayList();
        OWLModel owlModel = instance.getOWLModel();
        RDFProperty property = owlModel.getTodoAnnotationProperty();
        String prefix = owlModel.getTodoAnnotationPrefix();
        for (Iterator it = instance.getPropertyValues(property).iterator(); it.hasNext();) {
            Object value = it.next();
            String str = null;
            if (value instanceof RDFSLiteral) {
                str = ((RDFSLiteral) value).getString();
                value = str;
            }
            else {
                str = value.toString();
            }
            if (str.startsWith(prefix)) {
                results.add(value);
            }
        }
        return results;
    }


    public List test(RDFResource instance) {
        List items = getTODOListItems(instance);
        if (items.size() > 0) {
            List results = new ArrayList();
            for (Iterator it = items.iterator(); it.hasNext();) {
                final String str = (String) it.next();
                results.add(new DefaultOWLTestResult(str,
                        instance,
                        OWLTestResult.TYPE_WARNING,
                        this,
                        OWLIcons.getTODOIcon()) {
                    public String toString() {
                        return "TODO (at " + getHost().getBrowserText() + "): " + str;
                    }
                });
            }
            return results;
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
