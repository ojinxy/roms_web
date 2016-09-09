package org.fsl.roms.service.action;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.view.AddressView;
import org.fsl.roms.view.PoundBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.PoundBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.PoundCriteriaBO;
import fsl.ta.toms.roms.webservices.Maintenance;
/**
 * 
 * @author bssintern
 *
 */

@Service
public class PoundMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance poundMaintenanceService;

	public PoundMaintenanceServiceAction() {
		super();
	}
	
	public PoundMaintenanceServiceAction(
			Maintenance poundMaintenanceService) {
		this.poundMaintenanceService = poundMaintenanceService;
	}
	
	
	/**
	 * This function searches based on Pound criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {

		List<PoundBO> pounds = null;

		try {

			PoundCriteriaBO criteria = (PoundCriteriaBO) context
					.getFlowScope().get("criteria");

			// do the search 
			
			pounds = getMaintenanceService().lookupPound(criteria);


			if (pounds == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordfound")
								.build());
				context.getFlowScope().put("pounds",
						new PoundBean());
				return success();

			} else if (pounds.size() == 1) {

				context.getFlowScope().put("pound", pounds.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("pounds",
						new PoundBean(pounds));
				return yes();
			}

			// set the entire list in datatable
			context.getFlowScope().put("pounds",
					new PoundBean(pounds));

		}catch(NoRecordFoundException nRFex)
		{
			context.getMessageContext()
			.addMessage(
					new MessageBuilder().error().code("Norecordfound")
							.build());
			context.getFlowScope().put("pounds",
					new PoundBean());
			return error();
		}
		catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("pounds",
					new PoundBean());
			return error();
		}
		return success();
	}
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			PoundBean poundBean = (PoundBean)context.getFlowScope().get("pounds");
			System.err.println("Reason: "+poundBean.getSelectedPound().getPoundName()+" id:"+poundBean.getSelectedPound().getPoundId());
			context.getFlowScope().put("pound", poundBean.getSelectedPound());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		PoundCriteriaBO criteria = (PoundCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		List<SelectItem> poundList = new ArrayList<SelectItem>();
		poundList = (List<SelectItem>) getPoundsShadow();
		context.getFlowScope().put("poundList", poundList);
		
		PoundBean pBean = (PoundBean) context.getFlowScope().get("pounds");
		
		if(pBean.getPoundList().size() > 0)
		{
			search(context);
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			//WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			//System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			PoundBO poundBO = new PoundBO();
			poundBO.setStatusId("ACT");
			context.getFlowScope().put("pound", poundBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Event updatepound(RequestContext context) {

		try {
			PoundBO pound = (PoundBO) context.getFlowScope().get("pound");
			PoundCriteriaBO criteria = (PoundCriteriaBO) context
					.getFlowScope().get("criteria");
			PoundBean poundBean = (PoundBean)context.getFlowScope().get("pounds");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (pound == null)
				throw new Exception("Please enter valid details.");

			// get user details
			pound.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

			if (validateArgs(pound, context)) {
				return error();
			} else {
				
				getMaintenanceService().updatePound(pound);
				pound.setAuditEntryBO(new AuditEntryBO());
				pound.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				if(poundBean.getPoundList().size() > 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					/*PoundCriteriaBO poundCrit = new PoundCriteriaBO();
					poundCrit.setPoundId(pound.getPoundId());
					context.getFlowScope().put("criteria", poundCrit);
					search(context);*/
				}
			}
			context.getFlowScope().put("pound", pound);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordUpdated")
							.arg("Pound ").build());

			// retrieve results from database again
			// search(context);
			return success();
		}catch(InvalidFieldException e){ 
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
							.arg("Pound ").build());
			return error();
		}

	}

	public Event savepound(RequestContext context) {

		try {
			PoundBO pound = (PoundBO) context.getFlowScope().get("pound");
			PoundCriteriaBO criteria = (PoundCriteriaBO) context
					.getFlowScope().get("criteria");
			PoundBean poundBean = (PoundBean)context.getFlowScope().get("pounds");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (pound == null)
				throw new Exception("Please enter details.");

			// get user details
			pound.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			pound.setStatusId(Constants.Status.ACTIVE);
			if (validateArgs(pound, context)) {
				return error();
			} else {

				getMaintenanceService().savePound(pound);
				pound.setAuditEntryBO(new AuditEntryBO());
				pound.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				if(poundBean.getPoundList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					PoundCriteriaBO poundCrit = new PoundCriteriaBO();
					poundCrit.setPoundId(pound.getPoundId());
					context.getFlowScope().put("criteria", poundCrit);
					search(context);
				}
			}
			context.getFlowScope().put("pound", pound);
			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Pound ").build());

			return success();
		}catch (RequiredFieldMissingException e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();			
		}catch (InvalidFieldException e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext().addMessage(
							new MessageBuilder().error().code("RecordSavedErr")
							.arg("Pound ").build());
			return error();
		}

	}

	//Used to check if required fields are null
	//Returns message to indicate field is required if left null
	private Boolean validateArgs(PoundBO pound, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();

		if (StringUtils.isBlank(pound.getPoundName())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Pound Name").build());
			error = true;
		} 
		if (StringUtils.isBlank(pound.getShortDesc())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Description").build());
			error = true;
 
		}
		if (pound.getNumberOfLots() == null) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Number of Lots").build());
			error = true;

		}
		if (pound.getNumberOfParkingSpaces() == null) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Number of Parking Spaces").build());
			error = true;

		}
		
		if ((!StringUtils.isBlank(pound.getTelephoneCell())) && pound.getTelephoneCell().length() < 13) {
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Cell").build());
			error = true;

		}
		if (StringUtils.isBlank(pound.getTelephoneWork())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Work Number").build());
			error = true;
		}else if(pound.getTelephoneWork().length() < 13){
			messages.addMessage(new MessageBuilder().error()
					.code("Telephone").arg("Work").build());
			error = true;
		}
		
			
		if (StringUtils.isBlank(pound.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status ").build());
			error = true;
		} 
		if (StringUtils.isBlank(pound.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		}
		
		//populate addressview to be used by global validation method - 20150616 @kpowell
		AddressView poundAddress = new AddressView(pound.getMarkText(), pound.getParishCode(), pound.getPoBoxNo(),
				pound.getPoLocationName(), pound.getStreetName(), pound.getStreetNo(),
				null, null, null,pound.getParishName());
		
		//global address validation - kpowell
		boolean errorFoundInAddress  = validateAddress(context, poundAddress);
        
        if(errorFoundInAddress){
              error = true;
        }

		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("pounds");
		/*
		 * context.getFlowScope().put("pounds", new
		 * PoundBean());
		 */
		// pounds= new ArrayList<PoundBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}

	public Maintenance getMaintenanceService() {
		return poundMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance poundMaintenanceService) {
		this.poundMaintenanceService = poundMaintenanceService;
	}
	
}
