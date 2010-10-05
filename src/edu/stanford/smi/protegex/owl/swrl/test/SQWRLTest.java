
package edu.stanford.smi.protegex.owl.swrl.test;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.IndividualValue;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngine;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLQueryEngineFactory;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.SQWRLResult;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;
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
      SQWRLQueryEngine queryEngine = SQWRLQueryEngineFactory.create(owlModel);
      SWRLFactory factory = new SWRLFactory(owlModel);
      SQWRLResult result;
      
      SWRLImp imp1 = factory.createImp("Person(?p) ^ hasAge(?p, 12) ^ sameAs(?p, ?p) ^ differentFrom(?p, ?p) -> sqwrl:select(?p, \"afd\")");
      SWRLImp imp2 = factory.createImp("Person(?p) ^ hasAge(?p, 12) ^ sameAs(?p, ?p) ^ differentFrom(?p, ?p) -> sqwrl:select(?p, \"afdd\")");
      
      if (factory.areImpsEqual(imp1, imp2)) 
      	System.err.println("yup");
      else 
      	System.err.println("nope");
      
      /*
      queryEngine.createSQWRLQuery("T1", "Adult(?a) . sqwrl:makeSet(?s, ?a) . sqwrl:element(?e, ?s) -> sqwrl:select(?e)");
      
      SWRLImp imp = factory.getImp("T1");
      imp.addRuleGroup("G1");
      factory.disableAll();
      factory.enableAll("G1");
      
      */
      
      /*
      result = queryEngine.runSQWRLQuery("Smarthome-device");

      while (result.hasNext()) {
        IndividualValue e = result.getObjectValue("?service");
        System.err.println("value: e=" + e);
        result.next();
      } // while
      */
      
      //factory.deleteImps();
      
      //SWRLOWLUtil.writeJenaOWLModel2File(owlModel, owlFileName);

      /*
      result = queryEngine.runSQWRLQuery("PersonAverageDrugDosesAndAverageAllDrugDoses");

      while (result.hasNext()) {
      	IndividualValue p = result.getObjectValue("?p");
      	DataValue avgP = result.getDataValue("?avgP");
      	DataValue avgD = result.getDataValue("?avgD");
        System.err.println("value: p=" + p + ", avgP=" + avgP + ", avgD=" + avgD);
        result.next();
      } // while
      */

    } catch (SWRLParseException e) {
      System.err.println("SWRL exception: " + e.getMessage());
      e.printStackTrace();
    } catch (SQWRLException e) {
      System.err.println("SQWRL exception: " + e.getMessage());
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
