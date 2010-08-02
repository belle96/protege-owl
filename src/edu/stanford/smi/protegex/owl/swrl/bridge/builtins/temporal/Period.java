
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal;

import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.exceptions.TemporalException;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class representing an interval of time.
 */
public class Period
{
  // Both instants will have the same granularity so we do not store the granularity separately.
  private Instant startInstant, finishInstant;
  private Temporal temporal;

  public Period(Temporal temporal, long startGranuleCount, long finishGranuleCount, int granularity) throws TemporalException
  {
    this.temporal = temporal;
    startInstant = new Instant(temporal, startGranuleCount, granularity);
    finishInstant = new Instant(temporal, finishGranuleCount, granularity);

    orderCheck(); // Throws exception if start is after finish.    
  } // Period

  public Period(Temporal temporal, String startDatetime, String finishDatetime, int granularity) throws TemporalException
  {
    this.temporal = temporal;

    semanticCheck(startDatetime, finishDatetime);

    startInstant = new Instant(temporal, startDatetime, granularity, false);
    finishInstant = new Instant(temporal, finishDatetime, granularity, false);
    
    orderCheck(); // Throws exception if start is after finish.
  } 

  public Period(Temporal temporal, String startDatetime, String finishDatetime) throws TemporalException
  {
    this(temporal, startDatetime, finishDatetime, Temporal.FINEST);
  } 

  public Period(Temporal temporal, Period period) throws TemporalException
  {
    this(temporal, period.getStartGranuleCount(period.getGranularity()), period.getFinishGranuleCount(period.getGranularity()), 
         period.getGranularity());
  }

  public Period(Temporal temporal, Instant instant, int granularity) throws TemporalException
  {
    this(temporal, instant, instant, granularity);
  }

  public Period(Temporal temporal, Instant instant) throws TemporalException
  {
    this(temporal, instant, instant, instant.getGranularity());
  }

  public Period(Temporal temporal, Instant startInstant, Instant finishInstant, int granularity) throws TemporalException
  {
    this.startInstant = new Instant(temporal, startInstant);
    this.finishInstant = new Instant(temporal, finishInstant);

    this.startInstant.setGranularity(granularity);
    this.finishInstant.setGranularity(granularity);

    orderCheck();
  }

  public Period(Temporal temporal, Instant startInstant, Instant finishInstant) throws TemporalException
  {
    int granularity;

    this.startInstant = new Instant(temporal, startInstant);
    this.finishInstant = new Instant(temporal, finishInstant);

    // Set granularity to finest of both.
    if (startInstant.getGranularity() > finishInstant.getGranularity()) granularity = startInstant.getGranularity();
    else granularity = finishInstant.getGranularity();

    this.startInstant.setGranularity(granularity);
    this.finishInstant.setGranularity(granularity);

    orderCheck();
  }

  public Period(Temporal temporal, Timestamp startTimestamp, Timestamp finishTimestamp, int granularity) throws TemporalException
  {
    startInstant = new Instant(temporal, startTimestamp, granularity);
    finishInstant = new Instant(temporal, finishTimestamp, granularity);

    orderCheck();
  }

  public Period(Temporal temporal, Timestamp startTimestamp, Timestamp finishTimestamp) throws TemporalException
  {
    this(temporal, startTimestamp, finishTimestamp, Temporal.FINEST);
  }

  public Period(Temporal temporal, java.util.Date startDate, java.util.Date finishDate, int granularity) throws TemporalException
  {
    startInstant = new Instant(temporal, startDate, granularity);
    finishInstant = new Instant(temporal, finishDate, granularity);

    orderCheck();
  }

  public Period(Temporal temporal, java.util.Date startDate, java.util.Date finishDate) throws TemporalException
  {
    this(temporal, startDate, finishDate, Temporal.FINEST);
  }

  public Period(Temporal temporal, java.sql.Date startDate, java.sql.Date finishDate, int granularity) throws TemporalException
  {
    startInstant = new Instant(temporal, startDate, granularity);
    finishInstant = new Instant(temporal, finishDate, granularity);

    orderCheck();
  }

  public Period(Temporal temporal, java.sql.Date startDate, java.sql.Date finishDate) throws TemporalException
  {
    this(temporal, startDate, finishDate, Temporal.FINEST);
  } 

  // Temporal predicates

  public boolean before(Period p2, int granularity) throws TemporalException
  {
    return getFinishGranuleCount(granularity) < p2.getStartGranuleCount(granularity);
  }
  
  public boolean before(Period p2) throws TemporalException
  {
    return getFinishGranuleCount(Temporal.FINEST) < p2.getStartGranuleCount(Temporal.FINEST);
  } 

  public boolean starts_before(Period p2, int granularity) throws TemporalException
  {
    return getStartGranuleCount(granularity) < p2.getStartGranuleCount(granularity);
  } 

  public boolean starts_after(Period p2, int granularity) throws TemporalException
  {
    return getStartGranuleCount(granularity) > p2.getFinishGranuleCount(granularity);
  } 

  public boolean starts_after(Period p2) throws TemporalException
  {
    return getStartGranuleCount(Temporal.FINEST) > p2.getFinishGranuleCount(Temporal.FINEST);
  } 

  public boolean after(Period p2, int granularity) throws TemporalException
  {
    return getStartGranuleCount(granularity) > p2.getFinishGranuleCount(granularity);
  } 

  public boolean equals(Period p2, int granularity) throws TemporalException
  {
    return ((getStartGranuleCount(granularity) == p2.getStartGranuleCount(granularity) &&
	    (getFinishGranuleCount(granularity) == p2.getFinishGranuleCount(granularity))));
  } 

  public boolean meets(Period p2) throws TemporalException
  {
    return meets(p2, Temporal.FINEST);
  } 

  public boolean meets(Period p2, int granularity) throws TemporalException
  {
    return ((getFinishGranuleCount(granularity) + 1) == p2.getStartGranuleCount(granularity));
  } 

  public boolean met_by(Period p2) throws TemporalException
  {
    return met_by(p2, Temporal.FINEST);
  } 

  public boolean met_by(Period p2, int granularity) throws TemporalException
  {
    return p2.meets(this, granularity);
  } 

  public boolean overlaps(Period p2) throws TemporalException
  {
    return overlaps(p2, Temporal.FINEST);
  } 

  public boolean overlaps(Period p2, int granularity) throws TemporalException
  {
    return ((getStartGranuleCount(granularity) <= p2.getStartGranuleCount(granularity)) &&
	    (getFinishGranuleCount(granularity) <= p2.getFinishGranuleCount(granularity)) &&
	    (getFinishGranuleCount(granularity) >= p2.getStartGranuleCount(granularity)));

  } 

  public boolean overlapped_by(Period p2) throws TemporalException
  {
    return overlapped_by(p2, Temporal.FINEST);
  } 

  public boolean overlapped_by(Period p2, int granularity) throws TemporalException
  {
    return p2.overlaps(this, granularity);
  } 

  public boolean during(Period p2) throws TemporalException
  {
    return during(p2, Temporal.FINEST);
  } 

  public boolean during(Period p2, int granularity) throws TemporalException
  {
    return p2.contains(this, granularity) && !p2.equals(this, granularity);
  }

  public boolean contains(Period p2) throws TemporalException
  {
    return contains(p2, Temporal.FINEST);
  }

  public boolean contains(Period p2, int granularity) throws TemporalException
  {
    return (((p2.getStartGranuleCount(granularity) > getStartGranuleCount(granularity)) ||
	     (p2.getStartGranuleCount(granularity) == getStartGranuleCount(granularity))) &&
	    ((p2.getFinishGranuleCount(granularity) < getFinishGranuleCount(granularity)) ||
	     (p2.getFinishGranuleCount(granularity) == getFinishGranuleCount(granularity))));

  }

  public boolean starts(Period p2) throws TemporalException
  {
    return starts(p2, Temporal.FINEST);
  }

  public boolean starts(Period p2, int granularity) throws TemporalException
  {
    return ((getStartGranuleCount(granularity) == p2.getStartGranuleCount(granularity)) &&
	    (getFinishGranuleCount(granularity) < p2.getFinishGranuleCount(granularity)));

  }

  public boolean started_by(Period p2) throws TemporalException
  {
    return started_by(p2, Temporal.FINEST);
  }

  public boolean started_by(Period p2, int granularity) throws TemporalException
  {
    return p2.starts(this, granularity);
  } 

  public boolean finishes(Period p2) throws TemporalException
  {
    return finishes(p2, Temporal.FINEST);
  }

  public boolean finishes(Period p2, int granularity) throws TemporalException
  {
    return ((getStartGranuleCount(granularity) < p2.getStartGranuleCount(granularity)) &&
	    (getFinishGranuleCount(granularity) == p2.getFinishGranuleCount(granularity)));

  }

  public boolean finished_by(Period p2) throws TemporalException
  {
    return finished_by(p2, Temporal.FINEST);
  }

  public boolean finished_by(Period p2, int granularity) throws TemporalException
  {
    return p2.finishes(this, granularity);
  }

  public boolean adjacent(Period p2) throws TemporalException
  {
    return adjacent(p2, Temporal.FINEST);
  }

  public boolean adjacent(Period p2, int granularity) throws TemporalException
  {
    return (meets(p2, granularity) || met_by(p2, granularity));
  }

  public boolean intersects(Period p2) throws TemporalException
  {
    return intersects(p2, Temporal.FINEST);
  }

  public boolean intersects(Period p2, int granularity) throws TemporalException
  {
    return (intersection(p2, granularity) != null);
  }

  public Period intersection(Period p2) throws TemporalException
  {
    return intersection(p2, Temporal.FINEST);
  }

  public Period intersection(Period p2, int granularity) throws TemporalException
  {
    Period result;

    if (startInstant.equals(p2.getStartInstant(), granularity)) { // They must intersect
      if (finishInstant.after(p2.getFinishInstant(), granularity)) {
      	result = new Period(temporal, startInstant, p2.getFinishInstant(), granularity);
      } else {
      	result = new Period(temporal, startInstant, finishInstant, granularity);
      } // if
    } else if (startInstant.before(p2.getStartInstant(), granularity)) { // p2 starts after this Period
    	if (finishInstant.after(p2.getStartInstant(), granularity)) { // They intersect
    		if (finishInstant.before(p2.getFinishInstant(), granularity)) {
    			result = new Period(temporal, p2.getStartInstant(), finishInstant, granularity);
    		} else {
    			result = new Period(temporal, p2.getStartInstant(), p2.getFinishInstant(), granularity);
    		} // if
    	} else {
    		result = null;
    	} // if
    } else { // p2 start before this Period
    	if (p2.getFinishInstant().after(startInstant, granularity)) { // They intersect
    		if (finishInstant.before(p2.getFinishInstant(), granularity)) {
    			result = new Period(temporal, startInstant, finishInstant, granularity);
    		} else {
    			result = new Period(temporal, startInstant, p2.getFinishInstant(), granularity);
    		} // if
    	} else {
    		result = null;
    	} // if
    } // if

    return result;
  } 

  public Period merge(Period p2) throws TemporalException
  {
    return merge(p2, Temporal.FINEST);
  } // merge

  public Period merge(Period p2, int granularity) throws TemporalException
  {
    Instant resultStartInstant, resultFinishInstant;
    Period result;

    if ((intersection(p2, granularity) == null) && (!adjacent(p2, granularity))) 
      throw new TemporalException("start must be before or equal to the finish in a period: ('" +
				  toString(granularity) + "'), ('" + p2.toString(granularity) + "')");

    
    if (startInstant.before(p2.getStartInstant(), granularity)) resultStartInstant = startInstant;
    else resultStartInstant = p2.getStartInstant();
    
    if (getFinishInstant().before(p2.getFinishInstant(), granularity)) resultFinishInstant = p2.getFinishInstant();
    else resultFinishInstant = finishInstant;
    
    result = new Period(temporal, resultStartInstant, resultFinishInstant, granularity);

    return result;
  } 

  private void orderCheck() throws TemporalException
  {
    if (startInstant.after(finishInstant, Temporal.FINEST)) 
      throw new TemporalException("start must be before or equal to finish in a period: (" +
				  startInstant.toString(Temporal.FINEST) + ", " + finishInstant.toString(Temporal.FINEST) + ")");

  } 

  public long duration(int granularity) throws TemporalException
  {
    return startInstant.duration(finishInstant, granularity);
  } 

  // Take a list of periods (which will come from a value-equivalent row) and merge any of the periods that overlap or are adjacent to each
  // other, producing a new period list.

  // This routine modifies the periods list that it has been passed so this list should not be used again.
  public List<Period> coalesce(List<Period> periods, int granularity) throws TemporalException
  {
    Iterator<Period> iterator;
    Period p1, p2;
    boolean periodMerged;
    List<Period> resultList = new ArrayList<Period>();

    // Loop through each period in the list trying to merge with other periods.
    while (!periods.isEmpty()) {
      p1 = (Period)periods.get(0);
      periods.remove(0); // Remove each period as we deal with it.

      // See if we can merge this period with the remaining periods in the list. If we merge this period with an existing period we must
      // then scan through the remainder of the period list again from the beginning to see if this new merged period can be merged with
      // other periods and so on.String
      do {
      	periodMerged = false;
      	iterator = periods.iterator();
      	while (!periodMerged && iterator.hasNext()) { 
      		p2 = (Period)iterator.next();
      		// Merge contiguous or overlapping periods.
      		if ((p1.intersects(p2, granularity))
      				|| (p1.adjacent(p2, granularity))) {
      			p1 = p1.merge(p2, granularity);
      			iterator.remove(); // We have merged with period p2 - remove it.
      			periodMerged = true;
      		} // if
      	} // while
      } while (periodMerged);
      resultList.add(p1);
    } // while
      
    return resultList;
  }

  public void addGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    startInstant.addGranuleCount(granuleCount, granularity);
    finishInstant.addGranuleCount(granuleCount, granularity);
  }

  public void subtractGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    startInstant.subtractGranuleCount(granuleCount, granularity);
    finishInstant.subtractGranuleCount(granuleCount, granularity);
  }

  public void addStartGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    startInstant.addGranuleCount(granuleCount, granularity);
  }

  public void addFinishGranuleCount(long granuleCount, int granularity)
    throws TemporalException
  {
    finishInstant.addGranuleCount(granuleCount, granularity);
  } 

  public void subtractStartGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    startInstant.subtractGranuleCount(granuleCount, granularity);
  }

  public void subtractFinishGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    finishInstant.subtractGranuleCount(granuleCount, granularity);
  } 
  
  public Period coalesce(Period p2, int granularity) throws TemporalException
  {
    Period result = null;

    if (intersects(p2, granularity) || adjacent(p2, granularity)) result = merge(p2, granularity);

    return result;
  } // coalesce

  public void setStartInstant(Instant instant) throws TemporalException
  {
    startInstant = new Instant(temporal, instant);
    // Both instants must share same granularity.
    finishInstant.setGranularity(instant.getGranularity());
  }

  public void setFinishInstant(Instant instant) throws TemporalException
  {
    finishInstant = new Instant(temporal, instant);
    // Both instants must share same granularity.
    startInstant.setGranularity(instant.getGranularity());
  }

  public String getStartDatetimeString() throws TemporalException
  {
    return startInstant.getDatetimeString();
  }

  public java.util.Date getStartDate() throws TemporalException
  {
    return startInstant.getUtilDate();
  } 

  public Instant getStartInstant() { return startInstant; }

  public String getStartDatetimeString(int granularity) throws TemporalException
  {
    return startInstant.getDatetimeString(granularity);
  } 

  public java.util.Date getStartDate(int granularity) throws TemporalException  
  {
    return startInstant.getUtilDate(granularity);
  } 

  public String getFinishDatetime() throws TemporalException
  {
    return finishInstant.getDatetimeString();
  } 

  public java.util.Date getFinishDate() throws TemporalException
  {
    return finishInstant.getUtilDate();
  } 

  public Instant getFinishInstant() { return finishInstant; }

  public String getFinishDatetime(int granularity) throws TemporalException
  {
    return finishInstant.getDatetimeString(granularity);
  } 

  public java.util.Date getFinishDate(int granularity) throws TemporalException
  {
    return finishInstant.getUtilDate(granularity);
  } 

  public long getStartGranuleCount(int granularity) throws TemporalException
  {
    return startInstant.getGranuleCount(granularity);
  }

  public long getFinishGranuleCount(int granularity) throws TemporalException
  {
    return finishInstant.getGranuleCount(granularity);
  } 

  public boolean couldBeInstant() throws TemporalException
  {
    return startInstant.equals(finishInstant, Temporal.FINEST);
  } 

  public String toString(int granularity) throws TemporalException
  {
    return "(" + startInstant.toString(granularity) + ", " + finishInstant.toString(granularity) + ")";
  } // toString

  public String toString()
  {
    String s = "";

    try {
      return toString(Temporal.FINEST);
    } catch (TemporalException e) {}

    return s;
  } // toString

  public int getGranularity()
  {
    return startInstant.getGranularity(); // start and finish instant have same granularity.
  } 

  // We do not have to signal 'now' as an invalid start time value here because it is an allowed value. The orderCheck() method will check
  // for the invalid ordering of timestamps that a 'now' value *could* generate.
  private void semanticCheck(String startDatetime, String finishDatetime) throws TemporalException
  {
    if (startDatetime.equals("+")) throw new TemporalException("'+' cannot be used at the start of a period");

    if (finishDatetime.equals("-")) throw new TemporalException("'-' cannot be used at the end of a period");
  }     

} 
