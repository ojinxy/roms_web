package org.fsl.roms.service.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.util.NameUtil;
import org.fsl.roms.view.TAVehicleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.amvswebservice.FslWebServiceException_Exception;
import fsl.ta.toms.roms.amvswebservice.Vehicle;
import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.TAVehicleBO;
import fsl.ta.toms.roms.bo.VehicleBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.TAVehicleCriteriaBO;
import fsl.ta.toms.roms.webservices.Maintenance;
//import com.ibm.ws.security.admintask.audit.notification.GetEmailList;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class TaVehicleMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance taVehicleMaintenanceService;
	private String plateSearchVal;
	
	
	public TaVehicleMaintenanceServiceAction() {
		super();
	}
	
	public TaVehicleMaintenanceServiceAction(
			Maintenance taVehicleMaintenanceService) {
		this.taVehicleMaintenanceService = taVehicleMaintenanceService;
	}
	
	/**
	 * This function searches based on TA Vehicle criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {
		System.err.println("Search has been fired");
		List<TAVehicleBO> taVehicles = null;

		try {

			TAVehicleCriteriaBO criteria = (TAVehicleCriteriaBO) context
					.getFlowScope().get("criteria");
			//Strips and sets plateno
			criteria.getVehicle().setPlateRegNo(stripPlate(criteria.getVehicle().getPlateRegNo()));
			
			System.err.println("criteria" + criteria.getStatusId() + " location Code " + criteria.getOfficeLocationCode());
			// do the search
			
		/*	MaintenancePortProxy proxy = new MaintenancePortProxy();
			
			proxy._getDescriptor().setEndpoint("http://devwas85:9080/ROMSWS/MaintenanceService");*/
			
			taVehicles = getMaintenanceService().lookupTAVehicle(criteria);
			System.err.println("taVehicles after");

			if (taVehicles == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordfound")
								.build());
				context.getFlowScope().put("taVehicles",
						new TAVehicleBean());
				return success();

			} else if (taVehicles.size() == 1) {

				context.getFlowScope().put("taVehicle", taVehicles.get(0));

				context.getFlowScope().put("mode", "update");

				context.getFlowScope().put("taVehicles",
						new TAVehicleBean(taVehicles));
				return yes();
			}

			// set the entire list in datatable
			context.getFlowScope().put("taVehicles",
					new TAVehicleBean(taVehicles));

		}catch (NoRecordFoundException NRex) {
			context.getMessageContext().addMessage(
					new MessageBuilder().error().code("Norecordfound")
							.build());
			context.getFlowScope().put("taVehicles",
					new TAVehicleBean());
			return error();
		}
		catch (Exception e) {
			System.err.println("inside catch");
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("taVehicles",
					new TAVehicleBean());
			return error();
		}
		return success();
	}
	
	public String stripPlate(String data){
		//String plate = "";
		String nPlate = "";
		if(!StringUtils.isBlank(data)){
			String[] plate = data.split("-");
			nPlate = plate[0].toString();
		}
		
		return nPlate;
	}
	
	public void test(RequestContext context)
	{
		System.out.println("Ttest called");
		addErrorMessageText(context, "Test caled");
	}
	public Event searchPlate(RequestContext context){
		System.out.println("Search Plate called");
		try{
			TAVehicleBO tavehicle = (TAVehicleBO)context.getFlowScope().get("taVehicle");
			String onUpdateAction = (String)context.getFlowScope().get("updatehideBtn");
			NameUtil nameUtil = new NameUtil();
			if(StringUtils.isBlank(tavehicle.getVehicle().getPlateRegNo())){
				addErrorMessageWithParameter(context,  "RequiredFields.Maintenance", "Plate Registration #");
				return error();
			}
			
			if(taVehiclePlateNoExistsInROMS(tavehicle.getVehicle().getPlateRegNo()) && !StringUtils.isEmpty(onUpdateAction)){
				addErrorMessageText(context,"Another TA Vehicle exists with selected Plate No.");
				add(context);
				return error();
			}
			
			Vehicle vehicleBO = getAMVSWebService().getVehicleByPlateNumber(tavehicle.getVehicle().getPlateRegNo());
			
			if(vehicleBO.getVehInfo() == null){
				addErrorMessageText(context, "Norecordfound");
				add(context);
				return error();
			}
			
			if(vehicleBO.getVehInfo().getEngineNo() != null){
				tavehicle.getVehicle().setEngineNo(vehicleBO.getVehInfo().getEngineNo());
				setPlateSearchVal(tavehicle.getVehicle().getPlateRegNo());
			}
			
			if(vehicleBO.getVehInfo().getVehicleMakeDesc() != null){
				tavehicle.getVehicle().setMakeDescription(vehicleBO.getVehInfo().getVehicleMakeDesc());
			}
			
			if(vehicleBO.getVehInfo().getVehicleModel() != null){
				tavehicle.getVehicle().setModel(vehicleBO.getVehInfo().getVehicleModel());
			}
			
			if(vehicleBO.getVehInfo().getChassisNo() != null){
				tavehicle.getVehicle().setChassisNo(vehicleBO.getVehInfo().getChassisNo());
			}
			
			if(vehicleBO.getVehInfo().getVehicleTypeDesc() != null){
				tavehicle.getVehicle().setTypeDesc(vehicleBO.getVehInfo().getVehicleTypeDesc());
			}
			
			if(vehicleBO.getVehOwners().getVehOwner() != null && vehicleBO.getVehOwners().getVehOwner().size() != 0){
				tavehicle.getVehicle().setOwnerName(nameUtil.getLastNameCapsFirstNameMiddleName(vehicleBO.getVehOwners().getVehOwner().get(0).getFirstName().toLowerCase(),vehicleBO.getVehOwners().getVehOwner().get(0).getLastName(),""));
			}
			
			if(vehicleBO.getVehInfo().getVehicleYear() != null){
				tavehicle.getVehicle().setYear(Integer.parseInt(vehicleBO.getVehInfo().getVehicleYear()));
			}
			System.out.println("Vehicle Year BO " + vehicleBO.getVehInfo().getVehicleYear() + " ta vehile year" + tavehicle.getVehicle().getYear());
			if(vehicleBO.getVehInfo().getVehicleColour() != null){
				tavehicle.getVehicle().setColour(vehicleBO.getVehInfo().getVehicleColour());
			}
			
			context.getFlowScope().put("taVehicle", tavehicle);
			
			return success();
			}
			 catch(FslWebServiceException_Exception fslEx)
			 {
				 addErrorMessage(context, "Norecordfound");
				 add(context);
				 return error();
			 }
			catch(Exception ex)
			{
				addErrorMessage(context,  "search.failure");
				ex.printStackTrace();
				return error();
			}
		}
	
	public Boolean taVehiclePlateNoExistsInROMS(String plateRegNo){
		//Checks if TRN Exists in ROMS
		try {
			boolean answer = getMaintenanceService().taVehiclePlateRegNoExists(plateRegNo);
			if(answer)
				return true;
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public void select(RequestContext context){
		System.err.println("select called");
		
		try {
			TAVehicleBean taVehicleBean = (TAVehicleBean)context.getFlowScope().get("taVehicles");
			System.err.println("TaVehicle: plate"+taVehicleBean.getSelectedTaVehicle().getVehicle().getPlateRegNo()+" id:"+taVehicleBean.getSelectedTaVehicle().getTaVehicleId() +"make "+ taVehicleBean.getSelectedTaVehicle().getVehicle().getMakeDescription());
			setPlateSearchVal(taVehicleBean.getSelectedTaVehicle().getVehicle().getPlateRegNo());
			context.getFlowScope().put("taVehicle", taVehicleBean.getSelectedTaVehicle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context){
		TAVehicleCriteriaBO criteria = (TAVehicleCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		
		TAVehicleBean taVehBean = (TAVehicleBean) context.getFlowScope().get("taVehicles");
		
		if(taVehBean.getTaVehicleList().size() > 0)
		{
			search(context);
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		
		try {
			//TAVehicleBO taVehicle = (TAVehicleBO)context.getFlowScope().get("taVehicle");
			//System.err.println("vehicle model" + taVehicle.getVehicle().getModel());
			
			//VehicleBO v = new VehicleBO();
			//v.set
			//taVehicle.setVehicle(taVehicle.getVehicle());
			TAVehicleBO taVehicle = new TAVehicleBO();
			taVehicle.setStatusId("ACT");
			taVehicle.setVehicle(new VehicleBO());
			context.getFlowScope().put("taVehicle",taVehicle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Event updatetaVehicle(RequestContext context) {
		
		System.err.println("Updating TA vehicles");
			
		try {
			TAVehicleBO taVehicle = (TAVehicleBO) context.getFlowScope().get("taVehicle");
			TAVehicleCriteriaBO criteria = (TAVehicleCriteriaBO) context
					.getFlowScope().get("criteria");
			TAVehicleBean taVehicleBean = (TAVehicleBean)context.getFlowScope().get("taVehicles");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			if (taVehicle == null)
				throw new Exception("Please enter valid details.");

			// get user details
			taVehicle.setCurrentUsername(this.getRomsLoggedInUser().getUsername());

			if (validateArgs(taVehicle, context)) {
				return error();
			} else {
				
				getMaintenanceService().updateTAVehicle(taVehicle);
				taVehicle.setAuditEntryBO(new AuditEntryBO());
				taVehicle.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				taVehicle.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				
				if(taVehicleBean.getTaVehicleList().size() > 1){
					System.out.println("If update");
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					
					System.out.println("Else update");
					//TAVehicleCriteriaBO taVehCrit = new TAVehicleCriteriaBO();
					//taVehCrit.setTaVehicleId(taVehicle.getTaVehicleId());
					//context.getFlowScope().put("criteria", taVehCrit);
					//search(context);
				}
				
				context.getFlowScope().put("taVehicle", taVehicle);
				
				context.getMessageContext().addMessage(
						
						new MessageBuilder().info().code("RecordUpdated")
								.arg("TA Vehicle ").build());

				// retrieve results from database again
				// search(context);
				return success();
			}

			
		}catch(InvalidFieldException ex){
			
			context.getMessageContext().addMessage(
					new MessageBuilder().error().defaultText(ex.getMessage()).build());
			return error();
		} catch (Exception e) {

			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("TA Vehicle ").build());
			return error();
		}

	}

	public Event savetaVehicle(RequestContext context) {
			System.out.println("Save TA triggered");
		try {
			TAVehicleBO taVehicle = (TAVehicleBO) context.getFlowScope().get("taVehicle");
			TAVehicleCriteriaBO criteria = (TAVehicleCriteriaBO) context
					.getFlowScope().get("criteria");
			TAVehicleBean taVehicleBean = (TAVehicleBean)context.getFlowScope().get("taVehicles");
			String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());
			
			System.err.println("TaVehicle" + taVehicle.getVehicle().getPlateRegNo() + "make " + taVehicle.getVehicle().getMakeDescription());
			
			if (taVehicle == null)
				throw new Exception("Please enter details.");

			// get user details
			taVehicle.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			taVehicle.setStatusId(Constants.Status.ACTIVE);
			if (validateArgs(taVehicle, context)) {
				return error();
			} 
			if(!StringUtils.isEmpty(taVehicle.getVehicle().getPlateRegNo())){
				searchPlate(context);
			}
			
				getMaintenanceService().saveTAVehicle(taVehicle);
				taVehicle.setAuditEntryBO(new AuditEntryBO());
				taVehicle.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				if(taVehicleBean.getTaVehicleList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}
				
				context.getFlowScope().put("taVehicle", taVehicle);
				context.getMessageContext().addMessage(

						new MessageBuilder().info().code("RecordSaved")
								.arg("TA Vehicle ").build());

				return success();
			
			
		}catch(InvalidFieldException ex){
			
			context.getMessageContext().addMessage(
					new MessageBuilder().error().defaultText(ex.getMessage()).build());
			return error();
		}
		catch (Exception e) {
			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordSavedErr")
							.arg("TA Vehicle ").build());
			return error();
		}

	}
	
	@SuppressWarnings("unchecked")
	public Boolean isRegionActive(){
		RequestContext context = RequestContextHolder.getRequestContext();
		TAVehicleBO taVehicle = (TAVehicleBO) context.getFlowScope().get("taVehicle");
		List<SelectItem> activeLocations = (ArrayList<SelectItem>)context.getFlowScope().get("officeLocationList");
		//System.err.println("Locations " + ObjectUtils.objectToString(activeLocations));
		//System.err.println("Loc Again " + activeLocations.get(0).getValue());
		for(int x = 0;x< activeLocations.size();x++){
			if(activeLocations.get(x).getValue().equals(taVehicle.getOfficeLocationCode())){
				System.err.println("Inside is if");
				return true;
			}
		}
		return false;
	}
	
	//Used to check if required fields are null
	//Returns message to indicate field is required if left null
	private Boolean validateArgs(TAVehicleBO taVehicle, RequestContext context) {

		Boolean error = false;
		MessageContext messages = context.getMessageContext();
		
		if (StringUtils.isBlank(taVehicle.getVehicle().getPlateRegNo())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Plate Registration Number").build());
			/*messages.addMessage(new MessageBuilder().error().defaultText("Please search using Plate Registration Number.").build());*/
			error = true;
		}
		
		if(!StringUtils.isBlank(getPlateSearchVal())) {
			if(getPlateSearchVal().length() > 1 && taVehicle.getVehicle().getPlateRegNo().length() > 1)
			{
				if(!getPlateSearchVal().equals(taVehicle.getVehicle().getPlateRegNo())){
					messages.addMessage(new MessageBuilder().error().defaultText("Plate Number does not match vehicle information.").build());
					error = true;
				}
			}
		}
		
		
		if (StringUtils.isBlank(taVehicle.getVehicle().getEngineNo())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Engine Number").build());
			error = true;
		}
		if (StringUtils.isBlank(taVehicle.getVehicle().getMakeDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Make Description").build());
			error = true;
		}
		if (StringUtils.isBlank(taVehicle.getVehicle().getModel())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Model").build());
			error = true;
		}
		if (StringUtils.isBlank(taVehicle.getVehicle().getChassisNo())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Chassis Number").build());
			error = true;
		}
		
		if (StringUtils.isBlank(taVehicle.getVehicle().getTypeDesc())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Type Description").build());
			error = true;
		}
		if (StringUtils.isBlank(taVehicle.getVehicle().getOwnerName())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Owner Name").build());
			error = true;
		}
		if (taVehicle.getVehicle().getYear()== null) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Year").build());
			error = true;
		}
		if (StringUtils.isBlank(taVehicle.getVehicle().getColour())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Colour").build());
			error = true;
		}
		
		if (StringUtils.isBlank(taVehicle.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status").build());
			error = true;
		} 
		if (StringUtils.isBlank(taVehicle.getOfficeLocationCode())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Region ").build());
			error = true;
		}else if(!isRegionActive()){
			String codeDescription = getOfficeLocationDesc(taVehicle.getOfficeLocationCode());
			messages.addMessage(new MessageBuilder().error()
					.code("RegionNotActive").arg(codeDescription).build());
			error = true;
		}
		
		
		
		/*if (!getPlateSearchVal().equals(taVehicle.getVehicle().getPlateRegNo())) {
			messages.addMessage(new MessageBuilder().error().defaultText("Plate Number does not match vehicle information").build());
			error = true;
		}*/

		return error;
	}

	public Event cancel(RequestContext context) {

		// context.getFlowScope().remove("taVehicles");
		/*
		 * context.getFlowScope().put("taVehicles", new
		 * TAVehicleBean());
		 */
		// taVehicles= new ArrayList<TAVehicleBO>();
		return success();
	}

	public Event clear(RequestContext context) {

		System.err.println(" cancel ");
		context.getViewScope().clear();
		return success();

	}

	public Maintenance getMaintenanceService() {
		return taVehicleMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance taVehicleMaintenanceService) {
		this.taVehicleMaintenanceService = taVehicleMaintenanceService;
	}

	public String getPlateSearchVal() {
		return plateSearchVal;
	}

	public void setPlateSearchVal(String plateSearchVal) {
		this.plateSearchVal = plateSearchVal;
	}
	
	
}
