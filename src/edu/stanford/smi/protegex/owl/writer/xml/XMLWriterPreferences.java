package edu.stanford.smi.protegex.owl.writer.xml;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 8, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class XMLWriterPreferences {

    private static XMLWriterPreferences instance;

    private boolean useNamespaceEntities;

    private boolean indenting;

    private int indentSize;


    private XMLWriterPreferences() {
        useNamespaceEntities = true;
        indenting = true;
        indentSize = 4;
    }


    public static XMLWriterPreferences getInstance() {
        if (instance == null) {
            instance = new XMLWriterPreferences();
        }
        return instance;
    }


    public boolean isUseNamespaceEntities() {
        return useNamespaceEntities;
    }


    public void setUseNamespaceEntities(boolean useNamespaceEntities) {
        this.useNamespaceEntities = useNamespaceEntities;
    }


    public boolean isIndenting() {
        return indenting;
    }


    public void setIndenting(boolean indenting) {
        this.indenting = indenting;
    }


    public int getIndentSize() {
        return indentSize;
    }


    public void setIndentSize(int indentSize) {
        this.indentSize = indentSize;
    }
}

