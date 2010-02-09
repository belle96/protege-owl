
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.exceptions.TemporalException;

/**
 **  A class supporting processing of datetime strings represented in the standard JDBC format 'y-M-d h:m:s.S'.
 */
public class JDBCDatetimeStringProcessor extends DatetimeStringProcessor
{
  private static SimpleDateFormat _dateFormat = new SimpleDateFormat("y-M-d h:m:s.S");
  private static String _delimiters = " -:."; // Note the space.
  private GregorianCalendar gc = new GregorianCalendar();

  // The number of tokens (including delimeters) necessary to strip a datetime to a specified granularity.
  private static int[] _gTokenIndex = { 1, 3, 5, 7, 9, 11, 13 }; // 1=YEARS, 3=MONTHS etc.

  // Strings to pad a partially specified datetime.
  private static String _datetimeRoundDownPadding[] = {"-01-01 00:00:00.000","-01 00:00:00.000"," 00:00:00.000",":00:00.000",":00.000",".000", ""};

  /** .997 is used for the millisecond value instead of .999 because SQL Server appears to have a resolution of .003 milliseconds and will
   ** round to the next second if a value greater than .997 is used. Day-in-month is dealt with separately.
   */
  private static String _datetimeRoundUpPadding[] = { "-12-31 23:59:59.997", "", " 23:59:59.997", ":59:59.997", ":59.997", ".997", "" };

  public JDBCDatetimeStringProcessor()
  {
    super(_dateFormat, _delimiters, _gTokenIndex, _datetimeRoundDownPadding, _datetimeRoundUpPadding);
  } // JDBCDatetimeStringProcessor

  protected String constructDatetimeStringFromMillisecondsFrom1970Count(long millisecondsFrom1970) throws TemporalException
  {
	  Timestamp ts= new Timestamp(millisecondsFrom1970);
	  TimeZone tz = gc.getTimeZone();
	    
	  if (tz.inDaylightTime(ts)) 
		  ts = new Timestamp(millisecondsFrom1970 - daylightSavingsTimeOffsetInMillis);
	       
	  return ts.toString(); // Returns in JDBC format.
  } // constructDatetimeString

} // JDBCDatetimeStringProcessor

