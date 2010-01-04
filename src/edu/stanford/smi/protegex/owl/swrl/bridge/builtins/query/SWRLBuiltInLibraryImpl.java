
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.query;

import edu.stanford.smi.protegex.owl.swrl.bridge.BuiltInArgument;
import edu.stanford.smi.protegex.owl.swrl.bridge.exceptions.BuiltInException;
import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.AbstractSWRLBuiltInLibrary;

import java.util.*;

/**
 ** This built-in library has been superceded by the SQWRL built-in library. See See <a
 ** href="http://protege.cim3.net/cgi-bin/wiki.pl?SQWRL">here</a> for documentation on this new library.
 */
public class SWRLBuiltInLibraryImpl extends AbstractSWRLBuiltInLibrary 
{
  private static String QueryLibraryName = "SWRLQueryBuiltIns";

  public SWRLBuiltInLibraryImpl() { super(QueryLibraryName); }

  public void reset() {}

  public boolean select(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean selectDistinct(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean count(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean min(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean max(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean sum(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean avg(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean columnNames(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean orderBy(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  public boolean orderByDescending(List<BuiltInArgument> arguments) throws BuiltInException { return throwSupercededException(); }
  
  private boolean throwSupercededException() throws BuiltInException
  {
    throw new BuiltInException("\nThe query library has been superseded by the SQWRL library; to upgrade, import the SQWRL ontology\n" +
                               "(http://sqwrl.stanford.edu/ontologies/built-ins/3.4/sqwrl.owl) from the Protege-OWL repository, give it\n" +
                               "the prefix 'sqwrl', and replace 'query' with this prefix for all existing query built-ins"); 
  } // throwInternalQueryException

} // SWRLBuiltInLibraryImpl
