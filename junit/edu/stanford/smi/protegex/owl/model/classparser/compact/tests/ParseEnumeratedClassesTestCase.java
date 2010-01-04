package edu.stanford.smi.protegex.owl.model.classparser.compact.tests;

import java.io.File;
import java.util.Collection;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.compact.CompactOWLClassParser;
import edu.stanford.smi.protegex.owl.tests.AbstractOWLModelTestCase;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ParseEnumeratedClassesTestCase extends AbstractOWLModelTestCase {
    
    static {
  	  if (!ProtegeOWL.getPluginFolder().exists()) { // hack for users in ide's
		  ProtegeOWL.setPluginFolder(new File("etc"));
	  }
    }

    private OWLClassParser parser = new CompactOWLClassParser();


    public void testParseEnumerationOfIndividuals()  {
    	try {
    		ProfilesManager.setProfile(owlModel, OWLProfiles.OWL_DL.getURI());
    		OWLNamedClass genderCls = owlModel.createOWLNamedClass("Gender");
    		genderCls.createInstance("male");
    		genderCls.createInstance("female");
    		String expression = "{male female}";
    		RDFSClass aClass = parser.parseClass(owlModel, expression);
    		assertTrue(aClass instanceof OWLEnumeratedClass);
    		OWLEnumeratedClass enumeratedClass = (OWLEnumeratedClass) aClass;
    		Collection resources = enumeratedClass.getOneOf();
    		assertEquals(2, resources.size());
    		assertTrue(resources.contains(owlModel.getRDFResourceByBrowserText("male")));
    		assertTrue(resources.contains(owlModel.getRDFResourceByBrowserText("female")));
    	}
    	catch (Exception e) {
    		fail();
    	}
    }


    public void testParseEnumerationOfClassesAndSlots() {
    	try {
    		ProfilesManager.setProfile(owlModel, OWLProfiles.OWL_Full.getURI());
    		OWLNamedClass maleCls = owlModel.createOWLNamedClass("Male");
    		OWLObjectProperty slot = owlModel.createOWLObjectProperty("hasChildren");
    		String expression = "{Male hasChildren}";
    		RDFSClass aClass = parser.parseClass(owlModel, expression);
    		assertTrue(aClass instanceof OWLEnumeratedClass);
    		OWLEnumeratedClass enumeratedClass = (OWLEnumeratedClass) aClass;
    		Collection resources = enumeratedClass.getOneOf();
    		assertEquals(2, resources.size());
    		assertTrue(resources.contains(maleCls));
    		assertTrue(resources.contains(slot));
    	}
    	catch (Exception e) {
    		fail();
    	}
    }


    public void testParseEnumerationOfClassesAndSlotsInOWLDL() {
    	try {
    		ProfilesManager.setProfile(owlModel, OWLProfiles.OWL_DL.getURI());
    		owlModel.createOWLNamedClass("Male");
    		owlModel.createOWLObjectProperty("hasChildren");
    		String expression = "{Male hasChildren}";
    		try {
    			parser.parseClass(owlModel, expression);
    			assertFalse("Expected to fail parsing!", true);
    		}
    		catch (Exception ex) {
    		}
    	}
    	catch (Exception e) {
    		fail();
    	}
    }
}
