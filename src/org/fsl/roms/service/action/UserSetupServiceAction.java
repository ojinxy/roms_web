package org.fsl.roms.service.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;


import org.apache.commons.lang.StringUtils;
import org.fsl.roms.businessobject.RegionBO;
import org.fsl.roms.util.DateUtils;
import org.fsl.roms.view.UserSetupView;
import org.primefaces.event.RowEditEvent;
import org.springframework.stereotype.Service;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import fsl.ta.toms.roms.bo.BIMSStaffViewBO;
import fsl.ta.toms.roms.bo.LMISUserViewBO;
import fsl.ta.toms.roms.bo.RefCodeBO;
import fsl.ta.toms.roms.bo.StaffUserMappingBO;
import fsl.ta.toms.roms.bo.TAStaffTypeBO;
import fsl.ta.toms.roms.exception.ErrorSavingException;
import fsl.ta.toms.roms.exception.InvalidFieldException;
import fsl.ta.toms.roms.exception.NoRecordFoundException;
import fsl.ta.toms.roms.exception.RequiredFieldMissingException;
import fsl.ta.toms.roms.exception.UserMappingException;
import fsl.ta.toms.roms.search.criteria.impl.StaffUserMappingCriteriaBO;

@Service
public class UserSetupServiceAction extends BaseServiceAction{

	public Event getAllList(RequestContext context) {
		getRegionList(context);
		getStaffTypeList(context);
		getBIMSStaffList(context);
		return success();
	}
	public Event getRegionList(RequestContext context) {
		HashMap<String, String> criteria = new HashMap<String, String>();

		List<RefCodeBO> listRegions = super.getRefInfo(
				"roms_lmis_ta_office_location_view", criteria);

		List<RegionBO> selectListRegions = new ArrayList<RegionBO>();

		RegionBO regionBo = new RegionBO();

		if (listRegions != null) {

			for (RefCodeBO refCode : listRegions) {

				regionBo.setId(refCode.getCode());
				regionBo.setDescription(refCode.getDescription());
				selectListRegions.add(regionBo);
				regionBo = new RegionBO();

			}
		}
		context.getFlowScope().put("regionList", selectListRegions);
		return success();
	}
	
	public Event getStaffTypeList(RequestContext context) {
		
		List<TAStaffTypeBO> staffTypeBOList = new ArrayList<TAStaffTypeBO>();
		try {
			staffTypeBOList = getStaffUserMappingService().lookupBIMSStaffType();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		context.getFlowScope().put("staffTypeList", staffTypeBOList);
		return success();
	}
	
public Event getBIMSStaffList(RequestContext context) {
		
		List<BIMSStaffViewBO> staffList = new ArrayList<BIMSStaffViewBO>();
		try {
			staffList = getStaffUserMappingService().lookupAllStaff();
		} catch (NoRecordFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		context.getFlowScope().put("staffList", staffList);
		return success();
	}
	
	public Event clear(RequestContext context){
		UserSetupView userSetupView = new UserSetupView();
		userSetupView.clear();
		
		List<StaffUserMappingBO> staffUserMappingBOs  = new ArrayList<StaffUserMappingBO>();
		context.getFlowScope().put("bimsUserList", staffUserMappingBOs);
		context.getFlowScope().put("userSetupView", userSetupView);
		return success();
	}
	
	public Event search(RequestContext context){
		UserSetupView userSetupView =  (UserSetupView)context.getFlowScope().get("userSetupView");
		
		List<StaffUserMappingBO> staffUserMappingBOs  = new ArrayList<StaffUserMappingBO>();
		//Clear previous list
		context.getFlowScope().put("bimsUserList", staffUserMappingBOs);
		StaffUserMappingCriteriaBO staffUserMappingCriteriaBO = new StaffUserMappingCriteriaBO();
		
		try {
			staffUserMappingCriteriaBO.setLoginUsername(userSetupView.getUserName());
			if(userSetupView.getStaffViewBO()!=null){
				staffUserMappingCriteriaBO.setStaffId(userSetupView.getStaffViewBO().getStaffId());
			}
			
			staffUserMappingCriteriaBO.setStaffType(userSetupView.getStaffType());
			
			staffUserMappingCriteriaBO.getSelectedRegions().clear();

			List<RegionBO> regions = (List<RegionBO>)context.getFlowScope().get("taOfficeRegion");
			
			
			if(regions != null && regions.size() > 0){
				for(RegionBO region : regions){
					staffUserMappingCriteriaBO.getSelectedRegions().add(region.getId());
				}
			}
			
			staffUserMappingBOs = getStaffUserMappingService().lookupTAStaff(staffUserMappingCriteriaBO);
			if(staffUserMappingBOs==null || staffUserMappingBOs.size()==0){
				addErrorMessage(context,  "Norecordsfound");
				return error();
			}
			
			context.getFlowScope().put("bimsUserList", staffUserMappingBOs);
		} catch (NoRecordFoundException e) {
			addErrorMessage(context,  "Norecordsfound");
			e.printStackTrace();
			return error();
		}
		
		return success();
	}
	
	public List<String> allUserNameAutoComplete(String term) {

		
		List<String> returnList = new ArrayList<String>();

		List<LMISUserViewBO> userList = new ArrayList<LMISUserViewBO>();

		try {
			userList = getStaffUserMappingService().lookupUsersByCriteria(term);

			for (LMISUserViewBO user : userList)
				returnList.add(user.getUsername());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	public List<BIMSStaffViewBO> userSetupTaStaffAutoComplete(String term)
	{
		
		RequestContext context = RequestContextHolder.getRequestContext();
	
		List<BIMSStaffViewBO> taStaffList = (List<BIMSStaffViewBO>)context.getFlowScope().get("staffList");
	
		
		List<BIMSStaffViewBO> returnList = new ArrayList<BIMSStaffViewBO>();
		
		
		 for(BIMSStaffViewBO staff : taStaffList)
		 {
			 if(staff.getFullName().trim().toLowerCase().contains(term.toLowerCase()))
			 {
				 returnList.add(staff);
			 }
		 }
			
		 
		 

		 return returnList;
	}
	
	public List<String> unmappedUserNameAutoComplete(String term) {

		RequestContext context = RequestContextHolder.getRequestContext();
		UserSetupView userSetupView =  (UserSetupView)context.getFlowScope().get("userSetupView");
		List<String> returnList = new ArrayList<String>();

		List<LMISUserViewBO> userList = new ArrayList<LMISUserViewBO>();

		try {
			userList = getStaffUserMappingService().lookupAllUnmappedUsersByCriteriaExceptSpecificUser(term, userSetupView.getCurrentStaffId());
			for (LMISUserViewBO user : userList)
				returnList.add(user.getUsername());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnList;
	}
	
	public void initRowEdit(RowEditEvent event){
		RequestContext context = RequestContextHolder.getRequestContext();
		UserSetupView userSetupView =  (UserSetupView)context.getFlowScope().get("userSetupView");
		
		StaffUserMappingBO staffUserMappingBO = (StaffUserMappingBO) event.getObject();
		
		userSetupView.setCurrentStaffId(staffUserMappingBO.getStaffId());
		userSetupView.setCurrentUserName(staffUserMappingBO.getStaffUsername());
		userSetupView.setTableError(false);
		
		context.getFlowScope().put("userSetupView", userSetupView);
	}
	
	
	public void rowEdit(RowEditEvent event){
		RequestContext context = RequestContextHolder.getRequestContext();
		UserSetupView userSetupView =  (UserSetupView)context.getFlowScope().get("userSetupView");
		userSetupView.setTableError(false);
		
		StaffUserMappingBO staffUserMappingBO = (StaffUserMappingBO) event.getObject();
				
		if(StringUtils.isBlank(staffUserMappingBO.getStaffUsername())){
			//addErrorMessageWithParameter(context,  "RequiredFields", staffUserMappingBO.getFullName() + "User Name");
			addErrorMessageWithParameter(context,  "RequiredFields", "Login User Name");
			userSetupView.setTableError(true);
			context.getFlowScope().put("userSetupView", userSetupView);
			return;
		}
		
		
		try {
			if(!getStaffUserMappingService().isValidUsernameTAStaffRegionMapping(staffUserMappingBO.getStaffId(), staffUserMappingBO.getStaffUsername())){
				addErrorMessageText(context,  "Login User Name Region is different from Staff Region.");
				userSetupView.setTableError(true);
				return;
			}
			
			if(!staffUserMappingBO.getStaffUsername().equalsIgnoreCase(userSetupView.getCurrentUserName())){
								
				staffUserMappingBO.setCurrentUsername(getRomsLoggedInUser().getUsername());
				getStaffUserMappingService().saveOrUpdateStaffUserMapping(staffUserMappingBO);
				
				//Updated details to display in data table
				staffUserMappingBO.getAuditEntryBO().setUpdateUsername(getRomsLoggedInUser().getUsername());
				Calendar calendar = Calendar.getInstance();
				Date currentDate = calendar.getTime();
				try {
					staffUserMappingBO.getAuditEntryBO().setUpdateDTime(currentDate);
					
					UserSetupView userSetupViewX =  (UserSetupView)context.getFlowScope().get("userSetupView");
					if(userSetupView.getSelectedRegions().size() >= 1){
						context.getFlowScope().put("userSetupView", userSetupViewX);
						search(context);
					}else{
						UserSetupView userSetupCrit = new UserSetupView();
						userSetupCrit.setCurrentStaffId(userSetupView.getCurrentStaffId());
						context.getFlowScope().put("userSetupView", userSetupCrit);
						search(context);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (ErrorSavingException e) {
			e.printStackTrace();
			userSetupView.setTableError(true);
			addErrorMessage(context,  "search.failure");
		} catch (InvalidFieldException e) {
			e.printStackTrace();
			userSetupView.setTableError(true);
			if (StringUtils.isEmpty((e.getMessage())))
				addErrorMessage(context,  "search.failure");
			else
				addErrorMessageText(context,  e.getMessage());
		} catch (RequiredFieldMissingException e) {
			// TODO Auto-generated catch block
			userSetupView.setTableError(true);
			e.printStackTrace();
		} catch (UserMappingException e) {
			userSetupView.setTableError(true);
			addErrorMessage(context,  "search.failure");
			e.printStackTrace();
		}
		finally{
			context.getFlowScope().put("userSetupView", userSetupView);
		}
		
	}
	
	public void cancelRowEdit(RowEditEvent event){
		RequestContext context = RequestContextHolder.getRequestContext();
		UserSetupView userSetupView =  (UserSetupView)context.getFlowScope().get("userSetupView");
		
		
		StaffUserMappingBO staffUserMappingBO = (StaffUserMappingBO) event.getObject();
		
		staffUserMappingBO.setStaffUsername(userSetupView.getCurrentUserName());
	}
	
	
}
