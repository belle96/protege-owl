
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlxml;

import java.net.URI;
import java.net.URISyntaxException;

import org.jdom.Document;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.util.SWRLOWLUtil;

public class MapXML2OWL
{
  public static void main(String args[])
  {
    String xmlURI = "", owlFilename = "";
    Document doc;
    JenaOWLModel owlModel;
    edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlxml.XMLMapper  mapper;
    XMLProcessor processor = new XMLProcessor();

    if (args.length == 2) {
      xmlURI = args[0];
      owlFilename = args[1];
    } else Usage();

    try {
      doc = processor.processXMLStream(xmlURI);
      owlModel = SWRLOWLUtil.createJenaOWLModel();
      owlModel.setGenerateEventsEnabled(false);
      addSWRLXMLImport(owlModel);
      mapper = new edu.stanford.smi.protegex.owl.swrl.bridge.builtins.swrlxml.XMLMapper(owlModel);
      mapper.document2XMLDocument(doc);
      SWRLOWLUtil.writeJenaOWLModel2File(owlModel, owlFilename);
    } catch (Exception e) {
      System.err.println("error mapping XML document with URI '" + xmlURI + "': " + e.getMessage());
      e.printStackTrace();
    } // try
  } // main

  private static void Usage()
  {
    System.err.println("Usage: MapXML2OWL <xmlURI> <owlFilename>");
    System.exit(1);
  } // Usage

  private static void addSWRLXMLImport(OWLModel owlModel) 
  {
    ImportHelper importHelper = new ImportHelper(owlModel);

    try {
      owlModel.getNamespaceManager().setPrefix(new URI(SWRLNames.SWRLXML_NAMESPACE), SWRLNames.SWRLXML_PREFIX);
      
      if  (owlModel.getTripleStoreModel().getTripleStore(SWRLNames.SWRLXML_IMPORT) == null) 
        importHelper.addImport(new URI(SWRLNames.SWRLXML_IMPORT));

      importHelper.importOntologies(false);
    } catch (URISyntaxException e) {
      System.err.println("error importing SWRLXML ontology: " + e.getMessage());
      System.exit(-1);
    } catch (OntologyLoadException e) {
      System.err.println("error loading SWRLXML ontology: " + e.getMessage());
      System.exit(-1);
    } // try


  } // addSWRLXMLImport

} // MapXML2OWL
