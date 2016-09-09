/**
 * 
 */
package org.fsl.roms.service.action;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.fsl.datatable.DataTableMemory;
import org.fsl.roms.businessobject.RegionBO;
import org.fsl.roms.businessobject.RoadOperationDetailsSumary;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.constants.Constants.Category;
import org.fsl.roms.uicomponents.ArteryDataModel;
import org.fsl.roms.uicomponents.LocationDataModel;
import org.fsl.roms.uicomponents.ParishDataModel;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.util.StringUtil;
import org.fsl.roms.view.OperationStrategyRuleView;
import org.fsl.roms.view.RoadOpUnattendedResourceView;
import org.fsl.roms.view.RoadOperationView;
import org.fsl.roms.view.TeamView;
import org.fsl.ta.toms.roms.beans.ApplicationRunTimeStorage;
import org.primefaces.component.tabview.Tab;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.bo.AssignedTeamDetailsBO;
import fsl.ta.toms.roms.bo.AuthorizationBO;
import fsl.ta.toms.roms.bo.ComplianceBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.bo.JPBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.bo.PoliceOfficerBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.RoadOperationBO;
import fsl.ta.toms.roms.bo.RoadOperationOtherDetailsBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryBO;
import fsl.ta.toms.roms.bo.RoadOperationSummaryResultsBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.TAStaffBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.TeamBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.exception.UserMappingException;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.AvailableResourceCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.OperationSummaryReportCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadCompliancyCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RoadOperationCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;
import fsl.ta.toms.roms.ui.ScheduleMinMaxUi;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.webservices.Report;
import fsl.ta.toms.roms.webservices.RoadOperation;


/**
 * @author oanguin
 * @author rbrooks
 */

@Service
public class RoadOperationsServiceAction extends BaseServiceAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ParishDataModel parishDataMod;
	private LocationDataModel locationDataMod;
	private ArteryDataModel arteryDataMod;
	private Date startDate, endDate;
	private AvailableResourceCriteriaBO availResourceCriteria;
	private TreeNode locationTreeView;

	private List<LocationBO> listLocations;
	private List<ParishBO> listParish;
	private List<ArteryBO> listArtery;

	// private List<TAStaffBO> selectedTAStaff;
	// private TAStaffBO selectedTeamLead;
	private List<TAStaffBO> availableStaff;

	private final static String FROM_SEARCH = "search";
	
	public final static String ROAD_OPERATION_STARTED = Constants.Status.ROAD_OPERATION_STARTED;
	
	public final static String ROAD_OPERATION_COMPLETED = Constants.Status.ROAD_OPERATION_COMPLETED;
	
	@Autowired
	ApplicationRunTimeStorage applicationRunTimeStorage;
	
	private class SaveCachedData implements Runnable
	{
		private ExternalContext context;
		private Integer operationId;

		
		public SaveCachedData(Integer operationId)
		{
			super();
			this.context = context;
			this.operationId = operationId;
		}


		@Override
		public void run()
		{
			try
			{
				applicationRunTimeStorage.getValues().put(Constants.ROAD_OPERATION_LIST,getRoadOperationService().lookupAllRoadOperationForRoadCheck());
				applicationRunTimeStorage.getValues().put(Constants.ALL_ARTERY_LIST + operationId ,getRoadOperationService().lookupAllOperationArtery(operationId));
			} 
			catch (NoRecordFoundException e)
			{
				logger.error("Updating Road Operation List Cache in Runnable", e);
			} 
			catch (RequiredFieldMissingException e)
			{
				logger.error("Updating Road Operation Artery List Cache in Runnable", e);
			}
			
		}
		
	}

	public RoadOperationsServiceAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Event setCompleteValuesFormSearch(RequestContext context) {
		String opStatus = (String) context.getFlowScope().get("opStatus");
		String fromWhere = (String) context.getFlowScope().get("fromWhere");
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		System.err.println("inside setCompleteValuesFormSearch()");
		System.err.println("opStatus"+ opStatus);
		System.err.println("fromWhere"+ fromWhere);
		if ((opStatus.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING)|| 
				opStatus.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION))
				&& fromWhere.equalsIgnoreCase(FROM_SEARCH)) {

			context.getFlowScope().put("completeOpDetails", "true");
			context.getFlowScope().put("completeOpStrategy", "true");
			context.getFlowScope().put("completeOpResource", "true");
			if (roadOperationView.getCategoryId().equalsIgnoreCase(
					Constants.Category.REGIONAL)) {
				context.getFlowScope().put("completeOpCourtDetails", "true");
			}
			context.getFlowScope().put("completeOpReview", "true");
		}

		System.err.println("exit setCompleteValuesFormSearch()");
		return success();
	}

	public Event getResults(RequestContext context) {
		Boolean pass = true;
		RoadOperation roadOpService = new RoadOperation();


		// ReportPortProxy reportProxy = new ReportPortProxy();

		// reportProxy._getDescriptor().setEndpoint(ApplicationProperties.get("application.webservices.url").trim()
		// + "ReportService");

		RoadOperationCriteriaBO roadOpCriteria = (RoadOperationCriteriaBO) context
				.getFlowScope().get("criteria");
		
		StaffUserMappingBO teamLeadAdvSearchUserMapping = (StaffUserMappingBO) context
				.getFlowScope().get("teamLeadAdvSearch");

		if (teamLeadAdvSearchUserMapping != null)
			roadOpCriteria.setTeamLeadStaffId(teamLeadAdvSearchUserMapping
					.getStaffId());

		roadOpCriteria.setScheduledDTime(!roadOpCriteria.isScheduledDTime());

		List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();

		DataTableMemory dataTable = null;

		// OperationSummaryReportCriteriaBO reportCriteria = new
		// OperationSummaryReportCriteriaBO();

		List<RoadOperationDetailsSumary> roadOpDetailsSummaryList = new ArrayList<RoadOperationDetailsSumary>();

		MessageContext messages = context.getMessageContext();

		try {

			if (!this.isFilterAdded(roadOpCriteria, context)) {
				//this.addErrorMessage(context, "AtleastOneSearchCriteriaReq");
				
				return error();
			}
			if(roadOpCriteria.isScheduledDTime()){
				pass = validateStartNEndDate(context, roadOpCriteria.getOperationStartDate(), 
						roadOpCriteria.getOperationEndDate(), "Operation", true) && pass;
			}else{
				pass = validateStartNEndDate(context, roadOpCriteria.getOperationStartDate(), 
						roadOpCriteria.getOperationEndDate(), "Actual Operation", false) && pass;
			}
			/*if (roadOpCriteria.getOperationStartDate() != null
					&& roadOpCriteria.getOperationEndDate() != null
					&& roadOpCriteria.getOperationEndDate().compare(
							roadOpCriteria.getOperationStartDate()) == DatatypeConstants.LESSER) {
				this.addErrorMessageWithParameter(context,
						"EndDateBeforeStartDate", "Operation Scheduled");

				return error();
			}
			
			if(roadOpCriteria.getOperationStartDate() != null
					&& roadOpCriteria.getOperationEndDate() != null){
				if(!BaseServiceAction.validateDateRangeLimit
						(DateUtils.toDate(roadOpCriteria.getOperationStartDate()),
								DateUtils.toDate(roadOpCriteria.getOperationEndDate()))){
					this.addErrorMessageWithParameter(context,
							"DateRangeLimit", "Operation");

					return error();
				}
			}*/
			if(pass){
				roadOps = roadOpService.lookupRoadOperationWithDateRange(roadOpCriteria);
			

			// List<Integer> roadOpIds = new ArrayList<Integer>();
			//
			// for(RoadOperationBO roadOp : roadOps)
			// {
			// roadOpIds.add(roadOp.getRoadOperationId());
			// }
			//
			// reportCriteria.getRoadOperationIds().addAll(roadOpIds);
			//
			// RoadOperationSummaryBO roadOpSummary =
			// reportProxy.operationSummaryReport(reportCriteria,
			// this.getRomsLoggedInUser().getUsername(),
			// this.getRomsLoggedInUser().getLocationCode());

			// for(RoadOperationBO roadOp : roadOps)
			// {
			// Integer roadOpId = roadOp.getRoadOperationId();
			//
			// boolean found = false;
			//
			// Iterator<RoadOperationSummaryResultsBO> iterator =
			// roadOpSummary.getResults().iterator();
			//
			// while(!found && iterator.hasNext())
			// {
			// RoadOperationSummaryResultsBO roadOpSummaryResult =
			// iterator.next();
			//
			// if(roadOpSummaryResult.getOperationId().compareTo(roadOpId) == 0)
			// {
			// roadOpDetailsSummaryList.add(new
			// RoadOperationDetailsSumary(roadOp, roadOpSummaryResult ));
			//
			// found = true;
			// }
			//
			// }
			//
			//
			//
			// }

				validateUserStaffMapping(new RoadOperationView(), context);
				
				for (RoadOperationBO roadOp : roadOps) {
						
					
					roadOpDetailsSummaryList.add(new RoadOperationDetailsSumary(
							roadOp));
	
				}
	
				dataTable = new DataTableMemory(roadOpDetailsSummaryList,
						((DataTableMemory) context.getFlowScope().put("dataTable",
								dataTable)).rowsPerPage,
						roadOpDetailsSummaryList.size(), "Road Operations");
	
				dataTable.setSortBy("Date Created");
	
				dataTable.setSortOrder("desc");
	
				context.getFlowScope().put("dataTable", dataTable);
	
				getRoadOpReportDetails(context);
			}

		} catch (NoRecordFoundException exe) {
			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("Norecordsfound").build());

			return error(exe);
		} catch (Exception e) {

			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("SystemError").build());

			e.printStackTrace();
			return error();
		} finally {
			/* Reset scheduled d time in criteria to how it is on the screen */
			roadOpCriteria
					.setScheduledDTime(!roadOpCriteria.isScheduledDTime());
		}

		return success();
	}

	public void getRoadOpReportDetails(RequestContext context)
			throws NoRecordFoundException,RequiredFieldMissingException {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		if (dataTable == null || dataTable.getPageElements() == null)
			return;

		List<Integer> roadOpIds = new ArrayList<Integer>();

		Iterator iterator = dataTable.getPageElements().iterator();
		while (iterator.hasNext()) {
			RoadOperationDetailsSumary detailsSummary = (RoadOperationDetailsSumary) iterator
					.next();
			if (detailsSummary.getRoadOpSummary() == null)
				roadOpIds.add(detailsSummary.getRoadOp().getRoadOperationId());
		}

		if (roadOpIds.size() > 0) {
			OperationSummaryReportCriteriaBO reportCriteria = new OperationSummaryReportCriteriaBO();

			reportCriteria.getRoadOperationIds().addAll(roadOpIds);

			Report reportService = this.getReportService();

			RoadOperationSummaryBO roadOpSummary = reportService
					.operationSummaryReport(reportCriteria, this
							.getRomsLoggedInUser().getUsername(), this
							.getRomsLoggedInUser().getLocationCode());

			iterator = dataTable.getPageElements().iterator();
			while (iterator.hasNext()) {
				RoadOperationDetailsSumary detailsSummary = (RoadOperationDetailsSumary) iterator
						.next();
				if (detailsSummary.getRoadOpSummary() != null)
					continue;

				Integer roadOpId = detailsSummary.getRoadOp()
						.getRoadOperationId();

				boolean found = false;

				Iterator<RoadOperationSummaryResultsBO> iteratorSummaryResults = roadOpSummary
						.getResults().iterator();

				while (!found && iteratorSummaryResults.hasNext()) {
					RoadOperationSummaryResultsBO roadOpSummaryResult = iteratorSummaryResults
							.next();

					if (roadOpSummaryResult.getOperationId()
							.compareTo(roadOpId) == 0) {
						detailsSummary.setRoadOpSummary(roadOpSummaryResult);

						found = true;
					}

				}
			}

		}
	}

	public Event getResults(RequestContext context, Integer roadOperationId) {
		RoadOperationCriteriaBO roadOpCriteria = new RoadOperationCriteriaBO();

		if(roadOperationId == null || roadOperationId == 0)
		{
			return null;
		}
		
		roadOpCriteria.setRoadOperationId(roadOperationId);

		RoadOperation roadOpService = new RoadOperation();


		Report reportService = new Report();


		StaffUserMappingBO teamLeadAdvSearchUserMapping = (StaffUserMappingBO) context
				.getFlowScope().get("teamLeadAdvSearch");

		if (teamLeadAdvSearchUserMapping != null)
			roadOpCriteria.setTeamLeadStaffId(teamLeadAdvSearchUserMapping
					.getStaffId());

		roadOpCriteria.setScheduledDTime(!roadOpCriteria.isScheduledDTime());

		List<RoadOperationBO> roadOps = new ArrayList<RoadOperationBO>();

		DataTableMemory dataTable = null;

		OperationSummaryReportCriteriaBO reportCriteria = new OperationSummaryReportCriteriaBO();

		reportCriteria.setRoadOperationId(roadOperationId);

		List<RoadOperationDetailsSumary> roadOpDetailsSummaryList = new ArrayList<RoadOperationDetailsSumary>();

		MessageContext messages = context.getMessageContext();

		try {

			/*if (!this.isFilterAdded(roadOpCriteria, context)) {
				//this.addErrorMessage(context, "AtleastOneSearchCriteriaReq");
				this.addErrorMessageText(context, "Please enter at least one of the following Operation Name or Operation Date range.");
				return error();
			}*/

			roadOps = roadOpService
					.lookupRoadOperationWithDateRange(roadOpCriteria);
			

			RoadOperationSummaryBO roadOpSummary = reportService
					.operationSummaryReport(reportCriteria, "system", "KRO");

			for (RoadOperationBO roadOp : roadOps) {
				Integer roadOpId = roadOp.getRoadOperationId();

				boolean found = false;

				Iterator<RoadOperationSummaryResultsBO> iterator = roadOpSummary
						.getResults().iterator();

				while (!found && iterator.hasNext()) {
					RoadOperationSummaryResultsBO roadOpSummaryResult = iterator
							.next();

					if (roadOpSummaryResult.getOperationId()
							.compareTo(roadOpId) == 0) {
						roadOpDetailsSummaryList
								.add(new RoadOperationDetailsSumary(roadOp,
										roadOpSummaryResult));
						System.out.println("roadop>>>>"
								+ roadOp.getOperationName());
						System.out.println("roadOp>>>details:"
								+ roadOp.getOperationStrategyList().size());

						found = true;
					}

				}

			}

			dataTable = new DataTableMemory(roadOpDetailsSummaryList, 10,
					roadOpDetailsSummaryList.size(), "Road Operations");

			dataTable.getPageElements().select(
					((List<RoadOperationDetailsSumary>) dataTable
							.getPageElements().getWrappedData()).get(0));

			context.getFlowScope().put("dataTable", dataTable);

		} catch (NoRecordFoundException exe) {
			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("Norecordsfound").build());

			return error(exe);
		} catch (Exception e) {

			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("SystemError").build());

			e.printStackTrace();
			return error();
		} finally {
			/* Reset scheduled d time in criteria to how it is on the screen */
			roadOpCriteria
					.setScheduledDTime(!roadOpCriteria.isScheduledDTime());
		}

		return success();
	}

	public boolean isFilterAdded(RoadOperationCriteriaBO roadOpCriteria,
			RequestContext context) {
		/*if ((roadOpCriteria.getOperationEndDate() != null && roadOpCriteria
				.getOperationStartDate() == null)
				|| (roadOpCriteria.getOperationEndDate() == null && roadOpCriteria
						.getOperationStartDate() != null)) {
			this.addErrorMessageText(
					context,
					"Please ensure that both operation start and end dates are entered if you are searching by operation date.");

			return false;
		}else */
		if(StringUtils.isEmpty(roadOpCriteria.getOperationName())){
			if(roadOpCriteria.getOperationEndDate()==null){
				if(roadOpCriteria.getOperationStartDate()==null){
					this.addErrorMessageText(context, "Please enter at least one of the following: Operation Name or Operation Date range.");
					return false;
				}
			}
		}
		
		
		/*if (StringUtils.isEmpty(roadOpCriteria.getOperationName()))
			if (StringUtils.isEmpty(roadOpCriteria.getCategoryId()))
				if (StringUtils.isEmpty(roadOpCriteria.getStatusId()))
					if (StringUtils.isEmpty(roadOpCriteria.getOfficeLocCode()))
						if (roadOpCriteria.getOperationEndDate() == null
								&& roadOpCriteria.getOperationStartDate() == null)
							if (roadOpCriteria.getRoadOperationId() == null
									|| roadOpCriteria.getRoadOperationId()
											.intValue() < 0)
								if (StringUtils.isEmpty(roadOpCriteria
										.getParishCode()))
									if (roadOpCriteria.getLocationId() == null
											|| roadOpCriteria.getLocationId()
													.intValue() < 0)
										if (StringUtils.isEmpty(roadOpCriteria
												.getTeamLeadStaffId()))
											if (roadOpCriteria.getStrategyId() == null
													|| roadOpCriteria
															.getStrategyId()
															.intValue() < 0)
												return false;*/

		return true;

	}

	public Date convertFromDisplayStringToDate(String dateString)
			throws ParseException {
		if (StringUtils.isNotEmpty(dateString)) {

			Date returnDate = new SimpleDateFormat("yyyy-MM-dd")
					.parse(dateString.trim());

			return returnDate;
		} else {
			return null;
		}

	}

	public Event next(RequestContext context)
			throws NoRecordFoundException,RequiredFieldMissingException {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		dataTable.nextPage();

		getRoadOpReportDetails(context);

		return success();
	}

	public Event prev(RequestContext context)
			throws NoRecordFoundException,RequiredFieldMissingException {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		dataTable.previousPage();

		getRoadOpReportDetails(context);

		return success();
	}

	public Event first(RequestContext context)
			throws NoRecordFoundException,RequiredFieldMissingException {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		dataTable.firstPage();

		getRoadOpReportDetails(context);

		return success();
	}

	public Event last(RequestContext context)
			throws NoRecordFoundException,RequiredFieldMissingException {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		dataTable.lastPage();

		getRoadOpReportDetails(context);

		return success();
	}

	public void changePageSize(ValueChangeEvent e)
			throws NoRecordFoundException,RequiredFieldMissingException {
		RequestContext requestContext = RequestContextHolder
				.getRequestContext();
		RequestControlContext context = (RequestControlContext) requestContext;

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		dataTable.changePageSize((Integer) e.getNewValue());

		getRoadOpReportDetails(context);

	}

	@SuppressWarnings("unchecked")
	public void sortBy(ValueChangeEvent e)
			throws NoRecordFoundException,RequiredFieldMissingException {

		if (e != null && e.getNewValue() == null)
			return;

		RequestContext requestContext = RequestContextHolder
				.getRequestContext();
		RequestControlContext context = (RequestControlContext) requestContext;

		final DataTableMemory dataTable = (DataTableMemory) context
				.getFlowScope().get("dataTable");

		List<RoadOperationDetailsSumary> roadOpDetailsSummaryListSort = dataTable
				.getDataList();

		if (roadOpDetailsSummaryListSort == null)
			return;

		if (e != null ? ((String) e.getNewValue()).trim().equalsIgnoreCase(
				"Operation Name") : dataTable.getSortBy().trim()
				.equalsIgnoreCase("Operation Name")) {
			Collections.sort(roadOpDetailsSummaryListSort,
					new Comparator<RoadOperationDetailsSumary>() {
						public int compare(final RoadOperationDetailsSumary a,
								final RoadOperationDetailsSumary d) {
							int pos = a
									.getRoadOp()
									.getOperationName()
									.trim()
									.compareToIgnoreCase(
											d.getRoadOp().getOperationName()
													.trim());

							if (dataTable.getSortOrder().equalsIgnoreCase(
									"desc")) {
								if (pos != 0)
									pos = pos * -1;
							}

							return pos;
						}
					});

			dataTable.refreshPageElements();
		} else if (e != null ? ((String) e.getNewValue()).trim()
				.equalsIgnoreCase("Category") : dataTable.getSortBy().trim()
				.equalsIgnoreCase("Category")) {
			Collections.sort(roadOpDetailsSummaryListSort,
					new Comparator<RoadOperationDetailsSumary>() {
						public int compare(final RoadOperationDetailsSumary a,
								final RoadOperationDetailsSumary d) {

							int pos = (a.getRoadOp().getCategoryDescription()
									.trim().compareToIgnoreCase(d.getRoadOp()
									.getCategoryDescription().trim()));

							if (dataTable.getSortOrder().equalsIgnoreCase(
									"desc")) {
								if (pos != 0)
									pos = pos * -1;
							}

							return pos;

						}
					});

			dataTable.refreshPageElements();
		} else if (e != null ? ((String) e.getNewValue()).trim()
				.equalsIgnoreCase("Status") : dataTable.getSortBy().trim()
				.equalsIgnoreCase("Status")) {
			Collections.sort(roadOpDetailsSummaryListSort,
					new Comparator<RoadOperationDetailsSumary>() {
						public int compare(final RoadOperationDetailsSumary a,
								final RoadOperationDetailsSumary d) {

							int pos = (a.getRoadOp().getStatusId().trim()
									.compareToIgnoreCase(d.getRoadOp()
											.getStatusId().trim()));

							if (dataTable.getSortOrder().equalsIgnoreCase(
									"desc")) {
								if (pos != 0)
									pos = pos * -1;
							}

							return pos;

						}
					});

			dataTable.refreshPageElements();
		} else if (e != null ? ((String) e.getNewValue()).trim()
				.equalsIgnoreCase("Operation Start Date") : dataTable
				.getSortBy().trim().equalsIgnoreCase("Operation Start Date")) {
			Collections.sort(roadOpDetailsSummaryListSort,
					new Comparator<RoadOperationDetailsSumary>() {
						public int compare(final RoadOperationDetailsSumary a,
								final RoadOperationDetailsSumary d) {

							int pos = a
									.getRoadOp()
									.getScheduledStartDate()
									.compareTo(
											d.getRoadOp()
													.getScheduledStartDate());

							if (dataTable.getSortOrder().equalsIgnoreCase(
									"desc")) {
								if (pos != 0)
									pos = pos * -1;
							}

							return pos;

						}
					});

			dataTable.refreshPageElements();
		} else if (e != null ? ((String) e.getNewValue()).trim()
				.equalsIgnoreCase("Date Created") : dataTable.getSortBy()
				.trim().equalsIgnoreCase("Date Created")) {
			Collections.sort(roadOpDetailsSummaryListSort,
					new Comparator<RoadOperationDetailsSumary>() {
						public int compare(final RoadOperationDetailsSumary a,
								final RoadOperationDetailsSumary d) {

							int pos = a.getRoadOp().getDateCreated()
									.compareTo(d.getRoadOp().getDateCreated());

							if (dataTable.getSortOrder().equalsIgnoreCase(
									"desc")) {
								if (pos != 0)
									pos = pos * -1;
							}

							return pos;

						}
					});

			dataTable.refreshPageElements();
		}

		getRoadOpReportDetails(context);

	}

	public Event sortOrderASC(RequestContext context)
			throws NoRecordFoundException,RequiredFieldMissingException {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		dataTable.setSortOrder("asc");

		this.sortBy(null);

		return success();
	}

	public Event sortOrderDESC(RequestContext context)
			throws NoRecordFoundException,RequiredFieldMissingException {

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		dataTable.setSortOrder("desc");

		this.sortBy(null);

		return success();
	}

	public Event getAvailableStrategiesList(RequestContext context) {
//System.err.println("inside getAvailableStrategiesList()");
		List<StrategyBO> selectedStrategies = new ArrayList<StrategyBO>();
		List<StrategyBO> availableStrategies = new ArrayList<StrategyBO>();

		Maintenance maintenanceService = new Maintenance();
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		String fromWhere = (String) context.getFlowScope().get("fromWhere");


		StrategyCriteriaBO stratCriteria = new StrategyCriteriaBO();

		stratCriteria.setStatusId(Constants.Status.ACTIVE);

		try {
			availableStrategies = maintenanceService
					.lookupStrategy(stratCriteria);

			Collections.sort(availableStrategies, new Comparator<StrategyBO>() {
				public int compare(StrategyBO result1, StrategyBO result2) {
					return result1.getStrategyDescription().toLowerCase().compareTo(result2.getStrategyDescription().toLowerCase());
				}
			});
		//	System.err.println("Object Strategy"+ ObjectUtils.objectToString(availableStrategies));
			roadOperationView.setListOfStrategyAux(availableStrategies);

		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DualListModel<StrategyBO> listOfStrategies = new DualListModel<StrategyBO>();
		List<StrategyBO> remSrcStrategies = new ArrayList<StrategyBO>();
		if (fromWhere != null) {
			System.out.println("fromWhere>>" + fromWhere);
			if (fromWhere.equalsIgnoreCase(FROM_SEARCH)) {

				DataTableMemory dataTable = (DataTableMemory) context
						.getFlowScope().get("theTable");

				if (dataTable != null) {
					RoadOperationDetailsSumary summary = (RoadOperationDetailsSumary) dataTable.pageElements
							.getSelectedRow();

					RoadOperationBO roadOpBO = summary.getRoadOp();

					List<StrategyBO> existingStrategies = new ArrayList<StrategyBO>();

					existingStrategies = roadOperationView
							.getSelectedStrategies();// roadOpBO.getOperationStrategyList();

					StrategyBO maintStrategy = new StrategyBO();

					for (StrategyBO strategyBO : existingStrategies) {

						selectedStrategies.add(strategyBO);

						System.err.println("Existing strategies "
								+ strategyBO.getStatusDescription());
					}

					// remove selected strategies
					if (selectedStrategies != null) {

						boolean addStrat;
						for (StrategyBO availstrat : availableStrategies) {
							addStrat = true;
							for (StrategyBO strategy : selectedStrategies) {

								if (availstrat.getStrategyId().equals(
										strategy.getStrategyId())) {
									System.err
											.println("Inside if statement availstrat.getStrategyId().equals(strategy.getStrategyId())");
									addStrat = false;
									break;

								}
							}

							if (addStrat == true) {
								remSrcStrategies.add(availstrat);
								System.err.println("Add remaindersource strat"
										+ availstrat.getStrategyDescription()
										+ availstrat.getStrategyId());
							}
						}

					}
				}
				listOfStrategies = new DualListModel<StrategyBO>(
						remSrcStrategies, selectedStrategies);
			}
		} else {
			listOfStrategies = new DualListModel<StrategyBO>(
					availableStrategies, selectedStrategies);
		}

		System.err.println("Source is being set here"
				+ listOfStrategies.getSource().size());

		// context.getFlowScope().put("availableStrategies", listOfStrategies);
		roadOperationView.setListOfStrategies(listOfStrategies);
		context.getFlowScope().put("operation", roadOperationView);
		return success();
	}

	public Event getParishList(RequestContext context) {
		// retrieve parishes

		Maintenance maintenanceService = super.getMaintenanceService();

		listParish = new ArrayList<ParishBO>();
		ParishBO parishBO = new ParishBO();

		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		ParishCriteriaBO parishCriteria = new ParishCriteriaBO();
		parishCriteria.setStatusId(Constants.Status.ACTIVE);
		parishCriteria.getOfficeLocationCode().addAll(
				roadOperationView.getSelectedRegions());

		try {
			listParish = maintenanceService.lookupParish(parishCriteria);
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Clean out dataset to prevent duplication
		roadOperationView.getHashMapOfParishes().clear();

		parishDataMod = new ParishDataModel(listParish);
		roadOperationView.setParishDataMod(parishDataMod);
		System.err.println("LIST par.getParishCode()::");
		// Populate Master List with Parishes
		for (ParishBO par : listParish) {

			roadOperationView.getHashMapOfParishes().put(par.getParishCode(),
					par);
			System.err.println("par.getParishCode()::" + par.getParishCode());

		}

		/************************2014-10-27:REDO******************************************/
		//added by kpowell-20140825- parish changes
				List<ParishBO> allParishes = new ArrayList<ParishBO>();
				try {
					copyFields(allParishes,listParish);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				roadOperationView.setAllParishes(allParishes);
				System.out.println("ALL Parishes>>"+ roadOperationView.getAllParishes().size());
		//
		/*****************************************************************/		
		context.getFlowScope().put("parishList", listParish);
		context.getFlowScope().put("parishDataMod", parishDataMod);
		context.getFlowScope().put("operation", roadOperationView);

		return success();
	}

	public ParishBO getParishForLocation(String parishCode){
		ParishBO selectedParish = new ParishBO();
		
		List<ParishBO> parishes = (List<ParishBO>) RequestContextHolder.getRequestContext().getFlowScope().get("parishList");
		for(ParishBO par : parishes){
			if(parishCode.equals(par.getParishCode())){
				try {
					org.fsl.roms.view.ObjectUtils.copyProperties(par, selectedParish);
					break;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		System.err.println("getParishForLocation["+ org.fsl.roms.view.ObjectUtils.objectToString(selectedParish));
		return selectedParish;
	}
	
	public LocationBO getLocationForArtery(Integer locId, String parishCode){
		LocationBO selectedLocation = new LocationBO();
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		List<LocationBO> availableLocations = new ArrayList<LocationBO>();

		Map<String, List<LocationBO>> mapOfLocations = roadOperationView
				.getHashMapOfLocations();

		List<LocationBO> locations = mapOfLocations.get(parishCode);
		for(LocationBO loc : locations){
			if(locId.equals(loc.getLocationId())){
				try {
					org.fsl.roms.view.ObjectUtils.copyProperties(loc, selectedLocation);
					break;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		System.err.println("selectedLocation"+ org.fsl.roms.view.ObjectUtils.objectToString(selectedLocation));
		return selectedLocation;
	}
	public Event getLocationList(RequestContext context) {

		System.err.println("inside getLocationList(RequestContext context)");
		Maintenance maintenanceService = super.getMaintenanceService();

		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		LocationCriteriaBO locationCriteriaBO = new LocationCriteriaBO();
		locationCriteriaBO.setStatusId(Constants.Status.ACTIVE);
		listLocations = new ArrayList<LocationBO>();

		try {
			listLocations = maintenanceService.lookupLocation(locationCriteriaBO);
			
			Collections.sort(listLocations, new Comparator<LocationBO>() {
				public int compare(LocationBO result1, LocationBO result2) {
					return result1.getShortDesc().toLowerCase().compareTo(result2.getShortDesc().toLowerCase());
				}
			});
			
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try{
		// Clean out dataset to prevent duplication
		roadOperationView.getHashMapOfLocations().clear();

		locationDataMod = new LocationDataModel(listLocations);

		// Populate Master List with Locations
		List<LocationBO> eachParishLocation = new ArrayList<LocationBO>();

		/***************2014-10-27-REDO******************/
		/*for (LocationBO loc : listLocations) {

			eachParishLocation = roadOperationView.getHashMapOfLocations().get(
					loc.getParishCode());

			if (eachParishLocation == null)
				eachParishLocation = new ArrayList<LocationBO>();

			eachParishLocation.add(loc);
			roadOperationView.getHashMapOfLocations().put(loc.getParishCode(),
					eachParishLocation);

			eachParishLocation = new ArrayList<LocationBO>();
		}*/

		roadOperationView.setAllLocationsMap(new HashMap<String, List<LocationBO>>());
		for (ParishBO par : roadOperationView.getAllParishes()) {
			//build AllLocationsMap
			eachParishLocation = new ArrayList<LocationBO>();	
			for (LocationBO loc : listLocations) {				
				if(par.getParishCode().trim().equalsIgnoreCase(loc.getParishCode().trim())){
					//System.err.println("EQUAL loc.getParishCode()>>"+par.getParishCode()+">>"+ loc.getParishCode());

					eachParishLocation.add(loc);
				
				//eachParishLocation = new ArrayList<LocationBO>();
				}
				
			}
			
			if(eachParishLocation.size()>0){
				roadOperationView.getHashMapOfLocations().put(par.getParishCode(),
						eachParishLocation);
				//added by kpowell-20140825- parish changes
				roadOperationView.getAllLocationsMap().put(par.getParishCode(),
						eachParishLocation);
			}
//			System.out.println("ALL Locations>>"+ roadOperationView.getAllLocationsMap().get(par.getParishCode()).size());
		}
		/*int i = 0;
		for (LocationBO locs : roadOperationView.getAllLocationsMap().get("01")) {
		System.out.println("ALL Locations>>"+ 
		org.fsl.roms.view.ObjectUtils.objectToString(locs));
		i++;
		}*/
		/***********************************/
		// context.getFlowScope().put("locationList", listLocations);
		context.getFlowScope().put("availableLocations", locationDataMod);
		context.getFlowScope().put("operation", roadOperationView);
		context.getFlowScope().put("locationDataMod", locationDataMod);

		
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		System.err.println("error Occurred in getLocationList");
		addErrorMessage(context, "SystemError");
	}
			System.err.println("exit getLocationList(RequestContext context)");
			return success();
	}

	/***********************2014-10-27-REDO***********************/
	public Map<Integer, List<ArteryBO>> getArteryList() {

		Maintenance maintenanceService = super.getMaintenanceService();

		RoadOperationView roadOperationView = new RoadOperationView();

		ArteryCriteriaBO arteryCriteriaBO = new ArteryCriteriaBO();
		arteryCriteriaBO.setStatusId(Constants.Status.ACTIVE);

		listArtery = new ArrayList<ArteryBO>();

		try {
			listArtery = maintenanceService.lookupArtery(arteryCriteriaBO);
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Clean out dataset to prevent duplication
		//roadOperationView.getHashMapOfArteries().clear();

		arteryDataMod = new ArteryDataModel(listArtery);

		// Populate Master List with Arteries
		List<ArteryBO> eachLocationArtery = new ArrayList<ArteryBO>();

		for (ArteryBO art : listArtery) {

			eachLocationArtery = roadOperationView.getHashMapOfArteries().get(
					art.getLocationId());

			if (eachLocationArtery == null)
				eachLocationArtery = new ArrayList<ArteryBO>();

			eachLocationArtery.add(art);
			roadOperationView.getHashMapOfArteries().put(art.getLocationId(),
					eachLocationArtery);
			
			/*//added by kpowell-20140825- parish changes
			roadOperationView.getAllArteriesMap().put(art.getLocationId(),
					eachLocationArtery);*/

			eachLocationArtery = new ArrayList<ArteryBO>();
		}

		// context.getFlowScope().put("arteryList", listArtery);
		//RequestContextHolder.getRequestContext().getFlowScope().put("operation", roadOperationView);
		// context.getFlowScope().put("arteryDataList",listArtery);

		return roadOperationView.getHashMapOfArteries();
	}

	public Event getArteryList(RequestContext context) {

		System.out.println("inside getArteryList(RequestContext context)");
		Maintenance maintenanceService = super.getMaintenanceService();

		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		ArteryCriteriaBO arteryCriteriaBO = new ArteryCriteriaBO();
		arteryCriteriaBO.setStatusId(Constants.Status.ACTIVE);

		listArtery = new ArrayList<ArteryBO>();

		try {
			listArtery = maintenanceService.lookupArtery(arteryCriteriaBO);
			
			Collections.sort(listArtery, new Comparator<ArteryBO>() {
				public int compare(ArteryBO result1, ArteryBO result2) {
					return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
				}
			});
			
		
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try{
		// Clean out dataset to prevent duplication
		roadOperationView.getHashMapOfArteries().clear();

		arteryDataMod = new ArteryDataModel(listArtery);

		// Populate Master List with Arteries
		List<ArteryBO> eachLocationArtery = new ArrayList<ArteryBO>();
		roadOperationView.setAllArteriesMap(new HashMap<Integer, List<ArteryBO>>());
		for (ArteryBO art : listArtery) {

			eachLocationArtery = roadOperationView.getHashMapOfArteries().get(
					art.getLocationId());

			if (eachLocationArtery == null)
				eachLocationArtery = new ArrayList<ArteryBO>();

			eachLocationArtery.add(art);
			roadOperationView.getHashMapOfArteries().put(art.getLocationId(),
					eachLocationArtery);
			
			//added by kpowell-20140825- parish changes
			roadOperationView.getAllArteriesMap().put(art.getLocationId(),
					eachLocationArtery);
			//System.out.println("ALL Arteries>>"+ roadOperationView.getAllArteriesMap().get(art.getLocationId()).size());

			eachLocationArtery = new ArrayList<ArteryBO>();
		}
		
		
		System.out.println("ALL Arteries>>"+ roadOperationView.getAllArteriesMap().size());

		// context.getFlowScope().put("arteryList", listArtery);
		context.getFlowScope().put("operation", roadOperationView);
		// context.getFlowScope().put("arteryDataList",listArtery);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("error Occurred in getArteryList");
			addErrorMessage(context, "SystemError");
		}
		System.out.println("exit getArteryList(RequestContext context)");
		return success();
	}
	/*public Event getArteryList(RequestContext context) {

		MaintenancePortProxy maintenanceProxy = super.getMaintenancePortProxy();

		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		ArteryCriteriaBO arteryCriteriaBO = new ArteryCriteriaBO();
		arteryCriteriaBO.setStatusId(Constants.Status.ACTIVE);

		listArtery = new ArrayList<ArteryBO>();

		try {
			listArtery = maintenanceProxy.lookupArtery(arteryCriteriaBO);
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Clean out dataset to prevent duplication
		roadOperationView.getHashMapOfArteries().clear();

		arteryDataMod = new ArteryDataModel(listArtery);

		// Populate Master List with Arteries
		List<ArteryBO> eachLocationArtery = new ArrayList<ArteryBO>();

		for (ArteryBO art : listArtery) {

			eachLocationArtery = roadOperationView.getHashMapOfArteries().get(
					art.getLocationId());

			if (eachLocationArtery == null)
				eachLocationArtery = new ArrayList<ArteryBO>();

			eachLocationArtery.add(art);
			roadOperationView.getHashMapOfArteries().put(art.getLocationId(),
					eachLocationArtery);

			eachLocationArtery = new ArrayList<ArteryBO>();
		}

		// context.getFlowScope().put("arteryList", listArtery);
		context.getFlowScope().put("operation", roadOperationView);
		// context.getFlowScope().put("arteryDataList",listArtery);

		return success();
	}*/
/*********************************************/
	
	
	public Event getRegionList(RequestContext context) {
				
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("theTable");
		HashMap<String, String> criteria = new HashMap<String, String>();
		
		if(dataTable == null){//only done for scheduling a new operation					
			criteria.put("status_code", "A");
		}

		List<RefCodeBO> listRegions = super.getRefInfo(
				"roms_lmis_ta_office_location_view", criteria);

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
	
	/******************2014-10-27-REDO****************************/
	public void addToSelectedArteriesToggle(ToggleSelectEvent toggle) 
	{
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		
		if(toggle.isSelected())
		{
			for(ArteryBO selectedArtery : roadOperationView.getSelectedArteryList())
			{
				this.addToSelectedArteries(selectedArtery);
			}
		}
		else
		{
			//2- remove all selected arteries for the location being removed
			List<ArteryBO> locationArteries = roadOperationView.getAllArteriesMap().
												get(roadOperationView.getCurrentTeam().getLocationCodeIndex());		
			//roadOperationView.getCurrentTeam().getSelectedTeamArteryList().removeAll(locationArteries);
					
			for(ArteryBO selectedArtery : locationArteries)
			{
				this.removeFromSelectedArteries(selectedArtery);
			}
		}
	}
	
	public void addToSelectedArteries(SelectEvent event) {
		ArteryBO selectedArtery = (ArteryBO) event.getObject();
		System.out.println("inside addToSelectedArteries()");
		this.addToSelectedArteries(selectedArtery);
		System.out.println("exit addToSelectedArteries()");
	}
	
	public void addToSelectedArteries(ArteryBO selectedArtery) {
		

		//System.err.println("inside addToSelectedArteries()");

		try{	
		/**
		 * new changes -kpowell
		 */
		RequestContext context = RequestContextHolder.getRequestContext();
		RoadOperationView roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");
				
		//get associated parish and check if selected
		boolean loactionFound = false;
		for (LocationBO locSel : roadOperationView.getCurrentTeam().getSelectedTeamLocationList()){
			System.err.println("for (LocationBO locSel : roadOperationView.getCurrentTeam().getSelectedTeamLocationList()){");
			if (locSel.getLocationId().equals(selectedArtery.getLocationId())){
				loactionFound = true;
				break;				
			}
		}
		
		if(loactionFound== false){
			System.err.println("Location Not Selected");
			//removeFromSelectedLocations(selectedLocation);
			addInfoMessageText(context,"Location : "+getLocationDesc(selectedArtery.getLocationId().intValue())+" must first be selected");
			
			//
			//addSelectedLocation(getLocationForArtery(selectedArtery.getLocationId(), selectedArtery.getParishCode()));
			
			//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(getLocationForArtery(selectedArtery.getLocationId(), selectedArtery.getParishCode())));
			//return error();
		}
				
		//		
		//1- due to ajax call a master list is kept for all selected locations per team
		if(roadOperationView.getCurrentTeam().getSelectedTeamArteryList()== null)
			roadOperationView.getCurrentTeam().setSelectedTeamArteryList(new ArrayList<ArteryBO>());
		
		if(roadOperationView.getCurrentTeam().getSelectedTeamArteryList().contains(selectedArtery)== false){
			roadOperationView.getCurrentTeam().getSelectedTeamArteryList().add(selectedArtery);
			//System.err.println("roadOperationView.getCurrentTeam().getSelectedTeamArteryList().contains(selectedArtery)"+ roadOperationView.getCurrentTeam().getSelectedTeamArteryList().contains(selectedArtery));
		}
		//System.out.println(roadOperationView.getCurrentTeam().getLocationCodeIndex()+ "--"+ roadOperationView.getAllArteriesMap().size());
		/*System.out.println("operation.allArteriesMap.get(operation.currentTeam.locationCodeIndex)::"+
				roadOperationView.getAllArteriesMap().get(roadOperationView.getCurrentTeam().getLocationCodeIndex()).size());*/
		
		// Update Objects on Flow- to be investigated
		//context.getFlowScope().put("operation", roadOperationView);
		System.out.println("SelectedArteryList::"+ roadOperationView.getCurrentTeam().getSelectedTeamArteryList().size());	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.err.println("exit addToSelectedArteries()");
	}

	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void removeFromSelectedArteries(UnselectEvent event) {
		ArteryBO selectedArtery = (ArteryBO) event.getObject();
		System.out.println("inside removeFromSelectedArteries()");
		this.removeFromSelectedArteries(selectedArtery);
		System.out.println("exit removeFromSelectedArteries()");

	}
	
	public void removeFromSelectedArteries(ArteryBO selectedArtery) {
		
		try{
			RequestContext context = RequestContextHolder.getRequestContext();
			RoadOperationView roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");

			System.out.println("SelectedTeamArteryList::"+ roadOperationView.getCurrentTeam().getSelectedTeamArteryList().size());
			//1- due to ajax call REMOVE from master list kept for all selected locations per team
			roadOperationView.getCurrentTeam().getSelectedTeamArteryList().remove(selectedArtery);	
			roadOperationView.getSelectedArteryList().remove(selectedArtery);	
			
			//begin: Method used since the remove facilitate was not removing the object when return from Search-RoadOp
			Iterator<ArteryBO> itr = roadOperationView.getCurrentTeam().getSelectedTeamArteryList().iterator();
			while (itr.hasNext()) {
			    ArteryBO arteryItem = itr.next();
			       if (arteryItem.getArteryId().equals(selectedArtery.getArteryId())) {
			    	   itr.remove();
			    	   break;
			       }
			}
			
			Iterator<ArteryBO> itr2 = roadOperationView.getSelectedArteryList().iterator();
			while (itr2.hasNext()) {
			    ArteryBO arteryItem = itr2.next();
			       if (arteryItem.getArteryId().equals(selectedArtery.getArteryId())) {
			    	   itr2.remove();
			    	   break;
			       }
			}
			//end

			
			// Update Objects on Flow
			context.getFlowScope().put("operation", roadOperationView);
			System.out.println("SelectedTeamArteryList::"+ roadOperationView.getCurrentTeam().getSelectedTeamArteryList().size());

			/**
			 * 
			 */		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/*public void addToSelectedArteriesToggle(ToggleSelectEvent toggle) 
	{
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		
		if(toggle.isSelected())
		{
			for(ArteryBO selectedArtery : roadOperationView.getCurrentTeam().getCurrentTeamListOperationArteryList())
			{
				this.addToSelectedArteries(selectedArtery);
			}
		}
		else
		{
			for(ArteryBO selectedArtery : (List<ArteryBO>)RequestContextHolder
					.getRequestContext().getFlowScope().get("arteryList"))
			{
				this.removeFromSelectedArteries(selectedArtery);
			}
		}
	}
	
	public void addToSelectedArteries(SelectEvent event) {
		ArteryBO selectedArtery = (ArteryBO) event.getObject();

		this.addToSelectedArteries(selectedArtery);
	}
	
	public void addToSelectedArteries(ArteryBO selectedArtery) {
		

		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		roadOperationView.getHashSelectedArteries().put(
				selectedArtery.getArteryId(), selectedArtery);

		// update hash map for team to ensure proper saving of selected values
		List<ArteryBO> arteries = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamArteries().get(selectedArtery.getLocationId());

		if (arteries != null) {
			for (ArteryBO arteryBO : arteries) {
				if (arteryBO.getArteryId().equals(selectedArtery.getArteryId())) {
					arteryBO.setSelected(true);
					break;
				}
			}
		}
		roadOperationView.getCurrentTeam().getHashMapOfTeamArteries()
				.put(selectedArtery.getLocationId(), arteries);

		// RequestContextHolder.getRequestContext().getFlowScope().put("operation",roadOperationView);
	}

	public void removeFromSelectedArteries(UnselectEvent event) {
		ArteryBO selectedArtery = (ArteryBO) event.getObject();

		this.removeFromSelectedArteries(selectedArtery);

	}
	
	public void removeFromSelectedArteries(ArteryBO selectedArtery) {
		

		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		roadOperationView.getHashSelectedArteries().remove(
				selectedArtery.getArteryId());

		// update hash map for team to ensure proper saving of selected values
		List<ArteryBO> arteries = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamArteries().get(selectedArtery.getLocationId());

		if (arteries != null) {
			for (ArteryBO arteryBO : arteries) {
				if (arteryBO.getArteryId().equals(selectedArtery.getArteryId())) {
					arteryBO.setSelected(false);
					break;
				}
			}
		}
		roadOperationView.getCurrentTeam().getHashMapOfTeamArteries()
				.put(selectedArtery.getLocationId(), arteries);

		// RequestContextHolder.getRequestContext().getFlowScope().put("operation",roadOperationView);

	}*/
	
	/*****************************************************************/
	/**************************2014-10-27-REDO***********/
	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void addToSelectedLocationsToggle(ToggleSelectEvent toggle) 
	{
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		
		if(toggle.isSelected())
		{
			for(LocationBO selectedLocation : roadOperationView.getSelectedLocationList())
			{
				addSelectedLocation(selectedLocation);
			}
		}
		else
		{
			List<LocationBO> parishLocations = roadOperationView.getAllLocationsMap().
												get(roadOperationView.getCurrentTeam().getParishCodeIndex());
			for(LocationBO selectedLocation : parishLocations)
			{
				removeFromSelectedLocations(selectedLocation);
			}
		}
	}

	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void addToSelectedLocations(SelectEvent event) 
	{
		LocationBO selectedLocation = (LocationBO) event.getObject();
		System.out.println("inside addToSelectedLocations()");
		this.addSelectedLocation(selectedLocation);
		System.out.println("exit addToSelectedLocations()");
	}
	
	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void addSelectedLocation(LocationBO selectedLocation)
	{
		try{	
		/**
		 * new changes -kpowell
		 */
		RequestContext context = RequestContextHolder.getRequestContext();
		RoadOperationView roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");
		
		//get associated parish and check if selected
		boolean parishFound = false;
		for (ParishBO parSel : roadOperationView.getCurrentTeam().getSelectedTeamParishList()){
			System.err.println("for (ParishBO parSel : roadOperationView.getCurrentTeam().getSelectedTeamParishList()){");
			if (parSel.getParishCode().equals(selectedLocation.getParishCode())){
				parishFound = true;
				break;				
			}
		}
		
		if(parishFound== false){
			System.err.println("Parish Not Selected");
			//removeFromSelectedLocations(selectedLocation);
			addInfoMessageText(context,"Parish : "+getParishDesc(selectedLocation.getParishCode())+" must first be selected");
		//	addToSelectedParishes(getParishForLocation(selectedLocation.getParishCode()));
			//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(getParishForLocation(selectedLocation.getParishCode())));
			//return error();
		}
		
		
		
		roadOperationView.getCurrentTeam().setLocationCodeIndex(selectedLocation.getLocationId());
				
		//1- due to ajax call a master list is kept for all selected locations per team
		if(roadOperationView.getCurrentTeam().getSelectedTeamLocationList()== null)
			roadOperationView.getCurrentTeam().setSelectedTeamLocationList(new ArrayList<LocationBO>());
		
		if(!roadOperationView.getCurrentTeam().getSelectedTeamLocationList().contains(selectedLocation)){
			roadOperationView.getCurrentTeam().getSelectedTeamLocationList().add(selectedLocation);
		}
		
		//2- due to ajax calls Set currentteam selectedLArteryList to selectedTeamArteryList(total)
		roadOperationView.getSelectedArteryList().addAll(roadOperationView.getCurrentTeam().getSelectedTeamArteryList());
		
		
		
		
		
		// Update Objects on Flow
		context.getFlowScope().put("operation", roadOperationView);
		System.out.println("SelectedLocationList::"+ roadOperationView.getCurrentTeam().getSelectedTeamLocationList().size());	
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
		
	}

	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void removeFromSelectedLocations(UnselectEvent event) {
		LocationBO selectedLocation = (LocationBO) event.getObject();
		System.out.println("inside removeFromSelectedLocations()"+ selectedLocation.getLocationId());
		
		this.removeFromSelectedLocations(selectedLocation);
		
		System.out.println("exit removeFromSelectedLocations()"+ selectedLocation.getLocationId());
	}
	
	/**
	 * updated @KPOWELL
	 * @param selectedLocation
	 */
	public void removeFromSelectedLocations(LocationBO selectedLocation) {
		try{
		/**
		 * new changes -kpowell
		 */
		RequestContext context = RequestContextHolder.getRequestContext();
		RoadOperationView roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");
		roadOperationView.getCurrentTeam().setLocationCodeIndex(selectedLocation.getLocationId());		
		
		//1- due to ajax call REMOVE from master list kept for all selected locations per team
		roadOperationView.getCurrentTeam().getSelectedTeamLocationList().remove(selectedLocation);	
		//roadOperationView.getSelectedLocationList().remove(selectedLocation);
				
		//2- remove all selected arteries for the location being removed
		List<ArteryBO> locationArteries = roadOperationView.getAllArteriesMap().get(roadOperationView.getCurrentTeam().getLocationCodeIndex());
		roadOperationView.getCurrentTeam().getSelectedTeamArteryList().removeAll(locationArteries);
		
		
		//begin: Method used since the remove facilitate was not removing the object when return from Search-RoadOp
		
		Iterator<ArteryBO> itr2 = roadOperationView.getCurrentTeam().getSelectedTeamArteryList().iterator();
		while (itr2.hasNext()) {
		    ArteryBO arteryItem = itr2.next();
		       if (arteryItem.getLocationId().equals(selectedLocation.getLocationId())) {
		    	   itr2.remove();
		       }
		}
		
		Iterator<ArteryBO> itr3 = roadOperationView.getSelectedArteryList().iterator();
		while (itr3.hasNext()) {
		    ArteryBO arteryItem = itr3.next();
		       if (arteryItem.getLocationId().equals(selectedLocation.getLocationId())) {
		    	   itr3.remove();
		       }
		}
		
		Iterator<LocationBO> itr = roadOperationView.getCurrentTeam().getSelectedTeamLocationList().iterator();
		while (itr.hasNext()) {
			LocationBO locationItem = itr.next();
		       if (locationItem.getLocationId().equals(selectedLocation.getLocationId())) {
		    	   itr.remove();
		    	   break;
		       }
		}
		
		
		//end

		
		//3- due to ajax calls Set currentteam selectedLArteryList to selectedTeamArteryList(total)
		roadOperationView.getSelectedArteryList().addAll(roadOperationView.getCurrentTeam().getSelectedTeamArteryList());
			
		
		
		// Update Objects on Flow
		context.getFlowScope().put("operation", roadOperationView);
		System.out.println("SelectedLocationList::"+ roadOperationView.getCurrentTeam().getSelectedTeamLocationList().size());
		
		/**
		 * 
		 */		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		

	}
	
	
	/****************************************************/
	
	
/*TO BE REMOVED 2014-10-27
 * 	public void addToSelectedLocationsToggle(ToggleSelectEvent toggle) 
 
	{
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		
		if(toggle.isSelected())
		{
			for(LocationBO selectedLocation : roadOperationView.getCurrentTeam().getCurrentTeamListLocationList())
			{
				addSelectedLocation(selectedLocation);
			}
		}
		else
		{
			List<LocationBO> parishLocations = roadOperationView.getAllLocationsMap().
					get(roadOperationView.getCurrentTeam().getParishCodeIndex());
			
			for(LocationBO selectedLocation : (List<LocationBO>)RequestContextHolder
					.getRequestContext().getFlowScope().get("locationList"))
			{
				removeFromSelectedLocations(selectedLocation);
			}
		}
	}

	public void addToSelectedLocations(SelectEvent event) 
	{
		LocationBO selectedLocation = (LocationBO) event.getObject();
		
		this.addSelectedLocation(selectedLocation);
	}
	
	public void addSelectedLocation(LocationBO selectedLocation)
	{
		populateArteryList(selectedLocation);

		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		roadOperationView.getHashSelectedLocations().put(
				selectedLocation.getLocationId(), selectedLocation);

		// update hash map for team to ensure proper saving of selected values
		List<LocationBO> locations = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamLocations()
				.get(selectedLocation.getParishCode());

		if (locations != null) {
			for (LocationBO locationBO : locations) {
				if (locationBO.getLocationId().equals(
						selectedLocation.getLocationId())) {
					locationBO.setSelected(true);
					break;
				}
			}
		}
		// remove items
		if (locations != null) {
			roadOperationView.getCurrentTeam().getHashMapOfTeamLocations()
					.remove(selectedLocation.getParishCode());
		}
		// add back to the map
		roadOperationView.getCurrentTeam().getHashMapOfTeamLocations()
				.put(selectedLocation.getParishCode(), locations);

		// Set list of selected locations based on existing hashmap
		List<ArteryBO> teamArteries = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamArteries()
				.get(selectedLocation.getLocationId());
		List<ArteryBO> selTeamArtAux = new ArrayList<ArteryBO>();

		if (teamArteries != null) {
			for (ArteryBO arteryBO : teamArteries) {
				if (arteryBO.isSelected()) {
					selTeamArtAux.add(arteryBO);
				}
			}
		}

		if (roadOperationView.getCurrentTeam()
				.getCurrentTeamListOperationArteryList() == null) {
			roadOperationView.getCurrentTeam()
					.setCurrentTeamListOperationArteryList(
							new ArrayList<ArteryBO>());
		}

		List<ArteryBO> teamArteryList = (List<ArteryBO>) RequestContextHolder
				.getRequestContext().getFlowScope()
				.get("currentOperationArteryList");

		//*
		// * if(teamArteryList!=null){
		// * //roadOperationView.getCurrentTeam().getOperationLocationList
		// * ().addAll(teamLocationList);
		// * System.err.println("BEFORE currentOperationArteryList::"+
		 * selTeamArtAux.size()); //selTeamArtAux.addAll(teamArteryList);
		// * System.err.println("currentOperationArteryList AFTER session add::"+
		/// * selTeamArtAux.size());
		// * 
		/ * }
		// *

		roadOperationView.getCurrentTeam()
				.getCurrentTeamListOperationArteryList().addAll(selTeamArtAux);

		// RequestContextHolder.getRequestContext().getFlowScope().put("currentOperationArteryList",
		// selTeamArtAux);

		// Update Objects on Flow
		RequestContextHolder.getRequestContext().getFlowScope()
				.put("operation", roadOperationView);
	}

	public void removeFromSelectedLocations(UnselectEvent event) {
		LocationBO selectedLocation = (LocationBO) event.getObject();

		this.removeFromSelectedLocations(selectedLocation);
	}
	
	public void removeFromSelectedLocations(LocationBO selectedLocation) {
		

		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		roadOperationView.getHashSelectedLocations().remove(
				selectedLocation.getLocationId());

		// update hash map for team to ensure proper saving of selected values
		List<LocationBO> locations = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamLocations()
				.get(selectedLocation.getParishCode());

		if (locations != null) {
			for (LocationBO locationBO : locations) {
				if (locationBO.getLocationId().equals(
						selectedLocation.getLocationId())) {
					locationBO.setSelected(false);
					break;
				}
			}
		}
		roadOperationView.getCurrentTeam().getHashMapOfTeamLocations()
				.put(selectedLocation.getParishCode(), locations);

		// unselect related arteries
		List<ArteryBO> arteries = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamArteries()
				.get(selectedLocation.getLocationId());

		if (arteries != null) {
			for (ArteryBO arteryBO : arteries) {
				if(arteryBO.isSelected()){
					roadOperationView.getHashSelectedArteries().remove(
							arteryBO.getArteryId());
					System.err.println("ARtery unselected::"+ arteryBO.getArteryDescription());
				}
				arteryBO.setSelected(false);
			}

		}

		roadOperationView.getCurrentTeam().getHashMapOfTeamArteries()
				.put(selectedLocation.getLocationId(), arteries);

		// Empty Artery List
		RequestContextHolder.getRequestContext().getFlowScope()
				.put("arteryList", new ArrayList<ArteryBO>());

		// Update on flow
		RequestContextHolder.getRequestContext().getFlowScope()
				.put("operation", roadOperationView);

	}
	*/

	
	/**2014-10-27
	 * updated @KPOWELL
	 * @param event
	 */
	public void addToSelectedParishes(SelectEvent event) {
		ParishBO selectedParish = (ParishBO) event.getObject();
		
		System.out.println("inside addToSelectedParishes()"+ selectedParish.getParishCode());
		
		this.addToSelectedParishes(selectedParish);
		
		System.out.println("exit addToSelectedParishes()");

	}	
	
	
	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void addToSelectedParishes(ParishBO selectedParish) {
		try{
		RequestContext context = RequestContextHolder.getRequestContext();

		System.err.println("addToSelectedParishes()");
		/**
		 * new changes -kpowell
		 */
		RoadOperationView roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");
		roadOperationView.getCurrentTeam().setParishCodeIndex(selectedParish.getParishCode());
		
		//set to first location
		List<LocationBO> parishLocations = roadOperationView.getAllLocationsMap().
				get(roadOperationView.getCurrentTeam().getParishCodeIndex());
		roadOperationView.getCurrentTeam().setLocationCodeIndex((parishLocations.get(0)).getLocationId());
						
		//1- due to ajax calls Set currentteam selectedLocationList to selectedTeamLocationList(total)
		roadOperationView.getSelectedLocationList().addAll(roadOperationView.getCurrentTeam().getSelectedTeamLocationList());
		
		//2- due to ajax calls Set currentteam selectedLArteryList to selectedTeamArteryList(total)
		roadOperationView.getSelectedArteryList().addAll(roadOperationView.getCurrentTeam().getSelectedTeamArteryList());
		
		// Update Objects on Flow
		context.getFlowScope().put("operation", roadOperationView);
		System.out.println("SelectedParishList::"+ roadOperationView.getCurrentTeam().getSelectedTeamParishList().size());
		
		context.getFlowScope().put("warnAbtJPMessages", 'Y');
		context.getMessageContext()
				.addMessage(
						new MessageBuilder()
								.warning()
								.defaultText(
										"Please note that any changes made to the selected "
												+ "parishes will affect the available J.P.s for this team. Please check to ensure that the required J.P.s are selected.")
								.build());
		//TODO BE REVIEWED
		populateJPList(roadOperationView);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**public void addToSelectedParishes(ParishBO selectedParish) {
		RequestContext context = RequestContextHolder.getRequestContext();

		System.err.println("addToSelectedParishes()");
	//	ParishBO selectedParish = (ParishBO) event.getObject();

		populateLocationList(selectedParish);
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		// System.err.println("before roadOperationView.getHashSelectedParishes()::"+
		// roadOperationView.getHashSelectedParishes().size());
		roadOperationView.getHashSelectedParishes().put(
				selectedParish.getParishCode(), selectedParish);

		// System.err.println("after roadOperationView.getHashSelectedParishes()::"+
		// roadOperationView.getHashSelectedParishes().size());
		// update hash map for team to ensure proper saving of selected values
		// ParishBO parish =
		// roadOperationView.getCurrentTeam().getHashMapOfTeamParishes().get(selectedParish.getParishCode());
		// if(parish != null) parish.setSelected(true);
		selectedParish.setSelected(true);
		roadOperationView.getCurrentTeam().getHashMapOfTeamParishes()
				.put(selectedParish.getParishCode(), selectedParish);

		// Set list of selected locations based on existing hashmap
		List<LocationBO> teamLocations = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamLocations()
				.get(selectedParish.getParishCode());
		List<LocationBO> selTeamLocAux = new ArrayList<LocationBO>();

		context.getFlowScope().put("arteryList", null);//empty artery list by default unless a location has been selected
		
		if (teamLocations != null) {
			for (LocationBO locationBO : teamLocations) {
				if (locationBO.isSelected()) {
					selTeamLocAux.add(locationBO);
					
					// populate artery list with artery for 1st selected location, since location is required
					System.err.println("populate artery list with artery for 1st selected location, since location is required");
					populateArteryList(locationBO);					
				}
			}
		}

		if (roadOperationView.getCurrentTeam().getCurrentTeamListLocationList() == null) {
			roadOperationView.getCurrentTeam().setCurrentTeamListLocationList(
					new ArrayList<LocationBO>());
		}

		List<LocationBO> teamLocationList = (List<LocationBO>) RequestContextHolder
				.getRequestContext().getFlowScope()
				.get("currentoperationLocationList");

		
		// * if(teamLocationList!=null){
		// * //roadOperationView.getCurrentTeam().getOperationLocationList
		// * ().addAll(teamLocationList);
		// * System.err.println("BEFORE getCurrentTeamListLocationList::"+
		// * selTeamLocAux.size()); //selTeamLocAux.addAll(teamLocationList);
		// * System
		// * .err.println("getCurrentTeamListLocationList AFTER session add::"+
		// * selTeamLocAux.size());
		// * 
		 //* }
		 

		roadOperationView.getCurrentTeam().getCurrentTeamListLocationList()
				.addAll(selTeamLocAux);
		populateJPList(roadOperationView);
		
		System.err.println("After JPLIST Src"+ roadOperationView.getListOfJP().getSource().size() );
		System.err.println("After JPLIST Target"+ roadOperationView.getListOfJP().getTarget().size() );
		// context.getFlowScope().put("currentOperationParishList",
		// selectedTeam.getOperationParishList());
		// context.getFlowScope().put("currentoperationLocationList",
		// selTeamLocAux);

		// Update Objects on Flow
		context.getFlowScope().put("operation", roadOperationView);
		//context.getFlowScope().put("arteryList", null);
		
		//if(roadOperationView.getListOfJP().getSource().size()==0 && roadOperationView.getListOfJP().getTarget().size()==0 ){
			context.getFlowScope().put("warnAbtJPMessages", 'Y');
	
			context.getMessageContext()
					.addMessage(
							new MessageBuilder()
									.warning()
									.defaultText(
											"Please note that any changes made to the selected "
													+ "parishes will affect the available J.P.s for this team. Please check to ensure that the required J.P.s are selected.")
									.build());
		//}
	}
**/
	
	public void addToSelectedParishesToggle(ToggleSelectEvent toggle) 
	{
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		
		if(toggle.isSelected())
		{
			/************2014-10-27:REDO************/
			//for(ParishBO selectedParish : roadOperationView.getCurrentTeam().getCurrentTeamListOperationParishList())
			for(ParishBO selectedParish : roadOperationView.getCurrentTeam().getSelectedTeamParishList())
			{
				this.addToSelectedParishes(selectedParish);
			}
		}
		else
		{
		
			for(ParishBO selectedParish : (List<ParishBO>)RequestContextHolder
					.getRequestContext().getFlowScope().get("parishList"))
			{
				this.removeFromSelectedParishes(selectedParish);
			}
		}
	}
	
	
	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void removeFromSelectedParishes(UnselectEvent event) {
		ParishBO selectedParish = (ParishBO) event.getObject();
		System.out.println("inside removeFromSelectedParishes()"+ selectedParish.getParishCode());
		
		this.removeFromSelectedParishes(selectedParish);
		
		System.out.println("exit removeFromSelectedParishes()");
	}
	
	/**
	 * updated @KPOWELL
	 * @param event
	 */
	public void removeFromSelectedParishes(ParishBO selectedParish) {
		RequestContext context = RequestContextHolder.getRequestContext();
		try{
			

		/**
		 * new changes -kpowell
		 */
		RoadOperationView roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");
		roadOperationView.getCurrentTeam().setParishCodeIndex(selectedParish.getParishCode());
		
		
		//1- Remove from currentteam selectedLocationList (total)
		List<LocationBO> parishLocations = roadOperationView.getAllLocationsMap().
				get(roadOperationView.getCurrentTeam().getParishCodeIndex());
		roadOperationView.getCurrentTeam().getSelectedTeamLocationList().removeAll(parishLocations);
		
		//2- due to ajax calls Set currentteam selectedLocationList to selectedTeamLocationList(total)
		//roadOperationView.getSelectedLocationList().addAll(roadOperationView.getCurrentTeam().getSelectedTeamLocationList());
		roadOperationView.getSelectedLocationList().removeAll(parishLocations);
		
		//3- remove arteries for the parish/locations being removed --kpowell2015-02-23
		for (Iterator iterator = parishLocations.iterator(); iterator.hasNext();) {
			LocationBO locationBO = (LocationBO) iterator.next();
			List<ArteryBO> arteryLocations = roadOperationView.getAllArteriesMap().get(locationBO.getLocationId());
			
			if(arteryLocations!= null){
			roadOperationView.getCurrentTeam().getSelectedTeamArteryList().removeAll(arteryLocations);
			roadOperationView.getSelectedArteryList().removeAll(arteryLocations);
			}
			
		}
		
		
		System.out.println("roadOperationView.getCurrentTeam().getSelectedTeamLocationList()"+ 
				roadOperationView.getCurrentTeam().getSelectedTeamLocationList().size());
		
		System.out.println("roadOperationView.getSelectedLocationList()"+ roadOperationView.getSelectedLocationList().size());
		
		//begin: Method used since the remove facilitate was not removing the object when return from Search-RoadOp
		
		
		Iterator<LocationBO> itr2 = roadOperationView.getSelectedLocationList().iterator();
		Iterator<ArteryBO> itr3 = roadOperationView.getCurrentTeam().getSelectedTeamArteryList().iterator();
		Iterator<ArteryBO> itr4 = roadOperationView.getSelectedArteryList().iterator();
		while (itr2.hasNext()) {
		    LocationBO locationItem = itr2.next();
		       if (locationItem.getParishCode().equals(selectedParish.getParishCode()) ){   	   		    	   
		    	   
		    	   //remove associated arteries			    	
			   		while (itr3.hasNext()) {
			   		    ArteryBO arteryItem = itr3.next();
			   		       if (arteryItem.getLocationId().equals(locationItem.getLocationId()) ){
			   		    	System.out.println("removed Artery:"+ arteryItem.getArteryDescription());
			   		    	   itr3.remove();			   		    	   
			   		       }
			   		}			   		
			   		
			   	//remove associated arteries			    	
			   		while (itr4.hasNext()) {
			   		    ArteryBO arteryItem2 = itr4.next();
			   		       if (arteryItem2.getLocationId().equals(locationItem.getLocationId()) ){
			   		    	System.out.println("removed Artery:"+ arteryItem2.getArteryDescription());
			   		    	   itr4.remove();
			   		    	 
			   		       }
			   		}
			   		System.out.println("removed Location:"+ locationItem.getLocationDescription());
			    	itr2.remove();
		    	   
		       }
		}
		
		Iterator<LocationBO> itr = roadOperationView.getCurrentTeam().getSelectedTeamLocationList().iterator();
		while (itr.hasNext()) {
			LocationBO locationItem = itr.next();
		       if (locationItem.getParishCode().equals(selectedParish.getParishCode())) {
		    	   itr.remove();
		    	   break;
		       }
		}
		
		
		//end
		
		//remove the parish from the list ----kpowell2015-02-23
		roadOperationView.getCurrentTeam().getSelectedTeamParishList().remove(selectedParish);
		
		//reset locationCode to first location in listing
		roadOperationView.getCurrentTeam().setLocationCodeIndex((parishLocations.get(0)).getLocationId());
				
		// Update Objects on Flow
		context.getFlowScope().put("operation", roadOperationView);
		System.out.println("SelectedParishList::"+ roadOperationView.getCurrentTeam().getSelectedTeamParishList().size());
		
		
		/**
		 * 
		 */	
		
		context.getFlowScope().put("warnAbtJPMessages", 'Y');	
		context.getMessageContext()
				.addMessage(
						new MessageBuilder()
								.warning()
								.defaultText(
										"Please note that any changes made to the selected "
												+ "parishes will affect the available J.P.s for this team. Please check to ensure that the required J.P.s are selected.")
								.build());
		
		populateJPList(roadOperationView);
		removeJPByParish(selectedParish.getParishCode(), roadOperationView);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
		}

	}
	
	/*public void removeFromSelectedParishes(ParishBO selectedParish) {
		RequestContext context = RequestContextHolder.getRequestContext();

		//ParishBO selectedParish = (ParishBO) event.getObject();

		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		roadOperationView.getHashSelectedParishes().remove(
				selectedParish.getParishCode());

		System.err.println("removeFromSelectedParishes()");
		*//*******************************************************************//*
		// update hash map for team to ensure proper saving of selected values
		ParishBO parish = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamParishes().get(selectedParish.getParishCode());

		// unselect parish
		parish.setSelected(false);
		roadOperationView.getCurrentTeam().getHashMapOfTeamParishes()
				.put(selectedParish.getParishCode(), parish);

		// unselect related locations
		List<LocationBO> locations = roadOperationView.getCurrentTeam()
				.getHashMapOfTeamLocations()
				.get(selectedParish.getParishCode());

		//**
		// * added by kpowell
		// 
		List<LocationBO> newLocationList = new ArrayList<LocationBO>();
		List<ArteryBO> newArteryList = new ArrayList<ArteryBO>();
		List<LocationBO> teamLocationList = (List<LocationBO>) RequestContextHolder
				.getRequestContext().getFlowScope()
				.get("currentoperationLocationList");
		List<ArteryBO> teamArteryList = (List<ArteryBO>) RequestContextHolder
				.getRequestContext().getFlowScope()
				.get("currentOperationArteryList");
		List<ArteryBO> arteries = new ArrayList<ArteryBO>();
		if (locations != null) {
			for (LocationBO locationBO : locations) {
				
				if(locationBO.isSelected()){
					roadOperationView.getHashSelectedLocations().remove(
						locationBO.getLocationId());
					System.err.println("Location unselected::"+ locationBO.getLocationDescription());
				}
				locationBO.setSelected(false);
				//System.err.println("unselect locationBO::"+locationBO.getLocationDescription()+" "+locationBO.isSelected());
				
				// unselect related arteries
				arteries = roadOperationView.getCurrentTeam()
						.getHashMapOfTeamArteries()
						.get(locationBO.getLocationId());

				if (arteries != null) {
					for (ArteryBO arteryBO : arteries) {
						
						if(arteryBO.isSelected()){
							roadOperationView.getHashSelectedArteries().remove(
									arteryBO.getArteryId());
							System.err.println("ARtery unselected::"+ arteryBO.getArteryDescription());
						}
						arteryBO.setSelected(false);
						System.err.println("unselect  arteryBO::"+arteryBO.getArteryDescription()+" "+arteryBO.isSelected());
						
					}
				}

				roadOperationView.getCurrentTeam().getHashMapOfTeamArteries().get(locationBO.getLocationId()).clear();
				roadOperationView.getCurrentTeam().getHashMapOfTeamArteries().put(locationBO.getLocationId(), arteries);

			}
			roadOperationView.getCurrentTeam().getHashMapOfTeamLocations().get(selectedParish.getParishCode()).clear();
			roadOperationView.getCurrentTeam().getHashMapOfTeamLocations().put(selectedParish.getParishCode(), locations);
		}

		//Remove all linked locations and arteries		
		roadOperationView.getCurrentTeam().getCurrentTeamListLocationList().removeAll(locations);
		roadOperationView.getCurrentTeam().getCurrentTeamListOperationArteryList().removeAll(arteries);
		
		List<ParishBO> teamParishList = (List<ParishBO>) RequestContextHolder
				.getRequestContext().getFlowScope()
				.get("currentOperationParishList");

		if (teamParishList != null) {
			// teamParishList.remove(parish.getParishCode());
			System.err.println("Parish deselected and removed");
		}

		// remove all location & arteries for the parish deselected - kpowell
		// context.getFlowScope().put("currentoperationLocationList",
		// newLocationList);
		// context.getFlowScope().put("currentOperationArteryList",
		// newArteryList);
		// context.getFlowScope().put("currentOperationParishList",
		// teamParishList);

		//**********************************************************
		// Empty Location List and Artery List
		context.getFlowScope().put("locationList", new ArrayList<LocationBO>());
		context.getFlowScope().put("arteryList", new ArrayList<ArteryBO>());

		context.getFlowScope().put("operation", roadOperationView);
		context.getFlowScope().put("warnAbtJPMessages", 'Y');

		populateJPList(roadOperationView);

		context.getMessageContext()
				.addMessage(
						new MessageBuilder()
								.warning()
								.defaultText(
										"Please note that any changes made to the selected "
												+ "parishes will affect the available J.P.s for this team. Please check to ensure that the required J.P.s are selected.")
								.build());

	}*/

	public void populateLocationList(ParishBO selectedParish) {

		System.err.println("This is parish " + selectedParish.getDescription());

		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		List<LocationBO> availableLocations = new ArrayList<LocationBO>();

		Map<String, List<LocationBO>> mapOfLocations = roadOperationView
				.getHashMapOfLocations();

		availableLocations = mapOfLocations.get(selectedParish.getParishCode());

		RequestContextHolder.getRequestContext().getFlowScope()
				.put("locationList", availableLocations);

		locationDataMod = new LocationDataModel(availableLocations);

	}

	public void populateArteryList(LocationBO selectedLocation) {

		System.err.println("This is location "
				+ selectedLocation.getLocationDescription());

		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		List<ArteryBO> availableArteries = new ArrayList<ArteryBO>();

		Map<Integer, List<ArteryBO>> mapOfArteries = roadOperationView
				.getHashMapOfArteries();

		availableArteries = mapOfArteries.get(selectedLocation.getLocationId());

		//added by kpowell: set selected Arteries for the Location
		if (availableArteries != null) {
			
			List<ArteryBO> selTeamArtAux =  new ArrayList<ArteryBO>();
			for (ArteryBO arteryBO : availableArteries) {
				if (arteryBO.isSelected()) {
					selTeamArtAux.add(arteryBO);
				}
			}
			
			if(selTeamArtAux.size()>0){
				roadOperationView.getCurrentTeam().getCurrentTeamListOperationArteryList().addAll(selTeamArtAux);
			}
			
		}
		
	//	System.err.println("arteryList"+ availableArteries.size());
		//
		RequestContextHolder.getRequestContext().getFlowScope()
				.put("arteryList", availableArteries);

		RequestContextHolder.getRequestContext().getFlowScope()
				.put("operation", roadOperationView);

	}

	public Event deleteTeams(RequestContext context) {

		//WHen updating and existing operation currenTeams cannot be NULL
		//mark the team deleted and keep it in  the list
		try{
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		// System.err.println("Selected Team " +
		// roadOperationView.getSelectedTeam().getTeamName());

		List<TeamView> currentTeams =  new ArrayList<TeamView>();;//= roadOperationView.getTeams();
		List<TeamView> deletedTeams = new ArrayList<TeamView>();
		List<TeamView> remainTeams = new ArrayList<TeamView>();
		int deletedTeamsCnt = 0;
		
		if (roadOperationView.getStatusId() != null) {
			System.out.println("roadOperationView.StatusId::"
					+ roadOperationView.getStatusId());
		} else {
			System.out.println("StatusId is NULL");
		}

		if (roadOperationView.getStatusId().equalsIgnoreCase("NONE")) {
			
			remainTeams = deleteNewOpTeams(roadOperationView,context); //replaces the code above since it will be used elsewhere
			//currentTeams.clear();
			currentTeams.addAll(remainTeams);

		} else {
			// TODO modify delete functionality below
			/*for (TeamView teamView : currentTeams) {

				System.err.println("Am i selected? " + teamView.getSelected()
						+ " --- " + teamView.getTeamName());

				if (teamView.getSelected().equalsIgnoreCase("true")) {
					//currentTeams.remove(teamView);
					teamView.setDelete(true);
					deletedTeams.add(teamView);
				}
			}*/
			deletedTeamsCnt = deleteExistingOpTeams(roadOperationView,context);
			roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");
			currentTeams.addAll(roadOperationView.getTeams());
		}
		
		//System.err.println("currentTeams"+currentTeams.size());
		//System.err.println("deletedTeams"+deletedTeams.size());
		if (currentTeams.size() == deletedTeamsCnt) {
			
			//WHen updating and existing operation currenTeams cannot be NULL
			//mark the team deleted and keep it in  the list
			if (roadOperationView.getStatusId().equalsIgnoreCase("NONE")){
				currentTeams = null;
			}
			System.err.println("No teams remain in teamDatatable ");
		}
		
		
		//sort the source picklist
		if(roadOperationView.getListOfStaff()!= null){
			Collections.sort(roadOperationView.getListOfStaff().getSource(),taStaffComperator );		
		}
		if(roadOperationView.getListOfITA()!= null){
			Collections.sort(roadOperationView.getListOfITA().getSource(),itaComperator );
		}
		if(roadOperationView.getListOfJP()!= null){
			Collections.sort(roadOperationView.getListOfJP().getSource(),jpComperator );
		}
		if(roadOperationView.getListOfVehicles()!= null){
			Collections.sort(roadOperationView.getListOfVehicles().getSource(),vehicleComperator );
		}
		//Collections.sort(roadOperationView.getListOfPolice().getSource(),policeComperator );

		
		roadOperationView.setTeams(currentTeams);
		context.getFlowScope().put("teamDatatable", currentTeams);
		context.getFlowScope().put("operation", roadOperationView);

		return success();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
			return error();
		}
	}

	
	/**
	 * @author kpowell
	 * @return
	 */
	public boolean noTeamExists(){
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		List<TeamView> currentTeams = roadOperationView.getTeams();
		int teamsMarkForDeleteCnt = 0;
		
		if(currentTeams== null){
			return true;
		}
		
		if(currentTeams!= null){
			for (TeamView teamView : currentTeams) {
	
				if (teamView.isDelete()) {
					System.err.println("Am i selected? " + teamView.getSelected()
							+ " --- " + teamView.getTeamName());
					teamsMarkForDeleteCnt++;
				}
				
			}
		
			if(currentTeams.size()== teamsMarkForDeleteCnt){
				//same size indicate all teams in the existing list has been deleted
				return true;
			}
		}
		return false;
	}
	
	
	public Event deleteTeam() {
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		System.err.println("Selected Team "
				+ roadOperationView.getSelectedTeam());

		List<TeamView> currentTeams = roadOperationView.getTeams();

		for (TeamView teamView : currentTeams) {
			System.err.println("Am i selected? " + teamView.getSelected()
					+ " --- " + teamView.getTeamName());
		}

		return success();
	}

	
	/**
	 * @author kpowell
	 * @param currentTeams
	 * @return
	 */
	private List<TeamView> deleteNewOpTeams(RoadOperationView roadOperationView, RequestContext context){

		List<TeamView> currentTeams = roadOperationView.getTeams();
		List<TeamView> remainTeams = new ArrayList<TeamView>();

		try{
			System.out.println("currentTeams size before delete::"
					+ currentTeams.size());
			Iterator<TeamView> iterator = currentTeams.iterator();
			while (iterator.hasNext()) {
				TeamView teamView = (TeamView) iterator.next();
				System.err.println("Am i selected? " + teamView.getSelected()
						+ " --- " + teamView.getTeamName());

				if (teamView.getSelected().equalsIgnoreCase("true")) {
					System.out.println("teamview to delete::"
							+ teamView.getTeamName());

					// TODO when a team is deleted replaced the resource
					if (teamView.getTaStaffList() != null) {
						System.err.println("Add TA STaff Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfStaff().getSource()
						.addAll(teamView.getTaStaffList());
						System.err.println("roadOperationView.getListOfStaff().getSource()"+ roadOperationView.getListOfStaff().getSource().size());

					}
					if (teamView.getItaExaminerList() != null) {
						System.err.println("Add ITA Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfITA().getSource()
						.addAll(teamView.getItaExaminerList());

					}
					if (teamView.getPoliceOfficerList() != null) {
						System.err.println("Add Police Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfPolice().getSource()
						.addAll(teamView.getPoliceOfficerList());

					}
					if (teamView.getJpList() != null) {
						System.err.println("Add JP Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfJP().getSource()
						.addAll(teamView.getJpList());

					}
					if (teamView.getTaVehicleList() != null) {
						System.err.println("Add Vehicles Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfVehicles().getSource()
						.addAll(teamView.getTaVehicleList());

					}
					
					///added
					//teamView.setDelete(true);					
					
				} else {

					// compile list of teams that were NOT deleted
					remainTeams.add(teamView);
				}
				
	
			}

			context.getFlowScope().put("operation", roadOperationView);
			//currentTeams.clear();
			//currentTeams.addAll(remainTeams);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("error Occurred in add_team");
			addErrorMessage(context, "SystemError");

		}
		return remainTeams;
	}
	
	
	/**
	 * @author kpowell
	 * delete team from previously save operation
	 * TODO merge with deleteNewOpTeams function
	 * @param roadOperationView
	 * @param context
	 * @return
	 */
	private int deleteExistingOpTeams(RoadOperationView roadOperationView, RequestContext context){

		List<TeamView> currentTeams = roadOperationView.getTeams();
		int deletedTeamsCnt = 0;

		try{
			System.out.println("currentTeams size before delete::"
					+ currentTeams.size());
			Iterator<TeamView> iterator = currentTeams.iterator();
			while (iterator.hasNext()) {
				TeamView teamView = (TeamView) iterator.next();
				System.err.println("Am i selected? " + teamView.getSelected()
						+ " --- " + teamView.getTeamName());

				if (teamView.getSelected().equalsIgnoreCase("true")) {
					System.out.println("teamview to delete::"
							+ teamView.getTeamName());

					// TODO when a team is deleted replaced the resource
					if (teamView.getTaStaffList() != null) {
						System.err.println("Add TA STaff Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfStaff().getSource()
						.addAll(teamView.getTaStaffList());
						System.err.println("roadOperationView.getListOfStaff().getSource()"+ roadOperationView.getListOfStaff().getSource().size());

					}
					if (teamView.getItaExaminerList() != null) {
						System.err.println("Add ITA Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfITA().getSource()
						.addAll(teamView.getItaExaminerList());

					}
					if (teamView.getPoliceOfficerList() != null) {
						System.err.println("Add Police Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfPolice().getSource()
						.addAll(teamView.getPoliceOfficerList());

					}
					if (teamView.getJpList() != null) {
						System.err.println("Add JP Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfJP().getSource()
						.addAll(teamView.getJpList());

					}
					if (teamView.getTaVehicleList() != null) {
						System.err.println("Add Vehicles Back to Pool");
						// add staff objects back to the pool of src staff
						roadOperationView.getListOfVehicles().getSource()
						.addAll(teamView.getTaVehicleList());

					}
					
					///added
					teamView.setDelete(true);	
					deletedTeamsCnt++;
					
				} 
			}

			context.getFlowScope().put("operation", roadOperationView);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("error Occurred in add_team");
			addErrorMessage(context, "SystemError");

		}
		return deletedTeamsCnt;
	}
	
	
	
	
	
	public static Comparator<StrategyBO> strategyComperator = new Comparator<StrategyBO>(){
		@Override
		public int compare(StrategyBO p1, StrategyBO p2)
		{
			if(p1.getStrategyDescription() == null  ){
				return 0;
			}
			
			if(p1.getStrategyDescription().equalsIgnoreCase(p2.getStrategyDescription())){
				return p1.getStrategyDescription().compareToIgnoreCase(p2.getStrategyDescription());
			}
			else{
				return p1.getStrategyDescription().compareToIgnoreCase(p2.getStrategyDescription());
			}
		}};
		
		
	public static Comparator<TAStaffBO> taStaffComperator = new Comparator<TAStaffBO>(){
		@Override
		public int compare(TAStaffBO p1, TAStaffBO p2)
		{
			if(p1.getFirstName() == null || p1.getLastName() == null || p2.getFirstName() == null || p2.getLastName() == null ){
				return 0;
			}
			
			if(p1.getLastName().equalsIgnoreCase(p2.getLastName())){
				return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
			}
			else{
				return p1.getLastName().compareToIgnoreCase(p2.getLastName());
			}
		}};
		
		
	public static Comparator<ITAExaminerBO> itaComperator = new Comparator<ITAExaminerBO>(){
		@Override
		public int compare(ITAExaminerBO p1, ITAExaminerBO p2)
		{
			if(p1.getPersonBO().getFirstName() == null || p1.getPersonBO().getLastName() == null || p2.getPersonBO().getFirstName() == null || p2.getPersonBO().getLastName() == null ){
				return 0;
			}
			
			if(p1.getPersonBO().getLastName().equalsIgnoreCase(p2.getPersonBO().getLastName())){
				return p1.getPersonBO().getFirstName().compareToIgnoreCase(p2.getPersonBO().getFirstName());
			}
			else{
				return p1.getPersonBO().getLastName().compareToIgnoreCase(p2.getPersonBO().getLastName());
			}
		}};
			
			
	public static Comparator<JPBO> jpComperator = new Comparator<JPBO>(){
		@Override
		public int compare(JPBO p1, JPBO p2)
		{
			if(p1.getPersonBO().getFirstName() == null || p1.getPersonBO().getLastName() == null || p2.getPersonBO().getFirstName() == null || p2.getPersonBO().getLastName() == null ){
				return 0;
			}
			
			if(p1.getPersonBO().getLastName().equalsIgnoreCase(p2.getPersonBO().getLastName())){
				return p1.getPersonBO().getFirstName().compareToIgnoreCase(p2.getPersonBO().getFirstName());
			}
			else{
				return p1.getPersonBO().getLastName().compareToIgnoreCase(p2.getPersonBO().getLastName());
			}
		}};
		
		
		public static Comparator<TAVehicleBO> vehicleComperator = new Comparator<TAVehicleBO>(){
			@Override
			public int compare(TAVehicleBO p1, TAVehicleBO p2)
			{
				if(p1.getVehicle().getModel() == null || p1.getVehicle().getModel() == null || p2.getVehicle().getTypeDesc() == null || p2.getVehicle().getTypeDesc() == null ){
					return 0;
				}
				
				if(p1.getVehicle().getTypeDesc().equalsIgnoreCase(p2.getVehicle().getTypeDesc())){
					return p1.getVehicle().getModel().compareToIgnoreCase(p2.getVehicle().getModel());
				}
				else{
					return p1.getVehicle().getTypeDesc().compareToIgnoreCase(p2.getVehicle().getTypeDesc());
				}
			}};
		
	public Event addTeam(RequestContext context) {
		try {

			System.err.println("init addTeam()");
			// Notes: get all details and create a team view
			// add this team view to the list of teams
			RoadOperationView roadOperationView = (RoadOperationView) context
					.getFlowScope().get("operation");
			
			//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(roadOperationView));

			List<TeamView> currentTeams = roadOperationView.getTeams();
			TeamView selectedTeam = new TeamView(roadOperationView.getCurrentTeam());
			List<TeamView> remainTeams = new ArrayList<TeamView>();

			TeamView teamView = new TeamView();
			String teamMode = (String) context.getFlowScope().get("teamMode");
			String stage = "add_team";
			
			boolean arterySelected = false;
			
			System.err.println("teamMode::" + teamMode);
			System.err.println("ADDING/UPDATING Team");
			// Validate first
			boolean validDetails = isValidOperationDetails(context,
					selectedTeam, stage);

			System.err.println("isValidOperationDetails::" + validDetails);

			if (!validDetails) {
				context.getFlowScope().put("closeTeamPopup", "N");
				// context.getFlowScope().put("operation", roadOperationView);
				return error();
			}

			context.getFlowScope().put("closeTeamPopup", "Y");

			if (roadOperationView.getStatusId() != null) {
				System.out.println("roadOperationView.StatusId::"
						+ roadOperationView.getStatusId());
			} else {
				System.out.println("StatusId is NULL");
			}

			/***
			 * SAVE LOCATION INFORMATION
			 */
			List<ArteryBO> teamArteries = new ArrayList<ArteryBO>();
			List<Integer> listofLocIDs = new ArrayList<Integer>();
			// Calculate Coverage
			Float tempCoverageSelectedArtery = 0.0F;
			Float tempCoverageNoSelectedArtery = 0.0F;

			// SAVE ARTERIES
			Map<Integer, List<ArteryBO>> teamArteryHash = selectedTeam
					.getHashMapOfTeamArteries();
			System.err.println("selectedTeam.getHashMapOfTeamArteries()::"
					+ selectedTeam.getHashMapOfTeamArteries().size());
			for (Map.Entry<Integer, List<ArteryBO>> eachListOfArtery : teamArteryHash
					.entrySet()) {

				if (eachListOfArtery != null) {
				//	System.err.println("inside teamArteryHash.entrySet");
					if (eachListOfArtery.getValue() != null) {
						//System.err.println("inside teamArteryHash.entrySet!=null");
						for (ArteryBO eachArtery : eachListOfArtery.getValue()) {
							if (eachArtery.isSelected()) {
								arterySelected = true;
								listofLocIDs.add(eachArtery.getLocationId());
								teamArteries.add(eachArtery);
								roadOperationView.getSelectedArteries().add(
										eachArtery);
							/*	tempCoverageSelectedArtery += eachArtery.getDistance();*/
								System.err.println("Artery Selected "
										+ eachArtery.getShortDescription() + "location ID" + listofLocIDs);
							}
						}
					}

				}

			}
			
			// SAVE LOCATIONS
			List<LocationBO> teamLocations = new ArrayList<LocationBO>();

			Map<String, List<LocationBO>> teamLocationHash = selectedTeam
					.getHashMapOfTeamLocations();
			
			Map<Integer, List<ArteryBO>> mapOfArteries = roadOperationView
					.getHashMapOfArteries();
			
			List<ArteryBO> availableArteries = new ArrayList<ArteryBO>();
			
			System.err.println("selectedTeam.getHashMapOfTeamLocations()::"
					+ selectedTeam.getHashMapOfTeamLocations().size());
			for (Map.Entry<String, List<LocationBO>> eachListOfLoc : teamLocationHash
					.entrySet()) {

				if (eachListOfLoc != null) {
					if (eachListOfLoc.getValue() != null) {
						for (LocationBO eachLoc : eachListOfLoc.getValue()) {
							if (eachLoc.isSelected()) {
								teamLocations.add(eachLoc);
								roadOperationView.getSelectedLocations().add(
										eachLoc);
								System.err.println("Location Selected "
										+ eachLoc.getShortDesc());
								//Checks if any arteries were selected
								
								if(listofLocIDs!=null){
									//System.err.println("No Artery Selected!!!" + arterySelected + " Arteries in List " + listofLocIDs + " Contain: " + listofLocIDs.contains(eachLoc.getLocationId()));
										if(!listofLocIDs.contains(eachLoc.getLocationId())){
											//gets arteries for selected location id
											System.out.println("No Artery Selected");
											availableArteries = mapOfArteries.get(eachLoc.getLocationId());
											if(availableArteries != null){
												System.out.println("Available artery not null");
												//Gets the total coverage for the selected location
											/*	for(ArteryBO artery: availableArteries){
														tempCoverageNoSelectedArtery += artery.getDistance();
												}*/
												//System.out.println("Artery Coverage for location " + tempCoverageNoSelectedArtery);
											}
										}
								
								}
							}
						}
					}
				}

			}
			
			
			//relocated code- kpowell-2010-10-29
			/*// Adding Coverage to TeamView
			if(tempCoverageSelectedArtery != null && tempCoverageSelectedArtery > 0.0){
				teamView.setCoverage(Float.parseFloat(String.format("%.1f", tempCoverageSelectedArtery + tempCoverageNoSelectedArtery)));
				System.out.println("Temp Coverage total with arteries" + tempCoverageSelectedArtery
					+ "  team View Total" + teamView.getCoverage() + "no artery coverage " + tempCoverageNoSelectedArtery);
			}else{
				teamView.setCoverage(Float.parseFloat(String.format("%.1f", tempCoverageSelectedArtery + tempCoverageNoSelectedArtery)));
				System.out.println("Temp Coverage total no arteries" + tempCoverageNoSelectedArtery
					+ "  teamVview " + teamView.getCoverage());
			}*/
			
			
			// SAVE PARISHES
			List<ParishBO> teamParishes = new ArrayList<ParishBO>();

			Map<String, ParishBO> teamParishHash = selectedTeam
					.getHashMapOfTeamParishes();
			System.err.println("selectedTeam.getHashMapOfTeamParishes()::"
					+ selectedTeam.getHashMapOfTeamParishes().size());
			if (teamParishHash != null) {
				for (Map.Entry<String, ParishBO> eachParish : teamParishHash
						.entrySet()) {

					if (eachParish.getValue().isSelected()) {
						teamParishes.add(eachParish.getValue());
						roadOperationView.getSelectedParishes().add(
								eachParish.getValue());
						roadOperationView.getHashSelectedParishes().put(
								eachParish.getKey(), eachParish.getValue());
						System.err.println("Parish Selected "
								+ eachParish.getValue());
					}
				}
			}

			/***
			 * SAVE RESOURCE INFORMATION
			 */

			/*************2014-10-27-REDO**********/
			// save team record
			teamView.setOperationLocationList(new ArrayList<LocationBO>());
			teamView.setOperationArteryList(new ArrayList<ArteryBO>());
			teamView.setOperationParishList(new ArrayList<ParishBO>());
			
			//teamView.getOperationLocationList().addAll(selectedTeam.getSelectedTeamLocationList());
			//teamView.getOperationArteryList().addAll(selectedTeam.getSelectedTeamArteryList());
			//teamView.getOperationParishList().addAll(selectedTeam.getSelectedTeamParishList());
			
			if(roadOperationView.getOperationLocationList()== null){
				roadOperationView.setOperationLocationList(new ArrayList<LocationBO>());
				roadOperationView.setOperationArteryList(new ArrayList<ArteryBO>());
				roadOperationView.setOperationParishList(new ArrayList<ParishBO>());
			}
			
			//tempCoverageNoSelectedArtery = 0.0F;
			tempCoverageSelectedArtery = 0.0F;
			Iterator<LocationBO> iterLoc = selectedTeam.getSelectedTeamLocationList().iterator();
			while(iterLoc.hasNext()){
				LocationBO locationIem = iterLoc.next();				
				if(!roadOperationView.getOperationLocationList().contains(locationIem)){
					roadOperationView.getOperationLocationList().add(locationIem);
					//System.out.println(locationIem.getLocationDescription()+" doesn't exist");
				}
				if(!teamView.getOperationLocationList().contains(locationIem)){
					teamView.getOperationLocationList().add(locationIem);
					//System.out.println(locationIem.getLocationDescription()+" doesn't exist");
				}
				
				/***TODO - to be revisited by rthompson
				 * if(selectedTeam.getSelectedTeamArteryList() == null){
					System.out.println("Available artery not null");
					//Gets the total coverage for the selected location
					for(ArteryBO artery: availableArteries){
							tempCoverageNoSelectedArtery += artery.getDistance();
					}
					//System.out.println("Artery Coverage for location " + tempCoverageNoSelectedArtery);
				}*/
				
			}
			
			Iterator<ArteryBO> iterArt = selectedTeam.getSelectedTeamArteryList().iterator();
			while(iterArt.hasNext()){
				ArteryBO arteryItem = iterArt.next();				
				if(!roadOperationView.getOperationArteryList().contains(arteryItem)){
					roadOperationView.getOperationArteryList().add(arteryItem);
					//System.out.println(arteryItem.getArteryDescription()+" doesn't exist");
				}
				if(!teamView.getOperationArteryList().contains(arteryItem)){
					teamView.getOperationArteryList().add(arteryItem);
					//System.out.println(arteryItem.getArteryDescription()+" doesn't exist");
					//map details
					tempCoverageSelectedArtery += arteryItem.getDistance();
				//	System.err.println("Selected arteries"+org.fsl.roms.view.ObjectUtils.objectToString(arteryItem));
				}
				
			}
			
			System.err.println("selectedTeam.getSelectedTeamParishList()"+ selectedTeam.getSelectedTeamParishList().size());
			Iterator<ParishBO> iterPar = selectedTeam.getSelectedTeamParishList().iterator();
			while(iterPar.hasNext()){
				ParishBO parishIem = iterPar.next();				
				if(!roadOperationView.getOperationParishList().contains(parishIem)){
					roadOperationView.getOperationParishList().add(parishIem);
					//System.out.println(parishIem.getDescription()+" doesn't exist inside OperationParishList");
				}
				if(!teamView.getOperationParishList().contains(parishIem)){
					teamView.getOperationParishList().add(parishIem);
					//System.out.println(parishIem.getDescription()+" doesn't exist");
				}
			}
			//roadOperationView.getOperationArteryList().addAll(selectedTeam.getSelectedTeamArteryList());
			//roadOperationView.getOperationParishList().addAll(selectedTeam.getSelectedTeamParishList());
			
			
			teamView.setSelectedTeamArteryList(selectedTeam.getSelectedTeamArteryList());
			teamView.setSelectedTeamLocationList(selectedTeam.getSelectedTeamLocationList());
			teamView.setSelectedTeamParishList(selectedTeam.getSelectedTeamParishList());

			System.err.println("setOperationParishList"
					+ teamView.getOperationParishList().size());
			System.err.println("setOperationLocationList"
					+ teamView.getOperationLocationList().size());
			System.err.println("setOperationArteryList" + teamView.getOperationArteryList().size());
					
			/*teamView.setOperationLocationList(teamLocations);
			teamView.setOperationArteryList(teamArteries);
			teamView.setOperationParishList(teamParishes);*/

			/*System.err.println("setOperationParishList"
					+ teamView.getOperationParishList().size());
			System.err.println("setOperationLocationList"
					+ teamLocations.size());
			System.err.println("setOperationArteryList" + teamArteries.size());
			
			
			teamView.setHashMapOfTeamArteries(teamArteryHash);
			teamView.setHashMapOfTeamLocations(teamLocationHash);
			teamView.setHashMapOfTeamParishes(teamParishHash);
			*
			*/
			
			

			//relocated- kpowell 2010-10-29
			// Adding Coverage to TeamView
			
				teamView.setCoverage(Float.parseFloat(String.format("%.1f", tempCoverageSelectedArtery)));
			

			/*****************************/	
			
			
			
			System.err
					.println("VehIDs" + selectedTeam.getListOfSelectedVehID());
			System.err.println("StaffIDs"
					+ selectedTeam.getListOfSelectedStaffID());
			System.err.println("listOfSelectedJPID"
					+ selectedTeam.getListOfSelectedJPID());
			System.err.println("listOfSelectedITAIDs"
					+ selectedTeam.getListOfSelectedITAID());
			System.err.println("listOfSelectedPoliceIDs"
					+ selectedTeam.getListOfSelectedPoliceID());
			// <h:inputText id="listOfSelectedJPID" style="display:none"
			// value="#{operation.currentTeam.listOfSelectedJPID}"></h:inputText>
			// <h:inputText id="listOfSelectedITAID" style="display:none"
			// value="#{operation.currentTeam.listOfSelectedITAID}"></h:inputText>
			// <h:inputText id="listOfSelectedPoliceID" style="display:none"
			// value="#{operation.currentTeam.listOfSelectedPoliceID}"></h:inputText>

			System.err.println("roadOperationView.getListOfStaff().target::"
					+ roadOperationView.getListOfStaff().getTarget().size());
			if (selectedTeam.getListOfSelectedStaffID() != null)// (roadOperationView.getListOfStaff()
																// != null)
			{
				teamView.setTaStaffList(getStaffForTeam(selectedTeam, context));
				teamView.setListOfSelectedStaffID(selectedTeam
						.getListOfSelectedStaffID());
				System.err.println("teamView.getTaStaffList().size()::"
						+ teamView.getTaStaffList().size());
				teamView.setNumOfTAStaff(teamView.getTaStaffList().size());
				
			}

			teamView.setNumOfITAExaminer(0);
			if (roadOperationView.getListOfITA() != null) {
				teamView.setItaExaminerList(getITAForTeam(selectedTeam, context));
				if (teamView.getItaExaminerList() != null) {
					teamView.setNumOfITAExaminer(teamView.getItaExaminerList()
							.size());
					teamView.setListOfSelectedITAID(selectedTeam
							.getListOfSelectedITAID());
					
					/*for (ITAExaminerBO test :  teamView.getItaExaminerList()) {
						System.err.println("ITA::"+ org.fsl.roms.view.ObjectUtils.objectToString(test));						
					}*/
					
				}

			}

			teamView.setNumOfJP(0);
			if (roadOperationView.getListOfJP() != null) {
				teamView.setJpList(getJPForTeam(selectedTeam, context));
				if (teamView.getJpList() != null) {
					teamView.setNumOfJP(teamView.getJpList().size());
					teamView.setListOfSelectedJPID(selectedTeam
							.getListOfSelectedJPID());
				//	System.err.println("JP::"+ org.fsl.roms.view.ObjectUtils.objectToString(teamView.getJpList()));
				}

			}

			teamView.setNumOfPolice(0);
			if (roadOperationView.getListOfPolice() != null) {
				teamView.setPoliceOfficerList(getPoliceForTeam(selectedTeam,
						context));
				if (teamView.getPoliceOfficerList() != null) {
					teamView.setNumOfPolice(teamView.getPoliceOfficerList()
							.size());
					teamView.setListOfSelectedPoliceID(selectedTeam
							.getListOfSelectedPoliceID());
					//System.err.println("Police::"+ org.fsl.roms.view.ObjectUtils.objectToString(teamView.getPoliceOfficerList()));
				}

			}

			teamView.setNumOfVehicle(0);
			if (roadOperationView.getListOfVehicles() != null) {
				teamView.setTaVehicleList(getVehForTeam(selectedTeam, context));
				if (teamView.getTaVehicleList() != null) {
					teamView.setNumOfVehicle(teamView.getTaVehicleList().size());
					teamView.setListOfSelectedVehID(selectedTeam
							.getListOfSelectedVehID());
				//	System.err.println("Vehicle::"+ org.fsl.roms.view.ObjectUtils.objectToString(teamView.getTaVehicleList()));
				}

			}

			teamView.setTeamIDNumber(selectedTeam.getTeamIDNumber());
			teamView.setTeamID(selectedTeam.getTeamID());
			teamView.setTeamName(selectedTeam.getTeamName());
			teamView.setNumOfMotorCycle((selectedTeam.getNumOfMotorCycle()== null)? 0 : selectedTeam.getNumOfMotorCycle());

			teamView.setTeamLead(selectedTeam.getTeamLead());
			// teamView.setTeamLead(this.getSelectedTeamLead());
			// teamView.setTeamLeadId(this.getSelectedTeamLead().getStaffId());

			if (currentTeams == null) {
				currentTeams = new ArrayList<TeamView>();
				System.err.println("No Teams Existed");
			}

			// assign a teamId based on the number of teams. This is just for
			// managing new teams added
			int numOfTeams = currentTeams.size();
			//int thisTeamID = numOfTeams + 1;
			int thisTeamID = roadOperationView.getTeamSerialId().intValue()+1;
			

			System.err.println("TEAM MODE " + teamMode);

			// teamView.setTeamID("NEW" + thisTeamID);
			// currentTeams.add(teamView);

			// TODO - Rework this
			if (teamMode.equalsIgnoreCase("ADD")) {// SAVE
				teamView.setTeamIDNumber(thisTeamID);
				teamView.setTeamID("NEW" + thisTeamID);
				currentTeams.add(teamView);
				System.err.println("The teamID " + teamView.getTeamID());
				
				roadOperationView.setTeamSerialId(new Integer(thisTeamID));
				//System.err.println("teams Serial = "+roadOperationView.getTeamSerialId());
			} else {// UPDATE

				System.err.println("currentTeams size" + currentTeams.size());

				Iterator<TeamView> iterator = currentTeams.iterator();
				while (iterator.hasNext()) {
					System.err.println("inside rebuild team");
					TeamView teamV = (TeamView) iterator.next();
					System.err.println("teamV ID::" + teamV.getTeamID());
					System.err.println("teamView::" + teamView.getTeamID());
					if (!teamV.getTeamID().equalsIgnoreCase(
							teamView.getTeamID())) {
						remainTeams.add(teamV);
						System.err.println("rebuild team list"
								+ teamV.getTeamID());
						// break;
					}

				}
				currentTeams.clear();

				if (remainTeams.size() > 0) {
					currentTeams.addAll(remainTeams);
					System.err.println("Add Remaining Teams");
				}
				/*
				 * for (TeamView teamV : currentTeams) {
				 * System.err.println("teamView.getTeamName()"+
				 * teamView.getTeamName());
				 * if(teamV.getTeamID().equals(teamView.getTeamID())) {
				 * currentTeams.remove(teamV);
				 * System.err.println("removed team"); break; } }
				 */
				System.err.println("The teamID " + teamView.getTeamID());

				currentTeams.add(teamView);

			}

			// only sort table if more than one tables exists in the listing of
			// teams
			// added by kpowell
			if (currentTeams.size() > 1) {
				Collections.sort(currentTeams);
				System.err.println("SOrted Table");
			}
			roadOperationView.setTeams(currentTeams);

			// OneSelectionTrackingListDataModel teamList = new
			// OneSelectionTrackingListDataModel(
			// roadOperationView.getTeams());
			List<TeamView> teamList = roadOperationView.getTeams();

			// teamDataMod = new TeamDataModel(roadOperationView.getTeams());

			context.getFlowScope().put("teamDatatable", teamList);
			
			this.clearPoliceFilter(context);

			context.getFlowScope().put("operation", roadOperationView);

			resetTeamPopup(context);
			// resetTeamPopupAfterExit(context);

			context.getFlowScope().put("currentStage", "resources");

			System.err.println("end addTeam");
			System.err.println("exit addTeam()");
			return success();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("error Occurred in add_team");
			System.err.println("exit addTeam()");
			addErrorMessage(context, "SystemError");
			return error();
		}

	}

	public List<TAVehicleBO> getVehForTeam(TeamView team, RequestContext context) {
		System.err.println("inside getVehForTeam");
		/****2014-10-28-kpowell***********/
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		if(roadOperationView.getListOfVehicles() != null){
			return roadOperationView.getListOfVehicles().getTarget();
		}
		else{
			return new ArrayList<TAVehicleBO>();
		}
		
		/*RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		List<TAVehicleBO> originalVeh = roadOperationView.getListOfVehAux();
		List<TAVehicleBO> selectedVeh = new ArrayList<TAVehicleBO>();
		String listOfVehInTarget = StringUtils.trimToNull(team
				.getListOfSelectedVehID());

		System.err.println("getVehForTeam");
		//System.err.println("roadOperationView.getListOfVehAux()"
		//		+ originalVeh.size());
		// System.err.println("originalVeh Size>>"+ originalVeh.size());

		if (listOfVehInTarget != null) {
			String[] splitListOfVehInTarget = listOfVehInTarget.split(",");

			if (originalVeh != null) {// no vehicles in the available list
				for (String string : splitListOfVehInTarget) {
					System.err.println("@@@@@@@@@ List of veh " + string);
					for (TAVehicleBO eachOrigVeh : originalVeh) {
						System.err.println("@@@@@@@@@ List of Oriog veh "
								+ eachOrigVeh.getTaVehicleId());
						if ((eachOrigVeh.getTaVehicleId())
								.equals((Integer) Integer.parseInt(string))) {
							selectedVeh.add(eachOrigVeh);
							System.err.println("Veh " + eachOrigVeh);
							break;
						}

					}

				}

			}
		}
		return selectedVeh;*/

	}

	public List<JPBO> getJPForTeam(TeamView team, RequestContext context) {
		System.err.println("inside getJPForTeam");
		/****2014-10-28-kpowell***********/
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		if(roadOperationView.getListOfJP() != null){
			return roadOperationView.getListOfJP().getTarget();
		}
		else{
			return new ArrayList<JPBO>();
		}
		/*RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		List<JPBO> originalJP = roadOperationView.getListOfJPAux();
	//	System.err.println("Size of Team Adding/Updating getListOfJPAux::"+ originalJP.size());
		List<JPBO> selectedJP = new ArrayList<JPBO>();
		String listOfJPInTarget = StringUtils.trimToNull(team
				.getListOfSelectedJPID());

		System.err.println("getJPForTeam");
		// System.err.println("roadOperationView.getListOfJPAux()"+
		//originalJP.size());
		//System.err.println("listOfJPInTarget"+listOfJPInTarget);
		if (listOfJPInTarget != null) {
			String[] splitListOfJPInTarget = listOfJPInTarget.split(",");

			if (originalJP != null) {// no JP in the available list
				for (String string : splitListOfJPInTarget) {
					System.err.println("getJPForTeamTArget::" + string);
					for (JPBO eachOrigJP : originalJP) {

			//				System.err.println("JP in Origin:"+eachOrigJP.getRegNumber());
						if (((eachOrigJP.getRegNumber()).trim()).equals(string)) {
							selectedJP.add(eachOrigJP);
			//				System.err.println("JP added::"+ eachOrigJP.getPersonBO().getFullName());
			//				System.err.println("JP in Origin Matched:"+eachOrigJP.getRegNumber());
							break;
						}

					}

				}
			}
		}
	//	System.err.println("Size of Team Adding/Updating selectedJP::"+ selectedJP.size());
		return selectedJP;*/
	}

	public List<ITAExaminerBO> getITAForTeam(TeamView team,
			RequestContext context) {
		
		System.err.println("inside getITAForTeam");
		
		/****2014-10-28-kpowell***********/
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		if(roadOperationView.getListOfITA() != null){
			return roadOperationView.getListOfITA().getTarget();
		}
		else{
			return new ArrayList<ITAExaminerBO>();
		}
		 
		/*RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		List<ITAExaminerBO> originalITA = roadOperationView.getListOfITAAux();
		List<ITAExaminerBO> selectedITA = new ArrayList<ITAExaminerBO>();
		String listOfITAInTarget = StringUtils.trimToNull(team
				.getListOfSelectedITAID());

		System.err.println("getITAForTeam");
		//System.err.println("roadOperationView.getListOfITAAux()"
		//+ originalITA.size());

		if (listOfITAInTarget != null) {
			System.err.println("STUFF FOR ITA" + listOfITAInTarget);
			String[] splitListOfITAInTarget = listOfITAInTarget.split(",");

			if (originalITA != null) {// no ITA in the available list
				for (String string : splitListOfITAInTarget) {
					System.err.println("getITAForTeamTArget::" + string);
					for (ITAExaminerBO eachOrigITA : originalITA) {

						if (((eachOrigITA.getIdNumber().toString()).trim()).equals(string)) {
							selectedITA.add(eachOrigITA);
							break;
						}

					}

				}
			}
		}
		return selectedITA;*/
	}

	public List<PoliceOfficerBO> getPoliceForTeam(TeamView team,
			RequestContext context) {
		
		System.err.println("inside getPoliceForTeam");
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		if(roadOperationView.getListOfPolice() != null){
			return roadOperationView.getListOfPolice().getTarget();
		}
		else{
			return new ArrayList<PoliceOfficerBO>();
		}
		 

		
//		List<PoliceOfficerBO> originalPol = roadOperationView
//				.getListOfPoliceAux();
//		List<PoliceOfficerBO> selectedPol = new ArrayList<PoliceOfficerBO>();
//		String listOfPolInTarget = StringUtils.trimToNull(team
//				.getListOfSelectedPoliceID());
//
//		System.err.println("getPoliceForTeam");
//		//System.err.println("roadOperationView.getListOfPoliceAux()"
//		//		+ originalPol.size());
//
//		if (listOfPolInTarget != null) {
//			System.err.println("STUFF FOR POLICE" + listOfPolInTarget);
//			String[] splitListOfPolInTarget = listOfPolInTarget.split(",");
//
//			if (originalPol != null) {// no police in the available list
//				for (String string : splitListOfPolInTarget) {
//					System.err.println("getPoliceForTeamTArget::" + string);
//					for (PoliceOfficerBO eachOrigPol : originalPol) {
//
//						if (((eachOrigPol.getPolOfficerCompNo() + "").trim())
//								.equals(string)) {
//							selectedPol.add(eachOrigPol);
//							System.out.println("added Police");
//							break;
//						}
//
//					}
//
//				}
//			}
//		}
//		return selectedPol;
	}

	public List<TAStaffBO> getStaffForTeam(TeamView team, RequestContext context) {
		System.err.println("inside getStaffForTeam");
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		if(roadOperationView.getListOfStaff() != null){
			return roadOperationView.getListOfStaff().getTarget();
		}
		else{
			return new ArrayList<TAStaffBO>();
		}
		/*RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		// getAvailableTaStaff(context);// sometimes the selected ta is missing
		// from the auxlist

		List<TAStaffBO> originalStaff = roadOperationView.getListOfStaffAux();
		List<TAStaffBO> selectedstaff = new ArrayList<TAStaffBO>();
		String listOfStaffInTarget = StringUtils.trimToNull(team
				.getListOfSelectedStaffID());

		System.err.println("getStaffForTeam");
		System.err.println("roadOperationView.getListOfStaffAux()"
				+ originalStaff.size());

		if (listOfStaffInTarget != null) {
			String[] splitListOfStaffInTarget = listOfStaffInTarget.split(",");

			if (originalStaff != null) {// no ta staff in the available list
				for (String string : splitListOfStaffInTarget) {

					System.err.println("StaffTarget::" + string);
					for (TAStaffBO eachOrigStaff : originalStaff) {

						System.err.println("eachOrigStaff.getStaffId()::"
								+ eachOrigStaff.getStaffId().trim());
						
						if (((eachOrigStaff.getStaffId()).trim())
								.equals(string)) {
							System.err.println("Matched STaff"
									+ eachOrigStaff.getStaffId());
							System.err.println("TASTAFF::"+ org.fsl.roms.view.ObjectUtils.objectToString(eachOrigStaff));
							selectedstaff.add(eachOrigStaff);
							break;
						}

					}

				}
			}
		}
		return selectedstaff;*/
	}

	/**
	 * @purpose Called when Add New Team is selected & after team is added
	 * @param context
	 */
	public void resetTeamPopup(RequestContext context) {

	try{
			
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		System.err.println("Clearing out current team - after adding");
		roadOperationView.setCurrentTeam(new TeamView());

		for (Map.Entry<String, ParishBO> eachParish : roadOperationView
				.getHashMapOfParishes().entrySet()) {
			// System.err.println("Inside 2nd parish loop");
			// if(eachParish.getKey().equalsIgnoreCase(par.getParishCode())){
			eachParish.getValue().setSelected(false);
			// System.err.println("eachParish.getValue() Selected::"+
			// eachParish.getValue().getDescription());
			// }
		}

		for (Map.Entry<String, List<LocationBO>> eachLocList : roadOperationView
				.getHashMapOfLocations().entrySet()) {
			// System.err.println("Inside 2nd loc loop");
			for (LocationBO eachLoc : eachLocList.getValue()) {
				// if(eachLoc.getLocationId().equals(loc.getLocationId())){
				eachLoc.setSelected(false);
				// System.err.println("eachLoc.getValue() Selected::"+
				// eachLoc.getLocationDescription());
				// }
			}
		}

		/*for (Map.Entry<Integer, List<ArteryBO>> eachArteryList : roadOperationView
				.getHashMapOfArteries().entrySet()) {
			// System.err.println("Inside 2nd artery loop");
			for (ArteryBO eachArtery : eachArteryList.getValue()) {
				// if(eachArtery.getArteryId().equals(artery.getArteryId())){
				eachArtery.setSelected(false);
				// System.err.println("artery.getValue() Selected::"+
				// eachArtery.getArteryDescription());
				// }
			}
		}*/
		List<ArteryBO> artList = new ArrayList<ArteryBO>();
		ArteryBO artRef = new ArteryBO();
		HashMap<Integer, List<ArteryBO>> hashOfAllArteries = new HashMap<Integer, List<ArteryBO>>();
		for (Map.Entry<Integer, List<ArteryBO>> eachArteryList : roadOperationView
				.getHashMapOfArteries().entrySet()) {
			// System.err.println("Inside 2nd artery loop");
			for (ArteryBO eachArtery : eachArteryList.getValue()) {
				// if(eachArtery.getArteryId().equals(artery.getArteryId())){
				eachArtery.setSelected(false);
				/*artRef.setArteryDescription(eachArtery.getArteryDescription());
				artRef.setArteryId(eachArtery.getArteryId());
				artRef.setLocationId(eachArtery.getLocationId());
				artRef.setParishCode(eachArtery.getParishCode());
				artRef.setLocationDescription(eachArtery.getLocationDescription());
				artRef.setArteryDescription(eachArtery.getArteryDescription());*/
				// System.err.println("artery.getValue() Selected::"+
				// eachArtery.getArteryDescription());
				// }
				copyFields(artRef,eachArtery);
				artList.add(artRef);
			}
			//System.err.println("eachArteryList.getKey()"+ eachArteryList.getKey()+ "size:"+artList.size());
			hashOfAllArteries.put(eachArteryList.getKey(), artList);
		}

		// repopulate with parishes from master list
		roadOperationView.getCurrentTeam().setHashMapOfTeamParishes(
				roadOperationView.getHashMapOfParishes());
		roadOperationView.getCurrentTeam().setHashMapOfTeamLocations(
				roadOperationView.getHashMapOfLocations());
		roadOperationView.getCurrentTeam().setHashMapOfTeamArteries(hashOfAllArteries);
				//roadOperationView.getHashMapOfArteries());

		roadOperationView.getCurrentTeam()
				.setCurrentTeamListOperationParishList(
						new ArrayList<ParishBO>());
		roadOperationView.getCurrentTeam().setCurrentTeamListLocationList(
				new ArrayList<LocationBO>());
		roadOperationView.getCurrentTeam()
				.setCurrentTeamListOperationArteryList(
						new ArrayList<ArteryBO>());

		roadOperationView.getCurrentTeam().setListOfSelectedITAID(null);
		roadOperationView.getCurrentTeam().setListOfSelectedJPID(null);
		roadOperationView.getCurrentTeam().setListOfSelectedPoliceID(null);
		roadOperationView.getCurrentTeam().setListOfSelectedStaffID(null);
		roadOperationView.getCurrentTeam().setListOfSelectedVehID(null);

		roadOperationView.getCurrentTeam().setSelectedTAStaff(
				new ArrayList<TAStaffBO>());

		if (roadOperationView.getListOfJP() != null)
			roadOperationView.getListOfJP().setSource(new ArrayList<JPBO>());

		if (roadOperationView.getListOfStaff() != null)
			roadOperationView.getListOfStaff().setTarget(
					new ArrayList<TAStaffBO>());
		if (roadOperationView.getListOfITA() != null)
			roadOperationView.getListOfITA().setTarget(
					new ArrayList<ITAExaminerBO>());
		if (roadOperationView.getListOfPolice() != null){
			roadOperationView.getListOfPolice().setTarget(
					new ArrayList<PoliceOfficerBO>());
			
			roadOperationView.getListOfPolice().setSource(new ArrayList<PoliceOfficerBO>());
		}
		if (roadOperationView.getListOfJP() != null)
			roadOperationView.getListOfJP().setTarget(new ArrayList<JPBO>());

		if (roadOperationView.getListOfVehicles() != null)
			roadOperationView.getListOfVehicles().setTarget(
					new ArrayList<TAVehicleBO>());

		roadOperationView.getCurrentTeam().setTeamLeadId(null);
		roadOperationView.getCurrentTeam().setNumOfMotorCycle(null);

		roadOperationView.getCurrentTeam().setTeamLead(new TAStaffBO());
		roadOperationView.setPossibleTeamLeads(new ArrayList<TAStaffBO>());
		roadOperationView.getCurrentTeam().setListOfSelectedStaffID(null);
		
		/**2014-10-27-REDO
		 * Updated Location/artery/parish - kpowell
		 */
		String parishCode = ((ParishBO)roadOperationView.getAllParishes().get(0)).getParishCode();
		Integer locId = ((LocationBO)roadOperationView.getAllLocationsMap().get(parishCode).get(0)).getLocationId();
		roadOperationView.getCurrentTeam().setParishCodeIndex(parishCode);
		roadOperationView.getCurrentTeam().setLocationCodeIndex(locId);
		roadOperationView.getCurrentTeam().setSelectedTeamArteryList(new ArrayList<ArteryBO>());
		roadOperationView.getCurrentTeam().setSelectedTeamLocationList(new ArrayList<LocationBO>());
		roadOperationView.getCurrentTeam().setSelectedTeamParishList(new ArrayList<ParishBO>());
		roadOperationView.setSelectedArteryList(new ArrayList<ArteryBO>());
		roadOperationView.setSelectedLocationList(new ArrayList<LocationBO>());
		roadOperationView.setSelectedParishList(new ArrayList<ParishBO>());
		/**
		 * 
		 */
//System.err.println("ResetTeam"+ org.fsl.roms.view.ObjectUtils.objectToString(roadOperationView.getCurrentTeam()));
		// context.getFlowScope().put("parishList", null);
		context.getFlowScope().put("possibleTeamLeads",
				new ArrayList<TAStaffBO>());
		context.getFlowScope().put("operation", roadOperationView);
		context.getFlowScope().put("currentStage", "add_team");
		context.getFlowScope().put("teamMode", "ADD");

		context.getFlowScope().put("locationList", null);
		context.getFlowScope().put("arteryList", null);
		
	}catch (Exception e) {
		// TODO: handle exception
		System.err.println("Error while resetting");
		e.printStackTrace();
	}

	}

	
	public void resetAvailableResourcesAfterCancel(RequestContext context){
		System.err.println("inside resetAvailableResourcesAfterCancel()");
		try{
			//org.primefaces.context.RequestContext.getCurrentInstance().reset("resourceDetails:teampanelID");
			RoadOperationView roadOperationView = (RoadOperationView) context
		
				.getFlowScope().get("operation");
		
		TeamView selectedTeam = new TeamView(roadOperationView.getCurrentTeam());
	//	org.primefaces.context.RequestContext.reset("form:teampanelID");
		
		if(selectedTeam.getTeamID()!=null){
			
			System.err.println("Team previously added::"+selectedTeam.getTeamID());
			roadOperationView.setSelectedTeam(new TeamView());
			roadOperationView.setCurrentTeam(new TeamView());
			context.getFlowScope().put("currentTeam", new TeamView());
			System.err.println("Team was previously saved");

			TeamView testObject = (TeamView)context.getFlowScope().get("selectedTeamTEST");
		//	System.err.println("testName"+testObject.getTeamName() );
		//	System.err.println("TEAM COPIED::"+org.fsl.roms.view.ObjectUtils.objectToString(testObject));
			
			//selectedTeam = new TeamView(testObject);
			
			//RESETTING the Picklist Source - 2014-11-21:kpowell
			//System.err.println("Staff ::Src"+ roadOperationView.getListOfStaff().getSource().size()+"  Tar::"+ roadOperationView.getListOfStaff().getTarget().size());
			boolean staffFound = false;
			if(roadOperationView.getListOfStaff()!= null){
				for(TAStaffBO staffTarget : roadOperationView.getListOfStaff().getTarget()){
					staffFound = false;
					for(TAStaffBO staffPerson : selectedTeam.getTaStaffList()){
						if(staffPerson.getPersonId() == staffTarget.getPersonId()){
							staffFound= true;
							break;
						}
					}
					
					if(staffFound == false){//if not found onteam add back to source
						roadOperationView.getListOfStaff().getSource().add(staffTarget);
						System.err.println("Staff Not on Team -"+ staffTarget.getFullName());
					}
				}
			}
			boolean itaFound = false;
			if(roadOperationView.getListOfITA()!= null){
				for(ITAExaminerBO itaTarget : roadOperationView.getListOfITA().getTarget()){
					itaFound = false;
					for(ITAExaminerBO itaPerson : selectedTeam.getItaExaminerList()){
						if(itaPerson.getPersonBO().getPersonId() == itaTarget.getPersonBO().getPersonId()){
							itaFound= true;
							break;
						}
					}
					
					if(itaFound == false){//if not found onteam add back to source
						roadOperationView.getListOfITA().getSource().add(itaTarget);
						System.err.println("ITA Not on Team-"+ itaTarget.getPersonBO().getFullName());
					}
				}
			}
			boolean vehFound = false;
			if(roadOperationView.getListOfVehicles()!= null){
				for(TAVehicleBO vehTarget : roadOperationView.getListOfVehicles().getTarget()){
					vehFound = false;
					for(TAVehicleBO vehicle : selectedTeam.getTaVehicleList()){
						if(vehicle.getVehicle().getVehicleId() == vehTarget.getVehicle().getVehicleId() ){
							vehFound= true;
							break;
						}
					}
					
					if(vehFound == false){//if not found onteam add back to source
						roadOperationView.getListOfVehicles().getSource().add(vehTarget);
						System.err.println("Vehicle Not on Team-"+ vehTarget.getVehicle().getPlateRegNo());
					}
				}
			}
			//TODO - JP Picklist
			if(roadOperationView.getListOfJP() != null && roadOperationView.getListOfJP().getTarget().size()>0){
				
				boolean jpFound = false;
				for(JPBO jpTarget : roadOperationView.getListOfJP().getTarget()){
					jpFound = false;
					for(JPBO jpPerson : selectedTeam.getJpList()){
						if(jpPerson.getPersonBO().getPersonId() == jpTarget.getPersonBO().getPersonId() ){
							jpFound= true;
							break;
						}
					}
					
					if(jpFound == false){//if not found onteam add back to source
						roadOperationView.getListOfJP().getSource().add(jpTarget);
						System.err.println("JP Not on Team-"+ jpTarget.getPersonBO().getFullName());
					}
				}
			}
			//
			
			
			TAStaffBO originalTeamLead = (TAStaffBO)context.getFlowScope().get("selectedTeamLead");
			if(originalTeamLead!= null){
				selectedTeam.setTeamLead(originalTeamLead);	
				selectedTeam.setTeamLeadId(originalTeamLead.getStaffId());
			//	selectedTeam.setTeamName(testObject.getTeamName());
				//roadOperationView.getCurrentTeam().setTeamLead(originalTeamLead.getTeamLead());
				//roadOperationView.getCurrentTeam().setTeamLeadId(originalTeamLead.getTeamLeadId());
				System.err.println("set team lead");
			}
			
			System.err.println("TeamLead::"+ selectedTeam.getTeamLead().getFullName());
			
			//System.err.println("Staff ::Src"+ roadOperationView.getListOfStaff().getSource().size()+"  Tar::"+ roadOperationView.getListOfStaff().getTarget().size());
			context.getFlowScope().put("operation", roadOperationView);
		}else{
			//when a team is Cancelled replaced the  resource		
			if(selectedTeam!=null && selectedTeam.getTeamID()== null){
				if(StringUtils.trimToNull(selectedTeam.getListOfSelectedVehID())!=null){
					System.err.println("Add Vehicles Back to Pool");
					//add staff objects back to the pool of src staff
					roadOperationView.getListOfVehicles().getSource().addAll(getVehForTeam(selectedTeam, context));
				}
				
				if(StringUtils.trimToNull(selectedTeam.getListOfSelectedStaffID())!=null){
					System.err.println("STaff Prev Size::"+ roadOperationView.getListOfStaff().getSource().size());
					System.err.println("Add TA STaff Back to Pool");
					//add staff objects back to the pool of src staff
					roadOperationView.getListOfStaff().getSource().addAll(getStaffForTeam(selectedTeam, context));
					System.err.println("STaff New Size::"+ roadOperationView.getListOfStaff().getSource().size());
				}
				
				if(StringUtils.trimToNull(selectedTeam.getListOfSelectedJPID())!=null){
					System.err.println("Add JP Back to Pool");
					//add staff objects back to the pool of src staff
					roadOperationView.getListOfJP().getSource().addAll(getJPForTeam(selectedTeam, context));
				}
				
				if(StringUtils.trimToNull(selectedTeam.getListOfSelectedITAID())!=null){
					System.err.println("Add ITA Back to Pool");
					//add staff objects back to the pool of src staff
					roadOperationView.getListOfITA().getSource().addAll(getITAForTeam(selectedTeam, context));
				}
				
				if(StringUtils.trimToNull(selectedTeam.getListOfSelectedPoliceID())!=null){
					System.err.println("Add Police Back to Pool");
					//add staff objects back to the pool of src staff
					roadOperationView.getListOfPolice().getSource().addAll(getPoliceForTeam(selectedTeam, context));
				}
				
				System.err.println("Team is a new team");
			}
			
			this.clearPoliceFilter(context);
			//roadOperationView.setCurrentTeam(selectedTeam);
			context.getFlowScope().put("operation", roadOperationView);
		}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.err.println("resetAvailableResourcesAfterCancel error");
			e.printStackTrace();
			addErrorMessage(RequestContextHolder.getRequestContext(),  "SystemError");
			
		}
		
	}
	
	/**
	 * @purpose Called on Close dialog an/or Cancel button
	 * @param context
	 */
	public void resetTeamPopupAfterExit(RequestContext context) {

		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		System.err.println("Clearing out current team - after exit");
		System.err.println("SelectedLoc::"
				+ roadOperationView.getHashSelectedLocations().size());
		// roadOperationView.setHashSelectedLocations(new HashMap<Integer,
		// LocationBO>());
		HashMap<Integer, LocationBO> toRemoveSelectLoc = new HashMap<Integer, LocationBO>();

		for (Map.Entry<Integer, LocationBO> selectedLocation : roadOperationView
				.getHashSelectedLocations().entrySet()) {// roadOperationView.getSelectedLocations()){

			System.err.println("selectedLocation::"
					+ selectedLocation.getValue().getLocationDescription());
			// roadOperationView.getHashSelectedLocations().remove(selectedLocation.getValue().getLocationId());//removed
			// by kpowell
			toRemoveSelectLoc.put(selectedLocation.getKey(),
					selectedLocation.getValue());// kpowell

			// update hash map for team to ensure proper saving of selected
			// values
			List<LocationBO> locations = roadOperationView.getCurrentTeam()
					.getHashMapOfTeamLocations()
					.get(selectedLocation.getValue().getParishCode());

			if (locations != null) {
				for (LocationBO locationBO : locations) {
					if (locationBO.getLocationId().equals(
							selectedLocation.getValue().getLocationId())) {
						locationBO.setSelected(false);
						break;
					}
				}
			}
			// roadOperationView.getCurrentTeam().getHashMapOfTeamLocations().put(selectedLocation.getParishCode(),
			// locations);
			roadOperationView.getHashMapOfLocations().put(
					selectedLocation.getValue().getParishCode(), locations);

			roadOperationView.getHashSelectedArteries().remove(
					selectedLocation.getValue().getLocationId());
			// unselect related arteries
			List<ArteryBO> arteries = roadOperationView.getCurrentTeam()
					.getHashMapOfTeamArteries()
					.get(selectedLocation.getValue().getLocationId());

			if (arteries != null) {
				for (ArteryBO arteryBO : arteries) {
					arteryBO.setSelected(false);
				}

			}

			// roadOperationView.getCurrentTeam().getHashMapOfTeamArteries().put(selectedLocation.getLocationId(),
			// arteries);
			roadOperationView.getHashMapOfArteries().put(
					selectedLocation.getValue().getLocationId(), arteries);
		}

		// kpowell
		for (Map.Entry<Integer, LocationBO> removLoc : toRemoveSelectLoc
				.entrySet()) {
			roadOperationView.getHashSelectedLocations().remove(removLoc);
			System.out.println("toRemoveSelectLoc "
					+ removLoc.getValue().getLocationDescription());
		}
		roadOperationView.setCurrentTeam(new TeamView());

		// repopulate with parishes from master list
		roadOperationView.getCurrentTeam().setHashMapOfTeamParishes(
				roadOperationView.getHashMapOfParishes());
		roadOperationView.getCurrentTeam().setHashMapOfTeamLocations(
				roadOperationView.getHashMapOfLocations());
		roadOperationView.getCurrentTeam().setHashMapOfTeamArteries(
				roadOperationView.getHashMapOfArteries());

		roadOperationView.getCurrentTeam()
				.setCurrentTeamListOperationParishList(
						new ArrayList<ParishBO>());
		roadOperationView.getCurrentTeam().setCurrentTeamListLocationList(
				new ArrayList<LocationBO>());
		roadOperationView.getCurrentTeam()
				.setCurrentTeamListOperationArteryList(
						new ArrayList<ArteryBO>());

		System.out.println("roadOperationView.getAvailableStaff()::"
				+ roadOperationView.getAvailableStaff().size());
		System.out.println("roadOperationView.getAvailableStaff()::"
				+ roadOperationView.getAvailableStaff().size());
		System.out.println("roadOperationView.getAvailablePolice()::"
				+ roadOperationView.getAvailablePolice().size());

		if (roadOperationView.getListOfStaff() != null)
			roadOperationView.getListOfStaff().setSource(
					getResidualStaff(roadOperationView.getAvailableStaff()));
		if (roadOperationView.getListOfITA() != null)
			roadOperationView.getListOfITA().setSource(
					getResidualITA(roadOperationView.getAvailableITA()));
		if (roadOperationView.getListOfPolice() != null)
			roadOperationView.getListOfPolice().setSource(
					getResidualPolice(roadOperationView.getAvailablePolice()));
		System.out.println("Police.Source()::"
				+ roadOperationView.getListOfPolice().getSource().size());

		if (roadOperationView.getListOfJP() != null)
			roadOperationView.getListOfJP().setSource(new ArrayList<JPBO>());
		if (roadOperationView.getListOfVehicles() != null)
			roadOperationView.getListOfVehicles().setSource(
					getResidualVeh(roadOperationView.getAvailableVehicles()));

		if (roadOperationView.getListOfStaff() != null)
			roadOperationView.getListOfStaff().setTarget(
					new ArrayList<TAStaffBO>());
		if (roadOperationView.getListOfITA() != null)
			roadOperationView.getListOfITA().setTarget(
					new ArrayList<ITAExaminerBO>());
		if (roadOperationView.getListOfPolice() != null)
			roadOperationView.getListOfPolice().setTarget(
					new ArrayList<PoliceOfficerBO>());
		if (roadOperationView.getListOfJP() != null)
			roadOperationView.getListOfJP().setTarget(new ArrayList<JPBO>());
		if (roadOperationView.getListOfVehicles() != null)
			roadOperationView.getListOfVehicles().setTarget(
					new ArrayList<TAVehicleBO>());

		context.getFlowScope().put("possibleTeamLeads",
				new ArrayList<TAStaffBO>());
		context.getFlowScope().put("operation", roadOperationView);
		context.getFlowScope().put("currentStage", "resources");
		context.getFlowScope().put("teamMode", "ADD");

		context.getFlowScope().put("locationList", null);
		context.getFlowScope().put("arteryList", null);

	}

	private List<TAVehicleBO> getResidualVeh(List<TAVehicleBO> availableVehicles) {
		// TODO Auto-generated method stub

		List<TeamView> currentTeams = (List<TeamView>) RequestContextHolder
				.getRequestContext().getFlowScope().get("teamDatatable"); // roadOpView.getTeams();
		List<TAVehicleBO> selectedVeh = new ArrayList<TAVehicleBO>();

		/** added by kpowell **/
		List<TAVehicleBO> resetVehSrc = new ArrayList<TAVehicleBO>();
		boolean addVeh;
		/**/

		if (currentTeams != null) {
			for (TeamView teamView : currentTeams) {

				selectedVeh = teamView.getTaVehicleList();

				if (selectedVeh != null && selectedVeh.size() > 0) {
					/*
					 * for (TAVehicleBO taVehicleBO : selectedVeh) {
					 * 
					 * if(availableVehicles.contains(taVehicleBO)) {
					 * availableVehicles.remove(taVehicleBO); } }
					 */

					for (TAVehicleBO availVeh : availableVehicles) {
						System.out.println("Inside first loop statements");
						addVeh = true;
						for (TAVehicleBO selVeh : selectedVeh) {
							System.out.println("Inside 2nd loop statements");
							if (availVeh
									.getVehicle()
									.getPlateRegNo()
									.equals(selVeh.getVehicle().getPlateRegNo())) {
								System.out
										.println("Inside if statement availVeh.getVehicle().getPlateRegNo().equals(selVeh.getVehicle().getPlateRegNo()::"
												+ selVeh.getVehicle()
														.getPlateRegNo());
								addVeh = false;
								break;

							}
						}

						if (addVeh == true) {
							resetVehSrc.add(availVeh);
							System.out.println("Add remaindersource Vehicle"
									+ availVeh.getTaVehicleId()
									+ availVeh.getVehicle().getChassisNo());
						}
					}

					availableVehicles.clear();
					System.out.println("Cleared availableVehicles");
					availableVehicles.addAll(resetVehSrc);
					resetVehSrc.clear();
				}
			}
		}
		return availableVehicles;
	}

	private List<JPBO> getResidualJP(List<JPBO> availableJP) {
		// TODO Auto-generated method stub
		
		try{
		System.out.println("Inside getResidualJP()::");
		List<TeamView> currentTeams = (List<TeamView>) RequestContextHolder
				.getRequestContext().getFlowScope().get("teamDatatable"); // roadOpView.getTeams();
		List<JPBO> selectedJP = new ArrayList<JPBO>();
		// System.out.println("currentTeams ="+ currentTeams.size());
		List<Integer> jpIds = new ArrayList<Integer>();

		/** added by kpowell **/
		List<JPBO> resetJPSrc = new ArrayList<JPBO>();
		boolean addJp = true;
		/**/
		
		List<String> teamIds = new ArrayList<String>();	
		RoadOperationView roadOperationView = (RoadOperationView)RequestContextHolder.getRequestContext().getFlowScope().get("operation");
		System.err.println("Full Available JP list"+ availableJP.size() );
		//1: remove those in target not in full available list of JP
		if(roadOperationView.getListOfJP()!= null ){
			System.err.println("roadOperationView.getListOfJP().getTarget().size()"+roadOperationView.getListOfJP().getTarget().size());
			if(roadOperationView.getListOfJP().getTarget().size()>0){	
		
			for(JPBO availJP : availableJP){
				addJp = true;
				for(JPBO targetJP : roadOperationView.getListOfJP().getTarget()){					
					if(targetJP.getRegNumber().equalsIgnoreCase(availJP.getRegNumber())){
						addJp = false;
						break;
					}
					
					
				}
				
				if (addJp == true) {
					resetJPSrc.add(availJP);
					System.out.println("Add remaindersource JP"
							+ availJP.getRegNumber()
							+ availJP.getPersonBO().getFullName());
				}
				
				
			}
			availableJP.clear();
			System.out.println("Cleared availableJP");
			availableJP.addAll(resetJPSrc);
			resetJPSrc.clear();
			
			System.err.println("1: size after removing those in target and not in full list"+ availableJP.size() );
		}
		}
		
		addJp = true;//reset for another verification
		//2: remove those already assigned to a team
		if (currentTeams != null) {
			for (TeamView teamView : currentTeams) {

				selectedJP = teamView.getJpList();
				teamIds.add(teamView.getTeamID());

				if (selectedJP != null && selectedJP.size() > 0) {
					System.out.println("selectedJP =" + selectedJP.size());
					/*
					 * for (JPBO jpbo : selectedJP) {
					 * 
					 * if(availableJP.contains(jpbo)) {
					 * availableJP.remove(jpbo);
					 * System.out.println("removeJP ="+
					 * jpbo.getPersonBO().getFullName()); }
					 * 
					 * }
					 */

					for (JPBO availJP : availableJP) {
						System.out.println("Inside first loop statements");
						addJp = true;
						for (JPBO selJP : selectedJP) {
							System.out.println("Inside 2nd loop statements");
							if (availJP.getRegNumber().equals(
									selJP.getRegNumber())) {
								System.out
										.println("Inside if statement availJP.getRegNumber().equals(selJP.getRegNumber())::"
												+ selJP.getRegNumber());
								addJp = false;
								break;

							}
						}

						if (addJp == true) {
							resetJPSrc.add(availJP);
							System.out.println("Add remaindersource JP"
									+ availJP.getRegNumber()
									+ availJP.getPersonBO().getFullName());
						}
					}

					availableJP.clear();
					System.out.println("Cleared availableJP");
					availableJP.addAll(resetJPSrc);
					resetJPSrc.clear();
				}

			}
			
			System.err.println("2: size after removing those added on previous team"+ availableJP.size() );
		}
		
		addJp = true;//reset for another verification
		//3:remove selectedJP on current team
		//and currentteam.teamid not in list of previously added team
		if((getJPForTeam(roadOperationView.getCurrentTeam(),RequestContextHolder.getRequestContext())!= null) 
				&& (!teamIds.contains(roadOperationView.getCurrentTeam().getTeamID()))
				&& roadOperationView.getListOfJP()!= null){
			
			//get it from current team
				//System.err.println("inside 2nd if ");

				selectedJP = roadOperationView.getListOfJP().getTarget();

				if (selectedJP != null && selectedJP.size() > 0) {
				//	System.err.println("selectedJP =" + selectedJP.size());

					for (JPBO availJP : availableJP) {
						System.err.println("Inside first loop statements");
						addJp = true;
						for (JPBO selJP : selectedJP) {
							System.err.println("Inside 2nd loop statements");
							if (availJP.getRegNumber().equals(
									selJP.getRegNumber())) {
								System.err
										.println("Inside if statement availJP.getRegNumber().equals(selJP.getRegNumber())::"
												+ selJP.getRegNumber());
								addJp = false;
								break;

							}
						}

						if (addJp == true) {
							resetJPSrc.add(availJP);
							System.err.println("Add remaindersource JP"
									+ availJP.getRegNumber()
									+ availJP.getPersonBO().getFullName());
						}
					}

					availableJP.clear();
					System.err.println("Cleared availableJP");
					availableJP.addAll(resetJPSrc);
					resetJPSrc.clear();
				
				}
			
		}

		System.err.println("return resetJPSrc =" + resetJPSrc.size());
		System.err.println("return availableJP =" + availableJP.size());
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return availableJP;
	}

	private List<PoliceOfficerBO> getResidualPolice(
			List<PoliceOfficerBO> availablePolice) {
		// TODO Auto-generated method stub
		List<TeamView> currentTeams = (List<TeamView>) RequestContextHolder
				.getRequestContext().getFlowScope().get("teamDatatable"); // roadOpView.getTeams();
		List<PoliceOfficerBO> selectedPolice = new ArrayList<PoliceOfficerBO>();

		/** added by kpowell **/
		List<PoliceOfficerBO> resetPolSrc = new ArrayList<PoliceOfficerBO>();
		boolean addPol;

		if (currentTeams != null) {
			for (TeamView teamView : currentTeams) {

				selectedPolice = teamView.getPoliceOfficerList();
				System.out.println("selectedTeam::" + teamView.getTeamName());

				if (selectedPolice != null && selectedPolice.size() > 0) {
					System.out.println("selectedPolice::"
							+ selectedPolice.size());
					/*
					 * for (PoliceOfficerBO policeOfficerBO : selectedPolice) {
					 * 
					 * if(availablePolice.contains(policeOfficerBO)) {
					 * availablePolice.remove(policeOfficerBO); } }
					 */
					System.out.println("availablePolice at the time is ::"
							+ availablePolice.size());
					for (PoliceOfficerBO availPol : availablePolice) {
						// System.out.println("Inside first loop statements");
						addPol = true;

						for (PoliceOfficerBO selPol : selectedPolice) {
							// System.out.println("Inside 2nd loop statements");
							if (availPol.getPolOfficerCompNo().equals(
									selPol.getPolOfficerCompNo())) {
								// System.out.println("Inside if statement availPol.getPolOfficerCompNo().equals(selPol.getPolOfficerCompNo() ::"+selPol.getPolOfficerCompNo());
								addPol = false;
								break;

							}
						}

						if (addPol == true) {
							resetPolSrc.add(availPol);
							// System.out.println(
							// "Add remaindersource Police"+availPol.getPolOfficerCompNo()+availPol.getFullName());
						} else {

							System.out.println("Police Not added:"
									+ +availPol.getPolOfficerCompNo()
									+ availPol.getFullName());
						}
					}

					availablePolice.clear();
					System.out.println("Cleared availablePolice");
					availablePolice.addAll(resetPolSrc);
					System.out.println("availablePolice after reinitia::"
							+ availablePolice.size());
					resetPolSrc.clear();

				}
			}
		}

		if (availablePolice != null)
			System.out.println("Size Police::" + availablePolice.size());
		return availablePolice;
	}

	private List<ITAExaminerBO> getResidualITA(List<ITAExaminerBO> availableITA) {
		// TODO Auto-generated method stub
		List<TeamView> currentTeams = (List<TeamView>) RequestContextHolder
				.getRequestContext().getFlowScope().get("teamDatatable"); // roadOpView.getTeams();
		List<ITAExaminerBO> selectedITA = new ArrayList<ITAExaminerBO>();

		/** added by kpowell **/
		List<ITAExaminerBO> resetITASrc = new ArrayList<ITAExaminerBO>();
		boolean addITA;

		if (currentTeams != null) {
			for (TeamView teamView : currentTeams) {

				selectedITA = teamView.getItaExaminerList();

				if (selectedITA != null && selectedITA.size() > 0) {
					/*
					 * for (ITAExaminerBO itaExaminerBO : selectedITA) {
					 * 
					 * if(availableITA.contains(itaExaminerBO)) {
					 * availableITA.remove(itaExaminerBO); } }
					 */

					for (ITAExaminerBO availITA : availableITA) {
						System.out.println("Inside first loop statements");
						addITA = true;
						for (ITAExaminerBO selITA : selectedITA) {
							System.out.println("Inside 2nd loop statements");
							if (availITA.getPersonBO().getPersonId()
									.equals(selITA.getPersonBO().getPersonId())) {
								System.out
										.println("Inside if statement availITA.getPersonBO().getPersonId().equals(selITA.getPersonBO().getPersonId() ::"
												+ selITA.getPersonBO()
														.getPersonId());
								addITA = false;
								break;

							}
						}

						if (addITA == true) {
							resetITASrc.add(availITA);
							System.out.println("Add remaindersource ITA"
									+ availITA.getIdNumber()
									+ availITA.getPersonBO().getFullName());
						}
					}

					availableITA.clear();
					System.out.println("Cleared availableITA");
					availableITA.addAll(resetITASrc);
					resetITASrc.clear();
				}
			}
		}
		return availableITA;

	}

	private List<TAStaffBO> getResidualStaff(List<TAStaffBO> availableStaff) {
		// TODO Auto-generated method stub
		List<TeamView> currentTeams = (List<TeamView>) RequestContextHolder
				.getRequestContext().getFlowScope().get("teamDatatable"); // roadOpView.getTeams();
		List<TAStaffBO> selectedStaff = new ArrayList<TAStaffBO>();

		System.err.println("getResidualStaff()");
		/** added by kpowell **/
		List<TAStaffBO> resetStaffSrc = new ArrayList<TAStaffBO>();
		boolean addTA;
		List<TAStaffBO> finalSTaffSrc = new ArrayList<TAStaffBO>();

		finalSTaffSrc.addAll(availableStaff);
		/**/

		System.err.println("availableStaff" + finalSTaffSrc.size());

		if (currentTeams != null) {
			for (TeamView teamView : currentTeams) {

				selectedStaff = teamView.getTaStaffList();
				System.out.println("selectedTeam::" + teamView.getTeamName());
				if (selectedStaff != null && selectedStaff.size() > 0) { /*
																		 * for (
																		 * TAStaffBO
																		 * taStaffBO
																		 * :
																		 * selectedStaff
																		 * ) {
																		 * System
																		 * .err.
																		 * println
																		 * (
																		 * " %%%%% checking for staff  "
																		 * +
																		 * taStaffBO
																		 * .
																		 * getFullName
																		 * ());
																		 * 
																		 * for(
																		 * TAStaffBO
																		 * eachAvailableStaff
																		 * :
																		 * availableStaff
																		 * ) {
																		 * if(
																		 * eachAvailableStaff
																		 * .
																		 * getStaffId
																		 * (
																		 * ).equals
																		 * (
																		 * taStaffBO
																		 * .
																		 * getStaffId
																		 * ()))
																		 * {
																		 * System
																		 * .err.
																		 * println
																		 * (
																		 * "???? contains checking for staff  "
																		 * +
																		 * taStaffBO
																		 * .
																		 * getFullName
																		 * ());
																		 * availableStaff
																		 * .
																		 * remove
																		 * (
																		 * eachAvailableStaff
																		 * );
																		 * break
																		 * ; } }
																		 * }
																		 */

					for (TAStaffBO availTA : finalSTaffSrc) {

						addTA = true;
						for (TAStaffBO selTA : selectedStaff) {

							if (availTA.getStaffId().equals(selTA.getStaffId())) {
								System.out
										.println("Inside if statement availTA.getStaffId().equals(selTA.getStaffId()) ::"
												+ selTA.getStaffId());
								addTA = false;
								break;

							}
						}

						if (addTA == true) {
							resetStaffSrc.add(availTA);
							System.out.println("Add remaindersource TA"
									+ availTA.getStaffId()
									+ availTA.getFullName());
						}
					}

					finalSTaffSrc.clear();
					System.out.println("Cleared availableStaff");
					finalSTaffSrc.addAll(resetStaffSrc);
					resetStaffSrc.clear();

				}
			}
		}

		return finalSTaffSrc;

	}

	public Event viewOneTeam(RequestContext context)
	// public void viewOneTeam(SelectEvent event)
	{
		// RequestContext context = RequestContextHolder.getRequestContext();
System.err.println("init viewOneTeam()");
		try {

			RoadOperationView roadOperationView = (RoadOperationView) context
					.getFlowScope().get("operation");

			/*RoadOperationView roadOpPrev =  new RoadOperationView();
			copyFields(roadOpPrev, roadOperationView);
			//org.fsl.roms.view.ObjectUtils.copyProperties(roadOperationView, roadOpPrev);
			context.getFlowScope().put("prevOperation", roadOpPrev);*/
			
			TeamView selectedTeam = new TeamView(roadOperationView.getSelectedTeam());
			roadOperationView.setCurrentTeam(new TeamView());

			/*System.err.println("selected " + org.fsl.roms.view.ObjectUtils.objectToString(selectedTeam));

			for(ParishBO par : selectedTeam.getSelectedTeamParishList() ){
				System.err.println("PARISH-"+org.fsl.roms.view.ObjectUtils.objectToString(par));
			}
			for(LocationBO loc : selectedTeam.getSelectedTeamLocationList() ){
				System.err.println("LOCATION-"+org.fsl.roms.view.ObjectUtils.objectToString(loc));
			}
			for(ArteryBO ar : selectedTeam.getSelectedTeamArteryList() ){
				System.err.println("ARTERY-"+org.fsl.roms.view.ObjectUtils.objectToString(ar));
			}*/
			// populate duallistmodel of selected values
			if (roadOperationView.getListOfStaff() != null) {
				roadOperationView.getListOfStaff().setTarget(
						selectedTeam.getTaStaffList());
				
				/*for(int i = 0; i<selectedTeam.getTaStaffList().size(); i++){
					System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(selectedTeam.getTaStaffList().get(i)));
				}*/
				context.getFlowScope().put("possibleTeamLeads",
						selectedTeam.getTaStaffList());
			} else {
				System.err
						.println("roadOperationView.getListOfStaff() == null");
			}

			if (roadOperationView.getListOfJP() != null) {
				if (selectedTeam.getJpList() != null) {
					roadOperationView.getListOfJP().setTarget(
							selectedTeam.getJpList());
					// System.out.println("size of getListOfJP().target ="+roadOperationView.getListOfJP().getTarget().size());
					
					//set sleected list
					
					//System.err.println("roadOperationView.setListOfSelectedJPID()::"+ 
					//		org.springframework.util.StringUtils.collectionToDelimitedString(selectedTeam.getJpList(), ",") );
					roadOperationView.getCurrentTeam().setListOfSelectedJPID(org.springframework.util.StringUtils.collectionToDelimitedString(selectedTeam.getJpList(), ",") );
					
				} else {
					roadOperationView.getListOfJP().setTarget(
							new ArrayList<JPBO>());
				}
			}

			// /////////////
			AvailableResourceCriteriaBO availResourceCriteria = populateAvailResourceCriteriaBO(
					roadOperationView, "VIEW");

			System.err.println("available resource stuff "
					+ availResourceCriteria.getParishCodeList());
			List<JPBO> availableJP = new ArrayList<JPBO>();
			List<JPBO> residualJP = new ArrayList<JPBO>();

			try {
				availableJP = getRoadOperationService()
						.getAvailableJPResource(availResourceCriteria);

				if(roadOperationView.getListOfJPAux()==null){
					roadOperationView.setListOfJPAux(availableJP);
				}
				
				System.err
						.println("size of availableJP =" + availableJP.size());
				//availableJP = getResidualJP(availableJP);

/*				if(selectedTeam.getJpList()!= null){
					residualJP.addAll(selectedTeam.getJpList());
					System.err.println("residualJP szie:" +residualJP.size());
				}*/
				// remove those previously selected for a team
				residualJP.addAll(availableJP);
				residualJP = getResidualJP(residualJP);
				//residualJP.addAll(availableJP);
				

				System.err.println("size of getResidualJP ="
						+ residualJP.size());
				System.err
				.println("size of availableJP =" + availableJP.size());


			} catch (Exception e) {
				e.printStackTrace();
			}

			if (roadOperationView.getListOfJP() != null) {
				roadOperationView.getListOfJP().setSource(residualJP);
				//System.err.println("size of getResidualJP ="
				//		+ residualJP.size());
			}
			// //////////

			if (roadOperationView.getListOfPolice() != null) {
				roadOperationView.getListOfPolice().setTarget(
						selectedTeam.getPoliceOfficerList());
			}

			if (roadOperationView.getListOfVehicles() != null) {
				roadOperationView.getListOfVehicles().setTarget(
						selectedTeam.getTaVehicleList());
			}

			if (roadOperationView.getListOfITA() != null) {
				roadOperationView.getListOfITA().setTarget(
						selectedTeam.getItaExaminerList());
			}

			System.err.println("Here setting up details of selected team");

			// Populate selected parishes based on team HashMap
			List<ParishBO> parishesOfSelectedTeam = selectedTeam
					.getOperationParishList();

			System.err.println("Team lead info "
					+ selectedTeam.getTeamLead().getStaffId() + " --- "
					+ selectedTeam.getTeamLead().getFullName());
			selectedTeam.setTeamLeadId(selectedTeam.getTeamLead().getStaffId());
			selectedTeam.setTeamLead(selectedTeam.getTeamLead());
			
			//keep a copy of the teams lead to facilitate Cancelling on the add team popup
			context.getFlowScope().put("selectedTeamLead", selectedTeam.getTeamLead());
			
			TeamView testObject = new TeamView(selectedTeam);
			//testObject.setTeamName(selectedTeam.getTeamName());
			context.getFlowScope().put("selectedTeamTEST", testObject);

			// Set list of selected locations based on existing hashmap
			String fromWhere = (String) context.getFlowScope().get("fromWhere");
			System.err.println("FROM WHERE " + fromWhere);

			/*if (selectedTeam.getCurrentTeamListOperationParishList() != null) {
				selectedTeam.getCurrentTeamListOperationParishList().clear();
				System.err
						.println("Cleared getCurrentTeamListOperationParishList");
			}
			if (selectedTeam.getCurrentTeamListOperationArteryList() != null) {
				selectedTeam.getCurrentTeamListOperationArteryList().clear();
				System.err
						.println("Cleared getCurrentTeamListOperationArteryList");
			}

			if (selectedTeam.getCurrentTeamListLocationList() != null) {
				selectedTeam.getCurrentTeamListLocationList().clear();
				System.err.println("Cleared setCurrentTeamListLocationList");
			}*/

			
			/**
			 * 2014-10-27-REDO
			 */
			
			System.err.println("selectedTeam.getSelectedTeamLocationList()"+ selectedTeam.getSelectedTeamLocationList().size());
			roadOperationView.setSelectedArteryList(selectedTeam.getSelectedTeamArteryList());
			roadOperationView.setSelectedLocationList(selectedTeam.getSelectedTeamLocationList());
			roadOperationView.setSelectedParishList(selectedTeam.getSelectedTeamParishList());
			roadOperationView.getCurrentTeam().setSelectedTeamArteryList(selectedTeam.getSelectedTeamArteryList());
			roadOperationView.getCurrentTeam().setSelectedTeamLocationList(selectedTeam.getSelectedTeamLocationList());
			roadOperationView.getCurrentTeam().setSelectedTeamParishList(selectedTeam.getSelectedTeamParishList());
			
			System.out.println("Existing team list ::"+ selectedTeam.getSelectedTeamParishList().size());
			selectedTeam.setParishCodeIndex(selectedTeam.getSelectedTeamParishList().get(0).getParishCode());
			roadOperationView.getCurrentTeam().setParishCodeIndex(selectedTeam.getParishCodeIndex());
			
			String firtParishSelected = selectedTeam.getSelectedTeamParishList().get(0).getParishCode();
			List<LocationBO> teamLocations = roadOperationView.getAllLocationsMap().get(firtParishSelected);
			for( LocationBO locationItem : teamLocations){
				if(selectedTeam.getSelectedTeamLocationList().size()>0){
				if(selectedTeam.getSelectedTeamLocationList().get(0).getLocationId().equals(locationItem.getLocationId())){
					//selectedTeam.setLocationCodeIndex(teamLocations.get(0).getLocationId());
					selectedTeam.setLocationCodeIndex(locationItem.getLocationId());
					break;
				}
				}
			}
			//System.err.println("First Location for team"+ org.fsl.roms.view.ObjectUtils.objectToString(teamLocations.get(0)));
			roadOperationView.getCurrentTeam().setLocationCodeIndex(selectedTeam.getLocationCodeIndex());
			
			
			selectedTeam.setOperationParishList(selectedTeam.getSelectedTeamParishList());
			selectedTeam.setOperationLocationList(selectedTeam.getSelectedTeamLocationList());
			/**
			 * 
			 */
			
			selectedTeam.setCurrentTeamListOperationParishList(selectedTeam
					.getOperationParishList());
			selectedTeam.setCurrentTeamListLocationList(selectedTeam
					.getOperationLocationList());
			selectedTeam.setCurrentTeamListOperationArteryList(selectedTeam
					.getOperationArteryList());

			context.getFlowScope().put("currentOperationParishList",
					selectedTeam.getOperationParishList());
			context.getFlowScope().put("currentoperationLocationList",
					selectedTeam.getOperationLocationList());
			context.getFlowScope().put("currentOperationArteryList",
					selectedTeam.getOperationArteryList());

			System.err.println("setCurrentTeamListOperationParishList"
					+ selectedTeam.getOperationParishList().size());
			System.err.println("setCurrentTeamListLocationList"
					+ selectedTeam.getOperationLocationList().size());
			System.err.println("setCurrentTeamListOperationArteryList"
					+ selectedTeam.getOperationArteryList().size());
			// selectedTeam.setcurrentTeam.teamLeadId;

			for (ParishBO par : selectedTeam.getOperationParishList()) {
				// System.err.println("inside 1st parish loop");
				for (Map.Entry<String, ParishBO> eachParish : selectedTeam
						.getHashMapOfTeamParishes().entrySet()) {
					// System.err.println("Inside 2nd parish loop");
					if (eachParish.getKey().equalsIgnoreCase(
							par.getParishCode())) {
						eachParish.getValue().setSelected(true);
						System.err.println("eachParish.getValue() Selected::"
								+ eachParish.getValue().getDescription());
					}
				}

			}

			for (LocationBO loc : selectedTeam.getOperationLocationList()) {
				// System.err.println("inside 1st loc loop");
				for (Map.Entry<String, List<LocationBO>> eachLocList : selectedTeam
						.getHashMapOfTeamLocations().entrySet()) {
					// System.err.println("Inside 2nd loc loop");
					for (LocationBO eachLoc : eachLocList.getValue()) {
						if (eachLoc.getLocationId().equals(loc.getLocationId())) {
							eachLoc.setSelected(true);
							System.err.println("eachLoc.getValue() Selected::"
									+ eachLoc.getLocationDescription());
						}
					}
				}

			}

			for (ArteryBO artery : selectedTeam.getOperationArteryList()) {
				// System.err.println("inside 1st artery loop");
				for (Map.Entry<Integer, List<ArteryBO>> eachArteryList : selectedTeam
						.getHashMapOfTeamArteries().entrySet()) {
					// System.err.println("Inside 2nd artery loop");
					for (ArteryBO eachArtery : eachArteryList.getValue()) {
						if (eachArtery.getArteryId().equals(
								artery.getArteryId())) {
							eachArtery.setSelected(true);
							System.err.println("artery.getValue() Selected::"
									+ eachArtery.getArteryDescription());
						}
					}
				}

			}

			
			/*System.err
					.println("selectedTeam.getHashMapOfTeamParishes().size()::"
							+ selectedTeam.getHashMapOfTeamParishes().size());*/
			/**
			 * selectedTeam.getHashMapOfTeamArteries());
			 * 
			 * selectedTeam.setHashMapOfTeamLocations(selectedTeam.
			 * getHashMapOfTeamLocations());
			 * selectedTeam.setHashMapOfTeamParishes
			 * (selectedTeam.getHashMapOfTeamParishes());
			 */

			System.err.println("selectedTeam.getHashMapOfTeamParishes"
					+ selectedTeam.getHashMapOfTeamParishes().size());
			System.err.println("selectedTeam.getHashMapOfTeamLocations"
					+ selectedTeam.getHashMapOfTeamLocations().size());

			roadOperationView.getCurrentTeam().getHashMapOfTeamParishes()
					.putAll(selectedTeam.getHashMapOfTeamParishes());
			roadOperationView.getCurrentTeam().getHashMapOfTeamLocations()
					.putAll(selectedTeam.getHashMapOfTeamLocations());
			roadOperationView.getCurrentTeam().getHashMapOfTeamArteries()
					.putAll(selectedTeam.getHashMapOfTeamArteries());

			// copy value of selected team into current team.

			// roadOperationView.getCurrentTeam().setCurrentTeamListOperationParishList(selectedTeam.getOperationParishList());
			roadOperationView.setCurrentTeam(new TeamView(selectedTeam));//--kpowell2015-02-23
			System.err.println("team Lead::"
					+ roadOperationView.getCurrentTeam().getTeamLead()
							.getFullName());

			// populate location list with location for 1st selected parish
			if (selectedTeam.getSelectedTeamParishList() != null) {
				System.err
						.println("populate location list with location for 1st selected parish");
				populateLocationList(selectedTeam.getSelectedTeamParishList().get(
						0));
			} else {
				// clear out location and artery
				context.getFlowScope().put("locationList", null);
			}

			// populate artery list with artery for 1st selected location, since
			// location is required
			if (selectedTeam.getSelectedTeamLocationList() != null) {
				System.err
						.println("populate artery list with artery for 1st selected location, since location is required");
				populateArteryList(selectedTeam.getSelectedTeamLocationList().get(
						0));
			} else {
				context.getFlowScope().put("arteryList", null);
			}
			
			
			
			
			//System.err.println("roadOpPrev"+ roadOpPrev.getAllTeamsSelected());
			
			// Location
						locationTreeView = new DefaultTreeNode("Root", null);

						List<ParishBO> parishBo = selectedTeam.getSelectedTeamParishList();
						List<LocationBO> alreadyUsedLocs = selectedTeam.getSelectedTeamLocationList();
						List<ArteryBO> alreadyUsedArts = selectedTeam.getSelectedTeamArteryList();

						HashMap<Integer, LocationBO> selLocations = roadOperationView.getHashSelectedLocations();

						TreeNode eachlocNode = new DefaultTreeNode();
						StringBuilder arteryDes = new StringBuilder();
						StringBuilder arteryDistance = new StringBuilder();
						StringBuilder arteryStrtLat = new StringBuilder();
						StringBuilder arteryStrtLon = new StringBuilder();
						StringBuilder arteryEndLat = new StringBuilder();
						StringBuilder arteryEndLon = new StringBuilder();
						StringBuilder arteryPoints = new StringBuilder();

						
						TreeNode root;
						TreeNode node0;
						TreeNode node00;
						TreeNode node10;
						for(ParishBO par : parishBo){
							root = new DefaultTreeNode("Root", null);
							
							 node0 = new DefaultTreeNode(par.getDescription(), locationTreeView);
					       // TreeNode node1 = new DefaultTreeNode("Node 1", root);
					         
					        for(LocationBO loc: alreadyUsedLocs){
					        	if(loc.getParishCode().equalsIgnoreCase(par.getParishCode())){ 
					        		node00 = new DefaultTreeNode(loc.getShortDesc(), node0);
					        		
					        		for(ArteryBO ar: alreadyUsedArts){
					        			if(ar.getLocationId().equals(loc.getLocationId())){ 
							        		node00 = new DefaultTreeNode(ar.getShortDescription(), node00);
					        			}
					        		}
					        	}
					        }
					        	/*TreeNode node00 = new DefaultTreeNode("Node 0.0", node0);
					        TreeNode node01 = new DefaultTreeNode("Node 0.1", node0);
					         
					        TreeNode node10 = new DefaultTreeNode("Node 1.0", node1);
					         
					        node1.getChildren().add(new DefaultTreeNode("Node 1.1"));
					        node00.getChildren().add(new DefaultTreeNode("Node 0.0.0"));
					        node00.getChildren().add(new DefaultTreeNode("Node 0.0.1"));
					        node01.getChildren().add(new DefaultTreeNode("Node 0.1.0"));
					        node10.getChildren().add(new DefaultTreeNode("Node 1.0.0"));
					        root.getChildren().add(new DefaultTreeNode("Node 2"));*/
							
						}
				
			//Sort lists before display	- 2014-11-21:kpowell	
			//modified to ensure NULL pointer doesnt occur
			if(roadOperationView.getListOfStaff()!= null){
				Collections.sort(roadOperationView.getListOfStaff().getSource(),taStaffComperator );
				Collections.sort(roadOperationView.getListOfStaff().getTarget(),taStaffComperator );
			}
			
			if(roadOperationView.getListOfITA()!= null){
				Collections.sort(roadOperationView.getListOfITA().getSource(),itaComperator );
				Collections.sort(roadOperationView.getListOfITA().getTarget(),itaComperator );
			}
			
			if(roadOperationView.getListOfJP()!= null){
				Collections.sort(roadOperationView.getListOfJP().getSource(),jpComperator );
				Collections.sort(roadOperationView.getListOfJP().getTarget(),jpComperator );
			}
			
			if(roadOperationView.getListOfVehicles()!= null){
				Collections.sort(roadOperationView.getListOfVehicles().getSource(),vehicleComperator );
				Collections.sort(roadOperationView.getListOfVehicles().getTarget(),vehicleComperator );
			}
			//
			
			context.getFlowScope().put("currentTeam", selectedTeam);
			
			

			context.getFlowScope().put("currentStage", "add_team");
			context.getFlowScope().put("teamMode", "VIEW");

			context.getFlowScope().put("operation", roadOperationView);
			System.err.println("exit viewOneTeam()");
			return success();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addErrorMessage(RequestContextHolder.getRequestContext(),
					"SystemError");
			System.err.println("exit viewOneTeam()");
			return error();
		}
	}

	private void getAvailableTaStaff(RequestContext context) {

		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		// TODO : kpowell : put in a function
		try {
			System.out.println("TA STaff AUX[getAvailableResources]");
			availableStaff = new ArrayList<TAStaffBO>();

			availableStaff = getRoadOperationService()
					.getAvailableTAStaffResource(availResourceCriteria);

			if (availableStaff == null)
				availableStaff = new ArrayList<TAStaffBO>();

			System.out.println("availableStaff::" + availableStaff.size());
			roadOperationView.setListOfStaffAux(availableStaff);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// end by kpowell
	}

	
	public void onJPTransfer(RequestContext context){
	//System.err.println("inside onJPTransfer()");
		try{
		List<JPBO> selectedJP = new ArrayList<JPBO>();

		// RequestContext context = RequestContextHolder.getRequestContext();
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		if (roadOperationView.getListOfJP().getTarget() == null) {
			roadOperationView.getListOfJP().setTarget(new ArrayList<JPBO>());
		}

		boolean jpInSelected = false;

		System.err
				.println("JP "
						+ roadOperationView.getCurrentTeam()
								.getListOfSelectedJPID());
		
		String listOfJPInTarget = roadOperationView.getCurrentTeam().getListOfSelectedJPID();
		String[] splitListOfJPInTarget = listOfJPInTarget.split(",");
		System.err.println("list in AUx"+ roadOperationView.getListOfJPAux().size());
		List<String> selectJPIds = new ArrayList<String>();
		for (JPBO selectJp : roadOperationView.getListOfJP().getTarget()) {
			selectJPIds.add(selectJp.getRegNumber().trim());
		}
		
		

		List<String> srcJPIds = new ArrayList<String>();
		for (JPBO srcJp : roadOperationView.getListOfJPAux()) {
			srcJPIds.add(srcJp.getRegNumber().trim());
		}
		for (String string : splitListOfJPInTarget) {
			System.err.println("splitListOfStaffInTarget- JP::" + string);
			for (JPBO eachOrigJp : roadOperationView.getListOfJPAux()) {
				System.err.println("eachOrigJP::" + eachOrigJp);
				if (eachOrigJp.getRegNumber().equals(string)) {
					System.err.println("eachOrigJp.getRegNumber().equals(string)::"+ eachOrigJp.getRegNumber());
		
					//remove from target
					if (!srcJPIds.contains(eachOrigJp.getRegNumber())) {
						System.err.println("Put Back JP inside"	+ eachOrigJp.getRegNumber());
						roadOperationView.getListOfJP().getSource().remove(eachOrigJp);
						
					}//add to source
					else if (!srcJPIds.contains(eachOrigJp.getRegNumber())) {
						// non-selected staff must be added back to the src list
						roadOperationView.getListOfJP().getTarget()
						.add(eachOrigJp);
						System.out.println("JP Added back to SRC::"
								+ eachOrigJp.getRegNumber());

				}
					break;//exit once a match is found
				}
				

		}
			}
		
		if(roadOperationView.getListOfJP()!= null){
			Collections.sort(roadOperationView.getListOfJP().getSource(),jpComperator );
			Collections.sort(roadOperationView.getListOfJP().getTarget(),jpComperator );
		}
		
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		/* Set<JPBO> s=new TreeSet<JPBO>();
         s.addAll(roadOperationView.getListOfJP().getSource());
         
         List<JPBO> newsource = new ArrayList<JPBO>();
         Iterator it=s.iterator();
         while (it.hasNext())  
         {  
        	 newsource.add((JPBO)it.next());
         } 

         roadOperationView.getListOfJP().setSource(newsource);*/
		//context.getFlowScope().put("operation", roadOperationView);


//		System.err.println("exit onJPTransfer()");
	}
	/******SORT METHODS FOR TRANSFER*******/
	
	public void onStaffTransferSort (TransferEvent event) {
		
		RequestContext context  =  RequestContextHolder.getRequestContext();
		RoadOperationView roadOperation = (RoadOperationView) context.getFlowScope().get("operation");
		
		if(roadOperation.getListOfStaff()!= null){
			Collections.sort(roadOperation.getListOfStaff().getSource(),taStaffComperator );
			Collections.sort(roadOperation.getListOfStaff().getTarget(),taStaffComperator );
		}
		
		context.getFlowScope().put("operation", roadOperation);
	}
	
	public void onITATransferSort (TransferEvent event) {
			
			RequestContext context  =  RequestContextHolder.getRequestContext();
			RoadOperationView roadOperation = (RoadOperationView) context.getFlowScope().get("operation");
			
			if(roadOperation.getListOfITA()!= null){
				Collections.sort(roadOperation.getListOfITA().getSource(),itaComperator );
				Collections.sort(roadOperation.getListOfITA().getTarget(),itaComperator );
			}
			
			context.getFlowScope().put("operation", roadOperation);
		}
	
	
	public void onJPTransferSort (TransferEvent event) {
		
		RequestContext context  =  RequestContextHolder.getRequestContext();
		RoadOperationView roadOperation = (RoadOperationView) context.getFlowScope().get("operation");
		
		if(roadOperation.getListOfJP()!= null){
			Collections.sort(roadOperation.getListOfJP().getSource(),jpComperator );
			Collections.sort(roadOperation.getListOfJP().getTarget(),jpComperator );
		}
		
		context.getFlowScope().put("operation", roadOperation);
	}
	
	
	public void onVehicleTransferSort (TransferEvent event) {
		
		RequestContext context  =  RequestContextHolder.getRequestContext();
		RoadOperationView roadOperation = (RoadOperationView) context.getFlowScope().get("operation");
		
		if(roadOperation.getListOfVehicles()!= null){
			Collections.sort(roadOperation.getListOfVehicles().getSource(),vehicleComperator );
			Collections.sort(roadOperation.getListOfVehicles().getTarget(),vehicleComperator );
		}
		
		context.getFlowScope().put("operation", roadOperation);
	}
	
	
	/*******REDO- kpowell:2014-11-21*******/
	public void onTAStaffTransfer(RequestContext context) {

		System.err.println("onTAStaffTransfer called");
		try{
		TAStaffBO staff = new TAStaffBO();
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		if (roadOperationView.getPossibleTeamLeads() == null) {
			roadOperationView.setPossibleTeamLeads(new ArrayList<TAStaffBO>());
		}
		else{		
			roadOperationView.getPossibleTeamLeads().clear();
		}


		System.err.println("LIST of Selected TA STaff::"+ roadOperationView.getListOfStaff().getTarget().size());
		
		for(TAStaffBO staffSel : roadOperationView.getListOfStaff().getTarget()){
			roadOperationView.getPossibleTeamLeads().add(staffSel);
		}

		System.err.println("roadOperationView.getPossibleTeamLeads()"
				+ roadOperationView.getPossibleTeamLeads().size());
		context.getFlowScope().put("possibleTeamLeads",
				roadOperationView.getPossibleTeamLeads());
		context.getFlowScope().put("operation", roadOperationView);
		}catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error  in onTAStaffTransfer ");
			addErrorMessage(context, "SystemError");
			e.printStackTrace();
			
		}
		System.err.println("onTAStaffTransfer exit");
	}
	
	/**TODO - remove**/
	/*public void onTAStaffTransfer(RequestContext context) {

		System.err.println("onTAStaffTransfer called");
		try{
		TAStaffBO staff = new TAStaffBO();

		// RequestContext context = RequestContextHolder.getRequestContext();
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		if (roadOperationView.getPossibleTeamLeads() == null) {
			roadOperationView.setPossibleTeamLeads(new ArrayList<TAStaffBO>());
		}

		// getAvailableTaStaff(context);

		System.err.println("LIST of Selected TA STaff::"+ roadOperationView.getListOfStaff().getTarget().size());
		System.err
				.println("User "
						+ roadOperationView.getCurrentTeam()
								.getListOfSelectedStaffID());

		roadOperationView.getPossibleTeamLeads().clear();

		List<TAStaffBO> originalStaff = roadOperationView.getListOfStaffAux();

		String listOfStaffInTarget = roadOperationView.getCurrentTeam()
				.getListOfSelectedStaffID();

		String[] splitListOfStaffInTarget = listOfStaffInTarget.split(",");

		System.err.println("StaffInTarget>>" + splitListOfStaffInTarget.length);

		if (roadOperationView.getCurrentTeam().getSelectedTAStaff() == null) {
			roadOperationView.getCurrentTeam().setSelectedTAStaff(
					new ArrayList<TAStaffBO>());
		}

		System.err.println("originalStaff" + originalStaff.size());

		List<Integer> selectTAIds = new ArrayList<Integer>();
		for (TAStaffBO selectStaff : roadOperationView.getCurrentTeam()
				.getSelectedTAStaff()) {
			selectTAIds.add(selectStaff.getPersonId());
		}

		List<Integer> srcTAIds = new ArrayList<Integer>();
		for (TAStaffBO srcSTaff : roadOperationView.getListOfStaff()
				.getSource()) {
			srcTAIds.add(srcSTaff.getPersonId());
		}
		for (String string : splitListOfStaffInTarget) {
			System.err.println("splitListOfStaffInTarget- TA::" + string);
			for (TAStaffBO eachOrigStaff : originalStaff) {
				System.err.println("eachOrigStaff- TA::" + eachOrigStaff);
				if (eachOrigStaff.getStaffId().equals(string)) {
					System.err
							.println("eachOrigStaff.getStaffId().equals(string)::"
									+ eachOrigStaff.getStaffId());
					roadOperationView.getPossibleTeamLeads().add(eachOrigStaff);

					// if(!roadOperationView.getCurrentTeam().getSelectedTAStaff().contains(eachOrigStaff))
					if (!selectTAIds.contains(eachOrigStaff.getPersonId())) {
						System.err.println("Put Back TA Staff inside"
								+ eachOrigStaff.getFullName());
						roadOperationView.getCurrentTeam().getSelectedTAStaff()
								.add(eachOrigStaff);
					}
					break;
				} else if (!srcTAIds.contains(eachOrigStaff.getPersonId())) {
					// non-selected staff must be added back to the src list
					roadOperationView.getListOfStaff().getSource()
							.add(eachOrigStaff);
					System.err.println("TA Added back to SRC::"
							+ eachOrigStaff.getFullName());
				}

			}

		}

		System.err.println("roadOperationView.getPossibleTeamLeads()"
				+ roadOperationView.getPossibleTeamLeads().size());
		context.getFlowScope().put("possibleTeamLeads",
				roadOperationView.getPossibleTeamLeads());
		context.getFlowScope().put("operation", roadOperationView);
		}catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error  in onTAStaffTransfer ");
			addErrorMessage(context, "SystemError");
			e.printStackTrace();
			
		}
		System.err.println("onTAStaffTransfer exit");
	}*/
/**************/
	
	
	
	private List<StrategyBO> getStrategyObjects(RequestContext context,
			String listOfStratInTarget) {
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		List<StrategyBO> originalStrat = roadOperationView
				.getListOfStrategyAux();
		List<StrategyBO> selectedStrat = new ArrayList<StrategyBO>();

		String[] splitListOfStratInTarget = listOfStratInTarget.split(",");

		for (String string : splitListOfStratInTarget) {
			System.err.println("@@@@@@@@@ List of strat " + string);

			for (StrategyBO eachOrigStrat : originalStrat) {
				System.err.println("@@@@@@@@@ List of Oriog strat "
						+ eachOrigStrat.getStrategyId());
				if (((eachOrigStrat.getStrategyId() + "").trim())
						.equals(string)) {
					selectedStrat.add(eachOrigStrat);
					System.err.println("Strat " + eachOrigStrat);
					break;
				}

			}
		}

		return selectedStrat;
	}

	
	
	
	public void onStrategyTransfer(RequestContext context) {

		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		List<StrategyBO> originalStrat = roadOperationView
				.getListOfStrategyAux();
		String listOfStratInTarget = roadOperationView
				.getListOfSelectedStrategyID();
		List<StrategyBO> selectedStrat = getStrategyObjects(context,
				listOfStratInTarget);

		StrategyCriteriaBO stratCriteria = new StrategyCriteriaBO();

		stratCriteria.setStatusId(Constants.Status.ACTIVE);

		// + (List<StrategyBO>
		// )context.getFlowScope().get("availableStrategies")).size());
		List<StrategyBO> availableStrategies = new ArrayList<StrategyBO>();
		List<StrategyBO> modAvailableStrategies = new ArrayList<StrategyBO>();
		List<StrategyBO> remSrcStrategies = new ArrayList<StrategyBO>();

		try {
			availableStrategies = this.getMaintenanceService()
					.lookupStrategy(stratCriteria);
			System.err.println("Available stratlist ::"
					+ availableStrategies.size());
			System.err.println("selectedStrat::" + selectedStrat.size());
			System.err.println("Source list::"
					+ roadOperationView.getListOfStrategies().getSource()
							.size());// (selectedStrategies);
			// modAvailableStrategies = availableStrategies;
 
			// remSrcStrategies = availableStrategies;

			if (selectedStrat != null) {
				System.out
						.println("SrcStrategies ::" + remSrcStrategies.size());
				/*
				 * for(StrategyBO strategy : selectedStrat){
				 * System.out.println("remSrcStrategies ::"
				 * +remSrcStrategies.size()); for(StrategyBO availstrat :
				 * availableStrategies){
				 * 
				 * if(availstrat.getStrategyId().equals(strategy.getStrategyId())
				 * ){ remSrcStrategies.remove(strategy.getStrategyId());
				 * System.out.println("remSrcStrategies removedSelectedOnes::"+
				 * remSrcStrategies.size()); break; } }
				 */
				System.out.println("Inside NEW statements");
				boolean addStrat;
				for (StrategyBO availstrat : availableStrategies) {
					System.out.println("Inside first loop statements");
					addStrat = true;
					for (StrategyBO strategy : selectedStrat) {
						System.out.println("Inside 2nd loop statements");
						if (availstrat.getStrategyId().equals(
								strategy.getStrategyId())) {
							System.out
									.println("Inside if statement availstrat.getStrategyId().equals(strategy.getStrategyId())");
							addStrat = false;
							break;

						}
					}

					if (addStrat == true) {
						remSrcStrategies.add(availstrat);
						System.out.println("Add remaindersource strat"
								+ availstrat.getStrategyDescription()
								+ availstrat.getStrategyId());
					}
				}

			}

			System.out.println("remSrcStrategies removedSelectedOnes::"
					+ remSrcStrategies.size());

			System.err.println("New Available STrat::"
					+ availableStrategies.size());

		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		roadOperationView.setListOfStrategyAux(availableStrategies);
		// roadOperationView.getListOfStrategies().getTarget().size();
		roadOperationView.getListOfStrategies().getSource().clear();
		roadOperationView.getListOfStrategies().setSource(remSrcStrategies);
		for (StrategyBO test1 : roadOperationView.getListOfStrategies()
				.getSource()) {
			System.out.println("loop to check target>>"
					+ test1.getStrategyDescription());
			System.out
					.println("loop to check target>>" + test1.getStrategyId());
		}

		// establish minimum required resources

		/*int minimumStaffReq = 0;
		int minimumITAReq = 0;
		int minimumPoliceReq = 0;
		int minimumJPReq = 0;
		int minimumVehReq = 0;

		if (strategyRuleList != null) {
			for (StrategyRuleBO rule : strategyRuleList) {

				System.err.println("For strategy rule " + rule.getStrategyId()
						+ "  person type ... " + rule.getPersonTypeId()
						+ " min " + rule.getMinimum());

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF)) {
					if (rule.getMinimum() > minimumStaffReq) {
						minimumStaffReq = rule.getMinimum();
						System.err.println("minimumStaffReq " + minimumStaffReq
								+ "  person type ... " + rule.getPersonTypeId()
								+ " min " + rule.getMinimum());
					}

				}

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.JP)) {
					if (rule.getMinimum() > minimumJPReq) {
						minimumJPReq = rule.getMinimum();
						System.err.println("minimumJPReq " + minimumJPReq
								+ "  person type ... " + rule.getPersonTypeId()
								+ " min " + rule.getMinimum());
					}
				}

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER)) {
					if (rule.getMinimum() > minimumITAReq) {
						minimumITAReq = rule.getMinimum();
						System.err.println("minimumITAReq " + minimumITAReq
								+ "  person type ... " + rule.getPersonTypeId()
								+ " min " + rule.getMinimum());
					}
				}

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER)) {
					if (rule.getMinimum() > minimumPoliceReq) {
						minimumPoliceReq = rule.getMinimum();
						System.err.println("minimumPoliceReq "
								+ minimumPoliceReq + "  person type ... "
								+ rule.getPersonTypeId() + " min "
								+ rule.getMinimum());
					}
				}
			}
		}*/
		
		OperationStrategyRuleView operationStrategyRuleView = new OperationStrategyRuleView();
	
		

		List<StrategyRuleBO> strategyRuleList = getStrategyRulesHelper(context);
		operationStrategyRuleView = getOperationStrategyRuleView(strategyRuleList);
		
		System.err.println("selectedStrat" + selectedStrat.size());

		for (StrategyBO strat : selectedStrat) {
			if (strat.isVehicleRequired()) {
				System.err.println("For strategy "
						+ strat.getStrategyDescription() + "  veh reqd? ... "
						+ strat.isVehicleRequired());
				operationStrategyRuleView.setMinimumVehReq(1);
				break;
			}

		}

		
		if(roadOperationView.getListOfStrategies()!= null){
			Collections.sort(roadOperationView.getListOfStrategies().getSource(),strategyComperator );
			Collections.sort(roadOperationView.getListOfStrategies().getTarget(),strategyComperator );
		}
		
		/*context.getFlowScope().put("minimumStaffReq", minimumStaffReq);
		context.getFlowScope().put("minimumITAReq", minimumITAReq);
		context.getFlowScope().put("minimumPoliceReq", minimumPoliceReq);
		context.getFlowScope().put("minimumJPReq", minimumJPReq);
		context.getFlowScope().put("minimumVehReq", minimumVehReq);*/
		
		context.getFlowScope().put("minimumStaffReq", operationStrategyRuleView.getMinimumStaffReq());
		context.getFlowScope().put("minimumITAReq", operationStrategyRuleView.getMinimumITAReq());
		context.getFlowScope().put("minimumPoliceReq", operationStrategyRuleView.getMinimumPoliceReq());
		context.getFlowScope().put("minimumJPReq", operationStrategyRuleView.getMinimumJPReq());
		context.getFlowScope().put("minimumVehReq", operationStrategyRuleView.getMinimumVehReq());
		

		context.getFlowScope().put("listOfSelectedStrategies", selectedStrat);
		context.getFlowScope().put("operation", roadOperationView);
	}

	
	private OperationStrategyRuleView getOperationStrategyRuleView(List<StrategyRuleBO> strategyRuleList ){
		OperationStrategyRuleView operationStrategyRuleView = new OperationStrategyRuleView();
		if (strategyRuleList != null) {
			for (StrategyRuleBO rule : strategyRuleList) {

				System.err.println("For strategy rule " + rule.getStrategyId()
						+ "  person type ... " + rule.getPersonTypeId()
						+ " min " + rule.getMinimum());

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF)) {
					if (rule.getMinimum() > operationStrategyRuleView.getMinimumStaffReq()) {
						operationStrategyRuleView.setMinimumStaffReq(rule.getMinimum());
						System.err.println("minimumStaffReq " + operationStrategyRuleView.getMinimumStaffReq()
								+ "  person type ... " + rule.getPersonTypeId()
								+ " min " + rule.getMinimum());
					}

				}

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.JP)) {
					if (rule.getMinimum() > operationStrategyRuleView.getMinimumJPReq()) {
						operationStrategyRuleView.setMinimumJPReq(rule.getMinimum());
						System.err.println("minimumJPReq " + operationStrategyRuleView.getMinimumJPReq()
								+ "  person type ... " + rule.getPersonTypeId()
								+ " min " + rule.getMinimum());
					}
				}

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER)) {
					if (rule.getMinimum() > operationStrategyRuleView.getMinimumITAReq()) {
						operationStrategyRuleView.setMinimumITAReq(rule.getMinimum());
						System.err.println("minimumITAReq " + operationStrategyRuleView.getMinimumITAReq()
								+ "  person type ... " + rule.getPersonTypeId()
								+ " min " + rule.getMinimum());
					}
				}

				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER)) {
					if (rule.getMinimum() > operationStrategyRuleView.getMinimumPoliceReq()) {
						operationStrategyRuleView.setMinimumPoliceReq(rule.getMinimum());
						System.err.println("minimumPoliceReq "
								+ operationStrategyRuleView.getMinimumPoliceReq() + "  person type ... "
								+ rule.getPersonTypeId() + " min "
								+ rule.getMinimum());
					}
				}
			}
		}
		
		return operationStrategyRuleView;
	}
	// public Event getAvailableResources(RequestContext context) {
	// availableStaff = new ArrayList<TAStaffBO>();
	// List<ITAExaminerBO> availableITAExam = new ArrayList<ITAExaminerBO>();
	// List<JPBO> availableJP = new ArrayList<JPBO>();
	// List<PoliceOfficerBO> availablePolice = new ArrayList<PoliceOfficerBO>();
	// List<TAVehicleBO> availableVehicle = new ArrayList<TAVehicleBO>();
	//
	// List<TAStaffBO> selectedStaff = new ArrayList<TAStaffBO>();
	// List<ITAExaminerBO> selectedITAExam = new ArrayList<ITAExaminerBO>();
	// List<JPBO> selectedJP = new ArrayList<JPBO>();
	// List<PoliceOfficerBO> selectedPolice = new ArrayList<PoliceOfficerBO>();
	// List<TAVehicleBO> selectedVehicle = new ArrayList<TAVehicleBO>();
	//
	//
	//
	// RoadOperationView roadOperation = (RoadOperationView) context
	// .getFlowScope().get("operation");
	//
	// RoadOperationPortProxy roadOpProxy = new RoadOperationPortProxy();
	//
	// roadOpProxy._getDescriptor().setEndpoint(
	// ApplicationProperties.get("application.webservices.url").trim()
	// + "RoadOperationService");
	//
	// availResourceCriteria = populateAvailResourceCriteriaBO(roadOperation);
	//
	//
	// AvailableResourceBO availResourceBO = new AvailableResourceBO();
	//
	// try {
	// availResourceBO = roadOpProxy
	// .getAvailableResource(availResourceCriteria);
	//
	// } catch (InvalidFieldException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (NoRecordFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (RequiredFieldMissingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// System.err.println("Message " + e.getMessage());
	// }
	//
	// if (availResourceBO != null) {
	// availableStaff = availResourceBO.getTaStaffList();
	// availableITAExam = availResourceBO.getItaExaminerList();
	// //availableJP = availResourceBO.getJpList();
	// availablePolice = availResourceBO.getPoliceOfficerList();
	// availableVehicle = availResourceBO.getTaVehicleList();
	// }
	//
	// // IF mode is view, then prepopulate selected items
	// String teamMode = (String) context.getFlowScope().get("teamMode");
	//
	// if (teamMode.equals("VIEW")) {
	//
	//
	// //TODO - prepopulate selected items
	// }
	//
	// roadOperation.setListOfStaff(new
	// DualListModel<TAStaffBO>(availableStaff,selectedStaff));
	// roadOperation.setListOfStaffAux(availResourceBO.getTaStaffList());
	// System.err.println("Aux List " + roadOperation.getListOfStaffAux());
	//
	// roadOperation.setListOfITA(new
	// DualListModel<ITAExaminerBO>(availableITAExam,selectedITAExam));
	//
	// //roadOperation.setListOfJP(new DualListModel<JPBO>(availableJP,
	// selectedJP));
	//
	// roadOperation.setListOfPolice(new
	// DualListModel<PoliceOfficerBO>(availablePolice,selectedPolice));
	//
	// roadOperation.setListOfVehicles(new
	// DualListModel<TAVehicleBO>(availableVehicle,selectedVehicle));
	//
	//
	// context.getFlowScope().put("operation",roadOperation);
	//
	// return success();
	// }

	private AvailableResourceCriteriaBO populateAvailResourceCriteriaBO(
			RoadOperationView roadOperation, String mode) {

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		// CALL getAvailableResource METHOD ON ROAD OPERATION WEB SERVICE
		AvailableResourceCriteriaBO availResourceCriteria = new AvailableResourceCriteriaBO();

		availResourceCriteria.setRoadOperationId(roadOperation
				.getRoadOperationId());
		availResourceCriteria.setCategoryId(roadOperation.getCategoryId());

		List<String> regions = roadOperation.getSelectedRegions();

		availResourceCriteria.getOfficeLocCodeList().addAll(regions);// (reg);

		for (String string : regions) {
			System.err.println("Regions " + string);
		}

		// only used for JP. Based on current parishes selected for team
		TeamView selectedTeam = roadOperation.getSelectedTeam();
		List<ParishBO> parishes = new ArrayList<ParishBO>();

		System.err.println("mode::" + mode);
		if (mode.equalsIgnoreCase("VIEW")) {

			System.err.println("setOperationParishList"
					+ selectedTeam.getOperationParishList().size());
			parishes = selectedTeam.getOperationParishList();
		} else {
			parishes = roadOperation.getCurrentTeam()
					.getSelectedTeamParishList();
		}

		Set<String> parishCodes = new HashSet<String>();
		System.err.println("Parishes " + parishes);

		if (parishes != null) {
			for (ParishBO parishBO : parishes) {
				System.err.println("Parishes for getting JP "
						+ parishBO.getDescription());
				parishCodes.add(parishBO.getParishCode());
			}

			// availResourceCriteria.getParishCodeList().addAll(parishCodes);
		}
		//if Kingston parish is in the list, add St. Andrew parish and vice versa
		if(parishCodes.contains(Constants.ParishKingstonAndStAndrew.KINGSTON) || parishCodes.contains(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
			parishCodes.add(Constants.ParishKingstonAndStAndrew.KINGSTON);
			parishCodes.add(Constants.ParishKingstonAndStAndrew.ST_ANDREW);
		}
		System.err.println("Adding parishCodes to availResourceCriteria");
		availResourceCriteria.getParishCodeList().addAll(parishCodes);
		// availResourceCriteria.setResourceId(roadOperation.getCategoryId());

		XMLGregorianCalendar scheduDate = null;
		Date schedStartDate = null;
		Date schedEndDate = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			availResourceCriteria.setScheduledStartDate(roadOperation
							.getScheduledStartDtime());

			availResourceCriteria.setScheduledEndDate(roadOperation
							.getScheduledEndDtime());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		availResourceCriteria.setScheduledEndTime(timeFormat
				.format(roadOperation.getScheduledEndDtime()));
		availResourceCriteria.setScheduledStartTime(timeFormat
				.format(roadOperation.getScheduledStartDtime()));
		
		availResourceCriteria.setPoliceOfficerCompNum(roadOperation.getPoliceOfficerCompNum());
		availResourceCriteria.setPoliceOfficerFirstName(roadOperation.getPoliceOfficerFirstName());
		availResourceCriteria.setPoliceOfficerLastName(roadOperation.getPoliceOfficerLastName());
		availResourceCriteria.setPoliceStation(roadOperation.getPoliceStation());
		availResourceCriteria.setUsePoliceMaxFilter(true);

		return availResourceCriteria;

	}

	public Event validateOperationCompleteDetails(RequestContext context) {
		boolean result = true;
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");

		System.err.println("roadOperationView Dtime"
				+ roadOperationView.getActualEndDtime());
		System.err.println("roadOperationView Dtime"
				+ roadOperationView.getActualStartDtime());
		System.err.println("NAme" + roadOperationView.getOperationName());

		if (roadOperationView.getActualEndDtime() == null) {
			addErrorMessageWithParameter(context, "RequiredFields",
					"Actual End Date and Time");
			result = false;

		}
		if (roadOperationView.getActualStartDtime() == null) {
			addErrorMessageWithParameter(context, "RequiredFields",
					"Actual Start Date and Time");
			result = false;

		}

		// Populate Team Information for Operation
		List<TeamView> teams = roadOperationView.getTeams();
		boolean teamAtend = true;
		if (teams != null) {
			System.err.println("Inside Team loop");
			teamsloop: for (TeamView teamView : teams) {

				if (teamView.getTaStaffList() != null) {
					for (TAStaffBO taStaff : teamView.getTaStaffList()) {

						if (taStaff.getAttended() == null) {
							teamAtend = false;
							break teamsloop;
						}/*
						 * else{
						 * System.err.println("TA Staff getAttend:"+taStaff
						 * .isAttended()); }
						 */
					}
				}

				if (teamView.getItaExaminerList() != null) {
					for (ITAExaminerBO itaExam : teamView.getItaExaminerList()) {

						if (itaExam.getAttended() == null) {
							teamAtend = false;
							break teamsloop;
						}
					}
				}

				if (teamView.getPoliceOfficerList() != null) {
					for (PoliceOfficerBO police : teamView
							.getPoliceOfficerList()) {

						if (police.getAttended() == null) {
							teamAtend = false;
							break teamsloop;
						}
					}
				}

				if (teamView.getTaVehicleList() != null) {
					for (TAVehicleBO vehicle : teamView.getTaVehicleList()) {

						if (vehicle.getAttended() == null) {
							teamAtend = false;
							break teamsloop;
						}
					}
				}

				// System.err.println("teamAtend::"+ teamAtend);
				if (teamAtend == false) {

					addErrorMessageWithParameter(context, "RequiredFields",
							"Attendance for All Resource");
					result = false;
				}

			}

		}
		// End all verification tests

		if (result) {
			System.err.println("Success Complete");
			return success();
		} else {
			System.err.println("Error");
			return error();
		}
	}

	public Event validateOperationDetails(RequestContext context) {
		try{
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		roadOperationView.setOfficeLocCode(this.getRomsLoggedInUser().getLocationCode());
		//System.err.println("CAT- "+roadOperationView.getCategoryId());
		System.err.println("validateOperationDetails()");
		String stage = (String) context.getFlowScope().get("currentStage");

		TeamView selectedTeam = new TeamView(roadOperationView.getCurrentTeam());
		boolean res = isValidOperationDetails(context, selectedTeam, stage);

		//System.err.println("CAT- "+roadOperationView.getCategoryId());
		System.err.println("validateOperationDetails() returned::" + res);
		if (res) {
			// added category details
			System.err.println("roadOperationView.getCategoryDescription()"
					+ roadOperationView.getCategoryDescription());
			/*if (StringUtils.trimToNull(roadOperationView
					.getCategoryDescription()) == null) {*/
				if (roadOperationView.getCategoryId().equals(
						Constants.Category.REGIONAL)) {
					roadOperationView
							.setCategoryDescription(Constants.CategoryDesc.REGIONAL);
				} else {
					roadOperationView
							.setCategoryDescription(Constants.CategoryDesc.SPECIAL);
				}
								

				//context.getFlowScope().put("operation", roadOperationView);
				System.err.println("Updated Category Description");
			/*}*/
			
			context.getFlowScope().put("operation", roadOperationView);
			
			//DONOT assess authorization for NO ACTION operation
			if(!roadOperationView.getStatusId().equals(Constants.Status.ROAD_OPERATION_NO_ACTION)){
				isBackDated(context);
			}
			
			
			return success();
		} else {
			return error();
		}
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
			return error();
		}
	}

	
	public void isBackDated(RequestContext context){
		
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		try{
			//determine if operation require authorization
			Calendar calendar = Calendar.getInstance();
			Date currentDate = calendar.getTime();
			
			boolean checkBackdated= true;
			String fromWhere = (String) context.getFlowScope().get("fromWhere");
			//System.err.println("fromWhere="+fromWhere);
			//NOT a new operation scheduled being created
			if(fromWhere!=null || roadOperationView.getRoadOperationId()!= null){
				
				if(roadOperationView.getSavedScheduledStartDtime()== null ||roadOperationView.getSavedScheduledEndDtime()== null){
					throw new Exception();
				}
				//ONLY IF SavedScheduleDates != NewSchedule indicates that Dates has changed
	
				System.err.println(roadOperationView.getScheduledStartDtime()+"  "+ roadOperationView.getSavedScheduledStartDtime() +
						roadOperationView.getScheduledStartDtime().compareTo(roadOperationView.getSavedScheduledStartDtime()));
	
				//check condition only set false when BOTH dates are the same as saved,[ Schedule dates have not changed ]
				if((roadOperationView.getScheduledStartDtime().compareTo(roadOperationView.getSavedScheduledStartDtime())==0 
						&& roadOperationView.getScheduledEndDtime().compareTo(roadOperationView.getSavedScheduledEndDtime())==0 )){
	
					checkBackdated= false;
					//reset preview object to reflect what was saved in the database by validating the saved dates
					roadOperationView.setBackDated((String)context.getFlowScope().get("savedBackDated"));
					roadOperationView.setAuthorized((String)context.getFlowScope().get("savedAuthorized"));			
					
				}
			//	System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(roadOperationView));
			}
			System.err.println("checkBackdated"+checkBackdated);
			
			if(checkBackdated){				
				
				System.err.println("SChSTart "+ DateUtils.before(roadOperationView.getScheduledStartDtime(), currentDate));
				System.err.println("SCHEND "+DateUtils.before(roadOperationView.getScheduledEndDtime(), currentDate));
				if(DateUtils.before(roadOperationView.getScheduledStartDtime(), currentDate) || DateUtils.before(roadOperationView.getScheduledEndDtime(), currentDate)){
					roadOperationView.setBackDated("Y");
					roadOperationView.setAuthorized("N");
					context.getMessageContext().addMessage(
							new MessageBuilder().info().defaultText(
											"This is a Backdated Road Operation and will require Authorization").build());
					System.err.println("This is a Backdated Road Operation and will require Authorization");
				}
				else{
					roadOperationView.setBackDated("N");
					roadOperationView.setAuthorized("Y");
					System.err.println("No Backdated");
					
				}
			}
			else{
				//only reset values if not already set - kpowell
				if(roadOperationView.getBackDated()== null && roadOperationView.getAuthorized()== null){
					System.err.println("roadOperationView.getBackDated()== null && roadOperationView.getAuthorized()== null");
					roadOperationView.setBackDated("N");
					roadOperationView.setAuthorized("Y");
					System.err.println("No Backdated");
				}				
				System.err.println("Schedule dates have not changed");
			}
			
			context.getFlowScope().put("operation", roadOperationView);
			
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
	}

	public boolean isValidOperationDetails(RequestContext context,
			TeamView selectedTeam, String stage) {

		// validate based on the stage we are at on the progress bar
		String teamMode = (String) context.getFlowScope().get("teamMode");
		System.err.println("isValidOperationDetails()");
		boolean result = true;
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		System.err.println("Stage " + stage);
		if (stage.equals("operation_details")) {
			if (!validOperationDetails(context)|| 
					//perform this validation only if the Operation was previously saved, check about theotehr states that would require this check
				(roadOpView.getStatusId().equals(Constants.Status.ROAD_OPERATION_SCHEDULING) && !validResources(context))) {
				result = false;
			}
		}

		if (stage.equals("strategy")) {
			if (!validOperationDetails(context)
					|| !validStrategyDetails(context)) {
				result = false;
			}
		}

		if (stage.equals("add_team")) {
			if (!validTeam(context, teamMode, selectedTeam))// ||
															// !validResources(context))
			{

				result = false;
			}
		}

		if (stage.equals("resources")) {
			if (!validOperationDetails(context)
					|| !validStrategyDetails(context) ||
					// validTeam(context,teamMode) &&
					!validResources(context)) {
				result = false;
			}
		}

		if (stage.equals("court")) {
			if (!validOperationDetails(context)
					|| !validStrategyDetails(context) ||
					// validTeam(context,teamMode) &&
					!validResources(context) || !validCourt(context)) {
				result = false;
			}
		}

		return result;
	}

	private boolean validCourt(RequestContext context) {
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		String opCategory = roadOpView.getCategoryId();

		Integer courtID = roadOpView.getCourtId();
		Date courtDate = roadOpView.getCourtDate();
		System.err.println("Court " + courtID);
		System.err.println("Court Date: " + roadOpView.getCourtDate());
		boolean result = true;
		try{
			if (opCategory.equals(Constants.Category.REGIONAL)) {
				/*
				 * if(courtID == null || courtDate==null) {
				 * context.getMessageContext().addMessage( new
				 * MessageBuilder().error
				 * ().defaultText("Court Details are Required for Rgional Operations"
				 * ).build());
				 * 
				 * result= false; }
				 */

				if (courtID == null || courtID == 0) {
					addErrorMessageWithParameter(context, "RequiredFields", "Court");
					result = false;
				}

				if (courtDate == null) {
					addErrorMessageWithParameter(context, "RequiredFields",
							"Court Date");
					result = false;
				} else {

					roadOpView.setCourtDate(returnCourtDateTime(courtDate));

					System.err.println("convertedDate: "
							+ roadOpView.getCourtDate());
				
					//} code below not need unless courtDate !=null

					System.err.println("Court Validation stuff "
						+ roadOpView.getCourtDate() + "... "
						+ roadOpView.getScheduledEndDate());

					int resultFromCourtValidation = DateUtils.validateCourtDate(
							roadOpView.getCourtDate(),
							roadOpView.getScheduledEndDtime());

					switch (resultFromCourtValidation) {
					case 0:
						// do nothing
						// result = true;
						break;

					case 1:
						context.getMessageContext()
						.addMessage(
								new MessageBuilder()
								.error()
								.defaultText(
										"Court Date Must Be Three(3) Clear Working Days from Scheduled End Date")
										.build());
						result = false;
						break;

					case 2:
						context.getMessageContext().addMessage(
								new MessageBuilder().error()
								.defaultText("Court Date cannot be a Weekend")
								.build());
						result = false;
						break;

					case 3:
						context.getMessageContext().addMessage(
								new MessageBuilder().error()
								.defaultText("Court Date Cannot Be a Holiday")
								.build());
						result = false;
						break;

					default:
						// do nothing
						// result = true;
						break;

					}
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
			result = false;
		}
		return result;

	}

	private boolean validResources(RequestContext context) {
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		List<StrategyBO> selectedStrategies = (List<StrategyBO>) context
				.getFlowScope().get("listOfSelectedStrategies");
		roadOpView.getListOfStrategies().setTarget(selectedStrategies);

		List<TeamView> currentTeamsWithDeletes = roadOpView.getTeams();
		List<TeamView> teams = new ArrayList<TeamView>();
		boolean result = true;

		if(currentTeamsWithDeletes!= null){
			//TODO Make deleted team functionality more efficient -20150112 @kpowell
			//remove deleted teams from list for processing
			Iterator<TeamView> iterator = currentTeamsWithDeletes.iterator();
			while (iterator.hasNext()) {
				TeamView teamView = (TeamView) iterator.next();
				if(!teamView.isDelete()){
					teams.add(teamView);
				}
			}
		}else{
			teams = null;
		}
		//
		
		// At leat one team is required
		if (teams == null || teams.isEmpty()) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("At least one Team ").build());

			result = false;
		}

		
	
		// resources must comply with strategy rules

		boolean isVehicleRequired = false;

		int staffListSize = 0;
		int jpListSize = 0;
		int itaExaminerListSize = 0;
		int policeOfficerListSize = 0;
		boolean vehicleAssigned = false;

		List<AssignedTeamDetailsBO> assignedTeamDetailsBOList = new ArrayList<AssignedTeamDetailsBO>();
		AssignedTeamDetailsBO assignedTeamDet = new AssignedTeamDetailsBO();
		TeamBO teamBO = new TeamBO();
		boolean TeamNameNotFound = false;
		if (teams != null) {
			for (TeamView teamView : teams) {

					//kpowell:
					//TODO should be caught by valid team but due to Cancel functionality flaw this is needed
					// Team Name Cannot be Null
					if (!StringUtil.isSet(teamView.getTeamName())&& !TeamNameNotFound) {
						TeamNameNotFound = true;
				/*		context.getMessageContext().addMessage(
								new MessageBuilder().error().code("RequiredFields")
										.arg("Team Name ").build());
		
						result = false;*/
					}
					teamBO.setTeamLead(teamView.getTeamLead());
					teamBO.setTeamName(teamView.getTeamName());

					assignedTeamDet.setTeamBO(teamBO);
					
					//removed conditions using assignedTeamDet. since all values are never intialized
					if (/*assignedTeamDet.getItaExaminerList() != null
							&&*/ teamView.getItaExaminerList() != null) {
						assignedTeamDet.getItaExaminerList().addAll(
								teamView.getItaExaminerList());
					}
					if (/*assignedTeamDet.getJpList() != null
							&& */teamView.getJpList() != null) {
						assignedTeamDet.getJpList().addAll(teamView.getJpList());
					}
					if (/*assignedTeamDet.getPoliceOfficerList() != null
							&&*/ teamView.getPoliceOfficerList() != null) {
						assignedTeamDet.getPoliceOfficerList().addAll(
								teamView.getPoliceOfficerList());
					}
					if (/*assignedTeamDet.getTaStaffList() != null
							&&*/ teamView.getTaStaffList() != null) {
						assignedTeamDet.getTaStaffList().addAll(
								teamView.getTaStaffList());
					}
					if (/*assignedTeamDet.getTaVehicleList() != null
							&& */teamView.getTaVehicleList() != null) {
						assignedTeamDet.getTaVehicleList().addAll(
								teamView.getTaVehicleList());
					}

					List<ArteryBO> arteries = teamView.getOperationArteryList();
					List<LocationBO> locations = teamView
							.getOperationLocationList();

					LocationBO opLocation = new LocationBO();
					ArteryBO opArtery = new ArteryBO();

					for (LocationBO locationBO : locations) {
						locationBO.setAuditEntryBO(null);
						opLocation = new LocationBO();

						opLocation.setLocationId(locationBO.getLocationId());
						opLocation.setParishCode(locationBO.getParishCode());

						assignedTeamDet.getOperationLocationList().add(opLocation);
					}

					for (ArteryBO arteryBO : arteries) {
						arteryBO.setAuditEntryBO(null);
						opArtery = new ArteryBO();

						opArtery.setArteryId(arteryBO.getArteryId());
						opArtery.setLocationId(arteryBO.getLocationId());

						assignedTeamDet.getOperationArteryList().add(opArtery);

					}

					assignedTeamDetailsBOList.add(assignedTeamDet);
					assignedTeamDet = new AssignedTeamDetailsBO();
					teamBO = new TeamBO();

					System.err.println("Called by validResources()>>>");

					// In addition, check to ensure that based on any changes to
					// operation details, each person is still available
					System.err.println("ABOUT TO CHECK AVAILABILITY");
					AvailableResourceCriteriaBO availResourceCriteria = new AvailableResourceCriteriaBO();
					availResourceCriteria = populateAvailResourceCriteriaBO(
							roadOpView, "N/A");

					if (roadOpView.getCurrentTeam()
							.getCurrentTeamListOperationParishList() != null) {
						System.err
						.println("getCurrentTeam().getCurrentTeamListOperationParishList()::"
								+ roadOpView
								.getCurrentTeam()
								.getCurrentTeamListOperationParishList());
					} else {
						System.err
						.println("getCurrentTeam().getCurrentTeamListOperationParishList()::: null");
					}

					// ITA
					List<ITAExaminerBO> listOfITA = teamView.getItaExaminerList();

					if (listOfITA != null) {
						for (ITAExaminerBO itaExaminerBO : listOfITA) {

							availResourceCriteria.setResourceId(itaExaminerBO
									.getPersonBO().getPersonId());

							try {
								if (!getRoadOperationService().isPersonAvailable(
										availResourceCriteria)) {
									context.getMessageContext()
									.addMessage(
											new MessageBuilder()
											.error()
											.defaultText(
													"ITA Examiner ["
															+ itaExaminerBO
															.getPersonBO()
															.getFullName()
															+ "] is no longer available based on current Operation details. ")
															.build());
									result = false;
								}
							} catch (InvalidFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RequiredFieldMissingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					// Police
					List<PoliceOfficerBO> listOfPolice = teamView
							.getPoliceOfficerList();

					if (listOfPolice != null) {
						for (PoliceOfficerBO policeOfficerBO : listOfPolice) {

							availResourceCriteria.setResourceId(policeOfficerBO
									.getPersonID());

							try {
								if (!getRoadOperationService().isPersonAvailable(
										availResourceCriteria)) {
									context.getMessageContext()
									.addMessage(
											new MessageBuilder()
											.error()
											.defaultText(
													"Police Officer ["
															+ policeOfficerBO
															.getFullName()
															+ "] is no longer available based on current Operation details. ")
															.build());
									result = false;
								}
							} catch (InvalidFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RequiredFieldMissingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					// JP
					List<JPBO> listOfJP = teamView.getJpList();

					if (listOfJP != null) {
						System.err.println("Checking jp availability");
						for (JPBO jpbo : listOfJP) {

							availResourceCriteria.setResourceId(jpbo.getPersonBO()
									.getPersonId());

							try {
								if (!getRoadOperationService().isPersonAvailable(
										availResourceCriteria)) {
									context.getMessageContext()
									.addMessage(
											new MessageBuilder()
											.error()
											.defaultText(
													"JP ["
															+ jpbo.getPersonBO()
															.getFullName()
															+ "] is no longer available based on current Operation details. ")
															.build());
									result = false;
								}
							} catch (InvalidFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RequiredFieldMissingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					// TA Saff
					List<TAStaffBO> listOfTAStaff = teamView.getTaStaffList();

					if (listOfTAStaff != null) {
						System.err.println("Checking staff availability "
								+ listOfTAStaff);
						for (TAStaffBO taStaffBO : listOfTAStaff) {

							availResourceCriteria.setResourceId(taStaffBO
									.getPersonId());

							try {
								if (!getRoadOperationService().isPersonAvailable(
										availResourceCriteria)) {
									context.getMessageContext()
									.addMessage(
											new MessageBuilder()
											.error()
											.defaultText(
													"TA Staff ["
															+ taStaffBO
															.getFullName()
															+ "] is no longer available based on current Operation details. ")
															.build());
									result = false;
								}
							} catch (InvalidFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RequiredFieldMissingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

					//add back validation check- kpowell-2015-01-29
					// Vehicle
					 List<TAVehicleBO> listOfTAVehicle = teamView.getTaVehicleList();
					
					 if(listOfTAVehicle != null) {
						 System.err.println("Checking vehicle availability "
									+ listOfTAVehicle);
						 for (TAVehicleBO taVehicleBO : listOfTAVehicle) {

							 availResourceCriteria.setResourceId(taVehicleBO.getTaVehicleId());

							 try {
								 if(!getRoadOperationService().isTAVehicleAvailable(availResourceCriteria))
								 {
									 context.getMessageContext().addMessage(
											 new
											 MessageBuilder().error().defaultText("TA Vehicle [Registration Number] "
													 + taVehicleBO.getVehicle().getPlateRegNo() +
													 " is no longer available based on current Operation details. ").build());
									 result = false;
								 }
							 } catch (InvalidFieldException e) {
								 // TODO Auto-generated catch block
								 e.printStackTrace();
							 } catch (RequiredFieldMissingException e) {
								 // TODO Auto-generated catch block
								 e.printStackTrace();
							 }catch(Exception e){
								 
							 }
						 }
					 }

					/**
					 * kpowell: 2015-01-15
					 * validate strategy per team
					 */
					if (validateStrategyRule(context, teamView, "resources") == false) {
						result = false;
					}
				}
			
			
			//kpowell:
			//TODO should be caught by valid team but due to Cancel functionality flaw this is needed
			// Team Name Cannot be Null
			if(TeamNameNotFound){
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("RequiredFields")
								.arg("Team Name ").build());
	
				result = false;
			}

		}

		List<StrategyRuleBO> strategyRuleList = (ArrayList<StrategyRuleBO>) context
				.getFlowScope().get("strategyRules");
		List<StrategyBO> strats = roadOpView.getListOfStrategies().getTarget();

		for (StrategyBO strat : strats) {
			if (strat.isVehicleRequired()) {
				isVehicleRequired = true;
				break;
			}

		}

		for (AssignedTeamDetailsBO teamDetails : assignedTeamDetailsBOList) {
			if (teamDetails.getTaVehicleList() != null
					&& teamDetails.getTaVehicleList().size() > 0) {
				vehicleAssigned = true;
			}

			if (teamDetails.getTaStaffList() != null) {
				staffListSize = staffListSize
						+ teamDetails.getTaStaffList().size();
			}

			if (teamDetails.getJpList() != null) {
				jpListSize = jpListSize + teamDetails.getJpList().size();
			}

			if (teamDetails.getItaExaminerList() != null) {
				itaExaminerListSize = itaExaminerListSize
						+ teamDetails.getItaExaminerList().size();
			}

			if (teamDetails.getPoliceOfficerList() != null) {
				policeOfficerListSize = policeOfficerListSize
						+ teamDetails.getPoliceOfficerList().size();
			}
		}

		/*if (isVehicleRequired == true && vehicleAssigned == false) {

			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("At least one Vehicle ").build());
			result = false;
		}

		System.err.println("%%%%% Strategy Rule List " + strategyRuleList);
		if (strategyRuleList != null) {
			for (StrategyRuleBO rule : strategyRuleList) {
				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF)) {
					if (staffListSize < rule.getMinimum()
							|| staffListSize > rule.getMaximum()) {

						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " TA Staff ").build());
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.JP)) {
					if (jpListSize < rule.getMinimum()
							|| jpListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum() + " JPs ")
										.build());
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER)) {
					if (itaExaminerListSize < rule.getMinimum()
							|| itaExaminerListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " ITA Examiners ").build());
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER)) {
					if (policeOfficerListSize < rule.getMinimum()
							|| policeOfficerListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " Police Officers ").build());
						result = false;
					}
				}
			}
		}*/
		
		//TODO- to be revised and only perform validation perform per team
		/**
		 * removed as this validation should ONLY be applied on a per team basis
		 * //kpowell-2015/01/13
		 
		 boolean validStrategyRule = validateOperationStrategyRule(strategyRuleList, isVehicleRequired, vehicleAssigned, staffListSize, jpListSize, itaExaminerListSize, policeOfficerListSize, "");

		if(validStrategyRule==false){
			result = false;
		}*/
		
		//System.err.println("validResourceDetails::"+ result);
		return result;

	}
	
	private boolean validateOperationStrategyRule(List<StrategyRuleBO> strategyRuleList, boolean isVehicleRequired, boolean vehicleAssigned, int staffListSize,
			int jpListSize, int itaExaminerListSize, int policeOfficerListSize, String teamName){
		
		boolean result = true;
		
		RequestContext context = RequestContextHolder.getRequestContext();
		String teamString = "";
		
		if(StringUtils.isNotBlank(teamName)){
			teamString = "for Team: " + teamName;
		}
		if (isVehicleRequired == true && vehicleAssigned == false) {

			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("At least one Vehicle " + teamString).build());
			result = false;
		}

		System.err.println("%%%%% Strategy Rule List " + strategyRuleList);
		
		if (strategyRuleList != null) {
			for (StrategyRuleBO rule : strategyRuleList) {
				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF)) {
					if (staffListSize < rule.getMinimum()
							|| staffListSize > rule.getMaximum()) {

						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " TA Staff "
												+ teamString).build());
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.JP)) {
					if (jpListSize < rule.getMinimum()
							|| jpListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum() + " JPs "
												+ teamString)
										.build());
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER)) {
					if (itaExaminerListSize < rule.getMinimum()
							|| itaExaminerListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " ITA Examiners "
												+ teamString).build());
						result = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER)) {
					if (policeOfficerListSize < rule.getMinimum()
							|| policeOfficerListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg("A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " Police Officers "
												+ teamString).build());
						result = false;
					}
				}
			}
		}
		
		return result;

	}

	public ScheduleMinMaxUi getMinMaxMessage(String personType){
		
		RequestContext context = RequestContextHolder.getRequestContext();
		
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		ScheduleMinMaxUi scheduleMinMaxUi = new ScheduleMinMaxUi("");
		String message = "";
		List<StrategyRuleBO> strategyRuleList = this.getStrategyRulesHelper(context);
		
		if (personType.equalsIgnoreCase("VE") && vehicleRequired(roadOpView.getListOfStrategies().getTarget())){
			message = "At least one vehicle is required";
			scheduleMinMaxUi.setMessage(message);
			
			return scheduleMinMaxUi;
			
		}
		
		if (strategyRuleList != null) {
			for (StrategyRuleBO rule : strategyRuleList) {
				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF) 
						&& personType.equalsIgnoreCase(rule.getPersonTypeId())) {
						
						message =		"A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " TA Staff is required";
												
				
					
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.JP) 
						&& personType.equalsIgnoreCase(rule.getPersonTypeId())) {
						message = "A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum() + " JPs is required";
												

					
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER) 
						&& personType.equalsIgnoreCase(rule.getPersonTypeId())) {
						message = "A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " ITA Examiners is required";

				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER) 
						&& personType.equalsIgnoreCase(rule.getPersonTypeId())) {
					message = "A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " Police Officers is required";
			
				}
			}
		}
		
		scheduleMinMaxUi.setMessage(message);
		
		return scheduleMinMaxUi;
	}
	
	private boolean validTeam(RequestContext context, String teamMode,
			TeamView selectedTeam) {
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		System.err.println("validTeam()::");
		boolean result = true;

		
		List<TeamView> currentTeams = new  ArrayList<TeamView>();
		List<TeamView> currentTeamsWithDeletes = (List<TeamView>) context.getFlowScope()
				.get("teamDatatable"); // roadOpView.getTeams();
		System.err.println("Teams " + currentTeamsWithDeletes);

		
		if(currentTeamsWithDeletes!= null){
			System.err.println("removing deleted teams");
			//TODO Make deleted team functionality more efficient -20150112
			//remove deleted teams from listing
			Iterator<TeamView> iterator = currentTeamsWithDeletes.iterator();
			while (iterator.hasNext()) {
				TeamView teamView = (TeamView) iterator.next();
				if(!teamView.isDelete()){
					currentTeams.add(teamView);
					//System.err.println("team added to list without deletes"+ teamView.getTeamName());
				}
			}
		}else{
			currentTeams = null;
		}
		//
		
		// TeamView selectedTeam = roadOpView.getCurrentTeam();

		List<TAStaffBO> selectedStaff = new ArrayList<TAStaffBO>();
		List<ITAExaminerBO> selectedITA = new ArrayList<ITAExaminerBO>();
		List<JPBO> selectedJP = new ArrayList<JPBO>();
		List<PoliceOfficerBO> selectedPolice = new ArrayList<PoliceOfficerBO>();
		List<TAVehicleBO> selectedVehicles = new ArrayList<TAVehicleBO>();

		selectedStaff = getStaffForTeam(selectedTeam, context);
		selectedITA = getITAForTeam(selectedTeam, context);
		selectedJP = getJPForTeam(selectedTeam, context);
		selectedPolice = getPoliceForTeam(selectedTeam, context);
		selectedVehicles = getVehForTeam(selectedTeam, context);

		DualListModel<StrategyBO> allStrategies = (DualListModel<StrategyBO>) context
				.getFlowScope().get("availableStrategies");
		List<StrategyBO> selectedStrategies = roadOpView
				.getListOfStrategies().getTarget();// allStrategies.getTarget();

		
		//2014-10-27-REDO
		/*
		 * HashMap<Integer, ArteryBO> selectedArt = roadOpView
				.getHashSelectedArteries();
		 * HashMap<Integer, LocationBO> selectedLoc = roadOpView
				.getHashSelectedLocations();*/
		List<ArteryBO> selectedArt = selectedTeam.getSelectedTeamArteryList();
		List<LocationBO> selectedLoc = selectedTeam.getSelectedTeamLocationList();
		List<ParishBO> selectedParishes = selectedTeam.getSelectedTeamParishList();

		// Team Name Cannot be Null
		if (!StringUtil.isSet(selectedTeam.getTeamName())) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("Team Name ").build());

			result = false;
		}

		// Team Name must be Unique if adding new team
		if (currentTeams != null ) // there are other teams to compare against
		{
			/**
			 * kpowell:2015-01-14
			 * removed since this validation must be performed always
			 */
			//if (teamMode.equalsIgnoreCase("ADD")) {
				if (nameUsedForOtherTeam(selectedTeam.getTeamName(),
						currentTeams, selectedTeam.getTeamID())) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Team Name "
															+ selectedTeam
																	.getTeamName()
															+ " already used for another team")
											.build());

					result = false;
				}
			//}
		}

		// Team must have a team lead
		if (!StringUtil.isSet(selectedTeam.getTeamLeadId())) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("Team Lead ").build());

			result = false;
		}

		// If strategy selected with "vehicle required" set to "Y" at least one
		// vehicle must be selected
		if (vehicleRequired(selectedStrategies)) {
			if (selectedVehicles == null || selectedVehicles.isEmpty()) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("RequiredFields")
								.arg("At least one Vehicle ").build());

				result = false;
			}
		}

		//validate Parish required
		if (selectedParishes == null || selectedParishes.isEmpty()) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("At least one Parish ").build());

			result = false;
		}
		/***/
		//validate once parishes exists
		if(selectedParishes!= null && selectedParishes.size()>0){
			int parLocCnt = 0;
			for(ParishBO par : selectedParishes){
				parLocCnt = 0;
				if(selectedLoc != null && selectedLoc.size()>0){
					for(LocationBO loc : selectedLoc){
						System.err.println("selectedLoc"+ loc.getParishCode()+"/"+loc.getLocationDescription());
						if(loc.getParishCode().equals(par.getParishCode())){
							parLocCnt++;
							
							break;//at least one location found
						}
					}
				}
				System.err.println("parLocCnt"+parLocCnt);
				//exits once it is found that a parish is selected without an selected location
				if(parLocCnt==0){
					context.getMessageContext().addMessage(
							new MessageBuilder()
									.error()
									.defaultText(
											"At least one Location is required for each selected parish")
									.build());
		
					result = false;
					break;
				}
				
				
			}
		}	
		
	
		
		if(selectedLoc!= null && selectedLoc.size()>0){
			int locArtCnt = 0;
			for(LocationBO selLoc : selectedLoc){
				locArtCnt = 0;
				if(selectedParishes != null && selectedParishes.size()>0){
					for(ParishBO par : selectedParishes){
						System.err.println("selectedLoc"+ par.getParishCode()+"/"+par.getDescription());
						if(selLoc.getParishCode().equals(par.getParishCode())){
							locArtCnt++;
							
							break;//at least one parish found
						}
					}
				}
				System.err.println("locArtCnt"+locArtCnt);
				//exits once it is found that a parish is selected without an selected location
				if(locArtCnt==0){
					context.getMessageContext().addMessage(
							new MessageBuilder()
									.error()
									.defaultText(
											"The associated parish is required for each selected location")
									.build());
		
					result = false;
					break;
				}
				
				
			}
		}	
		
		
		if(selectedArt!= null && selectedArt.size()>0){
			int artLocCnt = 0;
			for(ArteryBO selArt : selectedArt){
				artLocCnt = 0;
				if(selectedLoc != null && selectedLoc.size()>0){
					for(LocationBO loc2 : selectedLoc){
						System.err.println("loc2"+ loc2.getLocationId()+"/"+loc2.getLocationDescription());
						if(loc2.getLocationId().equals(selArt.getLocationId())){
							artLocCnt++;
							//System.err.println("artLocCnt"+artLocCnt);
							break;//at least one location found
						}
					}
				}
				System.err.println("artLocCnt"+artLocCnt);
				//exits once it is found that a parish is selected without an selected location
				if(artLocCnt==0){
					context.getMessageContext().addMessage(
							new MessageBuilder()
									.error()
									.defaultText(
											"The associated location is required for each selected artery")
									.build());
		
					result = false;
					break;
				}
				
				
			}
		}	
		
		/***/		
				
		if (selectedLoc == null || selectedLoc.isEmpty()) {
			if(!(selectedParishes!= null && selectedParishes.size()>0))
			{
				context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("At least one Location ").build());

				result = false;
			}
		}

		
		
		// If strategy selected with "artery required" set to "Y" at least one
		// artery must be selected
		if (arteryRequired(selectedStrategies)) {
			if (selectedArt == null || selectedArt.isEmpty()) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("RequiredFields")
								.arg("At least one Artery ").build());

				result = false;
			}
		}

		// No one or a vehicle should exist in more than one team
		if (!noTeamExists()) // there are other teams to compare against
		//if (currentTeams != null) // there are other teams to compare against
		{
			for (TAStaffBO taStaffBO : selectedStaff) {

				if (staffExistsInOtherTeam(taStaffBO, currentTeams,
						selectedTeam.getTeamID())) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"TA Staff "
															+ taStaffBO
																	.getFullName()
															+ " already exists in another team")
											.build());

					result = false;
				}
			}

			for (ITAExaminerBO itaExaminerBO : selectedITA) {
				if (iTAExistsInOtherTeam(itaExaminerBO, currentTeams,
						selectedTeam.getTeamID())) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"ITA Examiner "
															+ itaExaminerBO
																	.getPersonBO()
																	.getFullName()
															+ " already exists in another team")
											.build());

					result = false;
				}
			}

			for (JPBO jpbo : selectedJP) {
				if (jpExistsInOtherTeam(jpbo, currentTeams,
						selectedTeam.getTeamID())) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Justice of the Peace "
															+ jpbo.getPersonBO()
																	.getFullName()
															+ " already exists in another team")
											.build());

					result = false;
				}
			}

			for (PoliceOfficerBO policeOfficerBO : selectedPolice) {
				if (policeExistsInOtherTeam(policeOfficerBO, currentTeams,
						selectedTeam.getTeamID())) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Police Officer "
															+ policeOfficerBO
																	.getFullName()
															+ " already exists in another team")
											.build());

					result = false;
				}
			}

			for (TAVehicleBO taVehicleBO : selectedVehicles) {
				if (vehicleExistsInOtherTeam(taVehicleBO, currentTeams,
						selectedTeam.getTeamID())) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Vehicle "
															+ taVehicleBO
																	.getVehicle()
																	.getPlateRegNo()
															+ " already exists in another team")
											.build());

					result = false;
				}
			}
		}

		/**
		 * new paramter added to accomodate function calls from validateResource
		 */
		if (validateStrategyRule(context, selectedTeam, "add_team") == false) {
			result = false;
		}

		System.err.println("End validTeam()::");
		return result;

	}

	private String[] convertStringToArray(String toConvert) {
		if (toConvert != null) {
			return toConvert.split(",");
		}
		return null;
	}

	/**
	 * @modified to accomodate being called from validateResources
	 */
	private boolean validateStrategyRule(RequestContext context,
			TeamView selectedTeam, String stage) {
		System.err.println("validateStrategyRule");
		boolean valid = true;
		boolean isVehicleRequired = false;

		int staffListSize = 0;
		int jpListSize = 0;
		int itaExaminerListSize = 0;
		int policeOfficerListSize = 0;
		boolean vehicleAssigned = false;
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");
		List<StrategyRuleBO> strategyRuleList = (ArrayList<StrategyRuleBO>) context
				.getFlowScope().get("strategyRules");
		
		
		if(strategyRuleList== null || strategyRuleList.size()==0 ){
			System.err.println("Populating Strategy Rules");
			getStrategyRules(context);
			strategyRuleList = (ArrayList<StrategyRuleBO>) context.getFlowScope().get("strategyRules");
		}
		List<StrategyBO> strats = roadOpView.getListOfStrategies().getTarget();

		for (StrategyBO strat : strats) {
			if (strat.isVehicleRequired()) {
				isVehicleRequired = true;
				break;
			}

		}
		// System.err.println("isVehicleRequired: " + isVehicleRequired);

		if (selectedTeam != null) {
			if (StringUtils.trimToNull(selectedTeam.getListOfSelectedVehID()) != null) {
				vehicleAssigned = true;
			}

			if (StringUtils.trimToNull(selectedTeam.getListOfSelectedStaffID()) != null) {
				staffListSize = convertStringToArray(selectedTeam
						.getListOfSelectedStaffID()).length;
			}

			if (StringUtils.trimToNull(selectedTeam.getListOfSelectedJPID()) != null) {
				jpListSize = convertStringToArray(selectedTeam
						.getListOfSelectedJPID()).length;
			}

			if (StringUtils.trimToNull(selectedTeam.getListOfSelectedITAID()) != null) {
				itaExaminerListSize = convertStringToArray(selectedTeam
						.getListOfSelectedITAID()).length;
			}

			if (StringUtils
					.trimToNull(selectedTeam.getListOfSelectedPoliceID()) != null) {
				policeOfficerListSize = convertStringToArray(selectedTeam
						.getListOfSelectedPoliceID()).length;
			}
		}

		String inclTeamName="";
		/*
		 * Validation check already done in valid team and should only be here while validating resource stage by kpowell
		 * */
		if(stage.equalsIgnoreCase("resources")){			 		  
				
			inclTeamName = "Team [" +selectedTeam.getTeamName()+"]: ";
			if(isVehicleRequired==true && vehicleAssigned==false){		 
				  context.getMessageContext().addMessage( new
				  MessageBuilder().error().code("RequiredFields")
				  .arg(inclTeamName+"At least one Vehicle ").build()); valid = false; }	
	
			 
		}

		System.err.println("%%%%% Strategy Rule List " + strategyRuleList);
		if (strategyRuleList != null) {
			for (StrategyRuleBO rule : strategyRuleList) {
				if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.TA_STAFF)) {
					if (staffListSize < rule.getMinimum()
							|| staffListSize > rule.getMaximum()) {

						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg(inclTeamName+"A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " TA Staff ").build());
						valid = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.JP)) {
					if (jpListSize < rule.getMinimum()
							|| jpListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg(inclTeamName+"A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum() + " JPs ")
										.build());
						valid = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.ITA_EXAMINER)) {
					if (itaExaminerListSize < rule.getMinimum()
							|| itaExaminerListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg(inclTeamName+"A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " ITA Examiners ").build());
						valid = false;
					}
				} else if (rule.getPersonTypeId().equalsIgnoreCase(
						Constants.PersonType.POLICE_OFFCER)) {
					if (policeOfficerListSize < rule.getMinimum()
							|| policeOfficerListSize > rule.getMaximum()) {
						context.getMessageContext().addMessage(
								new MessageBuilder()
										.error()
										.code("RequiredFields")
										.arg(inclTeamName+"A minimum of "
												+ rule.getMinimum()
												+ " and a maximum of "
												+ rule.getMaximum()
												+ " Police Officers ").build());
						valid = false;
					}
				}
			}
		}

		return valid;
	}

	private boolean nameUsedForOtherTeam(String teamName,
			List<TeamView> currTeams, String teamID) {
		boolean result = false;

		if (StringUtil.isSet(teamName)) {
			if (currTeams != null) {
				//System.err.println("currTeams Size"+ currTeams.size());
				for (TeamView teamView : currTeams) {
					
					//teamName comparison is not case sensitive
					//kpowell
					if (teamView.getTeamName().equalsIgnoreCase(teamName)) {
						//teamID==null indicates that this is a new team
						if (teamID==null || !teamID.equalsIgnoreCase(teamView.getTeamID())) {
							result = true;
							break;
						}
					}
				}
			}
		}

		return result;

	}

	private boolean staffExistsInOtherTeam(TAStaffBO staff,
			List<TeamView> currTeams, String teamID) {
		boolean result = false;

		for (TeamView teamView : currTeams) {

			List<TAStaffBO> teamStaff = teamView.getTaStaffList();

			if (teamStaff != null) {
				for (TAStaffBO taStaffBO : teamStaff) {
					if (staff.equals(taStaffBO)) {
						if (!teamID.equalsIgnoreCase(teamView.getTeamID())) {
							result = true;
							break;
						}
					}
				}
			}

		}

		return result;
	}

	private boolean iTAExistsInOtherTeam(ITAExaminerBO staff,
			List<TeamView> currTeams, String teamID) {
		boolean result = false;

		for (TeamView teamView : currTeams) {

			List<ITAExaminerBO> teamPerson = teamView.getItaExaminerList();

			if (teamPerson != null) {
				for (ITAExaminerBO taStaffBO : teamPerson) {
					if (staff.equals(taStaffBO)) {
						if (!teamID.equalsIgnoreCase(teamView.getTeamID())) {
							result = true;
							break;
						}
					}
				}
			}

		}

		return result;
	}

	private boolean policeExistsInOtherTeam(PoliceOfficerBO staff,
			List<TeamView> currTeams, String teamID) {
		boolean result = false;

		for (TeamView teamView : currTeams) {

			List<PoliceOfficerBO> teamPerson = teamView.getPoliceOfficerList();

			if (teamPerson != null) {
				for (PoliceOfficerBO taStaffBO : teamPerson) {
					if (staff.equals(taStaffBO)) {
						if (!teamID.equalsIgnoreCase(teamView.getTeamID())) {
							result = true;
							break;
						}
					}
				}
			}

		}

		return result;
	}

	private boolean jpExistsInOtherTeam(JPBO staff, List<TeamView> currTeams,
			String teamID) {
		boolean result = false;

		for (TeamView teamView : currTeams) {

			List<JPBO> teamPerson = teamView.getJpList();

			if (teamPerson != null) {
				for (JPBO taStaffBO : teamPerson) {
					if (staff.equals(taStaffBO)) {
						if (!teamID.equalsIgnoreCase(teamView.getTeamID())) {
							result = true;
							break;
						}
					}
				}
			}

		}

		return result;
	}

	private boolean vehicleExistsInOtherTeam(TAVehicleBO staff,
			List<TeamView> currTeams, String teamID) {
		boolean result = false;

		for (TeamView teamView : currTeams) {

			List<TAVehicleBO> teamPerson = teamView.getTaVehicleList();

			if (teamPerson != null) {
				for (TAVehicleBO taStaffBO : teamPerson) {
					if (staff.equals(taStaffBO)) {
						if (!teamID.equalsIgnoreCase(teamView.getTeamID())) {
							result = true;
							break;
						}
					}
				}
			}

		}

		return result;
	}

	public boolean vehicleRequired(List<StrategyBO> selectedStrategies) {
		boolean result = false;

		for (StrategyBO strategyBO : selectedStrategies) {
			if (strategyBO.isVehicleRequired()) {
				result = true;
				break;
			}
		}

		return result;

	}

	private boolean arteryRequired(List<StrategyBO> selectedStrategies) {
		boolean result = false;

		for (StrategyBO strategyBO : selectedStrategies) {
			if (strategyBO.isArteryRequired()) {
				result = true;
				break;
			}
		}

		return result;

	}

	private boolean validStrategyDetails(RequestContext context) {
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		DualListModel<StrategyBO> allStrategies = (DualListModel<StrategyBO>) context
				.getFlowScope().get("availableStrategies");
		boolean valid = true;
		// List<StrategyBO> selectedStrategies =
		// roadOpView.getListOfStrategies().getTarget();//allStrategies.getTarget();
		List<StrategyBO> selectedStrategies = (List<StrategyBO>) context
				.getFlowScope().get("listOfSelectedStrategies");
		if (selectedStrategies != null) {
			roadOpView.getListOfStrategies().setTarget(selectedStrategies);
		}

		if (selectedStrategies == null || selectedStrategies.isEmpty()) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("At least one Strategy ").build());

			// return false;
			valid = false;
		}

		// If the available resources that were retrieved from the database do
		// not contain at least the minimum required,do not proceed
		List<StrategyRuleBO> strategyRuleList = (ArrayList<StrategyRuleBO>) context
				.getFlowScope().get("strategyRules");

		int staffListSize = 0;
		int jpListSize = 0;
		int itaExaminerListSize = 0;
		int policeOfficerListSize = 0;
		int vehicleListSize = 0;

		List<StrategyBO> strats = roadOpView.getListOfStrategies().getTarget();

		if (strategyRuleList != null) {
			if (roadOpView.getListOfStaffAux() != null)
				staffListSize = roadOpView.getListOfStaffAux().size();
			if (roadOpView.getListOfJPAux() != null)
				jpListSize = roadOpView.getListOfJPAux().size();
			if (roadOpView.getListOfITAAux() != null)
				itaExaminerListSize = roadOpView.getListOfITAAux().size();
			if (roadOpView.getListOfPoliceAux() != null)
				policeOfficerListSize = roadOpView.getListOfPoliceAux().size();
			if (roadOpView.getListOfVehAux() != null)
				vehicleListSize = roadOpView.getListOfVehAux().size();

			// considering the current road op is excluded from the available
			// resources on W/Services side, I need to count the ones currently
			// savedexcluded the
			List<TeamView> currentTeams = (List<TeamView>) context
					.getFlowScope().get("teamDatatable"); // roadOpView.getTeams();

			List<TAStaffBO> selectedStaff = new ArrayList<TAStaffBO>();
			List<ITAExaminerBO> selectedITA = new ArrayList<ITAExaminerBO>();
			List<JPBO> selectedJP = new ArrayList<JPBO>();
			List<PoliceOfficerBO> selectedPolice = new ArrayList<PoliceOfficerBO>();
			List<TAVehicleBO> selectedVehicles = new ArrayList<TAVehicleBO>();

			if (currentTeams != null) {
				for (TeamView eachTeam : currentTeams) {
					selectedStaff = eachTeam.getTaStaffList();
					if (selectedStaff != null) {
						staffListSize = staffListSize + selectedStaff.size();
					}

					selectedITA = eachTeam.getItaExaminerList();
					if (selectedITA != null) {
						itaExaminerListSize = itaExaminerListSize
								+ selectedITA.size();
					}

					selectedJP = eachTeam.getJpList();
					if (selectedJP != null) {
						jpListSize = jpListSize + selectedJP.size();
					}

					selectedPolice = eachTeam.getPoliceOfficerList();
					if (selectedPolice != null) {
						policeOfficerListSize = policeOfficerListSize
								+ selectedPolice.size();
					}

					selectedVehicles = eachTeam.getTaVehicleList();
					if (selectedVehicles != null) {
						vehicleListSize = vehicleListSize
								+ selectedVehicles.size();
					}

				}

			}

			// /////////////////////////////////////////////////////////////////////////////////////////
			for (StrategyRuleBO rule : strategyRuleList) {

				System.err.println("StrategyRuleInfo: "
						+ rule.getPersonTypeId() + rule.getMinimum());
				if (staffListSize < rule.getMinimum()
						&& rule.getPersonTypeId() == "TA") {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Based on the current Operation Details, there are not enough "
															+ "TA Staff Members Available to carry out this Operation.")
											.build());

					// return false;
					valid = false;
				}

				// if(jpListSize < rule.getMinimum())
				// {
				// context.getMessageContext().
				// addMessage(new
				// MessageBuilder().error().defaultText("Based on the current Operation Details, there are not enough "
				// +
				// "J.P.s Available to carry out this Operation.").build());
				//
				// return false;
				// }

				if (itaExaminerListSize < rule.getMinimum()
						&& rule.getPersonTypeId() == "IE") {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Based on the current Operation Details, there are not enough "
															+ "ITA Examiners Available to carry out this Operation.")
											.build());

					// return false;
					valid = false;
				}

				if (policeOfficerListSize < rule.getMinimum()
						&& rule.getPersonTypeId() == "PO") {
					System.err.println("policeOfficerListSize "
							+ policeOfficerListSize);
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Based on the current Operation Details, there are not enough "
															+ "Police Officers Available to carry out this Operation.")
											.build());
					System.err.println("StrategyRuleInfo: "
							+ rule.getPersonTypeId() + rule.getMinimum()
							+ "policeOfficerListSize " + policeOfficerListSize);

					// return false;
					valid = false;
				}

			}

			for (StrategyBO strat : strats) {
				if (strat.isVehicleRequired() && !(vehicleListSize > 0)) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Based on the current Operation Details, there are not enough "
															+ "Vehicle Available to carry out this Operation.")
											.build());

					// return false;
					valid = false;
				}

			}
		}

		// return true;
	//	System.err.println("validStrategyDetails::"+ valid);
		return valid;
		

	}

	private boolean validOperationDetails(RequestContext context) {
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		String opName = roadOpView.getOperationName();
		String opCategory = roadOpView.getCategoryId();
		List<String> regions = roadOpView.getSelectedRegions();
		Date scheduledStartDtime = roadOpView.getScheduledStartDtime();
		Date scheduledEndDtime = roadOpView.getScheduledEndDtime();

		
		//System.err.println("inside validOperationDetails::" + org.fsl.roms.view.ObjectUtils.objectToString(roadOpView)+"scheduledStartDtime"+scheduledStartDtime+
		//		"  scheduledEndDtime"+ scheduledEndDtime);
		// Removed because the operation name should be optional
		/*
		 * if (!StringUtil.isSet(opName)) {
		 * context.getMessageContext().addMessage( new
		 * MessageBuilder().error().code("RequiredFields")
		 * .arg("Operation Name").build());
		 * 
		 * return false; }
		 */

		boolean valid = true;
		
		try{
		List<StrategyBO> selectedStrategies = (List<StrategyBO>) context
				.getFlowScope().get("listOfSelectedStrategies");
		if (selectedStrategies != null)
			roadOpView.getListOfStrategies().setTarget(selectedStrategies);

		if (!StringUtil.isSet(opCategory)) {

			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("Category").build());
			valid = false;
			// return false;
		}

		if (regions == null || regions.isEmpty()) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("At least one Region").build());
			// return false;
			valid = false;
		}

		// if category is REGional, only one region can be selected and date
		// cannot span more than a day
		if (opCategory!=null && opCategory.equals(Constants.Category.REGIONAL)) {
			if (regions.size() > 1) {
				context.getMessageContext()
						.addMessage(
								new MessageBuilder()
										.error()
										.defaultText(
												"Only one Region May Be Selected for Regional Operations.")
										.build());
				// return false;
				valid = false;
			}

			if (scheduledStartDtime != null && scheduledEndDtime != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				String startDatePortion = (dateFormat
						.format(scheduledStartDtime)).trim();
				String endDatePortion = (dateFormat.format(scheduledEndDtime))
						.trim();

				if (!startDatePortion.equals(endDatePortion)) {
					context.getMessageContext()
							.addMessage(
									new MessageBuilder()
											.error()
											.defaultText(
													"Operations Must Start and End on the Same Day for Regional Operations.")
											.build());
					// return false;
					valid = false;
				}

			}

		}

		if (scheduledStartDtime == null) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("Scheduled Start Date").build());
			// return false;
			valid = false;
		}

		if (scheduledEndDtime == null) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("RequiredFields")
							.arg("Scheduled End Date").build());
			// return false;
			valid = false;
		}

		if (scheduledStartDtime != null && scheduledEndDtime != null) {
			if (scheduledEndDtime.before(scheduledStartDtime)) {
				context.getMessageContext().addMessage(
						new MessageBuilder()
								.error()
								.defaultText(
										"End Date Cannot Be Before Start Date")
								.build());
				// return false;
				valid = false;
			}

			if (scheduledEndDtime.equals((scheduledStartDtime))) {
				context.getMessageContext()
						.addMessage(
								new MessageBuilder()
										.error()
										.defaultText(
												"Start Date and Time Cannot Be Same As End Date and Time")
										.build());
				// return false;
				valid = false;
			}
			
			if (DateUtils.getDateDiff(scheduledStartDtime, scheduledEndDtime, TimeUnit.HOURS) < 1) {
				context.getMessageContext()
						.addMessage(
								new MessageBuilder()
										.error()
										.defaultText(
												"An operation has to have a minimum of ONE (1) hour.")
										.build());
				// return false;
				valid = false;
			}
		}
		
		

		// default
		// return true;
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("error Occurred in add_team");
			addErrorMessage(context, "SystemError");
			valid = false;
		}
		
		//System.err.println("validOperationDetails::"+ valid);
		return valid;
	}

	public Event prepareReviewScreen(RequestContext context) {
		try{
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");
		System.err.println("Prepare Review Screen Called");
		// Location
		locationTreeView = new DefaultTreeNode("Root", null);

		//List<LocationBO> alreadyUsedLocs = new ArrayList<LocationBO>();
		//List<ArteryBO> alreadyUsedArts = new ArrayList<ArteryBO>();

		/*
		 * ArrayList<String> arteryDes = new ArrayList<String>();
		 * ArrayList<Float> arteryDistance = new ArrayList<Float>();
		 * ArrayList<Float> arteryStrtLat = new ArrayList<Float>();
		 * ArrayList<Float> arteryStrtLon = new ArrayList<Float>();
		 * ArrayList<Float> arteryEndLat = new ArrayList<Float>();
		 * ArrayList<Float> arteryEndLon = new ArrayList<Float>();
		 * ArrayList<String> arteryPoints = new ArrayList<String>();
		 */

		List<TeamView> currentTeamsWithDeletes = roadOpView.getTeams();
		List<TeamView> teams = new ArrayList<TeamView>();
		
		if(currentTeamsWithDeletes!= null){
			//TODO Make deleted team functionality more efficient -20150112 @kpowell
			//remove deleted teams from list for processing
			Iterator<TeamView> iterator = currentTeamsWithDeletes.iterator();
			while (iterator.hasNext()) {
				TeamView teamView = (TeamView) iterator.next();
				if(!teamView.isDelete()){
					teams.add(teamView);
				}
			}
		}else{
			//teams = null;
			teams = new ArrayList<TeamView>();
		}
		//
				
				
				
		List<ParishBO> parishBo = new ArrayList<ParishBO>(); //= roadOpView.getOperationParishList();
		List<LocationBO> alreadyUsedLocs = new ArrayList<LocationBO>(); //= roadOpView.getOperationLocationList();
		List<Integer> locationIdsWithOutArt = new ArrayList<Integer>();
		List<ArteryBO> alreadyUsedArts = new ArrayList<ArteryBO>();//;= roadOpView.getOperationArteryList();
		ArteryCriteriaBO arteryCrit = new ArteryCriteriaBO();
		List<ArteryBO> arteriesByLocID = new ArrayList<ArteryBO>();
		
		int arteryExistSize = 0;
		Float tempCoverage = 0.0F;
		
		
		
		/********************************/
		//TODO REDO - kpowell
		for (TeamView teamView : teams) {
			
		if(teamView.isDelete()==false){//only show for team not deleted
			Iterator<LocationBO> iterLoc = teamView.getSelectedTeamLocationList().iterator();
			while(iterLoc.hasNext()){
				LocationBO locationIem = iterLoc.next();				
				if(!alreadyUsedLocs.contains(locationIem)){
					alreadyUsedLocs.add(locationIem);
					System.err.println(locationIem.getLocationDescription()+" doesn't exist");
				}	
				
			}
			
			Iterator<ArteryBO> iterArt = teamView.getSelectedTeamArteryList().iterator();
			while(iterArt.hasNext()){
				ArteryBO arteryItem = iterArt.next();				
				if(!alreadyUsedArts.contains(arteryItem)){
					alreadyUsedArts.add(arteryItem);
					System.err.println(arteryItem.getArteryDescription()+" doesn't exist");
				}
	
			}
			
			System.err.println("selectedTeam.getSelectedTeamParishList()"+ teamView.getSelectedTeamParishList().size());
			Iterator<ParishBO> iterPar = teamView.getSelectedTeamParishList().iterator();
			while(iterPar.hasNext()){
				ParishBO parishIem = iterPar.next();				
				if(!parishBo.contains(parishIem)){
					parishBo.add(parishIem);
					System.err.println(parishIem.getDescription()+" doesn't exist inside OperationParishList");
				}
				
			}
		
		}
		
		}
		/***********************************/
		
		
		HashMap<Integer, LocationBO> selLocations = roadOpView
				.getHashSelectedLocations();

		TreeNode eachlocNode = new DefaultTreeNode();
		StringBuilder arteryDes = new StringBuilder();
		StringBuilder arteryDistance = new StringBuilder();
		StringBuilder arteryStrtLat = new StringBuilder();
		StringBuilder arteryStrtLon = new StringBuilder();
		StringBuilder arteryEndLat = new StringBuilder();
		StringBuilder arteryEndLon = new StringBuilder();
		StringBuilder arteryPoints = new StringBuilder();
		
		
		TreeNode root;
		TreeNode LocationNode;
		TreeNode tempNode;
		TreeNode ArteryNode;
		TreeNode node10;
		TreeNode parishNode;
		
		List<String> locationToBeRemoved = new ArrayList<String>();
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		List<TreeNode> parishNodes = new ArrayList<TreeNode>();
		
		if(parishBo!= null){
		for(ParishBO par : parishBo){
			root = new DefaultTreeNode("Root", null);
			
			 parishNode = new DefaultTreeNode(par.getDescription(), locationTreeView);
	       // TreeNode node1 = new DefaultTreeNode("Node 1", root);
			 parishNodes.add(parishNode);
	        for(LocationBO loc: alreadyUsedLocs){
	        	if(loc.getParishCode().equalsIgnoreCase(par.getParishCode())){ 
	        		LocationNode = new DefaultTreeNode(loc.getShortDesc(), parishNode);
	        		 nodes.add(LocationNode);
	        		System.err.println("create location node"+loc.getShortDesc());
	        		arteryExistSize = 0;
	        		for(ArteryBO ar: alreadyUsedArts){
	        			if(ar.getLocationId().equals(loc.getLocationId())){ 
	        				ArteryNode = new DefaultTreeNode(ar.getShortDescription(), LocationNode);
	        				//adds arteries to map and tree node for arteries selected by Ricardo Thompson Oct. 2014
	        				arteryDes.append(ar.getShortDescription() + ",");
	        				arteryDistance.append(ar.getDistance() + ",");
							arteryStrtLat.append(ar.getStartlatitude() + ",");
							arteryStrtLon.append(ar.getStartlongitude() + ",");
							arteryEndLat.append(ar.getEndlatitude() + ",");
							arteryEndLon.append(ar.getEndlongitude() + ",");
							arteryPoints.append(ar.getPoints()+ ",");
			        		System.err.println("create artery node"+ar.getShortDescription());
			        		arteryExistSize++;
	        			}/*else{
	        				arteryExistSize = 0;
	        			}*/
	        		}
	        		System.err.println("arterySize " + arteryExistSize);
	        		//adds arteries to map and tree node if no arteries were selected by Ricardo Thompson Oct. 2014
	        		if(arteryExistSize == 0){
	        			System.err.println("Art Size " + arteryExistSize + "Row Key " + parishNode.getRowKey());
	        			locationToBeRemoved.add(loc.getShortDesc());
	        			//add new node with all the arteries that werent selected
	        			LocationNode =  new DefaultTreeNode(loc.getShortDesc()+"<span style='color:red;font-size:16px;'> *</span>", parishNode);
	        			LocationNode.setParent(parishNode);
	        					
	        					arteryCrit.setLocationId(loc.getLocationId());
	        					try{
	        						arteriesByLocID = getMaintenanceService().lookupArtery(arteryCrit);
	        					}catch(NoRecordFoundException e){
	        						e.printStackTrace();
	        					}
	        					
	        					for(ArteryBO arT : arteriesByLocID){
	        						if(arT.getLocationId().equals(loc.getLocationId())){ 
	        							ArteryNode = new DefaultTreeNode(arT.getShortDescription(), LocationNode);
	        							arteryDes.append(arT.getShortDescription() + ",");
	        							arteryDistance.append(arT.getDistance() + ",");
	        							arteryStrtLat.append(arT.getStartlatitude() + ",");
	        							arteryStrtLon.append(arT.getStartlongitude() + ",");
	        							arteryEndLat.append(arT.getEndlatitude() + ",");
	        							arteryEndLon.append(arT.getEndlongitude() + ",");
	        							arteryPoints.append(arT.getPoints()+ ",");
	        							tempCoverage += arT.getDistance();
	        						}
	        					}
	        		}
	        	}
	        }
		}
		System.err.println("Nodes Size " + nodes.size());
		//Removes node with existing name
		if(locationToBeRemoved != null || locationToBeRemoved.size() > 0)
		{
			for (TreeNode treeNode : nodes) {
					for(String locName:locationToBeRemoved){
						   if(treeNode.getData().equals(locName)){
						       treeNode.getParent().getChildren().remove(treeNode);
						       System.err.println("Removing Node " + locName);
						   }
					}
				}
		}
		
		/*for (TreeNode parishTreeNode : parishNodes) {
			for (TreeNode treeNode : nodes) {
				for(String locName:locationToBeRemoved){
					   if(treeNode.getData().equals(locName)){
					       treeNode.getParent().getChildren().remove(treeNode);
					       System.err.println("Removing Node " + locName);
					   }
				}
			}
		}*/
		}
		
		/*List<Integer> locWithSelArt = new ArrayList<Integer>();
		
		List<ArteryBO> availableArteries = new ArrayList<ArteryBO>();
		Map<Integer, List<ArteryBO>> mapOfArteries = roadOpView
				.getHashMapOfArteries();
		
		for (Map.Entry<Integer, LocationBO> eachLoc : selLocations.entrySet()) {

			if (!alreadyUsedLocs.contains(eachLoc.getValue())) {
				eachlocNode = new DefaultTreeNode(eachLoc.getValue()
						.getShortDesc(), locationTreeView);

				System.err.println("eachLoc.getValue().getLocationDescription()"+ eachLoc.getValue()
						.getLocationDescription());
				System.err.println("eachLoc.getValue().getLocationDescription()"+ eachLoc.getValue()
						.getShortDesc());
				
				alreadyUsedLocs.add(eachLoc.getValue());

				// Add arteries
				HashMap<Integer, ArteryBO> selArteries = roadOpView
						.getHashSelectedArteries();
				if(selArteries.size() > 0 && selArteries != null ){
					System.out.println("Inside IF " + eachLoc.getValue().getShortDesc());
						for (Map.Entry<Integer, ArteryBO> eachArt : selArteries
								.entrySet()) {
		
							if (eachLoc.getValue().getLocationId()
									.equals(eachArt.getValue().getLocationId())) {
								System.out.println("Inside IF inside For " + eachLoc.getValue().getShortDesc());
								if (!alreadyUsedArts.contains(eachArt.getValue())) {
									System.out.println("Does it contain arteries " + eachLoc.getValue().getShortDesc());
									TreeNode eachArtNode = new DefaultTreeNode(eachArt
											.getValue().getShortDescription(),
											eachlocNode);
									locWithSelArt.add(eachArt.getValue().getLocationId());
									alreadyUsedArts.add(eachArt.getValue());
									System.out.println("Sel Art Location IDs " +eachArt.getValue().getLocationDescription() +" size"+ locWithSelArt);
								if(locWithSelArt.contains(eachLoc.getValue().getLocationId())){	
									System.out.println("if in side IF !" + eachLoc.getValue().getShortDesc());
										arteryDes.append(eachArt.getValue()
												.getShortDescription() + ",");
										arteryDistance.append(eachArt.getValue()
												.getDistance() + ",");
										arteryStrtLat.append(eachArt.getValue()
												.getStartlatitude() + ",");
										arteryStrtLon.append(eachArt.getValue()
												.getStartlongitude() + ",");
										arteryEndLat.append(eachArt.getValue()
												.getEndlatitude() + ",");
										arteryEndLon.append(eachArt.getValue()
												.getEndlongitude() + ",");
										arteryPoints.append(eachArt.getValue().getPoints()
												+ ",");
								}
								
							}
		
							}else{
								System.out.println("inside If else in side IF " + eachLoc.getValue().getShortDesc());
								availableArteries = mapOfArteries.get(eachLoc.getValue().getLocationId());
								if(availableArteries != null){
									for(ArteryBO eachnArt: availableArteries)
									{
										arteryDes.append(eachnArt.getShortDescription() + ",");
										arteryDistance.append(eachnArt.getDistance() + ",");
										arteryStrtLat.append(eachnArt.getStartlatitude() + ",");
										arteryStrtLon.append(eachnArt.getStartlongitude() + ",");
										arteryEndLat.append(eachnArt.getEndlatitude() + ",");
										arteryEndLon.append(eachnArt.getEndlongitude() + ",");
										arteryPoints.append(eachnArt.getPoints()+ ",");
									}
								}
							
							}
						}
				}
			}

		}*/

//		List<TeamView> teams = roadOpView.getTeams();

		int numOfStrategies = 0;
		int numOfLocations = 0;
		int numOfITA = 0;
		int numOfTAStaff = 0;
		int numOfJP = 0;
		int numOfPolice = 0;
		int numOfVeh = 0;
		Float tempCoverageTotal = 0.0F;

		List<StrategyBO> selectedStrategies = (List<StrategyBO>) context
				.getFlowScope().get("listOfSelectedStrategies");

		if (selectedStrategies != null) {
			numOfStrategies = selectedStrategies.size();
		}

		if (teams != null) {
			for (TeamView teamView : teams) {

				if (teamView.getOperationLocationList() != null) {
					numOfLocations = numOfLocations
							+ teamView.getOperationLocationList().size();
					//System.err.println("numOfLocations::"+numOfLocations);
				}

				if (teamView.getItaExaminerList() != null) {
					numOfITA = numOfITA + teamView.getItaExaminerList().size();
				}

				if (teamView.getJpList() != null) {
					numOfJP = numOfJP + teamView.getJpList().size();
				}

				if (teamView.getPoliceOfficerList() != null) {
					numOfPolice = numOfPolice
							+ teamView.getPoliceOfficerList().size();
				}

				if (teamView.getTaStaffList() != null) {
					numOfTAStaff = numOfTAStaff
							+ teamView.getTaStaffList().size();
				}

				if (teamView.getTaVehicleList() != null) {
					numOfVeh = numOfVeh + teamView.getTaVehicleList().size();
				}

				if (teamView.getCoverage() != null) {
					tempCoverageTotal += teamView.getCoverage();
				}

			}
		}

		roadOpView.setNumOfITA(numOfITA);
		roadOpView.setNumOfJP(numOfJP);
		roadOpView.setNumOfLocations(numOfLocations);
		roadOpView.setNumOfStrategies(numOfStrategies);
		roadOpView.setNumOfPolice(numOfPolice);
		roadOpView.setNumOfTAStaff(numOfTAStaff);
		roadOpView.setNumOfVeh(numOfVeh);
		roadOpView.setTotalCoverage(tempCoverageTotal + tempCoverage);

		if (roadOpView.getStatusId().equalsIgnoreCase("NONE")) {
			roadOpView.setStatusDescription("Scheduling");
		}

		/**
		 * added by kpowell Retrieve list of region descriptions also Allow edit
		 * when user region is the same as the operation region
		 */
		List<RegionBO> activeListRegions = (List<RegionBO>) context
				.getFlowScope().get("regionList");
		List<RegionBO> selRegionDescrips = new ArrayList<RegionBO>();
		
		//Check to make sure that office loc code is not null when cancel is pressed
		if(!StringUtils.isBlank(roadOpView.getOfficeLocCode())){
			if(roadOpView.getOfficeLocCode().equalsIgnoreCase(this.getRomsLoggedInUser().getLocationCode()) || getRomsLoggedInUser().hasPermission(
					"ROLE_ROMS_ROAD_OP_SCHEDULE_SPC"))
			{
				//Only allow updates where the operations origin of creation is the same as the current user.
				context.getFlowScope().put("allowUpdate", "true");
			}else{
				
				//address - ROMS1.0-170 - done to prevent Nullpointer exception
				context.getFlowScope().put("allowUpdate", "false");
			}
		}
		
		for (String selregion : roadOpView.getSelectedRegions()) {
			for (RegionBO region : activeListRegions) {
				if (region.getId().equalsIgnoreCase(selregion)) {
					selRegionDescrips.add(region);
				}
			}
		}

		context.getFlowScope().put("selectedRegionList", selRegionDescrips);

		context.getFlowScope().put("listArteryDesc", arteryDes.toString());
		context.getFlowScope().put("listDistance", arteryDistance.toString());
		context.getFlowScope().put("listStrtLat", arteryStrtLat.toString());
		context.getFlowScope().put("listStrtLon", arteryStrtLon.toString());
		context.getFlowScope().put("listEndLat", arteryEndLat.toString());
		context.getFlowScope().put("listEndLon", arteryEndLon.toString());
		context.getFlowScope().put("listPoints", arteryPoints.toString());

		context.getFlowScope().put("operation", roadOpView);

		return success();
		}catch(Exception e){
			e.printStackTrace();
			
			return error();
		}
	}

	public Event authorizeOperation(RequestContext context) {
		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");

		// username and password required
		String authorizedUserName = roadOperation.getAuthorizedUserName();
		String authorizedUserPassword = roadOperation
				.getAuthorizedUserPassword();

		if ((!StringUtil.isSet(authorizedUserName))
				|| (!StringUtil.isSet(authorizedUserPassword))) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error()
							.defaultText("User Name and Password are Required")
							.build());

			return error();
		}

		// ensure user credentials are valid and has permission
		if (StringUtil.isSet(authorizedUserName)
				&& StringUtil.isSet(authorizedUserPassword)) {
			boolean valid = false;
			boolean hasPermisison = false;

			AuthorizationBO authBO = new AuthorizationBO();
			authBO.setUsername(authorizedUserName);
			authBO.setPassword(authorizedUserPassword);
			authBO.setPersonType(Constants.PersonType.TA_STAFF);
			authBO.setPermission(Constants.UserPermissions.ROMS_ROAD_OP_AUTH);

			try {
				valid = getAuthorizationService().validatePerson(authBO);
				hasPermisison = getAuthorizationService().hasPermission(
						authBO);

			} catch (InvalidFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (!valid) {
				/*
				 * context.getMessageContext().addMessage( new
				 * MessageBuilder().error().defaultText(
				 * "Invalid CredentialsDoesName and Password are Required"
				 * ).build());
				 */
				context.getMessageContext()
						.addMessage(
								new MessageBuilder()
										.error()
										.code("AbstractUserDetailsAuthenticationProvider.badCredentials")
										.build());

				return error();
			}

			if (!hasPermisison) {

				// context.getMessageContext().addMessage(new
				// MessageBuilder().error().code("NoPermission").build());
				addErrorMessageText(context,
						"User does not have permission to Authorize Operation");
				return error();
			}
		}

		RoadOperation roadOpService = new RoadOperation();

		roadOpService = getRoadOperationService();

		RoadOperationBO roadOpToSave = new RoadOperationBO();
		roadOpToSave.setRoadOperationId(roadOperation.getRoadOperationId());
		roadOpToSave.setAuthorizedUserName(authorizedUserName);
		roadOpToSave.setCurrentUsername(this.getRomsLoggedInUser()
				.getUsername());

		try {
			roadOpService.authorizeRoadOperation(roadOpToSave);

			context.getMessageContext().addMessage(
					new MessageBuilder()
							.info()
							.defaultText(
									"Road Operation Authorized Successfully")
							.build());
			roadOperation.setAuthorized("Y");
		} catch (ErrorSavingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.getFlowScope().put("errorMsg", "error");
			context.getMessageContext().addMessage(
					new MessageBuilder().error()
							.defaultText("Error: " + e.getMessage()).build());
			return error();

		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.getFlowScope().put("errorMsg", "error");
			context.getMessageContext().addMessage(
					new MessageBuilder().error()
							.defaultText("Error: " + e.getMessage()).build());
			return error();

		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.getFlowScope().put("errorMsg", "error");
			context.getMessageContext().addMessage(
					new MessageBuilder().error()
							.defaultText("Error: " + e.getMessage()).build());
			return error();
		}
		return success();

	}

	public Event saveOperation(RequestContext context) {
		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");

		RoadOperation roadOpService = new RoadOperation();

		//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(roadOperation));
		System.out.println("FUnction saveOperation");

		roadOpService = getRoadOperationService();
		
		//context.getMessageContext().clearMessages();//clear all messages before

		// ////////////////////////
		String fromWhere = (String) context.getFlowScope().get("fromWhere");
		String saveOrUpdate = (String) context.getFlowScope().get(
				"saveOrUpdate");

		RoadOperationBO roadOpToSave = new RoadOperationBO();
		List<AssignedTeamDetailsBO> roadOpTeamsToSave = new ArrayList<AssignedTeamDetailsBO>();
		CourtBO court = new CourtBO();

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// Populate Header Information for Operation
		roadOpToSave.setRoadOperationId(roadOperation.getRoadOperationId());

		// Set Schedule Date and Times

		System.out.println("TOP fromWhere::" + fromWhere);
		System.out.println("TOP saveOrUpdate::" + saveOrUpdate);
		try {

			roadOpToSave.setScheduledStartDate(roadOperation
							.getScheduledStartDtime());

			roadOpToSave.setScheduledEndDate(roadOperation
							.getScheduledEndDtime());

			if (roadOperation.getActualStartDtime() != null) {
				roadOpToSave.setActualStartDate(roadOperation
								.getActualStartDtime());

				roadOpToSave.setActualStartTime(timeFormat.format(roadOperation
						.getActualStartDtime()));
			}

			if (roadOperation.getActualEndDtime() != null) {
				roadOpToSave.setActualEndDate(roadOperation
								.getActualEndDtime());

				roadOpToSave.setActualEndTime(timeFormat.format(roadOperation
						.getActualEndDtime()));
			}

		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Set Strategies

		// roadOpToSave.getOperationStrategyList().addAll(roadOperation.getSelectedStrategies());

		List<StrategyBO> listOfStrats = new ArrayList<StrategyBO>();
		listOfStrats = roadOperation.getListOfStrategies().getTarget();

		StrategyBO roadOpStrat = new StrategyBO();

		for (StrategyBO strategyBO : listOfStrats) {
			roadOpStrat = new StrategyBO();
			strategyBO.setAuditEntryBO(null);

			roadOpStrat.setStrategyId(strategyBO.getStrategyId());

			roadOpToSave.getOperationStrategyList().add(roadOpStrat);

			System.err.println("Strategies before save "
					+ roadOpStrat.getStrategyId());
		}

		// Set Category
		roadOpToSave.setCategoryId(roadOperation.getCategoryId());

		// Set Regions
		List<String> regions = roadOperation.getSelectedRegions();

		for (String string : regions) {
			roadOpToSave.getOfficeLocCodeList().add(string);
		}

		// Set Status
		if (roadOperation.getStatusId() == null) {
			roadOpToSave.setStatusId(roadOperation.getStatusId());
		} else {
			if (!roadOperation.getStatusId().equals("NONE"))
				roadOpToSave.setStatusId(roadOperation.getStatusId());
		}

		if (roadOperation.getCategoryId().equals(Constants.Category.REGIONAL)) {
			// Set court Info
			court.setCourtId(roadOperation.getCourtId());
			roadOpToSave.setCourtBO(court);

			String courttime = timeFormat.format(roadOperation.getCourtDate());

			String courtdate = dateFormat.format(roadOperation.getCourtDate());

			roadOpToSave.setCourtTime(courttime);

			try {
				roadOpToSave.setCourtDate(roadOperation.getCourtDate());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		// Set Current User
		roadOpToSave.setCurrentUsername(this.getRomsLoggedInUser()
				.getUsername());

		// ////////////
		String schedstarttime = timeFormat.format(roadOperation
				.getScheduledStartDtime());
		String schedstartdate = dateFormat.format(roadOperation
				.getScheduledStartDtime());

		// roadOpToSave.setScheduledStartDate(schedstartdate);
		roadOpToSave.setScheduledStartTime(schedstarttime);

		String schedendtime = timeFormat.format(roadOperation
				.getScheduledEndDtime());
		String schedendtdate = dateFormat.format(roadOperation
				.getScheduledEndDtime());

		// roadOpToSave.setScheduledStartDate(schedendtdate);
		roadOpToSave.setScheduledEndTime(schedendtime);

		// set reason and comments - required for cancellation and termination
		roadOpToSave.setComment(roadOperation.getComment());
		roadOpToSave.setReasonId(roadOperation.getReasonId());
		System.err.println("Reason ID " + roadOperation.getReasonId());
		System.err.println("Comment" + roadOperation.getComment());

		roadOpToSave.setOperationName(roadOperation.getOperationName());
		// roadOpToSave.setScheduler(roadOperation.getOperationName());

		// Populate Team Information for Operation
		List<TeamView> teams = roadOperation.getTeams();
		AssignedTeamDetailsBO assignedTeamDet = new AssignedTeamDetailsBO();
		TeamBO teamBO = new TeamBO();

		if (teams != null) {
			for (TeamView teamView : teams) {

				// if this was a newly added team, the ID will not be
				// 'parsable', so dont attempt to
				if (!teamView.getTeamID().startsWith("NEW")) {
					teamBO.setTeamId(Integer.parseInt(teamView.getTeamID()));
				}

				System.err.println("teamId::"+ teamView.getTeamID());
				
				teamBO.setDelete(teamView.isDelete());
				teamBO.setTeamLead(teamView.getTeamLead());
				teamBO.setTeamName(teamView.getTeamName());

				assignedTeamDet.setTeamBO(teamBO);

				if (assignedTeamDet.getItaExaminerList() != null
						&& teamView.getItaExaminerList() != null)
					assignedTeamDet.getItaExaminerList().addAll(
							teamView.getItaExaminerList());
				if (assignedTeamDet.getJpList() != null
						&& teamView.getJpList() != null)
					assignedTeamDet.getJpList().addAll(teamView.getJpList());
				if (assignedTeamDet.getPoliceOfficerList() != null
						&& teamView.getPoliceOfficerList() != null)
					assignedTeamDet.getPoliceOfficerList().addAll(
							teamView.getPoliceOfficerList());
				if (assignedTeamDet.getTaStaffList() != null
						&& teamView.getTaStaffList() != null)
					assignedTeamDet.getTaStaffList().addAll(
							teamView.getTaStaffList());
				if (assignedTeamDet.getTaVehicleList() != null
						&& teamView.getTaVehicleList() != null)
					assignedTeamDet.getTaVehicleList().addAll(
							teamView.getTaVehicleList());
				teamBO.setNoMotorCycleAssigned(teamView.getNumOfMotorCycle());

				List<ArteryBO> arteries = teamView.getOperationArteryList();
				List<LocationBO> locations = teamView
						.getOperationLocationList();
				List<ParishBO> parishes = teamView.getOperationParishList();

				LocationBO opLocation = new LocationBO();
				ArteryBO opArtery = new ArteryBO();
				ParishBO opParish = new ParishBO();

				System.err.println("teamView.getOperationLocationList()::"
						+ teamView.getOperationLocationList().size());
				for (LocationBO locationBO : locations) {

					opLocation = new LocationBO();

					opLocation.setLocationId(locationBO.getLocationId());
					opLocation.setParishCode(locationBO.getParishCode());

					assignedTeamDet.getOperationLocationList().add(opLocation);
				}

				for (ArteryBO arteryBO : arteries) {

					opArtery = new ArteryBO();

					opArtery.setArteryId(arteryBO.getArteryId());
					opArtery.setLocationId(arteryBO.getLocationId());

					assignedTeamDet.getOperationArteryList().add(opArtery);

				}

				// set parishes to save
				for (ParishBO parishBO : parishes) {

					opParish = new ParishBO();

					opParish.setParishCode(parishBO.getParishCode());
					opParish.setDescription(parishBO.getDescription());
					opParish.setOfficeLocationCode(parishBO
							.getOfficeLocationCode());

					assignedTeamDet.getOperationParishList().add(opParish);

				}

				for (LocationBO l : assignedTeamDet
						.getOperationLocationList()) {
					System.err.println("Save location::" + l.getLocationId());
				}
				System.err.println("1Stuff to save " + ":"
						+ assignedTeamDet.getOperationLocationList() + "---"
						+ assignedTeamDet.getItaExaminerList() + "---"
						+ assignedTeamDet.getJpList() + "---"
						+ assignedTeamDet.getPoliceOfficerList() + "---"
						+ assignedTeamDet.getTaStaffList());

				for (TAStaffBO testStaff : assignedTeamDet.getTaStaffList()) {
					System.err.println("TA ::" + testStaff.getFullName() + " ["
							+ testStaff.getPersonId() + "]");

				}

				roadOpTeamsToSave.add(assignedTeamDet);
				assignedTeamDet = new AssignedTeamDetailsBO();
				teamBO = new TeamBO();
			}
		}

		// ////////////////////////
		System.err.println("Stuff to save " + ":"
				+ roadOpToSave.getRoadOperationId() + ":"
				+ roadOpToSave.getStatusId() + ":" + roadOpTeamsToSave + ":"
				+ roadOpToSave.getCategoryId() + ":"
				+ roadOpToSave.getScheduledStartDate() + ":"
				+ roadOpToSave.getScheduledEndDate() + ":"
				+ roadOpToSave.getCurrentUsername() + ":"
				+ roadOpToSave.getOfficeLocCodeList() + ":"
				+ roadOpToSave.getOperationStrategyList() + ":"
				+ roadOpToSave.getCategoryId() + ":"
				+ roadOpToSave.getCourtBO() + ":" + roadOpToSave.getCourtDate()
				+ ":" + roadOpToSave.getCourtTime() + ":"
				+ roadOpToSave.getScheduledStartTime() + ":"
				+ roadOpToSave.getScheduledEndTime());
		/*
		System.err.println("roadOperation.getActualStartDate()::"+ roadOperation.getActualStartDate());
		System.err.println("roadOperation.getActualStartDtime()::"+ roadOperation.getActualStartDtime());
		System.err.println("roadOpToSave.getActualStartDate()::"+ roadOpToSave.getActualStartDate());
		System.err.println("roadOpToSave.getActualStartDtime()::"+ roadOpToSave.getActualStartDtime());
*/
		RoadOperationOtherDetailsBO roadOpOtherDetails = new RoadOperationOtherDetailsBO();
		RoadOperationView newRoadOpView = new RoadOperationView();

		String actionStr = "";
		try {

			RoadOperationBO savedRoadOp = new RoadOperationBO();
			System.err.println("saveOrUpdate::" + saveOrUpdate);
			System.err.println("fromWhere::" + fromWhere);
			/*
			 * if(fromWhere!= null) {
			 */

			// update Operation
			if (saveOrUpdate.equals("update")) {

				System.out.println("Inside Update Operation");
				StringBuilder sb = new StringBuilder();
				// Variable that is set only when cancelled is clicked
				String cancelClicked = (String) context.getFlowScope().get(
						"cancelBtn");

				if (cancelClicked.equals("cancelled")) {
					if (roadOpToSave.getReasonId() == null) {
						System.out.println("Reason");
						sb.append("Reason_");

					}
					if (StringUtils.isEmpty(roadOpToSave.getComment())) {
						System.out.println("Comment");
						sb.append("Comments field_");
					}
					
					System.err.println("Sb length: "+sb.length());
					
					if (sb.length() > 0)// /TODO && road operation is being
										// cancelled
					{

						String[] lines = sb.toString().split("_");
						FacesContext contextFaces = FacesContext
								.getCurrentInstance();
						for (String s : lines) {
							System.out.println("S " + s);
							contextFaces.addMessage("msgCancel",
									new FacesMessage(
											FacesMessage.SEVERITY_ERROR, s
													+ " is required.", s
													+ " is required."));
						}
						return error();

					}
				}

				System.err.println("roadOpToSave.Comment"+ roadOpToSave.getComment());
				System.err.println("roadOpToSave.ReasonId"+ roadOpToSave.getReasonId());
				roadOpService
						.updateRoadOperation(roadOpToSave, roadOpTeamsToSave);

				actionStr = "Updated";
				newRoadOpView = roadOperation;
				// newRoadOpView = convertBOtoView(roadOpToSave);
			}

			/* } */
			else {
				
				//SAVE OPERATION
				roadOpToSave.setStatusId(null);
				System.err.println("before Saving........");
				savedRoadOp = roadOpService.saveRoadOperation(roadOpToSave,
						roadOpTeamsToSave);
				actionStr = "Saved";
				System.err.println("AFter SUcccessful saving......");
				/*
				 * if(fromWhere == null || !saveOrUpdate.equals("update")) {
				 */
				System.out.println("Inside SAVE Operation");
				// newRoadOpView = convertBOtoView(savedRoadOp);
				newRoadOpView = roadOperation;

				newRoadOpView.setStatusDescription("Scheduling");

				// added by kpowell
				newRoadOpView
						.setStatusId(Constants.Status.ROAD_OPERATION_SCHEDULING);
				newRoadOpView.setRoadOperationId(savedRoadOp
						.getRoadOperationId());

				System.err.println("getRoadOperationId::"+ savedRoadOp.getRoadOperationId());
				/* } */
				
				newRoadOpView.setScheduledStartTime(roadOpToSave.getScheduledStartTime());
				newRoadOpView.setScheduledEndTime(roadOpToSave.getScheduledEndTime());
				
				
				
				/*System.err.println("Op Backdated:"+savedRoadOp.getBackDated());
				System.err.println("Op Backdated:"+newRoadOpView.getBackDated());
				System.err.println("Op Authorized:"+savedRoadOp.getAuthorized());
				System.err.println("Op Authorized:"+newRoadOpView.getAuthorized());*/
				
				newRoadOpView.setBackDated(savedRoadOp.getBackDated());
				newRoadOpView.setAuthorized(savedRoadOp.getAuthorized());
				RoadOperationOtherDetailsBO roadOpDet = roadOpService.findRoadOperationOtherDetails(savedRoadOp.getRoadOperationId());
				
				// Populate Team Information for Operation
				List<TeamView> newteams =  newRoadOpView.getTeams();
				List<TeamView> updatedTeams = new ArrayList<TeamView>();
				List<AssignedTeamDetailsBO> newAssignedTeamDet  = roadOpDet.getAssignedTeamDetailsBOList();
				
				if (newteams != null) {
					for (TeamView teamView : newteams) {
						for(AssignedTeamDetailsBO singleTeam : newAssignedTeamDet){
							if(singleTeam.getTeamBO().getTeamName().equalsIgnoreCase(teamView.getTeamName())){
								System.err.println("Team Name matched::"+ singleTeam.getTeamBO().getTeamId().toString());
								teamView.setTeamID(singleTeam.getTeamBO().getTeamId().toString());
								break;
							}
						}
						
						updatedTeams.add(teamView);
						
					}
				}
				
				newRoadOpView.getTeams().clear();
				newRoadOpView.setTeams(updatedTeams);
				
				System.err.println("teamDatatable before::"+ ((List<TeamView>)context.getFlowScope().get("teamDatatable")).size());
				context.getFlowScope().put("teamDatatable", newRoadOpView.getTeams());
				context.getFlowScope().put("resourcesRetrieved", "false");
				
			}

			context.getFlowScope().put("errorMsg", "success");
			context.getMessageContext().addMessage(
					new MessageBuilder()
							.info()
							.defaultText(
									"Road Operation " + actionStr
											+ " Successfully").build());

			context.getFlowScope().put("saveOrUpdate", "update");
			/***
			 * removed by kpowell, only allow update from search and not after
			 * context.getFlowScope().put("fromWhere", "search");
			 */

			if (newRoadOpView.getStatusId().equalsIgnoreCase(
					Constants.Status.ROAD_OPERATION_TERMINATED))
				newRoadOpView.setStatusDescription("Terminated");
			else if (newRoadOpView.getStatusId().equalsIgnoreCase(
					Constants.Status.ROAD_OPERATION_COMPLETED))
				newRoadOpView.setStatusDescription("Completed");
			else if (newRoadOpView.getStatusId().equalsIgnoreCase(
					Constants.Status.ROAD_OPERATION_STARTED))
				newRoadOpView.setStatusDescription("Started");
			else if (newRoadOpView.getStatusId().equalsIgnoreCase(
					Constants.Status.ROAD_OPERATION_CANCELLED))
				newRoadOpView.setStatusDescription("Cancelled");
			else if (newRoadOpView.getStatusId().equalsIgnoreCase(
					Constants.Status.ROAD_OPERATION_CLOSED))
				newRoadOpView.setStatusDescription("Closed");
			else if (newRoadOpView.getStatusId().equalsIgnoreCase(
					Constants.Status.ROAD_OPERATION_NO_ACTION))
				newRoadOpView.setStatusDescription("No Action");

			context.getFlowScope().put("operation", newRoadOpView);

			// roadOperation.setStatusId(Constants.Status.ROAD_OPERATION_SCHEDULING);
			// context.getFlowScope().put("operation",roadOperation);

		} catch (ErrorSavingException e) {

			// context.getMessageContext().addMessage(
			// new
			// MessageBuilder().error().defaultText("Error occurred While Saving. Please Try Again").build());
			context.getFlowScope().put("errorMsg", "error");
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("SystemError").build());

			e.printStackTrace();

			return error();
		} catch (InvalidFieldException e) {
			context.getFlowScope().put("errorMsg", "error");
			context.getMessageContext().addMessage(
					new MessageBuilder().error()
							.defaultText("Error: " + e.getMessage()).build());

			e.printStackTrace();

			return error();
		} catch (RequiredFieldMissingException e) {
			context.getFlowScope().put("errorMsg", "error");
			context.getMessageContext().addMessage(
					new MessageBuilder().error()
							.defaultText("Error: " + e.getMessage()).build());

			e.printStackTrace();
			return error();
		} catch (Exception e) {
			context.getFlowScope().put("errorMsg", "error");
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("SystemError").build());

			e.printStackTrace();

			return error();
		}
		finally
		{
			System.err.println("Inside finally clause for saving.............");
			//context.getFlowScope().put("operation", newRoadOpView);
			
			RoadOperationView operation = (RoadOperationView) context.getFlowScope().get("operation");
			
			if(operation.getReasonId() != null && operation.getReasonId() > 0)
			{
				HashMap<String, String> mapReasonQuery = new HashMap<String, String>();
				mapReasonQuery.put("reason_id", operation.getReasonId().toString());
				
				List<RefCodeBO> refCodes = this.getRefInfo("roms_reason", mapReasonQuery);
				
				if(refCodes.size() > 0)
					operation.setReasonDescription(refCodes.get(0).getDescription());
				
			}
			
			if(operation.getStatusId()== null){
				operation.setStatusId("NONE");
			}
			
			//updated savedValues for comparison
			operation.setSavedScheduledStartDtime(operation.getScheduledStartDtime());
			operation.setSavedScheduledEndDtime(operation.getScheduledEndDtime());
			
			context.getFlowScope().put("savedBackDated", operation.getBackDated());			
			context.getFlowScope().put("savedAuthorized",operation.getAuthorized());
			//
			context.getFlowScope().put("operation", operation);
			
			/*Added by OA 5 March 2015 - If new operation added or changed update cached operations*/
			
			SimpleAsyncTaskExecutor tasks = new SimpleAsyncTaskExecutor();
			tasks.execute(new SaveCachedData(operation.getRoadOperationId()));
	
			/*_____________________________________________________________________________________*/
		
		}

		context.getFlowScope().put("txtCloseStatus", "close");

		System.out.println("TOP fromWhere::" + fromWhere);
		System.out.println("TOP saveOrUpdate::" + saveOrUpdate);
		
		
		

		return success();
	}
	
	
	public Event cancelRoadOp(RequestContext context){
		try
		{
			
		
		RoadOperationBO roadOpToSave = new RoadOperationBO();
		
		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");
						
		roadOpToSave = convertViewToBO(roadOperation);
		
		roadOpToSave.setCurrentUsername(this.getRomsLoggedInUser()
				.getUsername());
		
		if(!validateResonComent(context, roadOpToSave)){
			return error();
		}else{
				RoadOperation roadOpService = this.getRoadOperationService();
				System.err.println("roadOpToSave.Comment"+ roadOpToSave.getComment());
				System.err.println("roadOpToSave.ReasonId"+ roadOpToSave.getReasonId());
				roadOpService
						.cancelRoadOperation(roadOpToSave);
		
				//actionStr = "Updated";
				//newRoadOpView = roadOperation;
				
				context.getFlowScope().put("errorMsg", "success");
				context.getMessageContext().addMessage(
						new MessageBuilder()
								.info()
								.defaultText(
										"Road Operation Updated Successfully").build());			
				
				
				
				//roadOpToSave = roadOpProxy.lookupRoadOperation(roadOperationCriteriaBO)
				
				roadOperation.setStatusDescription(Constants.Status.ROAD_OPERATION_CANCELLED_DESC);
				
				context.getFlowScope().put("operation",roadOperation);
				
		
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return error();
		}
		
		return success();
		
	}
	
	
	public Event terminateRoadOp(RequestContext context){
		try
		{
		RoadOperationBO roadOpToSave = new RoadOperationBO();
		
		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		System.err.println("Greg"+roadOperation.getActualStartDtime() +"Start Date " + roadOperation.getActualStartDtime()+ " Start Time " + roadOperation.getActualStartTime());
		
		//Setting of Actual End Dates
		if(roadOperation.getActualEndDtime() != null){
			roadOperation.setActualEndDtime(roadOperation.getActualEndDtime());
			
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			roadOperation.setActualEndTime(timeFormat.format(roadOperation.getActualEndDtime()));
			
			roadOperation.setActualEndDate(roadOperation.getActualEndDtime().toString());
			System.out.println("ACtaul Date time " + roadOpToSave.getActualEndDate() + "time " + roadOpToSave.getActualEndDtime() + " after " + roadOperation.getActualEndDate() +" time "+ roadOperation.getActualEndTime());
		}
		
		roadOpToSave = convertViewToBO(roadOperation);
		
		roadOpToSave.setCurrentUsername(this.getRomsLoggedInUser()
				.getUsername());
		
		if(!validateResonComent(context, roadOpToSave,Constants.Status.ROAD_OPERATION_TERMINATED)){
			return error();
		}else{
				
				RoadOperation roadOpService = this.getRoadOperationService();
				System.err.println("roadOpToSave.Comment"+ roadOpToSave.getComment());
				System.err.println("roadOpToSave.ReasonId"+ roadOpToSave.getReasonId());
				roadOpService
						.terminateRoadOperation(roadOpToSave);
		
				//actionStr = "Updated";
				//newRoadOpView = roadOperation;
				
				context.getFlowScope().put("errorMsg", "success");
				context.getMessageContext().addMessage(
						new MessageBuilder()
								.info()
								.defaultText(
										"Road Operation Updated Successfully").build());
				
				roadOperation.setStatusDescription(Constants.Status.ROAD_OPERATION_TERMINATED_DESC);
				
				context.getFlowScope().put("operation",roadOperation);
				
				return success();
		}
		
		}catch (InvalidFieldException e) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().defaultText(e.getMessage())
							.build());

			e.printStackTrace();
			return error();
		} catch (Exception e) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().defaultText(e.getMessage())
							.build());

			e.printStackTrace();
			return error();
		} 
	}
	
	
	public boolean validateResonComent(RequestContext context,RoadOperationBO roadOpToSave, String...condition){
		boolean valid = true;
		StringBuilder sb = new StringBuilder();
		System.out.println("DAte time " + roadOpToSave.getActualEndDate() + "Dtime " + roadOpToSave.getActualEndDtime());
		
		if(condition.length > 0 ){
			if(condition[0].equals(Constants.Status.ROAD_OPERATION_TERMINATED)){
				System.err.println("Condition is " + condition[0].toString());
				
				if (roadOpToSave.getActualEndDtime() == null) {
					System.out.println("Actual End Date");
					sb.append("Actual End Date_");
					valid= false;
	
				}
			}

		}
				
		if (roadOpToSave.getReasonId() == null) {
			System.out.println("Reason");
			sb.append("Reason_");
			valid= false;

		}
		if (StringUtils.isEmpty(roadOpToSave.getComment())) {
			System.out.println("Comment");
			sb.append("Comments field_");
			valid= false;
		}
		
		System.err.println("Sb length: "+sb.length());
		
		if (!valid)// /TODO && road operation is being
							// cancelled
		{

			String[] lines = sb.toString().split("_");
			FacesContext contextFaces = FacesContext
					.getCurrentInstance();
			for (String s : lines) {
				System.out.println("S " + s);
				contextFaces.addMessage("msgCancel",
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR, s
										+ " is required.", s
										+ " is required."));
			}	

		}
		
		return valid;
	}
	
	
	

	// public Event viewOneOperationSub(RoadOperationDetailsSumary summary,
	// RequestContext context)
	public Event initializeOperationDetails(RequestContext context) {
		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("theTable");
		RoadOperationView roadOpView = new RoadOperationView();
		setRomsLoggedInUser(getRomsLoggedInUser());
		try {
			if (dataTable != null) {
				System.err.println("Initiate from search");
				RoadOperationDetailsSumary summary = (RoadOperationDetailsSumary) dataTable.pageElements
						.getSelectedRow();

				RoadOperationBO roadOpBO = summary.getRoadOp();
				
				//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(roadOpBO));
				context.getFlowScope().put("tempOperationDetails", roadOpBO);
				context.getFlowScope().put("saveOrUpdate", "update");
				try {

					// RoadOperationView roadOpView;

					roadOpView = convertBOtoView(roadOpBO);
					//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(roadOpView));
					List<StrategyBO> strats = roadOpView
							.getSelectedStrategies();

					context.getFlowScope().put("operation", roadOpView);
					context.getFlowScope().put("teamDatatable",
							roadOpView.getTeams());
					context.getFlowScope().put("listOfSelectedStrategies",
							strats);
					
					

					// carry out necessary formatting for review screen
					prepareReviewScreen(context);
					
					
					
					
					/**/

					if (validateUserStaffMapping(roadOpView, context) == false) {
						return error();
					}

				} catch (NoRecordFoundException e) {
					context.getMessageContext().addMessage(
							new MessageBuilder().error().code("SystemError")
									.build());

					e.printStackTrace();

					return error();

				} catch (RequiredFieldMissingException e) {
					context.getMessageContext().addMessage(
							new MessageBuilder().error().code("SystemError")
									.build());

					e.printStackTrace();

					return error();
				} catch (InvalidFieldException e) {
					context.getMessageContext().addMessage(
							new MessageBuilder().error().code("SystemError")
									.build());

					e.printStackTrace();

					return error();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			} else {
				// RoadOperationView
				roadOpView = new RoadOperationView();
				roadOpView.setStatusId("NONE");

				if (!getRomsLoggedInUser().hasPermission(
						"ROLE_ROMS_ROAD_OP_SCHEDULE_SPC")) {

					List<String> reg = new ArrayList<String>();
					reg.add(getRomsLoggedInUser().getLocationCode());
					roadOpView.setSelectedRegions(reg);
					roadOpView.setCategoryId(Constants.Category.REGIONAL);
					roadOpView
							.setCategoryDescription(Constants.CategoryDesc.REGIONAL);

				} else {
					// Allow edit when user has special permission
					context.getFlowScope().put("allowUpdate", "true");
					System.err.println("allowUpdate is true");
				}

				if (validateUserStaffMapping(roadOpView, context) == false) {
					return error();
				}

				context.getFlowScope().put("operation", roadOpView);
				context.getFlowScope().put("saveOrUpdate", "save");

			}

			// ONLY allow edit when user has special permission or the user
			// region is the same as the operation region
			/*
			 * if(getRomsLoggedInUser().hasPermission(
			 * "ROLE_ROMS_ROAD_OP_SCHEDULE_SPC") ||
			 * romsLoggedInUser.getLocationCode
			 * ().equals(roadOpView.getOfficeLocCode())){
			 * context.getFlowScope().put("allowUpdate", "true"); }
			 */

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("error Occurred in add_team");
			addErrorMessage(context, "SystemError");
		}
		return success();
	}

	public boolean validateUserMapping(RequestContext context){
		return validateUserStaffMapping(new RoadOperationView(), context);
	}
	
	private boolean validateUserStaffMapping(RoadOperationView roadOpView,
			RequestContext context) {
		roadOpView.setValidUserToScheduleOp(true);
		try {
			String username = getRomsLoggedInUser().getUsername();
			TAStaffBO loginTAStaffBO = new TAStaffBO();
			loginTAStaffBO = getStaffUserMappingService().getStaffByUsername(
					username);

			if (loginTAStaffBO == null) {

				addErrorMessageText(context,
						"Login User Name is not mapped to a TA Staff.");
				roadOpView.setValidUserToScheduleOp(false);
				context.getFlowScope().put("operation", roadOpView);
				return false;
			}
			boolean validUsernameStaffRegion = true;

			validUsernameStaffRegion = getStaffUserMappingService()
					.isValidUsernameTAStaffRegionMapping(
							loginTAStaffBO.getStaffId(), username);

			if (validUsernameStaffRegion == false) {
				addErrorMessageText(context,
						"Login User Name Region is different from Staff Region.");
				roadOpView.setValidUserToScheduleOp(false);
				context.getFlowScope().put("operation", roadOpView);
				return false;
			}

		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			addErrorMessageText(context,
					"Login User Name is not mapped to a TA Staff.");
			roadOpView.setValidUserToScheduleOp(false);
			context.getFlowScope().put("operation", roadOpView);
			return false;
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserMappingException e) {
			e.printStackTrace();
			addErrorMessageText(context,
					"Login User Name is not mapped to a TA Staff.");
			roadOpView.setValidUserToScheduleOp(false);
			roadOpView.setStatusId("NONE");
			context.getFlowScope().put("operation", roadOpView);
			return false;
		}
		return true;
	}

	public Event test(TransferEvent event) {
		// HashMap<Integer,ArteryBO> selArteries = (HashMap<Integer,ArteryBO>)
		// context.getFlowScope().get("selectedArteries");
		//
		//
		// System.err.println("@@@@@@@@@@@############^^^^^^^^^^^^^^^^^&&&&&&&&&&&&********************");
		// for (Map.Entry<Integer,ArteryBO> entry : selArteries.entrySet()) {
		//
		//
		// System.err.println("SEL " + entry.getKey() + "*** " +
		// entry.getValue());
		// }

		return success();
	}

	public Event test2(ToggleEvent event) {
		// HashMap<Integer,ArteryBO> selArteries = (HashMap<Integer,ArteryBO>)
		// context.getFlowScope().get("selectedArteries");
		//
		//
		// System.err.println("@@@@@@@@@@@############^^^^^^^^^^^^^^^^^&&&&&&&&&&&&********************");
		// for (Map.Entry<Integer,ArteryBO> entry : selArteries.entrySet()) {
		//
		//
		// System.err.println("SEL " + entry.getKey() + "*** " +
		// entry.getValue());
		// }

		return success();
	}

	public void calculateDuration() {

		RoadOperationView roadOperation = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		Date start = roadOperation.getScheduledStartDtime();
		Date end = roadOperation.getScheduledEndDtime();

		roadOperation.setSchedNumOfDays(getDurationNumOfDays(start, end));
		roadOperation.setSchedNumOfHours(getDurationNumOfHours(start, end));
		roadOperation.setSchedNumOfMinutes(getDurationNumOfMinutes(start, end));
		
		RequestContext requestContext = RequestContextHolder
				.getRequestContext();
		RequestControlContext context = (RequestControlContext) requestContext;
		
		context.getFlowScope().put("resourcesRetrieved", "false");

	}

	
	
	public void regionChangeListener() {

		RequestContext requestContext = RequestContextHolder
				.getRequestContext();
		RequestControlContext context = (RequestControlContext) requestContext;
		
		context.getFlowScope().put("resourcesRetrieved", "false");

	}
	
	
	private int getDurationNumOfDays(Date start, Date end) {
		TimeUnit timeUnit = TimeUnit.HOURS;
		int days = 0;
		int hours = 0;

		if (start != null && end != null) {
			long diffInMillies = end.getTime() - start.getTime();
			int diffInHours = (int) timeUnit.convert(diffInMillies,
					TimeUnit.MILLISECONDS);

			days = (diffInHours / 24);
			hours = diffInHours % 24;
		}
		return days;
	}

	private int getDurationNumOfHours(Date start, Date end) {
		TimeUnit timeUnit = TimeUnit.HOURS;
		int days = 0;
		int hours = 0;

		if (start != null && end != null) {
			long diffInMillies = end.getTime() - start.getTime();
			int diffInHours = (int) timeUnit.convert(diffInMillies,
					TimeUnit.MILLISECONDS);

			days = (diffInHours / 24);
			hours = diffInHours % 24;
		}
		return hours;
	}
	
	private int getDurationNumOfMinutes(Date start, Date end) {
		TimeUnit timeUnit = TimeUnit.MINUTES;

		int minutes = 0;
		
		if (start != null && end != null) {
			long diffInMillies = end.getTime() - start.getTime();
			int diffInMins = (int) timeUnit.convert(diffInMillies,
					TimeUnit.MILLISECONDS);

	
			minutes = diffInMins % 60;
		}
		return minutes;
	}

	/* This function handles all loaded Information into start operation pop-up */
	public Event startOperationLoadPopUp(RequestContext context) {

		System.out.println("Seting Up Start Opertation Pop Up!");

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		RoadOperationBO roadOperationBO = new RoadOperationBO();
		RoadOperationDetailsSumary summary = new RoadOperationDetailsSumary();
		RoadOperationOtherDetailsBO roadOpOtherDetails = new RoadOperationOtherDetailsBO();

		if (dataTable != null)
		{
			summary = (RoadOperationDetailsSumary) dataTable.pageElements
					.getSelectedRow();
			
			context.getFlowScope().put("summary", summary);
		}

		RoadOperation roadOpService = getRoadOperationService();

		MessageContext messages = context.getMessageContext();

		try {
				
				if (dataTable != null) 
				{
					roadOpOtherDetails = roadOpService
							.findRoadOperationOtherDetails(summary.getRoadOp()
									.getRoadOperationId());
					
					roadOperationBO = summary.getRoadOp();
					
					//added by kpowell- fix issue with operation not in flowscope
					roadOpView = convertBOtoView(summary.getRoadOp());
					context.getFlowScope().put("operation", roadOpView);
					//
					
				} 
				else 
				{
					roadOpOtherDetails = roadOpService
							.findRoadOperationOtherDetails(roadOpView
									.getRoadOperationId());
	
					roadOperationBO = convertViewToBO(roadOpView);
					
					
					//added by kpowell- fix issue with operation not in flowscope
					roadOpView = convertBOtoView(summary.getRoadOp());
					context.getFlowScope().put("operation", roadOpView);
					//
				}
	
				DataTableMemory dataTableTeam = new DataTableMemory(
						roadOpOtherDetails.getAssignedTeamDetailsBOList(), 1,
						roadOpOtherDetails.getAssignedTeamDetailsBOList().size(),
						"Teams");
	
				dataTableTeam
						.setCurrentPageHeader(roadOpOtherDetails
								.getAssignedTeamDetailsBOList().size() > 0 ? roadOpOtherDetails
								.getAssignedTeamDetailsBOList().get(0).getTeamBO()
								.getTeamName()
								: "");
	
				context.getFlowScope().put("dataTableTeam", dataTableTeam);
				
				context.getFlowScope().put("roadOpBO", roadOperationBO);
				
				this.setOperationActualTime(roadOperationBO);	
				
				return success();

		} 
		catch (NoRecordFoundException exe) 
		{
			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("Norecordsfound").build());

			return error();

		} catch (Exception e) {

			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("SystemError").build());

			e.printStackTrace();

			return error();

		}

	}

	/*
	 * This function handles all loaded Information into start operation pop-up.
	 * This is used with road operation calendar which has now table in its flow
	 * scope
	 */
	public Event startOperationLoadPopUp(RequestContext context,
			Integer roadOperationId) {

//		System.err.println("inside startOperationLoadPopUp");
		System.out.println("Seting Up Start Opertation Pop Up for Calendar!");

		DataTableMemory dataTable = (DataTableMemory) context.getFlowScope()
				.get("dataTable");

		RoadOperation roadOpService = this.getRoadOperationService();

		MessageContext messages = context.getMessageContext();

		RoadOperationBO roadOpBO = null;

		try {
			// RoadOperationOtherDetailsBO roadOpOtherDetails =
			// roadOpProxy.findRoadOperationOtherDetails(roadOperationId);

			RoadOperationOtherDetailsBO roadOpOtherDetails = roadOpService
					.findRoadOperationOtherDetails(roadOperationId);

			roadOpBO = (RoadOperationBO) context.getFlowScope().get("roadOp");

			if (roadOpBO == null) {
				/*
				 * If Start operation popup is called from review road operation
				 * the roadOpBO needs to be created
				 */
				RoadOperationView operation = (RoadOperationView) context
						.getFlowScope().get("operation");
				roadOpBO = this.convertViewToBO(operation);
			}

			DataTableMemory dataTableTeam = new DataTableMemory(
					roadOpOtherDetails.getAssignedTeamDetailsBOList(), 1,
					roadOpOtherDetails.getAssignedTeamDetailsBOList().size(),
					"Teams");

			dataTableTeam
					.setCurrentPageHeader(roadOpOtherDetails
							.getAssignedTeamDetailsBOList().size() > 0 ? roadOpOtherDetails
							.getAssignedTeamDetailsBOList().get(0).getTeamBO()
							.getTeamName()
							: "");

			context.getFlowScope().put("roadOpBO", roadOpBO);
			
			context.getFlowScope().put("dataTableTeam", dataTableTeam);
			
			this.setOperationActualTime(roadOpBO);

		} catch (NoRecordFoundException exe) {
			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("Norecordsfound").build());

			return error();

		} catch (Exception e) {

			dataTable = new DataTableMemory(
					new ArrayList<RoadOperationDetailsSumary>(), 10);
			context.getFlowScope().put("dataTable", dataTable);

			messages.addMessage(new MessageBuilder().error()
					.code("SystemError").build());

			e.printStackTrace();

			return error();
		}


		return success();

	}

	private void setOperationActualTime(RoadOperationBO roadOperationBO) throws DatatypeConfigurationException{
		/*UR-052: Set Actual Operation start time to the current system time where the operation actual start time has not been set*/
		Date currentDate = Calendar.getInstance().getTime();
		
		if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING)){
			
			roadOperationBO.setActualStartDate(currentDate);
		}
		
		
		if(roadOperationBO.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED)){
	
				
			roadOperationBO.setActualEndDate(currentDate);

			this.calculateActualDuration();
		}
	}
	private RoadOperationBO convertViewToBO(RoadOperationView view) {
		RoadOperationBO roadOperationBO = new RoadOperationBO();

		try {

			roadOperationBO.setRoadOperationId(view.getRoadOperationId());

			if (view.getActualEndDate() != null) {
				roadOperationBO.setActualEndDate(view.getActualEndDtime());
				roadOperationBO.setActualEndDtime(view.getActualStartDtime());
			} else {
				Date dateToSet = view.getActualEndDtime() != null ? view.getActualEndDtime()
						: null;
				roadOperationBO.setActualEndDate(dateToSet);
				roadOperationBO.setActualEndDtime(dateToSet);
			}

			roadOperationBO.setActualEndTime(view.getActualEndTime());

			if (view.getActualStartDtime() != null) {
				roadOperationBO.setActualStartDate(view
						.getActualStartDtime());
				roadOperationBO.setActualStartDtime(view
						.getActualStartDtime());
			} else {
				Date dateToSet = view.getActualStartDtime() != null ? view.getActualStartDtime()
						: null;
				roadOperationBO.setActualStartDate(dateToSet);
				roadOperationBO.setActualStartDtime(dateToSet);
			}

			roadOperationBO.setActualStartTime(view.getActualStartTime());
			roadOperationBO.setAuthorized(view.getAuthorized());
			roadOperationBO
					.setAuthorizedDtime(view.getAuthorizedDtime() != null ? view.getAuthorizedDtime()
							: null);
			roadOperationBO.setAuthorizedUserName(view.getAuthorizedUserName());
			roadOperationBO.setBackDated(view.getBackDated());
			roadOperationBO.setCategoryDescription(view
					.getCategoryDescription());
			roadOperationBO.setCategoryId(view.getCategoryId());
			roadOperationBO.setComment(view.getComment());
			roadOperationBO.setCourtBO(view.getCourtBO());
			roadOperationBO
					.setCourtDate(view.getCourtDate() != null ? view.getCourtDate()
							: null);
			roadOperationBO
					.setCourtDTime(view.getCourtDate() != null ? view.getCourtDate()
							: null);

			if (view.getCourtTime() != null) {
				roadOperationBO.setCourtTime(view.getCourtTime());

			} else if (roadOperationBO.getCourtDTime() != null) {
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				roadOperationBO.setCourtTime(timeFormat.format(roadOperationBO
						.getCourtDTime()));

			}

			roadOperationBO.setCurrentUsername(view.getCurrentUsername());
			// roadOperationBO.setDateCreated(view)
			roadOperationBO.setOfficeLocCode(view.getOfficeLocCode());
			roadOperationBO.setOperationName(view.getOperationName());
			roadOperationBO.setReasonId(view.getReasonId());
			roadOperationBO.setRoadOperationId(view.getRoadOperationId());
			roadOperationBO
					.setScheduledEndDate(view.getScheduledEndDtime() != null ? view
									.getScheduledEndDtime() : null);
			roadOperationBO
					.setScheduledEndDtime(view.getScheduledEndDtime() != null ? view
									.getScheduledEndDtime() : null);
			roadOperationBO.setScheduledEndTime(view.getScheduledEndTime());
			roadOperationBO
					.setScheduledStartDate(view.getScheduledStartDtime() != null ? view
									.getScheduledStartDtime() : null);
			roadOperationBO.setScheduledStartDtime(view
					.getScheduledStartDtime() != null ? view.getScheduledStartDtime()
					: null);
			roadOperationBO.setScheduledStartTime(view.getScheduledStartTime());

			TAStaffBO scheduler = new TAStaffBO();
			scheduler.setStaffId(view.getSchedulerStaffId());
			roadOperationBO.setScheduler(scheduler);

			roadOperationBO.setStatusDescription(view.getStatusDescription());
			roadOperationBO
					.setStatusDtime(view.getStatusDtime() != null ? view.getStatusDtime()
							: null);
			roadOperationBO.setStatusId(view.getStatusId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return roadOperationBO;
	}

	
	/**
	 * This method return a Integer List of TaStaff Id from the TaStaff List passed
	 * @param 
	 * @return
	 */
	private List<Integer> returnIntegerTaStaffList(List<TAStaffBO> taStaffList){
List<Integer> integerList = null;
		
		if(taStaffList != null){
			integerList = new ArrayList<Integer>();
		for(TAStaffBO taStaff : taStaffList){
			integerList.add(taStaff.getPersonId());
		}
		}
		return integerList;
	}
	
	/**
	 * This method return a Integer List of TaStaff Id from the TaStaff List passed
	 * @param 
	 * @return
	 */
	private List<Integer> returnIntegerITAList(List<ITAExaminerBO> itaExaminerList){
		List<Integer> integerList = null;
		
		if(itaExaminerList != null){
			integerList = new ArrayList<Integer>();
			
			for(ITAExaminerBO ita : itaExaminerList){
			integerList.add(ita.getPersonBO().getPersonId());
		}
		}
		return integerList;
	}
	
	/**
	 * This method return a Integer List of TaStaff Id from the TaStaff List passed
	 * @param 
	 * @return
	 */
	private List<Integer> returnIntegerPoliceList(List<PoliceOfficerBO> policeList){
		List<Integer> integerList = null;
		
		if(policeList != null){
			integerList = new ArrayList<Integer>();
			for(PoliceOfficerBO police : policeList){
				integerList.add(police.getPersonID());
			}
		}
		return integerList;
	}
	
	/**
	 * This method return a Integer List of TaStaff Id from the TaStaff List passed
	 * @param 
	 * @return
	 */
	private List<Integer> returnIntegerJPList(List<JPBO> jpList){
List<Integer> integerList = null;
		
		if(jpList != null){
			integerList = new ArrayList<Integer>();
		for(JPBO jp : jpList){
			integerList.add(jp.getPersonBO().getPersonId());
		}
		}
		return integerList;
	}
	
	/**
	 * This method return a Integer List of TaStaff Id from the TaStaff List passed
	 * @param 
	 * @return
	 */
	private List<Integer> returnIntegerVehicleList(List<TAVehicleBO> taVehicleList){
List<Integer> integerList = null;
		
		if(taVehicleList != null){
			integerList = new ArrayList<Integer>();
		for(TAVehicleBO taVehicle : taVehicleList){
			integerList.add(taVehicle.getVehicle().getVehicleId());
		}
		}
		return integerList;
	}
	private RoadOperationView convertBOtoView(RoadOperationBO roadOpBO)
			throws InvalidFieldException,
			NoRecordFoundException,
			RequiredFieldMissingException,
			NoRecordFoundException,
			RequiredFieldMissingException {
		RoadOperationView roadOpView = new RoadOperationView();
		try {

//			System.err.println("inside convertBOtoView()");
			RoadOperation roadOpService = getRoadOperationService();
			RequestContext context = RequestContextHolder.getRequestContext();
			Maintenance maintenanceService = getMaintenanceService();

			/**************************************************************/
			roadOpView.setRoadOperationId(roadOpBO.getRoadOperationId());
			roadOpView.setOperationName(roadOpBO.getOperationName());
			roadOpView.setCategoryId(roadOpBO.getCategoryId());
			roadOpView
					.setCategoryDescription(roadOpBO.getCategoryDescription());
			roadOpView.setStatusId(roadOpBO.getStatusId());
			roadOpView.setAuthorized(roadOpBO.getAuthorized());
			roadOpView.setBackDated(roadOpBO.getBackDated());

			RoadOperationOtherDetailsBO roadOpOtherDetails = new RoadOperationOtherDetailsBO();

			roadOpOtherDetails = roadOpService
					.findRoadOperationOtherDetails(roadOpView
							.getRoadOperationId());

			List<StrategyBO> opStrategies = roadOpOtherDetails
					.getOperationStrategyList(); // TODO - confirm this. there
													// is also one on the
													// operation itself

			
			
			roadOpView.setSelectedStrategies(new ArrayList<StrategyBO>());

			for (StrategyBO strategyBO : opStrategies) {

				StrategyBO mainStratBO = new StrategyBO();

				mainStratBO.setStrategyId(strategyBO.getStrategyId());
				mainStratBO.setStrategyDescription(strategyBO
						.getStrategyDescription());
				
				//kpowell
				//populate the other fields on the object to address areas where
				//values where set to NULL
				mainStratBO.setVehicleRequired(strategyBO.isVehicleRequired());
				mainStratBO.setArteryRequired(strategyBO.isArteryRequired());
				mainStratBO.setStatusDescription(strategyBO.getStatusDescription());
				mainStratBO.setStatusId(strategyBO.getStatusId());

				roadOpView.getSelectedStrategies().add(mainStratBO);
			}
//			System.err.println("SelectedStrategies"+ roadOpView.getSelectedStrategies().size());

			// Regions
			roadOpView.setSelectedRegions(new ArrayList<String>());

			List<String> regionsSelected = roadOpOtherDetails
					.getOperationRegionList();

			if (regionsSelected != null) {
				roadOpView.getSelectedRegions().addAll(regionsSelected); // add
																			// region
																			// (a.k.a.
																			// office
																			// location
																			// code)
				/*HashMap<String, String> criteria = new HashMap<String, String>();
				for(String region : regionsSelected){
					criteria.put("loc_code", region);
				}
				List<RefCodeBO> listRegions = super.getRefInfo(
						"roms_lmis_ta_office_location_view", criteria);
				*/
				/*List<RegionBO> selectListRegions = new ArrayList<RegionBO>();

				RegionBO regionBo = new RegionBO();

				if (listRegions != null) {

					for (RefCodeBO refCode : listRegions) {

						regionBo.setId(refCode.getCode());
						regionBo.setDescription(refCode.getDescription());
						selectListRegions.add(regionBo);
						regionBo = new RegionBO();

					}
				}*/
				//context.getFlowScope().put("regionList", selectListRegions);
				//return success();

			}

			// Strategies
			if (roadOpView.getListOfStrategies() == null) {
				roadOpView.setListOfStrategies(new DualListModel<StrategyBO>());
			}
			roadOpView.getListOfStrategies().setTarget(
					roadOpView.getSelectedStrategies());

			roadOpView.setListOfStaff(new DualListModel<TAStaffBO>(
					new ArrayList<TAStaffBO>(), new ArrayList<TAStaffBO>()));
			roadOpView
					.setListOfVehicles(new DualListModel<TAVehicleBO>(
							new ArrayList<TAVehicleBO>(),
							new ArrayList<TAVehicleBO>()));
			roadOpView.setListOfITA(new DualListModel<ITAExaminerBO>(
					new ArrayList<ITAExaminerBO>(),
					new ArrayList<ITAExaminerBO>()));
			roadOpView.setListOfPolice(new DualListModel<PoliceOfficerBO>(
					new ArrayList<PoliceOfficerBO>(),
					new ArrayList<PoliceOfficerBO>()));
			roadOpView.setListOfJP(new DualListModel<JPBO>(
					new ArrayList<JPBO>(), new ArrayList<JPBO>()));

			List<AssignedTeamDetailsBO> assignedTeams = roadOpOtherDetails
					.getAssignedTeamDetailsBOList();
			TeamView teamView = new TeamView();

			if(roadOpView.getOperationLocationList()== null){
				roadOpView.setOperationLocationList(new ArrayList<LocationBO>());
				roadOpView.setOperationArteryList(new ArrayList<ArteryBO>());
				roadOpView.setOperationParishList(new ArrayList<ParishBO>());
				roadOpView.setSelectedArteryList(new ArrayList<ArteryBO>());
				roadOpView.setSelectedParishList(new ArrayList<ParishBO>());
				roadOpView.setSelectedLocationList(new ArrayList<LocationBO>());
				
			}
			
			for (AssignedTeamDetailsBO assignedTeamDetailsBO : assignedTeams) {
				System.err.println("Is there a team ID?"
						+ assignedTeamDetailsBO.getTeamBO().getTeamId());

				teamView.setTeamIDNumber(assignedTeamDetailsBO.getTeamBO()
						.getTeamId());

				teamView.setTeamName(assignedTeamDetailsBO.getTeamBO()
						.getTeamName());
				teamView.setTeamLead(assignedTeamDetailsBO.getTeamBO()
						.getTeamLead());
				teamView.setTeamID(""
						+ assignedTeamDetailsBO.getTeamBO().getTeamId());
				teamView.setTeamName(assignedTeamDetailsBO.getTeamBO()
						.getTeamName());

				teamView.setTaVehicleList(assignedTeamDetailsBO
						.getTaVehicleList());
				teamView.setTaStaffList(assignedTeamDetailsBO.getTaStaffList());
				teamView.setJpList(assignedTeamDetailsBO.getJpList());
				teamView.setItaExaminerList(assignedTeamDetailsBO
						.getItaExaminerList());
				teamView.setPoliceOfficerList(assignedTeamDetailsBO
						.getPoliceOfficerList());
				
				//kpowell:2015-01-15
				//initialize IDLists to be used in the validateStrategy function
				teamView.setListOfSelectedVehID(org.springframework.util.StringUtils.collectionToDelimitedString(returnIntegerVehicleList(assignedTeamDetailsBO
						.getTaVehicleList()), ","));
				teamView.setListOfSelectedStaffID(org.springframework.util.StringUtils.collectionToDelimitedString(returnIntegerTaStaffList(assignedTeamDetailsBO
						.getTaStaffList()), ","));
				teamView.setListOfSelectedJPID(org.springframework.util.StringUtils.collectionToDelimitedString(returnIntegerJPList(assignedTeamDetailsBO
						.getJpList()), ","));
				teamView.setListOfSelectedITAID(org.springframework.util.StringUtils.collectionToDelimitedString(returnIntegerITAList(assignedTeamDetailsBO
						.getItaExaminerList()), ","));
				teamView.setListOfSelectedPoliceID(org.springframework.util.StringUtils.collectionToDelimitedString(returnIntegerPoliceList(assignedTeamDetailsBO
						.getPoliceOfficerList()), ","));
				
				
				//
				teamView.setNumOfITAExaminer(assignedTeamDetailsBO
						.getItaExaminerList().size());
				teamView.setNumOfJP(assignedTeamDetailsBO.getJpList().size());
				teamView.setNumOfPolice(assignedTeamDetailsBO
						.getPoliceOfficerList().size());
				teamView.setNumOfTAStaff(assignedTeamDetailsBO.getTaStaffList()
						.size());
				teamView.setNumOfVehicle(assignedTeamDetailsBO
						.getTaVehicleList().size());
				teamView.setNumOfMotorCycle(assignedTeamDetailsBO.getTeamBO()
						.getNoMotorCycleAssigned());
				
				teamView.setNumOfMotorCycleUsed(assignedTeamDetailsBO.getTeamBO().getNoMotorCycleUsed());

				System.err
						.println("Num of motor cysles from assigned team details "
								+ assignedTeamDetailsBO.getTeamBO()
										.getNoMotorCycleAssigned());

				// //////////////////////Set
				// Arteries/////////////////////////////////////////
				List<ArteryBO> listOfArteries = assignedTeamDetailsBO
						.getOperationArteryList();

				ArteryBO maintArtery = new ArteryBO();

				List<ArteryBO> tempArteryList = new ArrayList<ArteryBO>();
				teamView.setOperationArteryList(tempArteryList);

				List<ArteryBO> mainArteryLst = new ArrayList<ArteryBO>();
				Float tempCoverage = 0.0F;
				
				/**2014-10-27-REDO***/
				teamView.setOperationLocationList(new ArrayList<LocationBO>());
				teamView.setOperationArteryList(new ArrayList<ArteryBO>());
				teamView.setOperationParishList(new ArrayList<ParishBO>());
				teamView.setSelectedTeamArteryList(new ArrayList<ArteryBO>());
				teamView.setSelectedTeamLocationList(new ArrayList<LocationBO>());
				teamView.setSelectedTeamParishList(new ArrayList<ParishBO>());
				
				/*********/
				for (ArteryBO arteryBO : listOfArteries) {
//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(arteryBO));
					arteryBO.setAuditEntryBO(null);
					maintArtery = new ArteryBO();

					maintArtery.setArteryId(arteryBO.getArteryId());
					maintArtery.setArteryDescription(arteryBO
							.getArteryDescription());
					maintArtery.setLocationId(arteryBO.getLocationId());
					maintArtery.setDistance(arteryBO.getDistance());
					maintArtery.setShortDescription(arteryBO
							.getShortDescription());
					maintArtery.setLocationDescription(arteryBO
							.getLocationDescription());
					maintArtery.setParishDescription(arteryBO
							.getParishDescription());
					maintArtery.setStartlatitude(arteryBO.getStartlatitude());
					maintArtery.setStartlongitude(arteryBO.getStartlongitude());
					maintArtery.setEndlatitude(arteryBO.getEndlatitude());
					maintArtery.setEndlongitude(arteryBO.getEndlongitude());
					maintArtery.setPoints(arteryBO.getPoints());
					mainArteryLst.add(maintArtery);
					tempCoverage += maintArtery.getDistance();
					roadOpView.getHashSelectedArteries().put(
							arteryBO.getArteryId(), maintArtery);
				}
				tempArteryList.addAll(mainArteryLst);
				teamView.setOperationArteryList(tempArteryList);
				
				System.err.println("mainArteryLst"+ mainArteryLst.size());
				System.err.println("tempArteryList"+ tempArteryList.size());
				/**2014-10-27-REDO new- kpowell**/
				teamView.getSelectedTeamArteryList().addAll(tempArteryList);
				roadOpView.getSelectedArteryList().addAll(teamView.getSelectedTeamArteryList());
				roadOpView.getOperationArteryList().addAll(teamView.getSelectedTeamArteryList());
				/******/
				
				System.err
						.println("Total Team Coverage in convertBOtoViewMethod "
								+ tempCoverage);
				teamView.setCoverage(tempCoverage);

				System.err.println("oooooooooo A "
						+ teamView.getOperationArteryList());

				// ///////////////////////////////Set
				// Locations/////////////////////////////////////////
				List<LocationBO> teamLocations = assignedTeamDetailsBO
						.getOperationLocationList();
				String parishCode;
				ParishBO parishBO = new ParishBO();
				ParishCriteriaBO parishCriteria = new ParishCriteriaBO();
				List<ParishBO> parishes;
				LocationBO maintLocation = new LocationBO();
				List<LocationBO> tempLocationList = new ArrayList<LocationBO>();
				teamView.setOperationLocationList(tempLocationList);
				List<ArteryBO> hashArteries = new ArrayList<ArteryBO>();
				
				List<Integer> teamLocIds = new ArrayList<Integer>();
				for(LocationBO teamLoc : teamLocations){
					teamLocIds.add(teamLoc.getLocationId());
				//	System.err.println("teamLoc.getLocationId():"+teamLoc.getLocationId());
				}
				// Set Locations
				for (LocationBO locationBO : teamLocations) {

					maintLocation = new LocationBO();

					maintLocation.setLocationId(locationBO.getLocationId());
					maintLocation.setLocationDescription(locationBO
							.getLocationDescription());
					maintLocation.setShortDesc(locationBO
							.getShortDesc());
					
					maintLocation.setParishCode(locationBO.getParishCode());

					//updated by kpowell 2014-05-21
					//if (!teamView.getOperationLocationList().contains(
					//		maintLocation)) {
					if(!teamLocIds.contains(maintLocation.getLocationId())){
						teamView.getOperationLocationList().add(maintLocation);
						//System.err.println("maintLocation"+ maintLocation.getLocationDescription()+maintLocation.getLocationId() );
					}

					tempLocationList.add(maintLocation);

					roadOpView.getHashSelectedLocations().put(
							locationBO.getLocationId(), maintLocation);

					// added by kpowell
					hashArteries = new ArrayList<ArteryBO>();
					for (ArteryBO arteryBO : tempArteryList) {
						if (arteryBO.getLocationId().equals(
								maintLocation.getLocationId())) {
							arteryBO.setSelected(true);
							hashArteries.add(arteryBO);
						}
					}

					teamView.getHashMapOfTeamArteries().put(
							maintLocation.getLocationId(), hashArteries);
					//

				}
				teamView.setOperationLocationList(tempLocationList);
				
				/****2014-10-27-REDO*****new- kpowell********/
				teamView.getSelectedTeamLocationList().addAll(tempLocationList);
				roadOpView.getSelectedLocationList().addAll(teamView.getSelectedTeamLocationList());
				roadOpView.getOperationLocationList().addAll(teamView.getSelectedTeamLocationList());
				/******/
				System.err.println("Get SelectedTeamLocationList"+teamView.getTeamName()+"  "+ teamView.getSelectedTeamLocationList().size());

				System.err.println("oooooooooo L "
						+ teamView.getOperationLocationList());
				// /////////////////////////////////////////////////////////////////////////////////////////////////////////

				// Set Parishes
				teamView.setOperationParishList(new ArrayList<ParishBO>());
				List<ParishBO> seletedParishes = assignedTeamDetailsBO
						.getOperationParishList();
				ParishBO maintParish = new ParishBO();
				List<ParishBO> tempParishList = new ArrayList<ParishBO>();
				List<LocationBO> hashLocations = new ArrayList<LocationBO>();

				for (ParishBO parishBO2 : seletedParishes) {

					maintParish = new ParishBO();
					maintParish.setParishCode(parishBO2.getParishCode());
					maintParish.setDescription(parishBO2.getDescription());
					maintParish.setOfficeLocationCode(parishBO2
							.getOfficeLocationCode());
					maintParish.setOfficeLocationDescription(parishBO2
							.getOfficeLocationDescription());
					maintParish.setSelected(true);
					teamView.getOperationParishList().add(maintParish); // add
																		// parish
					roadOpView.getHashSelectedParishes().put(
							maintParish.getParishCode(), maintParish);
					teamView.getHashMapOfTeamParishes().put(
							parishBO2.getParishCode(), maintParish);

					// added by kpowell
					hashLocations = new ArrayList<LocationBO>();
					for (LocationBO locationBO : tempLocationList) {
						if (locationBO.getParishCode().equals(
								parishBO2.getParishCode())) {
							locationBO.setSelected(true);
							hashLocations.add(locationBO);
						}
					}

					teamView.getHashMapOfTeamLocations().put(
							parishBO2.getParishCode(), hashLocations);
					//
					tempParishList.add(maintParish);
				}

				teamView.setOperationParishList(tempParishList);
				
				/**2014-10-27-REDO ****new- kpowell**/
				teamView.getSelectedTeamParishList().addAll(tempParishList);
				roadOpView.getSelectedParishList().addAll(teamView.getSelectedTeamParishList());
				roadOpView.getOperationParishList().addAll(teamView.getSelectedTeamParishList());
				/******/
				
				
				// /////////////////////////////////////////////////////////////////////////////////////////////////////////
				// System.err.println("Initialize Team Object HashParish"+
				// teamView.getHashMapOfTeamParishes().size());
				// System.err.println("Initialize Team Object HashLocations"+
				// teamView.getHashMapOfTeamLocations().size());
				// System.err.println("Initialize Team Object HashArtery"+
				// teamView.getHashMapOfTeamArteries().size());
				if (roadOpView.getTeams() == null) {
					roadOpView.setTeams(new ArrayList<TeamView>());
				}

				roadOpView.getTeams().add(teamView);

				System.err.println("setting all targets on the ListOf**");
				roadOpView.getListOfStaff().getTarget()
						.addAll(teamView.getTaStaffList());
				roadOpView.getScheduledStaff().addAll(teamView.getTaStaffList());
				
				roadOpView.getListOfVehicles().getTarget()
						.addAll(teamView.getTaVehicleList());
				roadOpView.getScheduledVehicles().addAll(teamView.getTaVehicleList());
				
				roadOpView.getListOfITA().getTarget()
						.addAll(teamView.getItaExaminerList());
				roadOpView.getScheduledITA().addAll(teamView.getItaExaminerList());
				
				roadOpView.getListOfPolice().getTarget()
						.addAll(teamView.getPoliceOfficerList());
				roadOpView.getScheduledPolice().addAll(teamView.getPoliceOfficerList());
								
				roadOpView.getListOfJP().getTarget()
						.addAll(teamView.getJpList());
				roadOpView.getScheduledJP().addAll(teamView.getJpList());
				
				System.err
						.println("finished setting all targets on the ListOf**");

				parishCode = null;
				teamView = new TeamView();
				parishBO = new ParishBO();
			}

			roadOpView
					.setSchedulerStaffId(roadOpBO.getScheduler().getStaffId());
			roadOpView.setSchedulerFirstName(roadOpBO.getScheduler()
					.getFirstName());
			roadOpView.setSchedulerLastName(roadOpBO.getScheduler()
					.getLastName());
			roadOpView.setSchedulerMiddleName(roadOpBO.getScheduler()
					.getMiddleName());
			roadOpView.setOfficeLocCode(roadOpBO.getScheduler()
					.getOfficeLocationCode());

			roadOpView.setScheduledStartDtime(roadOpBO
							.getScheduledStartDtime() != null ? roadOpBO
							.getScheduledStartDtime() : roadOpBO
							.getScheduledStartDate());
			roadOpView.setScheduledEndDtime(roadOpBO
							.getScheduledEndDtime() != null ? roadOpBO
							.getScheduledEndDtime() : roadOpBO
							.getScheduledEndDate());
			//kpowell-used to verify backdates
			roadOpView.setSavedScheduledStartDtime(roadOpView.getScheduledStartDtime());
			roadOpView.setSavedScheduledEndDtime(roadOpView.getScheduledEndDtime());
			
			context.getFlowScope().put("savedBackDated", roadOpView.getBackDated());			
			context.getFlowScope().put("savedAuthorized",roadOpView.getAuthorized());
			//
			roadOpView.setScheduledStartTime(roadOpBO.getScheduledStartTime());
			roadOpView.setScheduledEndTime(roadOpBO.getScheduledEndTime());
			roadOpView.setSchedNumOfDays(getDurationNumOfDays(
					roadOpView.getScheduledStartDtime(),
					roadOpView.getScheduledEndDtime()));
			roadOpView.setSchedNumOfHours(getDurationNumOfHours(
					roadOpView.getScheduledStartDtime(),
					roadOpView.getScheduledEndDtime()));
			roadOpView.setSchedNumOfMinutes(getDurationNumOfMinutes(roadOpView.getScheduledStartDtime(), roadOpView.getScheduledEndDtime()));
			
			roadOpView.setActualStartDtime(roadOpBO
							.getActualStartDtime() != null ? roadOpBO
							.getActualStartDtime() : roadOpBO
							.getActualStartDate());
			
			roadOpView.setActualStartDtime(roadOpBO
							.getActualStartDtime() != null ? roadOpBO
							.getActualStartDtime() : roadOpBO
							.getActualStartDate());
			System.err.println("Orig Val" + roadOpBO.getActualStartDtime() + "or " + roadOpBO.getActualStartDate());
			System.err.println("Greg val in convertBO" + roadOpView.getActualEndDtime());
			roadOpView
					.setActualEndDtime(roadOpBO
									.getActualEndDtime() != null ? roadOpBO
									.getActualEndDtime() : roadOpBO
									.getActualEndDate());
			roadOpView.setActualStartTime(roadOpBO.getActualStartTime());
			roadOpView.setActualEndTime(roadOpBO.getActualEndTime());
			roadOpView.setActualNumOfDays(getDurationNumOfDays(
					roadOpView.getActualStartDtime(),
					roadOpView.getActualEndDtime()));
			roadOpView.setActualNumOfHours(getDurationNumOfHours(
					roadOpView.getActualStartDtime(),
					roadOpView.getActualEndDtime()));
			roadOpView.setActualNumOfMinutes(getDurationNumOfMinutes(roadOpView.getActualStartDtime(), roadOpView.getActualEndDtime()));

			roadOpView.setStatusId(roadOpBO.getStatusId());
			roadOpView.setStatusDescription(roadOpBO.getStatusDescription());

			roadOpView.setAuthorized(roadOpBO.getAuthorized());
			roadOpView.setAuthorizedUserName(roadOpBO.getAuthorizedUserName());
			roadOpView.setAuthorizedDtime(roadOpBO
							.getAuthorizedDtime());

			roadOpView.setReasonId(roadOpBO.getReasonId());

			roadOpView.setComment(roadOpBO.getComment());

			if (roadOpView.getCategoryId().equalsIgnoreCase(Category.REGIONAL)) {
				CourtBO crt = roadOpBO
						.getCourtBO();
				roadOpView.setCourtBO(roadOpBO.getCourtBO());
				roadOpView.setCourtId(roadOpBO.getCourtBO().getCourtId());
				/*
				 * roadOpView.setCourtAddress(AddressFormattingUtil.
				 * getFormattedAddress(crt.getMarkText(),
				 * crt.getParishName(),crt.getParishCode(), crt.getPoBoxNo(),
				 * crt.getPoLocationName(), crt.getStreetName(),
				 * crt.getStreetNo()));
				 */
				roadOpView.setCourtAddressLine1(getAddressLine1(
						crt.getStreetName(), crt.getMarkText(),
						crt.getStreetNo()));
				roadOpView.setCourtAddressLine2(getAddressLine2(
						crt.getPoBoxNo(), crt.getPoLocationName(),
						crt.getParishName()));

				roadOpView
						.setCourtDate(returnCourtDateTime(roadOpBO
										.getCourtDate()));
			}
			
			//Get reason description
			//TODO - kpowell- 2014-10-04: replace fix for system reason to a generic fix
			/*if(roadOpBO.getReasonId() != null && roadOpBO.getReasonId().equals(Constants.Reason.SystemReasonCode.ROAD_OPERATION_CANCEL_REASON)){
				roadOpView.setReasonDescription(Constants.Reason.SystemReasonDescription.ROAD_OPERATION_CANCEL_REASON_DESC);
			}else*/
			if(roadOpBO.getReasonId() != null && roadOpBO.getReasonId() > 0)
			{
				HashMap<String, String> mapReasonQuery = new HashMap<String, String>();
				mapReasonQuery.put("reason_id", roadOpBO.getReasonId().toString());
				mapReasonQuery.put("ignore_is_visible_flag", Constants.YesNo.YES_LABEL_STR);//ignore is_visble and retrieve reason based on reason_id
				
				List<RefCodeBO> refCodes = this.getRefInfo("roms_reason", mapReasonQuery);
				
				if(refCodes.size() > 0)
					roadOpView.setReasonDescription(refCodes.get(0).getDescription());
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error occured");
			e.printStackTrace();
			addErrorMessage(RequestContextHolder.getRequestContext(),
					"SystemError");

		}

		return roadOpView;

	}

	private Date returnCourtDateTime(Date courtDate) {

		Date convertedDate = null;
		try {
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			String courtDateString = DateUtils.formatDate("yyyy-MM-dd",
					courtDate);
			convertedDate = dateTimeFormat.parse(courtDateString + " 10:00:00");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertedDate;
	}

	
	
	
	
	/**
	 * This function uses the Road Operation Web Service to Start a road
	 * operation or complete the road operation. If their are any errors during the call a <b>failure</b> event
	 * is returned and the page remains on the pop-up. If all is well then a
	 * <b>success</b> event is returned and the pop will close and return to the
	 * search screen.
	 * 
	 * @param context
	 * @return
	 */
	public Event startCompleteRoadOperation(RequestContext context, String newStatus) 
	{

		
		System.err.println("inside startCompleteRoadOperation()");
try{
		boolean error = false;
		/* Instantiate Webservice proxy */
		RoadOperation roadOpService = this.getRoadOperationService();

		/* Get all necessary data to call web service. */
		RoadOperationBO roadOp = (RoadOperationBO) context.getFlowScope().get(
				"roadOpBO");
		
		//System.err.println(org.fsl.roms.view.ObjectUtils.objectToString(roadOp));
		DataTableMemory assignedTeamDataTable = (DataTableMemory) context
				.getFlowScope().get("dataTableTeam");

		List<AssignedTeamDetailsBO> assignedTeamDetailsBOList = assignedTeamDataTable
				.getDataList();

		/* Set Current User Name calling Operation */
		roadOp.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

		/* Change road operation status to started */
		String previousStatus = roadOp.getStatusId();
		roadOp.setStatusId(newStatus);

		/* Set office codes related to the road operation */
		List<String> officeLocCodeList = new ArrayList<String>();
		try{
		/* Set String for Operation Times */
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar calendar = Calendar.getInstance();
		Date currentDate = calendar.getTime();

		Date today = null;
		SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		//TODO REMOVE
		try {
			today = Calendar.getInstance().getTime();		
			//System.err.println("currentDate"+ sdfFull.format(currentDate));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//System.err.println("sdfFull.format(today)"+ sdfFull.format(today));
		if (roadOp.getActualEndDate() != null) {
			Date actualEndDate = roadOp.getActualEndDate();
			roadOp.setActualEndTime(sdf.format(actualEndDate));
			roadOp.setActualEndDtime(roadOp.getActualEndDate());
			
		
			//added by kpowell
			if (roadOp.getActualEndDate().compareTo(today) > 0) {
				addErrorMessageText(context,
						"Actual End Date and Time cannot be after Current Date and Time :"+ sdfFull.format(currentDate));
				context.getFlowScope().put("closeStatus", "open");
				roadOp.setStatusId(previousStatus);
				//return error();
				error = true;
			}
			
			//Actual End Date/Time (must only be within scheduled period and the actual end date-time must be after the last associated road check)
			/*if (roadOp.getActualEndDate().compare(roadOp.getLastOffenceDate()) > 0) {
				
			}*/
		}
		else{
	
			if(previousStatus.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED)){
						
				this.addErrorMessageWithParameter(context, "RequiredFields",
						"Actual End Date");
				context.getFlowScope().put("closeStatus", "open");
				roadOp.setStatusId(previousStatus);
				//return error();
				error = true;
			}
		}

		if (roadOp.getActualStartDate() != null) {
			Date actualStartDate = roadOp.getActualStartDate();
			roadOp.setActualStartTime(sdf.format(actualStartDate));
			roadOp.setActualStartDtime(roadOp.getActualStartDate());

			//added by kpowell
			if (roadOp.getActualStartDate().compareTo(today) > 0) {
				addErrorMessageText(context,
						"Actual Start Date and Time cannot be after Current Date and Time :"+ sdfFull.format(currentDate));
				context.getFlowScope().put("closeStatus", "open");
				roadOp.setStatusId(previousStatus);
				//return error();
				error = true;
			}
			
			//Actual Start Date/Time (the actual start date-time must be before or on the first associated road check)
			/*if (roadOp.getActualEndDate().compare(roadOp.getFirstOffenceDate()) > 0) {
				
			}*/
			
		} else {
			
			this.addErrorMessageWithParameter(context, "RequiredFields",
					"Actual Start Date");
			context.getFlowScope().put("closeStatus", "open");
			roadOp.setStatusId(previousStatus);
			//return error();
			error = true;
		}
		
		

		
		
		
		
		
		// Populate Team Information for Operation
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		
		
		
		RoadOperationOtherDetailsBO roadOpOtherDetails = roadOpService
				.findRoadOperationOtherDetails(roadOp.getRoadOperationId());
		
		
		//Minimum Requirement Validation for Starting a Operation

		//if(previousStatus.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING)){
		
		context.getFlowScope().put("resourceMinReqMet", "yes");
			
			List<StrategyBO> selectedStrat = roadOpOtherDetails.getOperationStrategyList();
		
			//int minimumVehReq = 0;
	
			
			List<Integer> selectedStratIDs = new ArrayList<Integer>();
	
			for (StrategyBO strat : selectedStrat) {
				selectedStratIDs.add(strat.getStrategyId());
			}
	
			RoadOperation roadOperationService = getRoadOperationService();
			
			List<StrategyRuleBO> strategyRuleList = new ArrayList<StrategyRuleBO>();
	
			try {
				if (selectedStratIDs.size() > 0) {// kpowell
					strategyRuleList = roadOperationService
							.getOperationStrategyRule(selectedStratIDs);
				}
			} catch (InvalidFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			boolean isVehicleRequired = false;
			for (StrategyBO strat : selectedStrat) {
				if (strat.isVehicleRequired()) {
					isVehicleRequired= true;
					break;
				}
	
			}
	
	//	List<TeamView> teams = roadOperationView.getTeams();
	//	List<RoadOpUnattendedResourceView> unattendedList = new ArrayList<RoadOpUnattendedResourceView>();
		//RoadOpUnattendedResourceView unattendedResourceView = new RoadOpUnattendedResourceView();
		List<AssignedTeamDetailsBO> teams = (List<AssignedTeamDetailsBO>) assignedTeamDataTable.getDataList();
		Integer taStaffCount = 0;
		Integer itaExamCount = 0;
		Integer jpCount = 0;
		Integer policeCount = 0;
		Integer vehicleCount = 0;
		boolean teamAtend = true;
		boolean allTeamAttend = true;
		boolean commentRequired = false;
		boolean teamLeadAttend = true;
		String teamLeadFullName= "";
		if (teams != null) {
			for (AssignedTeamDetailsBO teamView : teams) {
				teamAtend = true;
				taStaffCount = 0;
				itaExamCount = 0;
				jpCount = 0;
				policeCount = 0;
				vehicleCount = 0;
				//unattendedResourceView = new RoadOpUnattendedResourceView();
				if (teamView.getTaStaffList() != null) {
					for (TAStaffBO taStaff : teamView.getTaStaffList()) {
						if (taStaff.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(taStaff.getAttended()== true){
							taStaffCount++;
						}
						else if(taStaff.getAttended()== false){ 
							
							if(StringUtils.isBlank(taStaff.getComment())){
								commentRequired =true;
							}
						//	unattendedResourceView = new RoadOpUnattendedResourceView(taStaff);
						//	unattendedList.add(unattendedResourceView);
							
							//kpowell:2014-10-22:teamLead marked absent
							if(taStaff.getStaffId().equalsIgnoreCase(teamView.getTeamBO().getTeamLead().getStaffId())){
								teamLeadAttend = false;
								teamLeadFullName = teamView.getTeamBO().getTeamLead().getFullName()+
										" - "+ teamView.getTeamBO().getTeamLead().getStaffId();
								
								addErrorMessageText(context, "Team : " +teamView.getTeamBO().getTeamName()+ 
													"- Team Lead ("+ teamLeadFullName+ ") was marked Absent. A New Team Lead is required");
								error = true;
							}
						}
						
					}
				}

				if (teamView.getItaExaminerList() != null) {
					for (ITAExaminerBO itaExam : teamView.getItaExaminerList()) {

						if (itaExam.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(itaExam.getAttended()== true){
							itaExamCount++;
						}
						else if(itaExam.getAttended()== false){
							if(StringUtils.isBlank(itaExam.getComment())){
								commentRequired =true;
							}
							
						//	unattendedResourceView = new RoadOpUnattendedResourceView(itaExam);
							//unattendedList.add(unattendedResourceView);
						}
					}
				}

				if (teamView.getPoliceOfficerList() != null) {
					for (PoliceOfficerBO police : teamView
							.getPoliceOfficerList()) {

						if (police.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(police.getAttended()== true){
							policeCount++;
						}
						else if(police.getAttended()== false){ 
							
							if(StringUtils.isBlank(police.getComment())){
								commentRequired =true;
							}
							
						//	unattendedResourceView = new RoadOpUnattendedResourceView(police);
						//	unattendedList.add(unattendedResourceView);
							
						}
				}
				}
				if (teamView.getJpList()!= null) {
					for (JPBO jp : teamView
							.getJpList()) {

						if (jp.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(jp.getAttended()== true){
							jpCount++;
						}
						else if(jp.getAttended()== false){
							
							if(StringUtils.isBlank(jp.getComment())){
								commentRequired =true;
							}
							
						//	unattendedResourceView = new RoadOpUnattendedResourceView(jp);
						//	unattendedList.add(unattendedResourceView);
						}
					}
				}

				if (teamView.getTaVehicleList() != null) {
					for (TAVehicleBO vehicle : teamView.getTaVehicleList()) {

						if (vehicle.getAttended() == null) {
							teamAtend = false;
							allTeamAttend=false;
							//break teamsloop;
						}
						else if(vehicle.getAttended()== true){
							vehicleCount++;
						}
						else if(vehicle.getAttended()== false){ 
							
							if(StringUtils.isBlank(vehicle.getComment())){
								commentRequired =true;
							}
							
						//	unattendedResourceView = new RoadOpUnattendedResourceView(vehicle);
						//	unattendedList.add(unattendedResourceView);
						}
					}
				}

				
				if(previousStatus.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING) ||
						previousStatus.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION)){
					
					
					//context.getFlowScope().put("unattendedList", unattendedList);
					
					if(teamAtend==true){
						
						boolean vehicleAssigned = false;
						if(vehicleCount>0){
							vehicleAssigned = true;
						}
						
						boolean validStrategyRule = validateOperationStrategyRule(strategyRuleList, isVehicleRequired, vehicleAssigned, taStaffCount, jpCount, itaExamCount, policeCount, teamView.getTeamBO().getTeamName());
	
						if(validStrategyRule==false){
							error = true;
						}
						//System.err.println("validStrategyRule"+ validStrategyRule);
						if((validStrategyRule==false || teamLeadAttend==false) && !( roadOp.getAuthorized().equals("Y") && roadOp.getBackDated().equals("Y")) ){
							context.getFlowScope().put("resourceMinReqMet", "no");
							if (this.isHandHeld(context)) {
								addInfoMessageText(context, "Please contact the office to update the assigned resources.");
							}
							else{
								addInfoMessageText(context, "To update the assigned resources please click the Update Assigned Resources button.");
							}
							error = true;
							System.err.println("context.getFlowScope().put(resourceMinReqMet, no)");
						}
						
						//kpowell-2014-10-30
						if(roadOp.getAuthorized().equals("Y") && roadOp.getBackDated().equals("Y")){
							
						/*	context.getMessageContext().addMessage(
									new MessageBuilder().info().defaultText(
													"This Backdated Road Operation has been Authorized, and scheduled details cannot be updated").build());*/
							
							FacesContext.getCurrentInstance().addMessage("infoMessagesStartRoadOp",new FacesMessage(FacesMessage.SEVERITY_INFO, "This Backdated Road Operation has been Authorized. Scheduled details cannot be updated", "This Backdated Road Operation has been Authorized. Scheduled details cannot be updated"));
						}
						/*if(teamLeadAttend==false){
							addErrorMessageText(context, "Team Lead ("+ teamLeadFullName+ ") was marked Absent. A New Team Lead is required");
							error = true;
						}*/
						
					}
				}

			}
			
			// System.err.println("teamAtend::"+ teamAtend);
			String errorMsg = "";
			if (allTeamAttend == false) {
				errorMsg ="Attendance is not completed";
				if(teams.size()>1){
					errorMsg= errorMsg+" for All Teams";
				}
				/*addErrorMessageWithParameter(context, "RequiredFields",
						"Attendance for All Resource");*/
				addErrorMessageText(context, errorMsg);
				///return error();
				error = true;
			}
			
			if(commentRequired == true){
				addErrorMessageText(context, "A Comment is required for all Resources that will not be in attendance");
				error = true;
			}

		}
		
		
		
	
			//OperationStrategyRuleView operationStrategyRuleView = new OperationStrategyRuleView();
			//operationStrategyRuleView = getOperationStrategyRuleView(strategyRuleList);

			
			
			
			
		//}
		//System.err.println("Error state"+ error);
		
		if(error == true){
			context.getFlowScope().put("closeStatus", "open");
			roadOp.setStatusId(previousStatus);
			System.err.println("error==true");
			return error();
		}

		/* Check for Required Fields */
		System.out.println("Check for Required Fields");
		System.out.println("Road Operation ID: " + roadOp.getRoadOperationId());
		System.out.println("Status ID: " + roadOp.getStatusId());
		System.out.println("User Name: " + roadOp.getCurrentUsername());
		System.out.println("Start Date: "
				+ roadOp.getActualStartDate()
						.toLocaleString());
		System.out.println("Start Time: " + roadOp.getActualStartTime());
	
		
		/* Call the web service */
		//try {
			//officeLocCodeList = roadOpProxy.findOfficeLocCodeList(roadOp
			//		.getRoadOperationId());

		

			roadOp.getOperationStrategyList().addAll(
					roadOpOtherDetails.getOperationStrategyList());

		//roadOp.getOfficeLocCodeList().addAll(officeLocCodeList);
			roadOp.getOfficeLocCodeList().addAll(roadOpOtherDetails.getOperationRegionList());
			// Populate Team Information for Operation
			/**
			 * TODO remove
			 */
			if (assignedTeamDetailsBOList != null) {
				for (AssignedTeamDetailsBO teamDetBO : assignedTeamDetailsBOList) {	
					System.err.println("Starting...1Stuff to save " + ":"
							+ teamDetBO.getOperationLocationList() + "---"
							+ teamDetBO.getItaExaminerList() + "---"
							+ teamDetBO.getJpList() + "---"
							+ teamDetBO.getPoliceOfficerList() + "---"
							+ teamDetBO.getTaStaffList()+ "---"
							+ teamDetBO.getTeamBO().getTeamId());					
				}
			}	
			
			System.err.println("Starting...Stuff to save " + ":"
					+ roadOp.getRoadOperationId() + ":"
					+ roadOp.getStatusId() + ":" + roadOp + ":"
					+ roadOp.getCategoryId() + ":"
					+ roadOp.getScheduledStartDate() + ":"
					+ roadOp.getScheduledEndDate() + ":"
					+ roadOp.getCurrentUsername() + ":"
					+ roadOp.getOfficeLocCodeList() + ":"
					+ roadOp.getOperationStrategyList() + ":"
					+ roadOp.getCategoryId() + ":"
					+ roadOp.getCourtBO() + ":" + roadOp.getCourtDate()
					+ ":" + roadOp.getCourtTime() + ":"
					+ roadOp.getScheduledStartTime() + ":"
					+ roadOp.getScheduledEndTime());
			
			
			
			roadOpService.updateRoadOperation(roadOp, assignedTeamDetailsBOList);

			context.getFlowScope().put("closeStatus", "close");

			String screen = (String) context.getFlowScope().get("screen");
			if (screen.equalsIgnoreCase("search")){
				this.getResults(context);
//				RoadOperationDetailsSumary roadOpSum = (RoadOperationDetailsSumary) context
//						.getFlowScope().get("summary");
//				
//				if(newStatus.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED)){
//					roadOp.setStatusDescription(Constants.Status.ROAD_OPERATION_STARTED_DESC);
//				}
//				else{
//					roadOp.setStatusDescription(Constants.Status.ROAD_OPERATION_COMPLETED_DESC);
//				}
//				
//				roadOpSum.setRoadOp(roadOp);
				
				
			}
				

		} catch (ErrorSavingException e) {
			this.addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			context.getFlowScope().put("closeStatus", "open");
			roadOp.setStatusId(previousStatus);
			return error();
		} catch (InvalidFieldException e) {
			this.addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			context.getFlowScope().put("closeStatus", "open");
			roadOp.setStatusId(previousStatus);
			return error();
		} catch (RequiredFieldMissingException e) {
			// this.addErrorMessage(context, "AtleastOneSearchCriteriaReq");
			this.addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			context.getFlowScope().put("closeStatus", "open");
			roadOp.setStatusId(previousStatus);
			return error();
		} catch (NoRecordFoundException e) {
			this.addErrorMessageText(context, e.getMessage());
			e.printStackTrace();
			context.getFlowScope().put("closeStatus", "open");
			roadOp.setStatusId(previousStatus);
			return error();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
			return error();
			
		}
}catch (Exception e) {

	// TODO: handle exception
		e.printStackTrace();
		addErrorMessage(context, "SystemError");
		return error();
		
}
		System.err.println("exit startCompleteRoadOperation()");
		return success();

	}
	
	
	
	public Event getUnattendedResources(RequestContext context){
		System.err.println("inside getUnattendedResources()");
		try{
		
		DataTableMemory assignedTeamDataTable = (DataTableMemory) context
				.getFlowScope().get("dataTableTeam");
		List<RoadOpUnattendedResourceView> unattendedList = new ArrayList<RoadOpUnattendedResourceView>();
		RoadOpUnattendedResourceView unattendedResourceView = new RoadOpUnattendedResourceView();
		List<AssignedTeamDetailsBO> teams = (List<AssignedTeamDetailsBO>) assignedTeamDataTable.getDataList();
		
		context.getFlowScope().put("unattendedList", unattendedList);
	
		if (teams != null) {
			for (AssignedTeamDetailsBO teamView : teams) {
				unattendedResourceView = new RoadOpUnattendedResourceView();
				if (teamView.getTaStaffList() != null) {
					for (TAStaffBO taStaff : teamView.getTaStaffList()) {
							if(taStaff.getAttended()!=null && taStaff.getAttended()== false){
								unattendedResourceView = new RoadOpUnattendedResourceView(taStaff);
								unattendedList.add(unattendedResourceView);
						}
					}
				}
				
				if (teamView.getItaExaminerList() != null) {
					for (ITAExaminerBO itaExam : teamView.getItaExaminerList()) {
					 if(itaExam.getAttended()!=null && itaExam.getAttended()== false){		
							unattendedResourceView = new RoadOpUnattendedResourceView(itaExam);
							unattendedList.add(unattendedResourceView);
						}
					}
				}

				if (teamView.getPoliceOfficerList() != null) {
					for (PoliceOfficerBO police : teamView
							.getPoliceOfficerList()) {

						 if(police.getAttended()!=null && police.getAttended()== false){ 							
							unattendedResourceView = new RoadOpUnattendedResourceView(police);
							unattendedList.add(unattendedResourceView);	
						}
				}
				}
				if (teamView.getJpList()!= null) {
					for (JPBO jp : teamView
							.getJpList()) {

						 if(jp.getAttended()!=null && jp.getAttended()== false){
							unattendedResourceView = new RoadOpUnattendedResourceView(jp);
							unattendedList.add(unattendedResourceView);
						}
					}
				}

				if (teamView.getTaVehicleList() != null) {
					for (TAVehicleBO vehicle : teamView.getTaVehicleList()) {

						 if(vehicle.getAttended()!=null && vehicle.getAttended()== false){ 
							unattendedResourceView = new RoadOpUnattendedResourceView(vehicle);
							unattendedList.add(unattendedResourceView);
						}
					}
				}
			
			}
		
		}
		System.err.println("exit getUnattendedResources()");
		context.getFlowScope().put("unattendedList", unattendedList);
		return success();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("inside getUnattendedResources()");
			return error();
		}
	}

	public void synchronizeStartedRoadOp(RequestContext context) {
		
		RoadOperationBO roadOp = (RoadOperationBO) context.getFlowScope().get(
				"roadOpBO");
		
		RoadOperationView operation = (RoadOperationView) context.getFlowScope().get("operation");
		
		Integer roadOpId = null;
		
		roadOpId = roadOp.getRoadOperationId();
		
		if(roadOpId == null)
			roadOpId = operation.getRoadOperationId();
		

		if (roadOpId != null) {
			try {
				RoadOperationCriteriaBO roadOpCrit = new RoadOperationCriteriaBO();
				roadOpCrit.setRoadOperationId(roadOpId);

				List<RoadOperationBO> roadOps = this
						.getRoadOperationService().lookupRoadOperation(
								roadOpCrit);

				if (roadOps.size() > 0)
					roadOp = roadOps.get(0);

				operation = this.convertBOtoView(roadOp);

				FacesContext contextT = FacesContext.getCurrentInstance();
				if (operation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_STARTED))
				{
					contextT.addMessage(null, new FacesMessage("Road Operation Started Successfully"));
				}
				else if(operation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_COMPLETED))
				{
					contextT.addMessage(null, new FacesMessage("Road Operation Completed Successfully"));
				}
				else if(operation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED))
				{
					contextT.addMessage(null, new FacesMessage("Road Operation Terminated Successfully"));
				}
				else if(operation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CANCELLED))
				{
					contextT.addMessage(null, new FacesMessage("Road Operation Cancelled Successfully"));
				}
					

				context.getFlowScope().put("operation", operation);
				
				prepareReviewScreen(context);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public Event startRoadOpCompleteMessage(RequestContext context) {
//		FacesContext contextT = FacesContext.getCurrentInstance();
//		contextT.addMessage(null, new FacesMessage(
//				"Road Operation Started Successfully"));
		synchronizeStartedRoadOp(context);

		return success();
	}

	

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public AvailableResourceCriteriaBO getAvailResourceCriteria() {
		return availResourceCriteria;
	}

	public void setAvailResourceCriteria(
			AvailableResourceCriteriaBO availResourceCriteria) {
		this.availResourceCriteria = availResourceCriteria;
	}

	public TreeNode getLocationTreeView() {
		return locationTreeView;
	}

	public void setLocationTreeView(TreeNode locationTreeView) {
		this.locationTreeView = locationTreeView;
	}

	public void radioChanged(ValueChangeEvent event) {
		RequestContext requestContext = RequestContextHolder
				.getRequestContext();

		requestContext.getFlowScope().put("option",
				event.getNewValue().toString());

		System.out.println("Old Value is " + event.getOldValue().toString());

		System.out.println("New Value is " + event.getNewValue().toString());
	}

	public void getAvailableResources(RequestContext context) {

		try {

			System.err.println("getAvailableResources");
			RoadOperationView roadOperation = (RoadOperationView) context
					.getFlowScope().get("operation");
			String resourcesRetrieved = (String) context.getFlowScope().get(
					"resourcesRetrieved");

			System.err.println("Resources Retrieved " + resourcesRetrieved);
			// if(!resourcesRetrieved.equals("true"))
			// {

			String fromWhere = (String) context.getFlowScope().get("fromWhere");
			System.err.println("fromWhere::" + fromWhere);

			availResourceCriteria = new AvailableResourceCriteriaBO();

			String stage = (String) context.getFlowScope().get("currentStage");

			List<ITAExaminerBO> availableITAExam = new ArrayList<ITAExaminerBO>();
			List<JPBO> availableJP = new ArrayList<JPBO>();
			List<PoliceOfficerBO> availablePolice = new ArrayList<PoliceOfficerBO>();
			List<TAVehicleBO> availableVehicle = new ArrayList<TAVehicleBO>();

			List<TAStaffBO> selectedStaff = new ArrayList<TAStaffBO>();
			List<ITAExaminerBO> selectedITAExam = new ArrayList<ITAExaminerBO>();
			List<JPBO> selectedJP = new ArrayList<JPBO>();
			List<PoliceOfficerBO> selectedPolice = new ArrayList<PoliceOfficerBO>();
			List<TAVehicleBO> selectedVehicle = new ArrayList<TAVehicleBO>();

			List<TAStaffBO> residualStaff = new ArrayList<TAStaffBO>();
			List<ITAExaminerBO> residualITAExam = new ArrayList<ITAExaminerBO>();
			List<JPBO> residualJP = new ArrayList<JPBO>();
			List<PoliceOfficerBO> residualPolice = new ArrayList<PoliceOfficerBO>();
			List<TAVehicleBO> residualVehicle = new ArrayList<TAVehicleBO>();

			availResourceCriteria = populateAvailResourceCriteriaBO(
					roadOperation, "N/A");

			System.err.println("RESOURCES RETRIEVED? " + resourcesRetrieved);
			if (!resourcesRetrieved.equalsIgnoreCase("true")) {
				for (int i = 1; i <= 5; i++) {
					if (i == 1) {
						// Get ITA Examiner
						if (roadOperation.getListOfITA() != null) {
							selectedITAExam = roadOperation.getListOfITA()
									.getTarget();
							System.out.println("selectedITAExam ="
									+ selectedITAExam.size());
						}

						try {
							availableITAExam = getRoadOperationService()
									.getAvailableITAResource(
											availResourceCriteria);

							if (availableITAExam == null)
								availableITAExam = new ArrayList<ITAExaminerBO>();
							;

							System.err
									.println("setListOfITAAux before target added::"
											+ availableITAExam.size());
							// added by kpowell::from search the selected staff
							// wouldnt be in the available list
							if (fromWhere != null) {
								if (fromWhere.equals(FROM_SEARCH)) {
									availableITAExam.addAll(selectedITAExam);
								}
							}

							roadOperation.setListOfITAAux(availableITAExam);
							System.err.println("setListOfITAAux::"
									+ roadOperation.getListOfITAAux().size());

							// remove those previously selected for a team
							/**TODO REMOVE CODE
							//availableITAExam = getResidualITA(availableITAExam);
							//residualITAExam.addAll(availableITAExam);
							**/
							residualITAExam.addAll(availableITAExam);
							residualITAExam = getResidualITA(residualITAExam);

							System.err
									.println("residualITAExam after remove those previously selected for a team::"
											+ residualITAExam.size());
							roadOperation
									.setListOfITA(new DualListModel<ITAExaminerBO>(
											residualITAExam, selectedITAExam));

							roadOperation.setAvailableITA(residualITAExam);
							
							if(roadOperation.getListOfITA()!= null){
								Collections.sort(roadOperation.getListOfITA().getSource(),itaComperator );
								Collections.sort(roadOperation.getListOfITA().getTarget(),itaComperator );
							}

						} catch (InvalidFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;

						} catch (NoRecordFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;

						} catch (RequiredFieldMissingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;

						}

					}

					if (i == 2) {

						// Get Staff

						if (roadOperation.getListOfStaff() != null) {
							selectedStaff = roadOperation.getListOfStaff()
									.getTarget();
							System.out.println("selectedStaff ="
									+ selectedStaff.size());
						}
						try {
							System.out
									.println("TA STaff AUX[getAvailableResources]");
							availableStaff = new ArrayList<TAStaffBO>();

							availableStaff = getRoadOperationService()
									.getAvailableTAStaffResource(
											availResourceCriteria);

							if (availableStaff == null)
								availableStaff = new ArrayList<TAStaffBO>();

							System.err
									.println("setListOfStaffAux before target added::"
											+ availableStaff.size());
							// added by kpowell::from search the selected staff
							// wouldnt be in the available list
							if (fromWhere != null) {
								if (fromWhere.equals(FROM_SEARCH)) {
									availableStaff.addAll(selectedStaff);
								}
							}

							System.err.println("setListOfStaffAux::"
									+ availableStaff.size());
							roadOperation.setListOfStaffAux(availableStaff);

							// remove those previously selected for a team
							/**TODO REMOVE CODE
							 * availableStaff = getResidualStaff(availableStaff);
							residualStaff.addAll(availableStaff);
							**/
							residualStaff.addAll(availableStaff);
							residualStaff = getResidualStaff(residualStaff);
							

							System.err.println("getResidualStaff::"
									+ residualStaff.size());
							roadOperation
									.setListOfStaff(new DualListModel<TAStaffBO>(
											residualStaff, selectedStaff));

							roadOperation.setAvailableStaff(residualStaff);
							System.err.println("setAvailableStaff::"
									+ residualStaff.size());
							
							if(roadOperation.getListOfStaff()!= null){
								Collections.sort(roadOperation.getListOfStaff().getSource(),taStaffComperator );
								Collections.sort(roadOperation.getListOfStaff().getTarget(),taStaffComperator );
							}
							
						} catch (InvalidFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;

						} catch (NoRecordFoundException e) {
							// TODO Auto-generated catch block
							// added by kpowell::from search the selected staff
							// wouldnt be in the available list
							if (fromWhere != null) {
								if (fromWhere.equals(FROM_SEARCH)) {
									availableStaff.addAll(selectedStaff);
								}
							}
							e.printStackTrace();
							continue;

						} catch (RequiredFieldMissingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}

					if (i == 3) {

						// Get Vehicle
						if (roadOperation.getListOfVehicles() != null) {
							selectedVehicle = roadOperation.getListOfVehicles()
									.getTarget();
							System.err.println("selectedVehicle ="
									+ selectedVehicle.size());
						}

						try {
							availableVehicle = getRoadOperationService()
									.getAvailableTAVehResource(
											availResourceCriteria);

							if (availableVehicle == null)
								availableVehicle = new ArrayList<TAVehicleBO>();

							System.err
									.println("setListOfVehAux before target added::"
											+ availableVehicle.size());
							// added by kpowell::from search the selected staff
							// wouldnt be in the available list
							if (fromWhere != null) {
								if (fromWhere.equals(FROM_SEARCH)) {
									availableVehicle.addAll(selectedVehicle);
								}
							}

							roadOperation.setListOfVehAux(availableVehicle);
							System.err.println("setListOfVehAux::"
									+ roadOperation.getListOfVehAux().size());

							// remove those previously selected for a team
							/**TODO REMOVE CODE
							 * availableVehicle = getResidualVeh(availableVehicle);
							residualVehicle.addAll(availableVehicle);
							**/
							residualVehicle.addAll(availableVehicle);
							residualVehicle = getResidualVeh(residualVehicle);
							
							System.err
									.println("setListOfVehAux:: AFter residual"
											+ roadOperation.getListOfVehAux()
													.size());

							roadOperation
									.setListOfVehicles(new DualListModel<TAVehicleBO>(
											residualVehicle, selectedVehicle));
							roadOperation.setAvailableVehicles(residualVehicle);

							System.err.println("setAvailableVehicles::"
									+ roadOperation.getAvailableVehicles()
											.size());
							
							if(roadOperation.getListOfVehicles().getSource()!= null){
								Collections.sort(roadOperation.getListOfVehicles().getSource(),vehicleComperator );
								Collections.sort(roadOperation.getListOfVehicles().getTarget(),vehicleComperator );
							}

						} catch (InvalidFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;

						} catch (NoRecordFoundException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
							System.err.println("I AM HERE!!!!!");
							continue;

						} catch (RequiredFieldMissingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;

						}
					}

					if (i == 4) {			
						/*Code was here to get police when resource page is loaded it has now been removed to an undemand polcie filter*/
					}

					if (i == 5) {

						context.getFlowScope()
								.put("resourcesRetrieved", "true");
						context.getFlowScope().put("operation", roadOperation);
						System.err.println("End getAvailableResources()");
					}
				}
								

			} else {
				System.err.println("Already resourcesRetrieved");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("Error Occured in getAvailableResources()");
			e.printStackTrace();
			addErrorMessage(context, "SystemError");
			// return error();

		}
		// }

	}

	/**
	 * This function checks is a police filter was used.
	 * @param roadOperation
	 * @return <b>TRUE</b> is filter used already. <b>FALSE</b> if filter was never used
	 */
	private boolean policeFilterUsedBefore(RoadOperationView roadOperation){
		boolean filterUsed = false;
		
		StringBuilder filter = new StringBuilder();
		
		filter.append(roadOperation.getPoliceOfficerFirstName());
		filter.append(roadOperation.getPoliceOfficerLastName());
		filter.append(roadOperation.getPoliceOfficerCompNum());
		filter.append(roadOperation.getPoliceStation());
		
		for(String policeFilter : roadOperation.getPoliceFilters()){
			if(policeFilter.equalsIgnoreCase(filter.toString())){
				filterUsed = true;
				break;
			}
		}
		
		if(!filterUsed){
			roadOperation.getPoliceFilters().add(filter.toString());
		}
		
		return filterUsed;
		
	}
	
	/**
	 * This function validates id the amount of filters need for police filter is set
	 * @param amntFiltersNeeded - The amount of filters you are checking for
	 * @return True if filters meet minimum requirements False if it does not
	 */
	private boolean policeFilterValid(Integer amntFiltersNeeded,RoadOperationView roadOperation){
		
		int amountOfFilters = 0;
		
		if(roadOperation.getPoliceOfficerCompNum() != null && roadOperation.getPoliceOfficerCompNum() > 0){
			amountOfFilters ++;
		}
		
		if( StringUtil.isSet(roadOperation.getPoliceOfficerFirstName())){
			amountOfFilters ++;
		}
		
		if( StringUtil.isSet(roadOperation.getPoliceOfficerLastName())){
			amountOfFilters ++;
		}
		
		if( StringUtil.isSet(roadOperation.getPoliceStation())){
			amountOfFilters ++;
		}
			
		
		
		if(amountOfFilters >= amntFiltersNeeded){
			return true;
		}
		else{
			return false;
		}
		
		
	}
	
	public void clearPoliceFilter(RequestContext context){
		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		roadOperation.getPoliceFilters().clear();
		roadOperation.setPoliceOfficerCompNum(null);
		roadOperation.setPoliceOfficerFirstName(null);
		roadOperation.setPoliceOfficerLastName(null);
		roadOperation.setPoliceStation(null);
		
		if(roadOperation.getListOfPolice() != null){
			roadOperation.getListOfPolice().getSource().clear();
		}
	}
	

	
	public static Comparator<PoliceOfficerBO> policeComperator = new Comparator<PoliceOfficerBO>(){
		@Override
		public int compare(PoliceOfficerBO p1, PoliceOfficerBO p2)
		{
			if(p1.getFirstName() == null || p1.getLastName() == null || p2.getFirstName() == null || p2.getLastName() == null ){
				return 0;
			}
			
			if(p1.getLastName().equalsIgnoreCase(p2.getLastName())){
				return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
			}
			else{
				return p1.getLastName().compareToIgnoreCase(p2.getLastName());
			}
		}};
	
	public void getPoliceListFilter(RequestContext context){
		// Get Police
		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");		
		
//		if(policeFilterUsedBefore(roadOperation)){
//			
//			FacesContext.getCurrentInstance().addMessage("policeFilter",new FacesMessage(FacesMessage.SEVERITY_WARN, "The current selections for the police officer filter have already been used.", "The current selections for the police officer filter have already been used."));
//			return;
//		}
//		
		if(!policeFilterValid(1,roadOperation)){
			FacesContext.getCurrentInstance().addMessage("policeFilter",new FacesMessage(FacesMessage.SEVERITY_ERROR, "One or more filter items must be used.", "Two or more filter items must be used."));
			return;
		}
		
		
		availResourceCriteria = populateAvailResourceCriteriaBO(
				roadOperation, "N/A");
		
		List<PoliceOfficerBO> selectedPolice = new ArrayList<PoliceOfficerBO>();
		List<PoliceOfficerBO> availablePolice = new ArrayList<PoliceOfficerBO>();
		List<PoliceOfficerBO> residualPolice = new ArrayList<PoliceOfficerBO>();
		
		String fromWhere = (String) context.getFlowScope().get("fromWhere");
		
		if (roadOperation.getListOfPolice() != null) {
//			if(roadOperation.getListOfPolice().getSource().size() > 100){
//				String message = "The current list of selectable police officers is " + roadOperation.getListOfPolice().getSource().size() + ". It is recommended that you clear the filter for increased performance." +
//						" The current list of selected police officers will be preserved.";
//				FacesContext.getCurrentInstance().addMessage("policeFilter",new FacesMessage(FacesMessage.SEVERITY_WARN,message , message));
//			}
//			selectedPolice = roadOperation.getListOfPolice()
//					.getTarget();
			
//			System.out.println("selectedPolice ="
//					+ selectedPolice.size());
		}

		try {

			availablePolice = getRoadOperationService()
					.getAvailablePoliceResource(
							availResourceCriteria);


			if (availablePolice == null)
				availablePolice = new ArrayList<PoliceOfficerBO>();

			System.err
					.println("setListOfVehAux before target added::"
							+ availablePolice.size());
			// added by kpowell::from search the selected staff
			// wouldnt be in the available list
			if (fromWhere != null ) {
				if (fromWhere.equals(FROM_SEARCH) && availablePolice.size() < 1) {
					availablePolice.addAll(selectedPolice);
				}
			}
			
			roadOperation.setListOfPoliceAux(availablePolice);
			System.err
					.println("setListOfPoliceAux::"
							+ roadOperation
									.getListOfPoliceAux()
									.size());

			// remove those previously selected for a team
			/**TODO REMOVE CODE
			 * availablePolice = getResidualPolice(availablePolice);
			residualPolice.addAll(availablePolice);
			**/
			residualPolice.addAll(availablePolice);
			residualPolice = getResidualPolice(residualPolice);


			
			if(roadOperation.getListOfPolice() == null){
				roadOperation
					.setListOfPolice(new DualListModel<PoliceOfficerBO>(
							residualPolice, selectedPolice));
			}
			else{
				
				List<PoliceOfficerBO> policeToAdd = new ArrayList<PoliceOfficerBO> ();
				
				for(PoliceOfficerBO policeRes : residualPolice){
					
					boolean found = false;
					
					for(PoliceOfficerBO policeSource : roadOperation.getListOfPolice().getSource()){
						if(policeSource.getPersonID().intValue() == policeRes.getPersonID().intValue()){
							found = true;
							break;
						}
					}
					
					for(PoliceOfficerBO policeSource : roadOperation.getListOfPolice().getTarget()){
						if(policeSource.getPersonID().intValue() == policeRes.getPersonID().intValue()){
							found = true;
							break;
						}
					}
					
					if(!found){
						policeToAdd.add(policeRes);
					}
				}
				
				if(policeToAdd.size() < 1){
					FacesContext.getCurrentInstance().addMessage("policeFilter",new FacesMessage(FacesMessage.SEVERITY_WARN,"No new results were found." ,"No new results were found." ));
				}
				else{
					roadOperation.getListOfPolice().getSource().clear();
					
					roadOperation.getListOfPolice().getSource().addAll(policeToAdd);
				}
				
				
				
				
				
				
				
				//roadOperation.getListOfPolice().getTarget().clear();
				//roadOperation.getListOfPolice().getTarget().addAll(selectedPolice);
				
			}
				
			if(roadOperation.getListOfPolice()!= null){
				Collections.sort(roadOperation.getListOfPolice().getSource(),policeComperator );
				Collections.sort(roadOperation.getListOfPolice().getTarget(),policeComperator );
			}

			roadOperation.setAvailablePolice(residualPolice);

			System.err.println("residualPolice"
					+ residualPolice.size());

		} catch (InvalidFieldException e) {

			e.printStackTrace();
			

		} catch (NoRecordFoundException e) {
			
			e.printStackTrace();
			System.err.println("getAvailableResources<<<"
					+ availablePolice.size());

			FacesContext.getCurrentInstance().addMessage("policeFilter",new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
				
	

		} catch (RequiredFieldMissingException e) {
			
			e.printStackTrace();
			

		}
	}
	

/***2014-10-28**/
/*	public static Comparator<TAVehicleBO> taVehComperator = new Comparator<TAVehicleBO>(){
		@Override
		public int compare(TAVehicleBO p1, TAVehicleBO p2)
		{
			if(p1.getFirstName() == null || p1.getLastName() == null || p2.getFirstName() == null || p2.getLastName() == null ){
				return 0;
			}
			
			if(p1.getLastName().equalsIgnoreCase(p2.getLastName())){
				return p1.getFirstName().compareToIgnoreCase(p2.getFirstName());
			}
			else{
				return p1.getLastName().compareToIgnoreCase(p2.getLastName());
			}
		}};*/
		
	
	
	
	
/*************/
	public String getUserRegion() {
		return getRomsLoggedInUser().getLocationCode();
	}

	public String getOpName() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");

		return roadOperation.getOperationName();
	}

	
	public Event setRegions(RequestContext context) {
		System.err.println("inside setRegions");
		try{
		//RequestContext context = RequestContextHolder.getRequestContext();

		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");

		String opCategory = roadOperation.getCategoryId();

	//	System.err.println("opCategory"+ opCategory);
		if (opCategory.equals(Constants.Category.REGIONAL)) {
			List<String> reg = new ArrayList<String>();
			// System.out.println("Region: " +
			// getRomsLoggedInUser().getLocationCode());
			// reg.add(getRomsLoggedInUser().getLocationCode());
			reg.add(getRomsLoggedInUser().getLocationCode());
			roadOperation.setSelectedRegions(reg);
		}

		
		
		/**
		 * Delete ALL teams once the Category has changed
		 */
		context.getFlowScope().put("resourcesRetrieved", "false");
		if(roadOperation.getTeams()!= null){
			for(TeamView team : roadOperation.getTeams()){//delete all teams
				team.setSelected("true");
			}
			System.err.println("Before roadOperation.getTeams()"+ roadOperation.getTeams().size());
			List<TeamView> remainTeams = deleteNewOpTeams(roadOperation,context); //replaces the code above since it will be used elsewhere
			roadOperation.getTeams().clear();
			System.err.println("After roadOperation.getTeams()"+ roadOperation.getTeams().size());
			context.getFlowScope().put("teamDatatable", null);
			
			//force users' to re-evaluate these steps
			context.getFlowScope().put("completeOpResource", null);
			context.getFlowScope().put("completeOpCourtDetails", null);
			context.getFlowScope().put("completeOpReview", null);
			//
		
		}
		
		//System.err.println("opCategory"+ roadOperation.getCategoryId());
		context.getFlowScope().put("operation", roadOperation);
		
		return success();
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return error();
		}
	}
	
	

	public Event setTeamLead() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");

		String teamLeadID = roadOperation.getCurrentTeam().getTeamLeadId();
		List<TAStaffBO> staffs = roadOperation.getListOfStaff().getTarget();
		//this.getAvailableStaff();// roadOperation.getListOfStaff().getSource();

	//	System.out.println("Possible TeamLeads before "
	//			+ roadOperation.getPossibleTeamLeads().size());

		for (TAStaffBO taStaffBO : staffs) {
			System.err.println("Going through staff " + taStaffBO.getStaffId()
					+ "---" + teamLeadID);
			if (taStaffBO.getStaffId().equals(teamLeadID)) {
				System.err.println("$$$$$$$$Setting Team Lead " + taStaffBO);
				roadOperation.getCurrentTeam().setTeamLead(taStaffBO);
				// this.setSelectedTeamLead(taStaffBO);
				break;
			}
		}

		/*System.out.println("Possible TeamLeads AFTER"
				+ roadOperation.getPossibleTeamLeads().size());
		System.err.println("TEAM LEAD ID " + teamLeadID);*/

		// context.getFlowScope().put("operation",roadOperation);

		return success();
	}

	public Event setCourt() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadOperationView roadOperation = (RoadOperationView) context
				.getFlowScope().get("operation");
		List<CourtBO> listOfCourts = (List<CourtBO>) context.getFlowScope()
				.get("courtList");

		CourtBO operCrt = new CourtBO();

		String courtID = "" + roadOperation.getCourtId();
		String individualCourtID = "";

		if (StringUtil.isSet(courtID) && !courtID.equalsIgnoreCase("0")) {
			for (CourtBO courtBO : listOfCourts) {
				System.err.println("Going through courts "
						+ courtBO.getCourtId() + "---" + courtID);

				individualCourtID = "" + courtBO.getCourtId();

				if (individualCourtID.equals(courtID)) {
					operCrt.setCourtId(courtBO.getCourtId());
					operCrt.setDescription(courtBO.getDescription());
					operCrt.setShortDescription(courtBO.getShortDescription());

					roadOperation.setCourtBO(operCrt);
					/*
					 * roadOperation.setCourtAddress(AddressFormattingUtil.
					 * getFormattedAddress(courtBO.getMarkText(),
					 * courtBO.getParishName(),courtBO.getParishCode(),
					 * courtBO.getPoBoxNo(), courtBO.getPoLocationName(),
					 * courtBO.getStreetName(), courtBO.getStreetNo()));
					 */
					roadOperation.setCourtAddressLine1(getAddressLine1(
							courtBO.getStreetName(), courtBO.getMarkText(),
							courtBO.getStreetNo()));
					roadOperation.setCourtAddressLine2(getAddressLine2(
							courtBO.getPoBoxNo(), courtBO.getPoLocationName(),
							courtBO.getParishName()));

					break;
				}
			}
		} else {
			roadOperation.setCourtBO(null);
			// roadOperation.setCourtAddress(null);
			roadOperation.setCourtAddressLine1(null);
			roadOperation.setCourtAddressLine2(null);

		}

		context.getFlowScope().put("operation", roadOperation);

		return success();
	}

	public String getStatusOfSelectedOp(DataTableMemory dataTable) {

		RoadOperationBO roadOpBO = new RoadOperationBO();

		RoadOperationDetailsSumary summary = (RoadOperationDetailsSumary) dataTable.pageElements
				.getSelectedRow();

		roadOpBO = summary.getRoadOp();

		String stat = roadOpBO.getStatusId();

		System.err.println("OpStat " + stat);

		return stat;
	}

	public boolean goToReview(String status, String specificAction) {
		if (specificAction != null) {
			if (specificAction.equalsIgnoreCase("authorize")) {
				return true;
			}
		}

		// Please note that the commented out code below is because from search
		// if the operation status is Scheduling then go to the review screen
		// and not step 1
		if ((status == null)) // ||
								// (status.equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING)))
		{
			// if(status == null)
			// {
			RequestContextHolder.getRequestContext().getFlowScope()
					.put("opStatus", "NONE");
			// }

			return false;
		}

		return true;

	}
	
	public boolean goToResources(String fromStart) {
			
		if(fromStart!=null){
			if (fromStart.equalsIgnoreCase("yes")) {
				RequestContext requestContext = RequestContextHolder.getRequestContext();
				setCompleteValuesFormSearch(requestContext);
				return true;
			}
		}
		
		return false;

	}


	public void getReasons(RequestContext context) {
		List<SelectItem> canReasons = new ArrayList<SelectItem>();
		List<SelectItem> terminReasons = new ArrayList<SelectItem>();

		canReasons = getActiveReasonsByModuleType(Constants.ReasonTypeCode.ROAD_OPERATION_CANCELLED);
		terminReasons = getActiveReasonsByModuleType(Constants.ReasonTypeCode.ROAD_OPERATION_TERMINATED);

		if (canReasons.isEmpty())
			canReasons = new ArrayList<SelectItem>();
		if (terminReasons.isEmpty())
			terminReasons = new ArrayList<SelectItem>();

		context.getFlowScope().put("reasonListCancel", canReasons);
		context.getFlowScope().put("roadOpTerminateReasons", terminReasons);
	}

	/**
	 * PURPOSE: remove JP based on parish removed
	 * @author kpowell
	 * @date 2015-02-10
	 * @param parishCode
	 * @param roadOperation
	 */
	public void removeJPByParish(String parishCode, RoadOperationView roadOperation){
		
		List<JPBO> newJPTarget = new ArrayList<JPBO>();
		Iterator<JPBO> iterJp = roadOperation.getListOfJP().getTarget().iterator();
		
		List<ParishBO> parishes = roadOperation.getCurrentTeam().getSelectedTeamParishList();	//excludes the parish that was removed
		Set<String> parishCodes = new HashSet<String>();
		Set<String> removeParishCodes = new HashSet<String>();
		//System.err.println("Parishes " + parishes);	
		if (parishes != null) {
			for (ParishBO parishBO : parishes) {
				System.err.println("Parishes for getting JP "
						+ parishBO.getDescription());
				parishCodes.add(parishBO.getParishCode());
			}
		}
		//if Kingston parish is in the list, add St. Andrew parish and vice versa
		if(parishCode.equalsIgnoreCase(Constants.ParishKingstonAndStAndrew.KINGSTON)||parishCode.equalsIgnoreCase(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
			
			if(parishCodes.contains(Constants.ParishKingstonAndStAndrew.KINGSTON) || parishCodes.contains(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
				//DONOT remove any JP as one of the following parish is still in the selected list of parishes
				return;
			}else{
				
				//if only one of the following parishes [KGN, STANDREW] is to deleted, delete all JP associated with both
				removeParishCodes.add(parishCode);
				if(parishCode.equalsIgnoreCase(Constants.ParishKingstonAndStAndrew.KINGSTON)){
					removeParishCodes.add(Constants.ParishKingstonAndStAndrew.ST_ANDREW);
					
				}else if(parishCode.equalsIgnoreCase(Constants.ParishKingstonAndStAndrew.ST_ANDREW)){
					removeParishCodes.add(Constants.ParishKingstonAndStAndrew.KINGSTON);
					
				}
			}
		}
		else{
			removeParishCodes.add(parishCode);
		}
		
		//OTHERWISE continue to remove JP
		try{
			System.err.println("removeParishCodes size"+ removeParishCodes.size());
			while(iterJp.hasNext()){
				JPBO jpDetails = iterJp.next();				
				//if(!jpDetails.getParishCode().equalsIgnoreCase(parishCode)){
				if(!removeParishCodes.contains(jpDetails.getParishCode())){
					newJPTarget.add(jpDetails);				
				}else{
					//add to source
					//roadOperation.getListOfJP().getSource().add(jpDetails);
					System.err.println(jpDetails.getPersonBO().getFullName()+">>"+ jpDetails.getParishCode()+", removed from target");
				}			
			}
			
			roadOperation.getListOfJP().setTarget(newJPTarget);
		}catch(Exception e){
			e.printStackTrace();
		}
		//return newJPTarget;
		
	}
	
	public void populateJPList(RoadOperationView roadOperation) {
		// RoadOperationView roadOperation = (RoadOperationView)
		// RequestContextHolder.getRequestContext()
		// .getFlowScope().get("operation");

		availResourceCriteria = populateAvailResourceCriteriaBO(roadOperation,
				"N/A");
		System.err.println("Inside populateJPList()::");
		List<JPBO> availableJP = new ArrayList<JPBO>();
		List<JPBO> selectedJP = new ArrayList<JPBO>();
		List<JPBO> residualJP = new ArrayList<JPBO>();

		if (roadOperation.getListOfJP() != null) {
			
			selectedJP = getJPForTeam(roadOperation.getCurrentTeam(),RequestContextHolder.getRequestContext());//roadOperation.getListOfJP().getTarget();
			System.err.println("selectedJP =" + selectedJP.size());
		}

		try {
			availableJP = getRoadOperationService().getAvailableJPResource(
					availResourceCriteria);

			roadOperation.setListOfJPAux(availableJP);

			System.err.println("roadOperation.ListOfJPAux=="+ roadOperation.getListOfJPAux().size());
			// remove those previously selected for a team
			residualJP.addAll(availableJP);
			residualJP = getResidualJP(residualJP);
			//residualJP.addAll(availableJP);
			
			roadOperation.setListOfJP(new DualListModel<JPBO>(residualJP,
					selectedJP));

			roadOperation.setAvailableJP(residualJP);
			
			System.err.println("After updates roadOperation.ListOfJPAux=="+ roadOperation.getListOfJPAux().size());

			if(roadOperation.getListOfJP()!= null){
				Collections.sort(roadOperation.getListOfJP().getSource(),jpComperator );
				Collections.sort(roadOperation.getListOfJP().getTarget(),jpComperator );
			}

		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("populateJPList no record found");
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("populateJPList failed");
			e.printStackTrace();
		}
		
		finally{
			System.err.println("inside finally");
			if (availableJP.size()==0) {
				//availableJP = new ArrayList<JPBO>();
				System.err.println("inside if (availableJP == null)");
				System.err.println("no jp available intializing list");
				roadOperation.setListOfJP(new DualListModel<JPBO>(new ArrayList<JPBO>(),
						new ArrayList<JPBO>()));				

			}
			
			System.err.println("After JPLIST Src"+ roadOperation.getListOfJP().getSource().size() );
			System.err.println("After JPLIST Target"+ roadOperation.getListOfJP().getTarget().size() );
			
			RequestContextHolder.getRequestContext().getFlowScope()
			.put("operation", roadOperation);
		}

		
		

		/*
		 * TODO finish testing roadOperation.setListOfJPAux(availableJP);
		 * 
		 * availableJP = getResidualJP(availableJP);
		 * 
		 * roadOperation.setListOfJP(new
		 * DualListModel<JPBO>(availableJP,selectedJP));
		 * 
		 * roadOperation.setAvailableJP(availableJP);
		 * 
		 * RequestContextHolder.getRequestContext().getFlowScope().put("operation"
		 * ,roadOperation);
		 */
	}

	
	public void populateJPList(RoadOperationView roadOperation, String mode) {
		// RoadOperationView roadOperation = (RoadOperationView)
		// RequestContextHolder.getRequestContext()
		// .getFlowScope().get("operation");

		availResourceCriteria = populateAvailResourceCriteriaBO(roadOperation,
				mode);
		System.err.println("Inside populateJPList()::");
		List<JPBO> availableJP = new ArrayList<JPBO>();
		List<JPBO> selectedJP = new ArrayList<JPBO>();
		List<JPBO> residualJP = new ArrayList<JPBO>();

		if (roadOperation.getListOfJP() != null) {
			selectedJP = roadOperation.getListOfJP().getTarget();
			System.err.println("selectedJP =" + selectedJP.size());
		}

		try {
			availableJP = getRoadOperationService().getAvailableJPResource(
					availResourceCriteria);

			roadOperation.setListOfJPAux(availableJP);

			System.err.println("roadOperation.ListOfJPAux=="+ roadOperation.getListOfJPAux().size());
			// remove those previously selected for a team
			residualJP.addAll(availableJP);
			residualJP = getResidualJP(residualJP);
			//residualJP.addAll(availableJP);
			

			roadOperation.setListOfJP(new DualListModel<JPBO>(residualJP,
					selectedJP));

			roadOperation.setAvailableJP(residualJP);
			
			System.err.println("After updates roadOperation.ListOfJPAux=="+ roadOperation.getListOfJPAux().size());

			if(roadOperation.getListOfJP().getSource()!= null){
				Collections.sort(roadOperation.getListOfJP().getSource(),jpComperator );
				Collections.sort(roadOperation.getListOfJP().getTarget(),jpComperator );
			}

		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("populateJPList no record found");
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("populateJPList failed");
			e.printStackTrace();
		}
		
		finally{
			System.err.println("inside finally");
			if (availableJP.size()==0) {
				//availableJP = new ArrayList<JPBO>();
				System.err.println("inside if (availableJP == null)");
				System.err.println("no jp available intializing list");
				roadOperation.setListOfJP(new DualListModel<JPBO>(new ArrayList<JPBO>(),
						new ArrayList<JPBO>()));				

			}
			
			System.err.println("After JPLIST Src"+ roadOperation.getListOfJP().getSource().size() );
			System.err.println("After JPLIST Target"+ roadOperation.getListOfJP().getTarget().size() );
			
			RequestContextHolder.getRequestContext().getFlowScope()
			.put("operation", roadOperation);
		}

		
		

		/*
		 * TODO finish testing roadOperation.setListOfJPAux(availableJP);
		 * 
		 * availableJP = getResidualJP(availableJP);
		 * 
		 * roadOperation.setListOfJP(new
		 * DualListModel<JPBO>(availableJP,selectedJP));
		 * 
		 * roadOperation.setAvailableJP(availableJP);
		 * 
		 * RequestContextHolder.getRequestContext().getFlowScope().put("operation"
		 * ,roadOperation);
		 */
	}
	
	public static Boolean isDisplayJPMsg() {
		
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		
		try{
			
		if(roadOperationView.getListOfJP() == null ){
			System.err.println("roadOperationView.getListOfJP() == null ");
				return true;
		}else	
		if(roadOperationView.getListOfJP().getSource()== null && 
				roadOperationView.getListOfJP().getTarget()== null){
			System.err.println("roadOperationView.getListOfJP().getSource()== null && roadOperationView.getListOfJP().getTarget()== null");
			return true;
		}else
		if (roadOperationView.getListOfJP().getSource().size()== 0  &&
				roadOperationView.getListOfJP().getTarget().size()== 0){
			System.err.println("roadOperationView.getListOfJP().getSource().size()== 0  && roadOperationView.getListOfJP().getTarget().size()== 0");
			return true;
		}
			
				
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
		return false;
		
	}
	public void onTabChange(TabChangeEvent event) {

		try{
		System.err.println("onTabChange()::");
		System.err.println("%%%%%%%%% " + event.getTab().getId());

		String tab = event.getTab().getId();

		RoadOperationView roadOperation = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		availResourceCriteria = populateAvailResourceCriteriaBO(roadOperation,
				"N/A");

		List<JPBO> availableJP = new ArrayList<JPBO>();
		List<JPBO> selectedJP = new ArrayList<JPBO>();

		
		String fromWhere = (String) RequestContextHolder
				.getRequestContext().getFlowScope().get("fromWhere");
		System.err.println("fromWhere::" + fromWhere);
		String allAvailJPRetrieved = (String) RequestContextHolder.getRequestContext().getFlowScope().get("allAvailJPRetrieved");

		if(allAvailJPRetrieved== null &&(fromWhere != null && fromWhere.equalsIgnoreCase(FROM_SEARCH))){
			try {
	
				
				if (tab.equals("jp-tab"))  {
					if (roadOperation.getListOfJP() == null) {
	
						availableJP = getRoadOperationService()
								.getAvailableJPResource(availResourceCriteria);
	
						roadOperation.setListOfJPAux(availableJP);		
						System.err.println("setListOfJPAux"+ roadOperation.getListOfJPAux().size());
						RequestContextHolder.getRequestContext().getFlowScope().put("allAvailJPRetrieved", "Y");
						
					}
	
				
	
				RequestContextHolder.getRequestContext().getFlowScope()
						.put("operation", roadOperation);
				}
			} catch (InvalidFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}

	}

	public void getStrategyRules(RequestContext context) {

		List<StrategyRuleBO> stratRuleBOs = getStrategyRulesHelper(context);

		context.getFlowScope().put("strategyRules", stratRuleBOs);
	}

	public List<StrategyRuleBO> getStrategyRulesHelper(RequestContext context) {
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		// List<StrategyBO> selectedStrategies =
		// (List<StrategyBO>)context.getFlowScope().get("listOfSelectedStrategies");
		String listOfStratInTarget = roadOpView.getListOfSelectedStrategyID();
		List<StrategyBO> selectedStrategies = getStrategyObjects(context,
				listOfStratInTarget);

		if (selectedStrategies.size() > 0) {
			System.out.println("target "
					+ selectedStrategies.get(0).getStrategyDescription()
					+ " size " + selectedStrategies.size());
		}
		List<Integer> selectedStratIDs = new ArrayList<Integer>();

		for (StrategyBO strat : selectedStrategies) {
			selectedStratIDs.add(strat.getStrategyId());
		}

		RoadOperation roadOperationService = getRoadOperationService();
		
		List<StrategyRuleBO> stratRuleBOs = new ArrayList<StrategyRuleBO>();

		try {
			if (selectedStratIDs.size() > 0) {// kpowell
				stratRuleBOs = roadOperationService
						.getOperationStrategyRule(selectedStratIDs);
			}
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stratRuleBOs;
	}

	/**
	 * Used to retrieve courts by parish
	 * 
	 * @param context
	 * @return
	 */
	public Event getActiveCourtsByParish(RequestContext context) {

		List<CourtBO> courtList = new ArrayList<CourtBO>();
		CourtCriteriaBO courtCriteria = new CourtCriteriaBO();
		courtCriteria.setStatusId(Constants.Status.ACTIVE);
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		HashMap<String, ParishBO> selParishes = roadOperationView
				.getHashSelectedParishes();

		Set<String> selParCodes = new HashSet<String>();
		//selParCodes.addAll(selParishes.keySet());
		
		/**2014-10-29- kpowell -REDO**/
		if (roadOperationView.getOperationParishList() != null) {
			for (ParishBO parishBO : roadOperationView.getOperationParishList()) {
				System.err.println("Parishes for getting JP "
						+ parishBO.getDescription());
				selParCodes.add(parishBO.getParishCode());
			}
		}
		/********/

		try
		{
			//if Kingston parish is in the list, add St. Andrew parish and vice versa
			if(selParCodes.contains(Constants.ParishKingstonAndStAndrew.KINGSTON) || selParCodes.contains(Constants.ParishKingstonAndStAndrew.ST_ANDREW))
			{
				selParCodes.add(Constants.ParishKingstonAndStAndrew.KINGSTON);
				selParCodes.add(Constants.ParishKingstonAndStAndrew.ST_ANDREW);
			}
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		for (String string : selParCodes) {

			courtCriteria.setParishCode(string);
			try {
				courtList.addAll(getMaintenanceService().lookupCourt(
						courtCriteria));
				Collections.sort(courtList, new Comparator<CourtBO>() {
					@Override
					public int compare(CourtBO result1, CourtBO result2) {
						return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
					}
				});
			} catch (NoRecordFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RequiredFieldMissingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.err.println("courtList::" + courtList.size());

		context.getFlowScope().put("courtList", courtList);

		return success();
	}

	public void populateStrategyTarget(RequestContext context) {
		RoadOperationView roadOperationView = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		String fromWhere = (String) context.getFlowScope().get("fromWhere");

		if (fromWhere != null) {
			if (!fromWhere.equals(FROM_SEARCH)) {
				List<StrategyBO> selectedStrat = (List<StrategyBO>) context
						.getFlowScope().get("listOfSelectedStrategies");

				if (selectedStrat == null) {
					selectedStrat = new ArrayList<StrategyBO>();
				}

				roadOperationView.getListOfStrategies()
						.setTarget(selectedStrat);
				
				//sort the source picklist
				if(roadOperationView.getListOfStrategies()!= null){
					Collections.sort(roadOperationView.getListOfStrategies().getSource(),strategyComperator );	
					Collections.sort(roadOperationView.getListOfStrategies().getTarget(),strategyComperator );
				}
				
				context.getFlowScope().put("operation", roadOperationView);
				return;
			}
			else
			{
				//oneal: straties are not being validated from search unless you first go to step 2
				StringBuilder builder = new StringBuilder();
				
				for(StrategyBO strategy : roadOperationView.getListOfStrategies().getTarget())
				{
					builder.append(strategy.getStrategyId() + ",");
					//System.err.println("strats selected>>"+ strategy.getStatusDescription());
				}
				
				roadOperationView.setListOfSelectedStrategyID(builder.toString());
				
			}
		}

		// List<StrategyBO> selectedStrat2 =
		// roadOperationView.getSelectedStrategies();
		// System.err.println("###### STRATS " + selectedStrat2);
		// String helperStr = "";
		// for (StrategyBO strategyBO : selectedStrat2) {
		// System.err.println("## " + strategyBO.getStrategyId());
		// helperStr += helperStr + strategyBO.getStrategyId() + ",";
		// }
		//
		// roadOperationView.setListOfSelectedStrategyID(helperStr);
		// context.getFlowScope().put("operation", roadOperationView);
		return;
	}

	public ArteryDataModel getArteryDataMod() {
		return arteryDataMod;
	}

	public void setArteryDataMod(ArteryDataModel arteryDataMod) {
		this.arteryDataMod = arteryDataMod;
	}

	public ParishDataModel getParishDataMod() {
		return parishDataMod;
	}

	public void setParishDataMod(ParishDataModel parishDataMod) {
		this.parishDataMod = parishDataMod;
	}

	public LocationDataModel getLocationDataMod() {
		return locationDataMod;
	}

	public void setLocationDataMod(LocationDataModel locationDataMod) {
		this.locationDataMod = locationDataMod;
	}

	public List<LocationBO> getListLocations() {
		return listLocations;
	}

	public void setListLocations(
			List<LocationBO> listLocations) {
		this.listLocations = listLocations;
	}

	public List<ParishBO> getListParish() {
		return listParish;
	}

	public void setListParish(List<ParishBO> listParish) {
		this.listParish = listParish;
	}

	public List<ArteryBO> getListArtery() {
		return listArtery;
	}

	public void setListArtery(
			List<ArteryBO> listArtery) {
		this.listArtery = listArtery;
	}

	public List<TAStaffBO> getAvailableStaff() {
		return availableStaff;
	}

	public void setAvailableStaff(List<TAStaffBO> availableStaff) {
		this.availableStaff = availableStaff;
	}

	/**
	 * This function sets up data to be displayed on the terminate road
	 * operation popup.
	 * 
	 * @param context
	 */
	public void terminateOperationPopup(RequestContext context) {
		RoadOperationView roadOpView = (RoadOperationView) context
				.getFlowScope().get("operation");

		try {
			Integer countIncompleteRoadChecks = 0;

			try {
				RoadCompliancyCriteriaBO roadCompliancyCriteriaBO = new RoadCompliancyCriteriaBO();

				roadCompliancyCriteriaBO.setOperationID(roadOpView
						.getRoadOperationId());

				List<ComplianceBO> complianceList = this
						.getRoadCompliancyService().lookupRoadCompliance(
								roadCompliancyCriteriaBO);

				for (ComplianceBO comp : complianceList) {
					if (comp.getStatusId() == null
							|| comp.getStatusId().equalsIgnoreCase(
									Constants.Status.ROAD_CHECK_INCOMPLETE)) {
						countIncompleteRoadChecks++;

					}

				}
			} catch (Exception e) {
				// No RecordFoundExxception may be thrown.
			}

			/*
			 * RefCodeCriteriaBO refCodeCrit = new RefCodeCriteriaBO();
			 * 
			 * refCodeCrit.setTableName("roms_reason");
			 * refCodeCrit.setStatus(Constants.Status.ACTIVE);
			 * 
			 * RefCodeCriteriaBO.Filter filter = new RefCodeCriteriaBO.Filter();
			 * Entry entry = new Entry(); entry.setKey("type");
			 * entry.setValue(Constants
			 * .ReasonTypeCode.ROAD_OPERATION_TERMINATED);
			 * filter.getEntry().add(entry);
			 * 
			 * refCodeCrit.setFilter(filter);
			 * 
			 * List<RefCodeBO> refCodes =
			 * this.getReferenceCodePortProxy().getReferenceCode(refCodeCrit);
			 * 
			 * List<SelectItem> roadOpTerminateReasons = new
			 * ArrayList<SelectItem>();
			 * 
			 * for(RefCodeBO refCode : refCodes) {
			 * roadOpTerminateReasons.add(new SelectItem(refCode.getCode(),
			 * refCode.getDescription())); }
			 * 
			 * context.getFlowScope().put("roadOpTerminateReasons",
			 * roadOpTerminateReasons);
			 */
			context.getFlowScope().put("countIncompleteRoadChecks",
					countIncompleteRoadChecks);
		} catch (Exception exe) {
			exe.printStackTrace();
		}
	}

	/**
	 * This function cvalculates the days and hours between selected actual
	 * operation dates.
	 * 
	 * @param event
	 */
	public void calculateActualDuration() {
		RoadOperationView roadOperation = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		System.err.println("inside calculateActualDuration()");
		RoadOperationBO roadOp = (RoadOperationBO) RequestContextHolder
				.getRequestContext().getFlowScope().get("roadOpBO");
		System.err.println("roadOp actStartDtime" + roadOp.getActualStartDtime());
		System.err.println("roadOpertation actStartDtime" + roadOperation.getActualStartDtime());
		System.err.println("roadOp stat: " + roadOp == null);
		if (roadOp != null)
			try {
				
				roadOp.setActualEndDtime(roadOp.getActualEndDate());
				roadOp.setActualStartDtime(roadOp.getActualStartDate());
				roadOperation.setActualEndDtime(roadOp.getActualEndDate());
				roadOperation.setActualStartDtime(roadOp.getActualStartDate());				
				//roadOperation = this.convertBOtoView(roadOp); //removed since this roadOpView was alreadty initialized

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		Date start = roadOperation.getActualStartDtime();
		Date end = roadOperation.getActualEndDtime();

		
		/*System.err.println("getActualStartDtime()"+roadOperation.getActualStartTime());
		System.err.println("getActualEndDtime()"+roadOperation.getActualEndTime());*/
		
		roadOperation.setActualNumOfDays(getDurationNumOfDays(start, end));
		roadOperation.setActualNumOfHours(getDurationNumOfHours(start, end));
		roadOperation.setActualNumOfMinutes(getDurationNumOfMinutes(start, end));		

		RequestContextHolder.getRequestContext().getFlowScope()
				.put("operation", roadOperation);
		
		RequestContextHolder.getRequestContext().getFlowScope()
		.put("operationActualNumOfDays", roadOperation.getActualNumOfDays());
		
		RequestContextHolder.getRequestContext().getFlowScope()
		.put("operationActualNumOfHours", roadOperation.getActualNumOfHours());
		
		RequestContextHolder.getRequestContext().getFlowScope()
		.put("operationActualNumOfMinutes", roadOperation.getActualNumOfMinutes());
		
		System.err.println("exit calculateActualDuration()");

	}
	
	/**
	 * This function calculates the days and hours between selected actual
	 * operation dates for Terminate.
	 * 
	 * @param event
	 */
	@SuppressWarnings("unused")
	public void calculateActualDurationForTerminate() {
		RoadOperationView roadOperation = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");

		System.err.println("inside calculateActualDuration()");
		RoadOperationBO roadOp = (RoadOperationBO) RequestContextHolder
				.getRequestContext().getFlowScope().get("roadOpBO");
		System.err.println("roadOp actStartDtime" + roadOp.getActualStartDtime());
		System.err.println("roadOpertation actStartDtime" + roadOperation.getActualStartDtime());
		System.err.println("roadOp stat: " + roadOp == null);
		if (roadOp != null){
			try {
				System.err.println("Inside If !!!!!!!!");
				roadOp.setActualEndDtime(roadOp.getActualEndDate());
				roadOp.setActualStartDtime(roadOp.getActualStartDate());
				if(roadOperation.getActualStartDtime() != null){
					try {
						roadOp.setActualStartDtime(roadOperation.getActualStartDtime());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					roadOperation.setActualStartDtime(roadOp.getActualStartDate());
				}
				if (roadOperation.getActualEndDtime() != null) {
					 try {
						roadOp.setActualEndDtime(roadOperation.getActualEndDtime());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					roadOperation.setActualEndDtime(roadOp.getActualEndDate());
				}
								
				//roadOperation = this.convertBOtoView(roadOp); //removed since this roadOpView was alreadty initialized

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Date start = roadOperation.getActualStartDtime();
		Date end = roadOperation.getActualEndDtime();

		
		/*System.err.println("getActualStartDtime()"+roadOperation.getActualStartTime());
		System.err.println("getActualEndDtime()"+roadOperation.getActualEndTime());*/
		
		roadOperation.setActualNumOfDays(getDurationNumOfDays(start, end));
		roadOperation.setActualNumOfHours(getDurationNumOfHours(start, end));
		roadOperation.setActualNumOfMinutes(getDurationNumOfMinutes(start, end));		

		RequestContextHolder.getRequestContext().getFlowScope()
				.put("operation", roadOperation);
		
		RequestContextHolder.getRequestContext().getFlowScope()
		.put("operationActualNumOfDays", roadOperation.getActualNumOfDays());
		
		RequestContextHolder.getRequestContext().getFlowScope()
		.put("operationActualNumOfHours", roadOperation.getActualNumOfHours());
		
		RequestContextHolder.getRequestContext().getFlowScope()
		.put("operationActualNumOfMinutes", roadOperation.getActualNumOfMinutes());
		
		System.err.println("exit calculateActualDuration()");
	

	}
	
	public void filterLocations() {
		RequestContext context = RequestContextHolder.getRequestContext();

		RoadOperationCriteriaBO criteria = (RoadOperationCriteriaBO) context
				.getFlowScope().get("criteria");

		if (StringUtil.isSet(criteria.getParishCode()))
			this.getLocationsFilterbyParishCode(context,
					criteria.getParishCode());
		else
			this.getLocations(context);

	}
	
	public void closeRoadOp()
	{
		RequestContext context = RequestContextHolder.getRequestContext();
		
		RoadOperationView roadOperation = (RoadOperationView) RequestContextHolder
				.getRequestContext().getFlowScope().get("operation");
		
		String orginalStatus = roadOperation.getStatusId();
		
		try{
			RoadOperationBO roadOpToSave = new RoadOperationBO();
			
			
			
			roadOperation.setStatusId(Constants.Status.ROAD_OPERATION_CLOSED);
							
			roadOpToSave = convertViewToBO(roadOperation);
			
			roadOpToSave.setCurrentUsername(this.getRomsLoggedInUser()
					.getUsername());
			
			this.getRoadOperationService().closeRoadOperation(roadOpToSave);
			
			roadOperation.setStatusDescription(Constants.Status.ROAD_OPERATION_CLOSED_DESC);
			
			this.addInfoMessage(context, "RoadOperationClosed");
		}
		catch(Exception e){
			e.printStackTrace();
			roadOperation.setStatusId(orginalStatus);
			
			if(e.getMessage().toLowerCase().contains("unprinted")){
				this.addErrorMessageText(context, e.getMessage());
			}
			else{
				this.addErrorMessage(context, "RecordUpdateErr");
			}
				
			
		}
	}
	
	/**
	 * Conditions under which operation cannot be edited
	 * @return
	 */
	public boolean operationScheduleEditable(String component){
	
		//System.err.println("inside operationScheduleEditable(String "+component+")");
		RequestContext context = RequestContextHolder.getRequestContext();
		
		RoadOperationView roadOperation = (RoadOperationView) context.getFlowScope().get("operation");
		
		String allowUpdate = (String) context.getFlowScope().get("allowUpdate");
		String saveOrUpdate = (String) context.getFlowScope().get("saveOrUpdate");
				
		
		
		//A user who is not mapped cannot edit the schedule
				if(!roadOperation.isValidUserToScheduleOp()){
					return false;
				}
				
		//Validate Steps
		if(component.equalsIgnoreCase("menu")){//use on menu and buttons
			/*if(roadOperation.getStatusId().equals(Constants.Status.ROAD_OPERATION_NO_ACTION)){
			return false;
			}else{*/
			/**		if(!(roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING) && roadOperation.getStatusId().equalsIgnoreCase("NONE" )&&
							roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION))
						&& !allowUpdate.equalsIgnoreCase("true")
						&& !roadOperation.isValidUserToScheduleOp()){
						//allowUpdate = true;Only allow updates where the operations origin of creation is the same as the current user; 
						//user has special permission
						System.err.println("1 exit operationScheduleEditable(String component)");
						return false;
					}
					
				//}
				if(roadOperation.getAuthorized().equals("Y") && roadOperation.getBackDated().equals("Y")){
					System.err.println("2 exit operationScheduleEditable(String component)");
					return false;
				}
				**/
				if((roadOperation.getStatusId().equalsIgnoreCase("NONE" ) && saveOrUpdate.equalsIgnoreCase("save") ) || 
				(saveOrUpdate.equalsIgnoreCase("update") && roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING) ) 
				 && allowUpdate.equalsIgnoreCase("true") && (!(roadOperation.getAuthorized().equals("Y") && roadOperation.getBackDated().equals("Y")))){
				//	System.err.println("1 exit operationScheduleEditable(String "+component+")");
					 return true;
					 /*((operation.statusId =='NONE' &amp;&amp; flowScope.saveOrUpdate == 'save' ) || 
						(flowScope.saveOrUpdate == 'update' &amp;&amp; operation.statusId == 'RO_SCH') ) &amp;&amp; allowUpdate == 'true' &amp;&amp; 
						(!(operation.authorized == 'Y' &amp;&amp; operation.backDated == 'Y'))}*/
						
				
				/*THIS IS WHAT WS ON THE PAGE
				 * ((operation.statusId =='NONE' &amp;&amp; flowScope.saveOrUpdate == 'save' ) || 
									(flowScope.saveOrUpdate == 'update' &amp;&amp; operation.statusId == 'RO_SCH') )and allowUpdate == 'true' and (!(operation.authorized == 'Y' and operation.backDated == 'Y'))
											&amp;&amp; operation.validUserToScheduleOp
				 */
				 	
				}
				
				if(roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION)){
					//System.err.println("1a exit operationScheduleEditable(String "+component+")");
					
					//not backdated and authorized- 2014-12-15:kpowell
					if(!(roadOperation.getAuthorized().equals("Y") && roadOperation.getBackDated().equals("Y")) && allowUpdate.equalsIgnoreCase("true")){
						return  true;
					}
				}
		
		}else if(component.equalsIgnoreCase("singlecomp")){//single compnent
			
			if(roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_NO_ACTION)){
				//System.err.println("2 exit operationScheduleEditable(String "+component+")");
				return  true;
			}
	
		}else if(component.equalsIgnoreCase("tooltipcomp")){//tooltip component	
			/*System.err.println("OPERATION"+ org.fsl.roms.view.ObjectUtils.objectToString(roadOperation));
			System.err.println("saveOrUpdate" + saveOrUpdate+ " allowUpdate:"+ allowUpdate);*/
			if((roadOperation.getStatusId().equalsIgnoreCase("NONE" ) && saveOrUpdate.equalsIgnoreCase("save") ) || 
					(saveOrUpdate.equalsIgnoreCase("update") && roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_SCHEDULING) ) 
					 && allowUpdate.equalsIgnoreCase("true") && (!(roadOperation.getAuthorized().equals("Y") && roadOperation.getBackDated().equals("Y")))
					 && roadOperation.isValidUserToScheduleOp()
					 && !(roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CANCELLED) || 
					 		roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_TERMINATED) ||
					 		roadOperation.getStatusId().equalsIgnoreCase(Constants.Status.ROAD_OPERATION_CLOSED ))
					 ){
				
				
				//System.err.println("3 exit operationScheduleEditable(String "+component+")");		
				return true;
				/*THIS IS WHAT WS ON THE PAGE
				 * (flowScope.saveOrUpdate == 'update' &amp;&amp; operation.statusId == 'RO_SCH') and allowUpdate == 'true' and 
											(!(operation.authorized == 'Y' and operation.backDated == 'Y'))
											&amp;&amp; operation.validUserToScheduleOp &amp;&amp; operation.statusId != 'RO_CAN' &amp;&amp; operation.statusId != 'RO_TER' &amp;&amp; operation.statusId != 'RO_CLO'
											&amp;&amp; operation.statusId != 'RO_STA'
				 * 
				 */
						 
					 	
			}
			
		}
		
		return false;
	}
	
	private boolean checkIfPoliceFilterSet(RoadOperationView roadOperationView){
		if( !(roadOperationView.getPoliceOfficerCompNum() != null && roadOperationView.getPoliceOfficerCompNum() > 0)
				|| !(StringUtils.isEmpty(roadOperationView.getPoliceOfficerFirstName()))
				|| !(StringUtils.isEmpty(roadOperationView.getPoliceOfficerLastName()))
				|| !(StringUtils.isEmpty(roadOperationView.getPoliceStation()))){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void changeResourceTab(TabChangeEvent event) {
		
		Tab activeTab = event.getTab();
		
		RequestContext context = RequestContextHolder.getRequestContext();
		
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		if(activeTab.getId().toLowerCase().contains("police")  && roadOperationView.getListOfPolice() != null && roadOperationView.getListOfPolice().getSource().size() < 1){
			if(checkIfPoliceFilterSet(roadOperationView)){
				this.getPoliceListFilter(context);
			}
		}
		
	}
	
	/**ROMS1.0U-002(kpowell) :disable selction of parish/location that have NO associated location/artery respectively
	 * @author kpowell
	 * @param locationId
	 * @return
	 * function check if location has associated arteries
	 */
	public boolean hasArteries(Integer locationId){
		//System.err.println("boolean hasArteries called");
		RequestContext context = RequestContextHolder.getRequestContext();
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		List<ArteryBO> allArteries = roadOperationView.getAllArteriesMap().get(locationId);
		
		if(allArteries!=null){
			
			//System.err.println(locationId + ", "+allArteries.size()+" --TRUE");
			return true;
		}else{
			return false;
		}
	}
	
	/**ROMS1.0U-002(kpowell) :disable selction of parish/location that have NO associated location/artery respectively
	 * @author kpowell
	 * @param locationId
	 * @return
	 * function check if parish has associated locations
	 */
	public boolean hasLocations(String parishCode){
		//System.err.println("boolean hasLocations called");
		RequestContext context = RequestContextHolder.getRequestContext();
		RoadOperationView roadOperationView = (RoadOperationView) context
				.getFlowScope().get("operation");
		
		List<LocationBO> allLocations = roadOperationView.getAllLocationsMap().get(parishCode);
		
		if(allLocations!=null){
			
		//	System.err.println(parishCode + ", "+allLocations.size()+" --TRUE");
			return true;
		}else{
			return false;
		}
	}

	
	
	
	public void selectAllTeams(RequestContext context) {

		RoadOperationView roadOperationView = (RoadOperationView) context.getFlowScope().get("operation");

		if (roadOperationView.getTeams() != null && roadOperationView.getTeams().size() > 0) {
			for (TeamView team : roadOperationView.getTeams()) {
				
				if(roadOperationView.isAllTeamsSelected()== true){
					team.setSelected("true");
				}
				else{
					team.setSelected("false");
				}
			}
		}
	}

}
