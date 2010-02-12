
package edu.stanford.smi.protegex.owl.swrl.test;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.bridge.BridgeFactory;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineBridge;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.ObjectValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngine;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

public class SQWRLTest
{
  private static JenaOWLModel owlModel;

  public static void main(String args[]) 
  {
    OWLModel owlModel;
    SWRLRuleEngineBridge ruleEngine;
    String owlFileName = "";

    if (args.length == 1) {
      owlFileName = args[0];
    } else Usage();
    
    try {
      owlModel = SWRLOWLUtil.createJenaOWLModel(owlFileName);
      ruleEngine = BridgeFactory.createBridge(owlModel);
      SWRLFactory factory = new SWRLFactory(owlModel);
      SQWRLQueryEngine queryEngine = SQWRLQueryEngineFactory.create(owlModel);
      SQWRLResult result;

      /*
        ruleEngine.infer();
        
        for (OWLAxiom axiom : ruleEngine.getInferredAxioms()) {
        if (axiom instanceof OWLDatatypePropertyAssertionAxiom) {
        OWLDatatypePropertyAssertionAxiom da = (OWLDatatypePropertyAssertionAxiom)axiom;
        }
        }
      */

      // SQWRLResult result = ruleEngine.getSQWRLResult("Rule-6");

      //      SWRLImp imp = factory.createImp("T1", "Adult(?a) " + SWRLParser.IMP_CHAR + " sqwrl:select(?a)");
      SWRLImp imp = factory.createImp("T1", "Adult(?a) . sqwrl:makeSet(?s, ?a) . sqwrl:contains(?s, ?e) -> sqwrl:select(?e)");

      result = queryEngine.runSQWRLQuery("T1");

      while (result.hasNext()) {
        ObjectValue e = result.getObjectValue("?e");
        System.err.println("value: e=" + e);
        result.next();
      } // while

      result = queryEngine.runSQWRLQuery("T1");

      while (result.hasNext()) {
        ObjectValue e = result.getObjectValue("?e");
        System.err.println("value: e=" + e);
        result.next();
      } // while

    } catch (SQWRLException e) {
      System.err.println("SQWRL exception: " + e.getMessage());
      e.printStackTrace();
    } catch (SWRLRuleEngineException e) {
      System.err.println("Rule engine exception: " + e.getMessage());
      e.printStackTrace();
    } catch (SWRLParseException e) {
      System.err.println("Parse exception: " + e.getMessage());
      e.printStackTrace();
    } catch (SWRLOWLUtilException e) {
      System.err.println("Exception: " + e.getMessage());
      e.printStackTrace();
    } // try
  } // main

  private static void Usage()
  {
    System.err.println("Usage: SQWRLTest <owlFileName>");
    System.exit(1);
  } // Usage

} // SQWRLTest
