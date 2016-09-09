package org.fsl.roms.service.action;

import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.faces.model.OneSelectionTrackingListDataModel;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.bo.ConfigurationBO;
import fsl.ta.toms.roms.search.criteria.impl.ConfigurationCriteriaBO;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class ConfigurationMaintenanceServiceAction extends BaseServiceAction {

	private Maintenance configurationMaintenanceService;

	public ConfigurationMaintenanceServiceAction() {
		super();
	}
	
	public ConfigurationMaintenanceServiceAction(
			Maintenance courtMaintenanceService) {
		this.configurationMaintenanceService = configurationMaintenanceService;
	}
	
	/**
	 * This function searches based on Configuration criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {

		List<ConfigurationBO> configurations = null;

		try {

			ConfigurationCriteriaBO criteria = (ConfigurationCriteriaBO) context
					.getFlowScope().get("criteria");

			// do the search
			
			/*MaintenancePortProxy proxy = new MaintenancePortProxy();
			
			proxy._getDescriptor().setEndpoint("http://devwas85:9080/ROMSWS/MaintenanceService");*/
			
			configurations = getMaintenanceService().lookupConfiguration(criteria);


			if (configurations == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().info().code("Norecordsfound")
								.build());
				context.getFlowScope().put("configurations",
						new OneSelectionTrackingListDataModel());
				return success();

			} else if (configurations.size() == 1) {

				context.getFlowScope().put("configuration", configurations.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("configurations",
						new OneSelectionTrackingListDataModel(configurations));
				return yes();
			}

			// set the entire list in datatable
			context.getFlowScope().put("configurations",
					new OneSelectionTrackingListDataModel(configurations));

		} catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("configurations",
					new OneSelectionTrackingListDataModel());
			return error();
		}
		return success();
	}
	
	public Event updateconfiguration(RequestContext context) {

		try {
			ConfigurationBO configuration = (ConfigurationBO) context.getFlowScope().get("configuration");

			/*MaintenancePortProxy proxy = new MaintenancePortProxy();
			
			proxy._getDescriptor().setEndpoint("http://devwas85:9080/ROMSWS/MaintenanceService");*/
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			if (configuration == null)
				throw new Exception("Please enter valid details.");

			// get user details
			configuration.setCurrentUsername(usernameToFullName);

			if (validateArgs(configuration, context)) {
				return error();
			} else {
				
				getMaintenanceService().updateConfiguration(configuration);
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Configuration ").build());

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
							.arg("Configuration ").build());
			return error();
		}

	}

	public Event saveconfiguration(RequestContext context) {

		try {
			ConfigurationBO configuration = (ConfigurationBO) context.getFlowScope().get("configuration");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			/*MaintenancePortProxy proxy = new MaintenancePortProxy();
			
			proxy._getDescriptor().setEndpoint("http://devwas85:9080/ROMSWS/MaintenanceService");*/
			
			if (configuration == null)
				throw new Exception("Please enter details.");

			// get user details
			configuration.setCurrentUsername(usernameToFullName);

			if (validateArgs(configuration, context)) {
				return error();
			} else {

				getMaintenanceService().saveConfiguration(configuration);
				
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Configuration ").build());

			return success();
		} catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Configuration ").build());
			return error();
		}

	}

	//Used to check if required fields are null
	//Returns message to indicate field is required if left null	
	private Boolean validateArgs(ConfigurationBO configuration, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();

		if (StringUtils.isBlank(configuration.getCfgCode())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Configuration Code").build());
			error = true;
		}
		if (StringUtils.isBlank(configuration.getDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Configuration Description ").build());
			error = true;
		}
		if (StringUtils.isBlank(configuration.getValue())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Value").build());
			error = true;

		} 
		if (StringUtils.isBlank(configuration.getDataType())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Data Type").build());
			error = true;
		} 

		if (configuration.getStartDate()== null) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Start Date").build());
			error = true;
		} 

		if (configuration.getEndDate() == null) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("End Date").build());
			error = true;
		} 
		if (StringUtils.isBlank(configuration.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("Status").build());
			error = true;
		}
		if (StringUtils.isBlank(configuration.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields").arg("User Name").build());
			error = true;
		}

		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("configurations");
		/*
		 * context.getFlowScope().put("configurations", new
		 * OneSelectionTrackingListDataModel());
		 */
		// configurations= new ArrayList<ConfigurationBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}
	
}
