package edu.stanford.smi.protegex.owl.ui.classform.component;

import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matthew Horridge  <matthew.horridge@cs.man.ac.uk>
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class FormComponent extends JComponent implements Disposable {

    public static final int BORDER_THICKNESS = 4;

    private JComponent content;

    private boolean designTime;


    public FormComponent() {
        setBorder(BorderFactory.createEmptyBorder(BORDER_THICKNESS,
                BORDER_THICKNESS,
                BORDER_THICKNESS,
                BORDER_THICKNESS));
        setLayout(new BorderLayout());
    }


    public JComponent getContent() {
        return content;
    }


    public abstract void init(OWLModel owlModel);


    public boolean isDesignTime() {
        return designTime;
    }


    public void setContent(JComponent content) {
        this.content = content;
        removeAll();
        if (content != null) {
            add(BorderLayout.CENTER, content);
        }
    }

    // Was: setClass, but this name would be too generic.  Other options are setSubject, setEditedClass


    /**
     * Propagates the currently edited class of the form into this FormComponent.
     *
     * @param namedClass the new named class
     */
    public abstract void setNamedClass(OWLNamedClass namedClass);


    public void setDesignTime(boolean b) {
        designTime = b;
    }
}
