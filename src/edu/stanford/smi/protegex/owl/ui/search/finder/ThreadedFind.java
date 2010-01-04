package edu.stanford.smi.protegex.owl.ui.search.finder;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.smi.protege.util.ExclusiveRunnable;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         17-Mar-2006
 */
public class ThreadedFind extends BasicFind {
    private static Logger log = Log.getLogger(ThreadedFind.class);

    private Thread searchThread;
    private DoFind currentfind;

    public ThreadedFind(OWLModel owlModel, int type) {
        super(owlModel, type);
        currentfind = new DoFind();
    }

    /**
     * Starts the search.
     * 
     * Note that this routine  will wait for any previous searches to complete.
     * 
     * @param s The string to search for.
     * @param type The type of search.
     * 
     */
    public void startSearch(String s, int type) {
      synchronized (this) {
        currentfind.setString(s);
        currentfind.setType(type);

        searchThread = new Thread(currentfind);

        searchThread.start();
      }      
    }
    
    private void startSuperSearch(String s, int type) {
      super.startSearch(s, type);
    }

    public void cancelSearch() {
      if (log.isLoggable(Level.FINE)) {
          log.fine("Cancelling search [" + Thread.currentThread().getName() + "]");
      }
      super.cancelSearch();
      if (currentfind != null) {
        currentfind.abort();
      }
    }
    
    class DoFind extends ExclusiveRunnable {
      private String string;
      private int searchType;
      
      public void setString(String string) {
        this.string = string;
      }
      
      public void setType(int searchType) {
        this.searchType = searchType; 
      }
      
      public void execute() {
        startSuperSearch(string, searchType);
      }
    }
}
