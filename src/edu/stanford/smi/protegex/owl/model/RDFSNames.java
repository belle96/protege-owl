package edu.stanford.smi.protegex.owl.model;

import edu.stanford.smi.protege.model.FrameID;


/**
 * Defines the names of the RDFS related parts of the OWL system ontology.
 * This corresponds to the Model interface in general Protege.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RDFSNames {
	
	//added by TT for use of the fully qualified name for the owl metamodel
	
	static String RDFS_NAMESPACE= "http://www.w3.org/2000/01/rdf-schema#";

    public static interface Cls {

        final static String CONTAINER = RDFS_NAMESPACE + "Container";

        final static String DATATYPE = RDFS_NAMESPACE + "Datatype";

        final static String LITERAL = RDFS_NAMESPACE + "Literal";

        final static String NAMED_CLASS = RDFS_NAMESPACE + "Class";
    }


    public interface ClsID {

        FrameID NAMED_CLASS = new FrameID(Cls.NAMED_CLASS);
    }

    public static interface Slot {

        final static String COMMENT = RDFS_NAMESPACE + "comment";

        final static String DOMAIN = RDFS_NAMESPACE + "domain";

        final static String IS_DEFINED_BY = RDFS_NAMESPACE + "isDefinedBy";

        final static String LABEL = RDFS_NAMESPACE + "label";

        /**
         * @deprecated use LABEL
         */
        final static String LABELS = RDFS_NAMESPACE + "label";

        final static String MEMBER = RDFS_NAMESPACE + "member";

        final static String RANGE = RDFS_NAMESPACE + "range";

        final static String SEE_ALSO = RDFS_NAMESPACE + "seeAlso";

        final static String SUB_CLASS_OF = RDFS_NAMESPACE + "subClassOf";

        final static String SUB_PROPERTY_OF = RDFS_NAMESPACE + "subPropertyOf";
    }

    final static String RDFS_PREFIX = "rdfs";
}
