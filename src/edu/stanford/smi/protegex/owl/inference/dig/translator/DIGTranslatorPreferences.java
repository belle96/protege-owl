package edu.stanford.smi.protegex.owl.inference.dig.translator;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Oct 24, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DIGTranslatorPreferences {

	private static DIGTranslatorPreferences instance;

	private boolean ignoreNecessaryConditionsOnDefinedClasses = false;


	private DIGTranslatorPreferences() {

	}

	public static synchronized DIGTranslatorPreferences getInstance() {
		if(instance == null) {
			instance = new DIGTranslatorPreferences();
		}
		return instance;
	}


	public boolean isIgnoreNecessaryConditionsOnDefinedClasses() {
		return ignoreNecessaryConditionsOnDefinedClasses;
	}


	public void setIgnoreNecessaryConditionsOnDefinedClasses(boolean b) {
		this.ignoreNecessaryConditionsOnDefinedClasses = b;
	}
}

