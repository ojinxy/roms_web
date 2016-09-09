/**
 *  
 *  
 */
package org.fsl.roms.service.action.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.fsl.application.ApplicationProperties;
import org.fsl.roms.jqueryobject.CalendarEvent;
import org.fsl.roms.util.DateUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.webservices.RoadOperation;


/**
 * @author oanguin
 *
 */
//@Controller()
//@RequestMapping("/roadOperation")
public class RoadOperationController 
{

	public RoadOperationController() 
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */

	@RequestMapping(value="roadOpNamesAutoComp",method=RequestMethod.GET)
	@Cacheable(value={"roadOpNamesAutoComp"})
	public @ResponseBody List<String> roadOperationNameAutoComplete(@RequestParam(value="term",required=true) String term)
	{
		System.out.println("Getting Names List");
		RoadOperation roadOpService = new RoadOperation();
	
		RoadOperationCriteriaBO roadOpCriteria = new RoadOperationCriteriaBO();
		roadOpCriteria.setOperationName(term);
		
		List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();
		
		try 
		{
			roadOps = roadOpService.lookupRoadOperation(roadOpCriteria);
			
		} catch (Exception e) 
		{
			
		}
		
		List<String> returnList = new ArrayList<String>();
		
		
		 for(RoadOperationBO roadOp : roadOps)
			 returnList.add(roadOp.getOperationName());
		 
		 
		System.out.println("End Getting Names List");
		 return returnList;
	}
	
	
	@RequestMapping(value="roadOpCalendar",method=RequestMethod.GET)
	@Cacheable(value={"roadOpCalendar"})
	public @ResponseBody List<CalendarEvent> roadOperationCalendar(@RequestParam(value="start",required=true) String startUnixTimeStamp,
			@RequestParam(value="end",required=true) String endUnixTimeStamp,
			@RequestParam(value="actualDateCrit",required=true) boolean actualDateCrit) throws DatatypeConfigurationException
	{
		Date startDate = new Date(Long.parseLong(startUnixTimeStamp) * 1000);
		
		Date endDate = new Date(Long.parseLong(endUnixTimeStamp) * 1000);		
		
		RoadOperation roadOpService = new RoadOperation();

		RoadOperationCriteriaBO roadOpCriteria = new RoadOperationCriteriaBO();
		
		roadOpCriteria.setScheduledDTime(!actualDateCrit);
		
		roadOpCriteria.setOperationStartDate(startDate);
		
		roadOpCriteria.setOperationEndDate(endDate);
		
		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		
		List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();
		
		try 
		{
			roadOps = roadOpService.lookupRoadOperationWithDateRange(roadOpCriteria);
			
		} catch (Exception e) 
		{
			
		}
		
		
		 for(RoadOperationBO roadOp : roadOps)
		 {
			 
			 Long startDateL = null;
			 Long endDateL = null;
			 
			 if(actualDateCrit)
			 {
				 if(roadOp.getActualStartDate() != null)
					 //startDateL = roadOp.getActualStartDate().toGregorianCalendar().getTimeInMillis()/1000;
					 startDateL = roadOp.getActualStartDate().getTime()/1000;
				 
				 if(roadOp.getActualEndDate() != null)
					 //endDateL = roadOp.getActualEndDate().toGregorianCalendar().getTimeInMillis()/1000;
					 endDateL = roadOp.getActualEndDate().getTime()/1000;
			 }
			 else
			 {
				 if(roadOp.getScheduledStartDate() != null)
					 //startDateL = roadOp.getScheduledStartDate().toGregorianCalendar().getTimeInMillis()/1000;
					 startDateL = roadOp.getScheduledStartDate().getTime()/1000;
			
				 
				 if(roadOp.getScheduledEndDate() != null)
					 //endDateL = roadOp.getScheduledEndDate().toGregorianCalendar().getTimeInMillis()/1000;
					 endDateL = roadOp.getScheduledEndDate().getTime()/1000;
			 }
				 
			 CalendarEvent calendarEvent = new CalendarEvent(roadOp.getRoadOperationId()/*id*/, 
						roadOp.getOperationName()/*title*/, 
						startDateL/*start*/, 
						endDateL/*end*/);
			 
			 calendarEvent.status = roadOp.getStatusDescription();
			 calendarEvent.actualEndDateTime = roadOp.getActualEndDtime().toString();
			 calendarEvent.actualStartDateTime = roadOp.getActualStartDtime().toString();
			 calendarEvent.scheduledEndDateTime = roadOp.getScheduledEndDtime().toString();
			 calendarEvent.scheduledStartDateTime = roadOp.getScheduledStartDtime().toString();
			 
			 if(roadOp.getStatusDescription().trim().equalsIgnoreCase("scheduling"))
			 {
				 calendarEvent.borderColor = "#CC9933";
				 calendarEvent.backgroundColor = "#CC9933";
				 calendarEvent.textColor = "white";
			 }
			 else  if(roadOp.getStatusDescription().trim().equalsIgnoreCase("closed"))
			 {
				 calendarEvent.borderColor = "red";
				 calendarEvent.backgroundColor = "red";
				 calendarEvent.textColor = "white";
			 }
			 else  if(roadOp.getStatusDescription().trim().equalsIgnoreCase("started"))
			 {
				 calendarEvent.borderColor = "green";
				 calendarEvent.backgroundColor = "green";
				 calendarEvent.textColor = "white";
			 }
			 else  if(roadOp.getStatusDescription().trim().equalsIgnoreCase("completed"))
			 {
				 calendarEvent.borderColor = "orange";
				 calendarEvent.backgroundColor = "orange";
				 calendarEvent.textColor = "white";
			 }
			 else  if(roadOp.getStatusDescription().trim().equalsIgnoreCase("no action"))
			 {
				 calendarEvent.borderColor = "orange";
				 calendarEvent.backgroundColor = "white";
				 calendarEvent.textColor = "red";
			 }
			 else  if(roadOp.getStatusDescription().trim().equalsIgnoreCase("terminated"))
			 {
				 calendarEvent.borderColor = "orange";
				 calendarEvent.backgroundColor = "black";
				 calendarEvent.textColor = "red";
			 }
			 else  if(roadOp.getStatusDescription().trim().equalsIgnoreCase("cancelled"))
			 {
				 calendarEvent.borderColor = "orange";
				 calendarEvent.backgroundColor = "#FFFFCC";
				 calendarEvent.textColor = "red";
			 }
			 else
			 {
				 calendarEvent.borderColor = "blue";
				 calendarEvent.backgroundColor = "blue";
				 calendarEvent.textColor = "white";
			 }
			 
			
			 calendarEvents.add(calendarEvent);
		 }
		 
		return calendarEvents;
		
	}
	
}
