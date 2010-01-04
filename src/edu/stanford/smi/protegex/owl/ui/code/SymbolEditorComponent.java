package edu.stanford.smi.protegex.owl.ui.code;

import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 6, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public abstract class SymbolEditorComponent extends JComponent implements SymbolEditor {

    private SymbolEditorHandler symbolEditorHandler;

    private boolean multiline;

    private SymbolErrorDisplay errorDisplay;

    private OWLModel model;

    private Exception parseException;


    public SymbolEditorComponent(OWLModel model, SymbolErrorDisplay errorDisplay, boolean multiline) {
        this.model = model;
        this.errorDisplay = errorDisplay;
        this.multiline = multiline;
    }


    /**
     * Gets the JTextComponent that does the editing.  This will
     * be a child component of this component.
     */
    public abstract JTextComponent getTextComponent();


    protected abstract void parseExpression() throws Exception;


    public void assignExpression() {
        try {
            parseExpression();
            if (getSymbolEditorHandler() != null) {
                getSymbolEditorHandler().stopEditing();
            }
        }
        catch (Exception e) {
            parseException = e;
            displayError();
        }
    }


    public void displayError() {
        if (parseException != null) {
            try {
                parseExpression();
            }
            catch (Exception e) {
                parseException = e;
            }
        }
        errorDisplay.displayError(parseException);
    }


    public void setSymbolEditorHandler(SymbolEditorHandler symbolEditorHandler) {
        this.symbolEditorHandler = symbolEditorHandler;
    }


    public boolean isMultiline() {
        return multiline;
    }


    public SymbolEditorHandler getSymbolEditorHandler() {
        return symbolEditorHandler;
    }


    public SymbolErrorDisplay getErrorDisplay() {
        return errorDisplay;
    }


    public OWLModel getModel() {
        return model;
    }


    public void cancelEditing() {
        getSymbolEditorHandler().cancelEditing();
    }


    public void backspace() {
        int caretPos = getTextComponent().getCaretPosition();
        if (caretPos > 0) {
            Document doc = getTextComponent().getDocument();
            try {
                doc.remove(caretPos - 1, 1);
            }
            catch (BadLocationException e) {
              Log.getLogger().log(Level.SEVERE, "Exception caught", e);
            }
        }
    }


    public String getText() {
        return getTextComponent().getText();
    }


    public void insertText(String text) {
        insertText(text, 0);
    }


    public void insertText(String text,
                           int caretOffset) {

        JTextComponent textComponent = getTextComponent();
        try {
            Document doc = textComponent.getDocument();
            int caretPos = textComponent.getCaretPosition();
            doc.insertString(caretPos, text, null);
            textComponent.setCaretPosition(caretPos + caretOffset);
        }
        catch (BadLocationException e) {
          Log.getLogger().log(Level.SEVERE, "Exception caught", e);
        }
    }
}

