package edu.stanford.smi.protegex.owl.model.classparser.compact;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CompactOWLClassParser implements OWLClassParser {

    public void checkClass(OWLModel owlModel, String expression) throws OWLClassParseException {
        try {
            CompactParser.checkClass(owlModel, expression);
        }
        catch (ParseException ex) {
            throw wrapException(ex);
        }
    }


    public void checkHasValueFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        try {
            CompactParser.checkHasValueFiller(owlModel, expression);
        }
        catch (ParseException ex) {
            throw wrapException(ex);
        }
    }


    public void checkQuantifierFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        try {
            CompactParser.checkQuantifierFiller(owlModel, expression);
        }
        catch (ParseException ex) {
            throw wrapException(ex);
        }
    }


    public RDFSClass parseClass(OWLModel owlModel, String expression) throws OWLClassParseException {
        try {
            return CompactParser.parseClass(owlModel, expression);
        }
        catch (ParseException ex) {
            throw wrapException(ex);
        }
    }


    public Object parseHasValueFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        try {
            return CompactParser.parseHasValueFiller(owlModel, expression);
        }
        catch (ParseException ex) {
            throw wrapException(ex);
        }
    }


    public RDFResource parseQuantifierFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        try {
            return (RDFResource) CompactParser.parseQuantifierFiller(owlModel, expression);
        }
        catch (ParseException ex) {
            throw wrapException(ex);
        }
    }


    private OWLClassParseException wrapException(ParseException ex) {
        OWLClassParseException e = new OWLClassParseException(CompactParser.errorMessage);
        e.currentToken = ex.currentToken == null ? null : ex.currentToken.image;
        e.nextCouldBeClass = CompactParser.nextCouldBeCls;
        e.nextCouldBeIndividual = CompactParser.nextCouldBeInstance;
        e.nextCouldBeProperty = CompactParser.nextCouldBeSlot;
        e.recentHasValueProperty = CompactParser.recentHasValueProperty;
	    e.nextCouldBeDatatypeName = CompactParser.nextCouldBeDatatypeName;
        return e;
    }
}
