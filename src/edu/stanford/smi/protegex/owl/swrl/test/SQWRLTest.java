
package edu.stanford.smi.protegex.owl.swrl.test;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.swrl.SWRLRuleEngine;
import edu.stanford.smi.protegex.owl.swrl.bridge.SWRLRuleEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLRuleEngineException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.IndividualValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngine;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

public class SQWRLTest
{

  public static void main(String args[]) 
  {
    String owlFileName = "";

    if (args.length == 1) {
      owlFileName = args[0];
    } else Usage();
    
    try {
      JenaOWLModel owlModel = SWRLOWLUtil.createJenaOWLModel(owlFileName);
      SWRLRuleEngine ruleEngine = SWRLRuleEngineFactory.create(owlModel);
      SQWRLQueryEngine queryEngine = SQWRLQueryEngineFactory.create(owlModel);
      SWRLFactory factory = new SWRLFactory(owlModel);
      SQWRLResult result;
            
      result = queryEngine.runSQWRLQuery("Query1");

      while (result.hasNext()) {
        IndividualValue x = result.getObjectValue("?x");
        System.err.println("value: ?x=" + x);
        result.next();
      } // while

      ruleEngine.infer();

    } catch (SWRLRuleEngineException e) {
      System.err.println("SWRL rule engine exception: " + e.getMessage());
      e.printStackTrace();
    } catch (SWRLOWLUtilException e) {
      System.err.println("Exception: " + e.getMessage());
      e.printStackTrace();
    } // try
  }

  /*
   *     } catch (SWRLParseException e) {
      System.err.println("Parse exception: " + e.getMessage());
      e.printStackTrace();
    } catch (SWRLFactoryException e) {
      System.err.println("Factory exception: " + e.getMessage());
      e.printStackTrace();

   */
  private static void Usage()
  {
    System.err.println("Usage: SQWRLTest <owlFileName>");
    System.exit(1);
  }
}
