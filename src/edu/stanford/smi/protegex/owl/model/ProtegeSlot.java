package edu.stanford.smi.protegex.owl.model;

import edu.stanford.smi.protege.event.SlotListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;

import java.util.Collection;

/**
 * An interface to wrap the Protege Slot interface for OWL.
 * This basically deprecates most methods and points to their
 * replacements in the OWL API.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface ProtegeSlot extends ProtegeInstance, Slot {

    /**
     * @see RDFProperty#addSuperproperty
     * @deprecated
     */
    void addDirectSuperslot(Slot slot);


    /**
     * @see RDFProperty#addPropertyListener
     * @deprecated
     */
    void addSlotListener(SlotListener listener);


    /**
     * @see RDFProperty#getUnionRangeClasses
     * @deprecated
     */
    Collection getAllowedClses();


    /**
     * @deprecated set the value of the protege:allowedParents property directly
     */
    Collection getAllowedParents();


    /**
     * @see RDFProperty#getRange
     * @deprecated compare to OWLDataRange as range
     */
    Collection getAllowedValues();


    /**
     * @see RDFProperty#isFunctional
     * @deprecated
     */
    boolean getAllowsMultipleValues();


    /**
     * @deprecated no OWL equivalent
     */
    Facet getAssociatedFacet();


    /**
     * @deprecated no OWL equivalent
     */
    Collection getDefaultValues();


    /**
     * @see RDFProperty#getSubpropertyCount
     * @deprecated
     */
    int getDirectSubslotCount();


    /**
     * @see RDFProperty#getSubproperties (false)
     * @deprecated
     */
    Collection getDirectSubslots();


    /**
     * @see RDFProperty#getSuperpropertyCount
     * @deprecated
     */
    int getDirectSuperslotCount();


    /**
     * @see RDFProperty#getSuperproperties (false)
     * @deprecated
     */
    Collection getDirectSuperslots();


    /**
     * @see RDFProperty#isSubpropertyOf (false)
     * @deprecated
     */
    boolean hasDirectSuperslot(Slot slot);


    /**
     * @see RDFProperty#isSubpropertyOf (true)
     * @deprecated
     */
    boolean hasSuperslot(Slot slot);


    /**
     * @deprecated not needed
     */
    void moveDirectSubslot(Slot movedCls, Slot afterCls);


    /**
     * @see RDFResource#getComments
     * @deprecated
     */
    Collection getDocumentation();


    /**
     * @see RDFProperty#getInverseProperty
     * @deprecated
     */
    Slot getInverseSlot();


    /**
     * @see RDFProperty#isFunctional
     * @deprecated
     */
    int getMaximumCardinality();


    /**
     * @deprecated no OWL equivalent
     */
    Number getMaximumValue();


    /**
     * @deprecated no OWL equivalent
     */
    int getMinimumCardinality();


    /**
     * @deprecated no OWL equivalent
     */
    Number getMinimumValue();


    /**
     * @see RDFProperty#getSubproperties (true)
     * @deprecated
     */
    Collection getSubslots();


    /**
     * @see RDFProperty#getSuperproperties (true)
     * @deprecated
     */
    Collection getSuperslots();


    /**
     * @see RDFProperty#getUnionDomain (false)
     * @deprecated
     */
    Collection getDirectDomain();


    /**
     * @see RDFProperty#getUnionDomain (true)
     * @deprecated
     */
    Collection getDomain();


    /**
     * Returns the "template slot values" for a top level slot.  Usually this is empty.
     * Beware: this method probably doesn't do what you think!  It does NOT return own slot values at a particular frame.
     * To get own slot values at a class see {@link edu.stanford.smi.protege.model.Frame#getOwnSlotValues(Slot)}
     * What it does do is return the values which will become template slot values when this slot is attached to a class.
     */
    Collection getValues();


    /**
     * @see RDFProperty#getRange (true)
     * @deprecated
     */
    ValueType getValueType();


    /**
     * @deprecated not needed
     */
    boolean hasValueAtSomeFrame();


    /**
     * @see RDFProperty#removeSuperproperty
     * @deprecated
     */
    void removeDirectSuperslot(Slot slot);


    /**
     * @see RDFProperty#removePropertyListener
     * @deprecated
     */
    void removeSlotListener(SlotListener listener);


    /**
     * @see RDFProperty#setUnionRangeClasses
     * @deprecated
     */
    void setAllowedClses(Collection clses);


    /**
     * @deprecated set the value of the protege:allowedParents property directly
     */
    void setAllowedParents(Collection parents);


    /**
     * @see RDFProperty#setRange
     * @deprecated set an OWLDataRange as range
     */
    void setAllowedValues(Collection values);


    /**
     * @see RDFProperty#setFunctional
     * @deprecated
     */
    void setAllowsMultipleValues(boolean b);


    /**
     * @deprecated no OWL equivalent
     */
    void setAssociatedFacet(Facet facet);


    /**
     * @deprecated no OWL equivalent
     */
    void setDefaultValues(Collection values);


    /**
     * @deprecated not needed
     */
    void setDirectTypeOfSubslots(Cls cls);


    /**
     * @see RDFResource#setComment
     * @deprecated
     */
    void setDocumentation(String doc);


    /**
     * @see RDFProperty#setInverseProperty
     * @deprecated
     */
    void setInverseSlot(Slot slot);


    /**
     * @see RDFProperty#setFunctional
     * @deprecated
     */
    void setMaximumCardinality(int max);


    /**
     * @deprecated no OWL equivalent
     */
    void setMaximumValue(Number n);


    /**
     * @deprecated no OWL equivalent
     */
    void setMinimumCardinality(int min);


    /**
     * @deprecated no OWL equivalent
     */
    void setMinimumValue(Number n);


    /**
     * @deprecated no OWL equivalent
     */
    void setValues(Collection values);


    /**
     * @see RDFProperty#setRange
     * @deprecated
     */
    void setValueType(ValueType type);
}
