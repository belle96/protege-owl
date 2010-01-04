package edu.stanford.smi.protegex.owl.ui.dialogs.test;

import java.util.Collection;
import java.util.Collections;

import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;
import edu.stanford.smi.protegex.owl.ui.dialogs.SelectClassPanel;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         19-Jan-2006
 */
public class SelectClassPanelTestCase extends AbstractJenaTestCase{

    public void testWithOwlThingRoot(){

        createTestOntology();

        Collection rootClses = Collections.singleton(owlModel.getOWLThingClass());
        SelectClassPanel panel = new SelectClassPanel(owlModel, rootClses,
                                                     false, true);
        Collection clses = Collections.EMPTY_LIST;
        //TT: Test disabled because it required user interaction. Should be fixed later to have a different thread that clicks on the
        // OK button of the dialog.
//      int result = ProtegeUI.getModalDialogFactory().showDialog(null, panel, "Test select classes", ModalDialogFactory.MODE_OK_CANCEL);        
// 		if (result == ModalDialogFactory.OPTION_OK) {
//	   		clses = panel.getSelection();
//		}        
//		Log.getLogger().info("selected = " + clses);
        ComponentUtilities.dispose(panel);
        
    }

    public void testWithARoot(){

        createTestOntology();

        Collection rootClses = Collections.singleton(owlModel.getOWLNamedClass("A"));
        SelectClassPanel panel = new SelectClassPanel(owlModel, rootClses,
                                                      false, true);

        Collection clses = Collections.EMPTY_LIST;
        //TT: Test disabled because it required user interaction. Should be fixed later to have a different thread that clicks on the
        // OK button of the dialog.
//      int result = ProtegeUI.getModalDialogFactory().showDialog(null, panel, "Test select classes", ModalDialogFactory.MODE_OK_CANCEL);        
// 		if (result == ModalDialogFactory.OPTION_OK) {
//	   		clses = panel.getSelection();
//		}        
        ComponentUtilities.dispose(panel);

    }

    private void createTestOntology(){
        OWLNamedClass a = owlModel.createOWLNamedClass("A");
        RDFSNamedClass b = owlModel.createSubclass("B", a);
        RDFSNamedClass c = owlModel.createSubclass("C", a);
        RDFSNamedClass d = owlModel.createSubclass("D", a);
        RDFSNamedClass x = owlModel.createSubclass("X", b);
        RDFSNamedClass y = owlModel.createSubclass("Y", b);
        RDFSNamedClass z = owlModel.createSubclass("Z", b);
    }
    
  
    
}
