package edu.stanford.smi.protegex.owl.ui.resourcedisplay;

import edu.stanford.smi.protegex.owl.model.RDFIndividual;
import edu.stanford.smi.protegex.owl.model.RDFResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class TriplesViewResourceDisplayPlugin implements ResourceDisplayPlugin {


    public void initResourceDisplay(RDFResource resource, JPanel hostPanel) {
        if (resource instanceof RDFIndividual) {
            Component c = hostPanel.getParent();
            while (!(c instanceof ResourceDisplay)) {
                c = c.getParent();
            }
            final ResourceDisplay resourceDisplay = (ResourceDisplay) c;
            if (resourceDisplay != null) {
                final JRadioButton formViewBox = new JRadioButton("Form View");
                final JRadioButton triplesViewBox = new JRadioButton("Triples View");
                formViewBox.setSelected(!resourceDisplay.isTriplesDisplayed());
                formViewBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateResourceDisplay(resourceDisplay, formViewBox, triplesViewBox);
                    }
                });
                triplesViewBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        updateResourceDisplay(resourceDisplay, formViewBox, triplesViewBox);
                    }
                });
                triplesViewBox.setSelected(resourceDisplay.isTriplesDisplayed());
                ButtonGroup group = new ButtonGroup();
                group.add(formViewBox);
                group.add(triplesViewBox);
                Box box = Box.createHorizontalBox();
                box.add(formViewBox);
                box.add(Box.createHorizontalStrut(8));
                box.add(triplesViewBox);
                hostPanel.add(box);
            }
        }
    }


    private void updateResourceDisplay(ResourceDisplay resourceDisplay, JRadioButton formViewBox, JRadioButton triplesViewBox) {
        boolean formView = formViewBox.isSelected();
        boolean triplesView = triplesViewBox.isSelected();
        resourceDisplay.setMode(formView, triplesView);
    }
}
