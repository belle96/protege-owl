
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

/**
 ** This inferface defines the methods that must be provided by a SQWRL query engine.
 */
public interface SQWRLQueryEngine
{
  /**
   ** Run a named SQWRL query.
   */
  //  SQWRLResult runSQWRLQuery(String queryName) throws SQWRLException;

  /**
   ** Run a SQWRL query.
   */
  //SQWRLResult runSQWRLQuery(String queryName, String query) throws SQWRLException, SWRLParseException;

  /**
   ** Run all the SWRL rules and SQWRL queries in an ontology.
   */
  void runSQWRLQueries() throws SQWRLException;

  /**
   ** Get the results from a SQWRL query. Null is returned if there is no result.
   */
  SQWRLResult getSQWRLResult(String queryName) throws SQWRLException;
} // SQWRLQueryEngine
