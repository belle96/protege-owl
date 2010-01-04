package edu.stanford.smi.protegex.owl.testing.todo;

import edu.stanford.smi.protegex.owl.model.Deprecatable;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.testing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DeprecationOWLTest extends AbstractOWLTest implements RDFSClassTest, RDFPropertyTest {

    public String getDocumentation() {
        return "<HTML>Finds all classes and properties that are marked as deprecated " +
                "using owl:DeprecatedClass or owl:DeprecatedProperty.</HTML>";
    }


    public String getGroup() {
        return TodoAnnotationOWLTest.GROUP;
    }


    public String getName() {
        return "List deprecated classes and properties";
    }


    public List test(RDFProperty property) {
        if (property instanceof Deprecatable && ((Deprecatable) property).isDeprecated()) {
            return Collections.singletonList(new DefaultOWLTestResult("Property " + property.getBrowserText() +
                    " has been deprecated.",
                    property,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        return Collections.EMPTY_LIST;
    }


    public List test(RDFSClass aClass) {
        if (aClass instanceof Deprecatable && ((Deprecatable) aClass).isDeprecated()) {
            return Collections.singletonList(new DefaultOWLTestResult("Class " + aClass.getBrowserText() +
                    " has been deprecated.",
                    aClass,
                    OWLTestResult.TYPE_WARNING,
                    this));
        }
        return Collections.EMPTY_LIST;
    }
}
