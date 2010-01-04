package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLNames;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;

/**
 * A basic implementation of the OWLRestriction interface.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLRestriction extends AbstractOWLAnonymousClass implements OWLRestriction {


    public AbstractOWLRestriction(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    AbstractOWLRestriction() {
    }


    public static void checkExpression(String text, RDFProperty onProperty, RDFProperty restrictionProperty) throws OWLClassParseException {
        if (onProperty == null) {
            throw new OWLClassParseException("Please select a property.");
        }
        if (text.trim().length() == 0) {
            throw new OWLClassParseException("Please enter a filler.");
        }
        if (restrictionProperty.getName().equals(OWLNames.Slot.HAS_VALUE)) {
            DefaultOWLHasValue.checkFillerText(text, onProperty);
        }
        else if (restrictionProperty.getName().equals(OWLNames.Slot.ALL_VALUES_FROM)) {
            DefaultOWLAllValuesFrom.checkFillerText(text, onProperty, restrictionProperty.getOWLModel());
        }
        else if (restrictionProperty.getName().equals(OWLNames.Slot.SOME_VALUES_FROM)) {
            DefaultOWLSomeValuesFrom.checkFillerText(text, onProperty);
        }
        else {
            AbstractOWLCardinalityBase.checkFillerText(text, onProperty);
        }
    }


    protected String getBrowserTextFiller() {
        return isDefined() ? getFillerText() : "?";
    }


    /**
     * Gets a displayable form of the restricted slot.
     * If the slot is not specified yet, this will return a placeholder.
     * If the slot name contains spaces then the name will be put into apostrophs.
     *
     * @return the browser text of the slot
     */
    protected String getBrowserTextPropertyName() {
        RDFProperty property = getOnProperty();
        if (property == null) {
            return "<property>";
        }
        else {
            return property.getBrowserText();
        }
    }


    public String getNestedBrowserText() {
        return "(" + getBrowserText() + ")";
    }


    public RDFProperty getOnProperty() {
        final RDFProperty slot = getOWLModel().getRDFProperty(OWLNames.Slot.ON_PROPERTY);
        return (RDFProperty) getDirectOwnSlotValue(slot);
    }


    public boolean isDefined() {
        return getOnProperty() != null && isFillerDefined();
    }


    protected boolean isFillerDefined() {
        return getDirectOwnSlotValue(getFillerProperty()) != null;
    }


    public void setOnProperty(RDFProperty property) {
        final RDFProperty onPropertySlot = getOWLModel().getRDFProperty(OWLNames.Slot.ON_PROPERTY);
        setDirectOwnSlotValue(onPropertySlot, property);
    }
}
