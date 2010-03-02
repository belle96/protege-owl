
package edu.stanford.smi.protegex.owl.swrl.ui.code;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;
import edu.stanford.smi.protegex.owl.ui.code.OWLResourceNameMatcher;

public class SWRLResourceNameMatcher extends OWLResourceNameMatcher
{
  public String getInsertString(RDFResource resource) 
  {
  	if (resource instanceof SWRLVariable) return "?" + resource.getPrefixedName();
  	else return super.getInsertString(resource);
  } 

  public Set<RDFResource> getMatchingResources(String prefix, String leftString, OWLModel owlModel) 
  {
  	if (leftString.endsWith("?")) {
  		Set<RDFResource> resources = new HashSet<RDFResource>();
  		for (Iterator it = owlModel.getOWLNamedClass(SWRLNames.Cls.VARIABLE).getInstances(true).iterator(); it.hasNext();) {
  			SWRLVariable var = (SWRLVariable)it.next();
  			if (var.getName().startsWith(prefix)) resources.add(var);
  		} // for
  		return resources;
  	} else return super.getMatchingResources(prefix, leftString, owlModel);
  } 
  
  @Override
  protected boolean couldBeClass(OWLModel owlModel, String prefix) { return true; }
  
  @Override
  protected boolean couldBeProperty(OWLModel owlModel, String prefix) { return true; }
  
  @Override
  protected boolean couldBeIndividual(OWLModel owlModel, String prefix) { return true; }
    
  @Override
  protected boolean couldBeDatatype(OWLModel owlModel, String prefix) { return true; }
} 
