package edu.stanford.smi.protegex.owl.jena.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.ProtegeOWL;

/**
 * A Singleton that manages the list of namespaces (such as DC) that shall be
 * imported internally if resources from it are referenced as untyped in a file.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImplicitImports {

    public final static String LOCAL_FILE_NAME = "implicit-imports.repository";

    public final static File FILE =
            new File(ProtegeOWL.getPluginFolder(),
                    LOCAL_FILE_NAME);

    private static Set namespaces;


    /**
     * Checks whether a given namespace shall be imported even if no explicit
     * owl:imports statement is found.
     *
     * @param namespace the namespace in question
     * @return true  if the namespace shall be imported
     */
    public static boolean isImplicitImport(String namespace) {
        if (namespaces == null) {
            namespaces = new HashSet();
            File file = FILE;
            if (file.exists()) {
                try {
                    FileReader reader = new FileReader(file);
                    BufferedReader br = new BufferedReader(reader);
                    for (; ;) {
                        String line = br.readLine();
                        if (line == null || line.trim().length() == 0) {
                            break;
                        }
                        namespaces.add(line);
                    }
                }
                catch (Exception ex) {
                  Log.getLogger().warning("[ImplicitImports]  Warning: Could not load " + file);
                    Log.getLogger().log(Level.WARNING, "Exception caught", ex);
                }
            }
        }
        return namespaces.contains(namespace);
    }
}
