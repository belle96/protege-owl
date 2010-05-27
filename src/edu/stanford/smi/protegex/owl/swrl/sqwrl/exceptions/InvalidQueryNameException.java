
package edu.stanford.smi.protegex.owl.swrl.sqwrl.exceptions;

public class InvalidQueryNameException extends SQWRLException
{
  public InvalidQueryNameException(String queryName) { super("invalid query name " + queryName); } 
}
