package edu.stanford.smi.protegex.owl.model.framestore.updater;

import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

/**
 * An object capable of mapping restrictions into facet overrides, and vice versa.
 * The goal of these classes is to ensure a maximum of compatibility to existing Protege
 * components, such as the template slots widget, that allows to edit facet overrides
 * on a per class basis.  Furthermore, this class makes sure that legacy Protege
 * ontologies can be converted into OWL (so that restrictions are created for facet
 * overrides) and that OWL files can be saved in other formats as well (because restrictions
 * are mirrored in restrictions that are not exported in most formats).
 * <BR>
 * Depending on the type of event, this class does the following:
 * <UL>
 * <LI>directSuperclassAdded/Removed (at OWLNamedClass): Updates facet overrides </LI>
 * <LI>directTemplateSlotAdded/Removed (at OWLRestriction): Updates facet overrides </LI>
 * <LI>templateFacetValueChanged (at OWLRestriction, i.e. filler changed): Updates facet overrides </LI>
 * <LI>templateFacetValueChanged (at OWLNamedClass): Updates restrictions </LI>
 * </UL>
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface RestrictionUpdater {

    void copyFacetValuesIntoNamedClass(RDFSNamedClass cls, OWLRestriction restriction);


    void updateRestrictions(OWLNamedClass cls, RDFProperty property, Facet facet);
}
