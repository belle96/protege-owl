package edu.stanford.smi.protegex.owl.ui.clsproperties;

import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.profiles.OWLProfiles;
import edu.stanford.smi.protegex.owl.ui.profiles.ProfilesManager;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CreatePropertyAction extends AbstractAction {

    private String baseName;

    private CallBack callBack;

    private RDFSNamedClass type;


    public CreatePropertyAction(RDFSNamedClass type,
                                String baseName,
                                String baseIconName,
                                CallBack callBack) {
        super("Create " + type.getName() + "...", OWLIcons.getCreateIcon(baseIconName));
        this.baseName = baseName;
        this.callBack = callBack;
        this.type = type;
    }


    public void actionPerformed(ActionEvent e) {
        OWLModel owlModel = type.getOWLModel();
        String name = type.getOWLModel().createNewResourceName(baseName);
        try {
            owlModel.beginTransaction("Create new " + type.getBrowserText(), name);            
            RDFProperty property = (RDFProperty) type.createInstance(name);
            callBack.propertyCreated(property);
            property.getProject().show(property);
            owlModel.commitTransaction();			
		} catch (Exception ex) {
			owlModel.rollbackTransaction();
			OWLUI.handleError(owlModel, ex);
		}
    }


    public static void addActions(LabeledComponent lc, OWLModel owlModel, CallBack callBack) {
        if (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.Create_ObjectProperty)) {
            lc.addHeaderButton(new CreatePropertyAction(owlModel.getOWLObjectPropertyClass(),
                    "objectProperty", OWLIcons.OWL_OBJECT_PROPERTY, callBack));
        }
        if (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.Create_DatatypeProperty)) {
            lc.addHeaderButton(new CreatePropertyAction(owlModel.getOWLDatatypePropertyClass(),
                    "datatypeProperty", OWLIcons.OWL_DATATYPE_PROPERTY, callBack));
        }
        if (ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.RDF) ||
                ProfilesManager.isFeatureSupported(owlModel, OWLProfiles.RDF_but_not_OWL)) {
            lc.addHeaderButton(new CreatePropertyAction(owlModel.getRDFPropertyClass(),
                    "property", OWLIcons.RDF_PROPERTY, callBack));
        }
    }


    public static interface CallBack {

        void propertyCreated(RDFProperty property);
    }
}
