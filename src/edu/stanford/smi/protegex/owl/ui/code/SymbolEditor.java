package edu.stanford.smi.protegex.owl.ui.code;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SymbolEditor {

    /**
     * Ends the editing process and assigns the expression.
     * If the expression is invalid, then it calls <CODE>displayError</CODE>.
     */
    void assignExpression();


    /**
     * Performs a backspace operation.
     */
    void backspace();


    /**
     * Cancels the editing process without assigning a value.
     */
    void cancelEditing();


    /**
     * Checks the syntax of the current input and displays the error.
     */
    void displayError();


    /**
     * Gets the currently entered text.
     *
     * @return the text (raw)
     */
    String getText();


    /**
     * Inserts some text at the caret position.
     *
     * @param text the text to insert
     */
    void insertText(String text);


    /**
     * Inserts some text at the caret position and specifies the new caret position within
     * the inserted text.
     *
     * @param text        the text to insert
     * @param caretOffset the caret offset within the text
     */
    void insertText(String text, int caretOffset);
}
