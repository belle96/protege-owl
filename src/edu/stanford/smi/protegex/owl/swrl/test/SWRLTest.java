
package edu.stanford.smi.protegex.owl.swrl.test;

import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

public class SWRLTest
{
   public static void main(String args[]) 
  {
	  JenaOWLModel modelA = null, modelB = null;
    SWRLFactory factoryA = null, factoryB = null;
    String fileName1 = "", fileName2 = "", fileName3 = "", fileName4 = "";

    if (args.length == 4) {
      fileName1 = args[0];
      fileName2 = args[1];
      fileName3 = args[2];
      fileName4 = args[3];
    } else Usage();
   
//    String uriA = "file://dionisbg.di.funpic.de/familyA.owl";
    try {
      modelA=SWRLOWLUtil.createJenaOWLModel(fileName1);
      System.out.println("ModelA loaded ...");
    } catch (java.lang.Exception e) {
      e.printStackTrace();
    }
    factoryA = new SWRLFactory(modelA);
    System.out.println("SWRL FactoryA created ...");
    
    // -- OntologyB --
    try {
      modelB=SWRLOWLUtil.createJenaOWLModel(fileName2);
      System.out.println("ModelB loaded ...");
    }
    catch (java.lang.Exception e) {
      e.printStackTrace();
    }
    factoryB = new SWRLFactory(modelB);
    System.out.println("SWRL FactoryB created ...");
        
    // -- Copying rules between two models --
    try {
      System.out.print("Copying " + factoryA.getImps().size() + " rules to target ontology...");
         for (Object o : factoryA.getImps()) {
    	  if (o instanceof SWRLImp) {
    		  SWRLImp imp = (SWRLImp)o;
    		  System.err.println(imp.getBrowserText());
    		  factoryB.createImp(imp.getBrowserText());
    	  }
      }
      factoryA.deleteImps();
      
      System.out.print("Saving stripped ontology to " + fileName3 + "...");
      SWRLOWLUtil.writeJenaOWLModel2File(modelA, fileName3);
      System.out.println("...written.");
      
      System.out.print("Saving target ontology with rules to " + fileName4 + "...");
      SWRLOWLUtil.writeJenaOWLModel2File(modelB, fileName4);
      System.out.println("...written.");
      
    } catch (Throwable e) {
      e.printStackTrace();
    }
  } // main

  private static void Usage()
  {
    System.err.println("Usage: SWRLTest <fileName(with rules)> <fileName>(base)<fileName (stripped of rules)> <fileName>(only rules)");
    System.exit(1);
  } // Usage

} // SWRLTest
