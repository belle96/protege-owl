/**
 *
 */
package edu.stanford.smi.protegex.owl.swrl.model.factory;

import edu.stanford.smi.protege.model.SimpleInstance;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLAtomList;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLBuiltin;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLBuiltinAtom;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLClassAtom;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLDataRangeAtom;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLDatavaluedPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLDifferentIndividualsAtom;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLImp;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLIndividualPropertyAtom;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLSameIndividualAtom;
import edu.stanford.smi.protegex.owl.swrl.model.impl.DefaultSWRLVariable;


public enum SWRLFrameTypeId {
	/*
	 * Don't change the order!
	 */
	SWRL_ATOM_LIST(SWRLNames.Cls.ATOM_LIST, DefaultSWRLAtomList.class),
    SWRL_BUILTIN_ATOM(SWRLNames.Cls.BUILTIN_ATOM, DefaultSWRLBuiltinAtom.class),
    SWRL_CLASS_ATOM(SWRLNames.Cls.CLASS_ATOM, DefaultSWRLClassAtom.class),
    SWRL_DATARANGE_ATOM(SWRLNames.Cls.DATA_RANGE_ATOM, DefaultSWRLDataRangeAtom.class),
    SWRL_DATAVALUED_PROPERTY_ATOM(SWRLNames.Cls.DATAVALUED_PROPERTY_ATOM, DefaultSWRLDatavaluedPropertyAtom.class),
    SWRL_INDIVIDUAL_PROPERTY_ATOM(SWRLNames.Cls.INDIVIDUAL_PROPERTY_ATOM, DefaultSWRLIndividualPropertyAtom.class),
    SWRL_DIFFERENT_INDIVIDUALS_ATOM(SWRLNames.Cls.DIFFERENT_INDIVIDUALS_ATOM, DefaultSWRLDifferentIndividualsAtom.class),
    SWRL_SAME_INDIVIDUAL_ATOM(SWRLNames.Cls.SAME_INDIVIDUAL_ATOM, DefaultSWRLSameIndividualAtom.class),
    SWRL_BUILTIN(SWRLNames.Cls.BUILTIN, DefaultSWRLBuiltin.class),
    SWRL_IMP(SWRLNames.Cls.IMP, DefaultSWRLImp.class),
    SWRL_VARIABLE(SWRLNames.Cls.VARIABLE, DefaultSWRLVariable.class);


	public final static int SWRL_FRAME_TYPE_ID_BASE = 42; // leave some room for expansion of the owl java class ids.
    public final static int SWRL_FRAME_TYPE_ID_END  = SWRL_FRAME_TYPE_ID_BASE + SWRLFrameTypeId.values().length - 1;

    private String protegeName;
    private Class<? extends SimpleInstance> javaClass;


    private SWRLFrameTypeId(String protegeName, Class<? extends SimpleInstance> javaClass) {
    	this.protegeName = protegeName;
        this.javaClass = javaClass;
    }

    public int getFrameTypeId() {
        return SWRL_FRAME_TYPE_ID_BASE + ordinal();
    }

    public String getProtegeName() {
    	return protegeName;
    }

    public Class<? extends SimpleInstance> getJavaClass() {
        return javaClass;
    }
}