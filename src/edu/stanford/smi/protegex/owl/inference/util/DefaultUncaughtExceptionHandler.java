package edu.stanford.smi.protegex.owl.inference.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.inference.protegeowl.ReasonerManager;

public class DefaultUncaughtExceptionHandler implements UncaughtExceptionHandler{
  	
	public void uncaughtException(Thread t, Throwable e) {
		if (e instanceof OutOfMemoryError) {				
			ReasonerManager.getInstance().dispose();
			
			System.gc();
			System.runFinalization();
			System.gc();
			
			Log.getLogger().log(Level.SEVERE, "OutOfMemory caught. Trying to recover. Thread: " + t + " Free memory: " + Runtime.getRuntime().freeMemory());				
		} else {
			Log.getLogger().log(Level.WARNING, "Exception caught by default exception handler", e);
		}
	}    	
}