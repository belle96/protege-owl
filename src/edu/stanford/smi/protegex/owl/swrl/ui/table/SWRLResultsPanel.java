package edu.stanford.smi.protegex.owl.swrl.ui.table;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;
import edu.stanford.smi.protegex.owl.model.event.ModelListener;
import edu.stanford.smi.protegex.owl.ui.results.ResultsPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SWRLResultsPanel extends ResultsPanel 
{
	private RDFResource instance;
	private SWRLTablePanel tablePanel;
	
	private ModelListener listener = new ModelAdapter() 
	{
		public void classDeleted(RDFSClass cls) { if (instance.equals(cls)) { closeSoon(); } }
		public void individualDeleted(RDFResource resource) { if (instance.equals(resource)) { closeSoon(); } }
		public void propertyDeleted(RDFProperty property) { if (instance.equals(property)) { closeSoon(); } }
	};

  public SWRLResultsPanel(RDFResource resource) 
  {
  	super(resource.getOWLModel());
  	this.instance = resource;
  	OWLModel owlModel = resource.getOWLModel();
  	owlModel.addModelListener(listener);
  	tablePanel = new SWRLTablePanel(owlModel, resource);
  	add(BorderLayout.CENTER, tablePanel);
  }

  private void closeSoon() 
  {
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() { close(); }
  	});
  }

  public void dispose() 
  {
  	tablePanel.dispose();
  	OWLModel owlModel = instance.getOWLModel();
  	owlModel.removeModelListener(listener);
  }

  public String getTabName() { return "SWRL Rules about " + instance.getBrowserText(); }
}
