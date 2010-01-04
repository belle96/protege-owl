package edu.stanford.smi.protegex.owl.ui.clsdesc;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.ui.clsdesc.manchester.ManchesterOWLTextPane;
import edu.stanford.smi.protegex.owl.ui.code.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 7, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class ClassDescriptionEditorComponent extends SymbolEditorComponent {

    private ManchesterOWLTextPane textPane;


    public ClassDescriptionEditorComponent(OWLModel model, SymbolErrorDisplay errorDisplay, boolean multiline) {
        super(model, errorDisplay, multiline);
        textPane = new ManchesterOWLTextPane(model, errorDisplay,
                new OWLResourceNameMatcher(),
                new OWLSyntaxConverter(model));
        setLayout(new BorderLayout());
        add(textPane);
        textPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
    }


    public JTextComponent getTextComponent() {
        return textPane;
    }


    public void setSymbolEditorHandler(SymbolEditorHandler symbolEditorHandler) {
        super.setSymbolEditorHandler(symbolEditorHandler);
        textPane.setSymbolEditorHandler(symbolEditorHandler);
    }


    protected void parseExpression() throws OWLClassParseException {
        getModel().getOWLClassDisplay().getParser().checkClass(getModel(), getTextComponent().getText());
    }
}

