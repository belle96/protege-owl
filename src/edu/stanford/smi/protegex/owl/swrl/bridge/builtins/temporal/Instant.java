
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal;

import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.exceptions.TemporalException;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 ** Class that represents a single instant in time.
 */
public class Instant 
{
  private Temporal temporal;
  private boolean isNow = false;
  private long granuleCount;
  private int granularity;

  private long granuleCountArray[] = new long[Temporal.NUMBER_OF_GRANULARITIES];

  public Instant(Temporal temporal, long granuleCount, int granularity)
  {
    this.temporal = temporal;
    this.granuleCount = granuleCount;
    this.granularity = granularity;

    clearGranuleCountArray();
  } // Instant

  public Instant(Temporal temporal, Timestamp timestamp) throws TemporalException
  {
    this(temporal, timestamp, Temporal.FINEST);
  } // Instant

  public Instant(Temporal temporal, Timestamp timestamp, int granularity) throws TemporalException
  {
    this.temporal = temporal;
    this.granuleCount = temporal.timestamp2GranuleCount(timestamp, granularity);;
    this.granularity = granularity;

    clearGranuleCountArray();
  } // Instant

  public Instant(Temporal temporal, java.util.Date date) throws TemporalException
  {
    this(temporal, date, Temporal.FINEST);
  } // Instant

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
  } // Instant

  public Instant(Temporal temporal, java.sql.Date date, int granularity) throws TemporalException
  {
    this.temporal = temporal;
    this.granuleCount  = Temporal.sqlDate2GranuleCount(date, granularity);;
    this.granularity = granularity;

    clearGranuleCountArray();
  } // Instant

  public Instant(Temporal temporal, String datetimeString, int granularity) throws TemporalException
  {
    this(temporal, datetimeString, granularity, false);
  } // Instant

  public Instant(Temporal temporal, String datetimeString, int granularity, boolean roundUp) throws TemporalException
  {
    initialize(temporal, datetimeString, granularity, roundUp);

    clearGranuleCountArray();
  } // Instant

  public Instant(Temporal temporal, String datetimeString) throws TemporalException
  {
    this (temporal, datetimeString, false);
  } // Instant

  public Instant(Temporal temporal, String datetimeString, boolean roundUp) throws TemporalException
  {
    initialize(temporal, datetimeString, Temporal.FINEST, roundUp);

    clearGranuleCountArray();
  } // Instant

  public Instant(Temporal temporal, Instant instant) throws TemporalException
  {
    this(temporal, instant.getGranuleCount(instant.getGranularity()), instant.getGranularity());
  } // Instant

  public boolean isNow() { return isNow; }
  public boolean isOngoingInstant() { return isNow(); }
  public int getGranularity() { return granularity; }

  public void setGranularity(int granularity) throws TemporalException
  {
    if (this.granularity == granularity) return;

    if (!isNow) {
      granuleCount = Temporal.convertGranuleCount(granuleCount, this.granularity, granularity);
      clearGranuleCountArray(); // All previous granularity conversion will now be invalid.
    } // if

    this.granularity = granularity;
  } // setGranularity

  public void setGranuleCount(long granuleCount, int granularity)
  {
    isNow = false;  // Can no longer be an ongoing intstant if we explicitly set a granuleCount.

    this.granuleCount = granuleCount;
    this.granularity = granularity;

    clearGranuleCountArray(); // All previous granularity conversion will now be invalid.
  } // setGranularity

  public long getGranuleCount() throws TemporalException
  {
    if (isNow) return getNowGranuleCount(granularity);
    else return granuleCount;
  } // getGranuleCount

  // We use an array to cache the result of granule count conversions for each granularity.
  public long getGranuleCount(int granularity) throws TemporalException
  {
    long resultGranuleCount;

    if (isNow) resultGranuleCount = getNowGranuleCount(granularity);
    else {
      if (getGranularity() != granularity) {

	if (granuleCountArray[granularity] == -1) { // No conversion yet for this granularity.
	  resultGranuleCount = Temporal.convertGranuleCount(granuleCount, getGranularity(), granularity);
	  granuleCountArray[granularity] = resultGranuleCount;
	} else resultGranuleCount = granuleCountArray[granularity];
      } else resultGranuleCount = getGranuleCount(); // Same granularity.
    } // if

    return resultGranuleCount;
  } // getGranuleCount

  public String getDatetimeString() throws TemporalException
  {
      return getDatetimeString(Temporal.FINEST);
  } //  getDatetimeString

  public String getDatetimeString(int granularity) throws TemporalException
  {
    long localGranuleCount;

    if (isNow) localGranuleCount = getNowGranuleCount(granularity);
    else localGranuleCount = getGranuleCount(granularity);

    return temporal.stripDatetimeString(temporal.granuleCount2DatetimeString(localGranuleCount, granularity), granularity);
  } //  getDatetimeString

  public java.util.Date getUtilDate() throws TemporalException
  {
    return getUtilDate(granularity);
  } //  getUtilDate

  public java.util.Date getUtilDate(int granularity) throws TemporalException
  {
    long localGranuleCount;

    if (isNow) localGranuleCount = getNowGranuleCount(granularity);
    else localGranuleCount = granuleCount;

    return Temporal.granuleCount2UtilDate(localGranuleCount, granularity);
  } //  getDatetime

  public java.sql.Date getSQLDate() throws TemporalException
  {
    return getSQLDate(granularity);
  } // getSQLDate

  public java.sql.Date getSQLDate(int granularity) throws TemporalException
  {
    long localGranuleCount;

    if (isNow) localGranuleCount = getNowGranuleCount(granularity);
    else localGranuleCount = granuleCount;

    return Temporal.granuleCount2SQLDate(localGranuleCount, granularity);
  } //  getSQLDate

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

    if (isNow) isNow = false; // Can no longer be a 'now' valid time if we modify it.

    this.granuleCount = getGranuleCount() + plusGranuleCount;

    clearGranuleCountArray();
  } // addGranuleCount

  public void subtractGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    long subtractGranuleCount;

    subtractGranuleCount = Temporal.convertGranuleCount(granuleCount, granularity, this.granularity);

    this.granuleCount -= subtractGranuleCount;

    clearGranuleCountArray();
  } // subtractGranuleCount

  public long duration(Instant i2, int granularity) throws TemporalException
  {
    return java.lang.Math.abs(getGranuleCount(granularity) - i2.getGranuleCount(granularity));
  } // duration

  public boolean before(Instant i2, int granularity) throws TemporalException
  {
    return getGranuleCount(granularity) < i2.getGranuleCount(granularity);
  } // before

  public boolean after(Instant i2, int granularity) throws TemporalException
  {
    return getGranuleCount(granularity) > i2.getGranuleCount(granularity);
  } // after

  public boolean equals(Instant i2, int granularity) throws TemporalException
  {
    return getGranuleCount(granularity) == i2.getGranuleCount(granularity);
  } // equals

  public boolean meets(Instant i2, int granularity) throws TemporalException
  {
    return (((getGranuleCount(granularity) + 1) == i2.getGranuleCount(granularity)) ||
	    (getGranuleCount(granularity) == i2.getGranuleCount(granularity)));
  } // meets

  public boolean met_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.meets(this, granularity);
  } // met_by

  public boolean adjacent(Instant i2, int granularity) throws TemporalException
  {
    return (meets(i2, granularity) || met_by(i2, granularity));
  } // met_by

  public boolean overlaps(Instant i2, int granularity) throws TemporalException
  {
    return false; // Instants cannot overlap.
  } // overlaps

  public boolean overlapped_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.overlaps(this, granularity);
  } // overlapped_by

  public boolean contains(Instant i2, int granularity) throws TemporalException
  {
    return false; // Instant cannot contain another instant.
  } // contains

  public boolean during(Instant i2, int granularity) throws TemporalException
  {
    return false; // Instant cannot be during another instant.
  } // during

  public boolean starts(Instant i2, int granularity) throws TemporalException
  {
    return false; // One instant cannot start another
  } // starts

  public boolean started_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.starts(this, granularity);
  } // started_by

  public boolean finishes(Instant i2, int granularity) throws TemporalException
  {
    return false; // One instant cannot finish another
  } // finish

  public boolean finished_by(Instant i2, int granularity) throws TemporalException
  {
    return i2.finishes(this, granularity);
  } // finished_by

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
  } // coalesce

  private void initialize(Temporal temporal, String datetimeString, int granularity, boolean roundUp) throws TemporalException
  {
    String localDatetimeString;

    this.temporal = temporal;

    if (datetimeString.trim().equalsIgnoreCase("+")) isNow = true;

    // normalizeDatetimeString will deal with +, -, now etc.
    localDatetimeString = temporal.normalizeDatetimeString(datetimeString, granularity, roundUp);
    this.granularity = granularity;

    if (!isNow) {
      localDatetimeString = temporal.expressDatetimeStringAtGranularity(localDatetimeString, granularity);
      granuleCount = temporal.datetimeString2GranuleCount(localDatetimeString, granularity);
    } else granuleCount = -1;
  } // initialize

  private void clearGranuleCountArray()
  {
    for (int i = 0; i < Temporal.NUMBER_OF_GRANULARITIES; i++) granuleCountArray[i] = -1;
  } // clearGranuleCountArray

  protected long getNowGranuleCount(int granularity) throws TemporalException
  {
    return temporal.getNowGranuleCount(granularity);
  } // getNowGranuleCount

} // Instant

