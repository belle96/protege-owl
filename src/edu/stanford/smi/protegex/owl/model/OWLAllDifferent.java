package edu.stanford.smi.protegex.owl.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An RDFResource that represents a block of owl:AllDifferent individuals.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLAllDifferent extends OWLIndividual {

    void addDistinctMember(RDFResource resource);


    Collection getDistinctMembers();


    Iterator listDistinctMembers();


    void removeDistinctMember(RDFResource resource);


    void setDistinctMembers(List values);
}
