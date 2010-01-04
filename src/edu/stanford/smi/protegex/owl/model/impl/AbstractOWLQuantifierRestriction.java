package edu.stanford.smi.protegex.owl.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protegex.owl.model.classparser.compact.OWLCompactParserUtil;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * The common base class of DefaultOWLAllValuesFrom and DefaultOWLSomeValuesFrom.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLQuantifierRestriction extends AbstractOWLRestriction
        implements OWLQuantifierRestriction {


    AbstractOWLQuantifierRestriction(KnowledgeBase kb, FrameID id) {
        super(kb, id);
    }


    AbstractOWLQuantifierRestriction() {
    }


    public void checkFillerText(String text) throws Exception {
        final RDFProperty property = getOnProperty();
        if (property != null) {
            checkFillerText(text, property);
        }
        else {
            checkFillerText(text, property, (OWLModel) getKnowledgeBase());
        }
    }


    public static void checkFillerText(String text, final RDFProperty property) throws OWLClassParseException {
        checkFillerText(text, property, property.getOWLModel());
    }


    public static void checkFillerText(String text, RDFProperty property, OWLModel owlModel) throws OWLClassParseException {
        text = OWLCompactParserUtil.preprocessFiller(owlModel, text);
        OWLClassParser parser = owlModel.getOWLClassDisplay().getParser();
        if (text.startsWith(XSDNames.PREFIX)) {
            if (property instanceof OWLObjectProperty) {
                throw new OWLClassParseException(text + " cannot be applied to object properties");
            }
            parser.checkQuantifierFiller(owlModel, text);
        }
        else {
            if (property instanceof OWLDatatypeProperty) {
                throw new OWLClassParseException(text + " cannot be applied to datatype properties");
            }
            parser.checkClass(owlModel, text);
        }
    }


    public boolean equalsStructurally(RDFObject cls) {
        if (cls instanceof AbstractOWLQuantifierRestriction) {
            AbstractOWLQuantifierRestriction compCls = (AbstractOWLQuantifierRestriction) cls;
            return getOperator() == compCls.getOperator() &&
                    getOnProperty().equalsStructurally(compCls.getOnProperty()) &&
                    getFiller().equalsStructurally(compCls.getFiller());
        }
        return false;
    }

    /*public String getBrowserText() {
       String filler = "?";
       if (isDefined()) {
           RDFResource resource = getFiller();
           if (resource instanceof RDFSClass) {
               RDFSClass fillerClass = (RDFSClass) resource;
               if (fillerClass != null) {
                   filler = fillerClass.getNestedBrowserText();
               }
               else {
                   filler = "<null>";
               }
           }
           else {
               filler = getFillerText();
           }
       }
       return getBrowserTextPropertyName() + " " + getOperator() + " " + filler;
   } */


    public Collection getDependingClasses() {
        if (getFiller() instanceof OWLAnonymousClass) {
            return Collections.singleton(getFiller());
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }


    public RDFResource getFiller() {
        return (RDFResource) getPropertyValue(getFillerProperty());
    }


    public String getFillerText() {
        RDFResource filler = getFiller();
        if (filler != null) {
            return filler.getBrowserText();
        }
        else {
            return "";
        }
    }


    public void getNestedNamedClasses(Set set) {
        if (getFiller() instanceof RDFSClass) {
            ((RDFSClass) getFiller()).getNestedNamedClasses(set);
        }
    }


    public void setFiller(RDFResource filler) {
        setPropertyValue(getFillerProperty(), filler);
    }


    public void setFillerText(String text) throws Exception {
        OWLModel owlModel = getOWLModel();
        OWLClassParser parser = owlModel.getOWLClassDisplay().getParser();
        text = OWLCompactParserUtil.preprocessFiller(owlModel, text);
        if (text.startsWith(XSDNames.PREFIX)) {
            Object valueType = parser.parseQuantifierFiller(owlModel, text);
            if (valueType instanceof RDFSDatatype) {
                setFiller((RDFSDatatype) valueType);
            }
            else {
                OWLDataRange dataRange = getOWLModel().createOWLDataRange((RDFSLiteral[]) valueType);
                setFiller(dataRange);
            }
        }
        else {
            RDFSClass filler = parser.parseClass(owlModel, text);
            setFiller(filler);
        }
    }

    /*static void throwParseException(String text) throws ParseException {
       ParseException ex = new ParseException();
       ex.currentToken = new Token();
       ex.currentToken.image = text;
       OWLCompactParser.errorMessage = "Unknown datatype";
       throw ex;
   } */
}
