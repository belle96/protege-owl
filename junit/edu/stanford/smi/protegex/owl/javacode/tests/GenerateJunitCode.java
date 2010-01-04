package edu.stanford.smi.protegex.owl.javacode.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.javacode.EditableJavaCodeGeneratorOptions;
import edu.stanford.smi.protegex.owl.javacode.JavaCodeGenerator;
import edu.stanford.smi.protegex.owl.model.OWLModel;

public class GenerateJunitCode {
    private static Logger log = Log.getLogger(GenerateJunitCode.class);
    
    public static final String BASE_PACKAGE = "edu.stanford.smi.protegex.owl.code.generation";
    
    public static final String SOURCE_ONTOLOGY_01 = "junit/projects/code.generation/test01/CodeGen.pprj";
    public static final String SOURCE_ONTOLOGY_02 = SOURCE_ONTOLOGY_01;
    public static final String SOURCE_ONTOLOGY_03 = "junit/projects/code.generation/test03/ClassCast.pprj";
    
    private static void configureTest01() throws OntologyLoadException, IOException {
        EditableJavaCodeGeneratorOptions options = new JunitCodeGenerationOptions();
        options.setAbstractMode(false);
        options.setFactoryClassName("Test01Factory");
        options.setOutputFolder(new  File("build/gensrc"));
        options.setPackage(BASE_PACKAGE + ".test01");
        options.setPrefixMode(true);
        options.setSetMode(false);
        
        Collection errors = new ArrayList();
        Project p = new Project(SOURCE_ONTOLOGY_01, errors);
        handleErrors(errors);
        OWLModel owlModel = (OWLModel) p.getKnowledgeBase();
        JavaCodeGenerator generator = new JavaCodeGenerator(owlModel, options);
        generator.createAll();
    }
    
    private static void configureTest02() throws OntologyLoadException, IOException {
        EditableJavaCodeGeneratorOptions options = new JunitCodeGenerationOptions();
        options.setAbstractMode(false);
        options.setFactoryClassName("Test02Factory");
        options.setOutputFolder(new  File("build/gensrc"));
        options.setPackage(BASE_PACKAGE + ".test02");
        options.setPrefixMode(false);
        options.setSetMode(false);
        
        Collection errors = new ArrayList();
        Project p = new Project(SOURCE_ONTOLOGY_02, errors);
        handleErrors(errors);
        OWLModel owlModel = (OWLModel) p.getKnowledgeBase();
        JavaCodeGenerator generator = new JavaCodeGenerator(owlModel, options);
        generator.createAll();
    }

    private static void configureTest03() throws OntologyLoadException, IOException {
        EditableJavaCodeGeneratorOptions options = new JunitCodeGenerationOptions();
        options.setAbstractMode(false);
        options.setFactoryClassName("Factory");
        options.setOutputFolder(new  File("build/gensrc"));
        options.setPackage(BASE_PACKAGE + ".test03");
        options.setPrefixMode(false);
        options.setSetMode(true);
        
        Collection errors = new ArrayList();
        Project p = new Project(SOURCE_ONTOLOGY_03, errors);
        handleErrors(errors);
        OWLModel owlModel = (OWLModel) p.getKnowledgeBase();
        JavaCodeGenerator generator = new JavaCodeGenerator(owlModel, options);
        generator.createAll();
    }
    
    public static void handleErrors(Collection errors) throws OntologyLoadException {
        if (!errors.isEmpty()) {
            for (Object o : errors) {
                if (o instanceof Throwable) {
                    ((Throwable) o).printStackTrace();
                }
                else {
                    System.out.println("Error: " + o);
                }
            }
            throw new OntologyLoadException();
        }
    }
    
    /**
     * @param args
     * @throws IOException 
     * @throws OntologyLoadException 
     */
    public static void main(String[] args) throws OntologyLoadException, IOException {
        log.info("Junit Code Generation");
        log.info("Configuring test01");
        configureTest01();
        log.info("Configuring test02");
        configureTest02();
        log.info("Configuring test03");
        configureTest03();
        log.info("All tests configured");
    }

}
