package edu.stanford.smi.protegex.owl.ui.properties.domain.tests;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.properties.domain.OWLDomainTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLDomainTableModelTestCase extends AbstractJenaTestCase {


    public void testAddDomainCls1() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLDomainTableModel tableModel = new OWLDomainTableModel(property);
        assertEquals(1, tableModel.getRowCount());
        assertSame(tableModel.getValueAt(0, 0), owlModel.getOWLThingClass());
        
        assertNull(property.getDomain(false));

        property.setDomain(aCls);
        assertEquals(1, tableModel.getRowCount());

        property.addUnionDomainClass(bCls);
        assertEquals(2, tableModel.getRowCount());

        assertEquals(aCls, tableModel.getValueAt(0, 0));
        assertEquals(bCls, tableModel.getValueAt(1, 0));
    }
    
    public void testAddDomainCls2() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");
        OWLDomainTableModel tableModel = new OWLDomainTableModel(property);
        assertEquals(1, tableModel.getRowCount());
        assertSame(tableModel.getValueAt(0, 0), owlModel.getOWLThingClass());

        property.setDomainDefined(true);
        RDFSClass domain = property.getDomain(false);
        assertNotNull(domain);
        assertSame(owlModel.getOWLThingClass(), domain);

        property.setDomain(aCls);
        assertEquals(1, tableModel.getRowCount());

        property.addUnionDomainClass(bCls);
        assertEquals(2, tableModel.getRowCount());

        assertEquals(aCls, tableModel.getValueAt(0, 0));
        assertEquals(bCls, tableModel.getValueAt(1, 0));
    }


    public void testRemoveDomainCls() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");

        property.setDomain(aCls);
        property.addUnionDomainClass(bCls);
        OWLDomainTableModel tableModel = new OWLDomainTableModel(property);
        assertEquals(2, tableModel.getRowCount());
        assertEquals(aCls, tableModel.getValueAt(0, 0));
        assertEquals(bCls, tableModel.getValueAt(1, 0));

        property.removeUnionDomainClass(aCls);
        assertEquals(1, tableModel.getRowCount());
        assertEquals(bCls, tableModel.getValueAt(0, 0));

        property.removeUnionDomainClass(bCls);
        assertEquals(1, tableModel.getRowCount());
        assertSame(owlModel.getOWLThingClass(), tableModel.getValueAt(0, 0));
    }

    public void testInheritanceOfDomain() {
        OWLProperty property = owlModel.createOWLObjectProperty("property");
        OWLProperty subproperty = owlModel.createOWLObjectProperty("subproperty");
        OWLNamedClass aCls = owlModel.createOWLNamedClass("A");
        OWLNamedClass bCls = owlModel.createOWLNamedClass("B");

        subproperty.addSuperproperty(property);
        bCls.addSuperclass(aCls);

        property.setDomain(aCls);

        OWLDomainTableModel tableModel = new OWLDomainTableModel(subproperty);
        assertEquals(1, tableModel.getRowCount());
        assertSame(aCls, tableModel.getValueAt(0, 0));

        subproperty.setDomain(bCls);
        assertEquals(1, tableModel.getRowCount());
        assertSame(bCls, tableModel.getValueAt(0, 0));

        subproperty.removeUnionDomainClass(bCls);
        assertEquals(1, tableModel.getRowCount());
        assertSame(aCls, tableModel.getValueAt(0, 0));
    }
}
