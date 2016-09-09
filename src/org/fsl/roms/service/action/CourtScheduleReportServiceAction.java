package org.fsl.roms.service.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.datatype.DatatypeConstants;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.businessobject.RegionBO;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.util.StringUtil;
import org.fsl.roms.view.CourtScheduleReportView;
import org.fsl.servlet.BasePdfServlet;
import org.fsl.servlet.PdfServlet;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtScheduleCriteriaBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportBO;
import fsl.ta.toms.roms.bo.CourtScheduleReportResultsBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class CourtScheduleReportServiceAction extends BaseServiceAction {
	
	public CourtScheduleReportServiceAction() {
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
	
	public Event getCourtList(RequestContext context) {
		
		//HashMap<String, String> criteria = new HashMap<String, String>();
		//criteria.put("status_id", "ACT");
		//List<RefCodeBO> listRefCodes = super.getRefInfo("roms_court", criteria);
		List<RefCodeBO> listRefCodes = super.getRefInfo("roms_court");
		List<CourtBO> courts = new ArrayList<CourtBO>();
		CourtBO court = null;
		
		if(listRefCodes != null) {
			
			for(RefCodeBO refCode : listRefCodes) {
				
				court = new CourtBO();
				court.setCourtId(Integer.parseInt(refCode.getCode()));
				court.setDescription(refCode.getShortDescription());// jreid 2014-06-26 per descrepancy
				courts.add(court);
				
			}
			
		}
		
		context.getFlowScope().put("courtList", courts);
		
		return success();
		
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public Event generate(RequestContext context) {
		Boolean pass=true;
		CourtScheduleCriteriaBO criteria = (CourtScheduleCriteriaBO) context.getFlowScope().get("criteria");
		StaffUserMappingBO staffId = (StaffUserMappingBO)context.getFlowScope().get("taFullName");
		List<RegionBO> regions = (List<RegionBO>)context.getFlowScope().get("taOfficeRegion");
			
		
		if(regions != null && regions.size() > 0){
			for(RegionBO region : regions){
				criteria.getTAOfficeRegions().add(region.getId());
			}
		}
		
		//add TAstaff Id to criteria
		if(staffId != null)
			criteria.setTAStaffId(!StringUtils.isBlank(staffId.getStaffId()) ? staffId.getStaffId() : "");
		else
			criteria.setTAStaffId("");
		
		
		pass = valid(context, criteria);
		
		//if(context.getMessageContext().hasErrorMessages()) return error();
		if(pass){
			try {
			
				CourtScheduleReportBO courtScheduleReportBO
					= getReportService().courtScheduleReport(
						criteria, getRomsLoggedInUser().getUsername(), getRomsLoggedInUser().getLocationCode());
				
				if(!courtScheduleReportBO.getResults().isEmpty())
					getReportInPdf(
						courtScheduleReportBO.getResults(),
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
	
	private Boolean valid(RequestContext context, CourtScheduleCriteriaBO criteria) {
		Boolean pass = true;
		
		if(criteria.getTrialStartDate() == null && criteria.getTrialEndDate() == null)
		{
			context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Court Appearance Date").build());
			pass = false;
		}else{
		pass = validateStartNEndDate(context, criteria.getTrialStartDate(), 
				criteria.getTrialEndDate(), "Court Appearance", true) && pass;
		}
		return pass;
		/*if(criteria.getTrialStartDate() == null)
		{
			context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Court Appearance Date").build());
			return;
		}
			
		if(criteria.getTrialEndDate() == null)
		{	context.getMessageContext().addMessage(new MessageBuilder()
				.error().code("RequiredFields").arg("Court Appearance Date").build());
			return;
		}
		
		if(criteria.getTrialStartDate() != null && criteria.getTrialEndDate() != null){
			if(criteria.getTrialEndDate().compare(criteria.getTrialStartDate()) == DatatypeConstants.LESSER)
			{
				addErrorMessageText(context, "Court Appearance End date cannot be before Court Appearance Start Date");
				return;
			}
			
			if(!BaseServiceAction.validateDateRangeLimit
					(DateUtils.toDate(criteria.getTrialStartDate()),
							DateUtils.toDate(criteria.getTrialEndDate()))){
				addErrorMessageWithParameter(context,"DateRangeLimit", "Court Appearance");
			}
		}*/
		
	}
	
	public void clear(RequestContext context) {
		
	}
	
	private void getReportInPdf(
		List<CourtScheduleReportResultsBO> results, 
		RequestContext context,
		CourtScheduleCriteriaBO criteria) {
		
		String fileName = "/reports/CourtScheduleReport.jasper";
		
		//String subReportFileName = "/reports/";
		
		try {
		  
			List<CourtScheduleReportView> reportObjects = convertBOToView(results);
			
			HttpSession session = (HttpSession) 
				((HttpServletRequest) context.getExternalContext().getNativeRequest()).getSession();
			
			Map<String, Object> reportMap = new HashMap<String, Object>();
			reportMap.put(PdfServlet.REPORT_MAP_PARAMETERS_KEY, getReportParameters(criteria, reportObjects));
			
			removeValueHolderReportObjects(reportObjects);
			
			final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportObjects);
			
			((Map)reportMap.get(PdfServlet.REPORT_MAP_PARAMETERS_KEY)).put("SUB_DATA_SOURCE", new JRBeanCollectionDataSource(reportObjects));
			reportMap.put(PdfServlet.REPORT_MAP_DATASOURCE_KEY, dataSource);
			
			
			
			reportMap.put(PdfServlet.REPORT_MAP_REPORTPATH_KEY, fileName);
			
			
			
			session.setAttribute(PdfServlet.REPORT_MAP_SESSION_ATTRIBUTE, reportMap);
			session.setAttribute(BasePdfServlet.PDF_FILE_NAME_SESSION_ATTRIBUTE, fileName + ".pdf");
		  
			context.getFlashScope().put("openReportWindow", true);
		  
		} catch (Exception exception) { 
			exception.printStackTrace(); 
		}
		 
	}
	
	private List<CourtScheduleReportView> convertBOToView(List<CourtScheduleReportResultsBO> results) {
		
		int kro = 0, sro = 0, nro = 0, wro = 0;
		
		List<CourtScheduleReportView> convertedResults = new ArrayList<CourtScheduleReportView>();
		
		for(CourtScheduleReportResultsBO result : results) {
			
			if(result.getTAOfficeRegion().equalsIgnoreCase("KRO")) kro++;
			
			if(result.getTAOfficeRegion().equalsIgnoreCase("SRO")) sro++;
			
			if(result.getTAOfficeRegion().equalsIgnoreCase("NRO")) nro++;
			
			if(result.getTAOfficeRegion().equalsIgnoreCase("WRO")) wro++;
			
			CourtScheduleReportView view = new CourtScheduleReportView();
			
			view.setCourtCaseStatus(result.getCourtCaseStatus());
			view.setCourtDate(formatDate((result.getCourtDate())));
			view.setOffenceDate(formatDate(result.getOffenceDate()));
			view.setOffenceDetails(result.getOffenceDetails());
			view.setOffenderFullName(result.getOffenderFullName());
			view.setRoadOperationName(result.getRoadOperationName());
			view.setTaOfficeRegionDescription(result.getTAOfficeRegionDescription());
			view.setTaStaffFullName(result.getTAStaffFullName());
			view.setCourtName(result.getCourtBO().getShortDescription());
			
			
			convertedResults.add(view);
			
		}
		
		CourtScheduleReportView kroView = new CourtScheduleReportView();
		kroView.setTaOfficeRegion("" + kro);
		convertedResults.add(kroView);
		
		CourtScheduleReportView sroView = new CourtScheduleReportView();
		sroView.setTaOfficeRegion("" + sro);
		convertedResults.add(sroView);
		
		CourtScheduleReportView nroView = new CourtScheduleReportView();
		nroView.setTaOfficeRegion("" + nro);
		convertedResults.add(nroView);
		
		CourtScheduleReportView wroView = new CourtScheduleReportView();
		wroView.setTaOfficeRegion("" + wro);
		convertedResults.add(wroView);
		
		return convertedResults;
		
	}
	
	private Map<String, Object> getReportParameters(CourtScheduleCriteriaBO criteria, List<CourtScheduleReportView> reportObjects) {
		
		final Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("systemName", "Road Operation Management System");
		parameters.put("printDate", DateUtils.formatDate("yyyy-MM-dd hh:mm a", Calendar.getInstance().getTime()));
		parameters.put("userName", getRomsLoggedInUser().getUsername());
		parameters.put("location", getRomsLoggedInUser().getLocation());
		parameters.put("searchDates", getSearchDatesParameter(criteria));
		parameters.put("searchCriteria", getSearchCriteria(criteria));
		parameters.put("SUBREPORT_DIR", "/reports/");
		
		//addAdditionalParameters(parameters, reportObjects);
		
		return parameters;
		
	}

//	private void addAdditionalParameters(Map<String, Object> parameters, List<CourtScheduleReportView> reportObjects) {
//		
//		int size = reportObjects.size();
//		
//		parameters.put("wroCnt", reportObjects.get(size - 1).getTaOfficeRegion());
//		parameters.put("nroCnt", reportObjects.get(size - 2).getTaOfficeRegion());
//		parameters.put("sroCnt", reportObjects.get(size - 3).getTaOfficeRegion());
//		parameters.put("kroCnt", reportObjects.get(size - 4).getTaOfficeRegion());
//		
//	}

	private void removeValueHolderReportObjects(List<CourtScheduleReportView> reportObjects) {
		
		reportObjects.remove(reportObjects.size() - 1);
		reportObjects.remove(reportObjects.size() - 1);
		reportObjects.remove(reportObjects.size() - 1);
		reportObjects.remove(reportObjects.size() - 1);
		
	}
	
	private String getSearchCriteria(CourtScheduleCriteriaBO criteria) {
		
		String searchCriteria = "", regions = "", court = "";
		
		if(!criteria.getTAOfficeRegions().isEmpty()) {
			
			for(String region : criteria.getTAOfficeRegions())
				regions += getRegionDescription(region) + ", ";
			
			searchCriteria += "Regions: " + regions;
			searchCriteria = searchCriteria.substring(0, searchCriteria.lastIndexOf(","));
		
		} else
			searchCriteria += "Regions: ALL ";
		
		court = getCourtDescription(criteria.getCourtId());
		if(!court.equals("")) 
			searchCriteria += "; Court: " + court;
		else
			searchCriteria += "; Court: ALL ";
		
		if(StringUtil.isSet(criteria.getOffenderTRN())) 
			searchCriteria += "; Offender TRN: " + criteria.getOffenderTRN();
		
		if(StringUtil.isSet(criteria.getOffenderFirstName())) 
			searchCriteria += "; Offender First Name: " + criteria.getOffenderFirstName();
		
		if(StringUtil.isSet(criteria.getOffenderLastName())) 
			searchCriteria += "; Offender Last Name: " + criteria.getOffenderLastName();
		
		if(StringUtil.isSet(criteria.getTAStaffId())) 
			searchCriteria += "; TA Staff: " + getTAStaffName(criteria.getTAStaffId());
		
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
	private String getCourtDescription(Integer court) {
		
		List<CourtBO> courtList = (List<CourtBO>) 
			RequestContextHolder.getRequestContext().getFlowScope().get("courtList");
		
		for(CourtBO crt : courtList)
			if(crt.getCourtId().equals(court))
				return (String) crt.getDescription();
		
		return "";
		
	}
	
	@SuppressWarnings("unchecked")
	private String getTAStaffName(String taStaffId) {
		
		List<TAStaffBO> staffList = (List<TAStaffBO>) 
			RequestContextHolder.getRequestContext().getFlowScope().get("staffList");
		
		for(TAStaffBO staff : staffList)
			if(staff.getStaffId().equals(taStaffId))
				return staff.getLastName()+", "+staff.getFirstName();
		
		return "";
		
	}
	
	private String getSearchDatesParameter(CourtScheduleCriteriaBO criteria) {
		
		try {
			
			return 
				"For the period " + DateUtils.getFormattedUtilDate(criteria.getTrialStartDate()) + 
				" to " + DateUtils.getFormattedUtilDate(criteria.getTrialEndDate());
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "";
		
	}
	
}
