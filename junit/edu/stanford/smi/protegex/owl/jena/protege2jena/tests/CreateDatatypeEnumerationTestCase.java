package edu.stanford.smi.protegex.owl.jena.protege2jena.tests;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.DataRange;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.XMLSchemaDatatypes;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreateDatatypeEnumerationTestCase extends AbstractProtege2JenaTestCase {

    public void estCreateAllRestrictionWithInt() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property", owlModel.getXSDint());
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(property, new RDFSLiteral[]{
                owlModel.createRDFSLiteral(new Integer(1)),
                owlModel.createRDFSLiteral(new Integer(2))
        }));
        OntModel newModel = createOntModel();
        OntClass ontClass = newModel.getOntClass(cls.getURI());
        OntClass superClass = null;
        for (Iterator it = ontClass.listSuperClasses(); it.hasNext();) {
            superClass = (OntClass) it.next();
            if (superClass.canAs(AllValuesFromRestriction.class)) {
                break;
            }
        }
        AllValuesFromRestriction restriction =
                (AllValuesFromRestriction) superClass.as(AllValuesFromRestriction.class);
        DataRange dataRange = (DataRange) ((Resource) restriction.getAllValuesFrom()).as(DataRange.class);
        assertSize(2, dataRange.listOneOf());
        Iterator it = dataRange.listOneOf();
        Literal firstLiteral = (Literal) ((RDFNode) it.next()).as(Literal.class);
        assertEquals(XMLSchemaDatatypes.getDefaultXSDDatatype(ValueType.INTEGER),
                firstLiteral.getDatatype());
        assertEquals(1, firstLiteral.getInt());
        Literal secondLiteral = (Literal) ((RDFNode) it.next()).as(Literal.class);
        assertEquals(2, secondLiteral.getInt());
    }


    public void testCreateAllRestrictionWithString() {
        OWLNamedClass cls = owlModel.createOWLNamedClass("Cls");
        OWLDatatypeProperty property = owlModel.createOWLDatatypeProperty("property", owlModel.getXSDstring());
        cls.addSuperclass(owlModel.createOWLAllValuesFrom(property, new RDFSLiteral[]{
                owlModel.createRDFSLiteral("A"),
                owlModel.createRDFSLiteral("B")
        }));
        OntModel newModel = createOntModel();
        OntClass ontClass = newModel.getOntClass(cls.getURI());
        OntClass superClass = null;
        for (Iterator it = ontClass.listSuperClasses(); it.hasNext();) {
            superClass = (OntClass) it.next();
            if (superClass.canAs(AllValuesFromRestriction.class)) {
                break;
            }
        }
        AllValuesFromRestriction restriction =
                (AllValuesFromRestriction) superClass.as(AllValuesFromRestriction.class);
        DataRange dataRange = (DataRange) ((Resource) restriction.getAllValuesFrom()).as(DataRange.class);
        assertSize(2, dataRange.listOneOf());
        Iterator it = dataRange.listOneOf();
        Literal firstLiteral = (Literal) ((RDFNode) it.next()).as(Literal.class);
        assertEquals(XMLSchemaDatatypes.getDefaultXSDDatatype(ValueType.STRING),
                firstLiteral.getDatatype());
        assertEquals("A", firstLiteral.getString());
        Literal secondLiteral = (Literal) ((RDFNode) it.next()).as(Literal.class);
        assertEquals("B", secondLiteral.getString());
    }
}
