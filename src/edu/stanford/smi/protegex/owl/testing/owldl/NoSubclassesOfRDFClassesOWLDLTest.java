package edu.stanford.smi.protegex.owl.testing.owldl;

import com.hp.hpl.jena.vocabulary.RDF;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class NoSubclassesOfRDFClassesOWLDLTest extends AbstractOWLTest implements OWLDLTest, RDFSClassTest {

    public final static Set ILLEGAL_SYSTEM_CLASSES = new HashSet();


    static {
        ILLEGAL_SYSTEM_CLASSES.add(RDF.List.getURI());
    }


    public NoSubclassesOfRDFClassesOWLDLTest() {
        super(GROUP, null);
    }


    public List test(RDFSClass aClass) {
        List results = new ArrayList();
        if (aClass instanceof RDFSNamedClass) {
            for (Iterator it = aClass.getSuperclasses(false).iterator(); it.hasNext();) {
                Cls superCls = (Cls) it.next();
                if (superCls instanceof RDFSNamedClass) {
                    RDFSNamedClass rdfsClass = (RDFSNamedClass) superCls;
                    if (ILLEGAL_SYSTEM_CLASSES.contains(rdfsClass.getURI()) || !(superCls instanceof OWLNamedClass)) {
                        results.add(new DefaultOWLTestResult("OWL DL does not support subclasses of RDF(S) classes: The class " +
                                aClass.getBrowserText() + " is a subclass of " + superCls.getBrowserText() + ".",
                                aClass,
                                OWLTestResult.TYPE_OWL_FULL,
                                this));
                    }
                }
            }
        }
        return results;
    }
}
