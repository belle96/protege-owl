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
public class CreateClassSuggestion extends AbstractNamedObjectCreationSuggestion {

    public CreateClassSuggestion(OWLModel model, String name) {
        super(model, name);
    }


    public void createObject() {
        getModel().createOWLNamedClass(getName());
    }


    public String getObjectType() {
        return "class";
    }


    public Icon getIcon() {
        return OWLIcons.getClassesIcon();
    }
}

