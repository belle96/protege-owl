package edu.stanford.smi.protegex.owl.ui.actions.triple;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractTripleAction implements TripleAction {

    private String iconFileName;

    private Class iconResourceClass;

    private String name;


    public AbstractTripleAction(String name) {
        this(name, null, null);
    }


    public AbstractTripleAction(String name, String iconFileName, Class iconResourceClass) {
        this.name = name;
        this.iconFileName = iconFileName;
        this.iconResourceClass = iconResourceClass;
    }


    public String getGroup() {
        return null;
    }


    public String getIconFileName() {
        return iconFileName;
    }


    public Class getIconResourceClass() {
        return iconResourceClass;
    }


    public String getName() {
        return name;
    }
}
