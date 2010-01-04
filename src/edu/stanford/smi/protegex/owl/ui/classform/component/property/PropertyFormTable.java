package edu.stanford.smi.protegex.owl.ui.classform.component.property;

import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.util.ClosureAxiomFactory;
import edu.stanford.smi.protegex.owl.ui.cls.OWLClassesTab;
import edu.stanford.smi.protegex.owl.ui.clsdesc.ClassDescriptionEditorComponent;
import edu.stanford.smi.protegex.owl.ui.code.OWLSymbolPanel;
import edu.stanford.smi.protegex.owl.ui.code.OWLTextAreaPanel;
import edu.stanford.smi.protegex.owl.ui.code.SymbolEditorComponent;
import edu.stanford.smi.protegex.owl.ui.code.SymbolErrorDisplay;
import edu.stanford.smi.protegex.owl.ui.owltable.SymbolTable;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A SymbolTable showing a PropertyFormTableModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyFormTable extends SymbolTable implements Disposable {

    private PropertyFormTableModel tableModel;


    public PropertyFormTable(PropertyFormTableModel tableModel, OWLNamedClass namedClass, RDFProperty property) {
        super(tableModel, namedClass.getOWLModel(), true, new OWLSymbolPanel(namedClass.getOWLModel(), true, true));
        this.tableModel = tableModel;
    }


    protected SymbolEditorComponent createSymbolEditorComponent(OWLModel model, SymbolErrorDisplay errorDisplay) {
        return new ClassDescriptionEditorComponent(model, errorDisplay, true);
    }


    public void dispose() {
        tableModel.dispose();
    }


    protected String editMultiLine(RDFResource input) {
        if (input instanceof RDFSClass) {
            return OWLTextAreaPanel.showEditDialog(this, getOWLModel(), (RDFSClass) input);
        }
        else {
            return null;
        }
    }


    protected Collection getNavigationMenuItems(RDFResource resource) {
        if (resource instanceof RDFSClass) {
            Set set = new HashSet();
            ((RDFSClass) resource).getNestedNamedClasses(set);
            return set;
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }


    public PropertyFormTableModel getTableModel() {
        return tableModel;
    }


    protected String getToolTipText(RDFResource resource) {
        if (resource instanceof RDFSClass) {
            String str = OWLUI.getOWLToolTipText((RDFSClass) resource);
            if (str != null && str.length() > 0) {
                return str;
            }
        }
        return null;
    }


    public boolean isClosed() {
        if (tableModel.getRowCount() > 0) {
            OWLExistentialRestriction restriction = tableModel.getRestriction(0);
            return ClosureAxiomFactory.getClosureAxiom(tableModel.getNamedClass(), restriction) != null;
        }
        else {
            return false;
        }
    }


    protected void navigateTo(final RDFResource instance) {
        OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(this);
        if (tab != null && instance instanceof RDFSNamedClass) {
            tab.setSelectedCls((RDFSNamedClass) instance);
        }
    }


    public void setClosed(boolean closed) {
        // TODO
    }
}
