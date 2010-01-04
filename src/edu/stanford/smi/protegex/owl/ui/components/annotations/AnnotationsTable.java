package edu.stanford.smi.protegex.owl.ui.components.annotations;

import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTable;
import edu.stanford.smi.protegex.owl.ui.components.triples.TriplesTableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * A JTable optimized for displaying a AnnotationsTableModel.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 * @author Matthew Horridge <matthew.horridge@cs.man.ac.uk>
 */
public class AnnotationsTable extends TriplesTable {

    private static HashSet singleLineProperties = new HashSet();


    static {
        singleLineProperties.add(RDFSNames.Slot.SEE_ALSO);
        singleLineProperties.add(RDFSNames.Slot.LABEL);
    }


    public AnnotationsTable(Project project, AnnotationsTableModel model) {
        super(project, model, "annotation");
       
        TableColumn valueColumn = getColumnModel().getColumn(TriplesTableModel.COL_VALUE);
        AnnotationsValueRenderer renderer = new AnnotationsValueRenderer();
        
        OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
        
        valueColumn.setCellRenderer(renderer);
        valueColumn.setCellEditor(new AnnotationsValueEditor(owlModel, this));
	    valueColumn.setPreferredWidth(600);
	    
	    TableColumn propertyColumn = getColumnModel().getColumn(AnnotationsTableModel.COL_PROPERTY);
	    propertyColumn.setCellRenderer(renderer);
	    propertyColumn.setPreferredWidth(200);
		
	    // Lang Column (always the last column)
	    TableColumn langColumn = getColumnModel().getColumn(getColumnCount() - 1);
        langColumn.setCellRenderer(renderer);
        langColumn.setCellEditor(new AnnotationsLangEditor(owlModel, this));
        // Set the default row height to be that of a text field
        
        setRowHeight(new JTextField().getPreferredSize().height);
        setGridColor(Color.LIGHT_GRAY);
        setShowGrid(true);
        setIntercellSpacing(new Dimension(1, 1));
    }


    public static Collection getSingleLineProperties() {
        return Collections.unmodifiableCollection(singleLineProperties);
    }


    public static boolean isMultiLineProperty(RDFProperty property) {
        return singleLineProperties.contains(property.getName()) == false;
    }

	
}
