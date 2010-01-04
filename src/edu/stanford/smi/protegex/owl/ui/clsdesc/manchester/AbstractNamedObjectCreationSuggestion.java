package edu.stanford.smi.protegex.owl.ui.clsdesc.manchester;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class AbstractNamedObjectCreationSuggestion implements Suggestion {

    private OWLModel model;

    private String name;


    public AbstractNamedObjectCreationSuggestion(OWLModel model, String name) {
        this.model = model;
        this.name = name;
    }


    public OWLModel getModel() {
        return model;
    }


    public String getName() {
        return name;
    }


    public void performSuggestion() {
        if (model.getRDFResource(name) == null) {
            createObject();
        }
    }


    public abstract void createObject();


    public Icon getIcon() {
        return OWLIcons.getDownIcon();
    }


    public String getDescription() {
        return "Create " + getName() + " " + getObjectType();
    }


    public abstract String getObjectType();
}

