package edu.stanford.smi.protegex.owl.swrl.ui.actions;

import java.util.logging.Level;

import edu.stanford.smi.protege.model.WidgetDescriptor;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.ModalDialog;
import edu.stanford.smi.protege.widget.TabWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.swrl.ui.tab.SWRLTab;
import edu.stanford.smi.protegex.owl.ui.actions.AbstractOWLModelAction;
import edu.stanford.smi.protegex.owl.ui.actions.OWLModelActionConstants;

public class EnableSWRLTabAction extends AbstractOWLModelAction {

    private static final String SWRL_TAB_JAVA_CLASS_NAME = SWRLTab.class.getName();
    
	public final static String GROUP = OWLModelActionConstants.QUERY_GROUP;


    public String getIconFileName() {
        return "SWRLImps";
    }

    @Override
    public Class getIconResourceClass() {    
    	return SWRLIcons.class;
    }

    public String getMenubarPath() {
        return REASONING_MENU + PATH_SEPARATOR + GROUP;
    }


    public String getName() {
        return "Open SWRL Tab";
    }


    public boolean isSuitable(OWLModel owlModel) {
    	return true;
    }
	

	public void run(OWLModel owlModel) {
		try {
			ProjectView prjView = ProjectManager.getProjectManager().getCurrentProjectView();
			TabWidget tabWidget = prjView.getTabByClassName(SWRL_TAB_JAVA_CLASS_NAME);
			
			if (tabWidget != null) {
				prjView.setSelectedTab(tabWidget);
				return;
			}
			
			WidgetDescriptor d = owlModel.getProject().getTabWidgetDescriptor(SWRL_TAB_JAVA_CLASS_NAME);
			d.setVisible(true);
						
			prjView.addTab(d);
			tabWidget = prjView.getTabByClassName(SWRL_TAB_JAVA_CLASS_NAME);
			prjView.setSelectedTab(tabWidget);
			
		} catch (Exception e) {
			Log.getLogger().log(Level.WARNING, "Cannot enable SWRL Tab", e);
			ModalDialog.showMessageDialog(ProjectManager.getProjectManager().getCurrentProjectView(), "Cannot enable SWRLTab. See console for more details.");
		}		
	}

}
