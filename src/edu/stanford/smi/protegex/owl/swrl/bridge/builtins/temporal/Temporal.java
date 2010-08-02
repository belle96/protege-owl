
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.exceptions.TemporalException;

/** 
 * A class that supports temporal operations using the Gregorian calendar. In instance of this class is supplied with a
 * DatetimeStringProcessor that governs how timestamps are converted to and from datetime strings. Apart from the granularity constants,
 * users should generally not use this class directly but should instead use the Instant and Period classes in this package.
 */
public class Temporal
{
  public final static int MILLISECONDS = 6;
  public final static int SECONDS = 5;
  public final static int MINUTES = 4;
  public final static int HOURS = 3;
  public final static int DAYS = 2;
  public final static int MONTHS = 1;
  public final static int YEARS = 0;

  public final static int FINEST = MILLISECONDS;
  public final static int COARSEST = YEARS;

  public final static int NUMBER_OF_GRANULARITIES = 7;

  private long nowGranuleCountInMillis = -1;

  private DatetimeStringProcessor datetimeStringProcessor = null;

  // Number of milliseconds to January 1st 1970 from January 1st 1 C.E.
  public static final long millisecondsTo1970 = 62167363200000L;

  // Number of milliseconds to Gregorian change date of October 15th, 1582 from January 1st 1 C.E.
  public static final long millisecondsToGregorianChangeDate = 12219292800000L;

  // 10-day discontinuity between October 4, 1582 and October 15, 1582.
  public static final long millisecondsInGregorianDiscontinuity = 864000000L; // 10 * 24 * 60 * 60 * 1000L;
  
  // Number of milliseconds in one hour
  public static final long daylightSavingsTimeOffsetInMillis = 60 * 60 * 1000;

  // The following table is used to convert an integral number of granules at one granularity (the y axis) to an integral number of granules
  // at another granularity (the x axis). If the source granularity is finer than the target granularity we divide by the number in the
  // appropriate entry; if it is coarser, we multiply by the number in the entry.  e.g., 10 days will be converted to 10*(24*60*60) seconds;
  // 1000 seconds will be converted to 1000/(60) minutes. Months are a special case and individual routines will deal with them
  // separately. Leap years are also handled separately.
  private static final long conversion_table[][] = {
    /* year,              month,            day,           hours,      minutes,    seconds,      milliseconds */ 
    {  1,                 12,               365,           365*24,     365*24*60,  365*24*60*60, 365*24*60*60*1000L }, /* year */
    {  0,                  1,               0,             0,          0,          0,            0                  }, /* month */ 
    {  365,                0,               1,             24,         24*60,      24*60*60,     24*60*60*1000      }, /* day */ 
    {  365*24,             0,               24,            1,          60,         60*60,        60*60*1000         }, /* hour */
    {  365*24*60,          0,               24*60,         60,         1,          60,           60*1000            }, /* minute */
    {  365*24*60*60,       0,               24*60*60,      60*60,      60,         1,            1000               }, /* second */ 
    {  365*24*60*60*1000L, 0,               24*60*60*1000, 60*60*1000, 60*1000,    1000,         1                  }  /* mseconds */
  };

  private static final String[] stringGranularityRepresentation = { "years","months","days","hours","minutes","seconds","milliseconds" };

  // Individual routines will adjust February for leap years.
  private static final long[] days_in_month = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

  // Days up until a month. Routine convertMonth2Granularity will adjust for leap years.
  private static final long[] days_to_month = {
    0, // to January
    31, // to February
    31 + 28, // to March. 
    31 + 28 + 31, // to April etc.
    31 + 28 + 31 + 30, // to May
    31 + 28 + 31 + 30 + 31, // to June
    31 + 28 + 31 + 30 + 31 + 30, // to July
    31 + 28 + 31 + 30 + 31 + 30 + 31, // to August
    31 + 28 + 31 + 30 + 31 + 30 + 31 + 31, // to September
    31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30, // to October
    31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31, // to November
    31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30, // to December
    365
  };

  public Temporal(DatetimeStringProcessor _datetimeStringProcessor)
  {
    this.datetimeStringProcessor = _datetimeStringProcessor;
  } 

  public void setNow(String nowDatetimeString) throws TemporalException
  {
    nowGranuleCountInMillis = datetimeString2GranuleCount(nowDatetimeString, MILLISECONDS);
  } 

  public void setNow() throws TemporalException
  {
	  long millisecondsFrom1970 = System.currentTimeMillis();
	  Timestamp ts = new Timestamp(millisecondsFrom1970);
	  
	  nowGranuleCountInMillis = timestamp2GranuleCount(ts, MILLISECONDS);
  }

  public void checkGranularity(String granularity) throws TemporalException
  {
    getIntegerGranularityRepresentation(granularity); // Will throw an exception if it cannot convert the granularity.
  } 

  public static void checkMonthCount(long monthCount) throws TemporalException
  {
    if ((monthCount < 1) || (monthCount > 12)) throw new TemporalException("invalid month count #" + monthCount);
  } // if

  public static int getIntegerGranularityRepresentation(String granularity) throws TemporalException
  {
    if (granularity.equals("") || granularity.equalsIgnoreCase("finest")) return FINEST;
    else if (granularity.equalsIgnoreCase("coarsest")) return COARSEST;
    else { 
      int i = 0;
      while (i < stringGranularityRepresentation.length) {
      	if (stringGranularityRepresentation[i].equalsIgnoreCase(granularity)) return i;
      	i++;
      } // while
    } // if

    throw new TemporalException("invalid granularity '" + granularity + "'");
  }

  public static boolean isValidGranularityString(String granularity)
  {
    int i = 0;
    boolean found = false;

    while (i < stringGranularityRepresentation.length && !found) {
      if (stringGranularityRepresentation[i].equalsIgnoreCase(granularity)) found = true;
      i++;
    } // while

    return found;
  } 

  public static String getStringGranularityRepresentation(int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return stringGranularityRepresentation[granularity];
  } 

  /**
   * Take a granule count (from the beginning of calendar time, e.g., '0000-01-01 00:00:00.000' in JDBC timestamp format) at any
   * granularity and convert it to a Timestamp. Java Timestamps record time as milliseconds from January 1st 1970.
   * 
   * java.sql.timestamp will take car of the time zone offset plus daylight savings time.
   */
  public static java.sql.Timestamp granuleCount2Timestamp(long granuleCount, int granularity) throws TemporalException
  {
    long granuleCountInMilliSeconds;

    checkGranularity(granularity);
 
    granuleCountInMilliSeconds = convertGranuleCount(granuleCount, granularity, MILLISECONDS);

    if (granuleCountInMilliSeconds > millisecondsToGregorianChangeDate) granuleCountInMilliSeconds -= millisecondsInGregorianDiscontinuity;

    // The java.sql.Timestamp constructor will correctly deal with negative milliseconds.
    granuleCountInMilliSeconds -= millisecondsTo1970;

    return new java.sql.Timestamp(granuleCountInMilliSeconds);
  } 

  /**
   ** Take a timestamp and return the number of granules at the specified granularity since 1 C.E.
   */
  public long timestamp2GranuleCount(Timestamp timestamp, int granularity) throws TemporalException
  {
    GregorianCalendar gc = new GregorianCalendar();
    long years, months, days, hours, minutes, seconds, milliseconds;
    long resultGranuleCount;

    checkGranularity(granularity);

    gc.setTime(timestamp);

    years = gc.get(Calendar.YEAR);
    months = gc.get(Calendar.MONTH);
    days = gc.get(Calendar.DAY_OF_MONTH) - 1;
    hours = gc.get(Calendar.HOUR_OF_DAY);
    minutes = gc.get(Calendar.MINUTE);
    seconds = gc.get(Calendar.SECOND);
    milliseconds = gc.get(Calendar.MILLISECOND);    

    resultGranuleCount = convertGranuleCount(years, YEARS, granularity) + convertGranuleCount(months, MONTHS, granularity) +
      convertGranuleCount(days, DAYS, granularity) + convertGranuleCount(hours, HOURS, granularity) +
      convertGranuleCount(minutes, MINUTES, granularity) + convertGranuleCount(seconds, SECONDS, granularity) +
      convertGranuleCount(milliseconds, MILLISECONDS, granularity);

    if ((months > 1) && isLeapYear(years)) resultGranuleCount += conversion_table[granularity][DAYS];

    return resultGranuleCount;
  } 

  /**
   * Take a full specification datetime and return the number of granules at the specified granularity since 1 C.E.
   */
  public long datetimeString2GranuleCount(String datetimeString, int granularity) throws TemporalException
  {
    long years, months, days, hours, minutes, seconds, milliseconds;
    long resultGranuleCount;

    checkGranularity(granularity);

    years = datetimeStringProcessor.getYears(datetimeString);
    if(years < 1) throw new TemporalException("years must be 1 or greater in datetime: " + datetimeString);
        
    months = datetimeStringProcessor.getMonths(datetimeString) - 1;
    if(months < 0) throw new TemporalException("months must be 1 or greater in datetime: " + datetimeString);
    
    days = datetimeStringProcessor.getDays(datetimeString) - 1;
    if(days < 0) throw new TemporalException("days must be 1 or greater in datetime: " + datetimeString);
    
    hours = datetimeStringProcessor.getHours(datetimeString);
    if(hours < 0) throw new TemporalException("hours must be 0 or greater in datetime: " + datetimeString);
    
    minutes = datetimeStringProcessor.getMinutes(datetimeString);
    if(minutes < 0) throw new TemporalException("minutes must be 0 or greater in datetime: " + datetimeString);
    
    seconds = datetimeStringProcessor.getSeconds(datetimeString);
    if(seconds < 0) throw new TemporalException("seconds must be 0 or greater in datetime: " + datetimeString);
    
    milliseconds = datetimeStringProcessor.getMilliseconds(datetimeString);
    if(milliseconds < 0) throw new TemporalException("milliseconds must be 0 or greater in datetime: " + datetimeString);

    resultGranuleCount = convertGranuleCount(years, YEARS, granularity) + convertGranuleCount(months, MONTHS, granularity) +
      convertGranuleCount(days, DAYS, granularity) + convertGranuleCount(hours, HOURS, granularity) +
      convertGranuleCount(minutes, MINUTES, granularity) + convertGranuleCount(seconds, SECONDS, granularity) +
      convertGranuleCount(milliseconds, MILLISECONDS, granularity);

    if ((months > 1) && isLeapYear(years)) resultGranuleCount += conversion_table[granularity][DAYS]; // 1 == February

    return resultGranuleCount;
  } // datetimeString2GranuleCount

  /**
   * Convert a granule count from one granularity to another. 
   */
  public static long convertGranuleCount(long granuleCount, int from_granularity, int to_granularity) throws TemporalException
  {    long result, localGranuleCount, leapOffsetGranuleCount = 0;

    checkGranularity(from_granularity);
    checkGranularity(to_granularity);

    if (from_granularity == to_granularity) return granuleCount;

    localGranuleCount = granuleCount;

    // If we are converting from a granularity of days or finer to months or years, we need to account for the extra day's worth of granules
    // in each leap year up to that point in time.
    if ((from_granularity >= DAYS) && (to_granularity <= MONTHS)) {
      leapOffsetGranuleCount = - leapYearsUpToGranuleCount(localGranuleCount, from_granularity) * conversion_table[DAYS][from_granularity];
      localGranuleCount += leapOffsetGranuleCount;
    } // if

    // Months must be dealt with separately.
    if (from_granularity == MONTHS) result = convertMonthCount2GranuleCount(localGranuleCount, to_granularity);
    else if (to_granularity == MONTHS) result = convertGranuleCount2MonthCount(localGranuleCount, from_granularity);
    else if (from_granularity > to_granularity) result = localGranuleCount / conversion_table[from_granularity][to_granularity];
    else if (from_granularity < to_granularity) result = localGranuleCount * conversion_table[from_granularity][to_granularity];
    else  result = localGranuleCount;

    // If we are converting from a granularity of years or months to a granularity of days or finer, we need to account for the extra day's
    // worth of granules in each leap year up to that point in time.
    if ((from_granularity <= MONTHS) && (to_granularity >= DAYS)) {
      if (from_granularity == YEARS) leapOffsetGranuleCount = leapGranulesUpToYear(granuleCount, to_granularity);
      else leapOffsetGranuleCount = leapGranulesUpToMonth(granuleCount, to_granularity); // MONTHS
      result += leapOffsetGranuleCount;
    } // if

    return result;
  } 

  public static long getDaysInMonth(long monthCount) throws TemporalException
  {
    checkMonthCount(monthCount);

    return days_in_month[(int)monthCount - 1];
  } 

  public static boolean isLeapYear(long yearCount) 
  { 
    GregorianCalendar gc = new GregorianCalendar();

    return gc.isLeapYear((int)yearCount); 
  } 

  // We ignore leap years here - convertGranuleCount adjusts for them.
  public static long convertGranuleCount2MonthCount(long granuleCount, int from_granularity) throws TemporalException
  {
    long monthCount;

    if (from_granularity == YEARS) monthCount = granuleCount * conversion_table[YEARS][MONTHS];
    else if (from_granularity == MONTHS) monthCount = granuleCount;
    else { // DAY or finer
      long dayCount = granuleCount / conversion_table[from_granularity][DAYS];
      long yearCount =  dayCount / 365;
      monthCount = yearCount * 12;
      long dayInYear = dayCount % 365;

      int i = 1;
      while (days_to_month[i] <= dayInYear) {
      	monthCount++; 
      	i++;
      } // while
    } // if

    return monthCount;
  } 

  // We ignore leap years here - convertGranuleCount adjusts for them.

  public static java.util.Date sqlDate2UtilDate(java.sql.Date sqlDate, int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return granuleCount2UtilDate(sqlDate2GranuleCount(sqlDate, granularity), granularity);
  }

  public static java.util.Date sqlDate2UtilDate(java.sql.Date sqlDate) throws TemporalException
  {
    return sqlDate2UtilDate(sqlDate, FINEST);
  }

  public static long sqlDate2GranuleCount(java.sql.Date date, int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return utilDate2GranuleCount(sqlDate2UtilDate(date, granularity), granularity);
  }

  public static long utilDate2GranuleCount(java.util.Date date, int granularity) throws TemporalException
  {
    GregorianCalendar gc = new GregorianCalendar();
    long localGranuleCountInMilliseconds = 0;
    long millisecondsInTimeZoneOffset = 0; // TODO: gc.getZoneOffset();

    checkGranularity(granularity);

    // Is date is after Gregorian change date?
    if (date.compareTo(gc.getGregorianChange()) > 0) localGranuleCountInMilliseconds += millisecondsInGregorianDiscontinuity;

    localGranuleCountInMilliseconds += millisecondsTo1970;
    localGranuleCountInMilliseconds += millisecondsInTimeZoneOffset;
    // The getTime() function returns the mumber of milliseconds since 1970-01-01 GMT. It returns a negative number for dates before that.
    localGranuleCountInMilliseconds += date.getTime();

    return convertGranuleCount(localGranuleCountInMilliseconds, MILLISECONDS, granularity);
  }

  public java.sql.Date utilDate2SQLDate(java.util.Date date) throws TemporalException
  {
    if (date instanceof java.sql.Date) return (java.sql.Date)date;

    return datetimeString2SQLDate(utilDate2DatetimeString(date));
  }

  public static java.util.Date addGranuleCount(java.util.Date date, long granuleCount, int granularity) throws TemporalException
  {
    long resultGranuleCount;

    checkGranularity(granularity);

    resultGranuleCount = utilDate2GranuleCount(date, granularity) + granuleCount;

    return granuleCount2UtilDate(resultGranuleCount, granularity);
  } 

  public static java.util.Date subtractGranuleCount(java.util.Date date, long granuleCount, int granularity) throws TemporalException
  {
    long resultGranuleCount;

    checkGranularity(granularity);

    resultGranuleCount = utilDate2GranuleCount(date, granularity) - granuleCount;

    return granuleCount2UtilDate(resultGranuleCount, granularity);
  } 

  public java.sql.Date getNowSQLDate() throws TemporalException
  {
    return java.sql.Date.valueOf(getNowDatetimeString());
  } 

  public java.util.Date getNowUtilDate() throws TemporalException
  {
    return granuleCount2UtilDate(nowGranuleCountInMillis, FINEST);
  } 

  public long getNowGranuleCount(int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return convertGranuleCount(nowGranuleCountInMillis, MILLISECONDS, granularity);
  } 

  public static java.util.Date granuleCount2UtilDate(long granuleCount, int granularity) throws TemporalException
  {
    long granuleCountInMilliseconds;

    checkGranularity(granularity);
 
    granuleCountInMilliseconds  = convertGranuleCount(granuleCount, granularity, MILLISECONDS); // Also checks granularity for sanity.

    if (granuleCountInMilliseconds > millisecondsToGregorianChangeDate) granuleCountInMilliseconds -= millisecondsInGregorianDiscontinuity;

    // The java.sql.Timestamp constructor will correctly deal with negative milliseconds.
    granuleCountInMilliseconds -= millisecondsTo1970;

    return new java.util.Date(granuleCountInMilliseconds);
  } 

  public static java.sql.Date granuleCount2SQLDate(long granuleCount, int granularity) throws TemporalException
  {
    long granuleCountInMilliseconds;

    checkGranularity(granularity);
    
    granuleCountInMilliseconds = convertGranuleCount(granuleCount, granularity, MILLISECONDS);

    if (granuleCountInMilliseconds > millisecondsToGregorianChangeDate) granuleCountInMilliseconds -= millisecondsInGregorianDiscontinuity;

    // The java.sql.Timestamp constructor will correctly deal with negative milliseconds.
    granuleCountInMilliseconds -= millisecondsTo1970;

    return new java.sql.Date(granuleCountInMilliseconds);
  }

  public String getNowDatetimeString() throws TemporalException
  {
    return granuleCount2DatetimeString(nowGranuleCountInMillis, FINEST);
  }

  public String normalizeDatetimeString(String datetimeString, int granularity, boolean roundUp) throws TemporalException
  {
    return datetimeStringProcessor.normalizeDatetimeString(datetimeString, granularity, roundUp);
  } 

  public String normalizeDatetimeString(String datetimeString, int granularity) throws TemporalException
  {
    return normalizeDatetimeString(datetimeString, granularity, false);
  } // normalizeDatetimeString

  public String stripDatetimeString(String datetimeString, int granularity) throws TemporalException
  {
    return datetimeStringProcessor.stripDatetimeString(datetimeString, granularity);
  } // stripDatetimeString

  public String expressDatetimeStringAtGranularity(String datetimeString, int granularity, boolean roundUp) throws TemporalException
  {
    return datetimeStringProcessor.expressDatetimeStringAtGranularity(datetimeString, granularity, roundUp);
  } // expressDatetimeStringAtGranularity

  public String expressDatetimeStringAtGranularity(String datetimeString, int granularity) throws TemporalException
  {
    return expressDatetimeStringAtGranularity(datetimeString, granularity, false);
  } // expressDatetimeStringAtGranularity

  // Take a java.sql.Date object and convert it to a datetime string at the specified granularity.
  public String sqlDate2DatetimeString(java.sql.Date date, int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return granuleCount2DatetimeString(sqlDate2GranuleCount(date, granularity), granularity);
  } // sqlDate2DatetimeString

  public String sqlDate2DatetimeString(java.sql.Date date) throws TemporalException
  {
    return datetimeStringProcessor.granuleCount2DatetimeString(sqlDate2GranuleCount(date, FINEST), FINEST);
  } // sqlDate2DatetimeString

  // Take a java.util.Date object and convert it to a datetime string at the specified granularity.
  public String utilDate2DatetimeString(java.util.Date date, int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return granuleCount2DatetimeString(utilDate2GranuleCount(date, granularity), granularity);
  } // utilDate2DatetimeString

  public String utilDate2DatetimeString(java.util.Date date) throws TemporalException
  {
    return utilDate2DatetimeString(date, FINEST);
  } // utilDate2DatetimeString

  // Take a datetime string and convert it to a java.sql.Date object at the specified granularity.
  public java.sql.Date datetimeString2SQLDate(String datetimeString, int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return granuleCount2SQLDate(datetimeString2GranuleCount(datetimeString, granularity), granularity);
  } // datetimeString2SQLDate

  public java.sql.Date datetimeString2SQLDate(String datetimeString) throws TemporalException
  {
    return datetimeString2SQLDate(datetimeString, FINEST);
  } // datetimeString2SQLDate

  // Take a datetime string and convert it to a java.util.Date object at the specified granularity.
  public java.util.Date datetimeString2UtilDate(String datetimeString, int granularity) throws TemporalException
  {
    checkGranularity(granularity);

    return granuleCount2UtilDate(datetimeString2GranuleCount(datetimeString, granularity), granularity);
  } // datetimeString2UtilDate

  public java.util.Date datetimeString2UtilDate(String datetimeString) throws TemporalException
  {
    return datetimeString2UtilDate(datetimeString, FINEST);
  } // datetimeString2UtilDate

  public String addGranuleCount(String datetimeString, long granuleCount, int granularity) throws TemporalException
  {
    long resultGranuleCount;

    checkGranularity(granularity);

    resultGranuleCount = datetimeString2GranuleCount(datetimeString, granularity) + granuleCount;

    return granuleCount2DatetimeString(resultGranuleCount, granularity);
  } // addGranuleCount

  public String subtractGranuleCount(String datetimeString, long granuleCount, int granularity) throws TemporalException
  {
    long resultGranuleCount;

    checkGranularity(granularity);

    resultGranuleCount = datetimeString2GranuleCount(datetimeString, granularity) - granuleCount;

    return granuleCount2DatetimeString(resultGranuleCount, granularity);
  } // subtractGranuleCount

  /**
   ** Take a granule count (from the beginning of calendar time, i.e., January 1st 1 C.E) at any granularity and convert it to a datetime
   ** string.
   */
  public String granuleCount2DatetimeString(long granuleCount, int granularity) throws TemporalException
  {
    return datetimeStringProcessor.granuleCount2DatetimeString(granuleCount, granularity);
  }

  public static void checkGranularity(int granularity) throws TemporalException
  {
    if (granularity < COARSEST || granularity > FINEST) throw new TemporalException("invalid granularity '" + granularity + "'");
  }

  public static void throwInvalidDatetimeStringException(String datetimeString) throws TemporalException
  {
    throw new TemporalException("invalid datetime string: '" + datetimeString + "'");
  }

  private static long convertMonthCount2GranuleCount(long monthCount, int to_granularity) throws TemporalException
  {
    long daysUpToAndIncludingMonth, yearCount, dayCount, result;

    checkGranularity(to_granularity);

    if (to_granularity == MONTHS) return monthCount;

    yearCount = monthCount / 12; 

    daysUpToAndIncludingMonth = days_to_month[(int)monthCount % 12]; 

    dayCount = daysUpToAndIncludingMonth + (yearCount * 365);

    if (to_granularity > MONTHS)result = dayCount * conversion_table[DAYS][to_granularity];
    else result = dayCount / conversion_table[DAYS][to_granularity];
    
    return result;
  } // convertMonthCount2GranuleCount

  // Calculate the number of extra leap granules at a specific granularity up until the start of a year. Optimize. TODO: Very inefficient.
  private static long leapGranulesUpToYear(long yearCount, int granularity) throws TemporalException
  {
    long leapDays = 0;

    checkGranularity(granularity);

    for (long i = 0; i < yearCount; i++) if (isLeapYear(i)) leapDays++; // isLeap year takes care of Gregorian discontinuity

    return leapDays * conversion_table[DAYS][granularity];    
  } // leapGranulesUpToYear

  // Calculate the number of extra leap granules at a specific granularity up until the start of a month.
  private static long leapGranulesUpToMonth(long monthCount, int granularity) throws TemporalException
  {
    long leapGranules, yearCount, month;

    checkGranularity(granularity);

    yearCount = monthCount / 12; 

    leapGranules = leapGranulesUpToYear(yearCount, granularity);

    if (yearCount != 0) {
      month = monthCount % 12;
      if (isLeapYear(yearCount) && month > 1) leapGranules += conversion_table[DAYS][granularity];
    } // if

    return leapGranules;
  } // leapGranulesUpToMonth    

  // Calculate the number of leap years up until a granule count specified at any granularity. TODO: rewrite - very, very inefficient
  private static long leapYearsUpToGranuleCount(long granuleCount, int granularity) throws TemporalException
  {
    long yearCount, granulesInYearToFeb29th, granulesInYear, granulesInDay, cumulativeGranuleCount, leapYearCount = 0;

    checkGranularity(granularity);

    if (granuleCount == 0) return 0;

    if (granularity == YEARS) leapYearCount = leapGranulesUpToYear(granuleCount, YEARS);
    else if (granuleCount == MONTHS) leapYearCount = leapGranulesUpToMonth(granuleCount, YEARS);
    else { // DAYS or finer.

      cumulativeGranuleCount = 0;
      granulesInYear = conversion_table[YEARS][granularity];
      granulesInDay = conversion_table[DAYS][granularity];
      yearCount = 0;
      do {
	yearCount++;
	cumulativeGranuleCount += granulesInYear;

	if (isLeapYear(yearCount)) {
	  leapYearCount++;
	  cumulativeGranuleCount += granulesInDay;
	} // if
      } while (cumulativeGranuleCount < granuleCount);

      // Add a leap year if wraparound is later than Feb 29th in a leap year.
      granulesInYearToFeb29th = (days_to_month[2] + 1) * conversion_table[DAYS][granularity];

      if (isLeapYear(yearCount) && ((cumulativeGranuleCount - granuleCount) > granulesInYearToFeb29th)) leapYearCount++;
    } // if

    return leapYearCount;
  } // leapYearsUpToGranuleCount

} // Temporal

