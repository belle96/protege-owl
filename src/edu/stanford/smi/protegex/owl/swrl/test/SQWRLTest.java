
package edu.stanford.smi.protegex.owl.swrl.test;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.exceptions.SWRLOWLUtilException;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.DataValue;
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
      OWLModel owlModel = SWRLOWLUtil.createJenaOWLModel(owlFileName);
      SQWRLQueryEngine queryEngine = SQWRLQueryEngineFactory.create(owlModel);
      SQWRLResult result;
      
      result = queryEngine.runSQWRLQuery("T1", "Adult(?a) . sqwrl:makeSet(?s, ?a) . sqwrl:contains(?s, ?e) -> sqwrl:select(?e)");

      while (result.hasNext()) {
        IndividualValue e = result.getObjectValue("?e");
        System.err.println("value: e=" + e);
        result.next();
      } // while

      result = queryEngine.runSQWRLQuery("PersonAverageDrugDosesAndAverageAllDrugDoses");

      while (result.hasNext()) {
      	IndividualValue p = result.getObjectValue("?p");
      	DataValue avgP = result.getDataValue("?avgP");
      	DataValue avgD = result.getDataValue("?avgD");
        System.err.println("value: p=" + p + ", avgP=" + avgP + ", avgD=" + avgD);
        result.next();
      } // while

    } catch (SQWRLException e) {
      System.err.println("SQWRL exception: " + e.getMessage());
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
