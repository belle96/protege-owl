package edu.stanford.smi.protegex.owl.ui.matrix.property;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.FrameRenderer;
import edu.stanford.smi.protegex.owl.ui.matrix.AbstractMatrixColumn;

import javax.swing.table.TableCellRenderer;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class InversePropertyMatrixColumn extends AbstractMatrixColumn {

    public InversePropertyMatrixColumn() {
        super("Inverse", 150);
    }


    public TableCellRenderer getCellRenderer() {
        return new FrameRenderer() {

            private boolean calling = false;


            protected void loadSlot(Slot slot) {
                if (calling) {
                    super.loadSlot(slot);
                }
                else {
                    Slot inverseSlot = slot.getInverseSlot();
                    if (inverseSlot != null) {
                        calling = true;
                        loadSlot(inverseSlot);
                        calling = false;
                    }
                }
            }
        };
    }
}
