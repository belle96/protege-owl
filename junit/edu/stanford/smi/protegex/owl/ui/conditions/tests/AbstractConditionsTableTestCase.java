package edu.stanford.smi.protegex.owl.ui.conditions.tests;

import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableConstants;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractConditionsTableTestCase extends AbstractJenaTestCase
        implements ConditionsTableConstants {

    protected List events = new ArrayList();

    protected final static String SUFFICIENT = "SUFFICIENT";

    protected final static Integer NECESSARY = new Integer(TYPE_SUPERCLASS);

    protected final static Integer INHERITED = new Integer(TYPE_INHERITED);


    protected void assertTableModelStructure(ConditionsTableModel tableModel, Object[] structure) {
        assertEquals("Unexpected tableModel size", structure.length, tableModel.getRowCount());
        for (int i = 0; i < structure.length; i++) {
            Object o = structure[i];
            if (o == null) {
                assertTrue("Separator expected at row " + i, tableModel.isSeparator(i));
            }
            else if (o.equals(SUFFICIENT)) {
                assertTrue("Sufficient separator expected at row " + i,
                        tableModel.isSeparator(i) && tableModel.isDefinition(i));
            }
            else if (o instanceof Integer) {
                assertTrue("Separator of " + o + " expected at row " + i,
                        tableModel.isSeparator(i) && tableModel.getType(i) == ((Integer) o).intValue());
            }
            else if (o instanceof Class) {
                Class c = (Class) o;
                assertTrue(c.getName() + " expected at row " + i, c.isAssignableFrom(tableModel.getClass(i).getClass()));
            }
            else {
                assertEquals("" + o + " expected at row " + i, o, tableModel.getClass(i));
            }
        }
    }


    protected TableModelEvent getEvent(int index) {
        return (TableModelEvent) events.get(index);
    }


    protected ConditionsTableModel getTableModel(OWLNamedClass hostCls) {
        events.clear();
        ConditionsTableModel tableModel = new ConditionsTableModel(hostCls,
                owlModel.getSlot(Model.Slot.DIRECT_SUPERCLASSES));
        tableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                events.add(e);
            }
        });
        return tableModel;
    }
}
