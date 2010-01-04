package edu.stanford.smi.protegex.owl.ui;

import java.util.Collection;

import javax.swing.Icon;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.ui.FrameWithBrowserTextRenderer;
import edu.stanford.smi.protege.util.FrameWithBrowserText;
import edu.stanford.smi.protege.util.StringUtilities;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

public class ResourceRendererWithBrowserText extends
		FrameWithBrowserTextRenderer {
		
	private RDFSNamedClass owlDeprecatedClass;
	
	private static final long serialVersionUID = 5885951703279077675L;

	public ResourceRendererWithBrowserText(OWLModel owlModel) {		
		this.owlDeprecatedClass = owlModel.getSystemFrames().getOwlDeprecatedClassClass();
	}
	
	@Override
	public void load(Object value) {	
		super.load(value);
		if (value instanceof FrameWithBrowserText) {			
			if (isDeprecated((FrameWithBrowserText) value)) {
				addIcon(OWLIcons.getDeprecatedIcon());
			}
		}
	}
	
	protected boolean isDeprecated(FrameWithBrowserText fbt) {
		Collection<Cls> directTypes = fbt.getTypes();
		return directTypes != null && owlDeprecatedClass != null && directTypes.contains(owlDeprecatedClass);
	}
	
	@Override
	protected Icon getFbtIcon(FrameWithBrowserText fbt) {
		String iconName = fbt.getIconName();
		Frame frame = fbt.getFrame();
		if (iconName != null) {			
			return (frame.getKnowledgeBase() instanceof OWLModel) ?
					(frame.isEditable() ? OWLIcons.getImageIcon(iconName) : OWLIcons.getReadOnlyClsIcon(iconName)) :
					super.getFbtIcon(fbt);
		} else {
			return null;
		}
	}
	
	public void setMainText(String text) {        	
		super.setMainText(StringUtilities.unquote(text));
	}
}
