package edu.stanford.smi.protegex.owl.ui.search.finder;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         21-Mar-2006
 */
public abstract class SearchAdapter implements SearchListener {

    public void searchStartedEvent(Find source) {
    }

    public void resultsUpdatedEvent(int numResults, Find source) {
    }

    public void searchCompleteEvent(int numResults, Find source) {
    }

    public void searchCancelledEvent(Find source) {
    }

    public void searchEvent(Find source) {
    }
}
