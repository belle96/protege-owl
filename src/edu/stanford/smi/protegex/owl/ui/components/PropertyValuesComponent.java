package edu.stanford.smi.protegex.owl.ui.components;

import java.util.Collection;

import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface PropertyValuesComponent extends Disposable {

    RDFResource getSubject();


    RDFProperty getPredicate();


    Collection getObjects();


    void setSubject(RDFResource subject);


    void valuesChanged();
}
