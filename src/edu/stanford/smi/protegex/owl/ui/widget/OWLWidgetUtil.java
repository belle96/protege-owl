package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.*;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLWidgetUtil {

    public static boolean isDatatypeProperty(RDFSNamedClass cls, RDFProperty property) {
        if (property instanceof OWLObjectProperty) {
            return false;
        }
        else if (property instanceof OWLDatatypeProperty) {
            return true;
        }
        else { // pure RDFProperty
            if (cls instanceof OWLNamedClass) {
                final RDFResource allValuesFrom = ((OWLNamedClass) cls).getAllValuesFrom(property);
                if (allValuesFrom instanceof RDFSDatatype) {
                    return true;
                }
            }
            return property.getRange() instanceof RDFSDatatype;
        }
    }


    public static boolean isDatatypeProperty(RDFSDatatype datatype, RDFSNamedClass cls, RDFProperty property) {
        if (cls instanceof OWLNamedClass) {
            OWLNamedClass namedClass = (OWLNamedClass) cls;
            if (datatype.equals(namedClass.getAllValuesFrom(property))) {
                return true;
            }
        }
        return datatype.equals(property.getRange());
    }


    public static boolean isFunctionalProperty(RDFSNamedClass cls, RDFProperty property) {
        if (cls instanceof OWLNamedClass) {
            OWLNamedClass namedClass = (OWLNamedClass) cls;
            final int maxCardinality = namedClass.getMaxCardinality(property);
            return maxCardinality == 0 || maxCardinality == 1;
        }
        else {
            return property.isFunctional();
        }
    }


    public static boolean isRangelessDatatypeProperty(RDFSNamedClass cls, RDFProperty property) {
        if (property instanceof OWLObjectProperty) {
            return false;
        }
        if (cls instanceof OWLNamedClass) {
            final OWLNamedClass namedClass = ((OWLNamedClass) cls);
            final RDFResource allValuesFrom = namedClass.getAllValuesFrom(property);
            return allValuesFrom == null;
        }
        else {
            return property.getRange() == null;
        }
    }


    public static boolean isRangelessObjectProperty(RDFSNamedClass cls, RDFProperty property) {
        if (property instanceof OWLDatatypeProperty) {
            return false;
        }
        if (cls instanceof OWLNamedClass) {
            final OWLNamedClass namedClass = ((OWLNamedClass) cls);
            final RDFResource allValuesFrom = namedClass.getAllValuesFrom(property);
            return allValuesFrom == null;
        }
        else {
            return property.getRange() == null && property instanceof OWLObjectProperty;
        }
    }


    public static boolean isRestrictedProperty(RDFSNamedClass namedClass, RDFProperty property) {
        if (namedClass instanceof OWLNamedClass) {
            OWLNamedClass namedCls = (OWLNamedClass) namedClass;
            Collection restrictions = namedCls.getRestrictions(property, true);
            if (!restrictions.isEmpty()) {
                return true;
            }
            for (Iterator it = property.getSuperproperties(false).iterator(); it.hasNext();) {
                RDFProperty superproperty = (RDFProperty) it.next();
                if (isRestrictedProperty(namedClass, (RDFProperty) superproperty)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isSingleDatatypeProperty(RDFSDatatype datatype, RDFSNamedClass cls, RDFProperty property) {
        return isDatatypeProperty(datatype, cls, property) &&
                isFunctionalProperty(cls, property);
    }
}
