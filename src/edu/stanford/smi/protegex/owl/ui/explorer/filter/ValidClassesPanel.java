package edu.stanford.smi.protegex.owl.ui.explorer.filter;

import edu.stanford.smi.protege.util.DefaultRenderer;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Iterator;

/**
 * A JPanel that can be used to select the types of classes that shall be filtered
 * by a DefaultExplorerFilter.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ValidClassesPanel extends JPanel {

    private DefaultExplorerFilter filter;

    private JList list;

    public static final int PREFERRED_WIDTH = 260;


    public ValidClassesPanel(DefaultExplorerFilter filter) {
        this.filter = filter;

        list = new JList(new ListItem[]{
                new ListItem(RDFSNamedClass.class, "Named classes", OWLIcons.PRIMITIVE_OWL_CLASS),
                new ListItem(OWLAllValuesFrom.class, "AllValuesFrom restrictions", OWLIcons.OWL_ALL_VALUES_FROM),
                new ListItem(OWLSomeValuesFrom.class, "SomeValuesFrom restrictions", OWLIcons.OWL_SOME_VALUES_FROM),
                new ListItem(OWLHasValue.class, "HasValue restrictions", OWLIcons.OWL_HAS_VALUE),
                new ListItem(OWLCardinality.class, "Cardinality restrictions", OWLIcons.OWL_CARDINALITY),
                new ListItem(OWLMinCardinality.class, "MinCardinality restrictions", OWLIcons.OWL_MIN_CARDINALITY),
                new ListItem(OWLMaxCardinality.class, "MaxCardinality restrictions", OWLIcons.OWL_MAX_CARDINALITY),
                new ListItem(OWLIntersectionClass.class, "Intersection classes", OWLIcons.OWL_INTERSECTION_CLASS),
                new ListItem(OWLUnionClass.class, "Union classes", OWLIcons.OWL_UNION_CLASS),
                new ListItem(OWLComplementClass.class, "Complement classes", OWLIcons.OWL_COMPLEMENT_CLASS),
                new ListItem(OWLEnumeratedClass.class, "Enumerated classes", OWLIcons.OWL_ENUMERATED_CLASS)
        });
        list.setCellRenderer(new DefaultRenderer() {
            public void load(Object o) {
                ListItem item = (ListItem) o;
                setMainIcon(item.icon);
                setMainText(item.name);
            }
        });
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                updateFilter();
            }
        });
        updateListSelection();
        OWLLabeledComponent lc = new OWLLabeledComponent("Show only Classes of Types", new JScrollPane(list));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, lc);

        setPreferredSize(new Dimension(PREFERRED_WIDTH, 260));
    }


    private int getListIndex(Class c) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            ListItem listItem = ((ListItem) list.getModel().getElementAt(i));
            if (listItem.type == c) {
                return i;
            }
        }
        return -1;
    }


    private void updateFilter() {
        filter.removeAllValidClasses();
        int[] sels = list.getSelectedIndices();
        for (int i = 0; i < sels.length; i++) {
            ListItem listItem = ((ListItem) list.getModel().getElementAt(sels[i]));
            filter.addValidClass(listItem.type);
        }
    }


    private void updateListSelection() {
        ListSelectionModel selectionModel = list.getSelectionModel();
        selectionModel.clearSelection();
        Iterator it = filter.getValidClasses().iterator();
        while (it.hasNext()) {
            Class c = (Class) it.next();
            int index = getListIndex(c);
            selectionModel.addSelectionInterval(index, index);
        }
    }


    private class ListItem {

        Class type;

        Icon icon;

        String name;


        ListItem(Class type, String name, String iconName) {
            this.type = type;
            this.icon = OWLIcons.getImageIcon(iconName);
            this.name = name;
        }
    }
}
