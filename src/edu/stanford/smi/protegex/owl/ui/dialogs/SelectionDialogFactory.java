package edu.stanford.smi.protegex.owl.ui.dialogs;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSDatatype;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

import java.awt.*;
import java.util.Collection;
import java.util.Set;

/**
 * An interface for user interface dialogs to select resources from
 * a list or tree.
 * <p/>
 * An instance of this can be accessed from <CODE>ProtegeUI.getSelectionDialogFactory()</CODE>.
 * <p/>
 * All methods take a Component as first argument which can be left null: if no pointer
 * to an existing component is available then the system will use the ProjectView registered
 * for the OWLModel.  However, it is generally recommended to use a non-null parent.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SelectionDialogFactory {

    // selectClass --------------------------------------------


    RDFSNamedClass selectClass(Component parent, OWLModel owlModel);


    RDFSNamedClass selectClass(Component parent, OWLModel owlModel, String title);


    RDFSNamedClass selectClass(Component parent, OWLModel owlModel, Collection rootClasses);


    RDFSNamedClass selectClass(Component parent, OWLModel owlModel, Collection rootClasses, String title);


    RDFSNamedClass selectClass(Component parent, OWLModel owlModel, RDFSNamedClass rootClass, String title);

    // selectClasses ------------------------------------------


    Set selectClasses(Component parent, OWLModel owlModel, String title);


    Set selectClasses(Component parent, OWLModel owlModel, RDFSNamedClass rootClass, String title);


    Set selectClasses(Component parent, OWLModel owlModel, Collection rootClasses, String title);

    // selectProperty -----------------------------------------


    RDFProperty selectProperty(Component parent, OWLModel owlModel, Collection allowedProperties);


    RDFProperty selectProperty(Component parent, OWLModel owlModel, Collection allowedProperties, String title);

    RDFSDatatype selectDatatype(Component parent, OWLModel owlModel);
    
    // selectResource -----------------------------------------


    RDFResource selectResourceByType(Component parent, OWLModel owlModel, Collection allowedClasses);


    RDFResource selectResourceByType(Component parent, OWLModel owlModel, Collection allowedClasses, String title);


    RDFResource selectResourceFromCollection(Component parent, OWLModel owlModel, Collection resources, String title);

    // selectResources ----------------------------------------


    Set selectResourcesByType(Component parent, OWLModel owlModel, Collection allowedClasses);


    Set selectResourcesByType(Component parent, OWLModel owlModel, Collection allowedClasses, String title);


    Set selectResourcesFromCollection(Component parent, OWLModel owlModel, Collection resources, String title);
}
