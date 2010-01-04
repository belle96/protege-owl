package edu.stanford.smi.protegex.owl.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.resource.ResourceKey;
import edu.stanford.smi.protege.ui.BrowserTextListFinder;
import edu.stanford.smi.protege.ui.Finder;
import edu.stanford.smi.protege.ui.FrameWithBrowserTextRenderer;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.FrameWithBrowserText;
import edu.stanford.smi.protege.util.FrameWithBrowserTextComparator;
import edu.stanford.smi.protege.util.GetInstancesAndBrowserTextJob;
import edu.stanford.smi.protege.util.StringUtilities;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class SelectResourcesWithBrowserTextPanel extends SelectResourcesPanel {

	private static final long serialVersionUID = -844734066823481870L;
	
	public SelectResourcesWithBrowserTextPanel(OWLModel owlModel,
			Collection classes, boolean allowsMultipleSelection) {
		super(owlModel, classes, allowsMultipleSelection);	
	}

	@Override
	protected JComboBox createDirectAllInstanceComboBox() {	
		return super.createDirectAllInstanceComboBox();
	}
	
	@Override
	protected JComponent createInstanceList() {	
		JComponent instList = super.createInstanceList();
		((JList)instList).setCellRenderer(new FrameWithBrowserTextRenderer() {
			@Override
			public void setMainText(String text) {			
				super.setMainText(StringUtilities.unquote(text));
			}
		});
		return instList;
	}
	
	@Override
	protected Finder createListFinder() {	
		return new BrowserTextListFinder(_instanceList, ResourceKey.INSTANCE_SEARCH_FOR);
	}
	
	@Override
	protected Collection getInstances(Cls cls, boolean direct) {
		GetInstancesAndBrowserTextJob job = new GetInstancesAndBrowserTextJob(cls.getKnowledgeBase(),
				CollectionUtilities.createCollection(cls), direct);
		return job.execute();
	}
	
	
	@Override
	protected Comparator getInstancesComparator() {
		return new FrameWithBrowserTextComparator();
	}

	@Override
	protected Collection getInstanceSelection() {
		List<Frame> selectedFrames = new ArrayList<Frame>();
		Collection sel = super.getInstanceSelection();
		for (Iterator iterator = sel.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if (object instanceof FrameWithBrowserText) {
				selectedFrames.add(((FrameWithBrowserText)object).getFrame());
			}
		}
		return selectedFrames;
	}
	
}
