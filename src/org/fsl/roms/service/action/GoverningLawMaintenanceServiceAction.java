package org.fsl.roms.service.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.view.GoverningLawBean;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.GoverningLawBO;
import fsl.ta.toms.roms.search.criteria.impl.GoverningLawCriteriaBO;
import fsl.ta.toms.roms.webservices.Maintenance;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class GoverningLawMaintenanceServiceAction extends BaseServiceAction {

	private Maintenance governinglawMaintenanceService;

	public GoverningLawMaintenanceServiceAction() {
		super();
	}
	
	public GoverningLawMaintenanceServiceAction(
			Maintenance governinglawMaintenanceService) {
		this.governinglawMaintenanceService = governinglawMaintenanceService;
	}
	
	/**
	 * This function searches based on Governing Law criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {
		System.out.println("Search fired");
		List<GoverningLawBO> governingLaws = null;

		try {

			GoverningLawCriteriaBO criteria = (GoverningLawCriteriaBO) context
					.getFlowScope().get("criteria");

			// do the search
			System.out.println("Criteria "+criteria.getShortDesc()+" "+criteria.getStatusId());
			//criteria.setLawId(0);
			governingLaws = getMaintenanceService().lookupGoverningLaw(criteria);


			if (governingLaws == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().info().code("Norecordsfound")
								.build());
				context.getFlowScope().put("governingLaws",
						new GoverningLawBean());
				return success();

			} else if (governingLaws.size() == 1) {

				context.getFlowScope().put("governingLaw", governingLaws.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("governingLaws",
						new GoverningLawBean(governingLaws));
				return yes();
			}

			// set the entire list in datatable
			context.getFlowScope().put("governingLaws",
					new GoverningLawBean(governingLaws));

		} catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("governingLaws",
					new GoverningLawBean());
			return error();
		}
		return success();
	}
	
	public Event updategoverninglaw(RequestContext context) {

		try {
			GoverningLawBO governinglaw = (GoverningLawBO) context.getFlowScope().get("governingLaw");

			
			if (governinglaw == null)
				throw new Exception("Please enter valid details.");

			// get user details
			governinglaw.setCurrentUsername(getRomsLoggedInUser().getUsername());

			if (validateArgs(governinglaw, context)) {
				return error();
			} else {
				
				getMaintenanceService().updateGoverningLaw(governinglaw);
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("GoverningLaw").build());

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
							.arg("GoverningLaw").build());
			return error();
		}

	}

	public Event savegoverninglaw(RequestContext context) {

		try {
			GoverningLawBO governinglaw = (GoverningLawBO) context.getFlowScope().get("governingLaw");

			
			if (governinglaw == null)
				throw new Exception("Please enter details.");

			// get user details
			governinglaw.setCurrentUsername(getRomsLoggedInUser().getUsername());

			if (validateArgs(governinglaw, context)) {
				return error();
			} else {

				getMaintenanceService().saveGoverningLaw(governinglaw);
				/*governinglawMaintenanceDelegate.saveGoverningLaw(governinglaw);*/
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("GoverningLaw").build());

			return success();
		} catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("GoverningLaw").build());
			return error();
		}

	}

	//Used to check if required fields are null
	//Returns message to indicate field is required if left null	
	private Boolean validateArgs(GoverningLawBO governinglaw, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();

		if (StringUtils.isBlank(governinglaw.getDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Description").build());
			error = true;

		} 
		if (StringUtils.isBlank(governinglaw.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Status").build());
			error = true;
		}
		if (StringUtils.isBlank(governinglaw.getShortDesc())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Short Description").build());
			error = true;

		}
		if (StringUtils.isBlank(governinglaw.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("User Name ").build());
			error = true;
		}
		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("governingLaws");
		/*
		 * context.getFlowScope().put("governingLaws", new
		 * OneSelectionTrackingListDataModel());
		 */
		// governingLaws= new ArrayList<GoverningLawBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}

	public Maintenance getMaintenanceService() {
		return governinglawMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance governinglawMaintenanceService) {
		this.governinglawMaintenanceService = governinglawMaintenanceService;
	}
	
}
