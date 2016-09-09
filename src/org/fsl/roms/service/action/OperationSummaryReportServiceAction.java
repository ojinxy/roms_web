package org.fsl.roms.service.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.fsl.roms.businessobject.RegionBO;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.OperationSummaryReportView;
import org.fsl.servlet.BasePdfServlet;
import org.fsl.servlet.PdfServlet;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryResultsBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.OperationSummaryReportCriteriaBO;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class OperationSummaryReportServiceAction extends BaseServiceAction {
	
	public OperationSummaryReportServiceAction() {
		super();
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
		Boolean pass=true;
		OperationSummaryReportCriteriaBO criteria = (OperationSummaryReportCriteriaBO) context.getFlowScope().get("criteria");
		
		List<RegionBO> regions = (List<RegionBO>)context.getFlowScope().get("taOfficeRegion");
			
		
		if(regions != null && regions.size() > 0){
			for(RegionBO region : regions){
				criteria.getTAOfficeRegions().add(region.getId());
			}
		}		
		pass = valid(context, criteria);
		
		//if(context.getMessageContext().hasErrorMessages()) return error();
		if(pass){
			try {
			
				RoadOperationSummaryBO roadOperationSummaryBO
					= getReportService().operationSummaryReport(
						criteria, 
						getRomsLoggedInUser().getUsername(), 
						getRomsLoggedInUser().getLocationCode());
				
				if(!roadOperationSummaryBO.getResults().isEmpty())
					getReportInPdf(
						roadOperationSummaryBO.getResults(),
						context,
						criteria);
				
			} catch (NoRecordFoundException e) {
				
				context.getMessageContext().addMessage(new MessageBuilder().error().code("Norecordsfound").build());
				
			} catch (Exception e) {
				
				e.printStackTrace();
				context.getMessageContext().addMessage(new MessageBuilder().error().code("SystemError").build());
				criteria.getTAOfficeRegions().clear();
				return error();
				
			}
			
			criteria.getTAOfficeRegions().clear();
		}
		
		if(pass)
			return success();
		else
			return error();
		
	}
	
	private Boolean valid(RequestContext context, OperationSummaryReportCriteriaBO criteria) {
		Boolean pass = true;
		
		if(criteria.getOperationStartDate() == null && criteria.getOperationEndDate() == null)
		{
			context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Operation Date").build());
			pass = false;
		}else{
			pass = validateStartNEndDate(context, criteria.getOperationStartDate(), 
					criteria.getOperationEndDate(), "Operation", false) && pass;
		}
		return pass;
		
		/*if(criteria.getOperationStartDate() == null)
		{
			context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Operation Date").build());
			return;
		}
		
		if(criteria.getOperationEndDate() == null)
		{	context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Operation Date").build());
		
			return;
		}
		
		if(criteria.getOperationStartDate() != null && criteria.getOperationEndDate() != null){
			if(criteria.getOperationEndDate().compare(criteria.getOperationStartDate()) == DatatypeConstants.LESSER)
			{	addErrorMessageText(context, "Operation End date cannot be before Operation Start Date");
				return;
			}
	
			if(!BaseServiceAction.validateDateRangeLimit
					(DateUtils.toDate(criteria.getOperationStartDate()),
							DateUtils.toDate(criteria.getOperationEndDate()))){
				addErrorMessageWithParameter(context,"DateRangeLimit", "Operation");
			}
			
		}
		
		if(DateUtils.isDateInFuture(criteria.getOperationStartDate()))
		{	
			addErrorMessageText(context, "Start Date Cannot Be a Future Date");
			return;
		}
		
		if(DateUtils.isDateInFuture(criteria.getOperationEndDate()))
		{	
			addErrorMessageText(context, "End Date Cannot Be a Future Date");
			return;
		}*/
		
	}
	
	public void clear(RequestContext context) {
		
	}
	
	private void getReportInPdf(
		List<RoadOperationSummaryResultsBO> results, 
		RequestContext context,
		OperationSummaryReportCriteriaBO criteria) {
		
		String fileName = "/reports/OperationSummaryReport.jasper";
		
		try {
		  
			final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(convertBOToView(results));
			
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
	
	private List<OperationSummaryReportView> convertBOToView(List<RoadOperationSummaryResultsBO> results) {
		
		List<OperationSummaryReportView> convertedResults = new ArrayList<OperationSummaryReportView>();
		
		for(RoadOperationSummaryResultsBO result : results) {
			
			OperationSummaryReportView view = new OperationSummaryReportView();
			
			view.setActualEndDateTime(formatDate((result.getActualEndDateTime())));
			view.setActualStartDateTime(formatDate(result.getActualStartDateTime()));
			view.setAllInOrders(result.getAllInOrders());
			view.setCountAbsentMembers(result.getCountAbsentMembers());
			view.setCountBadgesChecked(result.getCountBadgesChecked());
			view.setCountCitationChecks(result.getCountCitationChecks());
			view.setCountDrivesLicenceChecked(result.getCountDrivesLicenceChecked());
			view.setCountMotorVehiclesChecked(result.getCountMotorVehiclesChecked());
			view.setCountOtherChecks(result.getCountOtherChecks());
			view.setCountSummonsIssued(result.getCountSummonsIssued());
			view.setCountTeamMembers(result.getCountTeamMembers());
			view.setCountVehiclesSeized(result.getCountVehiclesSeized());
			view.setOperationName(result.getOperationName());
			view.setCountWaningNoticesIssued(result.getCountWaningNoticesIssued());
			view.setWarningsForProcecution(result.getWarningsForProcecution());
			view.setCountJPs(result.getCountJPs());
			view.setCountITAExaminers(result.getCountITAExaminers());
			view.setCountTAInspectors(result.getCountTAInspectors());
			view.setCountPoliceOfficers(result.getCountPoliceOfficers());
			view.setCountWarningNoProsecutions(result.getCountWarningNoProsecutions());
			view.setOperationStatus(result.getOperationStatus());
			view.setOperationCategory(result.getOperationCategory());
			view.setTAOfficeRegion(result.getTAOfficeRegion());
			view.setTAOfficeRegionDescription(result.getTAOfficeRegionDescription());
			
			//UR-057
			view.setCountAbsentITA(result.getCountAbsentITAExaminers());
			view.setCountAbsentJP(result.getCountAbsentJPs());
			view.setCountAbsentPolice(result.getCountAbsentPoliceOfficers());
			view.setCountAbsentTAStaff(result.getCountAbsentTAInspectors());
				
			
			
			
			convertedResults.add(view);
			
		}
		
		return convertedResults;
		
	}
	
	private Map<String, Object> getReportParameters(OperationSummaryReportCriteriaBO criteria) {
		
		final Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("systemName", "Road Operation Management System");
		parameters.put("printDate", DateUtils.formatDate("yyyy-MM-dd hh:mm a", Calendar.getInstance().getTime()));
		parameters.put("userName", getRomsLoggedInUser().getUsername());
		parameters.put("location", getRomsLoggedInUser().getLocation());
		parameters.put("searchDates", getSearchDatesParameter(criteria));
		parameters.put("searchCriteria", getSearchCriteria(criteria));
		
		return parameters;
		
	}

	private String getSearchCriteria(OperationSummaryReportCriteriaBO criteria) {
		
		String searchCriteria = "", regions = "", category = "";
		
		if(!criteria.getTAOfficeRegions().isEmpty()) {
			
			for(String region : criteria.getTAOfficeRegions())
				regions += getRegionDescription(region) + ", ";
			
			searchCriteria += "Regions: " + regions;
			searchCriteria = searchCriteria.substring(0, searchCriteria.lastIndexOf(","));
		
		} else
			searchCriteria += "Regions: ALL ";
		
		category = getCategoryDescription(criteria.getOperationCategory());
		if(!category.equals("")) 
			searchCriteria += "; Operation Category: " + category;
		else
			searchCriteria += "; Operation Category: ALL ";
		
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
	private String getCategoryDescription(String category) {
		
		List<SelectItem> categoryList = (List<SelectItem>) 
			RequestContextHolder.getRequestContext().getFlowScope().get("categoryList");
		
		for(SelectItem item : categoryList)
			if(((String)item.getValue()).equalsIgnoreCase(category))
				return (String) item.getLabel();
		
		return "";
		
	}
	
	private String getSearchDatesParameter(OperationSummaryReportCriteriaBO criteria) {
		
		try {
			
			return 
				"For the period " + DateUtils.getFormattedUtilDate(criteria.getOperationStartDate()) + 
				" to " + DateUtils.getFormattedUtilDate((criteria.getOperationEndDate()));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "";
		
	}
	
}
