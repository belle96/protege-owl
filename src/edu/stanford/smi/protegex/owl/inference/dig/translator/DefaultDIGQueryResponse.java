package edu.stanford.smi.protegex.owl.inference.dig.translator;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.HashSet;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jul 20, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DefaultDIGQueryResponse implements DIGQueryResponse {

    private Element element;

    private OWLModel kb;


    public DefaultDIGQueryResponse(OWLModel kb) {
        this.kb = kb;
    }


    public void setElement(Element element) {
        this.element = element;
    }


    public String getID() {
        return element.getAttribute("id");
    }


    public Collection getConcepts() {
        NodeList synonymsList = element.getElementsByTagName(DIGVocabulary.Response.SYNONYMS);
        Collection conceptList = new HashSet(synonymsList.getLength());
        for (int i = 0; i < synonymsList.getLength(); i++) {
            final NodeList catomList = ((Element) synonymsList.item(i)).getElementsByTagName(DIGVocabulary.Language.CATOM);
            for (int j = 0; j < catomList.getLength(); j++) {
                String name = ((Element) catomList.item(j)).getAttribute("name");
                final RDFResource aClass = kb.getRDFResource(name);
                if (aClass != null) {
                	if (aClass instanceof OWLNamedClass) {
                		conceptList.add(aClass);
                	} else {
                		Log.getLogger().warning("Error at getting inferred type from DIG Response." +
                				" Expected an OWL Named Class, got: " + aClass);
                	}
                }
            }
            if (((Element) synonymsList.item(i)).getElementsByTagName(DIGVocabulary.Language.TOP).getLength() != 0) {
                conceptList.add(kb.getOWLThingClass());
            }
        }

        return conceptList;
    }


    public Collection getIndividuals() {
        Collection individuals = new HashSet();
        NodeList individualElementList = element.getElementsByTagName(DIGVocabulary.Language.INDIVIDUAL);
        for (int i = 0; i < individualElementList.getLength(); i++) {
            final Element individualElement = (Element) individualElementList.item(i);
            final OWLIndividual curInd = kb.getOWLIndividual(individualElement.getAttribute("name"));
            if (curInd != null) {
                individuals.add(curInd);
            }
        }
        return individuals;
    }


    public boolean getBoolean() {
        String val = element.getTagName();
        boolean b = true;
        if (val.equals("false")) {
            b = false;
        }
        return b;
    }
}

