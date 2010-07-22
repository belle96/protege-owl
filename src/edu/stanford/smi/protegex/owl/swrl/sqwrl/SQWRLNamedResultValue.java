
package edu.stanford.smi.protegex.owl.swrl.sqwrl;

/**
 * Interface that defines a named value (i.e., OWL class, property, or individual) returned from a SQWRL query.
 * 
 *  See <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SQWRLQueryAPI">here</a> for documentation.
 */
public interface SQWRLNamedResultValue extends SQWRLResultValue
{
	String getURI();
}