package edu.stanford.smi.protegex.owlx.examples.javaDemo;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owlx.examples.javaDemo.model.Customer;
import edu.stanford.smi.protegex.owlx.examples.javaDemo.model.MyFactory;
import edu.stanford.smi.protegex.owlx.examples.javaDemo.model.Product;
import edu.stanford.smi.protegex.owlx.examples.javaDemo.model.Purchase;

import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class JavaDemoApplication {

    public static void main(String[] args) throws Exception {
        String uri = "http://www.owl-ontologies.com/javaDemo.owl";
        OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);

        // Create a couple of products
        OWLNamedClass productClass = owlModel.getOWLNamedClass("Product");
        Product productA = (Product) productClass.createOWLIndividual("ProdcutA").as(Product.class);
        productA.setPrice(41.99f);
        Product productB = (Product) productClass.createOWLIndividual("ProductB").as(Product.class);
        productB.setPrice(1.50f);
        Product productC = (Product) productClass.createOWLIndividual("ProductC").as(Product.class);
        productC.setPrice(2.50f);

        OWLNamedClass customerClass = owlModel.getOWLNamedClass("Customer");
        Customer customer = (Customer) customerClass.createOWLIndividual("Hans").as(Customer.class);
        customer.setFirstName("Hans");
        customer.setLastName("Aldi");
        createPurchase(customer, productA, "2005-01-01");
        createPurchase(customer, productB, "2005-01-02");
        createPurchase(customer, productC, "2005-01-03");
        createPurchase(customer, productA, "2005-02-07");

        double sum = customer.getPurchasesSum();
        System.out.println("Customer " + customer.getBrowserText() + " has spent $" + sum);
    }


    private static Purchase createPurchase(Customer customer, Product product, String date) {
        OWLModel owlModel = customer.getOWLModel();
        Purchase purchase = new MyFactory(owlModel).createPurchase(null);
        purchase.setCustomer(customer);
        purchase.setProduct(product);
        RDFSDatatype xsdDate = owlModel.getRDFSDatatypeByName("xsd:date");
        purchase.setDate(owlModel.createRDFSLiteral(date, xsdDate));
        return purchase;
    }


    // Complicating version without model classes
    private static float getPurchasesSum(RDFIndividual customer) {
        OWLModel owlModel = customer.getOWLModel();
        float sum = 0;
        RDFProperty purchasesProperty = owlModel.getRDFProperty("purchases");
        RDFProperty productProperty = owlModel.getRDFProperty("product");
        RDFProperty priceProperty = owlModel.getRDFProperty("price");
        Iterator purchases = customer.listPropertyValues(purchasesProperty);
        while (purchases.hasNext()) {
            RDFIndividual purchase = (RDFIndividual) purchases.next();
            RDFIndividual product = (RDFIndividual) purchase.getPropertyValue(productProperty);
            Float price = (Float) product.getPropertyValue(priceProperty);
            sum += price.floatValue();
        }
        return sum;
    }
}
