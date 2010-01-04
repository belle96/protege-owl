package edu.stanford.smi.protegex.owl.server.metaproject;

import edu.stanford.smi.protege.server.metaproject.Operation;
import edu.stanford.smi.protege.server.metaproject.impl.UnbackedOperationImpl;

public class OwlMetaProjectConstants {
    public final static Operation SET_ACTIVE_IMPORT   = new  UnbackedOperationImpl("SetActiveImport", null);
    public final static Operation USE_OWL_CLASSES_TAB = new UnbackedOperationImpl("UseOwlClassesTab", null);
    public final static Operation USE_PROPERTY_TAB    = new UnbackedOperationImpl("UsePropertiesTab", null);
}
