
package edu.stanford.smi.protegex.owl.swrl.bridge.xsd;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.axis.types.Duration;
import org.apache.axis.types.Time;

public class XSDTimeUtil
{
  private static String xsdDateTimeFormatString = "yyyy-MM-dd'T'hh:mm:ss";
  private static String xsdDateFormatString = "yyyy-MM-dd";
  private static DateFormat xsdDateTimeFormat = new SimpleDateFormat(xsdDateTimeFormatString);
  private static DateFormat xsdDateFormat = new SimpleDateFormat(xsdDateFormatString);

  private static String jdbcDateTimeFormatString = "y-M-d h:m:s.S";
  private static String jdbcDateFormatString = "y-M-d";
  private static DateFormat jdbcDateTimeFormat = new SimpleDateFormat(jdbcDateTimeFormatString);
  private static DateFormat jdbcDateFormat = new SimpleDateFormat(jdbcDateFormatString);

  public static Duration addDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setYears(getYears(duration1) + getYears(duration2));
    result.setMonths(getMonths(duration1) + getMonths(duration2));
    result.setDays(getDays(duration1) + getDays(duration2));
    result.setHours(getHours(duration1) + getHours(duration2));
    result.setMinutes(getMinutes(duration1) + getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) + getSeconds(duration2));

    return result;
  } // addDurations

  public static Duration subtractDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setYears(getYears(duration1) - getYears(duration2));
    result.setMonths(getMonths(duration1) - getMonths(duration2));
    result.setDays(getDays(duration1) - getDays(duration2));
    result.setHours(getHours(duration1) - getHours(duration2));
    result.setMinutes(getMinutes(duration1) - getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) - getSeconds(duration2));

    return result;
  } // addDurations

  public static Duration multiplyDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setYears(getYears(duration1) * getYears(duration2));
    result.setMonths(getMonths(duration1) * getMonths(duration2));
    result.setDays(getDays(duration1) * getDays(duration2));
    result.setHours(getHours(duration1) * getHours(duration2));
    result.setMinutes(getMinutes(duration1) * getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) * getSeconds(duration2));

    return result;
  } // multiplyDurations

  public static Duration divideDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setYears(getYears(duration1) / getYears(duration2));
    result.setMonths(getMonths(duration1) / getMonths(duration2));
    result.setDays(getDays(duration1) / getDays(duration2));
    result.setHours(getHours(duration1) / getHours(duration2));
    result.setMinutes(getMinutes(duration1) / getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) / getSeconds(duration2));

    return result;
  } // multiplyDurations

  public static Duration addDayTimeDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getDays(duration1) + getDays(duration2));
    result.setHours(getHours(duration1) + getHours(duration2));
    result.setMinutes(getMinutes(duration1) + getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) + getSeconds(duration2));

    return result;
  } // addDayTimeDurations

  public static Duration subtractDayTimeDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getDays(duration1) - getDays(duration2));
    result.setHours(getHours(duration1) - getHours(duration2));
    result.setMinutes(getMinutes(duration1) - getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) - getSeconds(duration2));

    return result;
  } // subtractDayTimeDurations

  public static Duration multiplyDayTimeDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getDays(duration1) * getDays(duration2));
    result.setHours(getHours(duration1) * getHours(duration2));
    result.setMinutes(getMinutes(duration1) * getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) * getSeconds(duration2));

    return result;
  } // multiplyDayTimeDurations

  public static Duration divideDayTimeDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getDays(duration1) / getDays(duration2));
    result.setHours(getHours(duration1) / getHours(duration2));
    result.setMinutes(getMinutes(duration1) / getMinutes(duration2));
    result.setSeconds(getSeconds(duration1) / getSeconds(duration2));

    return result;
  } // divideDayTimeDurations

  public static Duration addYearMonthDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getYears(duration1) + getYears(duration2));
    result.setHours(getMonths(duration1) + getMonths(duration2));

    return result;
  } // addYearMonthDurations

  public static Duration subtractYearMonthDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getYears(duration1) - getYears(duration2));
    result.setHours(getMonths(duration1) - getMonths(duration2));

    return result;
  } // subtractYearMonthDurations

  public static Duration multiplyYearMonthDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getYears(duration1) * getYears(duration2));
    result.setHours(getMonths(duration1) * getMonths(duration2));

    return result;
  } // multiplyYearMonthDurations

  public static Duration divideYearMonthDurations(Duration duration1, Duration duration2)
  {
    Duration result = new Duration();

    result.setDays(getYears(duration1) / getYears(duration2));
    result.setHours(getMonths(duration1) / getMonths(duration2));

    return result;
  } // divideYearMonthDurations
  
  public static Duration subtractDates(Date date1, Date date2)
  {
    Calendar calendar1 = new GregorianCalendar();
    Calendar calendar2 = new GregorianCalendar();
    calendar1.setTime(date1); calendar2.setTime(date2); 

    int years = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
    int months = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);
    int days = calendar1.get(Calendar.DAY_OF_MONTH) - calendar2.get(Calendar.DAY_OF_MONTH);
    int hours = calendar1.get(Calendar.HOUR) - calendar2.get(Calendar.HOUR);
    int minutes = calendar1.get(Calendar.MINUTE) - calendar2.get(Calendar.MINUTE);
    double seconds = calendar1.get(Calendar.SECOND) - calendar2.get(Calendar.SECOND);

    return new Duration(false, years, months, days, hours, minutes, seconds);
  } // subtractDates

  public static Duration subtractTimes(Time time1, Time time2)
  {
    Calendar calendar1 = time1.getAsCalendar();
    Calendar calendar2 = time2.getAsCalendar();
    Duration result = new Duration();

    int hours = calendar1.get(Calendar.HOUR) - calendar2.get(Calendar.HOUR);
    int minutes = calendar1.get(Calendar.MINUTE) - calendar2.get(Calendar.MINUTE);
    double seconds = calendar1.get(Calendar.SECOND) - calendar2.get(Calendar.SECOND);
    
    result.setHours(hours);
    result.setMinutes(minutes);
    result.setSeconds(seconds);

    return result;
  } // subtractTimes

  public static Date addYearMonthDurationToDateTime(Date date, Duration duration)
  {
    Calendar calendar = new GregorianCalendar(), result = new GregorianCalendar();

    calendar.setTime(date);
   
    result.set(calendar.get(Calendar.YEAR) + getYears(duration), calendar.get(Calendar.MONTH) + getMonths(duration),
               calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR),
               calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    
    return result.getTime();
  } // addYearMonthDurationToDateTime

  public static Date subtractYearMonthDurationFromDateTime(Date date, Duration duration)
  {
    Calendar calendar = new GregorianCalendar(), result = new GregorianCalendar();

    calendar.setTime(date);
   
    result.set(calendar.get(Calendar.YEAR) - getYears(duration), calendar.get(Calendar.MONTH) - getMonths(duration),
               calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR),
               calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    
    return result.getTime();
  } // addYearMonthDurationToDateTime

  public static Date addDayTimeDurationToDateTime(Date date, Duration duration)
  {
    Calendar calendar = new GregorianCalendar(), resultCalendar = new GregorianCalendar();

    calendar.setTime(date);
   
    resultCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                       calendar.get(Calendar.DAY_OF_MONTH) + getDays(duration), 
                       calendar.get(Calendar.HOUR) + getHours(duration),
                       calendar.get(Calendar.MINUTE) + getMinutes(duration), 
                       (int)(calendar.get(Calendar.SECOND) + getSeconds(duration)));
    
    return resultCalendar.getTime();
  } // addDayTimeDurationToDateTime

  public static Date subtractDayTimeDurationFromDateTime(Date date, Duration duration)
  {
    Calendar calendar = new GregorianCalendar(), resultCalendar = new GregorianCalendar();

    calendar.setTime(date);
   
    resultCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                       calendar.get(Calendar.DAY_OF_MONTH) - getDays(duration), 
                       calendar.get(Calendar.HOUR) - getHours(duration),
                       calendar.get(Calendar.MINUTE) - getMinutes(duration), 
                       (int)(calendar.get(Calendar.SECOND) - getSeconds(duration)));
               
    return resultCalendar.getTime();
  } // subtractDayTimeDurationFromDateTime

  public static Date addYearMonthDurationToDate(Date date, Duration duration)
  {
    Calendar calendar = new GregorianCalendar(), resultCalendar = new GregorianCalendar();

    calendar.setTime(date);
   
    resultCalendar.set(calendar.get(Calendar.YEAR) + getYears(duration), calendar.get(Calendar.MONTH) + getMonths(duration),
                       calendar.get(Calendar.DAY_OF_MONTH));
    
    return resultCalendar.getTime();
  } // addYearMonthDurationToDate

  public static Date subtractYearMonthDurationFromDate(Date date, Duration duration)
  {
    Calendar calendar = new GregorianCalendar(), resultCalendar = new GregorianCalendar();

    calendar.setTime(date);

    resultCalendar.set(calendar.get(Calendar.YEAR) - getYears(duration), calendar.get(Calendar.MONTH) - getMonths(duration),
                       calendar.get(Calendar.DAY_OF_MONTH));
  
    return resultCalendar.getTime();
  } // subtractYearMonthDurationFromDate

  public static Time addDayTimeDurationToTime(Time time, Duration duration)
  {
    Calendar calendar = time.getAsCalendar(), resultCalendar = new GregorianCalendar();

    resultCalendar.set(0, 
                       calendar.get(Calendar.DAY_OF_MONTH) + getDays(duration),
                       calendar.get(Calendar.HOUR) + getHours(duration),
                       calendar.get(Calendar.MINUTE) + getMinutes(duration), 
                       (int)(calendar.get(Calendar.SECOND) + getSeconds(duration)));
    
    return new Time(resultCalendar);
  } // addDayTimeDurationToTime
  
  public static Time subtractDayTimeDurationFromTime(Time time, Duration duration)
  {
    Calendar calendar = time.getAsCalendar(), resultCalendar = new GregorianCalendar();
    
    resultCalendar.set(0, 
                       calendar.get(Calendar.DAY_OF_MONTH) - getDays(duration),
                       calendar.get(Calendar.HOUR) - getHours(duration),
                       calendar.get(Calendar.MINUTE) - getMinutes(duration), 
                       (int)(calendar.get(Calendar.SECOND) - getSeconds(duration)));
    
    return new Time(resultCalendar);
  } // subtractDayTimeDourationFromTime

  public static Duration subtractDateTimesYieldingYearMonthDuration(Date date1, Date date2)
  {
    Calendar calendar1 = new GregorianCalendar();
    Calendar calendar2 = new GregorianCalendar();
    calendar1.setTime(date1); calendar2.setTime(date2); 

    int years = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
    int months = calendar1.get(Calendar.MONTH) - calendar2.get(Calendar.MONTH);

    return new Duration(false, years, months, 0, 0, 0, 0);
  } // subtractDateTimesYieldingYearMonthDuration

  public static Duration subtractDateTimesYieldingDayTimeDuration(Date date1, Date date2)
  {
    Calendar calendar1 = new GregorianCalendar();
    Calendar calendar2 = new GregorianCalendar();
    calendar1.setTime(date1); calendar2.setTime(date2); 

    int days = calendar1.get(Calendar.DAY_OF_MONTH) - calendar2.get(Calendar.DAY_OF_MONTH);
    int hours = calendar1.get(Calendar.HOUR) - calendar2.get(Calendar.HOUR);
    int minutes = calendar1.get(Calendar.MINUTE) - calendar2.get(Calendar.MINUTE);
    double seconds = calendar1.get(Calendar.SECOND) - calendar2.get(Calendar.SECOND);

    return new Duration(false, 0, 0, days, hours, minutes, seconds);
  } // subtractDateTimesYieldingDayTimeDuration

  public static String date2XSDDateTimeString(java.util.Date date) { return xsdDateTimeFormat.format(date); }
  public static String date2XSDDateString(java.util.Date date) { return xsdDateFormat.format(date); }

  public static String date2XSDTimeString(java.util.Date date) 
  { 
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    Time time = new Time(calendar);
    return time.toString();
  } // date2XSDTimeString

  public static java.util.Date xsdDateTimeString2Date(String content) throws ParseException { return xsdDateTimeFormat.parse(content); }
  public static java.util.Date xsdDateString2Date(String content) throws ParseException { return xsdDateFormat.parse(content); }

  public static java.util.Date xsdTimeString2Date(String content) throws NumberFormatException 
  { 
    Time time = new Time(content); 
    return time.getAsCalendar().getTime();
  } // xsdTimeStringToDate

  public static String date2JDBCDateTimeString(java.util.Date date) { return jdbcDateTimeFormat.format(date); }
  public static String date2JDBCDateString(java.util.Date date) { return jdbcDateFormat.format(date); }

  public static java.util.Date jdbcDateTimeString2Date(String content) throws ParseException { return jdbcDateTimeFormat.parse(content); }
  public static java.util.Date jdbcDateString2Date(String content) throws ParseException { return jdbcDateFormat.parse(content); }
  
  public static boolean isValidXSDDateTime(String content)  
  { 
    boolean result = false;

    try {
      xsdDateTimeFormat.parse(content); 
      result = true;
    } catch (ParseException e) {}
    return result;
  } // isValidXSDDateTime

  public static boolean isValidXSDDate(String content)  
  { 
    boolean result = false;

    try {
      xsdDateFormat.parse(content); 
      result = true;
    } catch (ParseException e) {}
    return result;
  } // isValidXSDDate

  public static boolean isValidXSDTime(String content)  
  { 
    boolean result = false;

    try {
      new Time(content);
      result = true;
    } catch (NumberFormatException e) {}
    return result;
  } // isValidXSDDate

  public static boolean isValidXSDDuration(String content)  
  { 
    boolean result = false;

    try {
      new Duration(content);
      result = true;
    } catch (IllegalArgumentException e) {}
    return result;
  } // isValidXSDDuration

  public static boolean isValidJDBCDateTime(String content)  
  { 
    boolean result = false;

    try {
      jdbcDateTimeFormat.parse(content); 
      result = true;
    } catch (ParseException e) {}
    return result;
  } // isValidJDBCDateTime

  public static int addDurationYears(Duration duration1, Duration duration2) { return getYears(duration1) + getYears(duration2); }
  public static int addDurationMonths(Duration duration1, Duration duration2) { return getMonths(duration1) + getMonths(duration2); }
  public static int addDurationDays(Duration duration1, Duration duration2) { return getDays(duration1) + getDays(duration2); }
  public static int addDurationHours(Duration duration1, Duration duration2) { return getHours(duration1) + getHours(duration2); }
  public static int addDurationMinutes(Duration duration1, Duration duration2) { return getMinutes(duration1) + getMinutes(duration2); }
  public static double addDurationSeconds(Duration duration1, Duration duration2) { return getSeconds(duration1) + getSeconds(duration2); }

  public static int subtractDurationYears(Duration duration1, Duration duration2) { return getYears(duration1) - getYears(duration2); }
  public static int subtractDurationMonths(Duration duration1, Duration duration2) { return getMonths(duration1) - getMonths(duration2); }
  public static int subtractDurationDays(Duration duration1, Duration duration2) { return getDays(duration1) - getDays(duration2); }
  public static int subtractDurationHours(Duration duration1, Duration duration2) { return getHours(duration1) - getHours(duration2); }
  public static int subtractDurationMinutes(Duration duration1, Duration duration2) { return getMinutes(duration1) - getMinutes(duration2); }
  public static double subtractDurationSeconds(Duration duration1, Duration duration2) { return getSeconds(duration1) - getSeconds(duration2); }

  public static int getYears(Duration duration) { if (duration.isNegative()) return - duration.getYears(); else return duration.getYears(); }
  public static int getMonths(Duration duration) { if (duration.isNegative()) return - duration.getMonths(); else return duration.getMonths(); }
  public static int getDays(Duration duration) { if (duration.isNegative()) return - duration.getDays(); else return duration.getDays(); }
  public static int getHours(Duration duration) { if (duration.isNegative()) return - duration.getHours(); else return duration.getHours(); }
  public static int getMinutes(Duration duration) { if (duration.isNegative()) return - duration.getMinutes(); else return duration.getMinutes(); }
  public static double getSeconds(Duration duration) { if (duration.isNegative()) return - duration.getSeconds(); else return duration.getSeconds(); }

} // XSDTimeUtil
