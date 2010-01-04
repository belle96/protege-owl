package edu.stanford.smi.protegex.owl.ui.editors;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyValueEditorManager {

    private static Collection editors = new ArrayList();


    static {
        editors.add(new DateTimeValueEditor());
        editors.add(new DateValueEditor());
        editors.add(new TimeValueEditor());
        editors.add(new StringValueEditor());
    }


    public static PropertyValueEditor getEditor(final RDFResource subject, final RDFProperty predicate, Object value) {
        final Iterator it = PropertyValueEditorManager.listEditors();
        while (it.hasNext()) {
            PropertyValueEditor editor = (PropertyValueEditor) it.next();
            if (editor.canEdit(subject, predicate, value)) {
                return editor;
            }
        }
        return null;
    }


    public static Iterator listEditors() {
        return editors.iterator();
    }
}
