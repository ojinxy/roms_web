package org.fsl.roms.service.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.view.OffencesBean;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.Message;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.OffenceBO;
import fsl.ta.toms.roms.bo.OffenceParamBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.OffenceCriteriaBO;
import fsl.ta.toms.roms.webservices.Maintenance;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class OffenceMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance offenceMaintenanceService;
	private DualListModel<OffenceBO> listOfOffences;
	
	
	public OffenceMaintenanceServiceAction() {
		super();
	}
	
	public OffenceMaintenanceServiceAction(
			Maintenance offenceMaintenanceService) {
		this.offenceMaintenanceService = offenceMaintenanceService;
		listOfOffences = null;
	}
	
	/**
	 * This function searches based on Offence criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {
		System.out.println("Search was fired");
		List<OffenceBO> offences = null; 

		try {

			OffenceCriteriaBO criteria = (OffenceCriteriaBO) context
					.getFlowScope().get("criteria");

			// do the search
			
		
			offences = getMaintenanceService().lookupOffence(criteria);

			if (offences == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordfound")
								.build());
				context.getFlowScope().put("offences",
						new OffencesBean());
				return success();

			} /*else if (offences.size() == 1) {

				context.getFlowScope().put("offence", offences.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("offences",
						new OffencesBean(offences));
				return yes();
			}*/

			// set the entire list in datatable
			context.getFlowScope().put("offences",
					new OffencesBean(offences));

		}catch (NoRecordFoundException e) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordfound")
							.build());
			context.getFlowScope().put("offences",
			new OffencesBean());
	return error();
		}catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("offences",
					new OffencesBean());
			return error();
		}
		return success();
	}
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			OffencesBean offenceBean = (OffencesBean)context.getFlowScope().get("offences");
			System.err.println("Artery: "+offenceBean.getSelectedOffence().getShortDescription()+" id:"+offenceBean.getSelectedOffence().getOffenceId() +"Description "+ offenceBean.getSelectedOffence().getOffenceDescription());
			context.getFlowScope().put("offence",offenceBean.getSelectedOffence());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		OffenceCriteriaBO criteria = (OffenceCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		
		OffencesBean offBean = (OffencesBean) context.getFlowScope().get("offences");
		
		if(offBean.getOffenceList().size() > 0)
		{
			search(context);
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			//WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			//System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			OffenceBO offenceBO = new OffenceBO();
			offenceBO.setStatusId("ACT");
			context.getFlowScope().put("offence", offenceBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Event updateoffence(RequestContext context) {

		try {
			OffenceBO offence = (OffenceBO) context.getFlowScope().get("offence");
			OffenceCriteriaBO criteria = (OffenceCriteriaBO) context
					.getFlowScope().get("criteria");
			OffencesBean offenceBean = (OffencesBean)context.getFlowScope().get("offences");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			String str = (String) context.getFlowScope().get("paramsList");
			System.out.println("str "+ str);
			String[] strList;
			strList = str.split(",");
			List<Integer> paramsList = new ArrayList<Integer>();
			List<OffenceParamBO> offenceParamList = new ArrayList<OffenceParamBO>();
			System.err.println("get offence Id " + offence.getOffenceId());
			//offenceParamList = getMaintenancePortProxy().getOffenceParams(offence.getOffenceId());
			
			System.err.println("Offence param on update " + offenceParamList.size());
			
			if(StringUtils.isBlank(str))
			{
				System.err.println("str is null");
				/*context.getMessageContext().addMessage(
						
						new MessageBuilder().error().defaultText("Please add the appropriate parameters.").build());
				return error();*/
				//offenceParamList = null;
			}
			else{
			for(String s: strList)
			{
				paramsList.add(Integer.parseInt(s));
			}
			
			System.err.println("param List size" + paramsList.size());
			for(Integer param: paramsList)
			{
				OffenceParamBO paramBO = new OffenceParamBO();
				paramBO.setOffenceId(offence.getOffenceId());
				System.out.println("parambo id" + paramBO.getOffenceId());
				paramBO.setParamMapId(param);
				System.out.println("parambo paramid" + paramBO.getParamMapId());
				offenceParamList.add(paramBO);
				
			}
			System.err.println("Offence param list size after loop " + offenceParamList.size());
			}
			// get user details
			offence.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

			if (validateArgs(str,offence, context)) {
				return error();
			} else {

				getMaintenanceService().updateOffence(offence, offenceParamList);
				offence.setAuditEntryBO(new AuditEntryBO());
				offence.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				if(offenceBean.getOffenceList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
					System.err.println("Remove No Record Found");
					Message[] msgs = context.getMessageContext().getAllMessages();
					for(Message m : msgs){
						if(m.getText().equals("No record found.")){
							context.getMessageContext().clearMessages();
						}
					}
				}
			}
			context.getFlowScope().put("offence", offence);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordUpdated")
							.arg("Offence ").build());

			// retrieve results from database again
			// search(context);
			return success();
		}catch (InvalidFieldException e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(

					new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}catch (Exception e) {

			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Offence ").build());
			return error();
		}
		

	}

	@SuppressWarnings("unchecked")
	public Event saveoffence(RequestContext context) {
		System.out.println("Save Triggered");
		try {
			OffenceBO offence = (OffenceBO) context.getFlowScope().get("offence");
			OffenceCriteriaBO criteria = (OffenceCriteriaBO) context
					.getFlowScope().get("criteria");
			OffencesBean offenceBean = (OffencesBean)context.getFlowScope().get("offences");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			System.err.println("Offence " + offence.getShortDescription() + "id " + offence.getOffenceId() + "type " + offence.getRoadCheckTypeId() + "excerpt " + offence.getExcerpt() );
			String str = (String) context.getFlowScope().get("paramsList");
			String[] strList;
			strList = str.split(",");
			List<Integer> paramsList = new ArrayList<Integer>();
			System.err.println("str List" + strList);
			
			
			List<OffenceParamBO> offenceParamList = new ArrayList<OffenceParamBO>();
			
			if (offence == null)
				throw new Exception("Please enter details.");
			
			if(StringUtils.isBlank(str))
			{
				System.err.println("str is null");
				
			}
			else{
			for(String s: strList)
			{
				paramsList.add(Integer.parseInt(s));
			}
				
			System.err.println("param List size" + paramsList.size());
			for(Integer param: paramsList)
			{
				OffenceParamBO paramBO = new OffenceParamBO();
				paramBO.setOffenceId(offence.getOffenceId());
				System.out.println("parambo id" + paramBO.getOffenceId());
				paramBO.setParamMapId(param);
				System.out.println("parambo paramid" + paramBO.getParamMapId());
				offenceParamList.add(paramBO);
				
			}
			}
			// get user details
			offence.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			offence.setStatusId(Constants.Status.ACTIVE);

			if (validateArgs(str,offence, context)) {
				return error();
			} 

				getMaintenanceService().saveOffence(offence, offenceParamList);
				offence.setAuditEntryBO(new AuditEntryBO());
				offence.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				if(offenceBean.getOffenceList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
					
					Message[] msgs = context.getMessageContext().getAllMessages();
					for(Message m : msgs){
						if(m.getText().equals("No record found.")){
							context.getMessageContext().clearMessages();
						}
					}
				}
			
			context.getFlowScope().put("offence", offence);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Offence ").build());

			return success();
		}catch (InvalidFieldException e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(

					new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}
		catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Offence ").build());
			return error();
		}
 
	}

	private Boolean validateArgs(String str,OffenceBO offence, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();
		
		if (offence.getOffenceId() == null || offence.getOffenceId() < 1) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Offence Number").build());
			error = true;

		}
		
		if (StringUtils.isBlank(offence.getShortDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Offence Name").build());
			error = true;

		}
		
		if (StringUtils.isBlank(offence.getOffenceDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Offence Description").build());
			error = true;

		} 
	
		
		if (StringUtils.isBlank(offence.getRoadCheckTypeId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Road Check Type").build());
			error = true;

		}
		
		if (StringUtils.isBlank(offence.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status").build());
			error = true;
		}
		if (StringUtils.isBlank(offence.getExcerpt())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Excerpt").build());
			error = true;

		} 
		if (StringUtils.isBlank(offence.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		}
		
		if (StringUtils.isBlank(str)) {
			messages.addMessage(new MessageBuilder()
					.error().defaultText("Please add the appropriate parameters.").build());
			error = true;
		}
		
		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("offences");
		/*
		 * context.getFlowScope().put("offences", new
		 * OffencesBean());
		 */
		// offences= new ArrayList<OffenceBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}

	public Maintenance getMaintenanceService() {
		return offenceMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance offenceMaintenanceService) {
		this.offenceMaintenanceService = offenceMaintenanceService;
	}
	
}
