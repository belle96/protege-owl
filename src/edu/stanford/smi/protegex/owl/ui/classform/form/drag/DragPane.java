package edu.stanford.smi.protegex.owl.ui.classform.form.drag;

import javax.swing.*;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 9, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DragPane extends JPanel {

    // Commented out by Holger until Matt commits the missing DragClient class
/*
	private JFrame frame;

	private JComponent dragSource;

	private DragClient client;

	private JComponent component;

	private Rectangle dropRectangle;

	private boolean active;

	private int paintX;

	private int paintY;

	private int paintWidth;

	private int paintHeight;

	private Point lastMousePoint;

	private Stroke dragStroke;

	private Collection components;

	private ComponentListener componentListener;

	private Stroke dropRectangleStroke;


	private MouseListener mouseListener;

	private MouseMotionListener mouseMotionListener;

	public DragPane() {
		mouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				component = (JComponent) e.getComponent();
				Point p = SwingUtilities.convertPoint(component, e.getX(), e.getY(), DragPane.this);
				paintX = p.x;
				paintY = p.y;
				paintWidth = component.getWidth();
				paintHeight = component.getHeight();
				setVisible(true);
			}

			public void mouseReleased(MouseEvent e) {
				setVisible(false);
			}
		};
		mouseMotionListener = new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setVisible(true);
				if(component != null) {
					int dX = e.getX() - lastMousePoint.x;
					int dY = e.getY() - lastMousePoint.y;
					paintX = paintX += dX;
					paintY = paintY += dY;
					repaint();
					lastMousePoint = e.getPoint();
				}
			}
		};

			dragStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			dropRectangleStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		addMouseMotionListener(mouseMotionListener);
		setOpaque(false);
	}

	public void install(DragClient client) {
		if(this.client != null) {
			uninstall(this.client);
		}
		this.client = client;
		components = new ArrayList(client.getComponents());
		for(Iterator it = components.iterator(); it.hasNext(); ) {
			JComponent curComp = (JComponent) it.next();
			curComp.addMouseListener(mouseListener);
		}
		init();
	}

	public void uninstall(DragClient client) {
		for(Iterator it = components.iterator(); it.hasNext();) {
			JComponent curComp = (JComponent) it.next();
			curComp.removeMouseListener(mouseListener);
		}
	}

	private void init() {
		frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, dragSource);
		if(frame != null) {
			frame.setGlassPane(this);
		}
	}




	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

	public Dimension getPreferredSize() {
		return frame.getContentPane().getSize();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(3, 3, getWidth() - 6, getHeight() - 6);
		if(dropRectangle != null) {
			Color color = g2.getColor();
			Stroke oldStroke = g2.getStroke();
			g2.setColor(Color.MAGENTA);
			g2.setStroke(dragStroke);
			g2.drawRect(paintX, paintY, paintWidth, paintHeight);
			g2.drawRect(dropRectangle.x, dropRectangle.y, dropRectangle.width, dropRectangle.height);
			g2.setColor(color);
			g2.setStroke(oldStroke);
		}
		else {
			g.drawRect(paintX, paintY, paintWidth, paintHeight);

		}
		AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.2f);
		g2.setComposite(alphaComposite);
		g2.translate(paintX, paintY);
		component.paint(g2);
		g2.translate(-paintX, -paintY);
	}
*/

}

