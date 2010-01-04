package edu.stanford.smi.protegex.owlx.examples.javaDemo.model.impl;

import edu.stanford.smi.protege.model.FrameID;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owlx.examples.javaDemo.model.Customer;
import edu.stanford.smi.protegex.owlx.examples.javaDemo.model.Product;
import edu.stanford.smi.protegex.owlx.examples.javaDemo.model.Purchase;

import java.util.Iterator;

public class DefaultCustomer extends DefaultCustomer_
        implements Customer {

    public DefaultCustomer(OWLModel owlModel, FrameID id) {
        super(owlModel, id);
    }


    public DefaultCustomer() {
    }


    public String getBrowserText() {
        return getFirstName() + " " + getLastName();
    }


    public float getPurchasesSum() {
        float sum = 0;
        Iterator purchases = listPurchases();
        while (purchases.hasNext()) {
            Purchase purchase = (Purchase) purchases.next();
            Product product = purchase.getProduct();
            sum += product.getPrice();
        }
        return sum;
    }
}
