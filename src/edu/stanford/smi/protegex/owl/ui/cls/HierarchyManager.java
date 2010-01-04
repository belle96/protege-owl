package edu.stanford.smi.protegex.owl.ui.cls;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface HierarchyManager {

    void addHierarchy(Hierarchy hierarchy);


    void close(Hierarchy hierarchy);


    void spawnHierarchy(Hierarchy hierarchy);
}
