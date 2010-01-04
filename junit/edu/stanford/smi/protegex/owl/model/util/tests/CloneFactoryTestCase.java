package edu.stanford.smi.protegex.owl.model.util.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLAllValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLComplementClass;
import edu.stanford.smi.protegex.owl.model.OWLEnumeratedClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import edu.stanford.smi.protegex.owl.model.RDFList;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.util.CloneFactory;
import edu.stanford.smi.protegex.owl.model.util.ResourceCopier;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         11-Jan-2006
 */
public class CloneFactoryTestCase extends AbstractJenaTestCase {

    public void testCloneOWLNamedClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        a.addSuperclass(b);

        OWLNamedClass clone = CloneFactory.cloneOWLNamedClass(a);
        assertNotNull(clone);
        assertTrue(clone.getSuperclasses(false).contains(b));
    }

    public void testCloneOWLNamedClassWithRestrictionWithNamedClassFiller() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLObjectProperty p = owlModel.createOWLObjectProperty("p");
        OWLSomeValuesFrom r = owlModel.createOWLSomeValuesFrom(p, b);
        a.addSuperclass(r);

        OWLNamedClass clone = CloneFactory.cloneOWLNamedClass(a);
        assertNotNull(clone);

        OWLSomeValuesFrom rclone = null;
        Iterator clonesupers = clone.getSuperclasses(false).iterator();
        while (clonesupers.hasNext() && rclone == null) {
            RDFSClass s = (RDFSClass) clonesupers.next();
            if (s instanceof OWLSomeValuesFrom) {
                rclone = (OWLSomeValuesFrom) s;
            }
        }

        assertNotNull(rclone);
        assertTrue(rclone != r);
        assertSame(rclone.getFiller(), b);
        assertSame(rclone.getOnProperty(), p);
    }

    public void testCloneOWLNamedClassWithRestrictionWithUnionClassFiller() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLNamedClass c = owlModel.createOWLNamedClass("C");

        OWLUnionClass u = owlModel.createOWLUnionClass();
        u.addOperand(b);
        u.addOperand(c);

        OWLObjectProperty p = owlModel.createOWLObjectProperty("p");
        OWLAllValuesFrom r = owlModel.createOWLAllValuesFrom(p, u);
        a.addSuperclass(r);

        OWLNamedClass clone = CloneFactory.cloneOWLNamedClass(a);
        assertNotNull(clone);

        OWLAllValuesFrom rclone = null;
        Iterator clonesupers = clone.getSuperclasses(false).iterator();
        while (clonesupers.hasNext() && rclone == null) {
            RDFSClass s = (RDFSClass) clonesupers.next();
            if (s instanceof OWLAllValuesFrom) {
                rclone = (OWLAllValuesFrom) s;
            }
        }

        assertNotNull(rclone);
        assertNotSame(rclone, r);
        assertSame(rclone.getOnProperty(), p);
        assertNotSame(rclone.getFiller(), u);
        assertTrue(((OWLUnionClass) rclone.getFiller()).getOperands().contains(b));
        assertTrue(((OWLUnionClass) rclone.getFiller()).getOperands().contains(c));
    }

    public void testCopyOWLEnumeratedClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        Collection aInds = new ArrayList();
        aInds.add(a.createOWLIndividual("a1"));
        aInds.add(a.createOWLIndividual("a2"));
        aInds.add(a.createOWLIndividual("a3"));

        OWLEnumeratedClass e = owlModel.createOWLEnumeratedClass(aInds);

        ResourceCopier cfactory = new ResourceCopier();
        e.accept(cfactory);
        OWLEnumeratedClass clone = (OWLEnumeratedClass) cfactory.getCopy();
        assertNotNull(clone);

        for (Iterator i = clone.getOneOf().iterator(); i.hasNext();) {
            OWLIndividual ind = (OWLIndividual) i.next();
            assertTrue(aInds.contains(ind));
            aInds.remove(ind);
        }
    }

    public void testCopyComplementClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLComplementClass not = owlModel.createOWLComplementClass(a);

        ResourceCopier copier = new ResourceCopier();
        not.accept(copier);
        OWLComplementClass clone = (OWLComplementClass) copier.getCopy();

        assertNotNull(clone);
        assertSame(clone.getComplement(), a);
    }

    public void testCloneDefinedClass() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLObjectProperty p = owlModel.createOWLObjectProperty("p");
        OWLSomeValuesFrom r = owlModel.createOWLSomeValuesFrom(p, b);
        a.addEquivalentClass(r);

        OWLNamedClass clone = CloneFactory.cloneOWLNamedClass(a);
        assertNotNull(clone);
        assertTrue(clone.isDefinedClass());
    }

    public void testCopyRDFList() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        Collection values = new ArrayList();
        values.add(a);
        values.add(b);
        RDFList list = owlModel.createRDFList(values.iterator());

        ResourceCopier copier = new ResourceCopier();
        list.accept(copier);
        RDFList clone = (RDFList) copier.getCopy();

        assertNotNull(clone);
        assertSame(clone.getFirst(), a);
        assertSame(clone.getRest().getFirst(), b);
    }

    public void testCopyIntersection() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");

        Collection intClasses = new ArrayList();
        intClasses.add(a);
        intClasses.add(b);
        OWLIntersectionClass inter = owlModel.createOWLIntersectionClass(intClasses);

        ResourceCopier copier = new ResourceCopier();
        inter.accept(copier);
        OWLIntersectionClass clone = (OWLIntersectionClass) copier.getCopy();
        assertNotNull(clone);

        Collection operands = clone.getOperands();
        assertTrue(operands.contains(a));
        assertTrue(operands.contains(b));
    }

    public void testCloneDefinedClassUsingIntersection() {
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        OWLNamedClass b = owlModel.createOWLNamedClass("B");
        OWLNamedClass c = owlModel.createOWLNamedClass("C");

        Collection intClasses = new ArrayList();
        intClasses.add(a);
        intClasses.add(b);
        OWLIntersectionClass inter = owlModel.createOWLIntersectionClass(intClasses);

        c.addEquivalentClass(inter);

        OWLNamedClass clone = CloneFactory.cloneOWLNamedClass(c);
        assertNotNull(clone);
        assertTrue(clone.isDefinedClass());
    }

    public void testCloneCheeseyPizza() {
        OWLNamedClass pizza = owlModel.createOWLNamedClass("Pizza");
        OWLNamedClass cheeseyPizza = owlModel.createOWLNamedClass("CheeseyPizza");
        OWLNamedClass cheeseTopping = owlModel.createOWLNamedClass("CheeseTopping");
        OWLObjectProperty hasTopping = owlModel.createOWLObjectProperty("hasTopping");

        OWLSomeValuesFrom restr = owlModel.createOWLSomeValuesFrom(hasTopping, cheeseTopping);

        Collection intClasses = new ArrayList();
        intClasses.add(pizza);
        intClasses.add(restr);
        OWLIntersectionClass inter = owlModel.createOWLIntersectionClass(intClasses);

        cheeseyPizza.addEquivalentClass(inter);

        OWLNamedClass clone = CloneFactory.cloneOWLNamedClass(cheeseyPizza);
        assertNotNull(clone);
        assertTrue(clone.isDefinedClass());
    }

    public void testSavingAfterCloning() {

        testCloneCheeseyPizza();

        try {
            owlModel.save(new File("out.owl").toURI());
        }
        catch (Exception e) {
          Log.getLogger().log(Level.SEVERE, "Exception caught", e);
          fail();
        }
    }
}
