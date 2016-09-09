package fsl.ta.toms.roms.util.holiday;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.LocalDate;

import fsl.ta.toms.roms.exception.RequiredFieldMissingException;

public class HolidayValidator {


	public boolean isHoliday(Date date) throws RequiredFieldMissingException
	{
		boolean result = false;
	
		//Check for required field 
		if (date == null)
		{
			throw new RequiredFieldMissingException("A valid date is required");
		}
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		int year = cal1.get(Calendar.YEAR);
	
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
	    String formattedDate = fmt.format(date);
		

		
		String newYearDay = fmt.format(newYearsDay(year));
		String labourDay = fmt.format(labourDay(year));
		String emancipation = fmt.format(emancipationDay(year));
		String independce = fmt.format(independenceDay(year));
		String christmasDay = fmt.format(christmasDay(year));
		String boxingDay = fmt.format(boxingDay(christmasDay(year)));
		String heroesDay = fmt.format(heroesDay(year));
		String ashWed = fmt.format(AshWednesday(year));
		String easterMonday = fmt.format(EasterMonday(year));
		String goodFriday = fmt.format(GoodFridayObserved(year));
		
	    
		if (formattedDate.equals(newYearDay)  || 
				formattedDate.equals(labourDay)  || 
				formattedDate.equals(emancipation) || 
				formattedDate.equals(independce) || 
				formattedDate.equals(christmasDay) || 
				formattedDate.equals(boxingDay) || 
				formattedDate.equals(heroesDay)|| 
				formattedDate.equals(ashWed)|| 
				formattedDate.equals(easterMonday)  || 
				formattedDate.equals(goodFriday))
			{
				result = true;
			}
			 
	
		return result;
	}
	
	
	
	
	private static Date heroesDay(int nYear)
	 {
		 Date date = getNthDow(9, nYear, 2, 3).getTime();
		 return date;
	
	 }
	
	/**
	 * gets the Nth day of week in a month. For example, the 3rd monday in October.
	 * @param month
	 * @param year
	 * @param dayOfWeek
	 * @param n
	 * @return
	 */
	private static Calendar getNthDow(int month, int year, int dayOfWeek, int n) {
	    Calendar cal = Calendar.getInstance();
	    cal.set(year, month, 1);
	    cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
	    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, n);
	    return (cal.get(Calendar.MONTH) == month) && (cal.get(Calendar.YEAR) == year) ? cal : null;
	}
	
    
	
	private static Date boxingDay(Date christmasDay)
	 {
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(christmasDay);
		 cal.add(Calendar.DAY_OF_MONTH, 1);
		 
		 Date date = cal.getTime();
		 return date; //it is ok if boxing day falls on a sunday.
	
	 }
	
	
	private static Date christmasDay(int nYear)
	 {
		 Calendar cal = Calendar.getInstance();
		 cal.set(nYear,Calendar.DECEMBER, 25);
		 Date date = cal.getTime();
		 return adjustForSunday(date);
	
	 }
	

	private static Date independenceDay(int nYear)
	 {
		 Calendar cal = Calendar.getInstance();
		 cal.set(nYear,Calendar.AUGUST, 6);
		 Date date = cal.getTime();
		 return adjustForSunday(date);
	
	 }
	
	
	private static Date emancipationDay(int nYear)
	 {
		 Calendar cal = Calendar.getInstance();
		 cal.set(nYear,Calendar.AUGUST, 1);
		 Date date = cal.getTime();
		 return adjustForSunday(date);
	
	 }
	
	
	private static Date labourDay(int nYear)
	 {
		 Calendar cal = Calendar.getInstance();
		 cal.set(nYear, Calendar.MAY, 23);
		 Date date = cal.getTime();
		 return adjustForSunday(date);
	
	 }
	 
	
	
	 private static Date newYearsDay(int nYear)
	 {
		 Calendar cal = Calendar.getInstance();
		 cal.set(nYear, Calendar.JANUARY, 1);
		 Date date = cal.getTime();
		 return adjustForSunday(date);
		 
	 }
	 
	
	private static Date adjustForSunday(Date initDate)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(initDate);
		
		int dayOfWeek = cal1.get(Calendar.DAY_OF_WEEK);
		
		if(dayOfWeek == Calendar.SUNDAY)
		{
			//add one day
			cal1.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		Date date = cal1.getTime();
		return date;
	}
	 
	
	 private static Date AshWednesday(int nYear)
	 {
		 Date goodFri = GoodFridayObserved(nYear);
		 
		 return minusDays(goodFri,44);
		
	 }
	 
	 
	 
	 private static Date EasterMonday(int nYear)
		{
		 Date goodFri = GoodFridayObserved(nYear);
		 	 
		 return addDays(goodFri,3);
		
		}
	 
	 
	 private static Date GoodFridayObserved(int nYear)
		{
		// Get Easter Sunday and subtract two days
		int nEasterMonth	= 0;
		int nEasterDay		= 0;
		int nGoodFridayMonth	= 0;
		int nGoodFridayDay	= 0;
		Date dEasterSunday;
			
		dEasterSunday = EasterSunday(nYear);
		//rack nEasterMonth = dEasterSunday.getMonth(); 
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dEasterSunday);
		nEasterMonth = cal1.get(Calendar.MONTH);
		
		//rack nEasterDay = dEasterSunday.getDate();
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dEasterSunday);
		nEasterDay = cal2.get(Calendar.DAY_OF_MONTH);
		
		
		if (nEasterDay <= 3 && nEasterMonth == 3) // Check if <= April 3rd
		    {
		    switch(nEasterDay)
			{
			case 3 : 
			    nGoodFridayMonth = nEasterMonth - 1;
			    nGoodFridayDay   = nEasterDay - 2;
			    break;
			case 2 :
			    nGoodFridayMonth = nEasterMonth - 1;
			    nGoodFridayDay   = 31;
			    break;
			case 1 :
			    nGoodFridayMonth = nEasterMonth - 1;
			    nGoodFridayDay   = 31;
			    break;
			default:
			    nGoodFridayMonth = nEasterMonth;
			    nGoodFridayDay   = nEasterDay - 2;
			}
		    }
		else
		    {
		    nGoodFridayMonth = nEasterMonth;
		    nGoodFridayDay   = nEasterDay - 2;
		    }

		//rack return new Date(nYear, nGoodFridayMonth, nGoodFridayDay);
		
		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nGoodFridayMonth, nGoodFridayDay);
		Date date = cal.getTime();
		return date;
		
		}

	 private static Date EasterSunday(int nYear)
		{ 
		int nA			= 0;
		int nB			= 0;
		int nC			= 0;	
		int nD			= 0;
		int nE			= 0;
		int nF			= 0;
		int nG			= 0;
		int nH			= 0;
		int nI			= 0;
		int nK			= 0;
		int nL			= 0;
		int nM			= 0;
		int nP			= 0;
		int nYY			= 0;
		int nEasterMonth	= 0;
		int nEasterDay		= 0;

		// Calculate Easter
		nYY = nYear;
		if (nYear < 1900) 
		    { 
		    // if year is in java format put it into standard
		    // format for the calculation
		    nYear += 1900; 
		    }
		nA = nYear % 19;
		nB = nYear / 100;
		nC = nYear % 100;
		nD = nB / 4;
		nE = nB % 4;
		nF = (nB + 8) / 25;
		nG = (nB - nF + 1) / 3;
		nH = (19 * nA + nB - nD - nG + 15) % 30;
		nI = nC / 4;
		nK = nC % 4;
		nL = (32 + 2 * nE + 2 * nI - nH - nK) % 7;
		nM=  (nA + 11 * nH + 22 * nL) / 451;

		//  [3=March, 4=April]
		nEasterMonth = (nH + nL - 7 * nM + 114) / 31;
		--nEasterMonth;
		nP = (nH + nL - 7 * nM + 114) % 31;

		// Date in Easter Month.
		nEasterDay = nP + 1;

		// Incorrect for our earlier correction.
		nYear -= 1900;

		// Populate the date object...
		// rack return new Date(nYear, nEasterMonth, nEasterDay);
		
		Calendar cal = Calendar.getInstance();
		cal.set(nYear, nEasterMonth, nEasterDay);
		Date date = cal.getTime();
		return date;
		} 

	   
	    
	 private static Date minusDays(Date d, int days)
		{
		    Calendar c = Calendar.getInstance();
		    c.setTime(d);
		    c.add(Calendar.DATE, -days);
		    d.setTime( c.getTime().getTime() );
		    
		    return d;
		}
	    
	    
	 private static Date addDays(Date d, int days)
		{
		    Calendar c = Calendar.getInstance();
		    c.setTime(d);
		    c.add(Calendar.DATE, days);
		    d.setTime( c.getTime().getTime() );
		    
		    return d;
		}
	    
	 private static boolean isLeapYear(int year) {
			if ((year % 100) == 0 && (year % 400) == 0)
				return true;
			else if ((year % 4) == 0)
				return true;
			else
				return false;
		}
	    
	 
	 
}
