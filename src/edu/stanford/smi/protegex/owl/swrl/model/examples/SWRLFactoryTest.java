package edu.stanford.smi.protegex.owl.swrl.model.examples;

import java.util.ArrayList;
import java.util.Collection;

import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.jena.creator.OwlProjectFromUriCreator;
import edu.stanford.smi.protegex.owl.model.OWLDataRange;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFObject;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLAtomList;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLBuiltinAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDataRangeAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLDatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLFactory;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLIndividualPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLSameIndividualAtom;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLVariable;

public class SWRLFactoryTest {


    public static void main(String[] args) throws Exception {
        Collection errors = new ArrayList();
        OwlProjectFromUriCreator creator = new OwlProjectFromUriCreator();
        creator.setOntologyUri("http://protege.stanford.edu/plugins/owl/testdata/importSWRL.owl");
        creator.create(errors);
        JenaOWLModel owlModel = creator.getOwlModel();

        if (!errors.isEmpty()) {
            System.err.println("Error loading importSWRL.owl.");
            System.exit(-1);
        } // if


        testRuleCreation((SWRLFactory) ((KnowledgeBase) owlModel).getFrameFactory(), owlModel);

    } //  main

    // Take a simple rule and use the SWRLFactory to create the OWL
    // instances representing that rule.
    //
    // Pizza(?x) /\ sameAs(?x, ?y) /\ lessThan(?x, ?z) /\ hasIngredient(?x, ?z) /\ myDatatypeProperty(?x, 'ddd') -> PizzaBase(?y)


    private static void testRuleCreation(SWRLFactory swrlFactory, JenaOWLModel owlModel) {
        SWRLImp imp;
        SWRLAtomList head, body;
        SWRLClassAtom classAtom;
        SWRLSameIndividualAtom sameIndividualAtom;
        SWRLDataRangeAtom dataRangeAtom;
        SWRLDatavaluedPropertyAtom datavaluedPropertyAtom;
        SWRLBuiltinAtom builtinAtom;
        SWRLIndividualPropertyAtom individualPropertyAtom;
        SWRLBuiltin lessThanBuiltin;
        OWLDatatypeProperty myDatatypeProperty;
        OWLObjectProperty hasIngredient;
        RDFObject literalValue;
        OWLNamedClass pizzaClass, pizzaBase;
        SWRLVariable x, y, z;
        Collection<RDFObject> arguments;

        // OWL classes
        pizzaClass = owlModel.createOWLNamedClass("Pizza");
        pizzaBase = owlModel.createOWLNamedClass("PizzaBase");

        // OWL object properties
        hasIngredient = owlModel.createOWLObjectProperty("hasIngredient");

        // OWL datatype properties
        myDatatypeProperty = owlModel.createOWLDatatypeProperty("myDatatypeProperty");

        // x, y, z - variables
        x = swrlFactory.createVariable("x");
        y = swrlFactory.createVariable("y");
        z = swrlFactory.createVariable("z");

        // Built-ins
        lessThanBuiltin = swrlFactory.createBuiltin("lessThan");

        // head and body
        head = swrlFactory.createAtomList();
        body = swrlFactory.createAtomList();

        // Pizza(?x)
        classAtom = swrlFactory.createClassAtom(pizzaClass, x);
        body.append(classAtom);

        // PizzaBase(?z)
        classAtom = swrlFactory.createClassAtom(pizzaBase, z);
        head.append(classAtom);

        // sameAs(?x, ?y)
        sameIndividualAtom = swrlFactory.createSameIndividualAtom(x, y);
        head.append(sameIndividualAtom);

        // lessThan(?x, ?y)
        arguments = new ArrayList<RDFObject>();
        arguments.add(x);
        arguments.add(y);
        builtinAtom = swrlFactory.createBuiltinAtom(lessThanBuiltin, arguments.iterator());
        head.append(builtinAtom);

        // hasIngredient(?x, ?y)
        individualPropertyAtom = swrlFactory.createIndividualPropertyAtom(hasIngredient, x, z);
        head.append(individualPropertyAtom);

        // myDatatypeProperty(?x, 'ddd')
        literalValue = owlModel.createRDFSLiteral("ddd", owlModel.getXSDstring());
        datavaluedPropertyAtom = swrlFactory.createDatavaluedPropertyAtom(myDatatypeProperty, x, literalValue);
        head.append(datavaluedPropertyAtom);

        // myDataRange
        OWLDataRange dataRange = owlModel.createOWLDataRange(new RDFSLiteral[]{
                owlModel.createRDFSLiteral("first"),
                owlModel.createRDFSLiteral("second")
        });
        dataRangeAtom = swrlFactory.createDataRangeAtom(dataRange, x);
        head.append(dataRangeAtom);

        imp = swrlFactory.createImp(head, body);

        System.out.println("Imp: " + imp.getBrowserText());

        //owlModel.dumpRDF();

    } // testRuleCreation

} // SWRLFactoryTest
