package edu.stanford.smi.protegex.owl.ui.forms;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class DefaultWidgetClassMapper implements WidgetClassMapper {

    public String getWidgetClassURI(String widgetClassName) {
        if (widgetClassName.startsWith("edu.stanford.smi.protegex.owl.")) {
            int index = widgetClassName.lastIndexOf('.');
            widgetClassName = widgetClassName.substring(index + 1);
            return ProtegeFormsNames.NS + widgetClassName;
        }
        else {
            return null;
        }
    }
}
