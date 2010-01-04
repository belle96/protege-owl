package edu.stanford.smi.protegex.owl.testing;

/**
 * A base implementation of OWLTest.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public abstract class AbstractOWLTest implements OWLTest {

    public final static String SANITY_GROUP = "Sanity Tests";

    private String group;

    private String documenation;

    private String name;


    public AbstractOWLTest() {
        this(null, null, null);
    }


    public AbstractOWLTest(String group, String name) {
        this(group, name, null);
    }


    public AbstractOWLTest(String group, String name, String documentation) {
        this.group = group;
        this.documenation = documentation;
        this.name = name;
    }


    public String getGroup() {
        return group;
    }


    public String getName() {
        if (name == null) {
            String className = getClass().getName();
            int dotIndex = className.lastIndexOf('.');
            String localName = className.substring(dotIndex + 1);
            if (localName.endsWith("Test")) {
                localName = localName.substring(0, localName.length() - 4);
            }
            return localName;
        }
        else {
            return name;
        }
    }


    public String getDocumentation() {
        return documenation;
    }
}
