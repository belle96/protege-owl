package edu.stanford.smi.protegex.owl.inference.dig.reasoner.logger;

import edu.stanford.smi.protegex.owl.inference.dig.exception.DIGError;
import edu.stanford.smi.protegex.owl.inference.dig.reasoner.DIGReasoner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Feb 15, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DIGLogger {

    private static Map loggerMap;


    static {
        loggerMap = new WeakHashMap();
    }


    private ArrayList listeners;


    private DIGLogger() {
        listeners = new ArrayList();
    }


    public static DIGLogger getInstance(DIGReasoner digReasoner) {
        DIGLogger instance = (DIGLogger) loggerMap.get(digReasoner);
        if (instance == null) {
            instance = new DIGLogger();
            loggerMap.put(digReasoner, instance);
        }
        return instance;
    }


    public void logError(DIGError error) {
        for (Iterator it = new ArrayList(listeners).iterator(); it.hasNext();) {
            WeakReference ref = (WeakReference) it.next();
            if (ref.get() == null) {
                it.remove();
            }
            else {
                ((DIGLoggerListener) it.next()).errorLogged(error);
            }
        }
    }


    public void addListener(DIGLoggerListener listener) {
        listeners.add(new WeakReference(listener));
    }


    public void removeListener(DIGLoggerListener listener) {
        for (Iterator it = listeners.iterator(); it.hasNext();) {
            WeakReference ref = (WeakReference) it.next();
            if (ref.get() == null) {
                it.remove();
            }
            else if (ref.get() == listener) {
                it.remove();
            }
        }
    }


}

