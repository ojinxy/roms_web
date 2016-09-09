package org.fsl.roms.service.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.constants.Constants.StatusType;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.ArteryBean;
import org.fsl.roms.view.ObjectUtils;
import org.fsl.roms.view.StrategyTableBean;
import org.fsl.roms.view.WreckingCompanyBean;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.faces.model.OneSelectionTrackingListDataModel;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.ArteryBO;
import fsl.ta.toms.roms.search.criteria.impl.ArteryCriteriaBO;
import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.CourtBO;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.bo.ReasonBO;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.bo.RefCodeBO;

@Service
public class ArteryMaintenanceServiceAction extends BaseServiceAction {
	
	public List<ArteryBO> arteryList;
	
	@Autowired
	private Maintenance maintenanceService; 
	
	public ArteryMaintenanceServiceAction() {
		super();
		
	}
	
	public ArteryMaintenanceServiceAction(Maintenance _maintenanceService) {
		this.maintenanceService = _maintenanceService;
		
	}
	
	
	public Event search(RequestContext context) throws NoRecordFoundException, RequiredFieldMissingException {
						
		List<ArteryBO> arteries = null;
		
		try {

			ArteryCriteriaBO criteria = (ArteryCriteriaBO) context
					.getFlowScope().get("criteria");
			System.out.println("shortDesc "+criteria.getShortDescription() +"status "+ criteria.getStatusId() + "location "+ criteria.getLocationId() + "parishCode "+criteria.getParishCode());
				//System.out.println("Descript " + crite);	
			
			arteries = maintenanceService.lookupArtery(criteria);
			System.out.println(" size in search result" + arteries.size() );
				
			if (arteries.equals(null)) {
				
				/*context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordsfound")
								.build());*/
				//MessageBuilder messageBuilder = new MessageBuilder().error().code("Norecordsfound").;
				
				String error = this.buildErrorMessageWithParameters("Norecordsfound", null);
				FacesContext.getCurrentInstance().addMessage("msg",new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
				context.getFlowScope().put("arteries",
						new ArteryBean());
				return success();

			} /*else if (arteries.size() == 1) {
				
				context.getFlowScope().put("arteries", new ArteryBean(arteries));

				context.getFlowScope().put("mode", "update");

				//context.getFlowScope().put("arteries",
					//	new ArteryBean(arteries));
				return yes();
			}*/
			
			// set the entire list in datatable
			context.getFlowScope().put("arteries",new ArteryBean(arteries));
			
			

		}catch (NoRecordFoundException Nrex) {
			Nrex.printStackTrace();
			//context.getMessageContext().addMessage(new MessageBuilder().error().code("Norecordsfound").build());
			String error = this.buildErrorMessageWithParameters("Norecordsfound", null);
			FacesContext.getCurrentInstance().addMessage("msg",new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			context.getFlowScope().put("arteries",
					new ArteryBean());
			return error();
		} 
		
		catch (Exception e) {
			System.out.println("artery is broken");
			e.printStackTrace();
			//context.getMessageContext().addMessage(new MessageBuilder().error().code("search.failure").arg("Artery").build());
			String error = this.buildErrorMessageWithParameters("search.failure", new String[]{"Artery"});
			
			FacesContext.getCurrentInstance().addMessage("msg",new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error));
			context.getFlowScope().put("arteries",
					new ArteryBean().getArteryList());
			return error();
		}
			
		
		return success();
	}

		
	public Event saveartery(RequestContext context) {
			
				 
			ArteryBO artery = (ArteryBO) context.getFlowScope().get("artery");
			ArteryBean arteryBean = (ArteryBean)context.getFlowScope().get("arteries");
			ArteryCriteriaBO criteria = (ArteryCriteriaBO) context
					.getFlowScope().get("criteria");
			String usernameToFullName = maintenanceService.usernameToFullName(this.getRomsLoggedInUser().getUsername());
			System.out.println("Distance " + artery.getDistance() + "latitude " + artery.getStartlatitude() + "Name " + artery.getShortDescription());
			
			try {
				
				artery.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
				//artery.setCreatedtime()
				
				System.out.println("descript " + artery.getArteryDescription() + "location id " + artery.getLocationId());
				
				if (artery == null)
					throw new Exception("Please enter details.");
			

				if (validateArgs(artery, context)) {
					return error();
				} else {
					System.out.println("Message before save, artery descipt at this point " + artery.getShortDescription() + " " + artery.getLocationId());
					
					maintenanceService.saveArtery(artery);
					artery.setShortDescription(artery.getShortDescription());
					artery.setArteryDescription(artery.getArteryDescription());
					artery.setLocationDescription(getLocationDesc(artery.getLocationId()));
					artery.setParishDescription(getParishDesc(artery.getParishCode()));
					System.out.println("Parish Name " + artery.getParishDescription() + " artery " + artery.getShortDescription());
					artery.setAuditEntryBO(new AuditEntryBO());
					artery.getAuditEntryBO().setUpdateUsername(usernameToFullName);
					artery.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
					
					if(arteryBean.getArteryList().size() >= 1){
						context.getFlowScope().put("criteria", criteria);
						search(context);
					}else{
						/*ArteryCriteriaBO arteryCrit = new ArteryCriteriaBO();
						arteryCrit.setArteryId(artery.getArteryId());
						arteryCrit.setArteryDescription(artery.getArteryDescription());
						arteryCrit.setShortDescription(artery.getShortDescription());
						arteryCrit.setLocationId(artery.getLocationId());
						arteryCrit.setParishCode(artery.getParishCode());
						arteryCrit.setStatusId(artery.getStatusId());
						context.getFlowScope().put("criteria", arteryCrit);
						search(context);*/
					}
					
				}

				context.getMessageContext().addMessage(new MessageBuilder().info().code("RecordSaved")
								.arg("Artery").build());
				
				String info = this.buildErrorMessageWithParameters("RecordSaved", new String[]{"Artery"});
				FacesContext.getCurrentInstance().addMessage("msg",new FacesMessage(FacesMessage.SEVERITY_INFO, info, info));
				
				//context.getFlowScope().put("artery", new ArteryCriteriaBO());
				//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sample info message", "PrimeFaces rocks!"));
				return success();
			}catch (InvalidFieldException e) {
				e.printStackTrace();
				System.out.println("Inside catch");
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
			context.getFlowScope().put("criteria", new ArteryCriteriaBO());
			return error();
			}
			catch (Exception e) {
				e.printStackTrace();
					System.out.println("Inside catch");
				context.getMessageContext().addMessage(new MessageBuilder().error().code("RecordSavedErr")
								.arg("Artery").build());
				context.getFlowScope().put("criteria", new ArteryCriteriaBO());
				return error();
			}

		}

	
	
	public void select(RequestContext context){
		System.err.println("select called");
		//FacesContext msg = FacesContext.getCurrentInstance();
		try {
			ArteryBean arteryBean = (ArteryBean)context.getFlowScope().get("arteries");
			System.err.println("Artery: "+arteryBean.getSelectedArtery().getShortDescription()+" id:"+arteryBean.getSelectedArtery().getArteryId() +"points "+ arteryBean.getSelectedArtery().getPoints());
			context.getFlowScope().put("artery",arteryBean.getSelectedArtery());
			context.getMessageContext().addMessage(new MessageBuilder().warning().code("ArteryPrompt").build());
			getLocationList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void back(RequestContext context)
	{
		
		ArteryBO artery = (ArteryBO) context.getFlowScope().get("artery");
		
		System.err.println("back Artry parish: "+ artery.getParishCode());
		
		artery.setParishCode("");
		context.getFlowScope().put("artery", artery);
		
		ArteryCriteriaBO criteria = (ArteryCriteriaBO) context
				.getFlowScope().get("criteria");
		
		if(criteria.getLocationId()==null){
			resetLocation();
		}
		
		try
		{
			/*ArteryCriteriaBO criteria = (ArteryCriteriaBO) context
					.getFlowScope().get("criteria");*/
			
			/*Check if there where search results before searching again.*/
			ArteryBean arteryBean = (ArteryBean)context.getFlowScope().get("arteries");
			
			if(arteryBean.getArteryList().size() > 0)
			{
				search(context);
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void add(RequestContext context){
		System.err.println("add called");
		//FacesContext msg = FacesContext.getCurrentInstance();
		try {
			//WreckingCompanyBean wreckingCompanyBean = (WreckingCompanyBean)context.getFlowScope().get("wreckingCompanies");
			//System.err.println("Wrecking: "+wreckingCompanyBean.getSelectedWreckingCompany().getCompanyName()+" id:"+wreckingCompanyBean.getSelectedWreckingCompany().getWreckingCompanyId() +"trn "+ wreckingCompanyBean.getSelectedWreckingCompany().getTrnNbr());
			ArteryBO arteryBO = new ArteryBO();
			arteryBO.setStatusId("ACT");
			arteryBO.setParishCode("");
			context.getFlowScope().put("artery", arteryBO);
			getLocationList();
			context.getMessageContext().addMessage(new MessageBuilder().warning().code("ArteryPrompt").build());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*public void getLocationListFromSearch(){
		
		//Clear the artery parish code when searching
		
		RequestContext context = RequestContextHolder.getRequestContext();
		ArteryBO artery = (ArteryBO) context.getFlowScope().get("artery");
		
		System.err.println("Artry parish: "+ artery.getParishCode());
		
		artery.setParishCode("");
		context.getFlowScope().put("artery", artery);
		getLocationList();
	}*/
	
	public void getLocationList(){
		RequestContext context = RequestContextHolder.getRequestContext();
		System.err.println("get Location list called");
		String onUpdateAction = (String)context.getFlowScope().get("displayUpdateBtn");
		ArteryBO artery = (ArteryBO) context.getFlowScope().get("artery");
		ArteryCriteriaBO criteria = (ArteryCriteriaBO) context.getFlowScope().get("criteria");
		List<SelectItem> locationList = new ArrayList<SelectItem>();
		List<RefCodeBO> refcodes = null;
		HashMap<String, String> fields = new HashMap<String, String>();
		System.out.println("Parish code frm criteria artery " + criteria.getParishCode());
		System.err.println("Update Vaalue  "+ onUpdateAction);
		
		try {
			/*if(StringUtils.isEmpty(criteria.getParishCode()) || StringUtils.isEmpty(artery.getParishCode()))
			{
				refcodes = getRefInfo("roms_location");

				for (RefCodeBO codeBo : refcodes) {
					locationList.add(new SelectItem(codeBo.getCode(), codeBo
							.getDescription()));
				}
				
				context.getFlowScope().put("locationList", locationList);
				getLocations(context);
			}else{*/
				System.err.println("Get LocationList "+ artery.getParishCode());
				if(!StringUtils.isEmpty(artery.getParishCode())){
					fields.put("parish_code", artery.getParishCode());
					
					System.err.println("Parish code: "+artery.getParishCode());
					//refcodes = getRefInfo("roms_location", fields);
				}
				else{
					if(!StringUtils.isEmpty(criteria.getParishCode())){
						fields.put("parish_code", criteria.getParishCode());
					}		//refcodes = getRefInfo("roms_location");					
				}
				if(fields.isEmpty()){
					refcodes = getRefInfo("roms_location");
				}else{
					refcodes = getRefInfo("roms_location", fields);
				}
			
				
				if(refcodes!=null){
					for (RefCodeBO codeBo : refcodes) {
						locationList.add(new SelectItem(codeBo.getCode(), codeBo
								.getShortDescription()));
					}
				}
				context.getFlowScope().put("locationList", locationList);
			//}
		} catch (Exception e) {
			List<SelectItem> locationList2 = new ArrayList<SelectItem>();
			context.getFlowScope().put("locationList", locationList2);
			e.printStackTrace();
		}
	}
	
	public void getLocationListfrmSearchParish()
	{
		RequestContext context = RequestContextHolder.getRequestContext();
		System.err.println("reset Location list called");
		ArteryCriteriaBO criteria = (ArteryCriteriaBO) context.getFlowScope().get("criteria");
		
		List<SelectItem> locationList = new ArrayList<SelectItem>();
		List<RefCodeBO> refcodes = null;
		HashMap<String, String> fields = new HashMap<String, String>();
		System.out.println("Parish code frm criteria artery " + criteria.getParishCode());
		
		
		try 
		{
			if(StringUtils.isBlank(criteria.getParishCode()))
			{
				getLocations(context);
			}
			else
			{
				fields.put("parish_code", criteria.getParishCode());
				refcodes = getRefInfo("roms_location", fields);
			
				
				Collections.sort(refcodes, new Comparator<RefCodeBO>() {
					@Override
					public int compare(RefCodeBO result1, RefCodeBO result2) {
						return result1.getShortDescription().toLowerCase().compareTo(result2.getShortDescription().toLowerCase());
					}
				});
				
				if(refcodes!= null){
					for (RefCodeBO codeBo : refcodes) {
						locationList.add(new SelectItem(codeBo.getCode(), codeBo
								.getShortDescription()));
					}
				}
				
				context.getFlowScope().put("locationList", locationList);
			}
		} 
		catch (Exception e) 
		{
			List<SelectItem> locationList2 = new ArrayList<SelectItem>();
			context.getFlowScope().put("locationList", locationList2);
			e.printStackTrace();
		}
		
	}
	
	public void resetLocation(){
		
		RequestContext context = RequestContextHolder.getRequestContext();
		System.err.println("reset Location list called");
		getLocations(context);
	}
	
	
	public Event updateartery(RequestContext context) {
		System.err.println("Update fired");
		try {
			ArteryBO artery = (ArteryBO) context.getFlowScope().get("artery");
			ArteryBean arteryBean = (ArteryBean)context.getFlowScope().get("arteries");
			ArteryCriteriaBO criteria = (ArteryCriteriaBO) context
					.getFlowScope().get("criteria");
			String usernameToFullName = maintenanceService.usernameToFullName(this.getRomsLoggedInUser().getUsername());
			System.out.println("descript " + artery.getArteryDescription() + "location id " + artery.getLocationId());			
			if (artery == null)
				throw new Exception("Please enter valid details.");

			// get user details
			artery.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
			
			if (validateArgs(artery, context)) {
				return error();
			} 
			
			
			if(maintenanceService.arteryExists(artery.getArteryId())) 
			{
				System.err.println("Triggers if record exist");
				maintenanceService.updateArtery(artery);
				artery.setShortDescription(artery.getShortDescription());
				artery.setArteryDescription(artery.getArteryDescription());
				artery.setLocationDescription(getLocationDesc(artery.getLocationId()));
				artery.setParishDescription(getParishDesc(artery.getParishCode()));
				System.out.println("Parish Name " + artery.getParishDescription() + " artery " + artery.getShortDescription());
				artery.setAuditEntryBO(new AuditEntryBO());
				artery.getAuditEntryBO().setUpdateUsername(usernameToFullName);
				artery.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
				
				if(arteryBean.getArteryList().size() >= 1){
					context.getFlowScope().put("criteria", criteria);
					search(context);
				}else{
					/*ArteryCriteriaBO arteryCrit = new ArteryCriteriaBO();
					arteryCrit.setArteryId(artery.getArteryId());
					arteryCrit.setArteryDescription(artery.getArteryDescription());
					arteryCrit.setShortDescription(artery.getShortDescription());
					arteryCrit.setLocationId(artery.getLocationId());
					arteryCrit.setParishCode(artery.getParishCode());
					arteryCrit.setStatusId(artery.getStatusId());
					context.getFlowScope().put("criteria", arteryCrit);
					search(context);*/
				}
				
				
				/*context.getMessageContext().addMessage(

						new MessageBuilder().info().code("RecordUpdated")
								.arg("Artery").build());*/
				String error = this.buildErrorMessageWithParameters("RecordUpdated", new String[]{"Artery"});
				FacesContext.getCurrentInstance().addMessage("msg",new FacesMessage(FacesMessage.SEVERITY_INFO, error, error));
				

				// retrieve results from database again
				// search(context);
				return success();
			}
			else
			{
				System.err.println("record doesnt exist");
				context.getMessageContext().addMessage(

						new MessageBuilder().error().code("RecordUpdateErr")
								.arg("Artery").build());
				return error();
			}

			
		}catch (InvalidFieldException Inex){
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(Inex.getMessage()).build());
			Inex.printStackTrace();
			return error();
		}
		catch (Exception e) {

			e.printStackTrace();

			// Adds a message with code 'update.failure' from the
			// messages.properties file
			// found on the root of the classpath
			context.getMessageContext().addMessage(

					new MessageBuilder().error().code("RecordUpdateErr")
							.arg("Artery").build());
			return error();
		}

	}
	
	public ArteryBO editArtery(SelectEvent event){
		//RequestContext context = null;
		//ArteryCriteriaBO criteria = (ArteryCriteriaBO) context.getFlowScope().get("selectedArtery");
		//
		ArteryBO criteria = (ArteryBO)event.getObject();
		//context.getFlowScope().put("criteria", criteria);
		System.err.println("Row selected"+criteria.getShortDescription());
		return criteria;
	}
	
	public Event cancelartery(RequestContext context) {
		context.getFlowScope().put("criteria", new ArteryCriteriaBO());
		return success();
	}
	
	
	private Boolean validateArgs(ArteryBO artery, RequestContext context) {
		Boolean error = false;
		MessageContext messages = context.getMessageContext();
		System.err.println("distance " + artery.getDistance());
		if ((StringUtils.isBlank(artery.getShortDescription()))) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Name").build());
			error = true;
		}
		if (StringUtils.isBlank(artery.getArteryDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Description").build());
			error = true;
		} 
		if (StringUtils.isBlank(artery.getParishCode()) || artery.getParishCode() == "0") {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Parish").build());
			error = true;
		}
		
		if (artery.getLocationId() == null || (artery.getLocationId() == 0)) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Location").build());
			error = true;
		}
		
		if (artery.getDistance() == null || artery.getDistance() == 0.00) {
			
			messages.addMessage(new MessageBuilder().error().code("MapPoints").build());
			error = true;
		}

		return error;
	}

	public Maintenance get_maintenanceService() {
		return maintenanceService;
	}

	public void set_maintenanceService(Maintenance _maintenanceService) {
		this.maintenanceService = _maintenanceService;
	}
	
	public void clear(RequestContext context)
	{
		//context.getFlowScope().put("criteria", new ArteryBean());
		ArteryBO arteryBO = new ArteryBO();
		arteryBO.setStatusId("ACT");
		arteryBO.setParishCode("");
		//arteryBO.setLocationId("");
		context.getFlowScope().put("artery", arteryBO);
		context.getMessageContext().addMessage(new MessageBuilder().warning().code("ArteryPrompt").build());
	}
	
}
