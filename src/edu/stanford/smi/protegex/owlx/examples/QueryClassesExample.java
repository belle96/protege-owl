package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.*;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class QueryClassesExample {

    public static void main(String[] args) throws Exception {

        String uri = "http://www.owl-ontologies.com/travel.owl";
        OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);

        // Print all classes and their instances
        Collection classes = owlModel.getUserDefinedOWLNamedClasses();
        for (Iterator it = classes.iterator(); it.hasNext();) {
            OWLNamedClass cls = (OWLNamedClass) it.next();
            Collection instances = cls.getInstances(false);
            System.out.println("Class " + cls.getBrowserText() + " (" + instances.size() + ")");
            for (Iterator jt = instances.iterator(); jt.hasNext();) {
                OWLIndividual individual = (OWLIndividual) jt.next();
                System.out.println(" - " + individual.getBrowserText());
            }
        }

        // Print all resources that have owl:Thing as their rdfs:subClassOf value
        RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_CLASS_OF);
        OWLNamedClass owlThingClass = owlModel.getOWLThingClass();
        Collection results = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, owlThingClass);
        System.out.println("Subclasses of owl:Thing:");
        for (Iterator it = results.iterator(); it.hasNext();) {
            RDFResource resource = (RDFResource) it.next();
            System.out.println(" - " + resource.getBrowserText());
        }
    }
}
