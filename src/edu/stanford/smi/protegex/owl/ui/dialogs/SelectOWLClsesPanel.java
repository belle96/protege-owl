package edu.stanford.smi.protegex.owl.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.DefaultRenderer;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protege.util.ModalDialog;
import edu.stanford.smi.protege.util.Validatable;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.cls.ClassTree;
import edu.stanford.smi.protegex.owl.ui.cls.ClassTreeRoot;
import edu.stanford.smi.protegex.owl.ui.search.finder.DefaultClassFind;
import edu.stanford.smi.protegex.owl.ui.search.finder.Find;
import edu.stanford.smi.protegex.owl.ui.search.finder.FindAction;
import edu.stanford.smi.protegex.owl.ui.search.finder.FindInDialogAction;
import edu.stanford.smi.protegex.owl.ui.search.finder.ResourceFinder;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

public class SelectOWLClsesPanel extends JComponent implements Validatable {

	private static final long serialVersionUID = -1715739135005096261L;
	
	protected ClassTree _tree;
    private boolean _allowsMultiple;
    protected OWLModel _owlModel;

    public SelectOWLClsesPanel(OWLModel kb) {
        this(kb, Collections.EMPTY_SET);
    }

    public SelectOWLClsesPanel(OWLModel kb, DefaultRenderer renderer) {
        this(kb, Collections.EMPTY_SET);
        _tree.setCellRenderer(renderer);
    }

    public SelectOWLClsesPanel(OWLModel kb, Collection clses) {
        this(kb, clses, true);
    }

    public SelectOWLClsesPanel(OWLModel kb, Collection clses, boolean allowsMultiple) {
    	_owlModel = kb;
        _allowsMultiple = allowsMultiple;
        if (clses.isEmpty()) {
            clses = CollectionUtilities.createCollection(kb.getOWLThingClass());
        }
        LazyTreeRoot root = new ClassTreeRoot(clses, OWLUI.getSortClassTreeOption());
        _tree = new ClassTree(null, root);
         
        _tree.setCellRenderer(new ResourceRenderer(false));
        int rows = _tree.getRowCount();
        int diff = rows - clses.size();
        for (int i = rows - 1; i > diff; --i) {
            _tree.expandRow(i);
        }
        _tree.setSelectionRow(0);
        setLayout(new BorderLayout());
        add(new JScrollPane(_tree), BorderLayout.CENTER);
        
        FindAction fAction = new FindInDialogAction(new DefaultClassFind(_owlModel, Find.CONTAINS),
                Icons.getFindClsIcon(),
                _tree, true);
		ResourceFinder finder = new ResourceFinder(fAction);
        add(finder, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(300, 300));
    }


	public Collection getSelection() {
        return _tree.getSelection();
    }
    

	public boolean validateContents() {
        boolean isValid = _allowsMultiple || getSelection().size() <= 1;
        if (!isValid) {
            ModalDialog.showMessageDialog(this, "Only 1 class can be selected", ModalDialog.MODE_CLOSE);
        }
        return isValid;
    }


	public void saveContents() {
        // do nothing
    }
	

	
}
