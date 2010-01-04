package edu.stanford.smi.protegex.owl.model;

import java.net.URI;
import java.util.Collection;

/**
 * A RDFIndividual that represents an OWL Ontology (tag).
 * Ontologies represent metadata such as versioning info and includes.
 * There is usually only one Ontology defined in each OWL file.
 * <p/>
 * Although this inherits namespace support, the Ontology's URI itself
 * is stored in a separate own slot, which is accessible through the
 * <CODE>getOntologyURI()</CODE> and <CODE>setOntologyURI()</CODE>
 * methods.  Note that it is not recommended to do low level changes
 * to OWLOntologies; in particular the name should not be changed.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public interface OWLOntology extends RDFResource {

    void addBackwardCompatibleWith(String resource);


    /**
     * Adds an URI to the owl:imports statements of this ontology.
     * Note that this does not actually load the imported ontology - it only adds the import statement.
     * If you want to load the import immediately, then you need to call the corresponding method in
     * <CODE>ProtegeOWLParser</CODE> (for file-based projects).
     *
     * @param uri an URI (not ending with a deliminator such as #)
     * @see edu.stanford.smi.protegex.owl.jena.parser.ProtegeOWLParser#addImport
     */
    void addImports(String uri);


    /**
     * 
     * @param uri
     */
    void addImports(URI uri);


    /**
     * @deprecated use version with RDFUntypedResource instead
     */
    void addImports(RDFExternalResource resource);


    void addImports(OWLOntology ontology);


    void addImports(RDFUntypedResource resource);


    void addIncompatibleWith(String resource);


    void addPriorVersion(String resource);


    Collection getBackwardCompatibleWith();


    /**
     * Gets the URIs of the imports (as strings)
     *
     * @return a Collection of strings
     */
    Collection<String> getImports();


    /**
     * Gets the URIs of the imports as RDFUntypedResources or OWLOntologies.
     *
     * @return the import resources
     */
    Collection getImportResources();


    Collection getIncompatibleWith();


    /**
     * @see #getURI
     * @deprecated now getURI() can be used for that
     */
    String getOntologyURI();


    Collection getPriorVersions();


    void removeBackwardCompatibleWith(String resource);


    void removeImports(String uri);


    void removeIncompatibleWith(String resource);


    void removePriorVersion(String resource);
}
