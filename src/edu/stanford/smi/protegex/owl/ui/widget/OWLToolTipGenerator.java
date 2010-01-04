package edu.stanford.smi.protegex.owl.ui.widget;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;

/**
 * An interface for objects that can generate tool tip texts for class trees,
 * OWLTables, etc.
 * <p/>
 * To change the default tool tip renderer use OWLUI.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLToolTipGenerator {

    /**
     * Gets a tool tip text for a given RDFSClass.
     *
     * @param aClass the RDFSClass to get the tool tip for
     * @return the text or null if there is no tool tip available for aClass
     */
    String getToolTipText(RDFSClass aClass);

    /**
     * Gets a tool tip text for a given RDFProperty.
     *
     * @param prop the RDFProperty to get the tool tip for
     * @return the text or null if there is no tool tip available for prop
     */
    String getToolTipText(RDFProperty prop);

    String getToolTipText(RDFResource res);
}
