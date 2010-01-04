package edu.stanford.smi.protegex.owl.model.impl.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

public class UnionDomainTestCase extends TestCase {
    
    @SuppressWarnings("unchecked")
    public void testUnionDomain() {
        List errors = new ArrayList();
        Project p = new Project("junit/projects/domain.pprj", errors);
        assertTrue(errors.isEmpty());
        OWLModel owlModel = (OWLModel) p.getKnowledgeBase();
        
        OWLNamedClass a = owlModel.getOWLNamedClass("A");
        OWLNamedClass b = owlModel.getOWLNamedClass("B");
        OWLNamedClass c = owlModel.getOWLNamedClass("C");
        OWLNamedClass d = owlModel.getOWLNamedClass("D");

        Collection vacuous = Collections.singleton(owlModel.getOWLThingClass());
        Collection ab = new HashSet();
        ab.add(a);
        ab.add(b);
        
        // baseline
        assertTrue(new HashSet(owlModel.getRDFProperty("p").getUnionDomain(true))
                        .equals(ab));
        assertTrue(new HashSet(owlModel.getRDFProperty("p").getUnionDomain(false))
                        .equals(ab));

        // test sub property has smaller domain than parent
        // falls through if statements inside loop
        assertTrue(new HashSet(owlModel.getRDFProperty("q").getUnionDomain(true))
                         .equals(Collections.singleton(a)));
        assertTrue(new HashSet(owlModel.getRDFProperty("q").getUnionDomain(false))
                         .equals(Collections.singleton(a)));

        // test vacuous converted to some domain
        // second if inside loop
        assertTrue(new HashSet(owlModel.getRDFProperty("r").getUnionDomain(true))
                        .equals(Collections.singleton(a)));
        assertTrue(new HashSet(owlModel.getRDFProperty("r").getUnionDomain(false))
                        .equals(vacuous));
        
        // test incompatible domains for union
        // third if inside loop - there are other acceptable behaviors.
        assertTrue(new HashSet(owlModel.getRDFProperty("s").getUnionDomain(true))
                        .equals(Collections.singleton(c)));
        assertTrue(new HashSet(owlModel.getRDFProperty("s").getUnionDomain(false))
                        .equals(Collections.singleton(c)));
        
        // test incompatible domains for union
        // third if inside loop - there are other acceptable behaviors.
        assertTrue(new HashSet(owlModel.getRDFProperty("t").getUnionDomain(true))
                        .equals(Collections.singleton(d)));
        assertTrue(new HashSet(owlModel.getRDFProperty("t").getUnionDomain(false))
                        .equals(Collections.singleton(d)));
        
        // test null converted to vacuous - might not really test the if outside the loop
        assertTrue(new HashSet(owlModel.getRDFProperty("u").getUnionDomain(true))
                        .equals(vacuous));
        assertTrue(new HashSet(owlModel.getRDFProperty("u").getUnionDomain(false))
                        .equals(vacuous));
        
        // test non-vacuous domain beats vacuous domain - first if inside loop
        assertTrue(new HashSet(owlModel.getRDFProperty("v").getUnionDomain(true))
                        .equals(Collections.singleton(a)));
        assertTrue(new HashSet(owlModel.getRDFProperty("v").getUnionDomain(false))
                        .equals(Collections.singleton(a)));
        
        // test non-vacuous domain beats vacuous domain - first if inside loop (variation
        assertTrue(new HashSet(owlModel.getRDFProperty("w").getUnionDomain(true))
                        .equals(Collections.singleton(a)));
        assertTrue(new HashSet(owlModel.getRDFProperty("w").getUnionDomain(false))
                        .equals(vacuous));
        
        Collection abc = new HashSet();
        abc.add(a);
        abc.add(b);
        abc.add(c);
        
        assertTrue(new HashSet(owlModel.getRDFProperty("x").getUnionDomain(true))
                        .equals(ab));
        assertTrue(new HashSet(owlModel.getRDFProperty("x").getUnionDomain(false))
                        .equals(abc));
    }

}
