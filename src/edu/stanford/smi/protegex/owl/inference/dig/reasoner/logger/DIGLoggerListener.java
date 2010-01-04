package edu.stanford.smi.protegex.owl.inference.dig.reasoner.logger;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGError;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Feb 15, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface DIGLoggerListener {

    public void errorLogged(DIGError error);
}

