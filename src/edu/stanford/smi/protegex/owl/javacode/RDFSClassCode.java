package edu.stanford.smi.protegex.owl.javacode;

import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

import java.util.*;

/**
 * An object providing metadata about an RDFSNamedClass
 * or OWLNamedClass, suitable for Java code generation.
 *
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class RDFSClassCode {

    private RDFSNamedClass cls;

    private boolean usePrefix; 


    public RDFSClassCode(RDFSNamedClass cls, boolean usePrefixInNames) {
        this.cls = cls;
        this.usePrefix = usePrefixInNames;
    }


    public String getJavaName() {
		String prefix = cls.getNamespacePrefix();
		if ( usePrefix && prefix != null && (! prefix.equals("")) ) {
			prefix = prefix.toUpperCase() + "_";
			return getValidJavaName(prefix + cls.getLocalName());
		}
        return getValidJavaName(cls.getLocalName());
    }


    /**
     * @return a List of RDFPropertyAtClassCodes
     * @see RDFPropertyAtClassCode
     */
    public List getPropertyCodes(boolean transitive) {
        Set properties = new HashSet();
        List codes = new ArrayList();
        Collection unionDomainProperties = cls.getUnionDomainProperties(transitive);
        Set relevantProperties = new HashSet(unionDomainProperties);
        if (cls instanceof OWLNamedClass) {
            OWLNamedClass owlNamedClass = (OWLNamedClass) cls;
            for (Iterator rit = owlNamedClass.getRestrictions().iterator(); rit.hasNext();) {
                OWLRestriction restriction = (OWLRestriction) rit.next();
                relevantProperties.add(restriction.getOnProperty());
            }
        }
        for (Iterator it = relevantProperties.iterator(); it.hasNext();) {
            RDFProperty property = (RDFProperty) it.next();
            if (property.isSystem()) {
            	//skip system properties
            	continue;
            }
            properties.add(property);
            RDFPropertyAtClassCode code = new RDFPropertyAtClassCode(cls, property, usePrefix);
            codes.add(code);
            Collection subproperties = property.getSubproperties(true);
            Iterator sit = subproperties.iterator();
            while (sit.hasNext()) {
                RDFProperty subproperty = (RDFProperty) sit.next();
                if (!subproperty.isDomainDefined() && !properties.contains(subproperty)) {
                    codes.add(new RDFPropertyAtClassCode(cls, subproperty, usePrefix));
                    properties.add(subproperty);
                }
            }
        }
        Collections.sort(codes);
        return codes;
    }


    public static String getValidJavaName(String name) {
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isJavaIdentifierPart(c)) {
                name = name.replace(c, '_');
            }
        }
        return name;
    }
}
