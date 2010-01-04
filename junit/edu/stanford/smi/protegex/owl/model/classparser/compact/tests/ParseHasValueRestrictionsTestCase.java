package edu.stanford.smi.protegex.owl.model.classparser.compact.tests;

import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.tests.AbstractOWLModelTestCase;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

public class ParseHasValueRestrictionsTestCase extends AbstractOWLModelTestCase {

    private OWLClassParser parser = new CompactOWLClassParser();


    public void testParseHasValueBoolean() throws Exception {
        owlModel.createOWLDatatypeProperty("rich", owlModel.getXSDboolean());
        String expression = "rich $ true";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLHasValue);
        OWLHasValue restriction = (OWLHasValue) aClass;
        assertEquals(restriction.getOnProperty().getName(), "rich");
        assertEquals(restriction.getHasValue(), Boolean.TRUE);
    }


    public void testParseHasValueString() throws Exception {
        owlModel.createOWLDatatypeProperty("name", owlModel.getXSDstring());
        String expression = "name $ \"Hans Eichel\"";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLHasValue);
        OWLHasValue restriction = (OWLHasValue) aClass;
        assertEquals(restriction.getOnProperty().getName(), "name");
        assertEquals(restriction.getHasValue(), "Hans Eichel");
    }


    public void testParseHasClassInOWLFull() throws Exception {
        ProfilesManager.setProfile(owlModel, OWLProfiles.OWL_Full.getURI());
        OWLNamedClass namedCls = owlModel.createOWLNamedClass("Cls");
        owlModel.createOWLObjectProperty("slot");
        String expression = "slot $ Cls";
        RDFSClass aClass = parser.parseClass(owlModel, expression);
        assertTrue(aClass instanceof OWLHasValue);
        OWLHasValue restriction = (OWLHasValue) aClass;
        assertEquals(restriction.getOnProperty().getName(), "slot");
        assertEquals(restriction.getHasValue(), namedCls);
    }


    public void testParseHasClassInOWLDL() throws Exception {
        ProfilesManager.setProfile(owlModel, OWLProfiles.OWL_DL.getURI());
        owlModel.createOWLNamedClass("Cls");
        owlModel.createOWLObjectProperty("slot");
        String expression = "slot $ Cls";
        try {
            parser.parseClass(owlModel, expression);
            assertFalse("Expected failure!", true);
        }
        catch (Exception ex) {
            // Expected
        }
    }


    public void testParseIllegalHasFiller1() {
        owlModel.createOWLObjectProperty("slot");
        String expression = "slot $ 42";
        try {
            parser.parseClass(owlModel, expression);
            assertTrue(false);
        }
        catch (Exception ex) {
            // Expected
        }
    }


    public void testParseIllegalHasFiller2() {
        owlModel.createOWLDatatypeProperty("slot");
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        cls.createInstance("instance");
        String expression = "slot $ instance";
        try {
            parser.parseClass(owlModel, expression);
            assertTrue(false);
        }
        catch (Exception ex) {
            // Expected
        }
    }
}
