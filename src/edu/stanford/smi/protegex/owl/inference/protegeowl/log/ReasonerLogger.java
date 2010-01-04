package edu.stanford.smi.protegex.owl.inference.protegeowl.log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Aug 24, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ReasonerLogger {

    private static ReasonerLogger instance;

    private ArrayList listeners;


    protected ReasonerLogger() {
        listeners = new ArrayList();
    }


    public static synchronized ReasonerLogger getInstance() {
        if (instance == null) {
            instance = new ReasonerLogger();
        }

        return instance;
    }


    public void postLogRecord(ReasonerLogRecord logRecord) {
        fireLogRecordPosted(logRecord);
    }


    public void addListener(ReasonerLoggerListener lsnr) {
        listeners.add(lsnr);
    }


    public void removeListener(ReasonerLoggerListener lsnr) {
        listeners.remove(lsnr);
    }


    protected void fireLogRecordPosted(ReasonerLogRecord logRecord) {
        Iterator it = new ArrayList(listeners).iterator();

        while (it.hasNext()) {
            final ReasonerLoggerListener curLsnr = (ReasonerLoggerListener) it.next();

            curLsnr.logRecordPosted(logRecord);
        }
    }


}

