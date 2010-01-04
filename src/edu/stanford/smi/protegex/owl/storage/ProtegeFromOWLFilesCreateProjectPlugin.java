package edu.stanford.smi.protegex.owl.storage;

import java.net.URI;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import edu.stanford.smi.protege.Application;
import edu.stanford.smi.protege.model.KnowledgeBaseFactory;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.plugin.AbstractCreateProjectPlugin;
import edu.stanford.smi.protege.plugin.CreateProjectWizard;
import edu.stanford.smi.protege.storage.clips.ClipsKnowledgeBaseFactory;
import edu.stanford.smi.protege.util.FileUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.PropertyList;
import edu.stanford.smi.protege.util.WizardPage;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaKnowledgeBaseFactory;
import edu.stanford.smi.protegex.owl.jena.OWLFilesPlugin;
import edu.stanford.smi.protegex.owl.jena.importer.OWLImporter;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.ui.jena.ProtegeFromOWLFilesWizardPage;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class ProtegeFromOWLFilesCreateProjectPlugin
        extends AbstractCreateProjectPlugin
        implements OWLFilesPlugin {

    private String fileURI;


    public ProtegeFromOWLFilesCreateProjectPlugin() {
        super("OWL File (.owl)  -- Warning: conversion may be incomplete");
        JenaKnowledgeBaseFactory.useStandalone = false;
    }


    public void addImport(String uri, String prefix) {
    }


    protected Project buildNewProject(KnowledgeBaseFactory factory) {        
        Project project = super.buildNewProject(factory);
        if (project != null) {
            try {
                OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(fileURI);
                new OWLImporter(owlModel, project.getKnowledgeBase());
            }
            catch (Exception ex) {
                Log.getLogger().log(Level.SEVERE, "Exception caught", ex);
                JOptionPane.showMessageDialog(Application.getMainWindow(),
                        "Could not load " + fileURI + "\n" + ex,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }        
        return project;
    }


    public boolean canCreateProject(KnowledgeBaseFactory factory, boolean useExistingSources) {
        return useExistingSources && factory instanceof ClipsKnowledgeBaseFactory;
    }


    public WizardPage createCreateProjectWizardPage(CreateProjectWizard wizard, boolean useExistingSources) {
        return new ProtegeFromOWLFilesWizardPage(wizard, this);
    }


    protected URI getBuildProjectURI() {
        if (fileURI != null) {
            if (fileURI.startsWith("file:")) {
                int index = fileURI.lastIndexOf('.');
                if (index > 0) {
                    String uri = FileUtilities.replaceExtension(fileURI, ".pprj");
                    try {
                        return new URI(uri);
                    }
                    catch (Exception ex) {
                    }
                }
            }
        }
        return super.getBuildProjectURI();
    }


    protected void initializeSources(PropertyList sources) {
    }


    public void setDefaultClassView(Class typeClass) {
    }


    public void setOntologyName(String namespace) {
    }


    public void setDublinCoreRedirectToDLVersion(boolean b) {
    }


    public void setFile(String fileURI) {
        this.fileURI = fileURI;
    }


    public void setLanguage(String lang) {
    }


    public void setProfile(String profileURI) {
    }
}
