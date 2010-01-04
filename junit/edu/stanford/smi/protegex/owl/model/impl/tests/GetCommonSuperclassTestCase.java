package edu.stanford.smi.protegex.owl.model.impl.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class GetCommonSuperclassTestCase extends AbstractJenaTestCase {

    public void testSimple() {
        RDFSNamedClass top = owlModel.createOWLNamedClass("Top");
        RDFSNamedClass a = owlModel.createSubclass("A", top);
        RDFSNamedClass b = owlModel.createSubclass("B", top);
        Collection<RDFSNamedClass> classes = new ArrayList<RDFSNamedClass>();
        classes.add(a);
        classes.add(b);
        assertEquals(1, owlModel.getCommonSuperclasses(classes).size());
        assertEquals(top, owlModel.getCommonSuperclass(classes));
    }


    public void testThing() {
        RDFSNamedClass a = owlModel.createSubclass("A", owlThing);
        RDFSNamedClass a1 = owlModel.createSubclass("Aa", a);
        RDFSNamedClass b = owlModel.createSubclass("B", owlThing);
        RDFSNamedClass b1 = owlModel.createSubclass("B1", b);
        Collection<RDFSNamedClass> classes = new ArrayList<RDFSNamedClass>();
        classes.add(a1);
        classes.add(b1);
        assertEquals(1, owlModel.getCommonSuperclasses(classes).size());
        assertEquals(owlThing, owlModel.getCommonSuperclass(classes));
    }
    
    public void testTwoSupers() {
        RDFSNamedClass a = owlModel.createSubclass("A", owlThing);
        RDFSNamedClass b = owlModel.createSubclass("B", owlThing);
        
        Set<RDFSNamedClass> supers = new HashSet<RDFSNamedClass>();
        supers.add(a);
        supers.add(b);
        
        Set<RDFSNamedClass> subs = new HashSet<RDFSNamedClass>();
        RDFSNamedClass c = owlModel.createSubclass("C", supers);
        subs.add(c);

        assertEquals(supers, owlModel.getCommonSuperclasses(subs));
        
        RDFSNamedClass d = owlModel.createSubclass("D", a);
        subs.add(d);

        assertEquals(1, owlModel.getCommonSuperclasses(subs).size());
        assertEquals(a, owlModel.getCommonSuperclass(subs));
    }
    
    public void testChain() {
        RDFSNamedClass a = owlModel.createSubclass("A", owlThing);
        RDFSNamedClass b = owlModel.createSubclass("B", a);
        a.addSuperclass(b);
        RDFSNamedClass c = owlModel.createSubclass("C", a);
        
        Collection<RDFSNamedClass> subs = new HashSet<RDFSNamedClass>();
        subs.add(c);
        Set<RDFSNamedClass> supers = owlModel.getCommonSuperclasses(subs);
        
        assertTrue(supers.size() > 0);
        assertTrue(supers.contains(a) || supers.contains(b));
    }
    
}
