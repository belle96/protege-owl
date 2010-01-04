package edu.stanford.smi.protegex.owl.model.classparser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.classparser.dl.DLSyntaxParser;
import edu.stanford.smi.protegex.owl.model.classparser.manchester.ManchesterOWLParser;

public class LabelModeParseTestCase extends TestCase {
    
  static {
      ProtegeOWL.setPluginFolder(new File("etc"));
  }
  
  @SuppressWarnings("unchecked")
  private OWLModel getKb() {
    List errors = new ArrayList();
    Project p = new Project("junit/projects/parseWithLables.pprj", errors);
    if (!errors.isEmpty()) {
      fail();
    }
    OWLModel owlModel =  (OWLModel) p.getKnowledgeBase();
    owlModel.setExpandShortNameInMethods(true);
    return owlModel;
  }

  
  public void testManchesterParser() 
  throws edu.stanford.smi.protegex.owl.model.classparser.manchester.ParseException {
    OWLModel model = getKb();

    OWLSomeValuesFrom c1 = (OWLSomeValuesFrom) ManchesterOWLParser.parseClass(model, "XSB0020 some E");
    assertNotNull(c1);
    OWLSomeValuesFrom c2 = (OWLSomeValuesFrom) ManchesterOWLParser.parseClass(model, "f some E");
    assertNotNull(c2);
    OWLSomeValuesFrom c3 = (OWLSomeValuesFrom) ManchesterOWLParser.parseClass(model, "f some XSB0004");
    assertNotNull(c3);
    
    assertEquals(c1.getFiller(), c2.getFiller());
    assertEquals(c1.getOnProperty(), c2.getOnProperty());
    assertEquals(c1.getFiller(), c3.getFiller());
    assertEquals(c1.getOnProperty(), c3.getOnProperty());
    
    OWLSomeValuesFrom c4 = (OWLSomeValuesFrom) ManchesterOWLParser.parseClass(model, "f some C");
    assertNotNull(c4);
    OWLSomeValuesFrom c5 = (OWLSomeValuesFrom) ManchesterOWLParser.parseClass(model, "f some XSB0003");
    assertNotNull(c5);
    
    assertEquals(c4.getFiller(), c5.getFiller());
    assertEquals(c4.getOnProperty(), c5.getOnProperty());
  }
  
  
  public void testDLSyntaxParser() 
  throws edu.stanford.smi.protegex.owl.model.classparser.dl.ParseException {
    OWLModel model = getKb();

    OWLSomeValuesFrom c1 = (OWLSomeValuesFrom) DLSyntaxParser.parseExpression(model, "? XSB0020 E", true);
    assertNotNull(c1);
    OWLSomeValuesFrom c2 = (OWLSomeValuesFrom) DLSyntaxParser.parseExpression(model, "? f E", true);
    assertNotNull(c2);
    OWLSomeValuesFrom c3 = (OWLSomeValuesFrom) DLSyntaxParser.parseExpression(model, "? f XSB0004", true);
    assertNotNull(c3);
    
    assertEquals(c1.getFiller(), c2.getFiller());
    assertEquals(c1.getOnProperty(), c2.getOnProperty());
    assertEquals(c1.getFiller(), c3.getFiller());
    assertEquals(c1.getOnProperty(), c3.getOnProperty());
    
    OWLSomeValuesFrom c4 = (OWLSomeValuesFrom) DLSyntaxParser.parseExpression(model, "? f C", true);
    assertNotNull(c4);
    OWLSomeValuesFrom c5 = (OWLSomeValuesFrom) DLSyntaxParser.parseExpression(model, "? f XSB0003", true);
    assertNotNull(c5);
    
    assertEquals(c4.getFiller(), c5.getFiller());
    assertEquals(c4.getOnProperty(), c5.getOnProperty());
  }
  
  public void testDLSyntaxNestedConstructs() 
  throws edu.stanford.smi.protegex.owl.model.classparser.dl.ParseException {
    OWLModel model = getKb();
    assertNotNull(DLSyntaxParser.parseExpression(model, "* f (C & ? f E)", true));
    assertNotNull(DLSyntaxParser.parseExpression(model, "* f (C & ( ? f E ))", true));
  }
  
  public void testDLSyntaxParserEnumerations() 
  throws edu.stanford.smi.protegex.owl.model.classparser.dl.ParseException {
      
    OWLModel model = getKb();
    
    OWLEnumeratedClass c;
    c = (OWLEnumeratedClass) DLSyntaxParser.parseExpression(model, "{ e1 e2 e3 e4 }", true);
    assertEquals(c.getOneOf().size(),4);
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0030")));
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0031")));   
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0032"))); 
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0033")));
    
    c = (OWLEnumeratedClass) DLSyntaxParser.parseExpression(model, "{ e1 e2 e3 XSB0033 }", true);
    assertEquals(c.getOneOf().size(),4);
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0030")));
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0031")));   
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0032"))); 
    assertTrue(c.getOneOf().contains(model.getOWLIndividual("XSB0033")));
  }
}
