package edu.stanford.smi.protegex.owlx.examples;

import com.hp.hpl.jena.util.FileUtils;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A simple demo that loads the travel ontology.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class LoadTravelOntology {

    public static void main(String[] args) throws Exception {
        String uri = "http://www.owl-ontologies.com/travel.owl";
        JenaOWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);
        OWLNamedClass destinationClass = owlModel.getOWLNamedClass("Destination");
        System.out.println("Destination has " + destinationClass.getInstanceCount(true) + " instances:");
        for (Iterator it = destinationClass.getInstances(true).iterator(); it.hasNext();) {
            OWLIndividual individual = (OWLIndividual) it.next();
            RDFSClass type = individual.getProtegeType();
            System.out.println("- " + individual.getBrowserText() + " (" + type.getBrowserText() + ")");
        }

        // Let's access some other resources
        OWLObjectProperty hasContactProperty = owlModel.getOWLObjectProperty("hasContact");
        OWLDatatypeProperty hasZipCodeProperty = owlModel.getOWLDatatypeProperty("hasZipCode");
        OWLIndividual sydney = owlModel.getOWLIndividual("Sydney");

        String fileName = "travel-saved.owl";
        Collection errors = new ArrayList();
        owlModel.save(new File(fileName).toURI(), FileUtils.langXMLAbbrev, errors);
        System.out.println("File saved with " + errors.size() + " errors.");
    }
}
