package edu.stanford.smi.protegex.owl.ui.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Facet;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.event.ClassAdapter;
import edu.stanford.smi.protegex.owl.model.event.ClassListener;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.AbstractPropertyWidget;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassTypeConverterWidget extends AbstractPropertyWidget implements Disposable {

    private boolean blocked;

    private JCheckBox checkBox;

    private OWLNamedClass cls;

    private ClassListener classListener = new ClassAdapter() {
        public void subclassAdded(RDFSClass cls, RDFSClass subclass) {
            updateSelection();
        }


        public void subclassRemoved(RDFSClass cls, RDFSClass subclass) {
            updateSelection();
        }


        public void superclassAdded(RDFSClass cls, RDFSClass superclass) {
            updateSelection();
        }


        public void superclassRemoved(RDFSClass cls, RDFSClass superclass) {
            updateSelection();
        }
    };


    public void dispose() {
        if (cls != null) {
            cls.removeClassListener(classListener);
        }
    }


    private void handleChange() {
        OWLClassesTab tab = OWLClassesTab.getOWLClassesTab(this);
        if (cls.getDefinition() != null) {
            ConvertToPrimitiveClassAction.performAction(cls);
        }
        else {
            ConvertToDefinedClassAction.performAction(cls);
        }
        tab.ensureClsSelected(cls, -1);
        updateSelection();  // Just in case no event is fired because action rejected
    }


    public void initialize() {
        checkBox = new JCheckBox("Has necessary & sufficient conditions ");
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!blocked) {
                    handleChange();
                }
            }
        });
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(checkBox);
        add(new JLabel(OWLIcons.getPrimitiveClsIcon()));
        add(new JLabel("/"));
        add(new JLabel(OWLIcons.getImageIcon(OWLIcons.DEFINED_OWL_CLASS)));
    }


    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return (cls.getKnowledgeBase() instanceof OWLModel) &&
                slot.getName().equals(RDFSNames.Slot.SEE_ALSO);
    }


    public void setCls(OWLNamedClass cls) {
        if (this.cls != null) {
            this.cls.removeClassListener(classListener);
        }
        this.cls = cls;
        if (cls != null) {
            cls.addClassListener(classListener);
        }
        setEnabled(cls != null && cls.isEditable());
        updateSelection();
    }


    public void setInstance(Instance newInstance) {
        setCls((OWLNamedClass) newInstance);
    }


    private void updateSelection() {
        if (cls != null) {
            blocked = true;
            checkBox.setSelected(cls.getDefinition() != null);
            blocked = false;
        }
    }
}
