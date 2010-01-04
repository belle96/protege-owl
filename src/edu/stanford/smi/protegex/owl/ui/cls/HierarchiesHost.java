package edu.stanford.smi.protegex.owl.ui.cls;

/**
 * A component hosting a HierarchyManager.
 * This is basically the callback from the HierarchiesPanel to the OWLClassesTab,
 * for the case that the number of hierarchies has changed, and the divider
 * should be relocated.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface HierarchiesHost {

    void hierarchiesChanged(int newPreferredWidth);


    void showInferredHierarchy();
}
