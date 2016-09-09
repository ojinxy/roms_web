package org.fsl.roms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.util.holiday.HolidayValidator;

public class DateUtils {

	private static final int MILLIS_IN_SECOND = 1000;
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;
	private static final int DAYS_IN_YEAR = 365;

	public static final long MILLISECONDS_IN_YEAR = (long) MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR
			* HOURS_IN_DAY * DAYS_IN_YEAR;

	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	private static final DateFormat utilDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	private static final DateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

	public String START_TAX_YEAR = "-04-1";
	public String END_TAX_YEAR = "-03-31";

	private static final long MILLISECONDS_IN_A_DAY = 86400000;

	/**
	 * jreid
	 * 
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
	 * 
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

	public static java.sql.Date utilDateToSqlDate(java.util.Date uDate) throws ParseException {
		return java.sql.Date.valueOf(sqlDateFormatter.format(uDate));
	}

	public static java.util.Date sqlDateToutilDate(java.sql.Date sDate) throws ParseException {
		return utilDateFormatter.parse(utilDateFormatter.format(sDate));
	}

	final static public String formatDate(String dateFormat, java.util.Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat();

		formatter.applyPattern(dateFormat);

		if (date == null)
			return "";
		else
			return formatter.format(date);

	}

	/**
	 * Returns a Date object given a Date string and the format the string adheres to. The default date format is
	 * yyyy-MM-dd. If there is a mismatch between the formatted date String and the date format null is returned.
	 * 
	 * @param formattedDate
	 * @param dateFormat
	 * @return
	 */
	final static public java.util.Date parse(String formattedDate, String dateFormat) {
		java.util.Date result = null;

		if (formattedDate == null)
			return null;

		try {

			if (dateFormat == null)
				result = utilDateFormatterFsl.parse(formattedDate);
			else {
				SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
				result = formatter.parse(formattedDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	final static public String formatDateLong(java.util.Date date) {

		String longDateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date);

		if (date == null)
			return "";
		else
			return longDateFormat;

	}

	final static public boolean isDateValid(String formattedDate, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat();

		formatter.applyPattern(dateFormat);

		try {
			java.util.Date parsedDate = formatter.parse(formattedDate);

			String validationDate = formatter.format(parsedDate);

			if (validationDate.equals(formattedDate))
				return true;
			else
				return false;

		} catch (ParseException e) {
			// Date Invalid
			return false;
		}
	}

	final static public boolean before(java.util.Date fromDate, java.util.Date toDate) {
		if ((fromDate != null) && (toDate != null)) {
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

	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext ,
	 * javax.faces.component.UIComponent, java.lang.Object) ensures zero cannot be written for day or month either
	 * @Jreid */
	public static boolean isDateValid(String value) {

		if (value == null || StringUtils.isBlank(value))
			return false;

		try {
			sqlDateFormatter.parse(value);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		String[] dateArray = StringUtils.split(value.toString(), "-");

		int year = Integer.parseInt(dateArray[0]), month = Integer.parseInt(dateArray[1]), day = Integer
				.parseInt(dateArray[2]);

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

	/* (non-Javadoc)
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext ,
	 * javax.faces.component.UIComponent, java.lang.Object) ensures zero cannot be written for day or month either It
	 * also checks whether it should be verifying a long or short time
	 * @OAnguin */
	public static boolean isDateValid(String value, Boolean showTime) {

		if (value == null || StringUtils.isBlank(value))
			return false;

		try {

			if (!showTime)
				sqlDateFormatter.parse(value);
			else
				longDateFormatter.parse(value);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		String[] dateArray = StringUtils.split(value.toString().substring(0, 10), "-");

		int year = Integer.parseInt(dateArray[0]), month = Integer.parseInt(dateArray[1]), day = Integer
				.parseInt(dateArray[2]);

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

	// ensure when u roll up month changes if needs be
	public static Calendar rollUp(Calendar date) {

		int day = date.get(Calendar.DAY_OF_MONTH);
		int month = date.get(Calendar.MONTH);

		if (day == 31 && month == 12) {
			date.roll(Calendar.DAY_OF_MONTH, 1);
			date.set(Calendar.MONTH, 1);
			date.roll(Calendar.YEAR, 1);
			return date;
		}

		date.roll(Calendar.DAY_OF_MONTH, 1);
		if (day == 1) {
			date.roll(Calendar.MONTH, 1);
			return date;
		}

		return date;

	}

	// ensure when u roll up month changes if needs be
	public static Calendar rollDown(Calendar date) {

		int day = date.get(Calendar.DAY_OF_MONTH);
		int month = date.get(Calendar.MONTH);

		if (day == 1 && month == 1) {
			date.roll(Calendar.DAY_OF_MONTH, -1);
			date.roll(Calendar.MONTH, -1);
			date.roll(Calendar.YEAR, -1);
			return date;
		}

		if (day == 1) {
			date.roll(Calendar.DAY_OF_MONTH, -1);
			date.roll(Calendar.MONTH, -1);
			return date;
		}

		//
		// if(month == 9 || month == 4 || month == 6 || month == 11){
		// if (day == 30){
		// date.roll(date.MONTH, -1);
		// return date;
		// }
		// }
		//

		//
		// if(isLeapYear(year) && month ==3){
		// if (day == 29){
		// date.roll(date.MONTH, -1);
		// return date;
		// }
		// }
		//
		// if(month ==3){
		// if (day == 28){
		// date.roll(date.MONTH, -1);
		// return date;
		// }
		// }
		return date;
	}

	public static String getFormattedUtilDate(java.util.Date sDate) throws ParseException {
		return utilDateFormatterFsl.format(sDate);
	}
	
	public static String getFormattedUtilLongDate(java.util.Date sDate) throws ParseException {
		return longDateFormatter.format(sDate);
		
		
	}

	public static String getFormattedSqlDate(java.sql.Date sDate) throws ParseException {
		return utilDateFormatterFsl.format(sDate);
	}

	public String getTodayDateString() {

		return utilDateFormatterFsl.format(new Date(System.currentTimeMillis()));

	}

	public Date getTodayDate() {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date(System.currentTimeMillis()));

		// normalize the object
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date currentDate = new Date(calendar.getTime().getTime());

		return currentDate;

	}

	public Calendar getTodayCalender() {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(new Date(System.currentTimeMillis()));

		// normalize the object
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;

	}

	// rennis
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

	public static final Date add(Date date, int unit, int unitAmount) {

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(unit, unitAmount);
		return new Date(cal.getTime().getTime());

	}

	public static DateFormat getLongDateFormatter() {
		return longDateFormatter;
	}

	public static XMLGregorianCalendar getXMLGregorianCalendar(Date dateToConvert)
			throws DatatypeConfigurationException {
		GregorianCalendar date = new GregorianCalendar();
		date.setTime(dateToConvert);
		XMLGregorianCalendar dateXML = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);

		return dateXML;
	}

	/**
	 * @author jreid
	 * @param dateXML
	 * @return
	 */
	public static boolean isDateInPast(XMLGregorianCalendar dateXML) {
		if (dateXML == null)
			return true;

		java.sql.Date date = new java.sql.Date(dateXML.toGregorianCalendar().getTimeInMillis());
		java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

		if (date.before(today))
			return true;

		return false;
	}

	/**
	 * @author jreid
	 * @param dateXML
	 * @return
	 */
	//TO BE REMOVED
	public static boolean isDateInFuture(XMLGregorianCalendar dateXML) {
		if (dateXML == null)
			return true;

		java.sql.Date date = new java.sql.Date(dateXML.toGregorianCalendar().getTimeInMillis());
		java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

		if (today.before(date))
			return true;

		return false;
	}
	
	public static boolean isDateInFutureDateType(Date date) {
		if (date == null)
			return true;

		//java.sql.Date date = new java.sql.Date(dateXML.toGregorianCalendar().getTimeInMillis());
		java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

		if (today.before(date))
			return true;

		return false;
	}

	/**
	 * Converts an XMLGregorianCalendar to an instance of java.util.Date
	 * 
	 * @param xgc
	 *            Instance of XMLGregorianCalendar or a null reference
	 * @return java.util.Date instance whose value is based upon the value in the xgc parameter. If the xgc parameter is
	 *         null then this method will simply return null.
	 */
	public static java.util.Date getXMLGregorianCalendarAsDate(XMLGregorianCalendar xgc) {
		if (xgc == null) {
			return null;
		} else {
			return xgc.toGregorianCalendar().getTime();
		}
	}

	/**
	 * Carries out Court Date Validations
	 * 
	 * @throws InvalidFieldException
	 *             rbrooks
	 * @throws RequiredFieldMissingException
	 */
	public static int validateCourtDate(Date courtDateTime, Date issueDate) {
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
		
		
//		if (!(numOfDaysBetween > 3)) {
		if (!(numOfDaysBetween > 2)) { //Feb 26. modified to use 2 based on user feedback (email dated 13/01/2014 
			
			if (isHoliday)
			{
				return 3; //holiday

			}else
			{
				return 1; //less than 3 clear days
			}
			
		} else {
			if (DateUtils.isWeekDay(calCourtDate) == false) {
				return 2; //weekend

			} else {
				
				if (isHoliday)
					return 3; //holiday

			}
		}

		return 0;

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
	
	
	
	

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isWeekDay(Calendar date) {
		int saturday = Calendar.SATURDAY;
		int sunday = Calendar.SUNDAY;

		if (date.get(Calendar.DAY_OF_WEEK) != saturday && date.get(Calendar.DAY_OF_WEEK) != sunday) {
			return true;
		}

		return false;
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
	
	/**
	 * Returns XML Gregorian Date with current time
	 * @throws DatatypeConfigurationException 
	 * 
	 */
	 public static XMLGregorianCalendar getCurrentXMLGregCalendarDate() throws DatatypeConfigurationException
	 {
		 GregorianCalendar c = new GregorianCalendar();
		    c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
		    c.set(Calendar.MINUTE, 0);
		    c.set(Calendar.SECOND, 0);
		    c.setTime(c.getTime());
		    XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		    return date2;
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
		
		  /*
	     * Converts XMLGregorianCalendar to java.util.Date in Java
	     * javarevisited.blogspot.com/2013/02/convert-xmlgregoriancalendar-to-date-xmlgregoriancalendar-java-example-tutorial.html#ixzz3Onx0uiDv
	     */
	    public static Date toDate(XMLGregorianCalendar calendar){
	        if(calendar == null) {
	            return null;
	        }
	        return calendar.toGregorianCalendar().getTime();
	    }


		
}
