
package edu.stanford.smi.protegex.owl.swrl.sqwrl.ui.icons;

import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;

import javax.swing.*;

public class QueryIcons 
{
  public static ImageIcon getQueryIcon() { return getImageIcon("SQWRL"); }
  
  public static ImageIcon getImageIcon(String name) {  return OWLIcons.getImageIcon(name, QueryIcons.class); }
} // QueryIcons
