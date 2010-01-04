package edu.stanford.smi.protegex.owl.ui.forms;

/**
 * An interface for objects that can find a widget description URI for
 * a widget Java class name. 
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface WidgetClassMapper {

    String getWidgetClassURI(String javaClassName);
}
