package edu.stanford.smi.protegex.owl.swrl.ui.table;

public class SWRLRuleGroup implements Comparable<SWRLRuleGroup>
{
  private String groupName = "";
  private Boolean isEnabled = false;
  
  public SWRLRuleGroup() {};
  
  public SWRLRuleGroup(String groupName, Boolean isEnabled)
  {
	  super();
	  this.groupName = groupName;
	  this.isEnabled = isEnabled;
  } // SWRLRuleGroup
		    
  public String getGroupName() { return groupName; }
  public void setGroupName(String groupName) { this.groupName = groupName; }
  public Boolean getIsEnabled() { return isEnabled; }
  public void setIsEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; }
  
  public int hashCode() 
  { 
	  final int PRIME = 31;
	  int result = 1;
	  result = PRIME * result + ((groupName == null) ? 0 : groupName.hashCode());
	  result = PRIME * result + ((isEnabled == null) ? 0 : isEnabled.hashCode());
	  return result;
  }
  
  public boolean equals(Object obj) {
	  if (this == obj) return true;
	  if (obj == null) return false;
	  if (getClass() != obj.getClass()) return false;
	  final SWRLRuleGroup other = (SWRLRuleGroup)obj;
	  if (groupName == null) { if (other.groupName != null)return false;
	  } else if (!groupName.equals(other.groupName)) return false;
	  if (isEnabled == null) { if (other.isEnabled != null) return false;
	  } else if (!isEnabled.equals(other.isEnabled)) return false;
	  return true;
  }
  
  public int compareTo(SWRLRuleGroup otherObject) {
	  int res = 0;
	  res = otherObject.getGroupName().compareTo(getGroupName());
	  return res;
  } 
  
}
