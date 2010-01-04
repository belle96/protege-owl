package edu.stanford.smi.protegex.owl.ui.clsdesc.manchester;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class SuggestionFactory {

    public static Collection getSuggestions(OWLModel model, OWLClassParseException e) {
        ArrayList suggestions = new ArrayList();
        if (e.nextCouldBeClass) {
            suggestions.add(new CreateClassSuggestion(model, getLastToken(e)));
            suggestions.add(new CreateSubClassSuggestion(model, getLastToken(e)));
        }
        if (e.nextCouldBeProperty) {
            suggestions.add(new CreateObjectPropertySuggestion(model, getLastToken(e)));
        }
        if (e.nextCouldBeIndividual) {
            suggestions.add(new CreateIndividualSuggestion(model, getLastToken(e)));
        }

        return suggestions;
    }


    private static String getLastToken(OWLClassParseException e) {
        return e.currentToken;
    }
}

