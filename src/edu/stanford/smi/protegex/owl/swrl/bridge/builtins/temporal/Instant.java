
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal;

import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.exceptions.TemporalException;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Class that represents a single instant in time.
 */
public class Instant 
{
  private Temporal temporal;
  private long granuleCount;
  private int granularity;

  private long granuleCountArray[] = new long[Temporal.NUMBER_OF_GRANULARITIES];

  public Instant(Temporal temporal, long granuleCount, int granularity)
  {
    this.temporal = temporal;
    this.granuleCount = granuleCount;
    this.granularity = granularity;

    clearGranuleCountArray();
  }

  public Instant(Temporal temporal, Timestamp timestamp) throws TemporalException
  {
    this(temporal, timestamp, Temporal.FINEST);
  } 

  public Instant(Temporal temporal, Timestamp timestamp, int granularity) throws TemporalException
  {
    this.temporal = temporal;
    this.granuleCount = temporal.timestamp2GranuleCount(timestamp, granularity);;
    this.granularity = granularity;

    clearGranuleCountArray();
  }

  public Instant(Temporal temporal, java.util.Date date) throws TemporalException
  {
    this(temporal, date, Temporal.FINEST);
  }

  public Instant(Temporal temporal, java.util.Date date, int granularity) throws TemporalException
  {
    this.temporal = temporal;
    this.granuleCount = Temporal.utilDate2GranuleCount(date, granularity);
    this.granularity = granularity;

    clearGranuleCountArray();
  } // Instant

  public Instant(Temporal temporal, java.sql.Date date) throws TemporalException
  {
    this(temporal, date, Temporal.FINEST);
  }

  public Instant(Temporal temporal, java.sql.Date date, int granularity) throws TemporalException
  {
    this.temporal = temporal;
    this.granuleCount  = Temporal.sqlDate2GranuleCount(date, granularity);;
    this.granularity = granularity;

    clearGranuleCountArray();
  }

  public Instant(Temporal temporal, String datetimeString, int granularity) throws TemporalException
  {
    this(temporal, datetimeString, granularity, false);
  }

  public Instant(Temporal temporal, String datetimeString, int granularity, boolean roundUp) throws TemporalException
  {
    initialize(temporal, datetimeString, granularity, roundUp);

    clearGranuleCountArray();
  } 

  public Instant(Temporal temporal, String datetimeString) throws TemporalException
  {
    this (temporal, datetimeString, false);
  } 

  public Instant(Temporal temporal, String datetimeString, boolean roundUp) throws TemporalException
  {
    initialize(temporal, datetimeString, Temporal.FINEST, roundUp);

    clearGranuleCountArray();
  }

  public Instant(Temporal temporal, Instant instant) throws TemporalException
  {
    this(temporal, instant.getGranuleCount(instant.getGranularity()), instant.getGranularity());
  }

  public int getGranularity() { return granularity; }

  public void setGranularity(int granularity) throws TemporalException
  {
    if (this.granularity == granularity) return;

    granuleCount = Temporal.convertGranuleCount(granuleCount, this.granularity, granularity);
    clearGranuleCountArray(); // All previous granularity conversion will now be invalid.

    this.granularity = granularity;
  }

  public void setGranuleCount(long granuleCount, int granularity)
  {
    this.granuleCount = granuleCount;
    this.granularity = granularity;

    clearGranuleCountArray(); // All previous granularity conversion will now be invalid.
  }

  public long getGranuleCount() throws TemporalException
  {
  	return granuleCount;
  } 

  // We use an array to cache the result of granule count conversions for each granularity.
  public long getGranuleCount(int granularity) throws TemporalException
  {
    long resultGranuleCount;

    if (getGranularity() != granularity) {

    	if (granuleCountArray[granularity] == -1) { // No conversion yet for this granularity.
    		resultGranuleCount = Temporal.convertGranuleCount(granuleCount, getGranularity(), granularity);
    		granuleCountArray[granularity] = resultGranuleCount;
    	} else resultGranuleCount = granuleCountArray[granularity];
    } else resultGranuleCount = getGranuleCount(); // Same granularity.

    return resultGranuleCount;
  }

  public String getDatetimeString() throws TemporalException
  {
  	return getDatetimeString(Temporal.FINEST);
  }

  public String getDatetimeString(int granularity) throws TemporalException
  {
    long localGranuleCount = getGranuleCount(granularity);

    return temporal.stripDatetimeString(temporal.granuleCount2DatetimeString(localGranuleCount, granularity), granularity);
  } 

  public java.util.Date getUtilDate() throws TemporalException
  {
    return getUtilDate(granularity);
  } 

  public java.util.Date getUtilDate(int granularity) throws TemporalException
  {
    long localGranuleCount = granuleCount;

    return Temporal.granuleCount2UtilDate(localGranuleCount, granularity);
  }

  public java.sql.Date getSQLDate() throws TemporalException
  {
    return getSQLDate(granularity);
  }

  public java.sql.Date getSQLDate(int granularity) throws TemporalException
  {
    long localGranuleCount = granuleCount;

    return Temporal.granuleCount2SQLDate(localGranuleCount, granularity);
  }

  public boolean isStartOfTime() { return (granuleCount == 0); }

  public String toString(int granularity) throws TemporalException { return getDatetimeString(granularity); }

  public String toString()
  {
    String s = "";

    try {
      return toString(Temporal.FINEST);
    } catch (TemporalException e) {
      s = "<INVALID_INSTANT: " + e.toString() + ">";
    } // try

    return s;
  } // toString

  public void addGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    long plusGranuleCount;

    plusGranuleCount = Temporal.convertGranuleCount(granuleCount, granularity, this.granularity);

    this.granuleCount = getGranuleCount() + plusGranuleCount;

    clearGranuleCountArray();
  } 

  public void subtractGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    long subtractGranuleCount;

    subtractGranuleCount = Temporal.convertGranuleCount(granuleCount, granularity, this.granularity);

    this.granuleCount -= subtractGranuleCount;

    clearGranuleCountArray();
  } 

  public long duration(Instant i2, int granularity) throws TemporalException
  {
    return java.lang.Math.abs(getGranuleCount(granularity) - i2.getGranuleCount(granularity));
  } 

  public boolean before(Instant i2, int granularity) throws TemporalException
  {
    return getGranuleCount(granularity) < i2.getGranuleCount(granularity);
  }

  public boolean after(Instant i2, int granularity) throws TemporalException
  {
    return getGranuleCount(granularity) > i2.getGranuleCount(granularity);
  }

  public boolean equals(Instant i2, int granularity) throws TemporalException
  {
    return getGranuleCount(granularity) == i2.getGranuleCount(granularity);
  } 

  public boolean meets(Instant i2, int granularity) throws TemporalException
  {
    return (((getGranuleCount(granularity) + 1) == i2.getGranuleCount(granularity)) ||
	    (getGranuleCount(granularity) == i2.getGranuleCount(granularity)));
  } // meets

  public boolean met_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.meets(this, granularity);
  } 

  public boolean adjacent(Instant i2, int granularity) throws TemporalException
  {
    return (meets(i2, granularity) || met_by(i2, granularity));
  } 

  public boolean overlaps(Instant i2, int granularity) throws TemporalException
  {
    return false; // Instants cannot overlap.
  }

  public boolean overlapped_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.overlaps(this, granularity);
  }

  public boolean contains(Instant i2, int granularity) throws TemporalException
  {
    return false; // Instant cannot contain another instant.
  }

  public boolean during(Instant i2, int granularity) throws TemporalException
  {
    return false; // Instant cannot be during another instant.
  }

  public boolean starts(Instant i2, int granularity) throws TemporalException
  {
    return false; // One instant cannot start another
  }

  public boolean started_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.starts(this, granularity);
  }

  public boolean finishes(Instant i2, int granularity) throws TemporalException
  {
    return false; // One instant cannot finish another
  }

  public boolean finished_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.finishes(this, granularity);
  }

  // Take a list of instants and remove dulicate identical elements.
  public List<Instant> coalesce(List<Instant> instants, int granularity) throws TemporalException
  {
    Instant i1, i2;
    List<Instant> resultList = new ArrayList<Instant>();

    // Loop through each instant in the list trying to merge with other instants.
    while (!instants.isEmpty()) {
      i1 = instants.get(0);
      instants.remove(0); // Remove each instants as we deal with it.

      // See if we can merge this instant with the remaining instants in the list. If we merge this instant with an existing instant later
      // in the list, remove the later element.
      Iterator<Instant> iterator = instants.iterator();
      while (iterator.hasNext()) { 
	    i2 = (Instant)iterator.next();
	    // Merge contiguous or overlapping periods.
	    if (i1.equals(i2, granularity)) {
	      iterator.remove(); // We have merged with instant i2 - remove it.
	    } // if
      } // while
      resultList.add(i1);
    } // while
      
    return resultList;
  }

  private void initialize(Temporal temporal, String datetimeString, int granularity, boolean roundUp) throws TemporalException
  {
    String localDatetimeString;

    this.temporal = temporal;

    if (datetimeString.equals("now")) localDatetimeString = temporal.getNowDatetimeString();
    else localDatetimeString = datetimeString.trim();
    
    localDatetimeString = temporal.normalizeDatetimeString(localDatetimeString, granularity, roundUp);
    
    this.granularity = granularity;

    localDatetimeString = temporal.expressDatetimeStringAtGranularity(localDatetimeString, granularity);
    granuleCount = temporal.datetimeString2GranuleCount(localDatetimeString, granularity);
  }

  private void clearGranuleCountArray()
  {
    for (int i = 0; i < Temporal.NUMBER_OF_GRANULARITIES; i++) granuleCountArray[i] = -1;
  }
} 

