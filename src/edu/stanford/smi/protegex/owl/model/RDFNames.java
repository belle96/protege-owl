package edu.stanford.smi.protegex.owl.model;

import edu.stanford.smi.protege.model.FrameID;


/**
 * Defines the names of the RDF(S) related parts of the OWL system ontology.
 * This corresponds to the Model interface in general Protege.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFNames {
	
	//added by TT for use of the fully qualified name for the owl metamodel
	
	static String RDF_NAMESPACE= "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static interface Cls {

        final static String ALT = (RDF_NAMESPACE + "Alt").intern();

        final static String BAG = (RDF_NAMESPACE + "Bag").intern();

        final static String DESCRIPTION = (RDF_NAMESPACE + "Description").intern();
        
        final static String LIST = (RDF_NAMESPACE + "List").intern();

        final static String PROPERTY = (RDF_NAMESPACE + "Property").intern();

        final static String SEQ = (RDF_NAMESPACE + "Seq").intern();

        final static String STATEMENT = (RDF_NAMESPACE + "Statement").intern();
        
        final static String EXTERNAL_RESOURCE = (ProtegeNames.PROTEGE_OWL_NAMESPACE +  "ExternalResource").intern();

        final static String EXTERNAL_CLASS = (ProtegeNames.PROTEGE_OWL_NAMESPACE +  "ExternalClass").intern();
        
        final static String EXTERNAL_PROPERTY = (ProtegeNames.PROTEGE_OWL_NAMESPACE +  "ExternalProperty").intern();

    }


    public interface ClsID {

        FrameID PROPERTY = new FrameID(Cls.PROPERTY);
    }

    public static interface Slot {

        /* *********************************************
         * These guys aren't really slots or even resources?  I think that
         * that are actually just part of the rdf syntax.
         */
        final static String ABOUT = "rdf:about";
        
        final static String ID = "rdf:ID";
        
        final static String RESOURCE = "rdf:resource";
        
        final static String PARSE_TYPE = "rdf:parseType";

        final static String DATATYPE = "rdf:datatype";
        /*
         * *********************************************
         */
        

        final static String FIRST = (RDF_NAMESPACE + "first").intern();

        final static String OBJECT = (RDF_NAMESPACE + "object").intern();

        final static String PREDICATE = (RDF_NAMESPACE + "predicate").intern();

        final static String REST = (RDF_NAMESPACE + "rest").intern();

        final static String SUBJECT = (RDF_NAMESPACE + "subject").intern();

        final static String TYPE = (RDF_NAMESPACE + "type").intern();

        final static String VALUE = (RDF_NAMESPACE + "value").intern();
    }


    public static interface Instance {

        final static String NIL = (RDF_NAMESPACE + "nil").intern();
    }

    final static String COLLECTION = "Collection";

    final static String RDF_PREFIX = "rdf";

    final static String XSD_PREFIX = "xsd";

    final static String XML_LITERAL = (RDF_NAMESPACE + "XMLLiteral").intern();
    
    /*
     * I don't think that this is  even a resources - it seems to just
     * be part  of the rdf syntax.
     */
    final static String RDF = "rdf:RDF";
}
