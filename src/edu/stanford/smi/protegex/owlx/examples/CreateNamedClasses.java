package edu.stanford.smi.protegex.owlx.examples;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;
import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateNamedClasses {

    public static void main(String[] args) throws OntologyLoadException {

        OWLModel owlModel = ProtegeOWL.createJenaOWLModel();

        OWLNamedClass personClass = owlModel.createOWLNamedClass("Person");

        // Create subclass (complicating version)
        OWLNamedClass brotherClass = owlModel.createOWLNamedClass("Brother");
        brotherClass.addSuperclass(personClass);
        brotherClass.removeSuperclass(owlModel.getOWLThingClass());

        // Create subclass (direct version)
        OWLNamedClass sisterClass = owlModel.createOWLNamedSubclass("Sister", personClass);

        printClassTree(personClass, "");

        OWLIndividual hans = brotherClass.createOWLIndividual("Hans");
        Collection brothers = brotherClass.getInstances(false);
        Assert.assertTrue(brothers.contains(hans));
        Assert.assertTrue(brothers.size() == 1);

        Assert.assertEquals(personClass.getInstanceCount(false), 0);
        Assert.assertEquals(personClass.getInstanceCount(true), 0);
        Assert.assertTrue(personClass.getInstances(true).contains(hans));

        Assert.assertTrue(hans.getProtegeType().equals(brotherClass));
        Assert.assertTrue(hans.hasProtegeType(brotherClass));
        Assert.assertFalse(hans.hasProtegeType(personClass, false));
        Assert.assertTrue(hans.hasProtegeType(personClass, true));
    }


    private static void printClassTree(RDFSClass cls, String indentation) {
        System.out.println(indentation + cls.getName());
        for (Iterator it = cls.getSubclasses(false).iterator(); it.hasNext();) {
            RDFSClass subclass = (RDFSClass) it.next();
            printClassTree(subclass, indentation + "    ");
        }
    }
}
