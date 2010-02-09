
package edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import edu.stanford.smi.protegex.owl.swrl.bridge.builtins.temporal.exceptions.TemporalException;

/**
 ** A class supporting processing of datetime strings represented in the standard XML Schema date format 'yyyy-MM-ddTHH:mm:ss.S'.
 ** <p>
 ** Timezone offsets are not yet supported.
 */
public class XSDDatetimeStringProcessor extends DatetimeStringProcessor
{
  private static SimpleDateFormat _dateFormat = new SimpleDateFormat("y-M-d'T'h:m:s.S"); // TODO: No support for Z yet
  private static String _delimiters = "-:.TZ";
  private GregorianCalendar gc = new GregorianCalendar();

  // The number of tokens (including delimeters) necessary to strip a datetime to a specified granularity.
  private  static int[] _gTokenIndex = { 1, 3, 5, 7, 9, 11, 13 }; // 1=YEARS, 3=MONTHS etc.

  // Strings to pad a partially specified datetime.
  private static String _datetimeRoundDownPadding[] = {"-01-01T00:00:00.000","-01T00:00:00.000","T00:00:00.000",":00:00.000",":00.000",".000", ""};

  // Day-in-month must be dealt with separately.
  private static String _datetimeRoundUpPadding[] = { "-12-31T23:59:59.999", "", "T23:59:59.999", ":59:59.999", ":59.999", ".999", "" };

  public XSDDatetimeStringProcessor()
  {
    super(_dateFormat, _delimiters, _gTokenIndex, _datetimeRoundDownPadding, _datetimeRoundUpPadding);
  } // XSDDatetimeStringProcessor

  protected String constructDatetimeStringFromMillisecondsFrom1970Count(long millisecondsFrom1970) throws TemporalException
  {
    Timestamp ts= new Timestamp(millisecondsFrom1970);
    TimeZone tz = gc.getTimeZone();
    
    if (tz.inDaylightTime(ts)) 
    	ts = new Timestamp(millisecondsFrom1970 - daylightSavingsTimeOffsetInMillis);
        
    return ts.toString().replace(' ', 'T'); // Timestamp.toString returns in JDBC format so replace space with 'T'.
  } // constructDatetimeStringFromMillisecondsFrom1970Count

} // XSDDatetimeStringProcessor
