package edu.stanford.smi.protegex.owl.ui.conditions;

import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An object representing an entry in the table.  One instance is kept for
 * each row.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConditionsTableItem implements ConditionsTableConstants, Comparable {


    protected RDFSClass aClass;

    private OWLIntersectionClass definition;

    private boolean isNew;

    private OWLNamedClass originCls;

    private int type;

    public static final String INHERITED = "INHERITED ";

    public static final String NECESSARY = "NECESSARY ";

    public static final String SUFFICIENT = "NECESSARY & SUFFICIENT ";

	private List<RDFSNamedClass> owlRestrictionMetaClses = null;


    protected ConditionsTableItem(RDFSClass aClass,
                                int type,
                                OWLNamedClass originCls,
                                OWLIntersectionClass definition,
                                boolean isNew) {
        this.aClass = aClass;
        this.definition = definition;
        this.type = type;
        this.originCls = originCls;
        this.isNew = isNew;
    }

    public void dispose() {
        this.aClass = null;
        this.definition = null;        
        this.originCls = null;        
    }
    
    /**
     * Sorts according to the following order:
     * 1) Equivalent classes > superclasses > inherited superclasses
     * 2) NamedClasses by name
     * 3) Anonymous classes > restrictions
     * 4) Restrictions by slot name
     *
     * @param o the other ClassDescriptionItem to compare to
     * @return -1 if this is smaller (higher in the list), 1 if the other is smaller
     */
    public int compareTo(Object o) {    	
        if (o instanceof ConditionsTableItem) {
            ConditionsTableItem other = (ConditionsTableItem) o;
            
            if (type > other.type) {
                return -1;
            }
            else if (type < other.type) {
                return 1;
            }
            else {   // type == other.type
                if (other.isSeparator()) {
                    return 1;
                }
                if (isSeparator()) {
                    return -1;
                }
                else {
                    return compareToWithSameType(other.aClass);
                }
            }
        }
        else {
            return 0;
        }
    }


    int compareToWithSameType(RDFSClass otherClass) {
        if (aClass instanceof OWLNamedClass) {
            if (otherClass instanceof OWLNamedClass) {
                return aClass.compareTo(otherClass);
            }
            else {
                return -1;
            }
        }
        else if (otherClass instanceof OWLNamedClass) {
            return 1;
        }
        else {
            return compareToWithAnonymousClses((OWLAnonymousClass) otherClass);
        }
    }


    private int compareToWithAnonymousClses(OWLAnonymousClass otherCls) {
        if (aClass instanceof OWLRestriction) {
            if (otherCls instanceof OWLRestriction) {
                return compareToWithRestrictions((OWLRestriction) otherCls);
            }
            else {
                return 1;
            }
        }
        else {
            return aClass.compareTo(otherCls);
        }
    }


    private int compareToWithRestrictions(OWLRestriction otherCls) {
        OWLRestriction restriction = (OWLRestriction) aClass;
        RDFProperty property = restriction.getOnProperty();
        RDFProperty otherProperty = otherCls.getOnProperty();
        if (property.equals(otherProperty)) {            
             if (owlRestrictionMetaClses == null) {            	 
            	 owlRestrictionMetaClses = Arrays.asList(aClass.getOWLModel().getOWLRestrictionMetaclasses());
             }
			int clsIndex = owlRestrictionMetaClses.indexOf(restriction.getProtegeType());
            int otherIndex = owlRestrictionMetaClses.indexOf(otherCls.getProtegeType());
            return new Integer(clsIndex).compareTo(new Integer(otherIndex));
        }
        else {
            return property.compareTo(otherProperty);
        }
    }


    protected static ConditionsTableItem create(RDFSClass aClass, int type) {
        return new ConditionsTableItem(aClass, type, null, null, false);
    }


    protected static ConditionsTableItem createInherited(RDFSClass aClass, OWLNamedClass originCls) {
        return new ConditionsTableItem(aClass, TYPE_INHERITED, originCls, null, false);
    }


    protected static ConditionsTableItem createNew(int type) {
        return new ConditionsTableItem(null, type, null, null, true);
    }


    protected static ConditionsTableItem createSufficient(RDFSClass aClass, int type, OWLIntersectionClass definition) {
        return new ConditionsTableItem(aClass, type, null, definition, false);
    }


    protected static ConditionsTableItem createSeparator(int type) {
        return new ConditionsTableItem(null, type, null, null, false);
    }


    public RDFSClass getCls() {
        return aClass;
    }


    protected OWLIntersectionClass getDefinition() {
        return definition;
    }


    protected Icon getIcon(int rowHeight) {
        if (isSeparator()) {
            return null;
        }
        else {
            if (isDefinition()) {
                return null; // return new DefinitionIcon(localIndex, rowHeight);
                //OWLIcons.getImageIcon("EquivalentClass");
            }
            else if (type == TYPE_INHERITED) {
                return OWLIcons.getImageIcon("SuperclassInherited");
            }
            else if (type == TYPE_SUPERCLASS) {
                return getSuperclassIcon();
            }
        }
        return null;
    }


    public OWLNamedClass getOriginCls() {
        return originCls;
    }


    protected static Icon getSuperclassIcon() {
        return OWLIcons.getImageIcon("Superclass");
    }


    protected int getType() {
        return type;
    }


    protected boolean isDefinition() {
        return type >= TYPE_DEFINITION_BASE;
    }


    protected boolean isInherited() {
        return type == TYPE_INHERITED;
    }


    protected boolean isNew() {
        return isNew;
    }


    protected boolean isSeparator() {
        return aClass == null && !isNew();
    }

    protected void setType(int value) {
        this.type = value;
    }


    public String toString() {
        if (isSeparator()) {
            if (isDefinition()) {
                return SUFFICIENT;
            }
            else if (isInherited()) {
                return INHERITED;
            }
            else {
                return NECESSARY;
            }
        }
        else {
            if (isInherited()) {
                return getDisplayText() + "    [" + originCls.getBrowserText() + "]";
            }
            else if (isNew()) {
                return "";
            }
            else {
                return getDisplayText();
            }
        }
    }


    private String getDisplayText() {
        return aClass.getBrowserText();
    }
}
