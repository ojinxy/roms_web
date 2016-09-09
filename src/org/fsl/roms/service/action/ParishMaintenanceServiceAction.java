package org.fsl.roms.service.action;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.view.ParishView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.ParishBO;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.ParishCriteriaBO;
import fsl.ta.toms.roms.webservices.Maintenance;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class ParishMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance parishMaintenanceService;

	public ParishMaintenanceServiceAction() {
		super();
	}
	
	public ParishMaintenanceServiceAction(
			Maintenance parishMaintenanceService) {
		this.parishMaintenanceService = parishMaintenanceService;
	}
	

	/**
	 * This function searches based on Parish criteria 
	 * @param context
	 * @return
	 */  
	public Event search(RequestContext context)
	{
		List<ParishBO> parishes = null;
			System.out.println("Parish search fired");	
		try
		{
			ParishCriteriaBO criteria = (ParishCriteriaBO) context
					.getFlowScope().get("criteria");

			// do the search
			
			System.out.println("Criteria" + criteria.getParishCode() + " " + criteria.getStatusId());
			parishes = getMaintenanceService().lookupParish(criteria);

			if (parishes == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordfound")
								.build());
				context.getFlowScope().put("parishView",
						new ParishView());
				return success();

			} /*else if (parishes.size() == 1) {

				context.getFlowScope().put("parish", parishes.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("parishes",
						new ParishView(parishes));
				return yes();
			}*/

			System.out.println("Size " + parishes.size());
			// set the entire list in datatable
			context.getFlowScope().put("parishView",
					new ParishView(parishes));

		}catch (NoRecordFoundException e) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordfound")
							.build());	
			context.getFlowScope().put("parishView",new ParishView());
			return error();
		}catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("parishView",
					new ParishView());
			return error();
		}
		return success();
	}
	
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			ParishView parishView = (ParishView)context.getFlowScope().get("parishView");
			System.err.println("Parish: OFC"+parishView.getSelectedParish().getOfficeLocationCode()+" id:"+parishView.getSelectedParish().getParishCode());
			context.getFlowScope().put("parish", parishView.getSelectedParish());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * This function used to update parish criteria  
	 * @param context
	 * @return
	 */
	public Event updateparish(RequestContext context) {
		System.out.println("update parish called");
		try {
			select(context);
			ParishBO parish = (ParishBO) context.getFlowScope().get("parish");
			ParishView parishView = (ParishView)context.getFlowScope().get("parishView");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			System.out.println("Selected Parish OFC" + parish.getOfficeLocationCode() + " Parish " + parish.getParishCode());
			if (parish == null)
				throw new Exception("Please enter valid details.");

			// get user details
			parish.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

			if (validateArgs(parish, context)) {
				return error();
			} else {

				getMaintenanceService().updateParish(parish);
				/*parish.setOfficeLocationDescription(getOfficeLocationDesc(parish.getOfficeLocationCode()));
				parish.getAuditEntryBO().setUpdateUsername(parish.getAuditEntryBO().getCreateUsername());
				parish.getAuditEntryBO().setUpdateDTime(DateUtils.getCurrentXMLGregCalendarDate());*/
				parish.setAuditEntryBO(new AuditEntryBO());
				parish.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				ParishCriteriaBO criteriaX = (ParishCriteriaBO) context
						.getFlowScope().get("criteria");
				if(parishView.getParishList().size() >= 1){
					
					System.out.println("Criteria from update " + criteriaX.getParishCode() + " name "+ criteriaX.getDescription() + " parish "+ criteriaX.getParishCode());
					context.getFlowScope().put("criteria", criteriaX);
					search(context);
				}else{
					ParishCriteriaBO parishCrit = new ParishCriteriaBO();
					/*parishCrit.setParishCode(parish.getParishCode());
					parishCrit.setDescription(parish.getDescription());
					parishCrit.setStatusId(parish.getStatusId());*/
					context.getFlowScope().put("criteria", parishCrit);
					search(context);
				}
				
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordUpdated")
							.arg("Parish ").build());

			// retrieve results from database again
			// search(context);
			return success();
		} catch (Exception e) {

			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Parish ").build());
			return error();
		}

	}

	/**
	 * This function used to save a new parish
	 * @param context
	 * @return
	 */
	public Event saveparish(RequestContext context) {

		try {
			ParishBO parish = (ParishBO) context.getFlowScope().get("parish");
			ParishView parishView = (ParishView)context.getFlowScope().get("parishView");
			ParishCriteriaBO criteria = (ParishCriteriaBO) context
					.getFlowScope().get("criteria");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (parish == null)
				throw new Exception("Please enter details.");

			// get user details
			parish.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

			if (validateArgs(parish, context)) {
				return error();
			} else {
				//proxy.saveParish(parish);
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Parish ").build());

			return success();
		} catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Parish ").build());
			return error();
		}

	}

	/**
	 * This function checks if required fields are null
	 * Returns message to indicate field is required if left null
	 * @param parish
	 * @param context
	 * @return
	 */
	private Boolean validateArgs(ParishBO parish, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();

		if (StringUtils.isBlank(parish.getParishCode())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Parish Code").build());
			error = true;
		} 
		if (StringUtils.isBlank(parish.getDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Description").build());
			error = true;
		}
		if (StringUtils.isBlank(parish.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status ").build());
			error = true;
		} 
		if (StringUtils.isBlank(parish.getOfficeLocationCode())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Office Location Code").build());
			error = true;

		}
		if (StringUtils.isBlank(parish.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		} 
		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("parishes");
		/*
		 * context.getFlowScope().put("parishes", new
		 * ParishView());
		 */
		// parishes= new ArrayList<ParishBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}

	public Maintenance getMaintenanceService() {
		return parishMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance parishMaintenanceService) {
		this.parishMaintenanceService = parishMaintenanceService;
	}
	
}
