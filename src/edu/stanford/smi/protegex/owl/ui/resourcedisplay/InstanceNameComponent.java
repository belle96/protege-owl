package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.util.Disposable;

/**
 * A Component that can be used to display type(s) and name of an Instance.
 * The name is displayed in a JTextField and may be edited.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InstanceNameComponent extends JPanel implements Disposable {

    private FrameListener frameListener = new FrameAdapter() {
        public void nameChanged(FrameEvent event) {
            updateAll();
        }
    };

    private Instance instance;
    private InstanceNameEditor textField;


    public InstanceNameComponent() {
        textField = new InstanceNameEditor();
   
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(textField);
    }


    public void dispose() {
        removeListener();
    }


    protected Instance getInstance() {
        return instance;
    }


    protected String getTypeText(Instance instance) {
        StringBuffer typeText = new StringBuffer();
        Iterator i = instance.getDirectTypes().iterator();
        while (i.hasNext()) {
            Cls type = (Cls) i.next();
            typeText.append(type.getBrowserText());
            if (i.hasNext()) {
                typeText.append(", ");
            }
        }
        return typeText.toString();
    }


    private void removeListener() {
        if (instance != null) {
            instance.removeFrameListener(frameListener);
        }
    }


    public void setInstance(Instance instance) {
        removeListener();
        textField.setInstance(instance);
        this.instance = instance;
        if (instance != null) {
            instance.addFrameListener(frameListener);
        }
        updateAll();
    }


    private void updateAll() {
        if (instance != null) {
            textField.setText(instance.getName());
        }
        else {
            textField.setText("");
            setEditable(false);
        }
    }

    // wrappers (mostly to keep the API from changing)

    public void setText(String text) {
        textField.setText(text);
    }

    public void selectAll() {
        textField.selectAll();
    }

    public void setEditable(boolean value) {
        textField.setEditable(value);
    }

    protected String getInvalidTextDescription(String text) {
        return textField.getInvalidTextDescription(text);
    }

    protected boolean validateText(String text) {
        return textField.validateText(text);
    }

    protected void commitChanges() {
        textField.attemptCommit();
    }
    
    public void setEnabled(boolean enabled) {
    	setEditable(enabled);
    	super.setEnabled(enabled);
    };
}
