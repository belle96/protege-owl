package edu.stanford.smi.protegex.owl.inference.dig.translator;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jun 29, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DefaultDIGQueryResponseIterator implements Iterator {

    private Document doc;

    private int currentElementIndex = 0;

    private NodeList nodeList;

    private boolean elementAvailable = false;

    private DefaultDIGQueryResponse interpreter;


    public DefaultDIGQueryResponseIterator(Document doc, OWLModel kb) {
        this.doc = doc;
        nodeList = this.doc.getDocumentElement().getChildNodes();
        currentElementIndex = 0;
        interpreter = new DefaultDIGQueryResponse(kb);
        advanceToNextElement();
    }


    public boolean hasNext() {
        return elementAvailable;
    }


    protected void advanceToNextElement() {
        int index = currentElementIndex;
        elementAvailable = true;
        // Search for the next element
        while (nodeList.item(index).getNodeType() != Node.ELEMENT_NODE ||
                nodeList.item(index).getNodeName().equals(DIGVocabulary.Response.ERROR)) {
            // Check to see if we are already on the last
            // node.  If we are then no more elements
            // are available
            if (index == nodeList.getLength() - 1) {
                // Last element
                elementAvailable = false;
                break;
            }
            // Carry on searching
            index++;
        }

        currentElementIndex = index;
    }


    public Object next() {
        Element element = (Element) nodeList.item(currentElementIndex);
        currentElementIndex++;
        interpreter.setElement(element);
        advanceToNextElement();
        return interpreter;
    }


    public void remove() {
        throw new UnsupportedOperationException();
    }
}

