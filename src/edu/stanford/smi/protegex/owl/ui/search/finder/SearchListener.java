package edu.stanford.smi.protegex.owl.ui.search.finder;

/**
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         17-Mar-2006
 */
public interface SearchListener {

    void searchStartedEvent(Find source);

    void resultsUpdatedEvent(int numResults, Find source);

    void searchCompleteEvent(int numResults, Find source);

    void searchCancelledEvent(Find source);

    void searchEvent(Find source);
}
