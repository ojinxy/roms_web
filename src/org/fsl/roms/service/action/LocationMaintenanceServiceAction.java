package org.fsl.roms.service.action;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.LocationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.LocationBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.search.criteria.impl.LocationCriteriaBO;
import fsl.ta.toms.roms.webservices.Maintenance;
/**
 * 
 * @author bssintern
 *
 */

@Service
public class LocationMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance locationMaintenanceService;

	public LocationMaintenanceServiceAction() {
		super();
	}
	
	public LocationMaintenanceServiceAction(
			Maintenance locationMaintenanceService) {
		this.locationMaintenanceService = locationMaintenanceService;
	}
	
	
	/**
	 * This function searches based on Location criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {
			
		List<LocationBO> locations = null;
			
		try {
			LocationCriteriaBO criteria = (LocationCriteriaBO) context
					.getFlowScope().get("criteria");
			System.out.println("criteria " + criteria.getLocationId() +" status "+ criteria.getStatusId() +" " + criteria.getParishCode());
			// do the search			
			/*MaintenancePortProxy proxy = new MaintenancePortProxy();
			
			proxy._getDescriptor().setEndpoint("http://devwas85:9080/ROMSWS/MaintenanceService");*/
			
			locations = getMaintenanceService().lookupLocation(criteria);

			if (locations == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordsfound")
								.build());
				context.getFlowScope().put("locations",
						new LocationBean());
				return success();

			} /*else if (locations.size() == 1) {

				context.getFlowScope().put("location", locations.get(0));
	
				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("locations",
						new LocationBean(locations));
				return yes();
			}*/
			System.out.println("size" + locations.size());
			// set the entire list in datatable
			context.getFlowScope().put("locations",
					new LocationBean(locations));

		} catch (NoRecordFoundException e) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordfound")
							.build());
			context.getFlowScope().put("locations",
					new LocationBean());
			return success();
		}
		catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("locations",
					new LocationBean());
			return error();
		}
		return success();
	}
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			LocationBean locationBean = (LocationBean)context.getFlowScope().get("locations");
			System.err.println("Location: "+locationBean.getSelectedLocation().getShortDesc()+" id:"+locationBean.getSelectedLocation().getLocationId());
			context.getFlowScope().put("location", locationBean.getSelectedLocation());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		LocationCriteriaBO criteria = (LocationCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		
		LocationBean locBean = (LocationBean) context.getFlowScope().get("locations");
		
		if(locBean.getLocationList().size() > 0)
		{
			search(context);
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			//WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			//System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			LocationBO locationBO = new LocationBO();
			locationBO.setStatusId("ACT");
			context.getFlowScope().put("location", locationBO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Event updatelocation(RequestContext context) {

		try {
			LocationBO location = (LocationBO) context.getFlowScope().get("location");
			LocationBean locationBean = (LocationBean)context.getFlowScope().get("locations");
			LocationCriteriaBO criteria = (LocationCriteriaBO) context
					.getFlowScope().get("criteria");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (location == null)
				throw new Exception("Please enter valid details.");
			
			// get user details
			location.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

			if (validateArgs(location, context)) {
				return error();
			} else {				
				getMaintenanceService().updateLocation(location);
				getLocations(context);
				location.setLocationDescription(location.getLocationDescription());
				location.setParishDescription(getParishDesc(location.getParishCode()));
				location.setStatusDescription(location.getStatusId()==Constants.Status.ACTIVE?"Active":"Inactive");
				System.out.println("Parish Name " + location.getParishDescription() + " location " + location.getLocationDescription());
				location.setAuditEntryBO(new AuditEntryBO());
				location.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				location.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				
				if(locationBean.getLocationList().size() > 1){
					System.err.println("If...");
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					System.err.println("Else...");
					LocationCriteriaBO locationCrit = new LocationCriteriaBO();
					locationCrit.setLocationId(location.getLocationId());
					locationCrit.setParishCode(location.getParishCode());
					locationCrit.setStatusId(location.getStatusId());
					context.getFlowScope().put("criteria", locationCrit);
					search(context);
				}
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordUpdated")
							.arg("Location").build());

			// retrieve results from database again
			// search(context);
			return success();
		}catch(InvalidFieldException e)
		{
			e.printStackTrace();
			context.getMessageContext().addMessage( new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}catch (Exception e) {

			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Location ").build());
			return error();
		}

	}

	public Event savelocation(RequestContext context) {
			System.out.println("Save fired");
		try {
			LocationBO location = (LocationBO) context.getFlowScope().get("location");
			LocationBean locationBean = (LocationBean)context.getFlowScope().get("locations");
			LocationCriteriaBO criteria = (LocationCriteriaBO) context
					.getFlowScope().get("criteria");
			
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			if (location == null)
				throw new Exception("Please enter details.");

			System.out.println("Criteria "+location.getParishCode()+" "+location.getStatusId() + " Location Desc"+ location.getLocationDescription());
			// get user details
			
			location.setParishDescription(getParishDesc(location.getParishCode()));
			location.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			location.setStatusId(Constants.Status.ACTIVE);
			if (validateArgs(location, context)) {
				return error();
			} else {
				
				getMaintenanceService().saveLocation(location);
				getLocations(context);
				location.setLocationDescription(location.getLocationDescription());
				location.setParishDescription(getParishDesc(location.getParishCode()));
				location.setStatusDescription(location.getStatusId()==Constants.Status.ACTIVE?"Active":"Inactive");
				System.out.println("Parish Name " + location.getParishDescription() + " location " + location.getLocationDescription());
				location.setAuditEntryBO(new AuditEntryBO());
				location.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				location.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				
				if(locationBean.getLocationList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					LocationCriteriaBO locationCrit = new LocationCriteriaBO();
					locationCrit.setLocationId(location.getLocationId());
					context.getFlowScope().put("criteria", locationCrit);
					search(context);
				}
			}

			context.getMessageContext().addMessage(

					new MessageBuilder().info().code("RecordSaved")
							.arg("Location ").build());

			return success();
		}catch(InvalidFieldException e)
		{
			e.printStackTrace();
			context.getMessageContext().addMessage( new MessageBuilder().error().defaultText(e.getMessage()).build());
			return error();
		}catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("Location ").build());
			return error();
		}

	}

	private Boolean validateArgs(LocationBO location, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();

		
		if (StringUtils.isBlank(location.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status ").build());
			error = true;
		} 
		if (StringUtils.isBlank(location.getShortDesc())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Name").build());
			error = true;

		}
		if (StringUtils.isBlank(location.getLocationDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Description").build());
			error = true;
		}
		if (StringUtils.isBlank(location.getParishCode())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Parish").build());
			error = true;

		}
		if (StringUtils.isBlank(location.getCurrentUsername())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("User Name ").build());
			error = true;
		} 
		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("locations");
		/*
		 * context.getFlowScope().put("locations", new
		 * LocationBean());
		 */
		// locations= new ArrayList<LocationBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}

	public Maintenance getMaintenanceService() {
		return locationMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance locationMaintenanceService) {
		this.locationMaintenanceService = locationMaintenanceService;
	}
	
}
