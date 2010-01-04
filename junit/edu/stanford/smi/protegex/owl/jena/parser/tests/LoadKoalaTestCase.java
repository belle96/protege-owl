package edu.stanford.smi.protegex.owl.jena.parser.tests;

import java.util.Iterator;
import java.util.logging.Level;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.jena.parser.FrameCreatorUtility;
import edu.stanford.smi.protegex.owl.jena.parser.LogicalClassCreatorUtility;
import edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser;
import edu.stanford.smi.protegex.owl.jena.parser.RestrictionCreatorUtility;
import edu.stanford.smi.protegex.owl.jena.parser.TripleProcessor;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadKoalaTestCase extends AbstractJenaTestCase {
    
    public static void enableDebug() {
        Log.setLoggingLevel(TripleProcessor.class, Level.FINEST);
        Log.setLoggingLevel(ProtegeOWLParser.class, Level.FINEST);
        Log.setLoggingLevel(FrameCreatorUtility.class, Level.FINEST);
        Log.setLoggingLevel(LogicalClassCreatorUtility.class, Level.FINEST);
        Log.setLoggingLevel(RestrictionCreatorUtility.class, Level.FINEST);
    }
    

    public void testLoadKoala() throws Exception {

        loadRemoteOntology("koala.owl");

        OWLObjectProperty hasChildrenSlot = owlModel.getOWLObjectProperty("hasChildren");
        OWLObjectProperty hasDegreeSlot = owlModel.getOWLObjectProperty("hasDegree");
        OWLObjectProperty hasGenderSlot = owlModel.getOWLObjectProperty("hasGender");
        OWLObjectProperty hasHabitatSlot = owlModel.getOWLObjectProperty("hasHabitat");
        OWLDatatypeProperty isHardWorkingSlot = owlModel.getOWLDatatypeProperty("isHardWorking");

        OWLNamedClass femaleCls = owlModel.getOWLNamedClass("Female");
        OWLNamedClass genderCls = owlModel.getOWLNamedClass("Gender");
        OWLNamedClass animalCls = owlModel.getOWLNamedClass("Animal");
        OWLNamedClass marsupialsCls = owlModel.getOWLNamedClass("Marsupials");
        OWLNamedClass koalaCls = owlModel.getOWLNamedClass("Koala");
        OWLNamedClass koalaWithPhDCls = owlModel.getOWLNamedClass("KoalaWithPhD");
        OWLNamedClass quokkaCls = owlModel.getOWLNamedClass("Quokka");
        OWLNamedClass tasmanianDevilCls = owlModel.getOWLNamedClass("TasmanianDevil");
        OWLNamedClass parentCls = owlModel.getOWLNamedClass("Parent");
        OWLNamedClass personCls = owlModel.getOWLNamedClass("Person");
        OWLNamedClass studentCls = owlModel.getOWLNamedClass("Student");
        OWLNamedClass graduateStudentCls = owlModel.getOWLNamedClass("GraduateStudent");
        OWLNamedClass maleStudentWith3DaughtersCls = owlModel.getOWLNamedClass("MaleStudentWith3Daughters");
        OWLNamedClass habitatCls = owlModel.getOWLNamedClass("Habitat");
        OWLNamedClass forestCls = owlModel.getOWLNamedClass("Forest");
        OWLNamedClass dryEucalypForestCls = owlModel.getOWLNamedClass("DryEucalyptForest");
        OWLNamedClass rainforestCls = owlModel.getOWLNamedClass("Rainforest");
        OWLNamedClass universityCls = owlModel.getOWLNamedClass("University");
        OWLNamedClass degreeCls = owlModel.getOWLNamedClass("Degree");
        OWLNamedClass maleCls = owlModel.getOWLNamedClass("Male");

        Instance femaleInstance = owlModel.getOWLIndividual("female");
        Instance maleInstance = owlModel.getOWLIndividual("male");
        Instance baInstance = owlModel.getOWLIndividual("BA");
        Instance bsInstance = owlModel.getOWLIndividual("BS");
        Instance maInstance = owlModel.getOWLIndividual("MA");
        Instance phdInstance = owlModel.getOWLIndividual("PhD");

        assertNotNull(hasChildrenSlot);
        assertNotNull(hasDegreeSlot);
        assertNotNull(hasGenderSlot);
        assertNotNull(hasHabitatSlot);
        assertNotNull(isHardWorkingSlot);
        assertNotNull(femaleCls);
        assertNotNull(genderCls);
        assertNotNull(animalCls);
        assertNotNull(marsupialsCls);
        assertNotNull(koalaCls);
        assertNotNull(koalaWithPhDCls);
        assertNotNull(quokkaCls);
        assertNotNull(tasmanianDevilCls);
        assertNotNull(parentCls);
        assertNotNull(personCls);
        assertNotNull(studentCls);
        assertNotNull(graduateStudentCls);
        assertNotNull(maleStudentWith3DaughtersCls);
        assertNotNull(habitatCls);
        assertNotNull(forestCls);
        assertNotNull(dryEucalypForestCls);
        assertNotNull(rainforestCls);
        assertNotNull(universityCls);
        assertNotNull(degreeCls);
        assertNotNull(maleCls);
        assertNotNull(femaleInstance);
        assertNotNull(maleInstance);
        assertNotNull(baInstance);
        assertNotNull(bsInstance);
        assertNotNull(maInstance);
        assertNotNull(phdInstance);

        assertHasDirectSuperclass(femaleCls, "hasGender has female");
        assertHasDirectSuperclass(animalCls, "hasGender exactly 1");
        assertHasDirectSuperclass(animalCls, "hasHabitat min 1");
        assertHasDirectSuperclass(koalaCls, "hasHabitat some DryEucalyptForest");
        assertHasDirectSuperclass(koalaCls, "isHardWorking has false");
        assertHasDirectDefinitionCls(koalaWithPhDCls, "hasDegree has PhD");
        assertHasDirectSuperclass(quokkaCls, "isHardWorking has true");
        assertHasDirectDefinitionCls(parentCls, "hasChildren min 1");
        assertHasDirectDefinitionCls(studentCls, "hasHabitat some University");
        assertHasDirectDefinitionCls(studentCls, "isHardWorking has true");
        assertHasDirectSuperclass(graduateStudentCls, "hasDegree some {BA BS}");
        assertHasDirectSuperclass(maleCls, "hasGender has male");

        assertEquals("1.1", animalCls.getPropertyValue(owlModel.getOWLVersionInfoProperty()));
    }


    private void assertHasDirectSuperclass(RDFSNamedClass aClass, String expression) {
        for (Iterator it = aClass.getSuperclasses(false).iterator(); it.hasNext();) {
            Cls superCls = (Cls) it.next();
            String browserText = superCls.getBrowserText();
            if (expression.equals(browserText)) {
                return;
            }
        }
        Log.getLogger().warning("Superclasses of " + aClass.getBrowserText() + ":");
        for (Iterator it = aClass.getSuperclasses(false).iterator(); it.hasNext();) {
            Cls cls = (Cls) it.next();
            Log.getLogger().info("- " + cls.getBrowserText());
        }
        assertTrue(aClass.getBrowserText() + " does not have superclass " + expression, false);
    }


    private void assertHasDirectDefinitionCls(OWLNamedClass cls, String expression) {
        OWLIntersectionClass intersectionCls =
                (OWLIntersectionClass) cls.getEquivalentClasses().iterator().next();
        for (Iterator it = intersectionCls.getOperands().iterator(); it.hasNext();) {
            Cls superCls = (Cls) it.next();
            String browserText = superCls.getBrowserText();
            if (expression.equals(browserText)) {
                return;
            }
        }
        assertTrue(cls.getBrowserText() + " does not have definition class " + expression, false);
    }
}
