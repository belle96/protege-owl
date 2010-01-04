package edu.stanford.smi.protegex.owl.model.triplestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A chain of Triples, so that the object of one Triple is the subject of
 * the next.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TriplePath implements TripleDescriptor {

    private List triples = new ArrayList();


    public TriplePath(Iterator it) {
        while(it.hasNext()) {
            triples.add(it.next());
        }
    }


    public TriplePath(Collection c) {
        this(c.iterator());
    }


    public Triple getFirstTriple() {
        if(triples.isEmpty()) {
            return null;
        }
        else {
            return (Triple) triples.get(0);
        }
    }


    public Triple getLastTriple() {
        if(triples.isEmpty()) {
            return null;
        }
        else {
            return (Triple) triples.get(triples.size() - 1);
        }
    }


    public int getLength() {
        return triples.size();
    }


    public Iterator listTriples() {
        return triples.iterator();
    }
}
