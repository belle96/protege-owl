package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protegex.owl.model.*;

import java.util.Collections;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultRDFSDatatypeFactory implements RDFSDatatypeFactory {

    private OWLModel owlModel;


    public DefaultRDFSDatatypeFactory(OWLModel owlModel) {
        this.owlModel = owlModel;
    }


    public RDFSDatatype createAnonymousDatatype(RDFSDatatype baseType) {
        String name = owlModel.getNextAnonymousResourceName();
        return createDatatype(baseType, name);
    }


    public RDFSDatatype createDatatype(RDFSDatatype baseType, String name) {
        RDFSDatatype datatype = owlModel.createRDFSDatatype(name);
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_BASE);
        datatype.setPropertyValue(property, baseType);
        return datatype;
    }


    public void setLength(RDFSDatatype datatype, int value) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_LENGTH);
        if (value >= 0) {
            datatype.setPropertyValue(property, new Integer(value));
        }
        else {
            datatype.setPropertyValues(property, Collections.EMPTY_LIST);
        }
    }


    public void setMaxExclusive(RDFSDatatype datatype, RDFSLiteral literal) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MAX_EXCLUSIVE);
        datatype.setPropertyValue(property, literal);
    }


    public void setMaxInclusive(RDFSDatatype datatype, RDFSLiteral literal) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MAX_INCLUSIVE);
        datatype.setPropertyValue(property, literal);
    }


    public void setMaxLength(RDFSDatatype datatype, int value) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MAX_LENGTH);
        if (value >= 0) {
            datatype.setPropertyValue(property, new Integer(value));
        }
        else {
            datatype.setPropertyValues(property, Collections.EMPTY_LIST);
        }
    }


    public void setMinExclusive(RDFSDatatype datatype, RDFSLiteral literal) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MIN_EXCLUSIVE);
        datatype.setPropertyValue(property, literal);
    }


    public void setMinInclusive(RDFSDatatype datatype, RDFSLiteral literal) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MIN_INCLUSIVE);
        datatype.setPropertyValue(property, literal);
    }


    public void setMinLength(RDFSDatatype datatype, int value) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_MIN_LENGTH);
        if (value >= 0) {
            datatype.setPropertyValue(property, new Integer(value));
        }
        else {
            datatype.setPropertyValues(property, Collections.EMPTY_LIST);
        }
    }


    public void setPattern(RDFSDatatype datatype, String value) {
        RDFProperty property = XSPNames.getRDFProperty(owlModel, XSPNames.XSP_PATTERN);
        datatype.setPropertyValue(property, value);
    }
}
