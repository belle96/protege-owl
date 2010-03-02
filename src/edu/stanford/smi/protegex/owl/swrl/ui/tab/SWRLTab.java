
package edu.stanford.smi.protegex.owl.swrl.ui.tab;

import java.awt.BorderLayout;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.logging.Level;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.ui.ProjectManager;
import edu.stanford.smi.protege.ui.ProjectView;
import edu.stanford.smi.protege.util.ApplicationProperties;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.widget.AbstractTabWidget;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.util.ImportHelper;
import edu.stanford.smi.protegex.owl.swrl.model.SWRLNames;
import edu.stanford.smi.protegex.owl.swrl.ui.SWRLProjectPlugin;
import edu.stanford.smi.protegex.owl.swrl.ui.icons.SWRLIcons;
import edu.stanford.smi.protegex.owl.swrl.ui.table.SWRLTablePanel;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;

/**
 * A tab widget that holds the <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLEditorFAQ">SWRL Editor</a> and other plugins that work
 * with SWRL rules. This tab serves as the entry point to all of the GUI-based software components that work with SWRL in Protege-OWL. <p>
 *
 * Full documentation is available <a href="http://protege.cim3.net/cgi-bin/wiki.pl?SWRLTab">here</a>.
 */
public class SWRLTab extends AbstractTabWidget
{
  private SWRLTablePanel panel = null;

  public void initialize() 
  {
  	setLabel("SWRL Rules");
  	setIcon(SWRLIcons.getImpsIcon());

  	activateSWRL();
  	panel = new SWRLTablePanel((OWLModel) getKnowledgeBase(), null, this);
	    	
  	add(panel);
  } 

  private void activateSWRL()
  {
    OWLModel owlModel = (OWLModel) getKnowledgeBase();
  
    try {
      if (owlModel.getProject().isMultiUserClient()) return;
      
      owlModel.getNamespaceManager().setPrefix(new URI(SWRLNames.SWRLA_NAMESPACE), SWRLNames.SWRLA_PREFIX);
      owlModel.getNamespaceManager().setPrefix(new URI(SWRLNames.SQWRL_NAMESPACE), SWRLNames.SQWRL_PREFIX);

      ImportHelper importHelper = new ImportHelper(owlModel);
      boolean importsAdded  = false;

      if (!ApplicationProperties.getBooleanProperty(SWRLNames.EXCLUDE_STANDARD_IMPORTS, false)) {
        importsAdded |= addImport(owlModel, SWRLNames.SWRLA_IMPORT, importHelper);
        importsAdded |= addImport(owlModel, SWRLNames.SQWRL_IMPORT, importHelper);

        importHelper.importOntologies(false);
      } // if

      // Make ":TO" and ":FROM" visible for dynamic expansion.
      owlModel.getSystemFrames().getToSlot().setVisible(true);
      owlModel.getSystemFrames().getFromSlot().setVisible(true);
      SWRLProjectPlugin.setSWRLClassesAndPropertiesVisible(getProject(), false);
      SWRLProjectPlugin.adjustWidgets(getProject());

      if (importsAdded)  {
    	  ProjectView prjView = ProjectManager.getProjectManager().getCurrentProjectView();
    	  if (prjView != null) prjView.reloadAllTabsExcept(this);
      }

    } catch (Exception ex) {
      ProtegeUI.getModalDialogFactory().showErrorMessageDialog(owlModel, "Could not activate SWRLTab: " + ex +
                                                               "\n. Your project might be in an inconsistent state now.");
      Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
    } // try
  } 

  private boolean addImport(OWLModel owlModel, String importUri, ImportHelper importHelper) throws URISyntaxException 
  {
  	if  (owlModel.getTripleStoreModel().getTripleStore(importUri) == null) {
  		importHelper.addImport(new URI(importUri));
  		return true;
   }
  	return false;
  }

  public void reconfigure()
  {
    if (panel != null) {
      remove(panel);
      setLayout(new BorderLayout());
      add(panel);
    } // if
  } // reconfigure


  public static boolean isSuitable(Project p, Collection errors)
  {
    if (p.getKnowledgeBase() instanceof OWLModel) {
      return true;
    } else {
      errors.add("This tab can only be used with OWL projects.");
      return false;
    } // if
  } // isSuitable
} // SWRLTab
