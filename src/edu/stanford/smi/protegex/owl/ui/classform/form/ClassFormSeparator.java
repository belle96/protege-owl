package edu.stanford.smi.protegex.owl.ui.classform.form;

import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableItem;
import edu.stanford.smi.protegex.owl.ui.conditions.SeparatorCellRenderer;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ClassFormSeparator extends SeparatorCellRenderer {

    public ClassFormSeparator(boolean necessaryAndSufficient) {
        super(true);
        String text = necessaryAndSufficient ?
                ConditionsTableItem.SUFFICIENT :
                ConditionsTableItem.NECESSARY;
        setText(text);
    }
}
