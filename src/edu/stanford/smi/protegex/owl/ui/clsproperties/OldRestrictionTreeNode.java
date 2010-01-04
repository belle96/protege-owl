package edu.stanford.smi.protegex.owl.ui.clsproperties;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.InstanceDisplay;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsWidget;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.*;

public class OldRestrictionTreeNode extends RestrictionTreeNode {

    private RDFSClass inheritedFromClass;

    private PropertyRestrictionsTree tree;


    public OldRestrictionTreeNode(PropertyRestrictionsTree tree,
                                  OWLRestriction restriction,
                                  RDFSClass inheritedFromClass) {
        this.inheritedFromClass = inheritedFromClass;
        this.tree = tree;
        setUserObject(restriction);
    }


    public void checkExpression(String text) throws Throwable {
        getRestriction().checkFillerText(text);
    }


    public String getFillerText() {
        return getRestriction().getFillerText();
    }


    public RDFSClass getInheritedFromClass() {
        return inheritedFromClass;
    }


    public char getOperator() {
        return getRestriction().getOperator();
    }


    public OWLRestriction getRestriction() {
        return (OWLRestriction) getUserObject();
    }


    public Cls getRestrictionMetaCls() {
        return getRestriction().getProtegeType();
    }


    public Icon getIcon() {
        OWLRestriction restriction = getRestriction();
        Icon baseIcon = ProtegeUI.getIcon(restriction);
        if (inheritedFromClass == null) {
            return baseIcon;
        }
        else {
            if (baseIcon instanceof ImageIcon) {
                return OWLIcons.getInheritedClsIcon((ImageIcon) baseIcon);
            }
            else {
                return baseIcon;
            }
        }
    }


    public boolean isInherited() {
        return inheritedFromClass != null;
    }


    public void setUserObject(Object userObject) {
        if (userObject instanceof String) {
            if (getRestriction().isIncluded()) {
                return;
            }
            String expr = (String) userObject;
            OWLModel owlModel = getRestriction().getOWLModel();
            try {
                getRestriction().checkFillerText(expr);
                try {
                    owlModel.beginTransaction("Change restriction filler of " +
                            getRestriction().getBrowserText() + " to " + expr);
                    getRestriction().setFillerText(expr);
                    owlModel.commitTransaction();
                }
                catch (Exception ex) {
                	owlModel.rollbackTransaction();
                    OWLUI.handleError(owlModel, ex);
                }
                updateConditionsWidget();
            }
            catch (Exception ex) {
                ProtegeUI.getModalDialogFactory().showMessageDialog(owlModel,
                        "Illegal restriction filler: " + expr);
            }
        }
        else {
            super.setUserObject(userObject);
        }
    }


    // A dirty hack because I am too lazy to attach the ConditionsWidget to all restrictions
    private void updateConditionsWidget() {
        Component c = tree.getParent();
        while (c != null && !(c instanceof InstanceDisplay)) {
            c = c.getParent();
        }
        if (c instanceof InstanceDisplay) {
            ConditionsWidget cw = (ConditionsWidget) OWLUI.searchComponentOfType((Container) c, ConditionsWidget.class);
            if (cw != null) {
                cw.repaint();
            }
        }
    }
}
