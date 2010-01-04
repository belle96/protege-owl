package edu.stanford.smi.protegex.owl.javacode;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFIndividual;

public abstract class AbstractCodeGeneratorIndividual extends DefaultRDFIndividual {
    private static final long serialVersionUID = -406831192956223749L;

    public AbstractCodeGeneratorIndividual() {
        
    }
    
    public AbstractCodeGeneratorIndividual(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }
    
    // Can't java 5 here because the super class does not
    @SuppressWarnings("unchecked")
    @Override
    public RDFResource as(Class javaInterface) {
        return ProtegeJavaMapping.as(this, javaInterface);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean canAs(Class javaInterface) {
        return ProtegeJavaMapping.canAs(this, javaInterface);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection getPropertyValuesAs(RDFProperty property,
                                           Class javaInterface) {
        Collection results = new ArrayList();
        for (Iterator it = getPropertyValues(property).iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof RDFIndividual) {
                RDFIndividual  resource = (RDFIndividual) o;
                if (ProtegeJavaMapping.canAs(resource, javaInterface)) {
                    results.add(ProtegeJavaMapping.as(resource, javaInterface));
                }
                else {
                    results.add(resource);
                }
            }
            else {
                results.add(o);
            }
        }
        return results;
    }
    
    @Override
    public Iterator listPropertyValuesAs(RDFProperty property, Class javaInterface) {
        return getPropertyValuesAs(property, javaInterface).iterator();
    }
    
}
