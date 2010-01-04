package edu.stanford.smi.protegex.owl.inference.util;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 21, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class TimeDifference {

    private long t0, t1;


    public void markStart() {
        t0 = System.currentTimeMillis();
    }


    public void markEnd() {
        t1 = System.currentTimeMillis();
    }


    public long getDifference() {
        return t1 - t0;
    }


    public String toString() {
        String message;

        double diff = getDifference() / 1000.0;

        if (diff < 0.001) {
            message = "less that 0.001 seconds";
        }
        else {
            message = diff + " seconds";
        }

        return message;
    }

}

