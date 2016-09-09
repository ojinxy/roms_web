package org.fsl.roms.service.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.ArteryBean;
import org.fsl.roms.view.CourtBean;
import org.fsl.roms.view.ObjectUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.faces.model.OneSelectionTrackingListDataModel;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.scope.FlowScope;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.search.criteria.impl.CourtCriteriaBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.bo.ITAExaminerBO;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;

import org.fsl.roms.constants.Constants;
/**
 * 
 * @author bssintern
 *
 */

@Service
public class CourtMaintenanceServiceAction extends BaseServiceAction {

	private Maintenance courtMaintenanceService;
	
	public CourtMaintenanceServiceAction() {
		super();
	}
	
	public CourtMaintenanceServiceAction(
			Maintenance courtMaintenanceService) {
		this.courtMaintenanceService = new Maintenance();
	}
	
	/**
	 * This function searches based on Court criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {
		
		List<CourtBO> courts = null;

		try {

			CourtCriteriaBO criteria = (CourtCriteriaBO) context
					.getFlowScope().get("criteria");

		System.out.println("Criteria: " + criteria.getDescription() +" Stat "+  criteria.getStatusId() +" Parish "+ criteria.getParishCode());	
			
		if(criteria != null){
			System.out.println("CRITERIA IS NOT NULL " + ObjectUtils.objectToString(criteria));
		}
		
			courts = getMaintenanceService().lookupCourt(criteria);
			

			if (courts == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordsfound")
								.build());
				context.getFlowScope().put("courts",
						new CourtBean());
				return success();

			} 
			System.out.println("results after search " + courts.size());
			// set the entire list in datatable
			context.getFlowScope().put("courts",new CourtBean(courts));

		}catch(NoRecordFoundException e)
		{
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordsfound")
							.build());
			context.getFlowScope().put("courts",new CourtBean());
			return success();
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Inside catch");
			context.getMessageContext().addMessage(new MessageBuilder().error().code("search.failure").arg("Court").build());
			/*context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());*/
			context.getFlowScope().put("courts",
					new CourtBean());
			return error();
		}
		return success();
	}
	
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			CourtBean courtBean = (CourtBean)context.getFlowScope().get("courts");
			//System.err.println("Court: "+courtBean.getSelectedCourt().getShortDescription()+" id:"+courtBean.getSelectedCourt().getCourtId() +"parish "+ courtBean.getSelectedCourt().getParishCode());
			context.getFlowScope().put("court", courtBean.getSelectedCourt());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		CourtCriteriaBO criteria = (CourtCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		
		/*check if courts was in list prior to searching*/
		CourtBean courtBean = (CourtBean) context.getFlowScope().get("courts");

		if(courtBean.getCourtList().size() > 0)
		{
			search(context);
		}
	}
	
	public void handleClose(RequestContext context)
	{
		CourtBO court = (CourtBO)context.getFlowScope().get("court");
		
		court.setParishName(getParishDesc(court.getParishCode()));
		System.out.println("Parish Name " + court.getParishName() + " court " + court.getShortDescription());
		court.setStatusId(court.getStatusId());
		court.setAuditEntryBO(new AuditEntryBO());
		court.getAuditEntryBO().setUpdateUsername(getRomsLoggedInUser().getUsername());
		try {
			court.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			//WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			//System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			CourtBO courtBO = new CourtBO();
			courtBO.setStatusId("ACT");
			context.getFlowScope().put("court", courtBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Event updatecourt(RequestContext context) {
		System.err.println("Update fired");
		try {
			CourtBO court = (CourtBO) context.getFlowScope().get("court");
			CourtBean courtBean = (CourtBean)context.getFlowScope().get("courts");
			CourtCriteriaBO criteria = (CourtCriteriaBO) context
					.getFlowScope().get("criteria");
			String usernameToFullName = getMaintenanceService().usernameToFullName(getRomsLoggedInUser().getUsername());
						
			if (court == null)
				throw new Exception("Please enter valid details.");

			// get user details
			court.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

			if (validateArgs(court, context)) {
				return error();
			} 
			
			
			if(getMaintenanceService().courtExists(court.getCourtId())) 
			{
				System.err.println("Triggers if record exist court parish" + court.getParishCode() + "some " + court.getDescription());
				getMaintenanceService().updateCourt(court);
				
				getCourts(context);
				
				court.setParishName(getParishDesc(court.getParishCode()));
				System.out.println("Parish Name " + court.getParishName() + " court " + court.getShortDescription());
				court.setStatusDescription(court.getStatusId().equalsIgnoreCase(Constants.Status.ACTIVE)?"Active":"Inactive");
				court.setAuditEntryBO(new AuditEntryBO());
				court.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				court.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				
				if(courtBean.getCourtList().size() > 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}/*else{
					CourtCriteriaBO courtCrit = new CourtCriteriaBO();
					courtCrit.setCourtId(court.getCourtId());
					context.getFlowScope().put("criteria", courtCrit);
					search(context);
				}*/
					
				
				
				context.getMessageContext().addMessage(

						new MessageBuilder().info().code("RecordUpdated")
								.arg("Court").build());
				
			
				return success();
			}
			else
			{
				System.err.println("record doesnt exist");
				context.getMessageContext().addMessage(

						new MessageBuilder().error().code("RecordUpdateErr")
								.arg("Court").build());
				return error();
			}

			
		} catch(InvalidFieldException ex){
			ex.getStackTrace();
			context.getMessageContext().addMessage(

					new MessageBuilder().error().defaultText(ex.getMessage()).build());
			return error();
		}catch(ErrorSavingException ex){
			ex.getStackTrace();
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Court").build());
			return error();
		} catch (Exception e) {

			e.printStackTrace();
			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Court").build());
			return error();
		}

	}

	public Event savecourt(RequestContext context) {

		try {
			CourtBO court = (CourtBO) context.getFlowScope().get("court");
			CourtBean courtBean = (CourtBean)context.getFlowScope().get("courts");
			CourtCriteriaBO criteria = (CourtCriteriaBO) context
					.getFlowScope().get("criteria");
			String usernameToFullName = getMaintenanceService().usernameToFullName(getRomsLoggedInUser().getUsername());			
			if (court == null)
				throw new Exception("Please enter details.");

			// get user details
			court.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			court.setStatusId(Constants.Status.ACTIVE);
			if (validateArgs(court, context)) {
				return error();
			} else {

				getMaintenanceService().saveCourt(court);
				
				court.setParishName(getParishDesc(court.getParishCode()));
				System.out.println("Parish Name " + court.getParishName() + " court " + court.getShortDescription());
				court.setStatusDescription(court.getStatusId()==Constants.Status.ACTIVE?"Active":"Inactive");
				court.setAuditEntryBO(new AuditEntryBO());
				court.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				court.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				if(courtBean.getCourtList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}/*else{
					CourtCriteriaBO courtCrit = new CourtCriteriaBO();
					courtCrit.setCourtId(court.getCourtId());
					context.getFlowScope().put("criteria", courtCrit);
					search(context);
				}*/
			}
			//retrieve the courts
			getCourts(context);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Court").build());
			return success();
		} catch(InvalidFieldException ex){
			ex.getStackTrace();
			context.getMessageContext().addMessage(

					new MessageBuilder().error().defaultText(ex.getMessage()).build());
			return error();
		}catch(ErrorSavingException ex){
			ex.getStackTrace();
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Court").build());
			return error();
		}catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Court").build());
			return error();
		}

	}

	//Used to check if required fields are null
	//Returns message to indicate field is required if left null	
	private Boolean validateArgs(CourtBO court, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();


		if (StringUtils.isBlank(court.getShortDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Court Name ").build());
			error = true;

		} 
		if (StringUtils.isBlank(court.getDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Court Description ").build());
			error = true;

		} 
		if (StringUtils.isBlank(court.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status ").build());
			error = true;
		} 
				
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView courtAddress = new AddressView(court.getMarkText(), court.getParishCode(), court.getPoBoxNo(),
				court.getPoLocationName(), court.getStreetName(), court.getStreetNo(),
				null, null, null,court.getParishName());
		
		//global address validation - kpowell
		boolean errorFoundInAddress  = validateAddress(context, courtAddress);
        
        if(errorFoundInAddress){
              error = true;
        }
		//
		
		/*if (StringUtils.isBlank(court.getLocationDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Location Description").build());
			error = true;
		} */
		if (StringUtils.isBlank(court.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		} 
		
		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("courts");
		/*
		 * context.getFlowScope().put("courts", new
		 * OneSelectionTrackingListDataModel());
		 */
		// courts= new ArrayList<CourtBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		/*System.err.println(" cancel ");
		context.getViewScope().clear();*/
		//context.getFlowScope().put("criteria", new ArteryBean());
		CourtBO court = (CourtBO) context.getFlowScope().get("court");
				System.err.println("clear called");
				try {
					CourtBO courtBO = new CourtBO();
					courtBO.setStatusId("ACT");
					if(court != null){
						if(court.getCourtId() != null)
							courtBO.setCourtId(court.getCourtId());
					}
				System.err.println("New CourtBO object " + ObjectUtils.objectToString(courtBO));
					context.getFlowScope().put("court", courtBO);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		return success();

	}


	public Maintenance getCourtMaintenanceService() {
		return courtMaintenanceService;
	}

	public void setCourtMaintenanceService(
			Maintenance courtMaintenanceService) {
		this.courtMaintenanceService = courtMaintenanceService;
	}

	
	
}
