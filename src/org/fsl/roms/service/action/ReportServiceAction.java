package org.fsl.roms.service.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.fsl.roms.businessobject.RegionBO;
import org.fsl.roms.util.DateUtils;
import org.fsl.servlet.BasePdfServlet;
import org.fsl.servlet.PdfServlet;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.EventAuditReportBO;
import fsl.ta.toms.roms.bo.EventAuditReportResultsBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.EventAuditReportCriteriaBO;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class ReportServiceAction extends BaseServiceAction {
	
	public ReportServiceAction() {
		super();
	}
	
	public Event init(RequestContext context) { 
		
		EventAuditReportCriteriaBO criteria = (EventAuditReportCriteriaBO) context.getFlowScope().get("criteria");
		criteria.setSortBy("eventdtime");
		context.getFlowScope().put("criteria", criteria);
		return success();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<LMISUserViewBO> eventAuditUserNameAutoComplete(String term)
	{
		
		RequestContext context = RequestContextHolder.getRequestContext();
	
		List<LMISUserViewBO> userList = (List<LMISUserViewBO>)context.getFlowScope().get("userList");
	
		
		List<LMISUserViewBO> returnList = new ArrayList<LMISUserViewBO>();
		
		
		 for(LMISUserViewBO user : userList)
		 {
			 if(user.getLastName().trim().toLowerCase().contains(term))
			 {
				 returnList.add(user);
			 }
		 }
			
		 
		 

		 return returnList;
	}
	
	public Event getLMISUserList(RequestContext context)
	{ 
		
		List<LMISUserViewBO> searchUserList= new ArrayList<LMISUserViewBO>();
		
			try {
				searchUserList = getStaffUserMappingService().lookupAllUsers();
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
			
			context.getFlowScope().put("userList", searchUserList);
			
		return success();
	}
	
	public Event getEventTypeList(RequestContext context) {
		
		List<RefCodeBO> events = super.getActivRefInfo("roms_cd_event");

		List<SelectItem> eventTypeList = new ArrayList<SelectItem>();

		Collections.sort(events, new Comparator<RefCodeBO>() {
			public int compare(RefCodeBO result1, RefCodeBO result2) {
				return result1.getDescription().toLowerCase().compareTo(result2.getDescription().toLowerCase());
			}
		});
		
		
		for (RefCodeBO event : events)
			eventTypeList.add(new SelectItem(event.getCode(), event.getDescription()));
			
		context.getFlowScope().put("eventTypeList", eventTypeList);
		
		return success();
		
	}
	
	public Event getRegionList(RequestContext context) {
		
		HashMap<String, String> criteria = new HashMap<String, String>();

		List<RefCodeBO> listRegions = super.getRefInfo("roms_lmis_ta_office_location_view", criteria);

		List<RegionBO> selectListRegions = new ArrayList<RegionBO>();

		RegionBO regionBo = new RegionBO();

		if (listRegions != null) {

			for (RefCodeBO refCode : listRegions) {

				regionBo.setId(refCode.getCode());
				regionBo.setDescription(refCode.getDescription());
				selectListRegions.add(regionBo);
				regionBo = new RegionBO();

			}
		}
		
		context.getFlowScope().put("regionList", selectListRegions);
		
		return success();
		
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event generate(RequestContext context) {
		Boolean pass= true;
		EventAuditReportCriteriaBO criteria = (EventAuditReportCriteriaBO) context.getFlowScope().get("criteria");
		
		List<RegionBO> regions = (List<RegionBO>)context.getFlowScope().get("taOfficeRegion");
		
		if(regions != null && regions.size() > 0){
			for(RegionBO region : regions){
				criteria.getTAOfficeRegion().add(region.getId());
			}
		}
		 
		pass = valid(context, criteria);
		
		
		//if(context.getMessageContext().hasErrorMessages()) return error();
		if(pass){
			try {
			
				EventAuditReportBO eventAuditReportBO
					= getReportService().eventAuditReport(
						criteria, 
						getRomsLoggedInUser().getUsername(), 
						getRomsLoggedInUser().getLocationCode());
				
				if(!eventAuditReportBO.getResults().isEmpty())
					getReportInPdf(
						eventAuditReportBO.getResults(),
						context,
						criteria);
				
			} catch (NoRecordFoundException e) {
				
				context.getMessageContext().addMessage(new MessageBuilder().error().code("Norecordsfound").build());
				return error();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				context.getMessageContext().addMessage(new MessageBuilder().error().code("SystemError").build());
				criteria.getTAOfficeRegion().clear();
				return error();
				
			}
			
			criteria.getTAOfficeRegion().clear();
			
			//return success();
		}
		
		if(pass)
			return success();
		else
			return error();
		
	}
	
	private Boolean valid(RequestContext context, EventAuditReportCriteriaBO criteria) {
		Boolean pass = true;
		
		if(criteria.getStartDate() == null && criteria.getEndDate() == null)
		{
			context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Event Date").build());
			pass = false;
		}else{
			pass = validateStartNEndDate(context, criteria.getStartDate(), 
					criteria.getEndDate(), "Event", false) && pass;
		}
		/*if(criteria.getStartDate() == null)
		{
			context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Event Date").build());
			return;
		}
			
		if(criteria.getEndDate() == null)
		{
			context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Event Date").build());
		
		if(criteria.getStartDate() != null && criteria.getEndDate() != null){
			if(criteria.getEndDate().compare(criteria.getStartDate()) == DatatypeConstants.LESSER)
				addErrorMessageText(context, "Event End date cannot be before Event Start Date");
		}
			return;
		}
		
		if(criteria.getEndDate().compare(criteria.getStartDate()) == DatatypeConstants.LESSER)
		{
			addErrorMessageText(context, "End date cannot be before Start Date");
			return;
		}
		
		if(DateUtils.isDateInFuture(criteria.getStartDate()))
		{	
			addErrorMessageText(context, "Start Date Cannot Be a Future Date");
			return;
		}
		
		if(DateUtils.isDateInFuture(criteria.getEndDate()))
		{	
			addErrorMessageText(context, "End Date Cannot Be a Future Date");
			return;
		}
		
		if(criteria.getStartDate() != null && criteria.getEndDate() != null){
			if(!BaseServiceAction.validateDateRangeLimit
					(DateUtils.toDate(criteria.getStartDate()),
							DateUtils.toDate(criteria.getEndDate()))){
				addErrorMessageWithParameter(context,"DateRangeLimit", "Event");
			}
		}*/
		return pass;
		
	}
	
	public void clear(RequestContext context) {
		
	}
	
	private void getReportInPdf(
		List<EventAuditReportResultsBO> results, 
		RequestContext context,
		EventAuditReportCriteriaBO criteria) {
		
		String fileName = "/reports/EventAuditReport.jasper";
		
		System.err.println("record count: " + results.size());
		
		try {
		  
			final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(results);
			
			HttpSession session = (HttpSession) 
				((HttpServletRequest) context.getExternalContext().getNativeRequest()).getSession();
			
			Map<String, Object> reportMap = new HashMap<String, Object>();
			reportMap.put(PdfServlet.REPORT_MAP_PARAMETERS_KEY, getReportParameters(criteria));
			reportMap.put(PdfServlet.REPORT_MAP_DATASOURCE_KEY, dataSource);
			reportMap.put(PdfServlet.REPORT_MAP_REPORTPATH_KEY, fileName);
			
			session.setAttribute(PdfServlet.REPORT_MAP_SESSION_ATTRIBUTE, reportMap);
			session.setAttribute(BasePdfServlet.PDF_FILE_NAME_SESSION_ATTRIBUTE, fileName + ".pdf");
		  
			context.getFlashScope().put("openReportWindow", true);
		  
		} catch (Exception exception) { 
			exception.printStackTrace(); 
		}
		 
	}
	
	private Map<String, Object> getReportParameters(EventAuditReportCriteriaBO criteria) {
		
		final Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("systemName", "Road Operation Management System");
		parameters.put("printDate", DateUtils.formatDate("yyyy-MM-dd hh:mm a", Calendar.getInstance().getTime()));
		parameters.put("userName", getRomsLoggedInUser().getUsername());
		parameters.put("location", getRomsLoggedInUser().getLocation());
		parameters.put("searchDates", getSearchDatesParameter(criteria));
		parameters.put("searchCriteria", getSearchCriteria(criteria));
		
		return parameters;
		
	}

	private String getSearchDatesParameter(EventAuditReportCriteriaBO criteria) {
		
		try {
			
			return 
				"For the period " + DateUtils.getFormattedUtilDate(criteria.getStartDate()) + 
				" to " + DateUtils.getFormattedUtilDate(criteria.getEndDate());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "";
		
	}
	
	private String getSearchCriteria(EventAuditReportCriteriaBO criteria) {
		
		String searchCriteria = "", regions = "", staff = "", eventType = ""; //, sortBy = "";
		
		if(!criteria.getTAOfficeRegion().isEmpty()) {
			
			for(String region : criteria.getTAOfficeRegion())
				regions += getRegionDescription(region) + ", ";
			
			searchCriteria += "Regions: " + regions;
			searchCriteria = searchCriteria.substring(0, searchCriteria.lastIndexOf(","));
		
		} else
			searchCriteria += "Regions: ALL ";
		
		
		
		eventType = getEventType(criteria.getEventCode());
		if(!eventType.equals(""))
			searchCriteria += "; Event: " + eventType;
		else
			searchCriteria += "; Event: ALL";
		
		
		staff = getStaffMember(criteria.getCreateUserName());
		if(!staff.equals(""))
			searchCriteria += "; User: " + staff;
		else
			searchCriteria += "; User: ALL";
		
		
		
		if(criteria.getSortBy().equals("user"))
			searchCriteria += "; Sort By: Staff ";
		else if(criteria.getSortBy().equals("eventdtime"))
			searchCriteria += "; Sort By: Event Date ";
		else if(criteria.getSortBy().equals("location"))
			searchCriteria += "; Sort By: Location ";
		else if(criteria.getSortBy().equals("event"))
			searchCriteria += "; Sort By: Event";
		
		
		
		return searchCriteria;
		
	}
	
	@SuppressWarnings("unchecked")
	private String getRegionDescription(String region) {
		
		List<RegionBO> regionList = (List<RegionBO>) 
			RequestContextHolder.getRequestContext().getFlowScope().get("regionList");
		
		for(RegionBO item : regionList)
			if(item.getId().equalsIgnoreCase(region))
				return item.getDescription();
		
		return "";
		
	}
	
	@SuppressWarnings("unchecked")
	private String getEventType(Integer eventCode) {
		
		List<SelectItem> eventTypeList = (List<SelectItem>) 
			RequestContextHolder.getRequestContext().getFlowScope().get("eventTypeList");
		
		for(SelectItem code : eventTypeList)
			if(((String)code.getValue()).equals(String.valueOf(eventCode)))
				return (String) code.getLabel();
		
		return "";
		
	}
	
	@SuppressWarnings("unchecked")
	private String getStaffMember(String member) {
		
		List<SelectItem> staffList = (List<SelectItem>) 
			RequestContextHolder.getRequestContext().getFlowScope().get("staffDropDown");
		
		for(SelectItem staffMember : staffList)
			if(((String)staffMember.getValue()).equals(member))
				return (String) staffMember.getLabel();
		
		return "";
		
	}
	
}
