package edu.stanford.smi.protegex.owl.model.classparser.dl;

import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 25, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLSyntaxClassParser implements OWLClassParser {

    public void checkClass(OWLModel owlModel, String expression) throws OWLClassParseException {
        parse(owlModel, expression, false);
    }

    public void checkHasValueFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        parse(owlModel, expression, false);
    }

    public void checkQuantifierFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        parse(owlModel, expression, false);
    }

    public RDFSClass parseClass(OWLModel owlModel, String expression) throws OWLClassParseException {
        return parse(owlModel, expression, true);
    }

    public Object parseHasValueFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        return parse(owlModel, expression, true);
    }

    public RDFResource parseQuantifierFiller(OWLModel owlModel, String expression) throws OWLClassParseException {
        return parse(owlModel, expression, true);
    }

    private OWLClass parse(OWLModel owlModel, String expression, boolean create) throws OWLClassParseException {
        try {
            String parsableExpr = DLSyntaxParserUtil.getParseableString(expression);
            OWLClass cls = DLSyntaxParser.parseExpression(owlModel, parsableExpr, create);
            return cls;
        } catch (ParseException e) {
            throw wrapAndThrowException(e);
        }
    }

    private OWLClassParseException wrapAndThrowException(ParseException e) {
        OWLClassParseException parseException = new OWLClassParseException(e.getMessage());
        parseException.nextCouldBeClass = contains(e.expectedTokenSequences, DLSyntaxParserConstants.CLASS_ID);
        parseException.nextCouldBeProperty = contains(e.expectedTokenSequences, DLSyntaxParserConstants.DATATYPE_PROPERTY_ID) |
                contains(e.expectedTokenSequences, DLSyntaxParserConstants.OBJECT_PROPERTY_ID);
        parseException.nextCouldBeIndividual = contains(e.expectedTokenSequences, DLSyntaxParserConstants.INDIVIDUAL_ID);
        parseException.nextCouldBeDatatypeName = contains(e.expectedTokenSequences, DLSyntaxParserConstants.DATATYPE_ID);
        return parseException;
    }


    private boolean contains(int tokenSequences [] [], int val) {
        for(int i = 0; i < tokenSequences.length; i++) {
            int [] seq = tokenSequences[i];
            for(int j = 0; j < seq.length; j++) {
                if(seq[j] == val) {
                    return true;
                }
            }
        }
        return false;
    }

}
