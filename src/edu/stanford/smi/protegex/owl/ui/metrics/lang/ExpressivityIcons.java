package edu.stanford.smi.protegex.owl.ui.metrics.lang;

import edu.stanford.smi.protegex.owl.model.util.DLExpressivityChecker;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 22, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ExpressivityIcons {


	private static final String EXTENSION = ".png";

	private static  HashMap map;

	static {
		map = new HashMap();
		map.put(DLExpressivityChecker.FL0, loadIcon("FLO"));
		map.put(DLExpressivityChecker.FL_MINUS, loadIcon( "FLM"));
		map.put(DLExpressivityChecker.AL, loadIcon( "AL"));
		map.put(DLExpressivityChecker.C, loadIcon( "C"));
		map.put(DLExpressivityChecker.U, loadIcon( "U"));
		map.put(DLExpressivityChecker.E, loadIcon( "E"));
		map.put(DLExpressivityChecker.N, loadIcon( "N"));
		map.put(DLExpressivityChecker.Q, loadIcon( "Q"));
		map.put(DLExpressivityChecker.H, loadIcon( "H"));
		map.put(DLExpressivityChecker.I, loadIcon( "I"));
		map.put(DLExpressivityChecker.O, loadIcon( "O"));
		map.put(DLExpressivityChecker.F, loadIcon( "F"));
		map.put(DLExpressivityChecker.S, loadIcon( "S"));
		map.put(DLExpressivityChecker.DATATYPE, loadIcon( "Datatype"));
	}

	public static ImageIcon getIcon(String letters) {
		return (ImageIcon) map.get(letters);
	}

	private static ImageIcon loadIcon(String name) {
		return new ImageIcon(getURL(name));
	}

	private static URL getURL(String iconName) {
		return ExpressivityIcons.class.getResource("icons/" + iconName + EXTENSION);
	}

	public static void main(String [] args) {
		ArrayList features = new ArrayList();
		features.add(DLExpressivityChecker.S);
		features.add(DLExpressivityChecker.H);
		features.add(DLExpressivityChecker.O);
		features.add(DLExpressivityChecker.I);
		features.add(DLExpressivityChecker.N);
		features.add(DLExpressivityChecker.DATATYPE);
		JPanel panel = new DLNamePanel(features);
		JFrame f = new JFrame();
		f.setContentPane(panel);
		f.show();
	}

}

