/**
 * 
 */
package org.fsl.roms.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.time.DateUtils;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.service.action.RoadOperationsServiceAction;
import org.fsl.roms.util.StringUtil;
import org.joda.time.DateTime;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.webservices.RoadOperation;

/**
 * @author oanguin
 *
 */
public class RoadOpCalView  implements Serializable
{

	private static final long serialVersionUID = 1L;
	private ScheduleModel lazyScheduleModel;
	private Boolean actualDateCrit;
	
	
	RoadOperationsServiceAction roadOperationsServiceAction;
	
	

	public RoadOperationsServiceAction getRoadOperationsServiceAction()
	{
		return roadOperationsServiceAction;
	}

	public void setRoadOperationsServiceAction(
			RoadOperationsServiceAction roadOperationsServiceAction)
	{
		this.roadOperationsServiceAction = roadOperationsServiceAction;
	}

	public RoadOpCalView(final RoadOperationsServiceAction roadOperationsServiceAction) 
	{
		actualDateCrit = new Boolean(false);
		
		lazyScheduleModel = new LazyScheduleModel()
		{

			@Override
			public void loadEvents(Date startDate, Date endDate) 
			{
				
				
				/*Check if the dates are longer than a day is so then the code will track
				 * the events being added to a day to ensure that only three events are added to a 
				 * day*/
				final DateTime startDateJoda = new DateTime(startDate.getTime());
			    final DateTime endDateJoda = new DateTime(endDate.getTime());
			    Integer daysBetween = org.joda.time.Days.daysBetween(startDateJoda, endDateJoda).getDays();
			    boolean isSameDay;
			    
			    if(daysBetween > 1)
			    	isSameDay = false;
			    else
			    	isSameDay = true;
			    	
				
								
				RoadOperation roadOpProxy = new RoadOperation();
				
				//roadOpProxy._getDescriptor().setEndpoint( ApplicationProperties.get("application.webservices.url").trim() + "RoadOperationService");
				
				RoadOperationCriteriaBO roadOpCriteria = new RoadOperationCriteriaBO();
				
				roadOpCriteria.setScheduledDTime(!actualDateCrit);
				
	
				
				List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();
				
				try 
				{
					roadOpCriteria.setOperationStartDate(startDate);
					
					roadOpCriteria.setOperationEndDate(endDate);
					
					if(! roadOperationsServiceAction.hasSpecialPermissions()){
						roadOpCriteria.setOfficeLocCode(roadOperationsServiceAction.getRomsLoggedInUser().getLocationCode());
					}
					
					roadOps = roadOpProxy.lookupRoadOperationWithDateRange(roadOpCriteria);
					
				
						
			
					Date currentDay = null;
					
					int countEventsForDay = 0;
					
					/*If the calendar is displaying for a week or a month then sort road operation list.*/
					if(!isSameDay)
						roadOps = sortRoadOperationsByDate(roadOps, actualDateCrit);
					
					
					
					 for(RoadOperationBO roadOp : roadOps)
					 {
						 
							 Date startDateL = null;
							 Date endDateL = null;
							 
							 if(actualDateCrit)
							 {
								 if(roadOp.getActualStartDate() != null)
									 startDateL = roadOp.getActualStartDtime();
								 
								 if(roadOp.getActualEndDate() != null)
									 endDateL = roadOp.getActualEndDtime();
							 }
							 else
							 {
								 if(roadOp.getScheduledStartDate() != null)
									 startDateL = roadOp.getScheduledStartDtime();
							
								 
								 if(roadOp.getScheduledEndDate() != null)
									 endDateL = roadOp.getScheduledEndDtime();
							 }
							 
							 
							 /*Do not add event if both dates are null.*/
							 if(startDateL == null || endDateL == null)
								 continue;
							 /*Add Events in normal manner based on the dateDif. If the dateDif is greater than one day
							  * then the function will test currentDay Day and use currentDayCount to see how much events have been added
							  * to a day. Once the count reaches 4 then a more event will be added on no more events for that day will be 
							  * added.*/
							 if(! isSameDay)
							 {
								 if(currentDay == null)
									 currentDay = startDateL;
								 
								 if(DateUtils.isSameDay(currentDay, startDateL))
								 {
									 countEventsForDay += 1;
								 }
								 else
								 {
									 countEventsForDay = 1;
									 currentDay = startDateL;
								 }
									 
								 if(countEventsForDay < 4)
								 {
									 this.addEvent(roadOp, startDateL, endDateL);
								 }
								 else if(countEventsForDay == 4)
								 {						 
									 
									 this.addEvent(new RoadOpCalEvent("eventMor", null, null, endDateL, startDateL, "More", true, true));
								 }
								 
							 }
							 else
							 {
								 this.addEvent(roadOp, startDateL, endDateL);
							 }
					 	}
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						
						FacesContext.getCurrentInstance().addMessage("",
				                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error retrieving road operation events.", "Error retrieving road operation events."));
						
						RequestContextHolder.getRequestContext().getMessageContext().addMessage(
								new MessageBuilder().error().defaultText("Error retrieving road operation events.")
								.build()); 
					}
			
				}
			
				private void addEvent(RoadOperationBO roadOp, Date startDateL, Date endDateL)
				{
					String cssClass = getCSSClass(roadOp.getStatusDescription());
					 
					 String title = roadOp.getOperationName().replaceAll("\'", "\u02C8");				 
					 
					 ScheduleEvent event = new RoadOpCalEvent(cssClass, roadOp, roadOp.getRoadOperationId().toString(), endDateL, startDateL, title, false, true);
					 
					 event.setId(roadOp.getRoadOperationId().toString());
					 
					 addEvent(event);
				}
	
			};
		}
	
	

	public RoadOpCalView()
	{
		super();

	}

	public ScheduleModel getLazyScheduleModel() {
		return lazyScheduleModel;
	}

	public void setLazyScheduleModel(ScheduleModel lazyScheduleModel) {
		this.lazyScheduleModel = lazyScheduleModel;
	}

	public Boolean getActualDateCrit() {
		return actualDateCrit;
	}

	public void setActualDateCrit(Boolean actualDateCrit) {
		this.actualDateCrit = actualDateCrit;
	}
	
	private String getCSSClass(String status)
	{
		if(! StringUtil.isSet(status))
		{
			return "";
		}
		else if(status.trim().equalsIgnoreCase("scheduling"))
		 {
			 return "eventSch";
		 }
		 else  if(status.trim().equalsIgnoreCase("closed"))
		 {
			 return "eventClo";
		 }
		 else  if(status.trim().equalsIgnoreCase("started"))
		 {
			 return "eventSta";
		 }
		 else  if(status.trim().equalsIgnoreCase("completed"))
		 {
			 return "eventCom";
		 }
		 else  if(status.trim().equalsIgnoreCase("no action"))
		 {
			 return "eventNoa";
		 }
		 else  if(status.trim().equalsIgnoreCase("terminated"))
		 {
			 return "eventTer";
		 }
		 else  if(status.trim().equalsIgnoreCase("cancelled"))
		 {
			 return "eventCan";
		 }
		 else
		 {
			 return "eventUnk";
		 }
	}
	
	private List<RoadOperationBO> sortRoadOperationsByDate(List<RoadOperationBO> roadOpToSort, final Boolean actualDateCrit)
	{
		Collections.sort(roadOpToSort, 
				new Comparator<RoadOperationBO> ()
                { public int compare (final RoadOperationBO a, final RoadOperationBO d)
                	{ 
                		if(actualDateCrit)
                		{
                			if(a.getActualStartDtime() != null && d.getActualStartDtime() != null){
                				if(a.getActualStartDtime().compareTo(d.getActualStartDtime()) > 0){
                					return 1;
                				}
                				else if(a.getActualStartDtime().compareTo(d.getActualStartDtime()) < 0){
                					return -1;
                				}
                				else{
                					return 0;
                				}
                			}
                			else{
                				return -1;
                			}
                			
                			
                		}
                		else
                		{
                			
                 			if(a.getScheduledStartDtime() != null && d.getScheduledStartDtime() != null){
                				if(a.getScheduledStartDtime().compareTo(d.getScheduledStartDtime()) > 0){
                					return 1;
                				}
                				else if(a.getScheduledStartDtime().compareTo(d.getScheduledStartDtime()) < 0){
                					return -1;
                				}
                				else{
                					return 0;
                				}
                			}
                			else{
                				return -1;
                			}
                
       
                		}
                	}

			
                });
		
		return roadOpToSort;
	}
	
	/**
	 * This function listens to when an event is selected on the calendar.
	 * @param event
	 */
	public void listenToCalEventClick(SelectEvent event)
	{
		
		System.out.println("In Cal Event!!!!");
		RoadOpCalEvent calEvent = (RoadOpCalEvent)event.getObject();
		
		RequestContext requestContext = RequestContextHolder.getRequestContext();
		
		/*Check is calendar event is a real event and that it has a valid road operation.*/
		if(calEvent.getRoadOp() != null)
		{
			/*Assume that this is a valid road operation calendar event. Set the details for show in pop up.*/
			RoadOperationBO roadOp = calEvent.getRoadOp();
			
			requestContext.getFlowScope().put("roadOperationId",roadOp.getRoadOperationId());
			
			requestContext.getFlowScope().put("roadOp",roadOp);	
			
			requestContext.getFlowScope().put("opName", roadOp.getOperationName());
			
			requestContext.getFlowScope().put("opStatus", roadOp.getStatusDescription());
			
			requestContext.getFlowScope().put("opStatusId", roadOp.getStatusId());
			
			requestContext.getFlowScope().put("opSchStartDate", roadOp.getScheduledStartDtime());
			
			requestContext.getFlowScope().put("opSchEndDate", roadOp.getScheduledEndDtime());
			
			requestContext.getFlowScope().put("opActStartDate", roadOp.getActualStartDtime());
			
			requestContext.getFlowScope().put("opActEndDate", roadOp.getActualEndDtime());
			
			requestContext.getFlowScope().put("initialDate", calEvent.getStartDate());
			
			
		}
		else
		{
			/*Assume that this is a more event and try to view the day based on the selected input.*/
			/*Changing calendar to day view is done using JQuery Full Calendar Options on the client side. http://arshaw.com/fullcalendar/docs/current_date/gotoDate/*/
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(calEvent.getStartDate());
			requestContext.getFlowScope().put("selectedDay",cal.get(Calendar.DAY_OF_MONTH));
			requestContext.getFlowScope().put("selectedMonth",cal.get(Calendar.MONTH));
			requestContext.getFlowScope().put("selectedYear",cal.get(Calendar.YEAR));
			
			
		}
		
		
		
	}
}
