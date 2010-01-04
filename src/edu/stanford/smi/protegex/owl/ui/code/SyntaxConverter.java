package edu.stanford.smi.protegex.owl.ui.code;

import javax.swing.text.JTextComponent;

/**
 * An interface for objects that can convert textual input
 * such as "and" into other characters which are otherwise
 * hard to enter.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface SyntaxConverter {

    void convertSyntax(JTextComponent textComponent);
}
