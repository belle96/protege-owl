package edu.stanford.smi.protegex.owl.testing.sanity;

import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.testing.*;
import edu.stanford.smi.protegex.owl.ui.ProtegeUI;
import edu.stanford.smi.protegex.owl.ui.dialogs.ModalDialogFactory;

import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class SymmetricPropertyMustHaveEqualRangeAndDomainTest extends AbstractOWLTest
        implements RepairableOWLTest, RDFPropertyTest {

    public SymmetricPropertyMustHaveEqualRangeAndDomainTest() {
        super(SANITY_GROUP, null);
    }


    public static boolean fails(RDFProperty property) {
        if (property instanceof OWLObjectProperty && ((OWLObjectProperty) property).isSymmetric()) {
            if (!property.isDomainDefined()) {
                return false;
            }
            else {
                Collection domain = property.getUnionDomain();
                Collection range = property.getUnionRangeClasses();
                return !domain.containsAll(range) || !range.containsAll(domain);
            }
        }
        else {
            return false;
        }
    }


    private String getListString(Collection items) {
        String str = "{";
        for (Iterator it = items.iterator(); it.hasNext();) {
            Instance instance = (Instance) it.next();
            str += instance.getBrowserText();
            if (it.hasNext()) {
                str += ", ";
            }
        }
        return str + "}";
    }


    public boolean repair(OWLTestResult testResult) {
        OWLProperty property = (OWLProperty) testResult.getHost();
        Collection domain = property.getUnionDomain();
        Collection range = property.getUnionRangeClasses();
        String message = "The current range of " + property.getBrowserText() +
                " is " + getListString(range) +
                "\nand its current domain is " + getListString(domain) +
                ".\nDo you want to assign the range into the domain (yes)" +
                "\nor do you want to assign the domain into the range (no)?";
        int option = ProtegeUI.getModalDialogFactory().showConfirmCancelDialog(ProtegeUI.getTopLevelContainer(property.getProject()), message,
                "Repair test failure");
        if (option == ModalDialogFactory.OPTION_YES) {
            repairRangeIntoDomain(property);
            return true;
        }
        else if (option == ModalDialogFactory.OPTION_NO) {
            repairDomainIntoRange(property);
            return true;
        }
        return false;
    }


    public static void repairDomainIntoRange(OWLProperty property) {
        if (property.hasObjectRange()) {
            Collection domain = new ArrayList(property.getUnionDomain());
            property.setUnionRangeClasses(domain);
        }
    }


    public static void repairRangeIntoDomain(OWLProperty property) {
        Collection range = property.getUnionRangeClasses();
        Collection oldDomain = new ArrayList(property.getUnionDomain());
        for (Iterator it = oldDomain.iterator(); it.hasNext();) {
            OWLNamedClass namedCls = (OWLNamedClass) it.next();
            property.removeUnionDomainClass(namedCls);
        }
        for (Iterator it = range.iterator(); it.hasNext();) {
            RDFSClass cls = (RDFSClass) it.next();
            property.addUnionDomainClass(cls);
        }
    }


    public List test(RDFProperty property) {
        if (fails(property)) {
            return Collections.singletonList(new DefaultOWLTestResult("Symmetric properties must have equal ranges and domains.",
                    property,
                    OWLTestResult.TYPE_ERROR,
                    this));
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
}
