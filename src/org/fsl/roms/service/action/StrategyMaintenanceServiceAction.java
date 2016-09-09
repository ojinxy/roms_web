package org.fsl.roms.service.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.fsl.application.ApplicationProperties;
import org.fsl.roms.constants.Constants;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.StrategyTableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.faces.model.OneSelectionTrackingListDataModel;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.ibm.websphere.models.config.security.Filter;

import fsl.ta.toms.roms.bo.AuditEntryBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.StrategyBO;
import fsl.ta.toms.roms.bo.StrategyRuleBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.search.criteria.impl.RefCodeCriteriaBO;
import fsl.ta.toms.roms.search.criteria.impl.StrategyCriteriaBO;
import fsl.ta.toms.roms.webservices.Maintenance;
import fsl.ta.toms.roms.webservices.ReferenceCode;

/**
 * 
 * @author bssintern
 *
 */

@Service
public class StrategyMaintenanceServiceAction extends BaseServiceAction {

	@Autowired
	private Maintenance strategyMaintenanceService;

	public StrategyMaintenanceServiceAction() {
		super();
	}  
	
	public StrategyMaintenanceServiceAction(
			Maintenance strategyMaintenanceService) {
		this.strategyMaintenanceService = strategyMaintenanceService;
	}
	
	/**
	 * This function searches based on Strategy criteria 
	 * @param context
	 * @return
	 */
	public Event search(RequestContext context) {
		System.out.println("Search fired");
		List<StrategyBO> strategies = null;
   
		//StrategyTableBean strategyTable = new StrategyTableBean();
		
		
		try {

			StrategyCriteriaBO criteria = (StrategyCriteriaBO) context
					.getFlowScope().get("criteria");

			// do the search
			
			strategies = getMaintenanceService().lookupStrategy(criteria);


			if (strategies == null) {
				context.getMessageContext().addMessage(
						new MessageBuilder().error().code("Norecordfound")
								.build());
				context.getFlowScope().put("strategies",
						new StrategyTableBean());
				return success();

			} else if (strategies.size() == 1) {

				context.getFlowScope().put("strategy", strategies.get(0));

				context.getFlowScope().put("mode", "update");
				
				context.getFlowScope().put("strategies",
						new StrategyTableBean(strategies));
				
				return yes();
			}

			// set the entire list in datatable
									
			context.getFlowScope().put("strategies",
					new StrategyTableBean(strategies));
			return success();
		} catch (NoRecordFoundException e) {
			context.getMessageContext()
			.addMessage(
					new MessageBuilder().error().code("Norecordfound")
							.build());
			context.getFlowScope().put("strategies",
					new StrategyTableBean());
			return error();
		}
		catch (Exception e) {
			e.printStackTrace();
			context.getMessageContext()
					.addMessage(
							new MessageBuilder().error().code("search.failure")
									.build());
			context.getFlowScope().put("strategies",
					new StrategyTableBean());
			return error();
		}
		
	}
	
	public List<String> completeStrategy(String query) {
		List<StrategyBO> strategies = null;
		List<String> results = new ArrayList<String>();
		
		try{
			
			StrategyCriteriaBO criteria = new StrategyCriteriaBO();
			
			criteria.setStrategyDescription(query);
			
			strategies = getMaintenanceService().lookupStrategy(criteria);
	
			for(StrategyBO strObj:strategies){
				if(strObj.getStrategyDescription().toLowerCase().trim().startsWith(query.toLowerCase())){
					results.add(strObj.getStrategyDescription());
				}
			}
			
		}catch (Exception e) {
			
		}
		
		
		return results;
	}
	
	public void select(RequestContext context){
		//System.err.println("select called");
		
		StrategyTableBean tableBean = (StrategyTableBean)context.getFlowScope().get("strategies");
						
		context.getFlowScope().put("strategy", tableBean.getSelectedStrategy());
		
		try {
			List<StrategyRuleBO>ruleList = getMaintenanceService().getStrategyRulesForStrategy(tableBean.getSelectedStrategy().getStrategyId());
			
			context.getFlowScope().put("strategyrules", ruleList);
		
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void reset(RequestContext context){
		context.getFlowScope().put("strategies",
				new StrategyTableBean());
		
		 context.getFlowScope().put("criteria", new StrategyCriteriaBO());
		
	}
	
	public void back(RequestContext context){
		StrategyCriteriaBO criteria = (StrategyCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("criteria",criteria);
		List<StrategyBO> strategies = null;
		strategies = (List<StrategyBO>) context.getFlowScope().get("strategies");
		if(strategies.size() > 0){
			search(context);
		}
		
	}
	
	public void add(RequestContext context){
		
		/*context.getFlowScope().put("popupmsg", "");
		
		context.getFlowScope().remove("popupmsg");*/
		
		context.getFlowScope().put("strategy", new StrategyBO());
		
		List<StrategyRuleBO>ruleList = new ArrayList<StrategyRuleBO>();
		
		String personTypeIDs[] = {Constants.PersonType.JP, Constants.PersonType.ITA_EXAMINER, Constants.PersonType.POLICE_OFFCER, Constants.PersonType.TA_STAFF};
		
		for(int idx = 0;idx < personTypeIDs.length; idx++){
			RefCodeBO refCodeBO = getPersonType(personTypeIDs[idx]);
			if(refCodeBO!=null){
				StrategyRuleBO ruleItem = new StrategyRuleBO();
				ruleItem.setPersonTypeId(refCodeBO.getCode());
				ruleItem.setPersonTypeDescription(refCodeBO.getDescription());
				if(ruleItem.getPersonTypeDescription().equals("TA Staff")){
					ruleItem.setMaximum(1);
					ruleItem.setMinimum(1);
				}else{
					ruleItem.setMaximum(0);
					ruleItem.setMinimum(0);
				}
				ruleList.add(ruleItem);
			}			
		}
		
		context.getFlowScope().put("strategyrules", ruleList);
		
	}
	
	private RefCodeBO getPersonType(String type){
		ReferenceCode refCodeService = new ReferenceCode();
		RefCodeCriteriaBO refCodeCriteria = new RefCodeCriteriaBO();
				
		refCodeCriteria.setTableName("ROMS_cd_person_type");
		
		HashMap<String,String> filter = new HashMap<String,String>();
	
		filter.put("person_type_id",type);
				
		refCodeCriteria.setFilter(filter);
		
		try {
			List<RefCodeBO> refRsltList = refCodeService.getReferenceCode(refCodeCriteria);
			
			return refRsltList.get(0);
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
		
		return null;
	}
	
	public String save(RequestContext context){
		StrategyTableBean strategyBean = (StrategyTableBean)context.getFlowScope().get("strategies");
		StrategyCriteriaBO criteria = (StrategyCriteriaBO) context
				.getFlowScope().get("criteria");
		context.getFlowScope().put("popupmsg", "");
		
		context.getFlowScope().remove("popupmsg");
		
		StrategyBO strategy = (StrategyBO)context.getFlowScope().get("strategy");
		List<StrategyRuleBO>ruleList = (List<StrategyRuleBO>) context.getFlowScope().get("strategyrules");
		String usernameToFullName = getMaintenanceService().usernameToFullName(this.getRomsLoggedInUser().getUsername());		
		org.primefaces.context.RequestContext testContext = org.primefaces.context.RequestContext.getCurrentInstance();
		
		strategy.setCurrentUsername(this.getRomsLoggedInUser().getUsername());
		
		try {
			if(validateArgs(strategy, context)){
				if(strategy.getStrategyId()==null){
					if(ruleList != null)
					{
						for(StrategyRuleBO srBO : ruleList){
							if(validateArgs(srBO, context))
							{
								if(srBO.getPersonTypeDescription().equals("TA Staff")){
									if(srBO.getMinimum().equals(0))
									{
										srBO.setMinimum(1);
									}
								}
								if(srBO.getMaximum() == 0)
								{
									srBO.setMaximum(9999);
								}
								continue;
							}
							else
								return "error";
							
						}
					}
					
					
					//Saving a new record
					getMaintenanceService().saveStrategy(strategy, ruleList);
					
					strategy.setStrategyDescription(strategy.getStrategyDescription());
					strategy.setStatusId(Constants.Status.ACTIVE);
					System.out.println("Strategy Name " + strategy.getStrategyDescription() + "stat id " + strategy.getStatusId());
					strategy.setAuditEntryBO(new AuditEntryBO());
					strategy.getAuditEntryBO().setUpdateUsername(usernameToFullName);
					strategy.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
					
					if(strategyBean.getStrategyList().size() >= 1){
						context.getFlowScope().put("criteria", criteria);
						search(context);
					}else{
						StrategyCriteriaBO strategyCrit = new StrategyCriteriaBO();
						strategyCrit.setStrategyId(strategy.getStrategyId());
						context.getFlowScope().put("criteria", strategyCrit);
						search(context);
					}
					
					context.getMessageContext().addMessage(
							new MessageBuilder().info().code("RecordSaved")
									.arg("Strategy ").build());
				}else{                               //Updating an existing record 
					getMaintenanceService().updateStrategy(strategy, ruleList);
					
					strategy.setStrategyDescription(strategy.getStrategyDescription());
					strategy.setStatusId(strategy.getStatusId());
					System.out.println("Strategy Name " + strategy.getStrategyDescription() + "update stat id " + strategy.getStatusId());
					strategy.setAuditEntryBO(new AuditEntryBO());
					strategy.getAuditEntryBO().setUpdateUsername(usernameToFullName);
					strategy.getAuditEntryBO().setUpdateDTime(Calendar.getInstance().getTime());
					
					if(strategyBean.getStrategyList().size() > 1){
						context.getFlowScope().put("criteria", criteria);
						search(context);
					}else{
						StrategyCriteriaBO strategyCrit = new StrategyCriteriaBO();
						strategyCrit.setStrategyId(strategy.getStrategyId());
						context.getFlowScope().put("criteria", strategyCrit);
						search(context);
					}
					
					context.getMessageContext().addMessage(
							new MessageBuilder().info().code("RecordUpdated")
									.arg("Strategy ").build());
				}
				
				
				
				testContext.addCallbackParam("saved", "saved");
				return "success";
			}
		} catch (ErrorSavingException e) {
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
			e.printStackTrace();
		} catch (InvalidFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.getMessageContext().addMessage(new MessageBuilder().error().defaultText(e.getMessage()).build());
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		testContext.addCallbackParam("validationFailed", context.getFlowScope().get("popupmsg"));
			
		
		return "error";
		
	}
	
	
	
	/**
	 * @author bssintern
	 * @param context
	 * @return
	 * 
	 */
	
	public Event getStrategyRule (RequestContext context){
		List<StrategyRuleBO> strategyrules = null;
		
		try{
			
			StrategyBO strategy = (StrategyBO) context.getFlowScope().get("strategy");
			
			strategyrules = getMaintenanceService().getStrategyRulesForStrategy(strategy.getStrategyId());
			
			context.getFlowScope().put("strategyrules",new OneSelectionTrackingListDataModel(strategyrules));
		
		}
		
		catch(Exception e){}
		
		return success();
	}
	
	public Event initAddMode(RequestContext context){
		List<StrategyRuleBO> strategyrules = new ArrayList<StrategyRuleBO>();
		
		try{
			StrategyRuleBO strategyrule = (StrategyRuleBO) context.getFlowScope().get("strategyrule");
			
			if (strategyrule == null)
				throw new Exception("Please enter details.");
			else
			{ 
				strategyrules.add(strategyrule);				
				context.getFlowScope().put("strategyrules",new OneSelectionTrackingListDataModel(strategyrules));
			}
		}
		catch(Exception e)
		{}
		
		return success(); 
	}
	
	
	//Used to check if required fields are null
	//Returns message to indicate field is required if left null
	private Boolean validateArgs(StrategyBO strategy, RequestContext context) {

		Boolean pass = true;
		MessageContext messages = context.getMessageContext();

		if (StringUtils.isBlank(strategy.getStrategyDescription())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Strategy Description").build());
			
			//context.getFlowScope().put("popupmsg", "Strategy Description is required.");
			
			pass = false;
		} 
		/*if (StringUtils.isBlank(strategy.getStatusId())) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Status ").build());
			
			//context.getFlowScope().put("popupmsg", "Satus is required.");
			pass = false;
		} */
		
		try {
			
			if(strategy.getStrategyDescription()!=null&&getMaintenanceService().strategyDescriptionExist(strategy.getStrategyDescription(), strategy.getStrategyId())){
				messages.addMessage(new MessageBuilder().error()
						.code("RecordExists").arg("Strategy ").build());				
				pass = false;
			}	
			
			
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*if (strategy.getStrategyId() == null) {
			messages.addMessage(new MessageBuilder().error()
					.code("RequiredFields.Maintenance").arg("Strategy ID").build());
			pass = false;

		} */
		return pass;
	}
/*
 * Validate Strategy Rules
 * Author: Ricardo
 */
	private boolean validateArgs(StrategyRuleBO srBO, RequestContext context) {
		MessageContext messages = context.getMessageContext();
		Boolean pass = true;
		if (srBO.getMinimum() > 9999) {
			messages.addMessage(new MessageBuilder().error().defaultText("Minimum value cannot exceed 9999").build());
			pass = false;
		} 
		if (srBO.getMaximum() > 9999) {
			messages.addMessage(new MessageBuilder().error().defaultText("Maximum value cannot exceed 9999").build());
			pass = false;
		} 
		if(srBO.getPersonTypeDescription().equals("TA Staff")){
			if((srBO.getMinimum() < 1) || (srBO.getMaximum() < 1)){
				messages.addMessage(new MessageBuilder().error().defaultText("At least one(1) TA Staff is required").build());
				pass = false;
			}
		}
		
		return pass;
	}

	public Maintenance getMaintenanceService() {
		return strategyMaintenanceService;
	}

	public void setMaintenanceService(
			Maintenance strategyMaintenanceService) {
		this.strategyMaintenanceService = strategyMaintenanceService;
	}
	
}
