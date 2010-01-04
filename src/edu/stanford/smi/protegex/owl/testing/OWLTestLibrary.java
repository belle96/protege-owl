package edu.stanford.smi.protegex.owl.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import edu.stanford.smi.protege.plugin.PluginUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;

/**
 * A static utility class that manages the available OWLTests on this machine.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLTestLibrary {

    private static Map testMap = new HashMap();

    private static Set userDefinedClasses = new HashSet();


    /**
     * A work-around method for OWLTests from outside the OWL Plugin.
     * Since due to a bug in the Java virtual machine these Classes
     * can not be found through their manifest, they need to be added
     * manually, preferably through a ProjectPlugin.
     *
     * @param clazz the Class of an OWLTest to add
     */
    public static void addOWLTestClass(Class clazz) {
        userDefinedClasses.add(clazz);
    }


    private static OWLTest createOWLTest(Class clazz) {
        try {
            return (OWLTest) clazz.newInstance();
        }
        catch (Exception ex) {
            Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
            return null;
        }
    }


    public static OWLTest getOWLTest(Class clazz) {
        OWLTest test = (OWLTest) testMap.get(clazz);
        if (test == null) {
            test = createOWLTest(clazz);
            testMap.put(clazz, test);
        }
        return test;
    }


    public static Class[] getOWLTestClasses() {
        Collection clses = new ArrayList(PluginUtilities.getClassesWithAttribute("OWLTest", "True"));
        clses.addAll(userDefinedClasses);
        return (Class[]) clses.toArray(new Class[0]);
    }


    public static boolean isOWLDLTest(Class clazz) {
        OWLTest test = getOWLTest(clazz);
        return test instanceof OWLDLTest;
    }


    /**
     * Repairs all OWLSlotTests that are marked as AutoRepairableOWLTest on a given OWLProperty.
     *
     * @param property the OWLProperty to repair
     */
    public static void repairRDFPropertyTests(RDFProperty property) {
        OWLModel owlModel = property.getOWLModel();
        OWLTest[] tests = owlModel.getOWLTests();
        for (int i = 0; i < tests.length; i++) {
            OWLTest test = tests[i];
            if (test instanceof RDFPropertyTest && test instanceof AutoRepairableOWLTest) {
                final RDFPropertyTest owlSlotTest = ((RDFPropertyTest) test);
                repairOWLSlotTest(owlSlotTest, property);
                //if(property.getInverseSlot() instanceof OWLProperty) {
                //repairOWLSlotTest(owlSlotTest, (OWLProperty)property.getInverseSlot());
                //}
            }
        }
    }


    private static void repairOWLSlotTest(final RDFPropertyTest owlSlotTest, RDFProperty property) {
        List failures = owlSlotTest.test(property);
        for (Iterator it = failures.iterator(); it.hasNext();) {
            OWLTestResult result = (OWLTestResult) it.next();
            ((RepairableOWLTest) owlSlotTest).repair(result);
        }
    }
}
