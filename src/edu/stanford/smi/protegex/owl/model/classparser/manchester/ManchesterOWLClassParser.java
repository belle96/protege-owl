package edu.stanford.smi.protegex.owl.model.classparser.manchester;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 5, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ManchesterOWLClassParser implements OWLClassParser {

    public void checkClass(OWLModel owlModel,
                           String expression)
            throws OWLClassParseException {
        try {
            ManchesterOWLParser.checkClass(owlModel, expression);
        }
        catch (ParseException e) {
            throw wrapException(e);
        }
    }


    public void checkHasValueFiller(OWLModel owlModel,
                                    String expression)
            throws OWLClassParseException {
        try {
            ManchesterOWLParser.parseHasValueFiller(owlModel, expression);
        }
        catch (ParseException e) {
            throw wrapException(e);
        }
    }


    public void checkQuantifierFiller(OWLModel owlModel,
                                      String expression)
            throws OWLClassParseException {
        try {
            ManchesterOWLParser.parseQuantifierFiller(owlModel, expression);
        }
        catch (ParseException e) {
            throw wrapException(e);
        }
    }


    public RDFSClass parseClass(OWLModel owlModel,
                                String expression)
            throws OWLClassParseException {
        try {
            return ManchesterOWLParser.parseClass(owlModel, expression);
        }
        catch (ParseException e) {
            throw wrapException(e);
        }
    }


    public Object parseHasValueFiller(OWLModel owlModel,
                                      String expression)
            throws OWLClassParseException {
        try {
            return ManchesterOWLParser.parseHasValueFiller(owlModel, expression);
        }
        catch (ParseException e) {
            throw wrapException(e);
        }
    }


    public RDFResource parseQuantifierFiller(OWLModel owlModel,
                                             String expression)
            throws OWLClassParseException {
        try {
            return (RDFResource) ManchesterOWLParser.parseQuantifierFiller(owlModel, expression);
        }
        catch (ParseException e) {
            throw wrapException(e);
        }
    }


    private OWLClassParseException wrapException(ParseException ex) {
        OWLClassParseException e = new OWLClassParseException(ManchesterOWLParser.errorMessage);
        e.currentToken = ex.currentToken == null ? null : ex.currentToken.image;
        e.nextCouldBeClass = ManchesterOWLParser.nextCouldBeCls;
        e.nextCouldBeIndividual = ManchesterOWLParser.nextCouldBeInstance;
        e.nextCouldBeProperty = ManchesterOWLParser.nextCouldBeSlot;
        e.recentHasValueProperty = ManchesterOWLParser.recentHasValueProperty;
	    e.nextCouldBeDatatypeName = ManchesterOWLParser.nextCouldBeDatatypeName;
        return e;
    }
}
