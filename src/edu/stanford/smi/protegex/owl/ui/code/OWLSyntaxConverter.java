package edu.stanford.smi.protegex.owl.ui.code;

import edu.stanford.smi.protegex.owl.model.OWLModel;

import javax.swing.text.JTextComponent;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class OWLSyntaxConverter implements SyntaxConverter {

    private OWLModel owlModel;


    public OWLSyntaxConverter(OWLModel owlModel) {
        this.owlModel = owlModel;
    }


    public void convertSyntax(JTextComponent textComponent) {
        OWLTextFormatter.updateSyntax(textComponent, owlModel);
    }
}
