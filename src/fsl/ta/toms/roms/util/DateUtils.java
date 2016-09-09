package fsl.ta.toms.roms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.util.holiday.HolidayValidator;

public class DateUtils {
public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"; 
	
	private static final DateFormat utilDateFormatter = new SimpleDateFormat("dd-MM-yyyy");	
	private static final DateFormat fullDateFormatter = new SimpleDateFormat("yyyy-MM-dd h:mm a");
	private static final DateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public String START_TAX_YEAR = "-04-1";
	public String END_TAX_YEAR = "-03-31";
	
	private static final long MILLISECONDS_IN_A_DAY = 86400000;
	
	
	public enum SEARCHDATETYPE{START,END};
	
	
	
	public static int validateCourtDate(Date courtDateTime, Date issueDate)throws InvalidFieldException, RequiredFieldMissingException {
		// Court Date must be 3 clear working days from issueDate
		Calendar calIssueDate = Calendar.getInstance();
		calIssueDate.setTime(issueDate);

		Calendar calCourtDate = Calendar.getInstance();
		calCourtDate.setTime(courtDateTime);

		long numOfDaysBetween = DateUtils.workDaysBetween(calIssueDate, calCourtDate);
		System.err.println("Num of days between " + numOfDaysBetween);

		HolidayValidator holidayValidator = new HolidayValidator();
		boolean isHoliday = false;
		
		// validate for holiday
		try {
			isHoliday = holidayValidator.isHoliday(courtDateTime);
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (!(numOfDaysBetween > 2)) {  
			
			if (isHoliday)
			{
				throw new InvalidFieldException("Court Date Cannot Be a Holiday"); //holiday

			}else
			{
				throw new InvalidFieldException("Court Date Must Be Three(3) Clear Working Days from Issue Date"); //less than 3 clear days
			}
			
		} else {
			if (DateUtils.isWeekDay(calCourtDate) == false) {
				throw new InvalidFieldException("Court Date cannot be a Weekend"); //weekend

			} else {
				
				if (isHoliday)
					throw new InvalidFieldException("Court Date Cannot Be a Holiday"); //holiday

			}
		}

		return 0;

	}

	
	
	
	
	/**
	 * This function takes the date and returns the beginning or end of the day based on the SEARCHDATETYPE
	 * @param date
	 * @param type
	 * @return
	 */
	public static Date searchDateFormater(Date date,SEARCHDATETYPE type)
	{
		Calendar outputDate = Calendar.getInstance();
		
				
		if(SEARCHDATETYPE.START == type)
		{
			outputDate.setTime(date);
			outputDate.set(Calendar.HOUR_OF_DAY, 0);
			outputDate.set(Calendar.MINUTE, 0);
			outputDate.set(Calendar.SECOND, 0);
			outputDate.set(Calendar.MILLISECOND, 0);
		}
		else
		{
			outputDate.setTime(date);
			outputDate.set(Calendar.HOUR_OF_DAY, 23);
			outputDate.set(Calendar.MINUTE, 59);
			outputDate.set(Calendar.SECOND, 59);
			outputDate.set(Calendar.MILLISECOND, 999);
		}
		return outputDate.getTime();
	}
	/**
	 * jreid
	 * @return tax year start date
	 */	
	public static Date getStartCurrentTaxYear() {
		Date today = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// determine if today is before the start of the tax year
		if (today.before(calendar.getTime())) {
			// then the start of the tax year is last year
			// march 31, next year
			calendar.roll(Calendar.YEAR, -1);
		}
		// then the start of the calendar date is this year April

		return calendar.getTime();
	}

	/**
	 * jreid
	 * @return tax year END date
	 */
	public static Date getEndCurrentTaxYear() {
		Date today = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// determine if today is before the start of the tax year
		if (today.before(calendar.getTime())) {
			// then the end of the calendar date is this year
			// march 31, this year
			calendar.set(Calendar.MONTH, Calendar.MARCH);
			calendar.set(Calendar.DAY_OF_MONTH, 31);			
		} else {
			// then the end of the calendar date is this year march
			// march 31, this year
			calendar.set(Calendar.MONTH, Calendar.MARCH);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			calendar.roll(Calendar.YEAR, 1);
		}
		return calendar.getTime();

	}

	static final DateFormat utilDateFormatterFsl = new SimpleDateFormat("yyyy-MM-dd");

		public static java.sql.Date utilDateToSqlDate(java.util.Date uDate) throws ParseException
		{
		return java.sql.Date.valueOf(sqlDateFormatter.format(uDate));
		}
		
		
		public static java.util.Date sqlDateToutilDate(java.sql.Date sDate) throws ParseException
		{
		return utilDateFormatter.parse(utilDateFormatter.format(sDate));
		}
		
		final static public  String formatDate(String dateFormat ,java.util.Date date)
		{
			SimpleDateFormat formatter = new SimpleDateFormat(); 
			
			formatter.applyPattern(dateFormat);
			
			if(date == null)
				return "";
			else
				return formatter.format(date);

		}
		/**
		 * Returns a Date object given a Date string and the format the string adheres to. The default date format
		 * is yyyy-MM-dd. If there is a mismatch between the formatted date String and the date format null is returned.
		 * @param formattedDate
		 * @param dateFormat
		 * @return
		 */
		final static public java.util.Date parse(String formattedDate, String dateFormat)
		{
			java.util.Date result = null;
			
			if(formattedDate == null)
				return null;
			
			try{
			
			if(dateFormat == null)
				result = utilDateFormatterFsl.parse(formattedDate);
			else
			{
				SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
				result =  formatter.parse(formattedDate);
			}
			}
			catch(ParseException e)
			{
				e.printStackTrace();
			}
			
			return result;
		}
		
		final static public  String formatDateLong(java.util.Date date)
		{
			
			 String longDateFormat = DateFormat.getDateTimeInstance(
			            DateFormat.LONG, DateFormat.LONG).format(date);
			            
					
			if(date == null)
				return "";
			else
				return longDateFormat;

		}
		
		final static public boolean isDateValid(String formattedDate, String dateFormat)
		{
			SimpleDateFormat formatter = new SimpleDateFormat();
			
			formatter.applyPattern(dateFormat);
			
			try {
				java.util.Date parsedDate = formatter.parse(formattedDate);
				
				String validationDate = formatter.format(parsedDate); 
				
				if(validationDate.equals(formattedDate))
					return true;
				else
					return false;
					
			} catch (ParseException e) {
				// Date Invalid
				return false;
			}	
		}
		
		final static public boolean before(java.util.Date fromDate, java.util.Date toDate)
		{
			if((fromDate != null) && (toDate != null))
			{
				return fromDate.before(toDate);
			}
			
			return false;
		}


		public static DateFormat getUtilDateFormatter() {
			return utilDateFormatter;
		}


		public static DateFormat getSqlDateFormatter() {
			return sqlDateFormatter;
		}	

		

		/**
		 * @return the fulldateformatter
		 */
		public static DateFormat getFulldateformatter() {
			return fullDateFormatter;
		}




		/* (non-Javadoc)
		 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
		 * ensures  zero cannot be written for day or month either
		 * @Jreid
		 */
		public static boolean isDateValid(String value){	

			if(value == null || StringUtils.isBlank(value)) 
				return false;
			
			try {
				sqlDateFormatter.parse(value);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}		
			
			String[] dateArray = StringUtils.split(value.toString(), "-");

			int year = Integer.parseInt(dateArray[0]), month = Integer.parseInt(dateArray[1]), day = Integer.parseInt(dateArray[2]);

			if (year > 9999) {
				return false;
			}

			if (month < 1 || month > 12) {
				return false;
			}

			int[] daysInMonths = new int[] { 31, (isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			if (day < 1 || day > daysInMonths[month - 1]) {
				return false;
			}
			
			return true;
				
			
		}
		
		/**
		 * @param year
		 */
		public static boolean isLeapYear(int year) {
			if ((year % 100) == 0 && (year % 400) == 0)
				return true;
			else if ((year % 4) == 0)
				return true;
			else
				return false;
		}
		
		//ensure when u roll up month changes if needs be
		 public static Calendar rollUp(Calendar date){
			
			int day = date.get(Calendar.DAY_OF_MONTH);
			int month = date.get(Calendar.MONTH);
			
			if (day == 31 && month == 12){
				date.roll(Calendar.DAY_OF_MONTH, 1);
				date.set(Calendar.MONTH, 1);
				date.roll(Calendar.YEAR, 1);
				return date;
			}
			
			date.roll(Calendar.DAY_OF_MONTH, 1);
			if (day == 1){
				date.roll(Calendar.MONTH, 1);
				return date;
			}
			
			
			return date;
			
		 }
		 
		//ensure when u roll up month changes if needs be
		 public static Calendar rollDown(Calendar date){
			
			int day = date.get(Calendar.DAY_OF_MONTH);
			int month = date.get(Calendar.MONTH);
			
			
			

			if (day == 1 && month==1){
				date.roll(Calendar.DAY_OF_MONTH, -1);
				date.roll(Calendar.MONTH, -1);
				date.roll(Calendar.YEAR, -1);
				return date;	
			}
			
			if (day == 1){
				date.roll(Calendar.DAY_OF_MONTH, -1);
				date.roll(Calendar.MONTH, -1);
				return date;	
			}
			
		
	//	
//			if(month == 9	|| month == 4	|| month == 6	|| month == 11){		
//				if (day == 30){
//					date.roll(date.MONTH, -1);
//					return date;
//				}
//			}
//			
		
			
//			
//			if(isLeapYear(year) && month ==3){
//				if (day == 29){
//					date.roll(date.MONTH, -1);
//					return date;
//				}
//			}
//			
//			if(month ==3){
//				if (day == 28){
//					date.roll(date.MONTH, -1);
//					return date;
//				}
//			}
			return date;
		 }

		public static String getFormattedUtilDate(java.util.Date sDate) throws ParseException
		{
		return utilDateFormatterFsl.format(sDate);
		}
		
		public static String getFormattedUtilDate_YYYY_MM_DD_hh_mm_a(java.util.Date sDate)
		{
			try
			{
				return fullDateFormatter.format(sDate);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		
		public static String getFormattedSqlDate(java.sql.Date sDate) throws ParseException
		{
		return utilDateFormatterFsl.format(sDate);
		}
		
		public String getTodayDateString(){

			return utilDateFormatterFsl.format(  new Date( System.currentTimeMillis() )  );		
			
		}

		public static Date getTodayDate(){
			
			Calendar calendar = Calendar.getInstance();
			
			calendar.setTime( new Date(System.currentTimeMillis()) );
			
		    // normalize the object
		    calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);
		    
			Date currentDate = new Date(calendar.getTime().getTime());
			
			return currentDate;
			
			
		}
		
		
		public Calendar getTodayCalender(){
			
			Calendar calendar = Calendar.getInstance();
			
			calendar.setTime( new Date(System.currentTimeMillis()) );
			
		    // normalize the object
		    calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);	    
		
			return calendar;	
			
		}
		
	//rennis
	public static Date stringToDate(String date) {
		
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
		
		try {
			return dateFormat.parse(date);
		} catch (Exception e) {
			return null;
		}
		
	}


	public static long getMILLISECONDS_IN_A_DAY() {
		return MILLISECONDS_IN_A_DAY;
	}

	public static long daysBetween(Calendar startDate, Calendar endDate) {  
		   Calendar date = (Calendar) startDate.clone();  
		   long daysBetween = 0;  
		   while (date.before(endDate)) {  
		     date.add(Calendar.DAY_OF_MONTH, 1);  
		     daysBetween++;  
		   }  
		   return daysBetween;  
	}
	
	public static boolean isFutureDate(String dateStr){
		Date today = new Date();
		
		Date dateVal = parse(dateStr,DATE_FORMAT_YYYY_MM_DD);
		
		if(today.before(dateVal))
			return true;
		return false;		
	}
	
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long workDaysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		int saturday = Calendar.SATURDAY;
		int sunday = Calendar.SUNDAY;
		long daysBetween = 0;
		
		HolidayValidator holidayValidator = new HolidayValidator();
		
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			Date calDate = date.getTime();
			
			try {
				if ((date.get(Calendar.DAY_OF_WEEK) != saturday && date.get(Calendar.DAY_OF_WEEK) != sunday)
					&& (!holidayValidator.isHoliday(calDate)) )				//Dec. 29, 2014 - include check for holiday
				{	
				
					daysBetween++;
				}
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return daysBetween;
	}
	
	
	public static boolean isWeekDay(Calendar date){
		int saturday = Calendar.SATURDAY;
		   int sunday = Calendar.SUNDAY;
		  
		if(date.get(Calendar.DAY_OF_WEEK)!=saturday && date.get(Calendar.DAY_OF_WEEK)!=sunday){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Get a diff between two dates
	 * Code provided from http://stackoverflow.com/questions/1555262/calculating-the-difference-between-two-java-date-instances
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	
	
	public static Date minusDays(Date d, int days)
	{
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.DATE, -days);
	    d.setTime( c.getTime().getTime() );
	    
	    return d;
	}
	
	
	public static Date addDays(Date d, int days)
	{
	    Calendar c = Calendar.getInstance();
	    c.setTime(d);
	    c.add(Calendar.DATE, days);
	    d.setTime( c.getTime().getTime() );
	    
	    return d;
	}
	
	public static Date combineDateTime(Date datePart, String timePart) throws ParseException
	{
		String datePartString = DateUtils.formatDate(DATE_FORMAT_YYYY_MM_DD, datePart);
		
		//System.out.println("Date in date function :" + datePartString);
		
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datePartString + " " + timePart);
		
		return date;
	}
	
	
	/************************** PARSE BITS OF DATE FOR DOCUMENTS **********************************/
	/**
	 * Gets the day of Week from date e.g. Wednesday
	 * @param date
	 * @return
	 */
	public static String getDayOfWeekFromDate(Date date){
		if(date != null){			
			
			return new SimpleDateFormat("EEEE").format(date);			
			
		}
		return null;
		
	}
	
	/**
	 * Gets teh day of month e.g. 21
	 * @param date
	 * @return
	 */
	public static String getOrdinalDayOfMonthFromDate(Date date){
		if(date != null){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
			return formatOdinalDay(dayOfWeek);
			
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getDayOfMonthFromDate(Date date){
		if(date != null){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int dayOfWeek = c.get(Calendar.DAY_OF_MONTH);
			return dayOfWeek + "";
			
		}
		return null;
	}
	
	/**
	 * Gets teh month e.g. June
	 * @param date
	 * @return
	 */
	public static String getMonthFromDate(Date date){
		if(date != null){
			return new SimpleDateFormat("MMMM").format(date);		
			
		}
		return null;
	}
	
	
	/**
	 * Gets the year from date e.g. 2004
	 * @param date
	 * @return
	 */
	public static String getYearFromDate(Date date){
		if(date != null){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int dayOfWeek = c.get(Calendar.YEAR);
			return dayOfWeek + "";
			
		}
		return null;
	}
	
	public static String getPartYearFromDate(Date date){
		if(date != null){
			String d = new SimpleDateFormat("yy").format(date);	
			return d;
			
		}
		return null;
	}
	
	/**
	 * gets the time from a date e.g. 12:05 AM
	 * @param date
	 * @return
	 */
	public static String getTimeFromDate(Date date){
		if(date != null){
			if(date != null){			
				
				return new SimpleDateFormat("hh:mm a").format(date);			
				
			}
			
		}
		return null;
	}
	
	
	/******************* FORMAT *******************/

	public static String formatOdinalDay(int dayOfMonth) {

		final String[] suffixes =
		// 0 1 2 3 4 5 6 7 8 9
		{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 10 11 12 13 14 15 16 17 18 19
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				// 20 21 22 23 24 25 26 27 28 29
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 30 31
				"th", "st" };


		String dayStr = dayOfMonth + suffixes[dayOfMonth];

		return dayStr;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatOdinalDay(Date date) {

		final String[] suffixes =
		// 0 1 2 3 4 5 6 7 8 9
		{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 10 11 12 13 14 15 16 17 18 19
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				// 20 21 22 23 24 25 26 27 28 29
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				// 30 31
				"th", "st" };

		Calendar dateC = Calendar.getInstance();
		dateC.setTime(date);
		int day = dateC.get(Calendar.DAY_OF_MONTH);
		String dayStr = day + suffixes[day];

		return dayStr;
	}

	public static Date addOneDay(Date date){
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
	}
	

}
