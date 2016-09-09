package org.fsl.roms.service.action;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.ReasonBean;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.ReasonBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.ReasonCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;
//import com.ibm.ws.webservices.xml.wassysapp.systemApp;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.webservices.ReferenceCode;

/**
 * 
 * @author bssintern
 * @update by sjames
 *
 */

@Service
public class ReasonMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance reasonMaintenanceService;
	private boolean addPressed;
	private boolean searchPressed = false;
	public boolean reasonError;
	String errorMsg;
	public ReasonMaintenanceServiceAction() {
		super();
		setSearchPressed(false);
	}
	
	public ReasonMaintenanceServiceAction(
			Maintenance reasonMaintenanceService) {
		this.reasonMaintenanceService = reasonMaintenanceService;
		
	}
	
	/**
	 * This function searches based on Reason criteria 
	 * @param context
	 * @return
	 */
	
	
	public void init(RequestContext context){
		
	}
	

	
	public List<RefCodeBO> getReasnTypeList(){
		ReferenceCode refCodeProxy = new ReferenceCode();
		
		RefCodeCriteriaBO refCodeCriteria = new RefCodeCriteriaBO();
				
		refCodeCriteria.setTableName("ROMS_reason");
		
		List<RefCodeBO> refRsltList = null;
		try {
			refRsltList = refCodeProxy.getReferenceCode(refCodeCriteria);
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
		
		return refRsltList;
				
	}
	
	
	public Event search(RequestContext context) {
		System.out.println("Search called");
		List<ReasonBO> reasons = null;

		try {

			ReasonCriteriaBO criteria = (ReasonCriteriaBO) context
					.getFlowScope().get("criteria");

			// do the search
			
			
			reasons = getMaintenanceService().lookupReason(criteria);


			if (reasons == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordfound")
								.build());
				context.getFlowScope().put("reasonBean",
						new ReasonBean());
				return success();

			} else if (reasons.size() == 1) {

				context.getFlowScope().put("reason", reasons.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("reasonBean",
						new ReasonBean(reasons));
				return yes();
			}
			setSearchPressed(true);
			// set the entire list in datatable
			context.getFlowScope().put("reasonBean",
					new ReasonBean(reasons));

		} 
		catch(NoRecordFoundException e)
		{
			context.getMessageContext().addMessage(new MessageBuilder().error().code("Norecordfound").build());
			context.getFlowScope().put("reasonBean",
					new ReasonBean());
			return error();
		}
		catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("reasonBean",
					new ReasonBean());
			return error();
		}
		return success();
	}
	
	@SuppressWarnings("null")
	public Event addReasonRow(RequestContext context){
		try{
			System.out.println("add reason triggered");
			
			/*ArrayList<ReasonBO> reasons =  (ArrayList<ReasonBO>) context.getFlowScope().get(
					"reasonBean");*/
			ReasonBean reasonBean = (ReasonBean) context.getFlowScope().get("reasonBean");
			
			ReasonBO reason =new ReasonBO();
			reason.setReasonId(1);
			reason.setType("R");
			reason.setReasonDescription("");
			reason.setStatusId("ACT");
			reasonBean.getReasonList().add(0,reason);
			System.err.println("reason list size after add " + reasonBean.getReasonList().size());
			context.getFlowScope().put("firstEdit", "true");
			context.getFlowScope().put("reasonList", reasonBean.getReasonList());
			//context.getFlowScope().put("reasonBean", new ReasonBean(reasonBean.getReasonList()));
			setAddPressed(true);
			return success();
		}
		catch (Exception e) {
			e.printStackTrace();;
			return error();
		}
	}
	
	/*public Event onRowEdit(RowEditEvent event){
		RequestContext context = RequestContextHolder.getRequestContext();
		ReasonBean reasonBean = (ReasonBean)context.getFlowScope().get("reasonBean");
		if(isReasonError())
			return "";
		
		return "dfs";
	}*/
	
	public boolean onRowEdit(){
		if(isReasonError())
			return true;
		
		return false;
	}
	
	public void initRow(){
		System.err.println("INIT CALLED FOR ROW");
		//setReasonError(false);
	}
	
	@SuppressWarnings("unchecked")
	public Event cancelAdd(RowEditEvent event)
	{
		RequestContext context = RequestContextHolder.getRequestContext();
		ReasonBean reasonBean = (ReasonBean) context.getFlowScope().get("reasonBean");
		String firstEdit = (String)context.getFlowScope().get("firstEdit");
		System.err.println(firstEdit + "firtsedit cancelled add, was there error" + isReasonError());
		if(firstEdit.equals("true"))
		{
			if(isAddPressed()){
				System.out.println("removing unused records for add");
				reasonBean.getReasonList().remove(0);
				
			}else{
				System.out.println("removing unused records for edit");
				reasonBean.getReasonList().remove(0);
				reasonBean.getReasonList().remove(1);
			
			
			context.getFlowScope().put("reasonBean",reasonBean); 
			System.err.println("reason list size after cancel " + reasonBean.getReasonList().size());
			context.getFlowScope().put("firstEdit", "false");
			search(context);
			}
		}
		setAddPressed(false);
		return success();
	}
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			ReasonBean reasonBean = (ReasonBean)context.getFlowScope().get("reasonBean");
			System.err.println("Reason: "+reasonBean.getSelectedReason().getReasonDescription()+" id:"+reasonBean.getSelectedReason().getReasonId());
			context.getFlowScope().put("reason", reasonBean.getSelectedReason());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public Event updatereason(RequestContext context) {

		System.err.println("Update reason click");
		select(context);
		System.err.println("after select");
		try {
			
		ReasonBO reason = (ReasonBO) context.getFlowScope().get("reason");
		ReasonCriteriaBO criteria = (ReasonCriteriaBO) context
				.getFlowScope().get("criteria");
		ReasonBean reasonBean = (ReasonBean)context.getFlowScope().get("reasonBean");
		String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
		
		if (reason == null)
			throw new Exception("Please enter valid details.");
		
		if(StringUtils.isEmpty(reason.getReasonDescription()))
		{	
			System.out.println(" Update Reason Error " + reasonError);
			errorMsg = "true";
			System.out.println("Error Msg value " + errorMsg);
			context.getFlowScope().put("errorMsg", errorMsg);
			context.getFlowScope().put("reasonBean",reasonBean);
			this.addErrorMessageWithParameter(context, "RequiredFields.Maintenance", "Name");
			return error();
		}
		
		System.err.println("Reason: "+reason.getReasonDescription()+" id:"+reason.getReasonId() + "type: " + reason.getType());
		System.err.println("If exist" +  getMaintenanceService().reasonDescExistForSelectedType(reason));
			//ReasonBO reason = (ReasonBO) context.getFlowScope().get("reason");
			
			

			// get user details
		    reason.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			
			if (validateArgs(reason, context)) {
				System.out.println("failed validation");
				return error();
			} 
			
			System.out.println("Update Was add Pressed " + isAddPressed());
			if(isAddPressed())
			{
				savereason(context);
				if(isSearchPressed() && !isReasonError()){
					
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}/*else{
					ReasonCriteriaBO reasonCrit = new ReasonCriteriaBO();*/
					/*parishCrit.setParishCode(parish.getParishCode());
					parishCrit.setDescription(parish.getDescription());
					parishCrit.setStatusId(parish.getStatusId());*/
					/*context.getFlowScope().put("criteria", reasonCrit);
					search(context);
				}*/
			}
			else
			{
				System.out.println("update before");
				getMaintenanceService().updateReason(reason);
				reason.setAuditEntryBO(new AuditEntryBO());
				reason.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				reason.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				
				if(isSearchPressed()){
					
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}/*else{
					ReasonCriteriaBO reasonCrit = new ReasonCriteriaBO();*/
					/*parishCrit.setParishCode(parish.getParishCode());
					parishCrit.setDescription(parish.getDescription());
					parishCrit.setStatusId(parish.getStatusId());*/
					/*context.getFlowScope().put("criteria", reasonCrit);
					search(context);
				}*/
				
				
				System.out.println("Updated User " + reason.getAuditEntryBO().getUpdateUsername() + " updated time " + reason.getAuditEntryBO().getUpdateDTime());
				
				context.getMessageContext().addMessage(
								new MessageBuilder().info().code("RecordUpdated")
								.arg("Reason ").build());
				System.out.println("update after");
				System.out.println("Updated successfully!");
			}
			setAddPressed(false);
			return success();
		} 
		catch(InvalidFieldException Iex)
		{
			setReasonError(true);
			//context.getMessageContext().addMessage(new MessageBuilder().error().code("RecordExists")
				//	.arg("Reason ").build());
			savereason(context);
				Iex.printStackTrace();
				return success();
			
			
		}
		catch (Exception e) {
			setReasonError(true);
			e.printStackTrace();
			System.out.println("inside catch");
			
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Reason ").build());
			return error();
		}

	}

	public Event savereason(RequestContext context) {
		System.out.println("Save Reason clicked");
		System.out.println("Save Was add Pressed " + isAddPressed());
		try {
			ReasonBO reason = (ReasonBO) context.getFlowScope().get("reason");
			ReasonCriteriaBO criteria = (ReasonCriteriaBO) context
					.getFlowScope().get("criteria");
			ReasonBean reasonBean = (ReasonBean)context.getFlowScope().get("reasonBean");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			
			if (reason == null)
				throw new Exception("Please enter details.");

			// get user details
			reason.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			reason.setTypeDescription(getReasonDescriptions(reason.getType()));
			System.out.println("Reason type & Status " + reason.getType()+" -  "+ reason.getTypeDescription() + reason.getStatusId());
			System.out.println("Reason Error " + reasonError);
			if (validateArgs(reason, context)) {
				return error();
			}else if(getMaintenanceService().reasonDescExistForSelectedType(reason)){
				context.getMessageContext().addMessage(

						new MessageBuilder().error().code("RecordExists")
								.arg("Reason ").build());
				setReasonError(true);
				return error();
			}else{
				getMaintenanceService().saveReason(reason);
				
				
				System.out.println("Type Desc " + reason.getTypeDescription());
				reason.setStatusId(reason.getStatusId());
				reason.setAuditEntryBO(new AuditEntryBO());
				reason.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				reason.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				System.out.println("isSearch" + isSearchPressed());
				if(isSearchPressed()){
					
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					ReasonCriteriaBO reasonCrit = new ReasonCriteriaBO();
					/*parishCrit.setParishCode(parish.getParishCode());
					parishCrit.setDescription(parish.getDescription());
					parishCrit.setStatusId(parish.getStatusId());*/
					context.getFlowScope().put("criteria", reasonCrit);
					//search(context);
				}
				
				
				context.getMessageContext().addMessage(
						new MessageBuilder().info().code("RecordSaved")
								.arg("Reason ").build());
				System.out.println("record saved in save method");
			}

			
			setAddPressed(false);
			return success();
		}catch(InvalidFieldException Iex)
		{
			errorMsg = "true";
			setReasonError(true);
			ReasonBean reasonBean = (ReasonBean) context.getFlowScope().get("reasonBean");
			context.getMessageContext().clearMessages();
			
			//if(isAddPressed())
				//reasonBean.getReasonList().remove(0);
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordExists")
							.arg("Reason ").build());
			Iex.printStackTrace();
			return error();
		}
		
		catch (Exception e) {
			setReasonError(true);
			e.printStackTrace();
			
			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Reason ").build());
			return error();
		}

	}

	//Used to check if required fields are null
	//Returns message to indicate field is required if left null
	private Boolean validateArgs(ReasonBO reason, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();

		if (StringUtils.isBlank(reason.getReasonDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Reason Description").build());
			error = true;
		}
		if (StringUtils.isBlank(reason.getType())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Type ").build());
			error = true;
		} 
		if (StringUtils.isBlank(reason.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status ").build());
			error = true;
		}
		//Check to see if error was thrown
		setReasonError(error);
		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("reasons");
		/*
		 * context.getFlowScope().put("reasons", new
		 * OneSelectionTrackingListDataModel());
		 */
		// reasons= new ArrayList<ReasonBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		setSearchPressed(false);
		context.getViewScope().clear();
		return success();

	}

	public Maintenance getMaintenanceService() {
		return reasonMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance reasonMaintenanceService) {
		this.reasonMaintenanceService = reasonMaintenanceService;
	}

	public boolean isAddPressed() {
		return addPressed;
	}

	public void setAddPressed(boolean addPressed) {
		this.addPressed = addPressed;
	}

	public boolean isReasonError() {
		return reasonError;
	}

	public void setReasonError(boolean reasonError) {
		this.reasonError = reasonError;
	}

	public boolean isSearchPressed() {
		return searchPressed;
	}

	public void setSearchPressed(boolean searchPressed) {
		this.searchPressed = searchPressed;
	}

	
	
	
	
	
}
