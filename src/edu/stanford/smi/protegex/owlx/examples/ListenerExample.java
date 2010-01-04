package edu.stanford.smi.protegex.owlx.examples;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ClassAdapter;
import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ListenerExample {

    public static void main(String[] args) throws OntologyLoadException  {

        OWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass cls = owlModel.createOWLNamedClass("Class");
        cls.addClassListener(new ClassAdapter() {
            public void instanceAdded(RDFSClass cls, RDFResource instance) {
                System.out.println("Instance was added to class: " + instance.getName());
            }
        });

        for (int i = 0; i < 5; i++) {
            String newName = "Individual" + (int) (Math.random() * 10000);
            cls.createOWLIndividual(newName);
        }


        owlModel.addModelListener(new ModelAdapter() {
            public void propertyCreated(RDFProperty property) {
                System.out.println("Property created: " + property.getName());
            }
        });

        owlModel.createRDFProperty("RDF-Property");
        owlModel.createOWLObjectProperty("Object-Property");
        owlModel.createOWLDatatypeProperty("Datatype-Property");
    }
}
