package edu.stanford.smi.protegex.owl.model.visitor;

import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class OWLModelVisitorHelper {


    private OWLModel model;

    private OWLModelVisitor visitor;


    /**
     * Constructs a visitor helper, whose methods will visit the specified
     * <code>OWLModel</code> with the specified <code>OWLModelVisitor</code>
     *
     * @param model   The model whose elements will be visited by the methods
     *                on this helper.
     * @param visitor The visitor that will visit the elements.
     */
    public OWLModelVisitorHelper(OWLModel model, OWLModelVisitor visitor) {
        this.model = model;
        this.visitor = visitor;
    }


    public void visitUserDefinedOWLNamedClasses() {
        VisitorUtil.visitRDFResources(model.getUserDefinedOWLNamedClasses(), visitor);
    }


    public void visitUserDefinedOWLProperties() {
        VisitorUtil.visitRDFResources(model.getUserDefinedOWLProperties(), visitor);
    }


    public void visitUserDefinedRDFProperties() {
        VisitorUtil.visitRDFResources(model.getUserDefinedRDFProperties(), visitor);
    }


    public void visitUserDefinedRDFSNamedClasses() {
        VisitorUtil.visitRDFResources(model.getUserDefinedRDFSNamedClasses(), visitor);
    }
}

