package edu.stanford.smi.protegex.owl.ui.importstree;

import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protege.util.LazyTreeRoot;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.SelectableTree;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.results.HostResourceDisplay;

import javax.swing.tree.TreePath;
import java.util.*;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ImportsTree extends SelectableTree implements HostResourceDisplay {

    private static int MAX_EXPANSIONS = 50;

    private OWLOntology rootOntology;

    public ImportsTree(OWLOntology rootOntology) {
        super(null, new ImportsTreeRoot(rootOntology));
        this.rootOntology = rootOntology;
        setCellRenderer(new ResourceRenderer());
        setRootVisible(false);
        ComponentUtilities.fullSelectionExpand(this, MAX_EXPANSIONS);
    }


    private void addResources(Set result, ImportsTreeNode node) {
        Object value = node.getUserObject();
        if (!result.contains(value)) {
            result.add(value);
            for (int i = 0; i < node.getChildCount(); i++) {
                ImportsTreeNode childNode = (ImportsTreeNode) node.getChildAt(i);
                addResources(result, childNode);
            }
        }
    }


    public OWLOntology getRootOntology() {
        return rootOntology;
    }


    /**
     * Gets the currently selected resources.
     *
     * @return a Set of RDFResources
     */
    public Set getSelectedResources() {
        Set result = new HashSet();
        TreePath[] paths = getSelectionPaths();
        if (paths != null && paths.length > 0) {
            for (int i = 0; i < paths.length; i++) {
                TreePath path = paths[i];
                ImportsTreeNode node = (ImportsTreeNode) path.getLastPathComponent();
                result.add(node.getUserObject());
            }
        }
        else {
            LazyTreeRoot root = (LazyTreeRoot) getModel().getRoot();
            addResources(result, (ImportsTreeNode) root.getChildAt(0));
        }
        return result;
    }


    public boolean displayHostResource(RDFResource resource) {
    	if (rootOntology == null) {
    		Log.getLogger().warning("Root ontology = null!");    		
    		return false;
    	}
    	
        boolean result = false;
        if (resource instanceof OWLOntology) {

            List importsPath = new ArrayList();
            importsPath.add(resource);
            Collection ontologies = rootOntology.getOWLModel().getOWLOntologies();
            boolean progress = true;
            while (!importsPath.contains(rootOntology) && progress) {
                progress = false;
                for (Iterator i = ontologies.iterator(); i.hasNext();) {
                    OWLOntology ont = (OWLOntology) i.next();
                    OWLOntology previous = (OWLOntology) importsPath.get(importsPath.size() - 1);
                    if (ont.getImports().contains(previous.getURI().toString()) &&
                        !importsPath.contains(ont)) {
                        progress = true;
                        importsPath.add(ont);
                    }
                }
            }
            setSelectionPath(ComponentUtilities.getTreePath(this, importsPath));
            result = true;
        }
        return result;
    }
}
