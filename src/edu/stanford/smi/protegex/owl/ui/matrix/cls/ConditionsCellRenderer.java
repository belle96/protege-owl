package edu.stanford.smi.protegex.owl.ui.matrix.cls;

import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ConditionsCellRenderer extends FrameRenderer {


    private Collection getDirectSuperclasses(Cls cls) {
        if (cls instanceof OWLNamedClass) {
            OWLNamedClass namedCls = (OWLNamedClass) cls;
            List results = new ArrayList();
            results.addAll(namedCls.getEquivalentClasses());
            results.addAll(namedCls.getPureSuperclasses());
            return results;
        }
        else {
            return cls.getDirectSuperclasses();
        }
    }


    protected void loadCls(Cls cls) {

        Collection cs = getDirectSuperclasses(cls);

        setGrayedSecondaryText(false);
        String previousIconName = null;
        for (Iterator it = cs.iterator(); it.hasNext();) {
            Cls superCls = (Cls) it.next();
            String nextIconName = null;
            if (superCls.hasDirectSuperclass(cls)) {
                nextIconName = "OWLEquivalentClass";
            }
            else {
                nextIconName = "Superclass";
            }
            if (!nextIconName.equals(previousIconName)) {
                addIcon(OWLIcons.getImageIcon(nextIconName));
                previousIconName = nextIconName;
            }
            String str = " " + superCls.getBrowserText();
            if (it.hasNext()) {
                str += "    ";
            }
            setGrayedText(false);
            addText(str);
        }
    }
}
