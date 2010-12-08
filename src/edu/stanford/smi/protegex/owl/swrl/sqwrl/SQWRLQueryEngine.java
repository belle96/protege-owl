
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

import java.util.Set;

import edu.stanford.smi.protegex.owl.swrl.owlapi.SWRLRule;
import edu.stanford.smi.protegex.owl.swrl.parser.SWRLParseException;
import edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions.SQWRLException;

/**
 * This interface defines the methods that must be provided by a SQWRL query engine.
 */
public interface SQWRLQueryEngine
{
  /**
   * Run a named SQWRL query. SWRL rules will also be executed and any inferences produced by them will be available in the query.
   */
  SQWRLResult runSQWRLQuery(String queryName) throws SQWRLException;

  /**
   * Run a named SQWRL query without executing any SWRL rules in ontology.
   */
  SQWRLResult runStandaloneSQWRLQuery(String queryName) throws SQWRLException;

  /**
   * Run all SQWRL enabled 	queries.
   */
  void runSQWRLQueries() throws SQWRLException;

  /**
   * Create and run a SQWRL query. Query will be created and added to ontology.
   */
  SQWRLResult runSQWRLQuery(String queryName, String queryText) throws SQWRLException, SWRLParseException;
  
  /**
   * Create a SQWRL query.
   */
  void createSQWRLQuery(String queryName, String queryText) throws SQWRLException, SWRLParseException;

  /**
   * Delete a SQWRL query.
   */
  void deleteSQWRLQuery(String queryName) throws SQWRLException;

  /**
   * Get the results from a previously executed SQWRL query. Null is returned if there is no result.
   */
  SQWRLResult getSQWRLResult(String queryName) throws SQWRLException;

  /**
   * Get all the enabled SQWRL queries in the ontology.
   */
  Set<SWRLRule> getSQWRLQueries() throws SQWRLException;
  
  /**
   * Get the names of the enabled SQWRL queries in the ontology.
   */
  Set<String> getSQWRLQueryNames() throws SQWRLException;
  
  /**
   * Returns the name of the underlying targer rule engine.
   */
  String getTargetRuleEngineName();
  
  // TODO: temporary
  String uri2PrefixedName(String uri);
  String name2URI(String prefixedName);
}
