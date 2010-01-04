package edu.stanford.smi.protegex.owl.testing.style;

import edu.stanford.smi.protege.Application;
import edu.stanford.smi.protegex.owl.model.*;
import edu.stanford.smi.protegex.owl.testing.*;
import edu.stanford.smi.protegex.owl.ui.cls.ConvertToDefinedClassAction;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Finds classes that have a minimum cardinality zero restriction.
 * 
 * The fix removes this restriction and creates a defined subclass to model
 * "optionality".
 *
 * @author Nick Drummond, Medical Informatics Group, University of Manchester
 *         06-Feb-2006
 */
public class MinCardiZeroTest extends AbstractOWLTest
        implements RDFSClassTest, AutoRepairableOWLTest {

    private static boolean warn = true;
    private static boolean fix = false;


    public String getDocumentation() {
        return "Find classes that have a min cardi zero restriction on them";
    }


    public String getGroup() {
        return "Style";
    }


    public String getName() {
        return "Min Cardi Zero";
    }


    public List test(RDFSClass aClass) {
        if (aClass instanceof OWLNamedClass){

            OWLNamedClass namedClass = (OWLNamedClass)aClass;
            Collection allRestrictions = namedClass.getRestrictions();

            for (Iterator i = allRestrictions.iterator(); i.hasNext();){

                OWLRestriction restr = (OWLRestriction)i.next();
                if (restr instanceof OWLMinCardinality){

                    if (((OWLMinCardinality)restr).getCardinality() == 0){
                        return Collections.singletonList(
                                new DefaultOWLTestResult ("Min cardinality zero does not mean anything in OWL",
                                                          aClass,
                                                          OWLTestResult.TYPE_WARNING,
                                                          this)
                        );
                    }
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    public boolean repair(OWLTestResult testResult) {

        boolean result = false;

        handleUserWarning();

        if (fix){
            OWLNamedClass namedClass = (OWLNamedClass)testResult.getHost();

            OWLModel owlModel = namedClass.getOWLModel();

            Collection allRestrictions = namedClass.getRestrictions();

            for (Iterator i = allRestrictions.iterator(); i.hasNext();){

                OWLRestriction restr = (OWLRestriction)i.next();
                if (restr instanceof OWLMinCardinality){

                    if (((OWLMinCardinality)restr).getCardinality() == 0){

                        OWLMinCardinality cloneRestr = (OWLMinCardinality)restr.createClone();
                        cloneRestr.setCardinality(1);
                        String newClassName = namedClass.getBrowserText() + "That" +
                                              cloneRestr.getOnProperty().getBrowserText();
                        if (cloneRestr.getQualifier() != null){
                            newClassName += cloneRestr.getQualifier().getBrowserText();
                        }

                        OWLNamedClass newClass = owlModel.createOWLNamedClass(newClassName);
                        newClass.addSuperclass(namedClass);
                        newClass.removeSuperclass(owlModel.getOWLThingClass());
                        newClass.addSuperclass(cloneRestr);
                        ConvertToDefinedClassAction.performAction(newClass);

                        namedClass.removeSuperclass(restr);

                        result = true;
                    }
                }
            }
        }

        return result;
    }

    private void handleUserWarning() {
       if (warn){
            JPanel panel = new JPanel(new BorderLayout(6, 6));
            JLabel question = new JLabel("Fixing min cardinality 0 restrictions" +
                                         " creates new defined subclasses." +
                                         " Are you sure you want to do this?");

            JCheckBox doNotWarnAgainBox = new JCheckBox("do not warn me again this session", false);
            panel.add(question, BorderLayout.NORTH);
            panel.add(doNotWarnAgainBox, BorderLayout.SOUTH);

            fix = (JOptionPane.showConfirmDialog(Application.getMainWindow(),
                                                 panel,
                                                 "Fix min cardi 0",
                                                 JOptionPane.YES_NO_OPTION,
                                                 JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION);

            if (doNotWarnAgainBox.isSelected()){
                warn = false;
            }
        }
    }
}
