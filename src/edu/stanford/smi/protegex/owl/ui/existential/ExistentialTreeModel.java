package edu.stanford.smi.protegex.owl.ui.existential;

import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLProperty;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Feb 5, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ExistentialTreeModel implements TreeModel {

    private OWLClass root;

    private ExistentialFillerProvider fillerProvider;

    public ExistentialTreeModel(OWLClass root, OWLObjectProperty property) {
        this.root = root;
        this.fillerProvider = new ExistentialFillerProvider(property);
    }

    public Object getRoot() {
        return root;
    }

    public List getChildren(Object object) {
        fillerProvider.reset();
        OWLClass cls = (OWLClass) object;
        cls.accept(fillerProvider);
        ArrayList list = new ArrayList(fillerProvider.getFillers());
        return list;
    }

    public Object getChild(Object object, int i) {
        return getChildren(object).toArray()[i];
    }

    public int getChildCount(Object object) {
        return getChildren(object).size();
    }

    public boolean isLeaf(Object object) {
        return getChildCount(object) == 0;
    }

    public void valueForPathChanged(TreePath treePath, Object object) {
    }

    public int getIndexOfChild(Object object, Object object1) {
        return getChildren(object).indexOf(object1);
    }

    public void addTreeModelListener(TreeModelListener treeModelListener) {
    }

    public void removeTreeModelListener(TreeModelListener treeModelListener) {
    }
}
