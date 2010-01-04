package edu.stanford.smi.protegex.owl.ui.metrics.lang;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLNamePanel extends JPanel {

	private List icons;

	private int maxHeight = 0;

	private int maxWidth = 0;

	private Dimension prefSize;

	private static final int TRACKING_ADJUSTMENT = -7;

	public DLNamePanel(List langFeatures) {
		icons = new ArrayList();
		for(Iterator it = langFeatures.iterator(); it.hasNext(); ) {
			ImageIcon curIcon = ExpressivityIcons.getIcon((String) it.next());
			if(curIcon != null) {
				icons.add(curIcon);
				if(curIcon.getIconHeight() > maxHeight) {
					maxHeight = curIcon.getIconHeight();
				}
				maxWidth += curIcon.getIconWidth() + TRACKING_ADJUSTMENT;
			}
		}
		prefSize = new Dimension(maxWidth, maxHeight);
	}


	public Dimension getPreferredSize() {
		return prefSize;
	}


	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int x = 0;
		for(Iterator it = icons.iterator(); it.hasNext(); ) {
			ImageIcon curIcon = (ImageIcon) it.next();
			int y = maxHeight - curIcon.getIconHeight();
			g2.drawImage(curIcon.getImage(), x, y, null);
			x += curIcon.getIconWidth() + TRACKING_ADJUSTMENT;
		}

	}
}

