package edu.stanford.smi.protegex.owl.ui.cls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.stanford.smi.protege.util.LazyTreeNode;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.triplestore.Triple;
import edu.stanford.smi.protegex.owl.model.triplestore.impl.DefaultTriple;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.TripleSelectable;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;
import edu.stanford.smi.protegex.owl.ui.subsumption.TooltippedSelectableTree;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

/**
 * @author Holger Knublauch <holger@knublauch.com>
 */
public class ClassTree extends TooltippedSelectableTree implements TripleSelectable, HostResourceDisplay {

    public ClassTree(Action doubleClickAction, LazyTreeRoot root) {
        super(doubleClickAction, root);
        setCellRenderer(new ResourceRenderer() {
            @Override
            public void setMainText(String text) {
                super.setMainText(removeAllQuotes(text));
            }
        });
    }

    private static final char SINGLE_QUOTE = '\'';

    private String removeAllQuotes(String text) {
        if (text != null && text.length() > 0 && text.charAt(0) == SINGLE_QUOTE
                && text.charAt(text.length() - 1) == SINGLE_QUOTE) {
            return text.replaceAll("'", "");
        }
        return text;
    }

    public List getPrototypeTriples() {
        List triples = new ArrayList();
        Iterator it = getSelection().iterator();
        while (it.hasNext()) {
            Object sel = it.next();
            if (sel instanceof RDFSNamedClass) {
                RDFSNamedClass object = (RDFSNamedClass) sel;
                RDFProperty predicate = object.getOWLModel().getRDFSSubClassOfProperty();
                triples.add(new DefaultTriple(null, predicate, object));
            }
        }
        return triples;
    }

    public List getSelectedTriples() {
        List results = new ArrayList();
        TreePath[] paths = getSelectionPaths();
        if (paths != null) {
            for (TreePath path : paths) {
                if (path.getPathCount() > 1 && path.getLastPathComponent() instanceof LazyTreeNode) {
                    LazyTreeNode node = (LazyTreeNode) path.getLastPathComponent();
                    Object subject = node.getUserObject();
                    if (subject instanceof RDFSNamedClass) {
                        RDFSNamedClass subjectClass = (RDFSNamedClass) subject;
                        TreeNode parent = node.getParent();
                        if (parent instanceof LazyTreeNode) {
                            Object object = ((LazyTreeNode) parent).getUserObject();
                            if (object instanceof RDFSNamedClass) {
                                RDFSNamedClass objectClass = (RDFSNamedClass) object;
                                RDFProperty predicate = objectClass.getOWLModel().getRDFSSubClassOfProperty();
                                Triple triple = new DefaultTriple(subjectClass, predicate, objectClass);
                                results.add(triple);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public void setSelectedTriples(Collection triples) {
        // TODO
    }

    public boolean displayHostResource(RDFResource resource) {
        return OWLUI.setSelectedNodeInTree(this, resource);
    }

    public Collection getRoots() {
        return (Collection) ((LazyTreeRoot) getModel().getRoot()).getUserObject();
    }

}
