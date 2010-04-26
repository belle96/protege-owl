package edu.stanford.smi.protegex.owl.ui.components.annotations;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.util.LabeledComponent;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.ui.components.triples.AbstractTriplesComponent;
import edu.stanford.smi.protegex.owl.ui.components.triples.AddResourceAction;
import edu.stanford.smi.protegex.owl.ui.components.triples.CreateValueAction;
import edu.stanford.smi.protegex.owl.ui.components.triples.DeleteTripleAction;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTable;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTableModel;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

/**
 * A PropertyWidget to edit the values of annotation properties.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class AnnotationsComponent extends AbstractTriplesComponent {
    private static final long serialVersionUID = -8853232024353780133L;
    private Action deleteRowAction;
    private Action createValueAction;
    private Action todoAction;
    private Action addResourceAction;


    public AnnotationsComponent(RDFProperty predicate) {
    	this(predicate, false);
    }
    
    public AnnotationsComponent(RDFProperty predicate, boolean isReadOnly) {
    	super(predicate, "Annotations", OWLIcons.getImageIcon(OWLIcons.ANNOTATIONS_TABLE), isReadOnly);
    }
    

    protected void addButtons(LabeledComponent lc) {
        createValueAction = new CreateValueAction(getTable(), "Create new annotation value", OWLIcons.getCreateIcon(OWLIcons.ANNOTATION)) {
            private static final long serialVersionUID = 2018303283100762805L;

            @Override
            public void actionPerformed(ActionEvent e) {
                getTableModel().setAllowReadOnlyEdit(true);
                try {
                    super.actionPerformed(e);
                }
                finally {
                    getTableModel().setAllowReadOnlyEdit(false);
                }
            }
            
            @SuppressWarnings("unchecked")
            public Collection getSelectableResources() {
                TriplesTableModel tableModel = table.getTableModel();
                OWLModel owlModel = tableModel.getOWLModel();
                Collection<RDFProperty> properties = new ArrayList();
                Collection<RDFProperty> annotationProperties = owlModel.getOWLAnnotationProperties();
                Collection ontologyProperties = owlModel.getOWLOntologyProperties();
                RDFResource resource = tableModel.getSubject();
                for (RDFProperty property : annotationProperties) {
                    if (ontologyProperties.contains(property)) {
                        if (resource instanceof OWLOntology) {
                            properties.add(property);
                        }
                    }
                    else {
                        RDFSClass type = resource.getProtegeType();
                        Collection domainProperties = type.getUnionDomainProperties(true);
                        if (domainProperties.contains(property)) {
                            boolean zero = resource.getPropertyValues(property, true).isEmpty();
                            if (!property.isFunctional() || zero) {
                                properties.add(property);
                            }
                        }
                    }
                }
                return properties;
            }
        };
        
        lc.addHeaderButton(createValueAction);
        
        todoAction = new AddTodoAction((AnnotationsTable) getTable());
        
        lc.addHeaderButton(todoAction);

        addResourceAction = new AddResourceAction(getTable()) {
            protected Collection getAllowedProperties(OWLModel owlModel) {
                Collection<RDFProperty> allowedProperties = new ArrayList<RDFProperty>();
                for (RDFProperty property : owlModel.getOWLAnnotationProperties()) {
                    allowedProperties.add(property);
                }
                return allowedProperties;
            }
        };
        
        lc.addHeaderButton(addResourceAction);
        deleteRowAction = new DeleteTripleAction(getTable(), "Delete selected annotation", OWLIcons.getDeleteIcon(OWLIcons.ANNOTATION));
        deleteRowAction.setEnabled(false);
        lc.addHeaderButton(deleteRowAction);
    }


    protected TriplesTable createTable(Project project) {
        return new AnnotationsTable(project, (AnnotationsTableModel) getTableModel());
    }


    protected TriplesTableModel createTableModel() {
        return new AnnotationsTableModel();
    }


    protected void updateActions() {
        super.updateActions();
        final int row = getTable().getSelectedRow();
        TriplesTableModel tableModel = getTable().getTableModel();
        boolean deleteRowEnabled = false;
        if (row >= 0) {
            deleteRowEnabled = tableModel.isDeleteEnabled(row);
        }
        deleteRowAction.setEnabled(isEnabled() && deleteRowEnabled);   
    }
    
   
    public void setEnabled(boolean enabled) {    	
    	createValueAction.setEnabled(enabled);
    	addResourceAction.setEnabled(enabled);
    	todoAction.setEnabled(enabled);
    	deleteRowAction.setEnabled(enabled);
    	getTable().setEnabled(enabled);
    	super.setEnabled(enabled);
    };
}
