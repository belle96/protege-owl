package edu.stanford.smi.protegex.owl.jena.graph;

import com.hp.hpl.jena.graph.Capabilities;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeCapabilities implements Capabilities {


    public boolean addAllowed() {
        return false;
    }


    public boolean addAllowed(boolean b) {
        return false;
    }


    public boolean canBeEmpty() {
        return true;
    }


    public boolean deleteAllowed() {
        return false;
    }


    public boolean deleteAllowed(boolean b) {
        return false;
    }


    public boolean findContractSafe() {
        return false;
    }


    public boolean iteratorRemoveAllowed() {
        return false;
    }


    public boolean sizeAccurate() {
        return false;
    }


    public boolean handlesLiteralTyping() {
      return false;
    }
}
