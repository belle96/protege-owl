
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

/**
 * This interface defines the methods that must be provided by a SQWRL query engine.
 */
public interface SQWRLQueryEngine
{
  /**
   * Run a named SQWRL query.
   */
  SQWRLResult runSQWRLQuery(String queryName) throws SQWRLException;

  /**
   * Run all SQWRL queries.
   */
  void runSQWRLQueries() throws SQWRLException;

  /**
   ** Run a SQWRL query.
   */
  //SQWRLResult runSQWRLQuery(String queryName, String query) throws SQWRLException, SWRLParseException;
  
  /**
   * Get the results from a SQWRL query. Null is returned if there is no result.
   */
  SQWRLResult getSQWRLResult(String queryName) throws SQWRLException;
} // SQWRLQueryEngine
