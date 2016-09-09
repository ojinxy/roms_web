/**
 * Created By: oanguin
 * Date: May 8, 2013
 *
 */
package fsl.ta.toms.roms.search.criteria.impl;

import java.util.Calendar;
import java.util.Date;

import fsl.ta.toms.roms.search.criteria.SearchCriteria;
import fsl.ta.toms.roms.util.DateUtils;

/**
 * This is the parent report search criteria. It has some methods which all report criteria should be able to use.
 * @author oanguin
 * Created Date: May 8, 2013
 */
public class ParentReportCriteria implements SearchCriteria 
{
	public enum searchDate{START,END};
	
	/**
	 * This interface will be implemented by local classes to show criteria items as strings based
	 * on if they are date, integers, strings, etc.
	 * @author oanguin
	 * Created Date: May 8, 2013
	 */
	interface SearchCrieriaList
	{
		String criterionString(String itemName,Object item);
	}
	
	public class dateSearchCrieriaList implements SearchCrieriaList
	{

		@Override
		public String criterionString(String itemName, Object item) 
		{
			
			if(item != null)
			{
				
				return itemName + ": "+ DateUtils.formatDate("yyyy-MM-dd",((Date)item)) + "; ";
			}
			else
				return "";
			
			
		}
		
	}
	
	public class integerSearchCrieriaList implements SearchCrieriaList
	{

		@Override
		public String criterionString(String itemName, Object item) 
		{
			if(item != null && ((Integer)item) > 0)
				return itemName + ": "+ ((Integer)item).toString() + "; ";
			else
				return "";
			
			
		}
		
	}
	
	public class stringSearchCrieriaList implements SearchCrieriaList
	{

		@Override
		public String criterionString(String itemName, Object item) 
		{
			if(item != null && !((String)item).isEmpty())
				return itemName + ": "+ item + "; ";
			else
				return "";
			
			
		}
		
	}
	
	/**
	 * This function takes the search item name and the item o build a string similar to 
	 * {Area:ALL}. This function is generic and will format date, string and integer. Other values are not supported
	 * and the local SearchCrieriaList would need to be implemented by a new local class and code added accordingly.  
	 * @param itemName
	 * @param item
	 * @return
	 */
	public String getCriterionString(String itemName,Object item)
	{
		if(item == null)
			return "";
		
		SearchCrieriaList searchCriteriaList = null;
		
		if(item.getClass().equals(Date.class))
		{
			
			searchCriteriaList = new dateSearchCrieriaList();
		}
		else if(item.getClass().equals(Integer.class))
		{
			
			searchCriteriaList = new integerSearchCrieriaList();
		}
		else if(item.getClass().equals(String.class))
		{
			
			searchCriteriaList = new stringSearchCrieriaList();
		}
		
		
		if(searchCriteriaList != null)
		{
			
			return searchCriteriaList.criterionString(itemName, item);
		}
		else
		{
			return "";
		}
	}
	
	public Date searchDateFormater(Date date,searchDate type)
	{
		Calendar outputDate = Calendar.getInstance();
		
				
		if(searchDate.START == type)
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
}
