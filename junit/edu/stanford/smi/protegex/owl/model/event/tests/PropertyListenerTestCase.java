package edu.stanford.smi.protegex.owl.model.event.tests;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.event.PropertyAdapter;
import edu.stanford.smi.protegex.owl.model.event.PropertyListener;
import edu.stanford.smi.protegex.owl.tests.AbstractJenaTestCase;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class PropertyListenerTestCase extends AbstractJenaTestCase {
    
    public static class Action {
        private ActionType type;
        private RDFProperty property;
        private Object object;
        
        public Action(ActionType type, RDFProperty property, Object object) {
            super();
            this.type = type;
            this.property = property;
            this.object = object;
        }
        public ActionType getType() {
            return type;
        }
        public RDFProperty getProperty() {
            return property;
        }
        public Object getObject() {
            return object;
        }
        
        public String toString() {
            return "" + type + "[" + property + "," + object + "]";
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((object == null) ? 0 : object.hashCode());
            result = prime * result
                    + ((property == null) ? 0 : property.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }
        
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Action other = (Action) obj;
            if (object == null) {
                if (other.object != null)
                    return false;
            } else if (!object.equals(other.object))
                return false;
            if (property == null) {
                if (other.property != null)
                    return false;
            } else if (!property.equals(other.property))
                return false;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
        
        
    };
    
    private enum ActionType {
        SUB_PROPERTY_ADDED, SUB_PROPERTY_REMOVED, SUPER_PROPERTY_ADDED, SUPER_PROPERTY_REMOVED,
        UNION_CLASS_ADDED, UNION_CLASS_REMOVED;
    };
    
    private List<Action> seenEvents = new ArrayList<Action>();

    private PropertyListener listener = new PropertyAdapter() {
        public void subpropertyAdded(RDFProperty property, RDFProperty subproperty) {
            seenEvents.add(new Action(ActionType.SUB_PROPERTY_ADDED, property, subproperty));
        }


        public void subpropertyRemoved(RDFProperty property, RDFProperty subproperty) {
            seenEvents.add(new Action(ActionType.SUB_PROPERTY_REMOVED, property, subproperty));
        }


        public void superpropertyAdded(RDFProperty property, RDFProperty superproperty) {
            seenEvents.add(new Action(ActionType.SUPER_PROPERTY_ADDED, property, superproperty));
        }


        public void superpropertyRemoved(RDFProperty property, RDFProperty superproperty) {
            seenEvents.add(new Action(ActionType.SUPER_PROPERTY_REMOVED, property, superproperty));
        }


        public void unionDomainClassAdded(RDFProperty property, RDFSClass rdfsClass) {
            seenEvents.add(new Action(ActionType.UNION_CLASS_ADDED, property, rdfsClass));
        }


        public void unionDomainClassRemoved(RDFProperty property, RDFSClass rdfsClass) {
            seenEvents.add(new Action(ActionType.UNION_CLASS_REMOVED, property, rdfsClass));
        }
    };


    public void testEvents() {
        RDFSClass domainClass = owlModel.createRDFSNamedClass("Class");
        RDFProperty property = owlModel.createRDFProperty("property");
        RDFProperty subProperty = owlModel.createRDFProperty("sub");
        property.addPropertyListener(listener);
        subProperty.addPropertyListener(listener);
        
        seenEvents.clear();
        subProperty.addSuperproperty(property);
        assertSize(2, seenEvents);
        assertContains(new Action(ActionType.SUB_PROPERTY_ADDED, property, subProperty), seenEvents);
        assertContains(new Action(ActionType.SUPER_PROPERTY_ADDED, subProperty, property), seenEvents);
        
        seenEvents.clear();
        subProperty.removeSuperproperty(property);
        assertSize(2, seenEvents);
        assertContains(new Action(ActionType.SUB_PROPERTY_REMOVED, property, subProperty), seenEvents);
        assertContains(new Action(ActionType.SUPER_PROPERTY_REMOVED, subProperty, property), seenEvents);
        
        // in the next two the order is significant
        seenEvents.clear();
        property.addUnionDomainClass(domainClass);
        assertSize(2, seenEvents);
        List<Action> expectedEvents = new ArrayList<Action>();
        expectedEvents.add(new Action(ActionType.UNION_CLASS_REMOVED, property, owlModel.getOWLThingClass()));
        expectedEvents.add(new Action(ActionType.UNION_CLASS_ADDED, property, domainClass));
        assertEquals(expectedEvents, seenEvents);
        
        seenEvents.clear();
        property.removeUnionDomainClass(domainClass);
        assertSize(2, seenEvents);
        expectedEvents.clear();
        expectedEvents.add(new Action(ActionType.UNION_CLASS_REMOVED, property, domainClass));
        expectedEvents.add(new Action(ActionType.UNION_CLASS_ADDED, property, owlModel.getOWLThingClass()));
        assertEquals(expectedEvents, seenEvents);
    }
}
