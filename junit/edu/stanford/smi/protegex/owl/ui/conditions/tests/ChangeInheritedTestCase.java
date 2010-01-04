package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protegex.owl.model.OWLCardinality;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ChangeInheritedTestCase extends AbstractConditionsTableTestCase {

    public void testDeleteInheritedRestriction() {
        OWLObjectProperty genderProperty = owlModel.createOWLObjectProperty("gender");
        OWLNamedClass animalCls = owlModel.createOWLNamedClass("Animal");
        OWLCardinality restriction = owlModel.createOWLCardinality(genderProperty, 1);
        animalCls.addSuperclass(restriction);
        OWLNamedClass cls = owlModel.createOWLNamedSubclass("Person", animalCls);
        ConditionsTableModel tableModel = getTableModel(cls);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                animalCls,
                INHERITED,
                OWLCardinality.class
        });
        animalCls.removeSuperclass(restriction);
        assertTableModelStructure(tableModel, new Object[]{
                SUFFICIENT,
                NECESSARY,
                animalCls
        });
    }
}
