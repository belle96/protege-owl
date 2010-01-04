package edu.stanford.smi.protegex.owl.inference.dig.translator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 9, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 * <p/>
 * The factory responsible for creating
 * instances of DIGTranslator.
 */
public class DIGTranslatorFactory {

    private static DIGTranslatorFactory instance;


    protected DIGTranslatorFactory() {

    }


    /**
     * Gets the one and only instance of the factory.
     */
    public synchronized static DIGTranslatorFactory getInstance() {
        if (instance == null) {
            instance = new DIGTranslatorFactory();
        }

        return instance;
    }


    /**
     * Creates an instance of an implementation
     * of DIGTranslator
     */
    public DIGTranslator createTranslator() {
        return new DefaultDIGTranslator();
    }
}

